package com.theendercore.united_armory.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent
import java.util.*
import kotlin.jvm.optionals.getOrNull

data class CustomAttackSounds(
    val weak: Holder<SoundEvent>?,
    val strong: Holder<SoundEvent>?,
    val crit: Holder<SoundEvent>?,
) {
    companion object {
        var CODEC: Codec<CustomAttackSounds> = RecordCodecBuilder.create { inst ->
            inst.group(
                SoundEvent.CODEC.optionalFieldOf("weak").forGetter { Optional.ofNullable(it.weak) },
                SoundEvent.CODEC.optionalFieldOf("strong").forGetter { Optional.ofNullable(it.strong) },
                SoundEvent.CODEC.optionalFieldOf("crit").forGetter { Optional.ofNullable(it.crit) },
            ).apply(inst) { weak, strong, crit ->
                CustomAttackSounds(weak.getOrNull(), strong.getOrNull(), crit.getOrNull())
            }
        }
        var STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, CustomAttackSounds> = StreamCodec.composite(
            SoundEvent.STREAM_CODEC, CustomAttackSounds::weak,
            SoundEvent.STREAM_CODEC, CustomAttackSounds::strong,
            SoundEvent.STREAM_CODEC, CustomAttackSounds::crit,
            ::CustomAttackSounds
        )
    }
}
