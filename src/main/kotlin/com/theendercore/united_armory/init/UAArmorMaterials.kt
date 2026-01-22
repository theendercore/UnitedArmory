package com.theendercore.united_armory.init

import com.theendercore.united_armory.UnitedArmory.id
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.crafting.Ingredient

object UAArmorMaterials {

    val FARLANDS_CROWN = register(
        "farlands_crown", ArmorMaterial(
            mapOf(
                ArmorItem.Type.BOOTS to 0,
                ArmorItem.Type.LEGGINGS to 0,
                ArmorItem.Type.CHESTPLATE to 0,
                ArmorItem.Type.HELMET to 5,
                ArmorItem.Type.BODY to 5,
            ),
            0,
            SoundEvents.ARMOR_EQUIP_GOLD,
            { Ingredient.of(ItemTags.DIRT) },
            listOf(ArmorMaterial.Layer(id("farlands_crown"))),
            0f, 0f,
        )
    )


    fun init() = Unit
    fun register(id: String, item: ArmorMaterial): Holder.Reference<ArmorMaterial> =
        Registry.registerForHolder(BuiltInRegistries.ARMOR_MATERIAL, id(id), item)
}