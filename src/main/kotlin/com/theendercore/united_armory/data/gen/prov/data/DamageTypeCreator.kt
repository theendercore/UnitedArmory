package com.theendercore.united_armory.data.gen.prov.data

import com.theendercore.united_armory.data.UADamageTypes
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.damagesource.DamageScaling
import net.minecraft.world.damagesource.DamageType

object DamageTypeCreator {
    fun bootstrap(c: BootstrapContext<DamageType>) {
        // Steel from DamageTypes.class
        c.register(
            UADamageTypes.CUSTOM,
            DamageType(UADamageTypes.CUSTOM.location().path, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 1f)
        )
    }

}