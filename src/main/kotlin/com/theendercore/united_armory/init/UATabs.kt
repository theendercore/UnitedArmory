package com.theendercore.united_armory.init

import com.theendercore.united_armory.UnitedArmory.MODID
import com.theendercore.united_armory.UnitedArmory.id
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Items

@Suppress("SameParameterValue")
object UATabs {
    const val TAB_KEY = "itemGroup.$MODID.main"

    val MOD_TAB = register(
        MODID, FabricItemGroup.builder()
            .icon { Items.BLAZE_ROD.defaultInstance }
            .title(Component.translatable(TAB_KEY))
            .displayItems { _, tab -> UAItems.ITEMS.forEach(tab::accept) }
    )

    fun init() = Unit

    fun register(name: String, builder: CreativeModeTab.Builder): Holder.Reference<CreativeModeTab> {
        return Registry.registerForHolder(BuiltInRegistries.CREATIVE_MODE_TAB, id(name), builder.build())
    }
}