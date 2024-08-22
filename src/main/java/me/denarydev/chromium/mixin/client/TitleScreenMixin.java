/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import me.denarydev.chromium.ChromiumMod;
import me.denarydev.chromium.client.ChromiumClientMod;
import me.denarydev.chromium.client.dummy.DummyClientPlayNetworkHandler;
import me.denarydev.chromium.client.dummy.DummyClientPlayerEntity;
import me.denarydev.chromium.client.gui.OptionsScreenBuilder;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.QuickPlay;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.WolfVariant;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Unique
    private static final float SIZE = 40;

    @Unique
    private boolean baby;
    @Unique
    private RegistryEntry<WolfVariant> selectedWolfVariant;
    @Unique
    private DyeColor selectedSheepColor;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void chromium$addChromiumButtons(CallbackInfo ci) {
        assert this.client != null;
        addDrawableChild(ButtonWidget.builder(Text.literal("S"), (element) -> this.client.setScreen(OptionsScreenBuilder.build()))
                .dimensions(this.width - 22, 2, 20, 20)
                .build());
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void chromium$selectVariants(CallbackInfo ci) {
        baby = ThreadLocalRandom.current().nextBoolean();
        selectedWolfVariant = DummyClientPlayNetworkHandler.getInstance().randomWolfVariant();
        selectedSheepColor = DyeColor.byId(ThreadLocalRandom.current().nextInt(0, 15));
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void chromium$renderEntities(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        final var player = DummyClientPlayerEntity.getInstance();
        final int height = this.height / 4 + 132;
        final int playerX = this.width / 2 - 160;
        final int size = 40;
        final float playerLookX = -mouseX + playerX;
        final float lookY = -mouseY + height - size;
        chromium$renderEntityFollowsMouse(context, playerX, height, playerLookX, lookY, player);

        final var entity = ChromiumClientMod.getRandomEntity();
        final int entityX = this.width / 2 + 160;
        final float entityLookX = -mouseX + entityX;
        if (entity != null) {
            if (entity instanceof PassiveEntity passive) {
                passive.setBaby(baby);
            }

            if (selectedWolfVariant != null && entity instanceof WolfEntity wolf) {
                wolf.setVariant(selectedWolfVariant);
            } else if (selectedSheepColor != null && entity instanceof SheepEntity sheep) {
                sheep.setColor(selectedSheepColor);
            }

            chromium$renderEntityFollowsMouse(context, entityX, height, entityLookX, lookY, entity);
        }
    }

    @SuppressWarnings({"deprecation"})
    @Unique
    private void chromium$renderEntityFollowsMouse(DrawContext context, int x, int y, float lookX, float lookY, LivingEntity entity) {
        final float sideRot = (float) Math.atan(lookX / 200);
        final float upRot = (float) Math.atan(lookY / 200);
        final var poseMultiplier = (new Quaternionf()).rotateZ((float) Math.PI);
        final float bodyYaw = entity.bodyYaw;
        final float yaw = entity.getYaw();
        final float pitch = entity.getPitch();
        final float prevHeadYaw = entity.prevHeadYaw;
        final float headYaw = entity.headYaw;
        entity.bodyYaw = 180 + sideRot * 20;
        entity.setYaw(180 + sideRot * 40);
        entity.setPitch(-upRot * 20);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 50);
        context.getMatrices().multiplyPositionMatrix((new Matrix4f()).scaling(SIZE, SIZE, -SIZE));
        context.getMatrices().multiply(poseMultiplier);
        DiffuseLighting.enableGuiDepthLighting();
        final var dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);
        RenderSystem.runAsFancy(() -> dispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, context.getMatrices(), context.getVertexConsumers(), 15728880));
        context.draw();
        dispatcher.setRenderShadows(true);
        context.getMatrices().pop();
        DiffuseLighting.disableGuiDepthLighting();
        entity.bodyYaw = bodyYaw;
        entity.setYaw(yaw);
        entity.setPitch(pitch);
        entity.prevHeadYaw = prevHeadYaw;
        entity.headYaw = headYaw;
    }

    // Continue button
    @Unique
    private final MultiplayerServerListPinger serverListPinger = new MultiplayerServerListPinger();
    @Unique
    ButtonWidget continueButtonWidget = null;
    @Unique
    private ServerInfo continueServerInfo = null;
    @Unique
    private boolean isFirstRender = false;
    @Unique
    private boolean continueButtonReadyToShow = false;

    @Inject(at = @At("HEAD"), method = "initWidgetsNormal(II)V")
    public void chromium$addContinueButton(int y, int spacingY, CallbackInfo info) {
        if (!ChromiumMod.getConfig().continueButtonEnabled) return;

        final var continueInfo = ChromiumMod.getConfig().continueInfo;
        ButtonWidget.Builder continueButtonBuilder = ButtonWidget.builder(Text.translatable("button.chromium.continue"), button -> {
            if (continueInfo.local) {
                if (!continueInfo.lastName.isBlank()) {
                    QuickPlay.startSingleplayer(this.client, continueInfo.lastAddress);
                } else {
                    CreateWorldScreen.create(this.client, this);
                }
            } else {
                QuickPlay.startMultiplayer(this.client, continueInfo.lastAddress);
            }
        });
        continueButtonBuilder.dimensions(this.width / 2 - 100, y, 98, 20);
        continueButtonWidget = continueButtonBuilder.build();
        Screens.getButtons(this).add(continueButtonWidget);
    }

    @Inject(at = @At("HEAD"), method = "init()V")
    public void initAtHead(CallbackInfo info) {
        this.isFirstRender = true;
    }

    @Inject(at = @At("TAIL"), method = "init()V")
    public void init(CallbackInfo info) {
        for (final var button : Screens.getButtons(this)) {
            if (button.visible && !button.getMessage().equals(Text.translatable("button.chromium.continue"))) {
                button.setX(this.width / 2 + 2);
                button.setWidth(98);
                break;
            }
        }
    }

    @Unique
    private void chromium$atFirstRender() {
        final var continueInfo = ChromiumMod.getConfig().continueInfo;
        new Thread(() -> {
            if (!continueInfo.local) {
                continueServerInfo = new ServerInfo(continueInfo.lastName, continueInfo.lastAddress, ServerInfo.ServerType.OTHER);
                continueServerInfo.label = Text.translatable("multiplayer.status.pinging");
                try {
                    Runnable doNothing = () -> {
                    };
                    serverListPinger.add(continueServerInfo, doNothing, doNothing);
                } catch (UnknownHostException e) {
                    ChromiumMod.LOGGER.error("Failed to connect to server", e);
                }
            }
            continueButtonReadyToShow = true;
        }).start();
    }

    @Inject(at = @At("HEAD"), method = "render")
    public void chromium$renderAtHead(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (isFirstRender) {
            isFirstRender = false;
            chromium$atFirstRender();
        }
    }

    @Inject(at = @At("TAIL"), method = "render")
    public void chromium$renderAtTail(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (continueButtonWidget.isHovered() && this.continueButtonReadyToShow) {
            final var continueInfo = ChromiumMod.getConfig().continueInfo;
            if (continueInfo.local) {
                final var list = new ArrayList<OrderedText>();
                if (continueInfo.lastAddress.isEmpty()) {
                    list.add(Text.translatable("selectWorld.create").formatted(Formatting.GRAY).asOrderedText());
                } else {
                    list.add(Text.translatable("menu.singleplayer").formatted(Formatting.GRAY).asOrderedText());
                    list.add(Text.literal(continueInfo.lastName).asOrderedText());
                }
                context.drawOrderedTooltip(this.textRenderer, list, mouseX, mouseY);
            } else {
                final var list = new ArrayList<>(this.client.textRenderer.wrapLines(continueServerInfo.label, 270));
                list.addFirst(Text.literal(continueServerInfo.name).formatted(Formatting.GRAY).asOrderedText());
                context.drawOrderedTooltip(this.textRenderer, list, mouseX, mouseY);
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "tick()V")
    public void tick(CallbackInfo info) {
        serverListPinger.tick();
    }

    @Inject(at = @At("RETURN"), method = "removed()V")
    public void removed(CallbackInfo info) {
        serverListPinger.cancel();
    }
}
