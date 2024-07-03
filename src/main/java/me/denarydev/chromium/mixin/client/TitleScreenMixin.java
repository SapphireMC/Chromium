/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import me.denarydev.chromium.client.ChromiumClientMod;
import me.denarydev.chromium.client.dummy.DummyClientPlayerEntity;
import me.denarydev.chromium.client.gui.OptionsScreenBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Unique private static final float SIZE = 40;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void chromium$addChromiumSettingsButton(CallbackInfo ci) {
        assert this.client != null;
        addDrawableChild(ButtonWidget.builder(Text.literal("S"), (element) -> this.client.setScreen(OptionsScreenBuilder.build()))
                .dimensions(this.width - 22, 2, 20, 20)
                .build());
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void chromium$renderPlayerModel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
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
            chromium$renderEntityFollowsMouse(context, entityX, height, entityLookX, lookY, entity);
        }
    }

    @SuppressWarnings({"deprecation"})
    @Unique
    private void chromium$renderEntityFollowsMouse(DrawContext context, int x, int y, float lookX, float lookY, LivingEntity player) {
        final float sideRot = (float) Math.atan(lookX / 200);
        final float upRot = (float) Math.atan(lookY / 200);
        final var poseMultiplier = (new Quaternionf()).rotateZ((float) Math.PI);
        final float bodyYaw = player.bodyYaw;
        final float yaw = player.getYaw();
        final float pitch = player.getPitch();
        final float prevHeadYaw = player.prevHeadYaw;
        final float headYaw = player.headYaw;
        player.bodyYaw = 180 + sideRot * 20;
        player.setYaw(180 + sideRot * 40);
        player.setPitch(-upRot * 20);
        player.headYaw = player.getYaw();
        player.prevHeadYaw = player.getYaw();
        context.getMatrices().push();
        context.getMatrices().translate(x, y, 50);
        context.getMatrices().multiplyPositionMatrix((new Matrix4f()).scaling(SIZE, SIZE, -SIZE));
        context.getMatrices().multiply(poseMultiplier);
        DiffuseLighting.enableGuiDepthLighting();
        final var dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        dispatcher.setRenderShadows(false);
        RenderSystem.runAsFancy(() -> dispatcher.render(player, 0.0, 0.0, 0.0, 0.0F, 1.0F, context.getMatrices(), context.getVertexConsumers(), 15728880));
        context.draw();
        dispatcher.setRenderShadows(true);
        context.getMatrices().pop();
        DiffuseLighting.disableGuiDepthLighting();
        player.bodyYaw = bodyYaw;
        player.setYaw(yaw);
        player.setPitch(pitch);
        player.prevHeadYaw = prevHeadYaw;
        player.headYaw = headYaw;
    }
}
