package com.theendercore.united_armory.data

import com.theendercore.united_armory.UnitedArmory.id
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment


object NameEnchantments {
    val ENCHANTMENTS = mutableSetOf<ResourceKey<Enchantment>>()
    
    val CUSTOM = create("custom")

     fun create(id: String): ResourceKey<Enchantment> {
        val enchantment = ResourceKey.create(Registries.ENCHANTMENT, id(id))
        ENCHANTMENTS.add(enchantment)
        return enchantment
    }
}