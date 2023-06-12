/*
 * Copyright (c) 2023 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.chromium.client.compat.sodium.mixin.gui;

import io.sapphiremc.chromium.ChromiumMod;
import io.sapphiremc.chromium.config.ChromiumConfig;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SodiumGameOptionPages.class)
public class MixinSodiumGameOptionPages {

    @ModifyArg(method = "lambda$general$9",
            at = @At(value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/gui/options/control/SliderControl;<init>(Lme/jellysquid/mods/sodium/client/gui/options/Option;IIILme/jellysquid/mods/sodium/client/gui/options/control/ControlValueFormatter;)V"
            ),
            index = 2,
            remap = false
    )
    private static int chromium$getMaxGuiScale(int value) {
        if (ChromiumMod.getConfig().getTitleScreenProvider().equals(ChromiumConfig.TitleScreenProvider.CHROMIUM)) {
            return 4;
        } else {
            return Minecraft.getInstance().getWindow().calculateScale(0, Minecraft.getInstance().isEnforceUnicode());
        }
    }

    @ModifyArg(method = "lambda$performance$51",
            at = @At(value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/gui/options/control/SliderControl;<init>(Lme/jellysquid/mods/sodium/client/gui/options/Option;IIILme/jellysquid/mods/sodium/client/gui/options/control/ControlValueFormatter;)V"
            ),
            index = 4,
            remap = false
    )
    private static @NotNull ControlValueFormatter chromium$getChunkUpdateThreadsText(ControlValueFormatter formatter) {
        return ControlValueFormatter.quantityOrDisabled(Component.translatable("sodium.options.chunk_update_threads.threads").getString(), Component.translatable("sodium.options.chunk_update_threads.default").getString());
    }
}
