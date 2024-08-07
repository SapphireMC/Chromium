/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.mixin.client;

import me.denarydev.chromium.ChromiumMod;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
    private final ArrayList<Long> chromium$messageTimestamps = new ArrayList<>();
    @Unique
    private final float chromium$fadeOffsetYScale = 0.8f; // scale * lineHeight
    @Unique
    private final float chromium$fadeTime = 130;
    @Unique
    private int chromium$chatDisplacementY = 0;

    // Change message history size
    @ModifyConstant(method = "addVisibleMessage",
            constant = @Constant(intValue = 100)
    )
    private int chromium$getMaxMessages(int max) {
        return ChromiumMod.getConfig().messagesHistorySize;
    }

    // Timestamp prefix
    @Unique
    private static final Pattern CHROMIUM$TIMESTAMP_PATTERN = Pattern.compile("\\[\\d{2}:\\d{2}:\\d{2}]");

    @ModifyVariable(
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private Text chromium$messageWithTimestamp(Text message) {
        final var builder = Text.empty();
        final var msgString = message.getString();
        if (ChromiumMod.getConfig().showTimestamp && (msgString.length() < 13 || !CHROMIUM$TIMESTAMP_PATTERN.matcher(msgString.substring(0, 13)).find())) {
            final var hoverText = Text.translatable(Formatting.YELLOW + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss ").format(new Date()) + TimeZone.getDefault().getID());
            final var timeText = Text.translatable(Formatting.GRAY + new SimpleDateFormat("[HH:mm:ss] ").format(new Date()) + Formatting.RESET).styled(
                    (style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText))));
            builder.append(timeText);
        }
        builder.append(message);
        return builder;
    }

    // Chat animations (from https://github.com/Ezzenix/ChatAnimation)
    @ModifyArg(method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V",
                    ordinal = 0
            ),
            index = 1)
    private float chromium$applyYOffset(float y) {
        if (!ChromiumMod.getConfig().messageAnimations) return y;
        chromium$calculateYOffset();

        return y + chromium$chatDisplacementY;
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("TAIL"))
    private void chromium$captureMessageTimestamps(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        chromium$messageTimestamps.addFirst(System.currentTimeMillis());
        while (this.chromium$messageTimestamps.size() > this.visibleMessages.size()) {
            this.chromium$messageTimestamps.removeLast();
        }
    }

    @Unique
    private void chromium$calculateYOffset() {
        // Calculate current required offset to achieve slide in from bottom effect
        try {
            int lineHeight = this.getLineHeight();
            float maxDisplacement = (float) lineHeight * chromium$fadeOffsetYScale;
            long timestamp = chromium$messageTimestamps.getFirst();
            long timeAlive = System.currentTimeMillis() - timestamp;
            if (timeAlive < chromium$fadeTime && this.scrolledLines == 0) {
                chromium$chatDisplacementY = (int) (maxDisplacement - ((timeAlive / chromium$fadeTime) * maxDisplacement));
            }
        } catch (Exception ignored) {
        }
    }
}
