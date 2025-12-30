package com.theendercore.united_armory.mixin.phantasm;

import com.theendercore.united_armory.effect.RemovableMobEffect;
import com.theendercore.united_armory.init.UAMobEffects;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class PhantasmLivingEntityMixin {
    @Shadow
    public abstract boolean hasEffect(Holder<MobEffect> holder);

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isSleeping()Z"), cancellable = true)
    void customSwingSound(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        if (hasEffect(UAMobEffects.PHANTASM)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "onEffectRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;refreshDirtyAttributes()V"))
    void customSwingSound(MobEffectInstance mobEffectInstance, CallbackInfo ci) {
        if (mobEffectInstance.getEffect().value() instanceof RemovableMobEffect effect) {
            effect.onEffectRemove(((LivingEntity) (Object) this));
        }
    }
}
