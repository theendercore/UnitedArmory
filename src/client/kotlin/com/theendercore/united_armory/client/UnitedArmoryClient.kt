package com.theendercore.united_armory.client

import com.theendercore.united_armory.client.init.UARenderTypes
import com.theendercore.united_armory.client.renderer.entity.ThrownWeaponRenderer
import com.theendercore.united_armory.client.renderer.entity.UNAnchorRenderer
import com.theendercore.united_armory.init.UAEntityTypes
import com.theendercore.united_armory.init.UAParticleTypes
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.client.particle.AttackSweepParticle
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.server.packs.PackType

@Suppress("unused")
object UnitedArmoryClient {
    fun init() {
        UARenderTypes.init()
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(UAModelHolder)
        sweepParticle(UAParticleTypes.CUSTOM_SWEEP)
        sweepParticle(UAParticleTypes.BLOODLEACH_SWEEP)
        EntityRendererRegistry.register(UAEntityTypes.UNNAMES_ANCHOR, ::UNAnchorRenderer)
        EntityRendererRegistry.register(UAEntityTypes.UN_SPEAR, ::ThrownWeaponRenderer)
        NetheriteShield.init()
    }

    fun sweepParticle(particle: SimpleParticleType) =
        ParticleFactoryRegistry.getInstance().register(particle, AttackSweepParticle::Provider)
}