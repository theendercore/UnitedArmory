package com.theendercore.united_armory.config

import com.theendercore.united_armory.UnitedArmory
import com.theendercore.united_armory.UnitedArmory.id
import me.fzzyhmstrs.fzzy_config.annotations.NonSync
import me.fzzyhmstrs.fzzy_config.config.Config
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber

@Suppress("unused")
class UnitedArmoryConfig : Config(id(UnitedArmory.MODID)) {
    var groupName = ConfigGroup("group_id", false)
    var commonEntry = ValidatedInt(0, 10, -10, ValidatedNumber.WidgetType.TEXTBOX_WITH_BUTTONS)
    @NonSync
    @ConfigGroup.Pop
    var clientEntry = true
}