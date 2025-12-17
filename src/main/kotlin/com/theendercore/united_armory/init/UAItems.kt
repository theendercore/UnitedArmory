package com.theendercore.united_armory.init

import com.theendercore.united_armory.UnitedArmory.id
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item

@Suppress("unused", "MemberVisibilityCanBePrivate")
object UAItems {
    val ITEMS = mutableListOf<Item>()

//    val GAY_CORE = register("gay_core", Item(Item.Properties()))

    fun init() = Unit
    fun register(id: String, item: Item): Item {
        val regItem = Registry.register(BuiltInRegistries.ITEM, id(id), item)
        ITEMS.add(regItem)
        return regItem
    }
}