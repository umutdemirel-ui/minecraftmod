package com.ultimatelife.common.network;

import com.ultimatelife.UltimateLife;
import com.ultimatelife.common.config.ModConfig;
import com.ultimatelife.common.network.handler.PacketRateLimiter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.payload.PayloadConnection;
import net.minecraftforge.network.payload.PayloadFlow;

import java.util.function.BiConsumer;

/**
 * Networking entry point.
 *
 * <p>One versioned payload channel ({@code ultimatelife:main}) is created while the mod is being
 * constructed. Packets are registered in {@link #register()} only, because Forge locks the channel
 * builder once {@code build()} is called.</p>
 *
 * <p>Rules enforced here, so that no future system has to reinvent them:</p>
 * <ul>
 *     <li><b>Server authoritative.</b> Every client to server packet goes through
 *     {@link #registerServerbound(Type, StreamCodec, BiConsumer)}: the handler only runs when
 *     {@link CustomPayloadEvent.Context#getSender()} exists (i.e. we are on the server) and after the
 *     rate limiter accepted the packet. Handlers must never trust data that the server can compute
 *     itself.</li>
 *     <li><b>No spam.</b> {@link PacketRateLimiter} drops everything above the configured budget
 *     before any payload logic runs.</li>
 *     <li><b>Threading.</b> Handlers run on the netty thread. Touching the level or any other game
 *     state requires {@link CustomPayloadEvent.Context#enqueueWork(Runnable)} - the packet is marked
 *     as handled by the gate itself.</li>
 * </ul>
 *
 * <p>Phase 1 registers no packets on purpose: nothing needs one yet. The GUI, inventory, NPC, mail and
 * controller systems will add theirs in {@link #register()}.</p>
 */
public final class ModNetwork {

    /** Bump when a packet layout changes; Forge refuses connections with a mismatched version. */
    public static final int PROTOCOL_VERSION = 1;

    private static final ResourceLocation CHANNEL_ID =
            ResourceLocation.fromNamespaceAndPath(UltimateLife.MOD_ID, "main");

    private static final PacketRateLimiter SERVERBOUND_LIMITER = new PacketRateLimiter();

    private static Channel<CustomPacketPayload> channel;

    private ModNetwork() {
    }

    public static void register() {
        final PayloadConnection<CustomPacketPayload> connection = ChannelBuilder
                .named(CHANNEL_ID)
                .networkProtocolVersion(PROTOCOL_VERSION)
                .payloadChannel();

        // Flows are views on the same builder: both must be filled before build() locks the channel.
        final PayloadFlow<RegistryFriendlyByteBuf, CustomPacketPayload> serverbound =
                connection.play().serverbound();
        final PayloadFlow<RegistryFriendlyByteBuf, CustomPacketPayload> clientbound =
                serverbound.flow(PacketFlow.CLIENTBOUND);

        registerPackets(serverbound, clientbound);

        channel = clientbound.build();
    }

    /**
     * Registration point for every packet of the mod.
     *
     * <p>Empty in Phase 1 - add {@code serverbound(...)} / {@code clientbound(...)} calls here when a
     * system needs to talk over the network.</p>
     */
    private static void registerPackets(final PayloadFlow<RegistryFriendlyByteBuf, CustomPacketPayload> serverbound,
                                        final PayloadFlow<RegistryFriendlyByteBuf, CustomPacketPayload> clientbound) {
        // No packets yet.
    }

    /**
     * Registers a client to server packet behind the server-authoritative gate.
     *
     * @param handler executed on the network thread, only when a real sender exists and the rate
     *                limiter accepted the packet.
     */
    public static <MSG extends CustomPacketPayload> void registerServerbound(
            final PayloadFlow<RegistryFriendlyByteBuf, CustomPacketPayload> flow,
            final Type<MSG> type,
            final StreamCodec<RegistryFriendlyByteBuf, MSG> codec,
            final BiConsumer<MSG, CustomPayloadEvent.Context> handler) {

        flow.add(type, codec, (message, context) -> handleServerbound(message, context, handler));
    }

    /**
     * Registers a server to client packet. Client side handlers must treat the content as a hint only:
     * the server is the single source of truth for game state.
     */
    public static <MSG extends CustomPacketPayload> void registerClientbound(
            final PayloadFlow<RegistryFriendlyByteBuf, CustomPacketPayload> flow,
            final Type<MSG> type,
            final StreamCodec<RegistryFriendlyByteBuf, MSG> codec,
            final BiConsumer<MSG, CustomPayloadEvent.Context> handler) {

        flow.add(type, codec, (message, context) -> {
            context.setPacketHandled(true);
            handler.accept(message, context);
        });
    }

    private static <MSG extends CustomPacketPayload> void handleServerbound(
            final MSG message,
            final CustomPayloadEvent.Context context,
            final BiConsumer<MSG, CustomPayloadEvent.Context> handler) {

        context.setPacketHandled(true);

        final ServerPlayer sender = context.getSender();
        if (sender == null) {
            // Not received on the server: nothing to authorise, drop it.
            return;
        }

        if (!SERVERBOUND_LIMITER.tryAccept(sender.getUUID())) {
            if (ModConfig.logThrottledPackets()) {
                UltimateLife.LOGGER.warn("Dropped a packet from {} (rate limit exceeded)", sender.getGameProfile().getName());
            }
            return;
        }

        handler.accept(message, context);
    }

    /** Client to server send helper. No-op when the connection is not established yet. */
    public static void sendToServer(final CustomPacketPayload payload) {
        channel.send(payload, PacketDistributor.SERVER.noArg());
    }

    /** Server to client send helper for a single player. */
    public static void sendToClient(final CustomPacketPayload payload, final ServerPlayer player) {
        if (player.connection != null) {
            channel.send(payload, PacketDistributor.PLAYER.with(player));
        }
    }
}
