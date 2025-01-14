/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.mixin.client;

import me.denarydev.chromium.ChromiumMod;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityRenderer.class)
public interface BlockEntityRendererMixin<T extends BlockEntity> {

    @Inject(method = "isInRenderDistance", at = @At("HEAD"), cancellable = true)
    default void shouldRender(T blockEntity, Vec3d pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(blockEntity instanceof BeaconBlockEntity ?
                Vec3d.ofCenter(blockEntity.getPos()).multiply(1.0, 0.0, 1.0).isInRange(pos.multiply(1.0, 0.0, 1.0), this.getRenderDistance(blockEntity)) :
                Vec3d.ofCenter(blockEntity.getPos()).isInRange(pos, this.getRenderDistance(blockEntity)));
    }

    @Unique
    default double getRenderDistance(T blockEntity) {
        final var config = ChromiumMod.config();

        if (blockEntity instanceof BannerBlockEntity) {
            return config.bannerRenderDistance;
        } else if (blockEntity instanceof ChestBlockEntity
                || blockEntity instanceof EnderChestBlockEntity) {
            return config.chestRenderDistance;
        } else if (blockEntity instanceof ShulkerBoxBlockEntity) {
            return config.shulkerBoxRenderDistance;
        } else if (blockEntity instanceof SignBlockEntity) {
            return config.signRenderDistance;
        } else if (blockEntity instanceof SkullBlockEntity) {
            return config.skullRenderDistance;
        } else {
            return 64;
        }
    }
}
