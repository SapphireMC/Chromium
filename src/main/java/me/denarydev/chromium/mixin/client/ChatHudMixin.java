/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import me.denarydev.chromium.ChromiumMod;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {

    @Shadow
    protected abstract int getLineHeight();

    @Shadow
    private int scrolledLines;
    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;
    @Unique
    private final ArrayList<Long> messageTimestamps = new ArrayList<>();

    @Unique
    private final float fadeOffsetYScale = 0.8f; // scale * lineHeight
    @Unique
    private final float fadeTime = 130;

    @Unique
    private int chatLineIndex;
    @Unique
    private int chatDisplacementY = 0;


    // Change message history size
    @ModifyConstant(method = "addVisibleMessage",
            constant = @Constant(intValue = 100)
    )
    private int chromium$getMaxMessages(int max) {
        return ChromiumMod.getConfig().messagesHistorySize;
    }

    // Timestamp prefix
    @Unique
    private static final Pattern TIMESTAMP_PATTERN = Pattern.compile("\\[\\d{2}:\\d{2}:\\d{2}]");

    @ModifyVariable(
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private Text chromium$messageWithTimestamp(Text message) {
        final var builder = Text.empty();
        final var msgString = message.getString();
        if (ChromiumMod.getConfig().showTimestamp && !TIMESTAMP_PATTERN.matcher(msgString.substring(0, 13)).find()) {
            final var hoverText = Text.translatable(Formatting.YELLOW + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ").format(new Date()) + TimeZone.getDefault().getID());
            final var timeText = Text.translatable(Formatting.GRAY + new SimpleDateFormat("[HH:mm:ss] ").format(new Date()) + Formatting.RESET).styled(
                    (style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText))));
            builder.append(timeText);
        }
        builder.append(message);
        return builder;
    }

    // Chat messages animations
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;addedTime()I"))
    private void chromium$getChatLineIndex(CallbackInfo ci, @Local(ordinal = 13) int chatLineIndex) {
        if (!ChromiumMod.getConfig().messageAnimations) return;
        this.chatLineIndex = chatLineIndex;
    }

    @Unique
    private void calculateYOffset() {
        // Calculate current required offset to achieve slide in from bottom effect
        try {
            int lineHeight = this.getLineHeight();
            float maxDisplacement = (float) lineHeight * fadeOffsetYScale;
            long timestamp = messageTimestamps.get(chatLineIndex);
            long timeAlive = System.currentTimeMillis() - timestamp;
            if (chatLineIndex == 0 && timeAlive < fadeTime && this.scrolledLines == 0) {
                chatDisplacementY = (int) (maxDisplacement - ((timeAlive / fadeTime) * maxDisplacement));
            }
        } catch (Exception ignored) {
        }
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 1), index = 1)
    private float chromium$applyYOffset(float y) {
        if (!ChromiumMod.getConfig().messageAnimations) return y;
        calculateYOffset();

        // Raised mod compatibility
        if (FabricLoader.getInstance().getObjectShare().get("raised:hud") instanceof Integer distance) {
            y -= distance;
        } else if (FabricLoader.getInstance().getObjectShare().get("raised:distance") instanceof Integer distance) {
            y -= distance;
        }

        return y + chatDisplacementY;
    }

    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 2)
    private double chromium$modifyOpacity(double originalOpacity) {
        if (!ChromiumMod.getConfig().messageAnimations) return originalOpacity;
        double opacity = originalOpacity;

        try {
            long timestamp = messageTimestamps.get(chatLineIndex);
            long timeAlive = System.currentTimeMillis() - timestamp;
            if (timeAlive < fadeTime && this.scrolledLines == 0) {
                opacity = opacity * (0.5 + MathHelper.clamp(timeAlive / fadeTime, 0, 1) / 2);
            }
        } catch (Exception ignored) {
        }
        return opacity;
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(method = "render", at = @At("STORE"))
    private MessageIndicator chromium$removeMessageTag(MessageIndicator originalMessageTag) {
        if (!ChromiumMod.getConfig().messageAnimations) return originalMessageTag;
        return null; // Don't allow the chat indicator bar to be rendered
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At("TAIL")
    )
    private void chromium$addMessage(Text message, MessageSignatureData signature, MessageIndicator tag, CallbackInfo ci) {
        if (!ChromiumMod.getConfig().messageAnimations) return;
        messageTimestamps.add(0, System.currentTimeMillis());
        while (this.messageTimestamps.size() > this.visibleMessages.size()) {
            this.messageTimestamps.remove(this.messageTimestamps.size() - 1);
        }
    }
}
