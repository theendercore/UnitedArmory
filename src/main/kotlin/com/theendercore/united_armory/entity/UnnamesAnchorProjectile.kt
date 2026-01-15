package com.theendercore.united_armory.entity

import com.theendercore.united_armory.init.UADataAttachments.THROWN_ANCHOR
import com.theendercore.united_armory.init.UAEntityTypes
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.server.level.ServerEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.StringRepresentable
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

open class UnnamesAnchorProjectile : WeaponProjectile {
    constructor(type: EntityType<out UnnamesAnchorProjectile>, level: Level) : super(type, level)

    constructor(
        entityType: EntityType<out UnnamesAnchorProjectile>,
        owner: LivingEntity, level: Level, dir: Vec3, stack: ItemStack,
    ) : super(entityType, owner, level, stack) {
        var offset = 1f
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

    // Default
    open var accelerationPower = 0.025
    open var inertia = 0.94
    open var liquidInertia = inertia

    // Custom
    open var state = AnchorState.SHOOTING
    open var maxDistance = 20

    override fun tick() {
        val own = owner
        if (own == null || own.isRemoved) {
            discard()
            return
        }
        super.tick()
        if (state.shouldMove()) {
            if (state != AnchorState.RETRACTING && position().distanceTo(ownerPos()) >= maxDistance) {
                makeRetract()
            } else {
                if (ownerPos().subtract(position()).length() <= 0.1) {
                    discard()
                }
                move()
            }
            modifyMoveDelta()
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

    @Suppress("DEPRECATION")
    private fun modifyMoveDelta() {
        if (level().isClientSide || level().hasChunkAt(blockPosition())) {
            var delta = deltaMovement
            var mInertia = inertia
            if (isInWater) {
                val modX = x + delta.x
                val modY = y + delta.y
                val modZ = z + delta.z
                val scale = 0.25f
                repeat(3) {
                    level().addParticle(
                        ParticleTypes.BUBBLE,
                        modX - delta.x * scale, modY - delta.y * scale, modZ - delta.z * scale,
                        delta.x, delta.y, delta.z
                    )
                }
                mInertia = liquidInertia
            }

            when (state) {
                AnchorState.SHOOTING -> {
                    delta = delta.add(delta.normalize().scale(accelerationPower)).scale(mInertia)
                }

                AnchorState.RETRACTING -> {
                    delta = ownerPos().subtract(position()).normalize().scale(inertia)
                }

                AnchorState.HOLDING -> Unit
            }

            deltaMovement = delta
        }
        hasImpulse = true
    }

    fun makeRetract() {
        state = AnchorState.RETRACTING
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
            setPos(modX, modY, modZ)
        }
    }

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
        val target = result.entity
        if (target == owner) {
            discard()
            return
        }
        if (state == AnchorState.RETRACTING) {
            return
        }
        super.onHitEntity(result)
        val vec3 = result.getLocation().subtract(x, y, z)

        val damageSource = damageSources().mobProjectile(this, owner as? LivingEntity)
        target.hurt(damageSource, 5f)

        if (level() is ServerLevel) {
            EnchantmentHelper.doPostAttackEffectsWithItemSource(
                level() as ServerLevel,
                target,
                damageSource,
                weaponItem
            )
        }

        val vec32 = vec3.normalize().scale(0.05)
        setPosRaw(x - vec32.x, y - vec32.y, z - vec32.z)
//        playSound(getHitSoundEvent(), 1.0f, 1.2f / (random.nextFloat() * 0.2f + 0.9f))
        makeRetract()

        level().broadcastEntityEvent(this, 3.toByte())
    }

    override fun onHitBlock(result: BlockHitResult) {
        if (state == AnchorState.RETRACTING) return
        super.onHitBlock(result)
        val vec3 = result.getLocation().subtract(x, y, z)
        deltaMovement = vec3
        if (level() is ServerLevel) {
            hitBlockEnchantmentEffects(level() as ServerLevel, result, weaponItem)
        }

        val vec32 = vec3.normalize().scale(0.05)
        setPosRaw(x - vec32.x, y - vec32.y, z - vec32.z)
//        playSound(getHitGroundSoundEvent(), 1.0f, 1.2f / (random.nextFloat() * 0.2f + 0.9f))
        makeRetract()

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
        nbt.putString(ANCHOR_STATE, state.getSerializedName())
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        super.readAdditionalSaveData(nbt)
        state = AnchorState.valueOf(nbt.getString(ANCHOR_STATE).uppercase())
    }

    enum class AnchorState : StringRepresentable {
        SHOOTING,
        RETRACTING,
        HOLDING;

        override fun getSerializedName(): String = name.lowercase()

        fun shouldMove() = this != HOLDING
    }

    companion object {
        const val ANCHOR_STATE = "anchor_state"
    }
}