package com.theendercore.united_armory.util

import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvent
import kotlin.jvm.optionals.getOrNull

fun SoundEvent.holder(): Holder<SoundEvent> = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(this)

fun Holder<*>.getId() = unwrapKey().getOrNull()?.location()