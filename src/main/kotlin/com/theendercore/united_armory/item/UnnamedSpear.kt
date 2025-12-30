package com.theendercore.united_armory.item

import com.theendercore.united_armory.init.UAMobEffects
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier
import net.minecraft.world.level.Level

class UnnamedSpear(tier: Tier, properties: Properties) : SwordItem(tier, properties) {
    constructor(properties: Properties) : this(UATiers.UNNAMED_SPEAR, properties)

    override fun use(
        level: Level,
        player: Player,
        interactionHand: InteractionHand,
    ): InteractionResultHolder<ItemStack> {
        var result = super.use(level, player, interactionHand)
        if (!result.result.consumesAction()) {
            val stack = player.getItemInHand(interactionHand)
            val length = 8
//            player.startAutoSpinAttack(length, 0f, stack)
            player.addEffect(MobEffectInstance(UAMobEffects.PHANTASM, length))
            player.deltaMovement =
                player.calculateViewVector(player.xRot, player.yRot).multiply(1.25, 0.0, 1.25).add(0.0, 0.25, 0.0)
            player.hasImpulse = true
//            player.fallDistance = 0f // TODO ask if to keep this
            result = InteractionResultHolder.success(stack)
        }
        return result
    }
}