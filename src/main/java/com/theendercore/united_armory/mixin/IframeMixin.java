package com.theendercore.united_armory.mixin;

import com.theendercore.united_armory.init.UAMobEffects;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class IframeMixin {
    @Shadow
    public abstract boolean hasEffect(Holder<MobEffect> holder);

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isSleeping()Z"), cancellable = true)
    void customSwingSound(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        if (this.hasEffect(UAMobEffects.UNNAMED_IFRAMES)) {
            cir.setReturnValue(false);
        }
    }
}
