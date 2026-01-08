package com.theendercore.united_armory.entity

import com.theendercore.united_armory.init.UAEntityTypes
import com.theendercore.united_armory.init.UAItems
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile
import net.minecraft.world.entity.projectile.ItemSupplier
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.Vec3

open class UnnamesAnchorProjectile : AbstractHurtingProjectile, ItemSupplier {
    constructor(type: EntityType<out UnnamesAnchorProjectile>, level: Level) : super(type, level)

    constructor(
        entityType: EntityType<out UnnamesAnchorProjectile>, owner: LivingEntity, dir: Vec3, level: Level,
    ) : super(entityType, owner, dir, level)

    constructor(owner: LivingEntity, dir: Vec3, level: Level) : this(UAEntityTypes.UNNAMES_ANCHOR, owner, dir, level)

    open fun defaultWeapon(): ItemStack = UAItems.UNNAMED_ANCHOR.defaultInstance
    var weapon: ItemStack
        get() = entityData.get(DATA_ITEM_STACK)
        set(value) {
            entityData.set(DATA_ITEM_STACK, value)
        }

    override fun getWeaponItem(): ItemStack = weapon
    override fun getItem(): ItemStack = weapon

    var flyTime = 3 * 20
    var inGround = false

    //    open var accelerationPower = 0.1
    open var inertia = 0.94
    open var liquidInertia = inertia

    override fun shouldBurn(): Boolean = false
    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(DATA_ITEM_STACK, defaultWeapon())
    }


    override fun tick() {
        if (flyTime > 0) {
            super.tick()
//            if (!inGround) {
//                deltaMovement = calculateViewVector(xRot, yRot).normalize().scale(0.1)
//                move(MoverType.SELF, deltaMovement)
//            }
//            checkInsideBlocks()
            flyTime--
        } else {
            level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0.0, 0.0, 0.0)
        }


    }

    fun shouldFall(): Boolean = inGround && level().noCollision(AABB(position(), position()).inflate(0.06))
    fun startFalling() {
        inGround = false
        val vec3 = deltaMovement
        deltaMovement = vec3.multiply(random.nextFloat() * 0.2, random.nextFloat() * 0.2, random.nextFloat() * 0.2)
    }

    fun isNoPhysics() = true
    fun getPierceLevel(): Byte = 0
    open fun findHitEntity(pos: Vec3, nextPos: Vec3): EntityHitResult? = ProjectileUtil.getEntityHitResult(
        level(), this, pos, nextPos, boundingBox.expandTowards(deltaMovement).inflate(1.0), ::canHitEntity
    )

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

    override fun onHitBlock(result: BlockHitResult) {
//        lastState = level().getBlockState(result.blockPos)
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
        inGround = true

        level().broadcastEntityEvent(this, 3.toByte())
    }


    override fun addAdditionalSaveData(nbt: CompoundTag) {
        super.addAdditionalSaveData(nbt)
        nbt.put(WEAPON, weapon.save(registryAccess()))
        nbt.putInt(FLY_TIME, flyTime)
        nbt.putBoolean(IN_GROUND, inGround)
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        super.readAdditionalSaveData(nbt)
        weapon = ItemStack.parse(registryAccess(), nbt.getCompound(WEAPON)).orElse(defaultWeapon())
        flyTime = nbt.getInt(FLY_TIME)
        inGround = nbt.getBoolean(IN_GROUND)
    }

    companion object {
        const val WEAPON = "weapon"
        const val FLY_TIME = "fly_time"
        const val IN_GROUND = "in_ground"

        val DATA_ITEM_STACK: EntityDataAccessor<ItemStack> =
            SynchedEntityData.defineId(UnnamesAnchorProjectile::class.java, EntityDataSerializers.ITEM_STACK)
    }
}