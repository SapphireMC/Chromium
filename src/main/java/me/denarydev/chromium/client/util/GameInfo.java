/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.world.LightType;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author DenaryDev
 * @since 15:03 13.01.2025
 */
@Environment(EnvType.CLIENT)
public final class GameInfo {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static String lastCoordsInfo = "";
    private static String lastLightInfo = "";
    private static String lastBiomeInfo = "";

    @NotNull
    public static String fps() {
        final var maxFPS = (double) CLIENT.options.getMaxFps().getValue() == GameOptions.MAX_FPS_LIMIT ? "∞" : CLIENT.options.getMaxFps().getValue().toString();
        final var vsync = CLIENT.options.getEnableVsync().getValue().toString();
        return Text.translatable("options.chromium.fps", CLIENT.getCurrentFps(), maxFPS, vsync).getString();
    }

    @NotNull
    public static String time() {
        return Text.translatable("options.chromium.time", new SimpleDateFormat("HH:mm:ss dd/MM").format(new Date())).getString();
    }

    @NotNull
    public static String coords(final LivingEntity entity) {
        if (entity != null) {
            lastCoordsInfo = Text.translatable("options.chromium.coordinates", entity.getBlockX(), entity.getBlockY(), entity.getBlockZ()).getString();
        }
        return lastCoordsInfo;
    }

    @NotNull
    public static String light(final PlayerEntity player) {
        if (player != null && CLIENT.world != null) {
            final var blockPos = player.getBlockPos();
            final int clientLight = CLIENT.world.getChunkManager().getLightingProvider().getLight(blockPos, 0);
            final int skyLight = CLIENT.world.getLightLevel(LightType.SKY, blockPos);
            final int blockLight = CLIENT.world.getLightLevel(LightType.BLOCK, blockPos);
            lastLightInfo = Text.translatable("options.chromium.light", clientLight, skyLight, blockLight).getString();
        }
        return lastLightInfo;
    }

    @NotNull
    public static String biome(final PlayerEntity player) {
        if (player != null && CLIENT.world != null) {
            final var blockPos = player.getSteppingPos();
            final var biomes = CLIENT.world.getRegistryManager().getOrThrow(RegistryKeys.BIOME);
            final var biomeId = biomes.getId(CLIENT.world.getBiome(blockPos).value());

            if (biomeId != null) {
                lastBiomeInfo = Text.translatable("options.chromium.biome", Text.translatable("biome.minecraft." + biomeId.getPath())).getString();
            }
        }
        return lastBiomeInfo;
    }
}
