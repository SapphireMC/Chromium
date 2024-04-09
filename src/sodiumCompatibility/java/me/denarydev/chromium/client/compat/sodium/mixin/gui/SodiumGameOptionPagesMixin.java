/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client.compat.sodium.mixin.gui;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SodiumGameOptionPages.class)
public class SodiumGameOptionPagesMixin {

    @ModifyArg(method = "lambda$performance$52",
            at = @At(value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/gui/options/control/SliderControl;<init>(Lme/jellysquid/mods/sodium/client/gui/options/Option;IIILme/jellysquid/mods/sodium/client/gui/options/control/ControlValueFormatter;)V"
            ),
            index = 4,
            remap = false
    )
    private static @NotNull ControlValueFormatter chromium$getChunkUpdateThreadsText(ControlValueFormatter formatter) {
        return ControlValueFormatter.quantityOrDisabled(Text.translatable("sodium.options.chunk_update_threads.threads").getString(), Text.translatable("sodium.options.chunk_update_threads.default").getString());
    }
}
