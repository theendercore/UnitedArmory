package com.theendercore.united_armory.init

import com.theendercore.united_armory.UnitedArmory.id
import com.theendercore.united_armory.item.UATiers
import com.theendercore.united_armory.item.UnnamedAnchor
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.BlockTags
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.component.Tool

@Suppress("unused")
object UAItems {
    val ITEMS = mutableListOf<Item>()

    val UNNAMED_ANCHOR = register("unnamed_anchor", UnnamedAnchor(Properties().attributes(unnamedAnchor())))

    fun init() {
        DefaultItemComponentEvents.MODIFY.register { ctx ->
            ctx.modify(UNNAMED_ANCHOR) {
                val sword = it.getOrDefault(DataComponents.TOOL, UnnamedAnchor.toolProps())
                val pick = UNNAMED_ANCHOR.tier.createToolProperties(BlockTags.MINEABLE_WITH_PICKAXE)
                it.set(
                    DataComponents.TOOL, Tool(sword.rules + pick.rules, pick.defaultMiningSpeed, pick.damagePerBlock)
                )
            }
        }
    }

    fun swordAttributes(tier: Tier): ItemAttributeModifiers = SwordItem.createAttributes(tier, 3, -2.4F)
    fun unnamedAnchor(): ItemAttributeModifiers = SwordItem.createAttributes(UATiers.UNNAMED_ANCHOR, 3, -3.1F)
        .withModifierAdded(
            Attributes.SUBMERGED_MINING_SPEED,
            AttributeModifier(id("unnamed_anchor.mining"), 4.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
            EquipmentSlotGroup.MAINHAND
        )


    fun <T : Item> register(id: String, item: T): T {
        val regItem = Registry.register(BuiltInRegistries.ITEM, id(id), item)
        ITEMS.add(regItem)
        return regItem
    }
}