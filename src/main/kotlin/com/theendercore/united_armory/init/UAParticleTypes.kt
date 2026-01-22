package com.theendercore.united_armory.init

import com.theendercore.united_armory.UnitedArmory.id
//import com.theendercore.united_armory.util.getModEntries
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes.simple
import net.minecraft.core.Registry
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries

object UAParticleTypes {
//    val PARTICLE_TYPES get() = getModEntries(BuiltInRegistries.SOUND_EVENT)

    val CUSTOM_SWEEP: SimpleParticleType = register("custom_sweep", simple())
    val BLOODLEACH_SWEEP: SimpleParticleType = register("bloodleach_sweep", simple())

    fun init() = Unit

    fun <O : ParticleOptions, T : ParticleType<O>> register(id: String, particleType: T): T =
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, id(id), particleType)
}