package com.theendercore.united_armory.data.tag

import com.theendercore.united_armory.UnitedArmory.id
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object UAItemTags {
    val ITEM_TAGS = mutableSetOf<TagKey<Item>>()

    val ANCHOR_ENCHANTABLE = create("enchantable/anchor")


    fun create(id: String): TagKey<Item> {
        val tag = TagKey.create(Registries.ITEM, id(id))
        ITEM_TAGS.add(tag)
        return tag
    }
}