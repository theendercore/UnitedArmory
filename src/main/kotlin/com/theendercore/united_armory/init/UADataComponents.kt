package com.theendercore.united_armory.init

import com.mojang.serialization.Codec
import com.theendercore.united_armory.UnitedArmory.id
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.codec.ByteBufCodecs

object UADataComponents {
    fun init() = Unit
    // Steel from : DataComponents.class
    // Not Simple Setup
//    val GAY_CORD = register("gay_cord") { it.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).build() }
    fun <T> register(
        name: String, build: (DataComponentType.Builder<T>) -> DataComponentType<T>,
    ): DataComponentType<T> =
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id(name), build(DataComponentType.builder()))
}