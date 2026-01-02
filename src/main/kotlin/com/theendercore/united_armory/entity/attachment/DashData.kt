package com.theendercore.united_armory.entity.attachment

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack

data class DashData(var dashTicks: Int, var dashDamage: Int, var weapon: ItemStack) {
    companion object {
        var CODEC: Codec<DashData> = RecordCodecBuilder.create { inst ->
            inst.group(
                Codec.INT.fieldOf("dash_ticks").forGetter { it.dashTicks },
                Codec.INT.fieldOf("dash_damage").forGetter { it.dashDamage },
                ItemStack.OPTIONAL_CODEC.fieldOf("weapon").forGetter { it.weapon },
            ).apply(inst, ::DashData)
        }
        var STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, DashData> = StreamCodec.composite(
            ByteBufCodecs.INT, DashData::dashTicks,
            ByteBufCodecs.INT, DashData::dashDamage,
            ItemStack.OPTIONAL_STREAM_CODEC, DashData::weapon,
            ::DashData
        )
    }
}
