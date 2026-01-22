package com.theendercore.united_armory.data.gen.prov.data

import com.theendercore.united_armory.init.UAItems
import me.fzzyhmstrs.fzzy_config.cast
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapedRecipeBuilder.shaped
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import java.util.concurrent.CompletableFuture

class RecipeProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) : FabricRecipeProvider(o, r) {
    override fun buildRecipes(e: RecipeOutput) {
        shaped(RecipeCategory.BUILDING_BLOCKS, UAItems.NETHERITE_SHIELD)
            .pattern("#I#")
            .pattern("###")
            .pattern(" # ")
            .defineUnlockedBy('#', Items.ANCIENT_DEBRIS)
            .defineUnlockedBy('I', Items.NETHERITE_INGOT)
            .save(e)

    }


    // lib stuff
    fun ShapedRecipeBuilder.defineUnlockedBy(c: Char, item: ItemLike): ShapedRecipeBuilder =
        define(c, item).unlockedBy(item)

    fun ShapedRecipeBuilder.defineUnlockedBy(c: Char, tag: TagKey<Item>): ShapedRecipeBuilder =
        define(c, tag).unlockedBy(tag)

    inline fun <reified T : RecipeBuilder> T.unlockedBy(item: ItemLike): T =
        unlockedBy(getHasName(item), has(item)).cast<T>()

    inline fun <reified T : RecipeBuilder> T.unlockedBy(tag: TagKey<Item>): T =
        unlockedBy("has_${tag.location.path}", has(tag)).cast<T>()
}