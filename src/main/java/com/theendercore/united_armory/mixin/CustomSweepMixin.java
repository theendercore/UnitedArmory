package com.theendercore.united_armory.mixin;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.theendercore.united_armory.util.lib.SweepWindshieldKt.getCustomSweepParticle;
import static com.theendercore.united_armory.util.lib.SweepWindshieldKt.getCustomSweepSound;

@Mixin(Player.class)
public abstract class CustomSweepMixin extends LivingEntity {
    protected CustomSweepMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyArg(method = "sweepAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;sendParticles(Lnet/minecraft/core/particles/ParticleOptions;DDDIDDDD)I"))
    ParticleOptions customSwingParticle(ParticleOptions particleOptions) {
        var stack = getWeaponItem();
        if (!stack.isEmpty()) {
            var particle = getCustomSweepParticle(stack);
            if (particle != null) {
                return particle;
            }
        }
        return particleOptions;
    }

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", ordinal = 1))
    SoundEvent customSwingSound(SoundEvent sound) {
        var stack = getWeaponItem();
        if (!stack.isEmpty()) {
            var cSound = getCustomSweepSound(stack);
            if (cSound != null) {
                return cSound.value();
            }
        }
        return sound;
    }

}
