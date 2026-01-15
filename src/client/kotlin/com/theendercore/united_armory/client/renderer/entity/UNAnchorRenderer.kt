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
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.projectile.ItemSupplier
import net.minecraft.world.item.ItemDisplayContext


@Environment(EnvType.CLIENT)
class UNAnchorRenderer<T>(context: EntityRendererProvider.Context) :
    EntityRenderer<T?>(context) where T : Entity, T : ItemSupplier {
    private val itemRenderer: ItemRenderer = context.itemRenderer


    override fun render(
        entity: T?,
        f: Float,
        partialTick: Float,
        stack: PoseStack,
        buffer: MultiBufferSource?,
        light: Int,
    ) {
        if (entity!!.tickCount >= 2 || !(entityRenderDispatcher.camera.entity.distanceToSqr(entity) < MIN_CAMERA_DISTANCE_SQUARED)) {
            stack.pushPose()
            val offset = entity.type.dimensions.height / 2
            stack.translate(0f, offset, 0f)
            stack.scale(1.5f, 1.5f, 1.5f)
            stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.yRot) - 90.0F));
            stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.xRot) - 135f));
            itemRenderer.renderStatic(
                entity.item, ItemDisplayContext.FIXED, light,
                OverlayTexture.NO_OVERLAY, stack, buffer, entity.level(), entity.id
            )
            stack.popPose()
            super.render(entity, f, partialTick, stack, buffer, light)
        }
    }


    override fun getTextureLocation(entity: T?): ResourceLocation = TextureAtlas.LOCATION_BLOCKS

    companion object {
        private const val MIN_CAMERA_DISTANCE_SQUARED = 12.25f
    }
}

