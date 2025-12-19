package com.theendercore.united_armory.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.theendercore.united_armory.init.UAMobEffects;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    private <T extends Entity> void run(EntityRenderer<T> instance, T entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Operation<Void> original) {
        if (entity instanceof LivingEntity living && living.hasEffect(UAMobEffects.UNNAMED_IFRAMES)) {
            return;
        }
        original.call(instance, entity, f, g, poseStack, multiBufferSource, i);
    }
}
