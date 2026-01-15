package com.theendercore.united_armory.util

import com.theendercore.united_armory.UnitedArmory.MODID
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.Level
import kotlin.jvm.optionals.getOrNull

fun SoundEvent.holder(): Holder<SoundEvent> = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(this)

fun Holder<*>.getId() = unwrapKey().getOrNull()?.location()

fun <T> isModHolder(holder: Holder<T>) = holder.`is` { it.location().namespace == MODID }

fun <T> getModHolders(registry: Registry<T>): List<Holder<T>> = registry.holders()
    .filter(::isModHolder)
    .toList()

fun <T> getModEntries(registry: Registry<T>): List<T> = registry.holders()
    .filter(::isModHolder)
    .map(Holder<T>::value)
    .toList()

fun Level.getEnchantLevel(enchantment: ResourceKey<Enchantment>, weapon: ItemStack): Int {
    return EnchantmentHelper.getItemEnchantmentLevel(
        this.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment), weapon
    )
}
