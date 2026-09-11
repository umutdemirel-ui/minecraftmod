package com.ultimatelife.common.network.packets;

import com.ultimatelife.UltimateLife;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Server -&gt; client reply to {@link TestPingPayload}.
 *
 * <p>Part of the Phase 1 harness. It exists purely to prove that the clientbound direction
 * of the channel works before any real GUI/mail/NPC synchronisation is built on top of it.
 *
 * @param origin the packed block position echoed back from the ping
 * @param serverTime game time observed by the server when the ping was handled
 */
public record TestPongPayload(long origin, long serverTime) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<TestPongPayload> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(UltimateLife.MOD_ID, "test_pong"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TestPongPayload> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, TestPongPayload::origin,
            ByteBufCodecs.VAR_LONG, TestPongPayload::serverTime,
            TestPongPayload::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
