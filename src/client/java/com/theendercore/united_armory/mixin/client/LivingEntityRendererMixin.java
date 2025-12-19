package com.theendercore.united_armory.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.theendercore.united_armory.init.UADataAttachments;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @SuppressWarnings({"UnstableApiUsage", "UnresolvedLocalCapture"})
    @ModifyReturnValue(method = "getRenderType", at = @At("RETURN"))
    private RenderType run(@Nullable RenderType original, LivingEntity entity, @Local ResourceLocation id) {
        var effected = entity.getAttached(UADataAttachments.IFRAME_EFFECT);
        if (Boolean.TRUE.equals(effected)) {
            return RenderType.eyes(id);
        }
        return original;
    }
}
