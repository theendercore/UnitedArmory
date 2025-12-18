package com.theendercore.united_armory.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

import static com.theendercore.united_armory.util.lib.GreenModifiersKt.GREEN_MODS;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "addModifierTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;operation()Lnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;", ordinal = 0))
    private static void tagShieldCheck(Consumer<Component> consumer, @Nullable Player player, Holder<Attribute> holder, AttributeModifier attributeModifier, CallbackInfo ci,
                                       @Local(ordinal = 0) LocalDoubleRef amount, @Local LocalBooleanRef isGreen) {
        if (player == null) return;
        var mod = GREEN_MODS.get(attributeModifier.id());
        if (mod == null) return;
        var value = amount.get();
        switch (attributeModifier.operation()) {
            case ADD_VALUE -> value += player.getAttributeBaseValue(mod);
            case ADD_MULTIPLIED_BASE, ADD_MULTIPLIED_TOTAL -> value *= player.getAttributeBaseValue(mod);
        }
        amount.set(value);
        isGreen.set(true);
    }
}
