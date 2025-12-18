package com.theendercore.united_armory.util.lib

import com.theendercore.united_armory.UnitedArmory.id
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation.ADD_VALUE

@JvmField
var GREEN_MODS = mutableMapOf<ResourceLocation, Holder<Attribute>>()
fun greenModifier(
    mod: Holder<Attribute>, id: String, amount: Double, operation: AttributeModifier.Operation = ADD_VALUE,
) = greenModifier(mod, id(id), amount, operation)

fun greenModifier(
    mod: Holder<Attribute>,
    id: ResourceLocation,
    amount: Double,
    operation: AttributeModifier.Operation = ADD_VALUE,
): AttributeModifier {
    GREEN_MODS[id] = mod
    return AttributeModifier(id, amount, operation)
}