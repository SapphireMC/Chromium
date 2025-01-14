/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client.compat.sodium.mixin.gui;

import net.caffeinemc.mods.sodium.client.gui.SodiumOptionsGUI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SodiumOptionsGUI.class)
public final class SodiumOptionsGUIMixin {

    @ModifyArg(method = "rebuildGUIOptions",
            at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/util/Dim2i;<init>(IIII)V"),
            index = 2,
            remap = false
    )
    public int chromium$elementWidth(int value) {
        return 232;
    }

    @ModifyArg(method = "renderOptionTooltip",
            at = @At(value = "INVOKE",
                    target = "Ljava/lang/Math;min(II)I"
            ),
            index = 0,
            remap = false
    )
    public int chromium$tooltipWidth(int value) {
        return 232;
    }
}
