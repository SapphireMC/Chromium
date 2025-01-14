/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.mixin.client;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Main.class)
public final class MainMixin {

    @ModifyArg(method = "main",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/WindowSettings;<init>(IILjava/util/OptionalInt;Ljava/util/OptionalInt;Z)V"
            ),
            index = 0
    )
    private static int chromium$applyMinWidth(int width) {
        return Math.max(width, 960);
    }

    @ModifyArg(method = "main",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/WindowSettings;<init>(IILjava/util/OptionalInt;Ljava/util/OptionalInt;Z)V"
            ),
            index = 1
    )
    private static int chromium$applyMinHeight(int height) {
        return Math.max(height, 700);
    }
}
