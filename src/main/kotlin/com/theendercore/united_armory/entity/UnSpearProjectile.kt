package com.theendercore.united_armory.entity

import com.theendercore.united_armory.init.UAEntityTypes
import com.theendercore.united_armory.init.UAItems
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.entity.projectile.ItemSupplier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.Vec3

open class UnSpearProjectile : AbstractArrow, ItemSupplier {
    constructor(entityType: EntityType<out UnSpearProjectile>, level: Level) : super(entityType, level)
    constructor(level: Level, owner: LivingEntity, weapon: ItemStack) : super(UAEntityTypes.UN_SPEAR, level) {
        setOwner(owner)
        this.weapon = weapon.copy()
        isNoGravity = true
    }

    override fun getDefaultPickupItem(): ItemStack = getItem()
    override fun tryPickup(player: Player?): Boolean = player == owner && (isReturning || isReturning)
    override fun playerTouch(player: Player) {
        if (!level().isClientSide && (inGround || isReturning) && tryPickup(player)) {
            heldEntity?.addEffect(MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1))
            super.playerTouch(player)
        }
    }

    override fun getWeaponItem(): ItemStack = weapon
    override fun getItem(): ItemStack = weapon
    var weapon: ItemStack
        get() = entityData.get(WEAPON_DATA)
        set(value) {
            entityData.set(WEAPON_DATA, value)
        }
    var isReturning: Boolean
        get() = entityData.get(RETURNING_DATA)
        set(value) {
            entityData.set(RETURNING_DATA, value)
        }

    var heldEntity: LivingEntity? = null

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(WEAPON_DATA, UAItems.UNNAMED_SPEAR.defaultInstance)
        builder.define(RETURNING_DATA, false)
    }

    override fun setYRot(f: Float) {
        if (!isReturning) super.setYRot(f)
    }

    override fun setXRot(f: Float) {
        if (!isReturning) super.setXRot(f)
    }

    override fun tick() {
        val own = owner
        if (own == null || own.isRemoved) {
            blowTheFuckUp()
            return
        }

        if (position().distanceTo(ownerPos()) >= 18) {
            blowTheFuckUp()
        }

        if (inGroundTime > 1) {
            blowTheFuckUp()
        }

        if (isReturning) {
            noPhysics = true
            deltaMovement = ownerPos().subtract(position()).normalize().scale(0.85)
            hasImpulse = true
            heldEntity?.let { he ->
                if (he.isAlive) {
                    he.deltaMovement = deltaMovement
                    he.hasImpulse = true
                    he.resetFallDistance()
                } else {
                    heldEntity = null
                }
            }
        }

        super.tick()
    }

    fun ownerPos(): Vec3 {
        val own = owner ?: error("Anchor owner is null at position : [${position()}]")
        return own.position().add(0.0, own.type.dimensions.height / 2.0, 0.0)
    }


    override fun onHitEntity(result: EntityHitResult) {
        val entity = result.entity
        if (entity == owner) {
            blowTheFuckUp()
            return
        }
        var damageAmount = 8.0f
        val damageSource = damageSources().trident(this, (owner ?: this))
        if (level() is ServerLevel) {
            damageAmount = EnchantmentHelper.modifyDamage(
                level() as ServerLevel,
                getWeaponItem(),
                entity,
                damageSource,
                damageAmount
            )
        }

        if (entity.hurt(damageSource, damageAmount)) {
            if (level() is ServerLevel) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(
                    level() as ServerLevel,
                    entity,
                    damageSource,
                    getWeaponItem()
                )
            }

            if (entity is LivingEntity) {
                doKnockback(entity, damageSource)
                doPostHurtEffects(entity)
                if (!entity.isRemoved) {
                    heldEntity = entity
                    isReturning = true
                }
            }
        } else {
            blowTheFuckUp()
        }

        deltaMovement = deltaMovement.multiply(-0.01, -0.1, -0.01)
        playSound(SoundEvents.TRIDENT_HIT, 1.0f, 1.0f)
    }

    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
        setSoundEvent(hitGroundSoundEvent)
    }

    private fun blowTheFuckUp() {
        if (!level().isClientSide) {
            level().broadcastEntityEvent(this, 16)
            discard()
        }
    }

    override fun handleEntityEvent(b: Byte) {
        super.handleEntityEvent(b)
        if (b == 16.toByte()) {
            val scale = 2.0
            repeat(100) {
                level().addParticle(
                    ParticleTypes.TRIAL_OMEN,
                    x, y, z,
                    (random.nextFloat() - 0.5) * scale,
                    (random.nextFloat() - 0.5) * scale,
                    (random.nextFloat() - 0.5) * scale
                )
            }
        }
    }

    companion object {
        val WEAPON_DATA: EntityDataAccessor<ItemStack> =
            SynchedEntityData.defineId(UnSpearProjectile::class.java, EntityDataSerializers.ITEM_STACK)
        val RETURNING_DATA: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(UnSpearProjectile::class.java, EntityDataSerializers.BOOLEAN)
    }
}

