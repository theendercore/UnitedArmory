package com.theendercore.united_armory.init

import com.theendercore.united_armory.UnitedArmory.id
import com.theendercore.united_armory.init.UAArmorMaterials.CROWN
import com.theendercore.united_armory.init.misc.*
import com.theendercore.united_armory.item.CustomShieldItem
import com.theendercore.united_armory.item.UnnamedAnchor
import com.theendercore.united_armory.item.UnnamedScythe
import com.theendercore.united_armory.item.UnnamedSpear
import com.theendercore.united_armory.util.getId
import com.theendercore.united_armory.util.getModEntries
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponents.TOOL
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.BlockTags
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE
import net.minecraft.world.entity.ai.attributes.Attributes.*
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.component.Tool

@Suppress("unused")
object UAItems {
    val ITEMS get() = getModEntries(BuiltInRegistries.ITEM)

    val UNNAMED_ANCHOR = register("unnamed_anchor", UnnamedAnchor(unnamedAnchor()))
    val UNNAMED_SCYTHE = register("unnamed_scythe", UnnamedScythe(unnamedScythe()))
    val UNNAMED_SPEAR = register("unnamed_spear", UnnamedSpear(unnamedSpear()))
    val NETHERITE_SHIELD = register("netherite_shield", CustomShieldItem(shieldProps()))
    val UNNAMED_CROWN = register("unnamed_crown", ArmorItem(CROWN, ArmorItem.Type.HELMET, unnamedCrown()))
    val STRENGTH_RING = register("strength_ring", ring(ATTACK_DAMAGE, 12.0))
    val SPEED_RING = register("speed_ring", ring(MOVEMENT_SPEED, 0.8, ADD_MULTIPLIED_TOTAL))
    val HEALTH_BOOST_RING = register("health_boost_ring", ring(MAX_HEALTH, 10.0))

    fun init() {
        DefaultItemComponentEvents.MODIFY.register { ctx ->
            ctx.modify(UNNAMED_ANCHOR) {
                val sword = it.getOrDefault(TOOL, UnnamedAnchor.toolProps())
                val pick = UNNAMED_ANCHOR.tier.createToolProperties(BlockTags.MINEABLE_WITH_PICKAXE)
                it.set(TOOL, Tool(sword.rules + pick.rules, pick.defaultMiningSpeed, pick.damagePerBlock))
            }
        }
    }

    fun ring(mod: Holder<Attribute>, amount: Double, addType: AttributeModifier.Operation = ADD_VALUE) = Item(
        attributes(
            ItemAttributeModifiers.builder()
                .add(mod, AttributeModifier(id("ring.${mod.getId()!!.path}"), amount, addType), EquipmentSlotGroup.HAND)
                .build()
        )
    )


    fun <T : Item> register(id: String, item: T): T {
        val regItem = Registry.register(BuiltInRegistries.ITEM, id(id), item)
        return regItem
    }
}