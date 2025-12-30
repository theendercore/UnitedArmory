package com.theendercore.united_armory.effect

import net.minecraft.world.entity.LivingEntity

interface RemovableMobEffect {
    fun onEffectRemove(entity: LivingEntity)
}