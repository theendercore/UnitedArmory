package com.theendercore.united_armory.init

import com.theendercore.united_armory.UnitedArmory.id
import com.theendercore.united_armory.effect.IEffect
import com.theendercore.united_armory.util.getModEntries
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory

@Suppress("unused", "MemberVisibilityCanBePrivate")
object UAMobEffects {
    val MOB_EFFECTS = getModEntries(BuiltInRegistries.MOB_EFFECT)

    // Phantom
    @JvmField
    val UNNAMED_IFRAMES = register("unnamed_iframes", IEffect(MobEffectCategory.BENEFICIAL, 0x33d6ff))

    fun init() = Unit
    fun <T : MobEffect> register(id: String, effect: T): Holder<MobEffect> =
        Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, id(id), effect)
}