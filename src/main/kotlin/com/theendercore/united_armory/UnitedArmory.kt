package com.theendercore.united_armory

import me.fzzyhmstrs.fzzy_config.api.ConfigApi
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.theendercore.united_armory.config.UnitedArmoryConfig
import com.theendercore.united_armory.init.UADataComponents
import com.theendercore.united_armory.init.UAEntityTypes
import com.theendercore.united_armory.init.UAItems
import com.theendercore.united_armory.init.UATabs

@Suppress("unused")
object UnitedArmory {
    const val MODID = "united_armory"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(UnitedArmory::class.simpleName)

    @JvmField
    var config = ConfigApi.registerAndLoadConfig(::UnitedArmoryConfig)

    fun init() {
        log.info("Hello from Common")
        UADataComponents.init()
        UAItems.init()
        UATabs.init()
        UAEntityTypes.init()
    }

    fun id(namespace: String, path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, path)
    fun mc(path: String): ResourceLocation = ResourceLocation.withDefaultNamespace(path)
    fun id(path: String) = id(MODID, path)
}
