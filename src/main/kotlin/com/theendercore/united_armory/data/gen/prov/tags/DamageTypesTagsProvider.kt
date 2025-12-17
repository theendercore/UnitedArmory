package com.theendercore.united_armory.data.gen.prov.tags

import com.theendercore.united_armory.data.UADamageTypes
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageType
import java.util.concurrent.CompletableFuture

class DamageTypesTagsProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricTagProvider<DamageType>(o, Registries.DAMAGE_TYPE, r) {
    override fun addTags(arg: HolderLookup.Provider) {
        // vanilla
        getOrCreateTagBuilder(DamageTypeTags.BYPASSES_INVULNERABILITY).add(UADamageTypes.UNNAMED_BLOODSTEAL)
    }
}