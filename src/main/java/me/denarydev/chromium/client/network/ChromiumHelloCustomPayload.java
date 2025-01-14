/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium.client.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

/**
 * @author DenaryDev
 * @since 21:13 03.07.2024
 */
@Environment(EnvType.CLIENT)
public record ChromiumHelloCustomPayload(int protocol) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, ChromiumHelloCustomPayload> CODEC = CustomPayload.codecOf(ChromiumHelloCustomPayload::write, ChromiumHelloCustomPayload::new);
    public static final Id<ChromiumHelloCustomPayload> ID = CustomPayload.id("chromium_hello");
    public static final int PROTOCOL = 0;

    public ChromiumHelloCustomPayload() {
        this(PROTOCOL);
    }

    private ChromiumHelloCustomPayload(PacketByteBuf buf) {
        this(buf.readVarInt());
    }

    private void write(PacketByteBuf buf) {
        buf.writeVarInt(PROTOCOL);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
