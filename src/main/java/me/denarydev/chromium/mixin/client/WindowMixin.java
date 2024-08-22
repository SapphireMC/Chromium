/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.mixin.client;

import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Window.class)
public abstract class WindowMixin {

    @Redirect(method = "<init>",
            at = @At(value = "INVOKE",
                    target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowSizeLimits(JIIII)V"
            ),
            remap = false
    )
    private void chromium$minWindowLimits(long window, int minW, int minH, int maxW, int maxH) {
        GLFW.glfwSetWindowSizeLimits(window, 960, 700, maxW, maxH);
    }
}
