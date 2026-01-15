package com.theendercore.united_armory.entity

import com.theendercore.united_armory.init.UADataAttachments.THROWN_ANCHOR
import com.theendercore.united_armory.init.UAEntityTypes
import com.theendercore.united_armory.init.UAItems
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ItemSupplier
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

open class UnnamesAnchorProjectile : Projectile, ItemSupplier {
    constructor(type: EntityType<out UnnamesAnchorProjectile>, level: Level) : super(type, level)

    constructor(
        entityType: EntityType<out UnnamesAnchorProjectile>,
        owner: LivingEntity, level: Level, dir: Vec3, stack: ItemStack,
    ) : this(entityType, level) {
        this.owner = owner
        var offset = 1f
        weapon = stack
        moveTo(
            owner.x + dir.x * offset,
            owner.eyePosition.y + (dir.y * offset) - (type.dimensions.height / 2),
            owner.z + dir.z * offset,
            owner.yRot, owner.xRot,
        )
        reapplyPosition()
        assignDirectionalMovement(dir, 2.0)
    }

    constructor(owner: LivingEntity, level: Level, dir: Vec3, stack: ItemStack) : this(
        UAEntityTypes.UNNAMES_ANCHOR, owner, level, dir, stack
    )

    open fun defaultWeapon(): ItemStack = UAItems.UNNAMED_ANCHOR.defaultInstance
    var weapon: ItemStack
        get() = entityData.get(DATA_ITEM_STACK)
        set(value) {
            entityData.set(DATA_ITEM_STACK, value)
        }

    override fun getWeaponItem(): ItemStack = weapon
    override fun getItem(): ItemStack = weapon

    // Default
    open var accelerationPower = 0.025
    open var inertia = 0.94
    open var liquidInertia = inertia

    // Custom
    open var hasHit = false
    open var maxDistance = 20

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder.define(DATA_ITEM_STACK, defaultWeapon())
    }


    override fun tick() {
        val own = owner
        if (own == null || own.isRemoved) {
            discard()
            return
        }
        super.tick()

        if (!hasHit && position().distanceTo(ownerPos()) < maxDistance) {
            move()
        }

        // To owner particles
        val amount = ownerPos().distanceTo(position()) * 3
        repeat(amount.toInt()) {
            val pos2 = ownerPos().lerp(position(), it / amount)
            level().addParticle(ParticleTypes.ELECTRIC_SPARK, pos2.x, pos2.y, pos2.z, 0.0, 0.0, 0.0)
        }

        // Center Pos particles
        val vec = position().add(0.0, type.height / 2.0, 0.0)
        level().addParticle(ParticleTypes.OMINOUS_SPAWNING, vec.x, vec.y, vec.z, 0.0, 0.0, 0.0)
    }

    fun ownerPos(): Vec3 {
        val own = owner ?: error("Anchor owner is null at position : [${position()}]")
        return own.position().add(0.0, own.type.dimensions.height / 2.0, 0.0)
    }

    @Suppress("DEPRECATION")
    fun move() {
        if (level().isClientSide || level().hasChunkAt(blockPosition())) {
            val hitResult = ProjectileUtil.getHitResultOnMoveVector(this, ::canHitEntity)
            if (hitResult.type != HitResult.Type.MISS) {
                hitTargetOrDeflectSelf(hitResult)
            }

            checkInsideBlocks()
            val dMove = deltaMovement
            val modX = x + dMove.x
            val modY = y + dMove.y
            val modZ = z + dMove.z
            ProjectileUtil.rotateTowardsMovement(this, 0.2f)
            var mInertia = inertia
            if (isInWater) {
                val scale = 0.25f
                repeat(3) {
                    level().addParticle(
                        ParticleTypes.BUBBLE,
                        modX - dMove.x * scale, modY - dMove.y * scale, modZ - dMove.z * scale,
                        dMove.x, dMove.y, dMove.z
                    )
                }
                mInertia = liquidInertia
            }

            deltaMovement = dMove.add(dMove.normalize().scale(accelerationPower)).scale(mInertia)
            setPos(modX, modY, modZ)
        }
    }

    open fun findHitEntity(pos: Vec3, nextPos: Vec3): EntityHitResult? = ProjectileUtil.getEntityHitResult(
        level(), this, pos, nextPos, boundingBox.expandTowards(deltaMovement).inflate(1.0), ::canHitEntity
    )

    open fun assignDirectionalMovement(dir: Vec3, scale: Double) {
        deltaMovement = dir.normalize().scale(scale)
        hasImpulse = true
    }

    override fun getAddEntityPacket(entity: ServerEntity): Packet<ClientGamePacketListener> {
        val ownerId = owner?.id ?: 0
        val pos = entity.positionBase
        return ClientboundAddEntityPacket(
            id, getUUID(),
            pos.x(), pos.y(), pos.z(),
            entity.lastSentXRot, entity.lastSentYRot,
            type, ownerId, entity.lastSentMovement,
            0.0
        )
    }

    override fun recreateFromPacket(packet: ClientboundAddEntityPacket) {
        super.recreateFromPacket(packet)
        deltaMovement = Vec3(packet.xa, packet.ya, packet.za)
        if (owner == null) discard()
    }

    override fun handleEntityEvent(b: Byte) {
        super.handleEntityEvent(b)
        when (b) {
            3.toByte() -> {
                val scale = 0.08
                repeat(8) {
                    level().addParticle(
                        ItemParticleOption(ParticleTypes.ITEM, item),
                        x, y, z,
                        (random.nextFloat() - 0.5) * scale,
                        (random.nextFloat() - 0.5) * scale,
                        (random.nextFloat() - 0.5) * scale
                    )
                    level().playSound(null, x, y, z, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS)
                }
            }
        }
    }

    override fun onHitEntity(result: EntityHitResult) {
        super.onHitEntity(result)
        val vec3 = result.getLocation().subtract(x, y, z)
//        deltaMovement = vec3
//        val itemStack = weaponItem
        if (level() is ServerLevel) {
//            hitBlockEnchantmentEffects(serverLevel, result, itemStack)
        }
        val target = result.entity
        if (target is LivingEntity) {
            target.addEffect(MobEffectInstance(MobEffects.LEVITATION, 4 * 20))
        }
        if (target == owner) {
            discard()
        }

        val vec32 = vec3.normalize().scale(0.05)
        setPosRaw(x - vec32.x, y - vec32.y, z - vec32.z)
//        playSound(getHitGroundSoundEvent(), 1.0f, 1.2f / (random.nextFloat() * 0.2f + 0.9f))
        hasHit = true
        deltaMovement = Vec3.ZERO

        level().broadcastEntityEvent(this, 3.toByte())
    }

    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
        val vec3 = result.getLocation().subtract(x, y, z)
        deltaMovement = vec3
        val itemStack = weaponItem
        if (level() is ServerLevel) {
//            hitBlockEnchantmentEffects(serverLevel, result, itemStack)
        }

        val vec32 = vec3.normalize().scale(0.05)
        setPosRaw(x - vec32.x, y - vec32.y, z - vec32.z)
//        playSound(getHitGroundSoundEvent(), 1.0f, 1.2f / (random.nextFloat() * 0.2f + 0.9f))
        hasHit = true
        deltaMovement = Vec3.ZERO

        level().broadcastEntityEvent(this, 3.toByte())
    }

    @Suppress("UnstableApiUsage")
    override fun remove(removalReason: RemovalReason) {
        super.remove(removalReason)
        owner?.removeAttached(THROWN_ANCHOR)
    }

    override fun canUsePortal(bl: Boolean): Boolean = false

    override fun addAdditionalSaveData(nbt: CompoundTag) {
        super.addAdditionalSaveData(nbt)
        nbt.put(WEAPON, weapon.save(registryAccess()))
        nbt.putBoolean(HAS_HIT, hasHit)
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        super.readAdditionalSaveData(nbt)
        weapon = ItemStack.parse(registryAccess(), nbt.getCompound(WEAPON)).orElse(defaultWeapon())
        hasHit = nbt.getBoolean(HAS_HIT)
    }

    companion object {
        const val WEAPON = "weapon"
        const val HAS_HIT = "has_hit"

        val DATA_ITEM_STACK: EntityDataAccessor<ItemStack> =
            SynchedEntityData.defineId(UnnamesAnchorProjectile::class.java, EntityDataSerializers.ITEM_STACK)
    }
}