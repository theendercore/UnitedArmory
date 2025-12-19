package com.theendercore.united_armory.effect

import com.theendercore.united_armory.init.UADataAttachments
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

@Suppress("UnstableApiUsage")
class IEffect(category: MobEffectCategory, color: Int) : OpenMobEffect(category, color) {
    override fun onEffectStarted(livingEntity: LivingEntity, i: Int) {
        super.onEffectStarted(livingEntity, i)
        livingEntity.setAttached(UADataAttachments.IFRAME_EFFECT, true)
    }

    override fun onMobRemoved(livingEntity: LivingEntity, i: Int, removalReason: Entity.RemovalReason) {
        super.onMobRemoved(livingEntity, i, removalReason)
        livingEntity.removeAttached(UADataAttachments.IFRAME_EFFECT)
    }

}