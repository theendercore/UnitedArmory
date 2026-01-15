package com.theendercore.united_armory.entity

import com.theendercore.united_armory.init.UAItems
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ItemSupplier
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

open class WeaponProjectile : Projectile, ItemSupplier {
    constructor(type: EntityType<out WeaponProjectile>, level: Level) : super(type, level)

    constructor(
        entityType: EntityType<out WeaponProjectile>,
        owner: LivingEntity, level: Level, stack: ItemStack,
    ) : this(entityType, level) {
        this.owner = owner
        weapon = stack
    }


    open fun defaultWeapon(): ItemStack = UAItems.UNNAMED_ANCHOR.defaultInstance
    var weapon: ItemStack
        get() = entityData.get(WEAPON_DATA)
        set(value) {
            entityData.set(WEAPON_DATA, value)
        }

    override fun getWeaponItem(): ItemStack = weapon
    override fun getItem(): ItemStack = weapon

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        builder.define(WEAPON_DATA, defaultWeapon())
    }

    override fun recreateFromPacket(packet: ClientboundAddEntityPacket) {
        super.recreateFromPacket(packet)
        deltaMovement = Vec3(packet.xa, packet.ya, packet.za)
        if (owner == null) discard()
    }


    override fun addAdditionalSaveData(nbt: CompoundTag) {
        super.addAdditionalSaveData(nbt)
        nbt.put(WEAPON, weapon.save(registryAccess()))
    }

    override fun readAdditionalSaveData(nbt: CompoundTag) {
        super.readAdditionalSaveData(nbt)
        weapon = ItemStack.parse(registryAccess(), nbt.getCompound(WEAPON)).orElse(defaultWeapon())
    }


    companion object {
        const val WEAPON = "weapon"

        val WEAPON_DATA: EntityDataAccessor<ItemStack> =
            SynchedEntityData.defineId(WeaponProjectile::class.java, EntityDataSerializers.ITEM_STACK)
    }
}