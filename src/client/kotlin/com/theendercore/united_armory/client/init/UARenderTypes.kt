package com.theendercore.united_armory.client.init

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.Util
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderStateShard.LIGHTMAP
import net.minecraft.client.renderer.RenderStateShard.NO_TRANSPARENCY
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import java.util.function.BiFunction

object UARenderTypes {
    val ANCHOR_SHADER_SHARD = RenderStateShard.ShaderStateShard(GameRenderer::getParticleShader)

    val ANCHOR_RENDER_TYPE: BiFunction<ResourceLocation, Boolean, RenderType> =
        Util.memoize<ResourceLocation, Boolean, RenderType> { texture, outline ->
            val compositeState = RenderType.CompositeState.builder()
                .setShaderState(ANCHOR_SHADER_SHARD)
                .setTextureState(RenderStateShard.TextureStateShard(texture, false, false))
                .setTransparencyState(NO_TRANSPARENCY)
                .setCullState(RenderType.NO_CULL)
                .setOverlayState(RenderType.OVERLAY)
                .setLightmapState(LIGHTMAP)
                .createCompositeState(outline)
            RenderType.create(
                "subtle_effects:entity_particle_translucent",
                DefaultVertexFormat.PARTICLE,
                VertexFormat.Mode.QUADS,
                1536,
                true,
                true,
                compositeState
            )
        }

    fun anchor(texture: ResourceLocation, outline: Boolean = true) = ANCHOR_RENDER_TYPE.apply(texture, outline)
    fun init() = Unit
}