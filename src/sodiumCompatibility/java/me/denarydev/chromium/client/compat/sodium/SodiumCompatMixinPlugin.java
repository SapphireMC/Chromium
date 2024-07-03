/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client.compat.sodium;

import java.util.List;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class SodiumCompatMixinPlugin implements IMixinConfigPlugin {
    private final List<AllowedSodiumVersion> allowedSodiumVersions = List.of(
            new AllowedSodiumVersion("0.5.9"),
            new AllowedSodiumVersion("0.5.11")
    );
    private boolean validSodiumVersion = false;

    @Override
    public void onLoad(String mixinPackage) {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER)) return;
        validSodiumVersion = FabricLoader.getInstance().getModContainer("sodium").map(sodium -> {
            String version = sodium.getMetadata().getVersion().getFriendlyString();

            return isAllowedVersion(version);
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

    private boolean isAllowedVersion(String version) {
        for (AllowedSodiumVersion allowed : allowedSodiumVersions) {
            if (allowed.matches(version)) {
                return true;
            }
        }

        return false;
    }

    private record AllowedSodiumVersion(String version) {

        private boolean matches(String candidate) {
                return candidate.startsWith(version);
        }
    }
}
