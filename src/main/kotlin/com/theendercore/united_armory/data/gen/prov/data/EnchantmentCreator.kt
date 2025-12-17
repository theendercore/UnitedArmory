package com.theendercore.united_armory.data.gen.prov.data

import com.theendercore.united_armory.data.UAEnchantments
import com.theendercore.united_armory.data.tag.UAItemTags
import net.minecraft.advancements.critereon.DamageSourcePredicate
import net.minecraft.advancements.critereon.TagPredicate
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.DamageTypeTags
import net.minecraft.tags.EnchantmentTags
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents
import net.minecraft.world.item.enchantment.LevelBasedValue
import net.minecraft.world.item.enchantment.effects.AddValue
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition


@Suppress("HasPlatformType")
object EnchantmentCreator {

    fun bootstrap(c: BootstrapContext<Enchantment>) {
//        val damageType = c.lookup(Registries.DAMAGE_TYPE)
        val items = c.lookup(Registries.ITEM)
//        val blocks = c.lookup(Registries.BLOCK)
        val enchants = c.lookup(Registries.ENCHANTMENT)

        // Steel from Enchantments.class
        // or DD, there is a lot of custom stuff in DD
        c.register(
            UAEnchantments.TEMP_REELING,
            Enchantment.enchantment(
                Enchantment.definition(
                    items.getOrThrow(UAItemTags.ANCHOR_ENCHANTABLE),
                    10,
                    1,
                    Enchantment.dynamicCost(1, 11),
                    Enchantment.dynamicCost(12, 11),
                    1,
                    EquipmentSlotGroup.MAINHAND
                )
            )
                .exclusiveWith(enchants.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
                .withEffect(
                    EnchantmentEffectComponents.DAMAGE_PROTECTION,
                    AddValue(LevelBasedValue.perLevel(1.0f)),
                    DamageSourceCondition.hasDamageSource(
                        DamageSourcePredicate.Builder.damageType()
                            .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                    )
                )
        )
        c.register(
            UAEnchantments.TEMP_SHOCKWAVE,
            Enchantment.enchantment(
                Enchantment.definition(
                    items.getOrThrow(UAItemTags.ANCHOR_ENCHANTABLE),
                    10, 1,
                    Enchantment.dynamicCost(1, 11),
                    Enchantment.dynamicCost(12, 11),
                    1,
                    EquipmentSlotGroup.MAINHAND
                )
            )
                .exclusiveWith(enchants.getOrThrow(EnchantmentTags.ARMOR_EXCLUSIVE))
                .withEffect(
                    EnchantmentEffectComponents.DAMAGE_PROTECTION,
                    AddValue(LevelBasedValue.perLevel(1.0f)),
                    DamageSourceCondition.hasDamageSource(
                        DamageSourcePredicate.Builder.damageType()
                            .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                    )
                )
        )
    }


    fun BootstrapContext<Enchantment>.register(
        registryKey: ResourceKey<Enchantment>, builder: Enchantment.Builder,
    ) = register(registryKey, builder.build(registryKey.location()))
}