package com.theendercore.united_armory.item

import com.theendercore.united_armory.entity.UnSpearProjectile
import com.theendercore.united_armory.init.UAMobEffects
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier
import net.minecraft.world.level.Level

class UnnamedSpear(tier: Tier, properties: Properties) : SwordItem(tier, properties) {
    constructor(properties: Properties) : this(UATiers.UNNAMED_SPEAR, properties)

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        var result = super.use(level, player, hand)
        if (!result.result.consumesAction()) {
            val phantomLunge = false
            val weapon = player.getItemInHand(hand)
            if (phantomLunge) {
                val length = 8
//            player.startAutoSpinAttack(length, 0f, stack)
                player.addEffect(MobEffectInstance(UAMobEffects.PHANTASM, length))
                player.deltaMovement =
                    player.calculateViewVector(player.xRot, player.yRot).multiply(1.25, 0.0, 1.25).add(0.0, 0.25, 0.0)
                player.hasImpulse = true
//            player.fallDistance = 0f // TODO ask if to keep this
            } else {
                val spear = UnSpearProjectile(level, player, weapon)
                val viewVec = player.getViewVector(1f).scale(0.5)
                spear.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 2f, 1f)
                spear.setPos(
                    player.x + (viewVec.x),
                    player.eyePosition.y + (viewVec.y) - (spear.type.dimensions.height / 2),
                    player.z + (viewVec.z),
                )
                level.addFreshEntity(spear)
            }
            weapon.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand))

            player.awardStat(Stats.ITEM_USED.get(this))

            result = InteractionResultHolder.success(weapon)
        }
        return result
    }
}