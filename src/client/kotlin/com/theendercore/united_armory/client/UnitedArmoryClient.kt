package com.theendercore.united_armory.client

import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.server.packs.PackType

@Suppress("unused")
object UnitedArmoryClient {
    fun init() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(UAModelHolder)
        NetheriteShield.init()
    }
}