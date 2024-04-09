/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client.compat.sodium.mixin.gui;

import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SodiumOptionsGUI.class)
public class SodiumOptionsGUIMixin {

    @ModifyArg(method = "rebuildGUIOptions",
            at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/util/Dim2i;<init>(IIII)V"),
            index = 2,
            remap = false
    )
    public int chromium$getElementWidth(int value) {
        return 232;
    }

    @ModifyVariable(method = "renderOptionTooltip",
            ordinal = 2,
            at = @At(value = "STORE"),
            remap = false
    )
    public int chromium$getTooltipWidth(int value) {
        return 232;
    }
}
