/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium;

import me.denarydev.chromium.config.ChromiumConfig;
import me.denarydev.chromium.config.ConfigManager;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.world.LightType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChromiumMod implements ModInitializer {
    public static final String MOD_ID = "chromium";
    public static final Logger LOGGER = LoggerFactory.getLogger("Chromium");

    @Getter
    private static ConfigManager configManager;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing chromium by SapphireMC");

        configManager = new ConfigManager();
    }

    public static ChromiumConfig getConfig() {
        return configManager.getConfig();
    }

    @Environment(EnvType.CLIENT)
    public static String getFpsString() {
        final var client = MinecraftClient.getInstance();

        final var maxFPS = (double) client.options.getMaxFps().getValue() == GameOptions.MAX_FRAMERATE ? "∞" : client.options.getMaxFps().getValue().toString();
        final var vsync = client.options.getEnableVsync().getValue().toString();
        return Text.translatable("options.chromium.fps", client.getCurrentFps(), maxFPS, vsync).getString();
    }

    @Environment(EnvType.CLIENT)
    public static String getTimeString() {
        return Text.translatable("options.chromium.time", new SimpleDateFormat("HH:mm:ss dd/MM").format(new Date())).getString();
    }

    @Environment(EnvType.CLIENT)
    private static String cachedCoords = "";

    @Environment(EnvType.CLIENT)
    public static String getCoordsString(LivingEntity entity) {
        if (entity != null) {
            cachedCoords = Text.translatable("options.chromium.coordinates", entity.getBlockX(), entity.getBlockY(), entity.getBlockZ()).getString();
        }
        return cachedCoords;
    }

    @Environment(EnvType.CLIENT)
    private static String cachedLight = "";

    @Environment(EnvType.CLIENT)
    public static String getLightString(PlayerEntity player) {
        final var client = MinecraftClient.getInstance();
        if (player != null && client.world != null) {
            final var blockPos = player.getBlockPos();
            final int clientLight = client.world.getChunkManager().getLightingProvider().getLight(blockPos, 0);
            final int skyLight = client.world.getLightLevel(LightType.SKY, blockPos);
            final int blockLight = client.world.getLightLevel(LightType.BLOCK, blockPos);
            cachedLight = Text.translatable("options.chromium.light", clientLight, skyLight, blockLight).getString();
        }
        return cachedLight;
    }

    @Environment(EnvType.CLIENT)
    private static String cachedBiome = "";

    @Environment(EnvType.CLIENT)
    public static String getBiomeString(PlayerEntity player) {
        final var client = MinecraftClient.getInstance();
        if (player != null && client.world != null) {
            final var blockPos = player.getSteppingPos();
            final var biomes = client.world.getRegistryManager().get(RegistryKeys.BIOME);
            final var biome = client.world.getBiome(blockPos).value();
            final var biomeId = biomes.getId(biome);
            if (biomeId != null) {
                cachedBiome = Text.translatable("options.chromium.biome", Text.translatable("biome.minecraft." + biomeId.getPath())).getString();
            }
        }
        return cachedBiome;
    }
}
