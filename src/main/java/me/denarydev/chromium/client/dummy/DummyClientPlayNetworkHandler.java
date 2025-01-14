/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client.dummy;

import com.mojang.serialization.Lifecycle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.entity.passive.WolfVariant;
import net.minecraft.entity.passive.WolfVariants;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Environment(EnvType.CLIENT)
public final class DummyClientPlayNetworkHandler extends ClientPlayNetworkHandler {

    private static DummyClientPlayNetworkHandler instance;

    public static DummyClientPlayNetworkHandler getInstance() {
        if (instance == null) instance = new DummyClientPlayNetworkHandler();
        return instance;
    }

    private static final DynamicRegistryManager dummyRegistryManager = dummyRegistryManager();
    private final DummyPlayerListEntry dummyPlayerListEntry = new DummyPlayerListEntry();

    private DummyClientPlayNetworkHandler() {
        super(MinecraftClient.getInstance(), new ClientConnection(NetworkSide.CLIENTBOUND), new ClientConnectionState(MinecraftClient.getInstance().getGameProfile(), null, dummyRegistryManager.toImmutable(), FeatureSet.of(FeatureFlags.VANILLA), null, null, null, null, null, null, null));
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

    private static DynamicRegistryManager dummyRegistryManager() {
        final var registries = new ArrayList<Registry<?>>();

        registries.add(dummyDamageTypesRegistry());
        registries.add(dummyBiomesRegistry());
        registries.add(dummyWolfVariantsRegistry());
        registries.add(dummyItemRegistry());

        return new DynamicRegistryManager.ImmutableImpl(registries);
    }

    @NotNull
    private static Registry<Item> dummyItemRegistry() {
        final var registry = new SimpleRegistry<>(RegistryKeys.ITEM, Lifecycle.stable(), false);

        registry.add(RegistryKey.of(RegistryKeys.ITEM, Identifier.ofVanilla("air")), Items.AIR, RegistryEntryInfo.DEFAULT);

        return registry;
    }

    @NotNull
    private static Registry<DamageType> dummyDamageTypesRegistry() {
        final var registry = new SimpleRegistry<>(RegistryKeys.DAMAGE_TYPE, Lifecycle.stable(), false);

        registry.add(DamageTypes.IN_FIRE, new DamageType("inFire", 0.1F, DamageEffects.BURNING), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.CAMPFIRE, new DamageType("campfire", 0), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.LIGHTNING_BOLT, new DamageType("lightningBolt", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.ON_FIRE, new DamageType("onFire", 0, DamageEffects.BURNING), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.LAVA, new DamageType("lava", 0.1F, DamageEffects.BURNING), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.HOT_FLOOR, new DamageType("hotFloor", 0.1F, DamageEffects.BURNING), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.IN_WALL, new DamageType("inWall", 0), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.CRAMMING, new DamageType("cramming", 0), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.DROWN, new DamageType("drown", 0, DamageEffects.DROWNING), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.STARVE, new DamageType("starve", 0), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.CACTUS, new DamageType("cactus", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.FALL, new DamageType("fall", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0, DamageEffects.HURT, DeathMessageType.FALL_VARIANTS), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.ENDER_PEARL, new DamageType("fall", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F, DamageEffects.HURT, DeathMessageType.FALL_VARIANTS), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.FLY_INTO_WALL, new DamageType("flyIntoWall", 0), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.OUT_OF_WORLD, new DamageType("outOfWorld", 0), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.GENERIC, new DamageType("generic", 0), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.MAGIC, new DamageType("magic", 0), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.WITHER, new DamageType("wither", 0), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.DRAGON_BREATH, new DamageType("dragonBreath", 0), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.DRY_OUT, new DamageType("dryout", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.SWEET_BERRY_BUSH, new DamageType("sweetBerryBush", 0.1F, DamageEffects.POKING), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.FREEZE, new DamageType("freeze", 0, DamageEffects.FREEZING), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.STALAGMITE, new DamageType("stalagmite", 0), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.FALLING_BLOCK, new DamageType("fallingBlock", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.FALLING_ANVIL, new DamageType("anvil", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.FALLING_STALACTITE, new DamageType("fallingStalactite", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.STING, new DamageType("sting", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.MOB_ATTACK, new DamageType("mob", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.MOB_ATTACK_NO_AGGRO, new DamageType("mob", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.PLAYER_ATTACK, new DamageType("player", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.ARROW, new DamageType("arrow", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.TRIDENT, new DamageType("trident", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.MOB_PROJECTILE, new DamageType("mob", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.SPIT, new DamageType("mob", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.FIREWORKS, new DamageType("fireworks", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.UNATTRIBUTED_FIREBALL, new DamageType("onFire", 0.1F, DamageEffects.BURNING), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.FIREBALL, new DamageType("fireball", 0.1F, DamageEffects.BURNING), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.WITHER_SKULL, new DamageType("witherSkull", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.THROWN, new DamageType("thrown", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.INDIRECT_MAGIC, new DamageType("indirectMagic", 0.0F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.THORNS, new DamageType("thorns", 0.1F, DamageEffects.THORNS), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.EXPLOSION, new DamageType("explosion", DamageScaling.ALWAYS, 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.PLAYER_EXPLOSION, new DamageType("explosion.player", DamageScaling.ALWAYS, 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.SONIC_BOOM, new DamageType("sonic_boom", DamageScaling.ALWAYS, 0.0F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.BAD_RESPAWN_POINT, new DamageType("badRespawnPoint", DamageScaling.ALWAYS, 0.1F, DamageEffects.HURT, DeathMessageType.INTENTIONAL_GAME_DESIGN), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.OUTSIDE_BORDER, new DamageType("outsideBorder", 0), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.GENERIC_KILL, new DamageType("genericKill", 0), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.WIND_CHARGE, new DamageType("mob", 0.1F), RegistryEntryInfo.DEFAULT);
        registry.add(DamageTypes.MACE_SMASH, new DamageType("maceSmash", 0.1F), RegistryEntryInfo.DEFAULT);

        return registry;
    }

    @NotNull
    private static SimpleRegistry<Biome> dummyBiomesRegistry() {
        final var registry = new SimpleRegistry<>(RegistryKeys.BIOME, Lifecycle.stable(), false);

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
        registry.add(BiomeKeys.PLAINS, fakePlains, RegistryEntryInfo.DEFAULT);

        return registry;
    }

    @NotNull
    private static Registry<WolfVariant> dummyWolfVariantsRegistry() {
        final var registry = new SimpleRegistry<>(RegistryKeys.WOLF_VARIANT, Lifecycle.stable(), false);
        registerWolfVariant(registry, WolfVariants.PALE, "wolf");
        registerWolfVariant(registry, WolfVariants.SPOTTED, "wolf_spotted");
        registerWolfVariant(registry, WolfVariants.SNOWY, "wolf_snowy");
        registerWolfVariant(registry, WolfVariants.BLACK, "wolf_black");
        registerWolfVariant(registry, WolfVariants.ASHEN, "wolf_ashen");
        registerWolfVariant(registry, WolfVariants.RUSTY, "wolf_rusty");
        registerWolfVariant(registry, WolfVariants.WOODS, "wolf_woods");
        registerWolfVariant(registry, WolfVariants.CHESTNUT, "wolf_chestnut");
        registerWolfVariant(registry, WolfVariants.STRIPED, "wolf_striped");

        return registry;
    }

    private static void registerWolfVariant(SimpleRegistry<WolfVariant> registry, RegistryKey<WolfVariant> key, String textureName) {
        Identifier identifier = Identifier.ofVanilla("entity/wolf/" + textureName);
        Identifier identifier2 = Identifier.ofVanilla("entity/wolf/" + textureName + "_tame");
        Identifier identifier3 = Identifier.ofVanilla("entity/wolf/" + textureName + "_angry");
        registry.add(key, new WolfVariant(identifier, identifier2, identifier3, RegistryEntryList.empty()), RegistryEntryInfo.DEFAULT);
    }

    public RegistryEntry<WolfVariant> randomWolfVariant() {
        final var registry = getRegistryManager().getOrThrow(RegistryKeys.WOLF_VARIANT);
        final int index = ThreadLocalRandom.current().nextInt(0, 9);
        return registry.getEntry(index).orElse(null);
    }

    public static class DummyPlayerListEntry extends PlayerListEntry {
        public DummyPlayerListEntry() {
            super(MinecraftClient.getInstance().getGameProfile(), false);
        }

        @Override
        public @NotNull SkinTextures getSkinTextures() {
            return super.getSkinTextures();
        }
    }
}
