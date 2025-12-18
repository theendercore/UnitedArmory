package com.theendercore.united_armory

import com.theendercore.united_armory.init.UADataComponents
import com.theendercore.united_armory.init.UAEntityTypes
import com.theendercore.united_armory.init.UAItems
import com.theendercore.united_armory.init.UATabs
import com.theendercore.united_armory.util.addAttackTicker
import com.theendercore.united_armory.util.attackTicker
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.item.ItemStack
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object UnitedArmory {
    const val MODID = "united_armory"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(UnitedArmory::class.simpleName)

//    @JvmField
//    var config = ConfigApi.registerAndLoadConfig(::UnitedArmoryConfig)

    fun init() {
        log.info("Uniting Armor")
        UADataComponents.init()
        UAItems.init()
        UATabs.init()
        UAEntityTypes.init()
        UseItemCallback.EVENT.register { player, level, hand ->
            if (hand == InteractionHand.MAIN_HAND) {
                if (player is ServerPlayer) {
                    var str = "Held delay: ${player.currentItemAttackStrengthDelay}"
                    str += "\nScale: ${player.getAttackStrengthScale(0f)}"
                    str += "\nTicker: ${player.attackTicker()}"
                    val value = player.currentItemAttackStrengthDelay * player.getAttackStrengthScale(0f)
                    str += "\nValue: $value, Remainder: ${player.currentItemAttackStrengthDelay - value}"
                    player.sendSystemMessage(Component.literal(str))
                }
                player.addAttackTicker(player.currentItemAttackStrengthDelay.toInt() / 2)
            }
            InteractionResultHolder.pass(ItemStack.EMPTY)
        }
    }

    fun id(namespace: String, path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, path)
    fun mc(path: String): ResourceLocation = ResourceLocation.withDefaultNamespace(path)
    fun id(path: String) = id(MODID, path)
}
