package com.theendercore.united_armory.init

import com.theendercore.united_armory.UnitedArmory.id
import com.theendercore.united_armory.item.*
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponents
import net.minecraft.core.component.DataComponents.TOOL
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.BlockTags
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.component.Tool
import net.minecraft.world.level.block.entity.BannerPatternLayers

@Suppress("unused")
object UAItems {
    val ITEMS = mutableListOf<Item>()

    val UNNAMED_ANCHOR = register("unnamed_anchor", UnnamedAnchor(attrib(unnamedAnchor())))
    val UNNAMED_SCYTHE =
        register("unnamed_scythe", UnnamedScythe(attrib(swordAttributes(UATiers.UNNAMED_SCYTHE, -2.6f))))
    val UNNAMED_SPEAR = register("unnamed_spear", UnnamedSpear(attrib(unnamedSpear())))

    val NETHERITE_SHIELD = register(
        "netherite_shield", CustomShieldItem(
            Properties().durability(512).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
        )
    )
    val UNNAMED_CROWN = register(
        "unnamed_crown", ArmorItem(
            UAArmorMaterials.CROWN, ArmorItem.Type.HELMET,
            Properties().durability(ArmorItem.Type.HELMET.getDurability(10)).attributes(
                ItemAttributeModifiers.builder().add(
                    Attributes.MAX_HEALTH, AttributeModifier(id("unnamed_crown.max_health"), 10.0, ADD_VALUE),
                    EquipmentSlotGroup.HEAD
                ).build()
            )
        )
    )

    val STRENGTH_RING = register("strength_ring", Item(attrib(ring(Attributes.ATTACK_DAMAGE, 12.0))))
    val SPEED_RING = register("speed_ring", Item(attrib(ring(Attributes.MOVEMENT_SPEED, 0.8, ADD_MULTIPLIED_TOTAL))))
    val HEALTH_BOOST_RING = register("health_boost_ring", Item(attrib(ring(Attributes.MAX_HEALTH, 10.0 ))))

    fun ring(
        mod: Holder<Attribute>,
        amount: Double,
        addType: AttributeModifier.Operation = ADD_VALUE,
    ): ItemAttributeModifiers = ItemAttributeModifiers.builder().add(
        mod, AttributeModifier(id("ring.${mod.unwrapKey().get().location().path}"), amount, addType),
        EquipmentSlotGroup.HAND
    ).build()


    fun init() {
        DefaultItemComponentEvents.MODIFY.register { ctx ->
            ctx.modify(UNNAMED_ANCHOR) {
                val sword = it.getOrDefault(TOOL, UnnamedAnchor.toolProps())
                val pick = UNNAMED_ANCHOR.tier.createToolProperties(BlockTags.MINEABLE_WITH_PICKAXE)
                it.set(TOOL, Tool(sword.rules + pick.rules, pick.defaultMiningSpeed, pick.damagePerBlock))
            }
        }
    }

    fun attrib(mods: ItemAttributeModifiers): Properties = Properties().attributes(mods)

    fun swordAttributes(tier: Tier, speed: Float = -2.4f): ItemAttributeModifiers =
        SwordItem.createAttributes(tier, 3, speed)

    fun unnamedAnchor(): ItemAttributeModifiers = swordAttributes(UATiers.UNNAMED_ANCHOR, -3.1F)
        .withModifierAdded(
            Attributes.SUBMERGED_MINING_SPEED,
            AttributeModifier(id("unnamed_anchor.mining"), 4.0, ADD_MULTIPLIED_TOTAL),
            EquipmentSlotGroup.MAINHAND
        )

    fun unnamedSpear(): ItemAttributeModifiers = swordAttributes(UATiers.UNNAMED_SPEAR, -3.1F)
        .withModifierAdded(
            Attributes.BLOCK_INTERACTION_RANGE,
            AttributeModifier(id("unnamed_spear.block_interaction_range"), 1.5, ADD_VALUE),
            EquipmentSlotGroup.MAINHAND
        )
        .withModifierAdded(
            Attributes.ENTITY_INTERACTION_RANGE,
            AttributeModifier(id("unnamed_spear.entity_interaction_range"), 1.5, ADD_VALUE),
            EquipmentSlotGroup.MAINHAND
        )


    fun <T : Item> register(id: String, item: T): T {
        val regItem = Registry.register(BuiltInRegistries.ITEM, id(id), item)
        ITEMS.add(regItem)
        return regItem
    }
}