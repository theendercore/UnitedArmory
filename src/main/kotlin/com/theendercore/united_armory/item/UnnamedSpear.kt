package com.theendercore.united_armory.item

import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier

class UnnamedSpear(tier: Tier, properties: Properties) : SwordItem(tier, properties) {
    constructor(properties: Properties) : this(UATiers.UNNAMED_SPEAR, properties)
}