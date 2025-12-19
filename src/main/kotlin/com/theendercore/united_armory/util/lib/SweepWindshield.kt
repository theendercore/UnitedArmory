package com.theendercore.united_armory.util.lib

import com.theendercore.united_armory.init.UADataComponents.CUSTOM_SWEEP
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.ItemStack

fun getCustomSweepParticle(stack: ItemStack): ParticleOptions? = stack.get(CUSTOM_SWEEP)?.particle
fun getCustomSweepSound(stack: ItemStack): Holder<SoundEvent>? = stack.get(CUSTOM_SWEEP)?.sound
