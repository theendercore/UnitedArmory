package com.theendercore.united_armory.item

import com.theendercore.united_armory.data.UADamageTypes
import com.theendercore.united_armory.data.UADamageTypes.hurtByType
import com.theendercore.united_armory.data.tag.UAEntityTypeTags.CANT_BLOODSTEAL
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier

// Name ideas
// Bloodleach
// Bloodless Scythe
// Bloodlet
// Bloodletter
class UnnamedScythe(tier: Tier, properties: Properties) : SwordItem(tier, properties) {
    constructor(properties: Properties) : this(UATiers.UNNAMED_SCYTHE, properties)

    override fun postHurtEnemy(stack: ItemStack, victim: LivingEntity, player: LivingEntity) {
        super.postHurtEnemy(stack, victim, player)
        if (victim.type.`is`(CANT_BLOODSTEAL)) return

        if (victim.isAlive) {
            victim.hurtByType(UADamageTypes.UNNAMED_BLOODSTEAL, 1f, player, player)
        }
        player.heal(1f)
    }
}