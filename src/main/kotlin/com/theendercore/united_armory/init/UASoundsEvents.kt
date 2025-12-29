package com.theendercore.united_armory.init

import com.theendercore.united_armory.UnitedArmory.id
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvent

object UASoundsEvents {
//    val SOUNDS_EVENTS get() = getModEntries(BuiltInRegistries.SOUND_EVENT)

    val ATTACK_WEAK = attack("weak")
    val ATTACK_STRONG = attack("strong")
    val ATTACK_CRIT = attack("crit")
    val ATTACK_SWEEP = attack("sweep")

    fun init() = Unit

    fun attack(name: String) = register("united_armory.entity.player.attack.$name")
    fun register(id: String): Holder.Reference<SoundEvent> {
        val loc = id(id)
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, loc, SoundEvent.createVariableRangeEvent(loc))
    }
}