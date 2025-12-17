package com.theendercore.united_armory.item

import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tier
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

object UATiers {

    val UNNAMED_ANCHOR = UATier(10, 6f, 10f, 0, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, ItemTags.DIRT)
    val UNNAMED_SCYTHE = UATier(10, 6f, 8f, 0, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, ItemTags.DIRT)
    val UNNAMED_SPEAR = UATier(10, 6f, 6f, 0, BlockTags.INCORRECT_FOR_DIAMOND_TOOL, ItemTags.DIRT)

    class UATier(
        private val uses: Int, private val speed: Float, val attackDamage: Float, private val enchantmentValue: Int,
        val incorrectDrops: TagKey<Block>, val repairTag: TagKey<Item>,
    ) : Tier {
        override fun getUses(): Int = uses
        override fun getSpeed(): Float = speed
        override fun getAttackDamageBonus(): Float = attackDamage
        override fun getIncorrectBlocksForDrops(): TagKey<Block> = incorrectDrops
        override fun getEnchantmentValue(): Int = enchantmentValue
        override fun getRepairIngredient(): Ingredient = Ingredient.of(repairTag)
    }
}