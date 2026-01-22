package com.theendercore.united_armory.data.gen.prov.assets

import com.theendercore.united_armory.data.tag.UAItemTags
import com.theendercore.united_armory.init.UAEntityTypes
import com.theendercore.united_armory.init.UAItems
import com.theendercore.united_armory.init.UAMobEffects
import com.theendercore.united_armory.init.UATabs
import com.theendercore.united_armory.item.CustomShieldItem
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.util.concurrent.CompletableFuture

class EnLangProvider(o: FabricDataOutput, r: CompletableFuture<HolderLookup.Provider>) :
    FabricLanguageProvider(o, r) {

    override fun generateTranslations(lookup: HolderLookup.Provider, gen: TranslationBuilder) {
        UAItems.ITEMS.forEach { gen.add(it.descriptionId, genLang(it.id)) }
        gen.shieldLangGen(UAItems.NETHERITE_SHIELD)

        UAMobEffects.MOB_EFFECTS.forEach { gen.add(it.descriptionId, genLang(it.id)) }
        UAEntityTypes.ENTITY_TYPES.forEach { gen.add(it.descriptionId, genLang(it.id)) }

        gen.add(UATabs.TAB_KEY, "United Armory")
        UAItemTags.ITEM_TAGS.forEach { gen.add(it.translationKey, genLang(it.location)) }
    }

    private fun genLang(id: ResourceLocation): String = genLang(id.path)
    private fun genLang(id: String): String =
        id.split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }

    val Any.id
        get() = when (this) {
            is Item -> BuiltInRegistries.ITEM.getKey(this)
            is Block -> BuiltInRegistries.BLOCK.getKey(this)
            is EntityType<*> -> BuiltInRegistries.ENTITY_TYPE.getKey(this)
            else -> error("Invalid Entry")
        }


    fun TranslationBuilder.shieldLangGen(shield: CustomShieldItem) {
        for (color in DyeColor.entries.map(DyeColor::getName)) {
            add("${shield.descriptionId}.$color", genLang("${color}_${shield.id.path}"))
        }
    }
}