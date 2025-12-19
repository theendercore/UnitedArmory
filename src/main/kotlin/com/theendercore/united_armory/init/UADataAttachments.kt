package com.theendercore.united_armory.init

import com.mojang.serialization.Codec
import com.theendercore.united_armory.UnitedArmory.id
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.minecraft.network.codec.ByteBufCodecs

@Suppress("UnstableApiUsage")
object UADataAttachments {
    fun init() = Unit

    @JvmField
    val IFRAME_EFFECT: AttachmentType<Boolean> = AttachmentRegistry.create<Boolean>(id("iframe_effect")) {
        it.persistent(Codec.BOOL).syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all())
    }
}