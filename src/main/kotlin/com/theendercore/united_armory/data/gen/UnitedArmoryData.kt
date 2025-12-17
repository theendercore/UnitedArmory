package com.theendercore.united_armory.data.gen

import com.theendercore.united_armory.UnitedArmory.MODID
import com.theendercore.united_armory.data.gen.prov.assets.EnLangProvider
import com.theendercore.united_armory.data.gen.prov.assets.ModelProvider
import com.theendercore.united_armory.data.gen.prov.data.DamageTypeCreator
import com.theendercore.united_armory.data.gen.prov.data.EnchantmentCreator
import com.theendercore.united_armory.data.gen.prov.tags.DamageTypesTagsProvider
import com.theendercore.united_armory.data.gen.prov.tags.EntityTypeTagsProvider
import com.theendercore.united_armory.data.gen.prov.tags.ItemTagsProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import java.util.concurrent.CompletableFuture

@Suppress("unused")
object UnitedArmoryData : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        val pack = gen.createPack()

        // Asset
        pack.addProvider(::EnLangProvider)
        pack.addProvider(::ModelProvider)
        // Data
        pack.addProvider(::DynamicRegistryProvider)
        // Tags
        pack.addProvider(::ItemTagsProvider)
        pack.addProvider(::EntityTypeTagsProvider)
        pack.addProvider(::DamageTypesTagsProvider)
    }

    override fun buildRegistry(gen: RegistrySetBuilder) {
        gen.add(Registries.DAMAGE_TYPE, DamageTypeCreator::bootstrap)
        gen.add(Registries.ENCHANTMENT, EnchantmentCreator::bootstrap)
    }

    class DynamicRegistryProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
        FabricDynamicRegistryProvider(o, r) {

        override fun getName(): String = "$MODID/dyn_reg_data"

        override fun configure(reg: HolderLookup.Provider, e: Entries) {
            e.addAll(reg.lookupOrThrow(Registries.DAMAGE_TYPE))
            e.addAll(reg.lookupOrThrow(Registries.ENCHANTMENT))
        }
    }
}
