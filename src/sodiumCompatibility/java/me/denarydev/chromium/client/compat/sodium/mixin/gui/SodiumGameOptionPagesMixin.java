/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client.compat.sodium.mixin.gui;

import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptionPages;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * @author DenaryDev
 * @since 14:53 13.01.2025
 */
@Mixin(SodiumGameOptionPages.class)
public final class SodiumGameOptionPagesMixin {

    @ModifyArg(method = "lambda$performance$57",
            at = @At(value = "INVOKE",
                    target = "Lnet/caffeinemc/mods/sodium/client/gui/options/control/SliderControl;<init>(Lnet/caffeinemc/mods/sodium/client/gui/options/Option;IIILnet/caffeinemc/mods/sodium/client/gui/options/control/ControlValueFormatter;)V"
            ),
            index = 4,
            remap = false
    )
    private static ControlValueFormatter chromium$chunkUpdateThreadsFormatter(ControlValueFormatter formatter) {
        return ControlValueFormatter.quantityOrDisabled(Text.translatable("sodium.options.chunk_update_threads.threads").getString(), Text.translatable("sodium.options.chunk_update_threads.default").getString());
    }
}
