package com.theendercore.united_armory.init.misc

import com.theendercore.united_armory.UnitedArmory.id
import com.theendercore.united_armory.init.UADataComponents
import com.theendercore.united_armory.item.UATiers
import com.theendercore.united_armory.item.component.CustomSweep
import com.theendercore.united_armory.util.holder
import com.theendercore.united_armory.util.lib.greenModifier
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.level.block.entity.BannerPatternLayers


fun attributes(mods: ItemAttributeModifiers): Properties = Properties().attributes(mods)

fun swordAttributes(tier: Tier, speed: Float = -2.4f): ItemAttributeModifiers =
    SwordItem.createAttributes(tier, 3, speed)

fun unnamedAnchor(): Properties = Properties()
    .attributes(
        swordAttributes(UATiers.UNNAMED_ANCHOR, -3.1F)
            .withModifierAdded(
                Attributes.SUBMERGED_MINING_SPEED,
                AttributeModifier(id("unnamed_anchor.mining"), 4.0, ADD_MULTIPLIED_TOTAL),
                EquipmentSlotGroup.MAINHAND
            )
    )
    .customSweep(SoundEvents.ANVIL_PLACE, ParticleTypes.EXPLOSION)


fun unnamedSpear(): Properties = Properties()
    .attributes(
        swordAttributes(UATiers.UNNAMED_SPEAR, -3.1F)
            .withModifierAdded(
                Attributes.BLOCK_INTERACTION_RANGE,
                greenModifier(Attributes.BLOCK_INTERACTION_RANGE, "unnamed_spear.block_interaction_range", 1.5),
                EquipmentSlotGroup.MAINHAND
            )
            .withModifierAdded(
                Attributes.ENTITY_INTERACTION_RANGE,
                greenModifier(Attributes.ENTITY_INTERACTION_RANGE, "unnamed_spear.entity_interaction_range", 1.5),
                EquipmentSlotGroup.MAINHAND
            )
    )

fun shieldProps(durability: Int = 512): Properties = Properties()
    .durability(durability)
    .component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)

fun unnamedCrown(): Properties = Properties()
    .durability(666/*ArmorItem.Type.HELMET.getDurability(1)*/)
    .attributes(
        ItemAttributeModifiers.builder().add(
            Attributes.MAX_HEALTH,
            AttributeModifier(id("unnamed_crown.max_health"), 10.0, AttributeModifier.Operation.ADD_VALUE),
            EquipmentSlotGroup.HEAD
        ).build()
    )


fun Properties.customSweep(sound: SoundEvent, particle: ParticleOptions?): Properties =
    customSweep(sound.holder(), particle)

fun Properties.customSweep(sound: Holder<SoundEvent>?, particle: ParticleOptions?): Properties =
    customSweep(CustomSweep(sound, particle))

fun Properties.customSweep(customSweep: CustomSweep): Properties = component(UADataComponents.CUSTOM_SWEEP, customSweep)
