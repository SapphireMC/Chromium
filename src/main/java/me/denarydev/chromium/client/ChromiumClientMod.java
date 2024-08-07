/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client;

import com.google.common.io.Files;
import me.denarydev.chromium.ChromiumMod;
import me.denarydev.chromium.client.dummy.DummyClientWorld;
import me.denarydev.chromium.client.gui.OptionsScreenBuilder;
import lombok.Getter;
import me.denarydev.chromium.client.network.ChromiumHelloCustomPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;

public class ChromiumClientMod implements ClientModInitializer {

    private KeyBinding configKey;

    private final int protocolId = 0;
    private final Identifier hello = Identifier.of("chromium", "client");

    @Override
    public void onInitializeClient() {
        if (Boolean.getBoolean("chromium.killmclauncher") && Util.getOperatingSystem().equals(Util.OperatingSystem.WINDOWS)) {
            try {
                Runtime.getRuntime().exec("taskkill /F /IM Minecraft.exe");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.chromium.config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "key.chromium.category"
        ));

        initializeClientEvents();
    }

    private void initializeClientEvents() {
        initializeClientNetworkingEvents();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKey.isPressed()) {
                client.setScreen(OptionsScreenBuilder.build());
            }
        });
        ScreenEvents.BEFORE_INIT.register(((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof TitleScreen) updateRandomEntity();
        }));

        initializeClientConnectionEvents();
    }

    @SuppressWarnings("DataFlowIssue")
    private void initializeClientConnectionEvents() {
        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            final var continueInfo = ChromiumMod.getConfig().continueInfo;
            if (client.isIntegratedServerRunning()) {
                continueInfo.local = true;
                final var levelName = client.getServer().getSaveProperties().getLevelName();
                final var pathtoSave = Path.of(Files.simplifyPath(client.getServer().getSavePath(WorldSavePath.ROOT).toString()));
                final var folderName = pathtoSave.normalize().toFile().getName();
                continueInfo.lastName = levelName;
                continueInfo.lastAddress = folderName;
            } else {
                final var serverInfo = client.getCurrentServerEntry();
                continueInfo.local = false;
                continueInfo.lastName = serverInfo.name;
                continueInfo.lastAddress = serverInfo.address;
            }
            ChromiumMod.getConfigManager().writeConfig(true);
        }));
    }

    private void initializeClientNetworkingEvents() {
        PayloadTypeRegistry.configurationC2S().register(ChromiumHelloCustomPayload.ID, ChromiumHelloCustomPayload.CODEC);

        ClientConfigurationConnectionEvents.COMPLETE.register((handler, client) ->
                ClientConfigurationNetworking.send(new ChromiumHelloCustomPayload()));
    }

    private static final Random RANDOM = new Random();
    @Getter
    private static LivingEntity randomEntity;

    public static void updateRandomEntity() {
        final var entities = Registries.ENTITY_TYPE.stream()
                .filter(e -> e.getSpawnGroup() != SpawnGroup.MISC)
                .filter(e -> e != EntityType.ENDER_DRAGON && e != EntityType.WITHER)
                .toList();
        final var entity = entities.get(RANDOM.nextInt(entities.size())).create(DummyClientWorld.getInstance());
        if (entity instanceof LivingEntity livingEntity) randomEntity = livingEntity;
    }
}
