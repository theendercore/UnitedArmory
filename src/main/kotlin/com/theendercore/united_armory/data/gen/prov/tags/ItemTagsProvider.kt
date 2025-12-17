package com.theendercore.united_armory.data.gen.prov.tags

import com.theendercore.united_armory.data.tag.UAItemTags
import com.theendercore.united_armory.init.UAItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags
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
        getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE).add(UAItems.NETHERITE_SHIELD)

        // mod
        getOrCreateTagBuilder(UAItemTags.ANCHOR_ENCHANTABLE).add(UAItems.UNNAMED_ANCHOR)
        getOrCreateTagBuilder(UAItemTags.NON_DISABLEABLE_SHIELD).add(UAItems.NETHERITE_SHIELD)

        conventionTags()
    }

    fun conventionTags() {
        getOrCreateTagBuilder(ConventionalItemTags.SHIELD_TOOLS).add(UAItems.NETHERITE_SHIELD)
    }
}