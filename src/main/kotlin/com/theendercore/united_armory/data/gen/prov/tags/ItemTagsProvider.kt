package com.theendercore.united_armory.data.gen.prov.tags

import com.theendercore.united_armory.data.tag.UAItemTags
import com.theendercore.united_armory.init.UAItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.tags.ItemTags
import java.util.concurrent.CompletableFuture

class ItemTagsProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider.ItemTagProvider(o, r) {
    override fun addTags(arg: HolderLookup.Provider) {
        // vanilla
        getOrCreateTagBuilder(ItemTags.SWORDS)
            .add(UAItems.UNNAMED_ANCHOR, UAItems.UNNAMED_SCYTHE, UAItems.UNNAMED_SPEAR)
        getOrCreateTagBuilder(ItemTags.PICKAXES).add(UAItems.UNNAMED_ANCHOR)
        // mod
        getOrCreateTagBuilder(UAItemTags.ANCHOR_ENCHANTABLE).add(UAItems.UNNAMED_ANCHOR)

        conventionTags()
    }

    fun conventionTags() {
//        getOrCreateTagBuilder(ConventionalItemTags.EGGS)
//            .add(Items.HEAVY_CORE)
    }
}