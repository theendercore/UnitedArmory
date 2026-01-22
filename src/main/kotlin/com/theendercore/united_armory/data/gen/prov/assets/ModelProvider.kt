package com.theendercore.united_armory.data.gen.prov.assets


import com.theendercore.united_armory.init.UAItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.ModelTemplate
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.world.item.Item

class ModelProvider(o: FabricDataOutput) : FabricModelProvider(o) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockModelGenerators) = Unit
    override fun generateItemModels(gen: ItemModelGenerators) {
        gen.genList(
            listOf(
                UAItems.UNNAMED_ANCHOR,
                UAItems.BLOODLEACH,
                UAItems.UNNAMED_SPEAR
            ), ModelTemplates.FLAT_HANDHELD_ITEM
        )
        gen.genList(
            listOf(
                UAItems.UNNAMED_CROWN,
                UAItems.STRENGTH_RING,
                UAItems.SPEED_RING,
                UAItems.HEALTH_BOOST_RING
            ), ModelTemplates.FLAT_ITEM
        )
    }

    fun ItemModelGenerators.genList(flat: List<Item>, model: ModelTemplate) {
        for (item in flat) generateFlatItem(item, model)
    }
}
