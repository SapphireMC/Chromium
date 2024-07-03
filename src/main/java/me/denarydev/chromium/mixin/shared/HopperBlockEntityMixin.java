/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.mixin.shared;

import me.denarydev.chromium.ChromiumMod;
import net.minecraft.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    @ModifyArg(method = "insertAndExtract", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;setTransferCooldown(I)V"))
    private static int chromium$hopperTransfer$insertAndExtract(int cooldown) {
        return ChromiumMod.getConfig().hopperTransfer;
    }

    @ModifyArg(method = "insert", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;removeStack(II)Lnet/minecraft/item/ItemStack;"), index = 1)
    private static int chromium$hopperAmount$insert(int amount) {
        return ChromiumMod.getConfig().hopperAmount;
    }

    @ModifyArg(method = "extract(Lnet/minecraft/block/entity/Hopper;Lnet/minecraft/inventory/Inventory;ILnet/minecraft/util/math/Direction;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;removeStack(II)Lnet/minecraft/item/ItemStack;"), index = 1)
    private static int chromium$hopperAmount$extract(int amount) {
        return ChromiumMod.getConfig().hopperAmount;
    }

    @ModifyArg(method = "transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;setTransferCooldown(I)V"))
    private static int chromium$hopperTransfer$transfer(int cooldown) {
        return ChromiumMod.getConfig().hopperTransfer - (8 - cooldown);
    }
}
