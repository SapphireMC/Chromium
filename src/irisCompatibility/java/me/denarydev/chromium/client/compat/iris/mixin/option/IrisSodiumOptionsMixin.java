/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client.compat.iris.mixin.option;

import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.irisshaders.iris.compat.sodium.impl.options.IrisSodiumOptions;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(IrisSodiumOptions.class)
public class IrisSodiumOptionsMixin {

    @ModifyArg(method = "lambda$createMaxShadowDistanceSlider$0",
            at = @At(value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/gui/options/control/SliderControl;<init>(Lme/jellysquid/mods/sodium/client/gui/options/Option;IIILme/jellysquid/mods/sodium/client/gui/options/control/ControlValueFormatter;)V"
            ),
            index = 4,
            remap = false
    )
    private static @NotNull ControlValueFormatter chromium$getChunksText(ControlValueFormatter formatter) {
        return ControlValueFormatter.quantityOrDisabled(Text.translatable("sodium.options.chunks").getString(), Text.translatable("label.chromium.disabled").getString());
    }
}
