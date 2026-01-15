package com.theendercore.united_armory.init

import com.theendercore.united_armory.UnitedArmory.id
import com.theendercore.united_armory.entity.UnnamesAnchorProjectile
import com.theendercore.united_armory.entity.UnnamesAnchorProjectile2
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

@Suppress("unused", "MemberVisibilityCanBePrivate")
object UAEntityTypes {
    val ENTITY_TYPES = mutableListOf<EntityType<*>>()

//    // Steel from : EntityType.class
    val UNNAMES_ANCHOR = register(
        "hammer_head", EntityType.Builder.of(::UnnamesAnchorProjectile, MobCategory.MISC)
            .sized(0.5f, 0.5f)
            .noSummon()
    )
    val UNNAMES_ANCHOR2 = register(
        "unnames_anchor", EntityType.Builder.of(::UnnamesAnchorProjectile2, MobCategory.MISC)
            .sized(0.75f, 0.75f)
            .noSummon()
    )

    fun init() = Unit
    fun <T : Entity> register(id: String, builder: EntityType.Builder<T>): EntityType<T> {
        val regItem = Registry.register(BuiltInRegistries.ENTITY_TYPE, id(id), builder.build(id(id).toString()))
        ENTITY_TYPES.add(regItem)
        return regItem
    }
}