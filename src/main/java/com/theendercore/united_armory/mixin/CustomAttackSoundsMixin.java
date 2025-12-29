package com.theendercore.united_armory.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.theendercore.united_armory.util.lib.SweepWindshieldKt.*;

@Mixin(Player.class)
public abstract class CustomAttackSoundsMixin extends LivingEntity {
    protected CustomAttackSoundsMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }


    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", ordinal = 2))
    SoundEvent customCritSound(SoundEvent sound) {
        var customSound = getCritSound(getWeaponItem());
        if (customSound != null) {
            return customSound.value();
        }
        return sound;
    }

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", ordinal = 3))
    SoundEvent customStringSound(SoundEvent sound) {
        var customSound = getStringSound(getWeaponItem());
        if (customSound != null) {
            return customSound.value();
        }
        return sound;
    }

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V", ordinal = 4))
    SoundEvent customWeakSound(SoundEvent sound) {
        var customSound = getWeakSound(getWeaponItem());
        if (customSound != null) {
            return customSound.value();
        }
        return sound;
    }

}
