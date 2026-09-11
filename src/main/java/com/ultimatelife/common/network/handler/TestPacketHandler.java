package com.ultimatelife.common.network.handler;

import com.ultimatelife.UltimateLife;
import com.ultimatelife.common.network.ModNetwork;
import com.ultimatelife.common.network.packets.TestPingPayload;
import com.ultimatelife.common.network.packets.TestPongPayload;
import com.ultimatelife.common.utility.PacketGuard;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Handlers for the Phase 1 test payloads.
 *
 * <p>Handlers run on the network thread. Everything that touches game state is pushed onto
 * the main thread with {@link CustomPayloadEvent.Context#enqueueWork(Runnable)}; the rate
 * check deliberately stays on the network thread so a spamming client cannot fill the main
 * thread's queue in the first place.
 */
public final class TestPacketHandler {

    /** Maximum pings a player may have "in the bank" before further ones are rejected. */
    private static final int PING_BURST = 5;
    /** Tokens refilled per second. */
    private static final int PING_REFILL_PER_SECOND = 2;

    private TestPacketHandler() {
    }

    /** Client -&gt; server: validate, then reply. */
    public static void onPing(final TestPingPayload payload, final CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            final ServerPlayer sender = context.getSender();
            if (sender == null) {
                return;
            }

            // Server-authoritative: the client tells us *where it clicked*, we decide
            // whether that click is legitimate. Never trust the claimed position.
            if (!PacketGuard.allow(sender, "test_ping", PING_BURST, PING_REFILL_PER_SECOND)) {
                sender.connection.disconnect(Component.translatable("network.ultimatelife.rate_limited"));
                return;
            }

            final BlockPos pos = BlockPos.of(payload.origin());
            UltimateLife.LOGGER.info("[test] ping from {} at {}", sender.getGameProfile().getName(), pos.toShortString());
            ModNetwork.sendToPlayer(new TestPongPayload(payload.origin(), sender.level().getGameTime()), sender);
        });
        context.setPacketHandled(true);
    }

    /** Server -&gt; client: round-trip confirmation. */
    public static void onPong(final TestPongPayload payload, final CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> UltimateLife.LOGGER.info(
            "[test] pong for {} (server time {})", BlockPos.of(payload.origin()).toShortString(), payload.serverTime()));
        context.setPacketHandled(true);
    }
}
