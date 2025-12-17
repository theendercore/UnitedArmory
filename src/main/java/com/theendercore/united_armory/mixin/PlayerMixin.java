package com.theendercore.united_armory.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.theendercore.united_armory.data.tag.UAItemTags.NON_DISABLEABLE_SHIELD;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "blockUsingShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canDisableShield()Z"))
    private boolean disableDisabling(boolean original) {
        return original && !getMainHandItem().is(NON_DISABLEABLE_SHIELD);
    }

    @ModifyExpressionValue(method = "hurtCurrentlyUsedShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    boolean tagShieldCheck(boolean original) {
        return useItem.is(ConventionalItemTags.SHIELD_TOOLS);
    }
}
