package com.theendercore.united_armory.item

import com.theendercore.united_armory.data.tag.UAItemTags.NETHERITE_SHIELD_REPAIR
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ShieldItem

class CustomShieldItem(val repairTag: TagKey<Item>, properties: Properties) : ShieldItem(properties) {
    constructor(properties: Properties) : this(NETHERITE_SHIELD_REPAIR, properties)

    override fun isValidRepairItem(itemStack: ItemStack, itemStack2: ItemStack): Boolean =
        itemStack2.`is`(repairTag) || super.isValidRepairItem(itemStack, itemStack2)
}