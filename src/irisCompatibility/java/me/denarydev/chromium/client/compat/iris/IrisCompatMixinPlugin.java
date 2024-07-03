/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client.compat.iris;

import java.util.List;
import java.util.Set;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class IrisCompatMixinPlugin implements IMixinConfigPlugin {
    private final List<AllowedIrisVersion> allowedIrisVersions = List.of(
            new AllowedIrisVersion("1.7.1"),
            new AllowedIrisVersion("1.7.2"),
            new AllowedIrisVersion("1.7.3")
    );
    private boolean validIrisVersion = false;

    @Override
    public void onLoad(String mixinPackage) {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.SERVER)) return;
        validIrisVersion = FabricLoader.getInstance().getModContainer("iris").map(iris -> {
            final var version = iris.getMetadata().getVersion().getFriendlyString();

            return isAllowedVersion(version);
        }).orElse(false);

        if (!validIrisVersion) {
            System.err.println("[Chromium] Invalid/missing version of Iris detected, disabling compatibility mixins!");
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return validIrisVersion;
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
        for (final var allowed : allowedIrisVersions) {
            if (allowed.matches(version)) {
                return true;
            }
        }

        return false;
    }

    private record AllowedIrisVersion(String version) {

        private boolean matches(String candidate) {
            return candidate.startsWith(version);
        }
    }
}
