/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client.gui;

import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import me.denarydev.chromium.ChromiumMod;
import me.denarydev.chromium.config.ChromiumConfig;
import me.denarydev.chromium.util.ColorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;

/**
 * @author DenaryDev
 * @since 15:47 13.01.2025
 */
@Environment(EnvType.CLIENT)
public final class OptionsScreenBuilder {
    private final ChromiumConfig currentConfig;
    private final ChromiumConfig defaultConfig = new ChromiumConfig();

    public OptionsScreenBuilder(final ChromiumConfig currentConfig) {
        this.currentConfig = currentConfig;
    }

    public Screen createScreen(final Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("screen.chromium.settings"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("options.chromium.category.inGame"))
                        .group(infoPanel())
                        .group(tabList())
                        .group(chat())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("options.chromium.category.screens"))
                        .group(titleScreen())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("options.chromium.category.entities"))
                        .group(tileEntitiesViewDistance())
                        .build())
                .save(() -> ChromiumMod.configManager().writeConfig(true))
                .build()
                .generateScreen(parent);
    }

    private OptionGroup infoPanel() {
        return OptionGroup.createBuilder()
                .name(Text.translatable("options.chromium.group.info"))
                .description(description("options.chromium.group.info"))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("options.chromium.showFps"))
                        .description(description("options.chromium.showFps"))
                        .binding(defaultConfig.showFps, () -> currentConfig.showFps, it -> ChromiumMod.config().showFps = it)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("options.chromium.showTime"))
                        .description(description("options.chromium.showTime"))
                        .binding(defaultConfig.showTime, () -> currentConfig.showTime, it -> ChromiumMod.config().showTime = it)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("options.chromium.showCoords"))
                        .description(description("options.chromium.showCoords"))
                        .binding(defaultConfig.showCoords, () -> currentConfig.showCoords, it -> ChromiumMod.config().showCoords = it)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("options.chromium.showLight"))
                        .description(description("options.chromium.showLight"))
                        .binding(defaultConfig.showLight, () -> currentConfig.showLight, it -> ChromiumMod.config().showLight = it)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("options.chromium.showBiome"))
                        .description(description("options.chromium.showBiome"))
                        .binding(defaultConfig.showBiome, () -> currentConfig.showBiome, it -> ChromiumMod.config().showBiome = it)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .build();
    }

    private OptionGroup chat() {
        return OptionGroup.createBuilder()
                .name(Text.translatable("options.chromium.group.chat"))
                .description(description("options.chromium.group.chat"))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("options.chromium.showMessagesTime"))
                        .description(description("options.chromium.showMessagesTime"))
                        .binding(defaultConfig.showTimestamp, () -> currentConfig.showTimestamp, it -> ChromiumMod.config().showTimestamp = it)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("options.chromium.messageAnimations"))
                        .description(description("options.chromium.messageAnimations"))
                        .binding(defaultConfig.messageAnimations, () -> currentConfig.messageAnimations, it -> ChromiumMod.config().messageAnimations = it)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("options.chromium.maxMessages"))
                        .description(description("options.chromium.maxMessages"))
                        .binding(defaultConfig.messagesHistorySize, () -> currentConfig.messagesHistorySize, it -> ChromiumMod.config().messagesHistorySize = it)
                        .controller(option ->
                                IntegerFieldControllerBuilder.create(option)
                                        .min(50)
                                        .max(32000))
                        .build())
                .build();
    }

    private OptionGroup tabList() {
        return OptionGroup.createBuilder()
                .name(Text.translatable("options.chromium.group.tabList"))
                .description(description("options.chromium.group.tabList"))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("options.chromium.showPingAmount"))
                        .description(description("options.chromium.showPingAmount"))
                        .binding(defaultConfig.showPingAmount, () -> currentConfig.showPingAmount, it -> ChromiumMod.config().showPingAmount = it)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("options.chromium.replacePingBars"))
                        .description(description("options.chromium.replacePingBars"))
                        .binding(defaultConfig.replacePingBars, () -> currentConfig.replacePingBars, it -> ChromiumMod.config().replacePingBars = it)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("options.chromium.pingAmountAutoColor"))
                        .description(description("options.chromium.pingAmountAutoColor"))
                        .binding(defaultConfig.pingAmountAutoColor, () -> currentConfig.pingAmountAutoColor, it -> ChromiumMod.config().pingAmountAutoColor = it)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(Option.<Color>createBuilder()
                        .name(Text.translatable("options.chromium.pingAmountColor"))
                        .description(description("options.chromium.pingAmountColor"))
                        .binding(Color.decode(defaultConfig.pingAmountColor), () -> Color.decode(currentConfig.pingAmountColor), it -> ChromiumMod.config().pingAmountColor = ColorUtils.toHex(it.getRGB()))
                        .controller(ColorControllerBuilder::create)
                        .build())
                .build();
    }

    private OptionGroup titleScreen() {
        return OptionGroup.createBuilder()
                .name(Text.translatable("options.chromium.group.titleScreen"))
                .description(description("options.chromium.group.titleScreen"))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("options.chromium.continueButtonEnabled"))
                        .description(description("options.chromium.continueButtonEnabled"))
                        .binding(defaultConfig.continueButtonEnabled, () -> currentConfig.continueButtonEnabled, it -> ChromiumMod.config().continueButtonEnabled = it)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .option(ButtonOption.createBuilder()
                        .name(Text.translatable("options.chromium.clearContinueInfo"))
                        .description(description("options.chromium.clearContinueInfo"))
                        .action(((screen, option) -> ChromiumMod.config().continueInfo = new ChromiumConfig.ContinueInfo()))
                        .build())
                .build();
    }

    private OptionGroup tileEntitiesViewDistance() {
        return OptionGroup.createBuilder()
                .name(Text.translatable("options.chromium.group.teViewDistance"))
                .description(description("options.chromium.group.teViewDistance"))
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("options.chromium.render.te.bannerRenderDistance"))
                        .description(description("options.chromium.render.te.bannerRenderDistance"))
                        .binding(defaultConfig.bannerRenderDistance, () -> currentConfig.bannerRenderDistance, it -> ChromiumMod.config().bannerRenderDistance = it)
                        .controller(option ->
                                IntegerSliderControllerBuilder.create(option)
                                        .formatValue(integer -> Text.translatable("options.chunks", integer / 16))
                                        .range(32, 512)
                                        .step(16)
                        )
                        .build()
                )
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("options.chromium.render.te.chestRenderDistance"))
                        .description(description("options.chromium.render.te.chestRenderDistance"))
                        .binding(defaultConfig.chestRenderDistance, () -> currentConfig.chestRenderDistance, it -> ChromiumMod.config().chestRenderDistance = it)
                        .controller(option ->
                                IntegerSliderControllerBuilder.create(option)
                                        .formatValue(integer -> Text.translatable("options.chunks", integer / 16))
                                        .range(32, 512)
                                        .step(16)
                        )
                        .build()
                )
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("options.chromium.render.te.shulkerBoxRenderDistance"))
                        .description(description("options.chromium.render.te.shulkerBoxRenderDistance"))
                        .binding(defaultConfig.shulkerBoxRenderDistance, () -> currentConfig.shulkerBoxRenderDistance, it -> ChromiumMod.config().shulkerBoxRenderDistance = it)
                        .controller(option ->
                                IntegerSliderControllerBuilder.create(option)
                                        .formatValue(integer -> Text.translatable("options.chunks", integer / 16))
                                        .range(32, 512)
                                        .step(16)
                        )
                        .build()
                )
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("options.chromium.render.te.signRenderDistance"))
                        .description(description("options.chromium.render.te.signRenderDistance"))
                        .binding(defaultConfig.signRenderDistance, () -> currentConfig.signRenderDistance, it -> ChromiumMod.config().signRenderDistance = it)
                        .controller(option ->
                                IntegerSliderControllerBuilder.create(option)
                                        .formatValue(integer -> Text.translatable("options.chunks", integer / 16))
                                        .range(32, 512)
                                        .step(16)
                        )
                        .build()
                )
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("options.chromium.render.te.hangingSignRenderDistance"))
                        .description(description("options.chromium.render.te.hangingSignRenderDistance"))
                        .binding(defaultConfig.hangingSignRenderDistance, () -> currentConfig.hangingSignRenderDistance, it -> ChromiumMod.config().hangingSignRenderDistance = it)
                        .controller(option ->
                                IntegerSliderControllerBuilder.create(option)
                                        .formatValue(integer -> Text.translatable("options.chunks", integer / 16))
                                        .range(32, 512)
                                        .step(16)
                        )
                        .build()
                )
                .option(Option.<Integer>createBuilder()
                        .name(Text.translatable("options.chromium.render.te.skullRenderDistance"))
                        .description(description("options.chromium.render.te.skullRenderDistance"))
                        .binding(defaultConfig.skullRenderDistance, () -> currentConfig.skullRenderDistance, it -> ChromiumMod.config().skullRenderDistance = it)
                        .controller(option ->
                                IntegerSliderControllerBuilder.create(option)
                                        .formatValue(integer -> Text.translatable("options.chunks", integer / 16))
                                        .range(32, 512)
                                        .step(16)
                        )
                        .build()
                )
                .build();
    }

    @NotNull
    private OptionDescription description(final String key) {
        final var list = new ArrayList<Text>();

        for (int i = 0; i < 10; i++) {
            final var finalKey = key + ".description." + (i + 1);
            if (!I18n.hasTranslation(finalKey)) break;

            final var value = I18n.translate(finalKey);

            list.add(Text.literal(value));
        }

        return list.isEmpty() ? OptionDescription.EMPTY : OptionDescription.of(list.toArray(new Text[0]));
    }
}
