package com.ultimatelife.common.utility;

import com.ultimatelife.UltimateLife;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Per-player, per-action token bucket used to reject packet spam before it reaches the
 * main thread.
 *
 * <p>Call {@link #allow} from the network thread, <em>before</em>
 * {@code Context#enqueueWork}. The map is keyed by player UUID and by action name, so one
 * player spamming a GUI packet cannot starve their own other actions, and one spammer
 * cannot affect anyone else. Entries are dropped on logout ({@link #forget(UUID)}).
 *
 * <p>Thread-safe and allocation-free on the hot path after the first use.
 */
public final class PacketGuard {

    private record Bucket(AtomicLong tokens, AtomicLong lastRefillNanos) {
    }

    private static final Map<String, Bucket> BUCKETS = new ConcurrentHashMap<>();

    private PacketGuard() {
    }

    /**
     * Consumes one token for {@code (player, action)}.
     *
     * @param player           the sender
     * @param action           stable identifier of the packet / action being rate limited
     * @param burst            bucket capacity - how many messages may arrive at once
     * @param refillPerSecond  tokens added per second
     * @return {@code true} if the message is allowed through
     */
    public static boolean allow(final ServerPlayer player, final String action, final int burst, final int refillPerSecond) {
        final String key = player.getUUID() + ":" + action;
        final Bucket bucket = BUCKETS.computeIfAbsent(key,
            ignored -> new Bucket(new AtomicLong(burst), new AtomicLong(System.nanoTime())));

        final long now = System.nanoTime();
        long last = bucket.lastRefillNanos().get();
        // Refill at most once per refill interval to keep this O(1) and jitter-free.
        final long nanosPerToken = 1_000_000_000L / Math.max(1, refillPerSecond);
        final long elapsed = now - last;
        if (elapsed >= nanosPerToken && bucket.lastRefillNanos().compareAndSet(last, now)) {
            final long earned = Math.min(elapsed / nanosPerToken, burst);
            bucket.tokens().updateAndGet(current -> Math.min(burst, current + earned));
        }

        final boolean allowed = bucket.tokens().getAndUpdate(current -> current > 0 ? current - 1 : current) > 0;
        if (!allowed) {
            UltimateLife.LOGGER.warn("Rate limit hit for {} on '{}'", player.getGameProfile().getName(), action);
        }
        return allowed;
    }

    /** Drops every bucket belonging to a disconnected player. Call from the logout event. */
    public static void forget(final UUID playerId) {
        final String prefix = playerId + ":";
        BUCKETS.keySet().removeIf(key -> key.startsWith(prefix));
    }
}
