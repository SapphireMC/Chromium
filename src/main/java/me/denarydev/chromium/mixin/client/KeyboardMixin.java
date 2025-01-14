/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.mixin.client;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {

    @Shadow
    @Final
    private MinecraftClient client;
    @Unique
    private int cachedKey = 0;

    @Inject(method = "processF3", at = @At("HEAD"))
    private void chromium$cacheKey(int key, CallbackInfoReturnable<Boolean> cir) {
        cachedKey = key;
    }

    @Redirect(method = "processF3",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasPermissionLevel(I)Z")
    )
    public boolean chromium$hasPermissionLevel(ClientPlayerEntity player, int level) {
        if (cachedKey == 293) {
            return client.getCurrentServerEntry() != null || player.hasPermissionLevel(level);
        } else {
            return player.hasPermissionLevel(level);
        }
    }
}
