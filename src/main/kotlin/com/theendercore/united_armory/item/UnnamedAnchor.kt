package com.theendercore.united_armory.item

import com.theendercore.united_armory.entity.UnnamesAnchorProjectile
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

    override fun use(
        level: Level, player: Player, interactionHand: InteractionHand,
    ): InteractionResultHolder<ItemStack> {
        var result = super.use(level, player, interactionHand)
        if (!result.result.consumesAction()) {
//            val id = player.getAttached(THROWN_ANCHOR)
//            if (id == null) {
            val stack = player.getItemInHand(interactionHand)

            val anchor = UnnamesAnchorProjectile(level, player, stack)
            val viewVec = player.getViewVector(1f).scale(0.5)
            anchor.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 2f, 1f)
            anchor.setPos(
                player.x + (viewVec.x),
                player.eyePosition.y + (viewVec.y) - (anchor.type.dimensions.height / 2),
                player.z + (viewVec.z),
            )
            level.addFreshEntity(anchor)
            result = InteractionResultHolder.success(stack)
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