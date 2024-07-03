/*
 * Copyright (c) 2024 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client.dummy;

import com.mojang.datafixers.util.Either;
import me.denarydev.chromium.ChromiumMod;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryOwner;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DummyClientLevel extends ClientWorld {

    private static DummyClientLevel instance;

    private static final DimensionType DUMMY = new DimensionType(OptionalLong.empty(), true, false, false, false, 1.0, false, false, 0, 256, 256, BlockTags.INFINIBURN_OVERWORLD, Identifier.of(ChromiumMod.MOD_ID, "dummy_type"), 1, new DimensionType.MonsterSettings(false, false, ConstantIntProvider.create(0), 0));
    private static final RegistryKey<DimensionType> DUMMY_TYPE_KEY = RegistryKey.of(RegistryKeys.DIMENSION_TYPE, Identifier.of(ChromiumMod.MOD_ID, "dummy_type"));
    private static final RegistryKey<World> WORLD_KEY = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(ChromiumMod.MOD_ID, "dummy"));

    public static DummyClientLevel getInstance() {
        if (instance == null) instance = new DummyClientLevel();
        return instance;
    }

    private DummyClientLevel() {
        super(DummyClientPacketListener.getInstance(), new Properties(Difficulty.PEACEFUL, false, true), WORLD_KEY, new DummyDirect<>(DUMMY_TYPE_KEY, DUMMY), 0, 0, () -> null, null, false, 0);
    }

    private record DummyDirect<T>(RegistryKey<T> key, T value) implements RegistryEntry<T> {
        @Override
        public boolean hasKeyAndValue() {
            return key != null && value != null;
        }

        @Override
        public boolean matchesId(Identifier id) {
            return key.getValue().equals(id);
        }

        @Override
        public boolean matchesKey(RegistryKey<T> key) {
            return key.equals(this.key);
        }

        @Override
        public boolean matches(Predicate<RegistryKey<T>> predicate) {
            return predicate.test(key);
        }

        @Override
        public boolean isIn(TagKey<T> tag) {
            return false;
        }

        @Override
        public boolean matches(RegistryEntry<T> entry) {
            return false;
        }

        @NotNull
        @Override
        public Stream<TagKey<T>> streamTags() {
            return Stream.of();
        }

        @NotNull
        @Override
        public Either<RegistryKey<T>, T> getKeyOrValue() {
            return Either.right(this.value);
        }

        @NotNull
        @Override
        public Optional<RegistryKey<T>> getKey() {
            return Optional.of(key);
        }

        @NotNull
        @Override
        public Type getType() {
            return Type.DIRECT;
        }

        @Override
        public boolean ownerEquals(RegistryEntryOwner<T> owner) {
            return false;
        }

        @Override
        public String toString() {
            return "DummyDirect{" + this.value + "}";
        }
    }
}
