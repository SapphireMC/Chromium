/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client.compat.sodium;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class SodiumCompatMixinPlugin implements IMixinConfigPlugin {
    private static final List<String> ALLOWED_VERSIONS = List.of("0.6");
    private boolean validSodiumVersion = false;

    @Override
    public void onLoad(String mixinPackage) {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER)) return;
        validSodiumVersion = FabricLoader.getInstance().getModContainer("sodium").map(sodium -> {
            final var version = sodium.getMetadata().getVersion().getFriendlyString();

            return ALLOWED_VERSIONS.stream().anyMatch(version::startsWith);
        }).orElse(false);

        if (!validSodiumVersion) {
            System.err.println("[Chromium] Invalid/missing version of Sodium detected, disabling compatibility mixins!");
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return validSodiumVersion;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
