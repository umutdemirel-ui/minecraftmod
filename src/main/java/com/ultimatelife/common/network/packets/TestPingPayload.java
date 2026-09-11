package com.ultimatelife.common.network.packets;

import com.ultimatelife.UltimateLife;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Client -&gt; server test message.
 *
 * <p>Part of the Phase 1 harness: right-clicking the test block sends this so that the
 * whole networking stack (encode, handshake, rate limit, server handling, reply) can be
 * exercised at runtime. Payloads are plain records - no logic, no side effects.
 *
 * @param origin packed block position ({@code BlockPos#asLong()}) that was clicked,
 *               echoed back so the round trip can be verified
 */
public record TestPingPayload(long origin) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TestPingPayload> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(UltimateLife.MOD_ID, "test_ping"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TestPingPayload> STREAM_CODEC =
        StreamCodec.composite(ByteBufCodecs.VAR_LONG, TestPingPayload::origin, TestPingPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
