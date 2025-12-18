package com.theendercore.united_armory.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface AttackTickerAccessor {
    @Accessor("attackStrengthTicker")
    int un_getAttackStrengthTicker();

    @Mutable
    @Accessor("attackStrengthTicker")
    void un_setAttackStrengthTicker(int value);
}
