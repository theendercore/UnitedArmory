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
            val stack = player.getItemInHand(interactionHand)

            val offset = 1.0
            val viewVec = player.getViewVector(1.0f).normalize().scale(offset)
            val anchor = UnnamesAnchorProjectile(player, viewVec, level)
            anchor.weapon = stack
            anchor.setPos(
                player.x + viewVec.x * offset,
                player.eyePosition.y + (viewVec.y * offset) - (anchor.boundingBox.ysize / 2),
                player.z + viewVec.z * offset
            )
            level.addFreshEntity(anchor)
            result = InteractionResultHolder.success(stack)
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