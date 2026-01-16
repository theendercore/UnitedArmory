package com.theendercore.united_armory.client.renderer.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.projectile.ItemSupplier
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.ItemDisplayContext


@Environment(EnvType.CLIENT)
open class ThrownWeaponRenderer<T>(context: EntityRendererProvider.Context) :
    EntityRenderer<T>(context) where T : Entity, T : ItemSupplier {
    val itemRenderer: ItemRenderer = context.itemRenderer
    override fun getTextureLocation(entity: T?): ResourceLocation = InventoryMenu.BLOCK_ATLAS
    override fun render(
        entity: T?,
        f: Float,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        light: Int,
    ) {
        super.render(entity, f, partialTick, poseStack, bufferSource, light)
        if (entity == null) return
        poseStack.pushPose()
        val offset = entity.type.dimensions.height / 2
        poseStack.translate(0f, offset, 0f)
        poseStack.scale(1.5f, 1.5f, 1.5f)
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.yRot) - 90.0F))
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.xRot) - 135f))
        itemRenderer.renderStatic(
            entity.item, ItemDisplayContext.FIXED, light,
            OverlayTexture.NO_OVERLAY, poseStack, bufferSource, entity.level(), entity.id
        )
        poseStack.popPose()
    }
}

