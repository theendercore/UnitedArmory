package com.theendercore.united_armory.item

import net.minecraft.tags.BlockTags
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier
import net.minecraft.world.item.component.Tool
import net.minecraft.world.level.block.Blocks

class UnnamedAnchor(tier: Tier, properties: Properties) : SwordItem(tier, properties) {
    constructor(properties: Properties) : this(UATiers.UNNAMED_ANCHOR, properties)

    companion object {
        fun toolProps(): Tool = Tool(
            listOf(
                Tool.Rule.minesAndDrops(listOf(Blocks.COBWEB), 15.0f),
                Tool.Rule.overrideSpeed(BlockTags.SWORD_EFFICIENT, 1.5f)
            ), 1.0f, 2
        )
    }
}