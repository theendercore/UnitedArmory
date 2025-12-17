package com.theendercore.united_armory.data.tag

import com.theendercore.united_armory.UnitedArmory.id
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType

object UAEntityTypeTags {
    val CANT_BLOODSTEAL = create("cant_bloodsteal")


    fun create(id: String): TagKey<EntityType<*>> = TagKey.create<EntityType<*>>(Registries.ENTITY_TYPE, id(id))
}