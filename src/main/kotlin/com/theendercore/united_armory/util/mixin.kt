package com.theendercore.united_armory.util

import com.theendercore.united_armory.mixin.AttackTickerAccessor
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import kotlin.math.min

fun LivingEntity.attackTicker(): Int = (this as AttackTickerAccessor).un_getAttackStrengthTicker()
fun LivingEntity.setAttackTicker(value: Int) = (this as AttackTickerAccessor).un_setAttackStrengthTicker(value)
fun LivingEntity.addAttackTicker(value: Int) = setAttackTicker(attackTicker() + value)


fun doReverseKnockback(player: Player, original: Float, entity: Entity) {
    val kb = -(original + 1f)
    if (entity is LivingEntity) {
        entity.cKnockback(
            kb * 0.5,
            Mth.sin((player.yRot * (Math.PI / 180.0)).toFloat()).toDouble(),
            (-Mth.cos((player.yRot * (Math.PI / 180.0)).toFloat())).toDouble()
        )
    } else {
        entity.push(
            (-Mth.sin(player.yRot * (Math.PI / 180f).toFloat()) * kb * 0.5f).toDouble(),
            0.1,
            (Mth.cos(player.yRot * (Math.PI / 180.0).toFloat()) * kb * 0.5f).toDouble()
        )
    }
    player.deltaMovement = player.deltaMovement.multiply(0.6, 1.0, 0.6)
    player.setSprinting(false)
}

fun LivingEntity.cKnockback(d: Double, e: Double, f: Double) {
    var d = d
    var e = e
    var f = f
    d *= 1.0 - getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)
    this.hasImpulse = true
    val vec3 = deltaMovement

    while (e * e + f * f < 1.0E-5f) {
        e = (Math.random() - Math.random()) * 0.01
        f = (Math.random() - Math.random()) * 0.01
    }

    val vec32 = Vec3(e, 0.0, f).normalize().scale(d)
    setDeltaMovement(
        vec3.x / 2.0 - vec32.x,
        if (onGround()) min(0.4, vec3.y / 2.0 - d) else vec3.y,
        vec3.z / 2.0 - vec32.z
    )
}