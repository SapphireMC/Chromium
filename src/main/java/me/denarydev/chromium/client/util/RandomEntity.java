/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client.util;

import me.denarydev.chromium.client.dummy.DummyClientWorld;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.registry.Registries;

import java.util.Random;

/**
 * @author DenaryDev
 * @since 18:47 13.01.2025
 */
@Environment(EnvType.CLIENT)
public final class RandomEntity {
    private static final Random RANDOM = new Random();
    private static LivingEntity randomEntity;

    public static LivingEntity get() {
        return randomEntity;
    }

    public static void update() {
        final var entities = Registries.ENTITY_TYPE.stream()
                .filter(e -> e.getSpawnGroup() != SpawnGroup.MISC)
                .filter(e -> e != EntityType.ENDER_DRAGON && e != EntityType.WITHER)
                .toList();
        final var entity = entities.get(RANDOM.nextInt(entities.size())).create(DummyClientWorld.getInstance(), SpawnReason.CHUNK_GENERATION);
        if (entity instanceof LivingEntity livingEntity) randomEntity = livingEntity;
    }
}
