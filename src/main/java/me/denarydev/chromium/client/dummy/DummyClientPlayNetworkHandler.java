/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client.dummy;

import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.entry.RegistryEntryInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.damage.DamageEffects;
import net.minecraft.entity.damage.DamageScaling;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.damage.DeathMessageType;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;

public class DummyClientPlayNetworkHandler extends ClientPlayNetworkHandler {

    private static DummyClientPlayNetworkHandler instance;

    public static DummyClientPlayNetworkHandler getInstance() {
        if (instance == null) instance = new DummyClientPlayNetworkHandler();
        return instance;
    }

    private final DynamicRegistryManager dummyRegistryManager;
    private final DummyPlayerListEntry dummyPlayerListEntry;

    private DummyClientPlayNetworkHandler() {
        super(MinecraftClient.getInstance(), new ClientConnection(NetworkSide.CLIENTBOUND), new ClientConnectionState(MinecraftClient.getInstance().getGameProfile(), null, null, FeatureSet.of(FeatureFlags.VANILLA), null, null, null, null, null, false, null, null));
        this.dummyRegistryManager = dummyRegistryManager();
        this.dummyPlayerListEntry = new DummyPlayerListEntry();
    }

    @NotNull
    @Override
    public DynamicRegistryManager.Immutable getRegistryManager() {
        return dummyRegistryManager.toImmutable();
    }

    @Nullable
    @Override
    public PlayerListEntry getPlayerListEntry(@NotNull UUID uuid) {
        return dummyPlayerListEntry;
    }

    private DynamicRegistryManager dummyRegistryManager() {
        final var registries = new ArrayList<Registry<?>>();
        final var damageTypeRegistry = new SimpleRegistry<>(RegistryKeys.DAMAGE_TYPE, Lifecycle.stable(), false);
        damageTypeRegistry.add(DamageTypes.IN_FIRE, new DamageType("inFire", 0.1F, DamageEffects.BURNING), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.CAMPFIRE, new DamageType("campfire", 0), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.LIGHTNING_BOLT, new DamageType("lightningBolt", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.ON_FIRE, new DamageType("onFire", 0, DamageEffects.BURNING), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.LAVA, new DamageType("lava", 0.1F, DamageEffects.BURNING), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.HOT_FLOOR, new DamageType("hotFloor", 0.1F, DamageEffects.BURNING), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.IN_WALL, new DamageType("inWall", 0), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.CRAMMING, new DamageType("cramming", 0), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.DROWN, new DamageType("drown", 0, DamageEffects.DROWNING), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.STARVE, new DamageType("starve", 0), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.CACTUS, new DamageType("cactus", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.FALL, new DamageType("fall", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0, DamageEffects.HURT, DeathMessageType.FALL_VARIANTS), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.FLY_INTO_WALL, new DamageType("flyIntoWall", 0), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.OUT_OF_WORLD, new DamageType("outOfWorld", 0), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.GENERIC, new DamageType("generic", 0), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.MAGIC, new DamageType("magic", 0), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.WITHER, new DamageType("wither", 0), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.DRAGON_BREATH, new DamageType("dragonBreath", 0), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.DRY_OUT, new DamageType("dryout", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.SWEET_BERRY_BUSH, new DamageType("sweetBerryBush", 0.1F, DamageEffects.POKING), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.FREEZE, new DamageType("freeze", 0, DamageEffects.FREEZING), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.STALAGMITE, new DamageType("stalagmite", 0), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.FALLING_BLOCK, new DamageType("fallingBlock", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.FALLING_ANVIL, new DamageType("anvil", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.FALLING_STALACTITE, new DamageType("fallingStalactite", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.STING, new DamageType("sting", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.MOB_ATTACK, new DamageType("mob", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.MOB_ATTACK_NO_AGGRO, new DamageType("mob", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.PLAYER_ATTACK, new DamageType("player", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.ARROW, new DamageType("arrow", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.TRIDENT, new DamageType("trident", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.MOB_PROJECTILE, new DamageType("mob", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.SPIT, new DamageType("mob", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.FIREWORKS, new DamageType("fireworks", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.UNATTRIBUTED_FIREBALL, new DamageType("onFire", 0.1F, DamageEffects.BURNING), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.FIREBALL, new DamageType("fireball", 0.1F, DamageEffects.BURNING), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.WITHER_SKULL, new DamageType("witherSkull", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.THROWN, new DamageType("thrown", 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.INDIRECT_MAGIC, new DamageType("indirectMagic", 0.0F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.THORNS, new DamageType("thorns", 0.1F, DamageEffects.THORNS), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.EXPLOSION, new DamageType("explosion", DamageScaling.ALWAYS, 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.PLAYER_EXPLOSION, new DamageType("explosion.player", DamageScaling.ALWAYS, 0.1F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.SONIC_BOOM, new DamageType("sonic_boom", DamageScaling.ALWAYS, 0.0F), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.BAD_RESPAWN_POINT, new DamageType("badRespawnPoint", DamageScaling.ALWAYS, 0.1F, DamageEffects.HURT, DeathMessageType.INTENTIONAL_GAME_DESIGN), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.OUTSIDE_BORDER, new DamageType("outsideBorder", 0), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.GENERIC_KILL, new DamageType("genericKill", 0), RegistryEntryInfo.DEFAULT);
        damageTypeRegistry.add(DamageTypes.WIND_CHARGE, new DamageType("mob", 0.1F), RegistryEntryInfo.DEFAULT);
        registries.add(damageTypeRegistry);

        final var biomeRegistry = new SimpleRegistry<>(RegistryKeys.BIOME, Lifecycle.stable(), false);
        final var fakePlains = new Biome.Builder()
                .temperature(0).downfall(0)
                .effects(new BiomeEffects.Builder()
                        .fogColor(0)
                        .waterColor(0)
                        .waterFogColor(0)
                        .skyColor(0)
                        .build())
                .spawnSettings(new SpawnSettings.Builder().build())
                .generationSettings(new GenerationSettings.Builder().build())
                .build();
        biomeRegistry.add(BiomeKeys.PLAINS, fakePlains, RegistryEntryInfo.DEFAULT);
        registries.add(biomeRegistry);

        return new DynamicRegistryManager.ImmutableImpl(registries);
    }

    public static class DummyPlayerListEntry extends PlayerListEntry {
        private final SkinTextures skinWithoutCape;

        public DummyPlayerListEntry() {
            super(MinecraftClient.getInstance().getGameProfile(), false);
            final var currentSkin = getSkinTextures();
            this.skinWithoutCape = new SkinTextures(currentSkin.texture(), currentSkin.textureUrl(), null, currentSkin.elytraTexture(), currentSkin.model(), currentSkin.secure()); //TODO: Fix cape texture rendering
        }

        @Override
        public @NotNull SkinTextures getSkinTextures() {
            return super.getSkinTextures();
        }
    }
}
