package com.theendercore.united_armory.client.renderer.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import com.theendercore.united_armory.client.init.UARenderTypes
import com.theendercore.united_armory.entity.UnnamesAnchorProjectile
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.projectile.ItemSupplier
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.phys.Vec3
import kotlin.math.acos
import kotlin.math.atan2


@Environment(EnvType.CLIENT)
class UNAnchorRenderer<T>(context: EntityRendererProvider.Context) :
    ThrownWeaponRenderer<T>(context) where T : UnnamesAnchorProjectile, T : ItemSupplier {

    override fun render(
        entity: T?,
        f: Float,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        light: Int,
    ) {
        super.render(entity, f, partialTick, poseStack, bufferSource, light)
        if (entity == null) return
        // Chain renderer
        val owner = entity.owner ?: return
        val yOffset = entity.bbHeight * 0.5f
        poseStack.pushPose()
        poseStack.translate(0f, yOffset, 0f)

        val targetPos = getLerpedPos(owner, partialTick.toDouble(), owner.bbHeight * 0.5)
        val guardianPos = getLerpedPos(entity, partialTick.toDouble(), yOffset.toDouble())
        var dirVector = targetPos.subtract(guardianPos)
        val y = dirVector.length().toFloat() + 0.01f
        dirVector = dirVector.normalize()
        // Rot
        val xpRot = acos(dirVector.y.toFloat())
        val ypRot = atan2(dirVector.z, dirVector.x).toFloat()
        poseStack.mulPose(Axis.YP.rotationDegrees(((PI_F / 2f) - ypRot) * (180f / PI_F)))
        poseStack.mulPose(Axis.XP.rotationDegrees(xpRot * (180f / PI_F)))

        val angle = Mth.lerp(entity.uuid.hashCode() / 10f, -45f, 45f)
        poseStack.mulPose(Axis.YP.rotationDegrees(angle))
        // Pos Calc
        val chainSize = 0.1f
        val x1 = Mth.cos(Math.PI.toFloat()) * chainSize
        val z1 = Mth.sin(Math.PI.toFloat()) * chainSize
        val x2 = Mth.cos(0f) * chainSize
        val z2 = Mth.sin(0f) * chainSize
        val x3 = Mth.cos((Math.PI / 2).toFloat()) * chainSize
        val z3 = Mth.sin((Math.PI / 2).toFloat()) * chainSize
        val x4 = Mth.cos((Math.PI * 3.0 / 2.0).toFloat()) * chainSize
        val z4 = Mth.sin((Math.PI * 3.0 / 2.0).toFloat()) * chainSize

        val chains = 3f / 16f
        // UVs
        val u0 = 0f
        val u1 = chains
        val u2 = chains * 2f

        val v0 = y * 1f // scale of texture
        val v1 = 0f
        // Buffs
        val buffer = bufferSource.getBuffer(CHAIN_RENDER_TYPE)
        val pose = poseStack.last()

        buffer.vertex(pose, x1, y, z1, u0, v0, light)
        buffer.vertex(pose, x1, 0f, z1, u0, v1, light)
        buffer.vertex(pose, x2, 0f, z2, u1, v1, light)
        buffer.vertex(pose, x2, y, z2, u1, v0, light)

        buffer.vertex(pose, x3, y, z3, u1, v0, light)
        buffer.vertex(pose, x3, 0f, z3, u1, v1, light)
        buffer.vertex(pose, x4, 0f, z4, u2, v1, light)
        buffer.vertex(pose, x4, y, z4, u2, v0, light)
        poseStack.popPose()
    }

    fun getLerpedPos(entity: Entity, partialTick: Double, yOffset: Double = 0.0): Vec3 {
        val x = Mth.lerp(partialTick, entity.xOld, entity.x)
        val y = Mth.lerp(partialTick, entity.yOld, entity.y) + yOffset
        val z = Mth.lerp(partialTick, entity.zOld, entity.z)
        return Vec3(x, y, z)
    }


    override fun shouldRender(entity: T?, frustum: Frustum?, d: Double, e: Double, f: Double): Boolean = true
    override fun getTextureLocation(entity: T?): ResourceLocation = InventoryMenu.BLOCK_ATLAS

    companion object {
        private val CHAIN_LOCATION = ResourceLocation.withDefaultNamespace("textures/block/chain.png")
        private val CHAIN_RENDER_TYPE = UARenderTypes.anchor(CHAIN_LOCATION)

        const val PI_F: Float = Math.PI.toFloat()

        fun VertexConsumer.vertex(
            pose: PoseStack.Pose,
            x: Float, y: Float, z: Float,
            u: Float, v: Float, light: Int,
        ) {
            addVertex(pose, x, y, z)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setLight(light)
        }
    }
}

