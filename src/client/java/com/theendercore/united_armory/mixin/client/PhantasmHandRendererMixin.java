package com.theendercore.united_armory.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.theendercore.united_armory.init.UADataAttachments;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerRenderer.class)
public class PhantasmHandRendererMixin {
    @SuppressWarnings("UnstableApiUsage")
    @WrapOperation(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;entitySolid(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"))
    private RenderType renderHandSolid(ResourceLocation id, Operation<RenderType> original, @Local(argsOnly = true) AbstractClientPlayer entity) {
        var effected = entity.getAttached(UADataAttachments.PHANTASM_ATTACHMENT);
        if (Boolean.TRUE.equals(effected)) {
            return RenderType.eyes(id);
        }
        return original.call(id);
    }

    @SuppressWarnings("UnstableApiUsage")
    @WrapOperation(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;entityTranslucent(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"))
    private RenderType renderHandTrans(ResourceLocation id, Operation<RenderType> original, @Local(argsOnly = true) AbstractClientPlayer entity) {
        var effected = entity.getAttached(UADataAttachments.PHANTASM_ATTACHMENT);
        if (Boolean.TRUE.equals(effected)) {
            return RenderType.eyes(id);
        }
        return original.call(id);
    }
}
