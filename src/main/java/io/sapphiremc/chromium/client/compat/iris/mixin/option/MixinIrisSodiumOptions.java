/*
 * Copyright (c) 2022 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package io.sapphiremc.chromium.client.compat.iris.mixin.option;

import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import net.coderbot.iris.compat.sodium.impl.options.IrisSodiumOptions;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(IrisSodiumOptions.class)
public class MixinIrisSodiumOptions {

    @ModifyArg(method = "lambda$createMaxShadowDistanceSlider$0",
            at = @At(value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/gui/options/control/SliderControl;<init>(Lme/jellysquid/mods/sodium/client/gui/options/Option;IIILme/jellysquid/mods/sodium/client/gui/options/control/ControlValueFormatter;)V"
            ),
            index = 4
    )
    private static @NotNull ControlValueFormatter chromium$quantityOrDisabled(ControlValueFormatter formatter) {
        return ControlValueFormatter.quantityOrDisabled(new TranslatableText("options.chunks").getString(), new TranslatableText("label.chromium.disabled").getString());
    }

    @Contract("_ -> new")
    @ModifyArg(method = "lambda$createLimitedVideoSettingsButton$3",
            at = @At(value = "INVOKE",
                    target = "Lme/jellysquid/mods/sodium/client/gui/options/control/CyclingControl;<init>(Lme/jellysquid/mods/sodium/client/gui/options/Option;Ljava/lang/Class;[Lnet/minecraft/text/Text;)V"
            ),
            index = 2
    )
    private static @NotNull Text @NotNull [] chromium$quantityOrDisabled(Text[] names) {
        return new Text[]{new TranslatableText("options.graphics.fast"), new TranslatableText("options.graphics.fancy")};
    }
}
