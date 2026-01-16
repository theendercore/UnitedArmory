package com.theendercore.united_armory.entity

import com.theendercore.united_armory.data.UAEnchantments
import com.theendercore.united_armory.init.UAEntityTypes
import com.theendercore.united_armory.init.UAItems
import com.theendercore.united_armory.util.getEnchantLevel
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
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

open class UnnamesAnchorProjectile : AbstractArrow, ItemSupplier {
    constructor(entityType: EntityType<out UnnamesAnchorProjectile>, level: Level) : super(entityType, level)
    constructor(level: Level, owner: LivingEntity, weapon: ItemStack) : super(UAEntityTypes.UNNAMES_ANCHOR, level) {
        setOwner(owner)
        this.weapon = weapon.copy()
        isNoGravity = true
    }

    override fun getDefaultPickupItem(): ItemStack = getItem()
    override fun tryPickup(player: Player?): Boolean = player == owner
    override fun playerTouch(player: Player) {
        if (!level().isClientSide && (inGround || isNoPhysics || isReturning) && tryPickup(player)) {
            if (canReal() && airSupply < PICKUP_TIME) return
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

    open val smashRadius = 4.5

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(WEAPON_DATA, UAItems.UNNAMED_ANCHOR.defaultInstance)
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
            discard()
            return
        }

        if (airSupply < PICKUP_TIME) airSupply++

        if (position().distanceTo(ownerPos()) >= 25 && !(inGround && canReal())) {
            isReturning = true
        }

        if (isReturning) {
            noPhysics = true
            deltaMovement = deltaMovement.add(ownerPos().subtract(position()).normalize().scale(0.95))
            hasImpulse = true
        }

        if (inGround && canReal()) {
            owner!!.deltaMovement = owner!!.deltaMovement.add(position().subtract(ownerPos()).normalize().scale(0.95))
            owner!!.hasImpulse = true
            owner!!.resetFallDistance()
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
            airSupply = PICKUP_TIME
            return
        }

        var f = 8.0f
        val entity2 = owner
        val damageSource = damageSources().trident(this, (entity2 ?: this))
        if (level() is ServerLevel) {
            f = EnchantmentHelper.modifyDamage(level() as ServerLevel, getWeaponItem(), entity, damageSource, f)
        }

        if (entity.hurt(damageSource, f)) {
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
                val kbDir = entity.eyePosition.subtract(ownerPos()).normalize().scale(0.85)
                entity.addDeltaMovement(kbDir)
                entity.hasImpulse = true
//                owner.get
            }
        }

        deltaMovement = deltaMovement.multiply(-0.01, -0.1, -0.01)
        playSound(SoundEvents.TRIDENT_HIT, 1.0f, 1.0f)

        isReturning = true
    }

    override fun onHitBlock(result: BlockHitResult) {
        super.onHitBlock(result)
        setSoundEvent(hitGroundSoundEvent)
        if (canSmash()) {
            val targets = level().getEntities(this, boundingBox.inflate(smashRadius)) { it is LivingEntity }
            for (kbVictim in targets) {
                val kbDir = kbVictim.eyePosition.subtract(position())
                val scale = Mth.lerp(Mth.clamp(kbDir.length() / smashRadius, 0.0, 1.0), 1.0, 0.0)
                kbVictim.addDeltaMovement(kbDir.scale(scale * 0.85))
                kbVictim.hasImpulse = true
            }
        }
        if (!canReal()) isReturning = true
    }

    fun canReal(): Boolean = level().getEnchantLevel(UAEnchantments.TEMP_REELING, weapon) > 0
    fun canSmash(): Boolean = level().getEnchantLevel(UAEnchantments.TEMP_SHOCKWAVE, weapon) > 0

    companion object {
        const val PICKUP_TIME = 20 * 6;

        val WEAPON_DATA: EntityDataAccessor<ItemStack> =
            SynchedEntityData.defineId(UnnamesAnchorProjectile::class.java, EntityDataSerializers.ITEM_STACK)
        val RETURNING_DATA: EntityDataAccessor<Boolean> =
            SynchedEntityData.defineId(UnnamesAnchorProjectile::class.java, EntityDataSerializers.BOOLEAN)
    }
}

