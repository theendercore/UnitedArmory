package com.theendercore.united_armory.util

import com.theendercore.united_armory.mixin.AttackTickerAccessor
import net.minecraft.world.entity.LivingEntity

fun LivingEntity.attackTicker(): Int = (this as AttackTickerAccessor).un_getAttackStrengthTicker()
fun LivingEntity.setAttackTicker(value: Int) = (this as AttackTickerAccessor).un_setAttackStrengthTicker(value)
fun LivingEntity.addAttackTicker(value: Int) = setAttackTicker(attackTicker() + value)

