package com.theendercore.united_armory.client

import com.theendercore.united_armory.UnitedArmory.id
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ShieldModel
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.ResourceManagerReloadListener

object UAModelHolder : ResourceManagerReloadListener, IdentifiableResourceReloadListener {
    lateinit var shieldModel: ShieldModel

    override fun getFabricId(): ResourceLocation = id("custom_model_loader")
    override fun onResourceManagerReload(resourceManager: ResourceManager) {
        shieldModel = ShieldModel(modelSet().bakeLayer(ModelLayers.SHIELD))
    }

    fun modelSet(): EntityModelSet = Minecraft.getInstance().entityModels
}