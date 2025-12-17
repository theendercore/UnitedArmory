package com.theendercore.united_armory.data.gen.prov.assets


import com.theendercore.united_armory.init.UAItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.ItemModelGenerators
import net.minecraft.data.models.model.ModelTemplates

class ModelProvider(o: FabricDataOutput) : FabricModelProvider(o) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockModelGenerators) = Unit
    override fun generateItemModels(gen: ItemModelGenerators) {
        val weapon = listOf(UAItems.UNNAMED_ANCHOR, UAItems.UNNAMED_SCYTHE, UAItems.UNNAMED_SPEAR)
        for (item in weapon) {
            gen.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM)
        }
    }
}
