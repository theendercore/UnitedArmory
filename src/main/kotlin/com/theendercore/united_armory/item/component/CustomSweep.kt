package com.theendercore.united_armory.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class CustomSweep(val sound: Holder<SoundEvent>?, var particle: ParticleOptions?) {
    companion object {
        var CODEC: Codec<CustomSweep> = RecordCodecBuilder.create { inst ->
            inst.group(
                SoundEvent.CODEC.optionalFieldOf("sound").forGetter { Optional.ofNullable(it.sound) },
                ParticleTypes.CODEC.optionalFieldOf("particle").forGetter { Optional.ofNullable(it.particle) },
            ).apply(inst) { sound, particle -> CustomSweep(sound.getOrNull(), particle.getOrNull()) }
        }
        var STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, CustomSweep> = StreamCodec.composite(
            SoundEvent.STREAM_CODEC, CustomSweep::sound,
            ParticleTypes.STREAM_CODEC, CustomSweep::particle,
            ::CustomSweep
        )
    }
}
