package com.theendercore.united_armory.client

import com.theendercore.united_armory.UnitedArmory.id
import com.theendercore.united_armory.init.UAItems
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.minecraft.client.renderer.Sheets
import net.minecraft.client.renderer.blockentity.BannerRenderer
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.client.resources.model.Material
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.entity.BannerPatternLayers

object NetheriteShield {
    val SHIELD_BASE = Material(Sheets.SHIELD_SHEET, id("entity/shield_base"))
    val NO_PATTERN_SHIELD = Material(Sheets.SHIELD_SHEET, id("entity/shield_base_nopattern"))

    fun init() {
        ItemProperties.register(
            UAItems.NETHERITE_SHIELD, id("blocking")
        ) { stack, _, entity, _ -> if (entity != null && entity.isUsingItem && entity.getUseItem() == stack) 1f else 0f }

        BuiltinItemRendererRegistry.INSTANCE.register(UAItems.NETHERITE_SHIELD) { stack, _, poseStack, bufferSrc, light, overlay ->
            val patterns = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
            val color = stack.get(DataComponents.BASE_COLOR)
            val hasLayers = !patterns.layers().isEmpty() || color != null
            poseStack.pushPose()
            poseStack.scale(1.0f, -1.0f, -1.0f)
            val material = if (hasLayers) SHIELD_BASE else NO_PATTERN_SHIELD
            val vertexConsumer = material.sprite().wrap(
                ItemRenderer.getFoilBufferDirect(
                    bufferSrc, UAModelHolder.shieldModel.renderType(material.atlasLocation()), true, stack.hasFoil()
                )
            )
            UAModelHolder.shieldModel.handle().render(poseStack, vertexConsumer, light, overlay)
            if (hasLayers) {
                BannerRenderer.renderPatterns(
                    poseStack, bufferSrc, light, overlay,
                    UAModelHolder.shieldModel.plate(), material, false,
                    (color ?: DyeColor.WHITE), patterns, stack.hasFoil()
                )
            } else {
                UAModelHolder.shieldModel.plate().render(poseStack, vertexConsumer, light, overlay)
            }
            poseStack.popPose()
        }
    }
}