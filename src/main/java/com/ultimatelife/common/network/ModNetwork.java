package com.ultimatelife.common.network;

import com.ultimatelife.UltimateLife;
import com.ultimatelife.common.network.handler.TestPacketHandler;
import com.ultimatelife.common.network.packets.TestPingPayload;
import com.ultimatelife.common.network.packets.TestPongPayload;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;

/**
 * Forge networking entry point.
 *
 * <p>Ultimate Life uses Forge's {@link ChannelBuilder}/{@link Channel} API on top of
 * vanilla {@link CustomPacketPayload}s. One channel carries every payload of the mod; new
 * payloads are added to the {@code play()} flow below rather than to a new channel.
 *
 * <h2>Design rules</h2>
 * <ul>
 *   <li><b>Server-authoritative.</b> Client packets are requests, never commands. Handlers
 *       re-validate everything against server state and only then mutate the world.</li>
 *   <li><b>Never trust the client.</b> Every serverbound handler funnels through
 *       {@link com.ultimatelife.common.utility.PacketGuard} before it is allowed to touch
 *       the main thread.</li>
 *   <li><b>One payload per message.</b> Payload records hold only data; behaviour lives in
 *       {@code common.network.handler}.</li>
 * </ul>
 *
 * <p>The channel is required on both ends because the mod adds content. If vanilla clients
 * ever have to be allowed to join, add {@code .optional()} to the builder - but only for
 * payloads that are genuinely safe to skip.
 */
public final class ModNetwork {

    public static final ResourceLocation CHANNEL_NAME =
        ResourceLocation.fromNamespaceAndPath(UltimateLife.MOD_ID, "main");

    /**
     * Protocol version of the channel. Bump whenever a payload's wire format changes; the
     * handshake rejects mismatched peers instead of failing mid-game with a decode error.
     */
    public static final int PROTOCOL_VERSION = 1;

    public static final Channel<CustomPacketPayload> CHANNEL = ChannelBuilder.named(CHANNEL_NAME)
        .networkProtocolVersion(PROTOCOL_VERSION)
        .payloadChannel()
        .play()
        .serverbound()
            .add(TestPingPayload.TYPE, TestPingPayload.STREAM_CODEC, TestPacketHandler::onPing)
        .clientbound()
            .add(TestPongPayload.TYPE, TestPongPayload.STREAM_CODEC, TestPacketHandler::onPong)
        .build();

    private ModNetwork() {
    }

    /**
     * Forces channel construction so Forge sees it while the mod is being loaded.
     *
     * @param modBus the mod event bus, kept as a parameter so future login/configuration
     *               phase payloads can subscribe without changing the call site
     */
    public static void init(final IEventBus modBus) {
        UltimateLife.LOGGER.debug("Network channel {} (protocol v{}) ready", CHANNEL_NAME, PROTOCOL_VERSION);
    }

    // ------------------------------------------------------------------ //
    //  Send helpers - the only place that talks to PacketDistributor
    // ------------------------------------------------------------------ //

    /** Sends a payload from the client to the server it is connected to. */
    public static void sendToServer(final CustomPacketPayload payload) {
        CHANNEL.send(payload, PacketDistributor.SERVER.noArg());
    }

    /** Sends a payload from the server to one player. */
    public static void sendToPlayer(final CustomPacketPayload payload, final ServerPlayer player) {
        CHANNEL.send(payload, PacketDistributor.PLAYER.with(player));
    }

    /** Sends a payload from the server to every connected player. */
    public static void sendToEveryone(final CustomPacketPayload payload) {
        CHANNEL.send(payload, PacketDistributor.ALL.noArg());
    }
}
