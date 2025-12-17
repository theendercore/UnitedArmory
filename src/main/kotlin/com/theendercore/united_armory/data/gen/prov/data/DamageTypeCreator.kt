package com.theendercore.united_armory.data.gen.prov.data

import com.theendercore.united_armory.data.UADamageTypes
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.damagesource.DamageScaling
import net.minecraft.world.damagesource.DamageType

object DamageTypeCreator {
    fun bootstrap(c: BootstrapContext<DamageType>) = c.apply {
        // Steel from DamageTypes.class
        register(
            UADamageTypes.CUSTOM,
            DamageType(UADamageTypes.CUSTOM.location().path, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 1f)
        )
        register(
            UADamageTypes.UNNAMED_BLOODSTEAL,
            DamageType(UADamageTypes.UNNAMED_BLOODSTEAL.location().path, DamageScaling.NEVER, 1f)
        )
    }

}