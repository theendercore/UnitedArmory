package com.theendercore.united_armory.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.theendercore.united_armory.item.Bloodleach;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.theendercore.united_armory.util.MixinKt.doReverseKnockback;

@Mixin(Player.class)
public abstract class BloodleachKBMixin extends LivingEntity {
    protected BloodleachKBMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }


    @ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getKnockback(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;)F"))
    float bloodleachCustomKB(float original, Entity entity, @Local(ordinal = 2) boolean crit) {
        var stack = getWeaponItem();
        if (crit && stack.getItem() instanceof Bloodleach) {
            doReverseKnockback((Player) (Object) this, original, entity);
            return -100;
        }
        return original;
    }
}
