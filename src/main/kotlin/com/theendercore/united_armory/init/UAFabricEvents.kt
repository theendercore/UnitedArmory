package com.theendercore.united_armory.init

import com.theendercore.united_armory.init.UADataAttachments.THROWN_ANCHOR
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents

object UAFabricEvents {
    @Suppress("UnstableApiUsage")
    fun init() {
        ServerPlayerEvents.JOIN.register { it.removeAttached(THROWN_ANCHOR) }
        ServerPlayerEvents.LEAVE.register { it.removeAttached(THROWN_ANCHOR) }
    }

}