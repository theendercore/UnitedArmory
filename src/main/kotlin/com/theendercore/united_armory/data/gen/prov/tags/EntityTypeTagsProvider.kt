package com.theendercore.united_armory.data.gen.prov.tags

import com.theendercore.united_armory.data.tag.UAEntityTypeTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import java.util.concurrent.CompletableFuture

class EntityTypeTagsProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider.EntityTypeTagProvider(o, r) {
    override fun addTags(arg: HolderLookup.Provider) {
        getOrCreateTagBuilder(UAEntityTypeTags.CANT_BLOODSTEAL)
            .add(EntityType.ARMOR_STAND)
            .addOptional(ResourceLocation.parse("dummmmmmy:target_dummy"))
    }
}