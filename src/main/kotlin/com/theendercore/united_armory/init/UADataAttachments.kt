package com.theendercore.united_armory.init

import com.mojang.serialization.Codec
import com.theendercore.united_armory.UnitedArmory.id
import com.theendercore.united_armory.entity.attachment.DashData
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.minecraft.network.codec.ByteBufCodecs
import java.util.function.Consumer
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate.all as syncAllPredicate

@Suppress("UnstableApiUsage")
object UADataAttachments {
    fun init() = Unit

    @JvmField
    val PHANTASM_ATTACHMENT =
        register("phantasm_attachment") { it.persistent(Codec.BOOL).syncWith(ByteBufCodecs.BOOL, syncAllPredicate()) }

    @JvmField
    val DASH_DATA =
        register("dash_data") { it.persistent(DashData.CODEC).syncWith(DashData.STREAM_CODEC, syncAllPredicate()) }

    fun <T> register(id: String, builder: Consumer<AttachmentRegistry.Builder<T>>): AttachmentType<T> =
        AttachmentRegistry.create<T>(id(id), builder)
}