package com.theendercore.united_armory.item

import com.theendercore.united_armory.entity.UnnamesAnchorProjectile2
import net.minecraft.tags.BlockTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier
import net.minecraft.world.item.component.Tool
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks

class UnnamedAnchor(tier: Tier, properties: Properties) : SwordItem(tier, properties) {
    constructor(properties: Properties) : this(UATiers.UNNAMED_ANCHOR, properties)

    @Suppress("UnstableApiUsage")
    override fun use(
        level: Level, player: Player, interactionHand: InteractionHand,
    ): InteractionResultHolder<ItemStack> {
        var result = super.use(level, player, interactionHand)
        if (!result.result.consumesAction()) {
//            val id = player.getAttached(THROWN_ANCHOR)
//            if (id == null) {
            val stack = player.getItemInHand(interactionHand)

//            val anchor = UnnamesAnchorProjectile(player, level, player.getViewVector(1.0f), stack)
//            if (player.deltaMovement.length() > 0.1) {
//                anchor.deltaMovement = anchor.deltaMovement.add(player.deltaMovement)
//                anchor.hasImpulse = true
//            }
//            level.addFreshEntity(anchor)
//            player.setAttached(THROWN_ANCHOR, anchor.id)
            val anchor2 = UnnamesAnchorProjectile2(level, player, stack)
            val dir = player.getViewVector(1f)
            anchor2.shootFromRotation(player,player.xRot, player.yRot , 0.0F, 2f, 1f)
            anchor2.setPos(
                player.x + dir.x,
                player.eyePosition.y + (dir.y) - (anchor2.type.dimensions.height / 2),
                player.z + dir.z,
            )
            level.addFreshEntity(anchor2)
            result = InteractionResultHolder.success(stack)
//            } else {
//                val anchor = level.getEntity(id)
//                if (anchor is UnnamesAnchorProjectile && anchor.state == UnnamesAnchorProjectile.AnchorState.HOLDING) {
//                    anchor.kill()
//                }
//            }
        }
        return result
    }

    companion object {
        fun toolProps(): Tool = Tool(
            listOf(
                Tool.Rule.minesAndDrops(listOf(Blocks.COBWEB), 15.0f),
                Tool.Rule.overrideSpeed(BlockTags.SWORD_EFFICIENT, 1.5f)
            ), 1.0f, 2
        )
    }
}