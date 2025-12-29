package com.theendercore.united_armory.init

import com.theendercore.united_armory.UnitedArmory.id
import com.theendercore.united_armory.item.component.CustomAttackSounds
import com.theendercore.united_armory.item.component.CustomSweep
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries

object UADataComponents {
    fun init() = Unit

    val CUSTOM_SWEEP = register("custom_sweep") {
        it.persistent(CustomSweep.CODEC).networkSynchronized(CustomSweep.STREAM_CODEC).cacheEncoding().build()
    }

    val CUSTOM_ATTACK_SOUNDS = register("custom_attack_sounds") {
        it.persistent(CustomAttackSounds.CODEC).networkSynchronized(CustomAttackSounds.STREAM_CODEC)
            .cacheEncoding().build()
    }

    fun <T> register(
        name: String, build: (DataComponentType.Builder<T>) -> DataComponentType<T>,
    ): DataComponentType<T> =
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id(name), build(DataComponentType.builder()))
}