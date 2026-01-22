package com.theendercore.united_armory.mixin.shield;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AxeItem.class)
public class DisableStrippingMixin {
    @ModifyExpressionValue(method = "playerHasShieldUseIntent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private static boolean tagShieldCheck(boolean original, @Local Player player) {
        return player.getOffhandItem().is(ConventionalItemTags.SHIELD_TOOLS);
    }
}
