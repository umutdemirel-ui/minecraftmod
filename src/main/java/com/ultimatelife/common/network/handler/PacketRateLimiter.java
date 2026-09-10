package com.ultimatelife.common.network.handler;

import com.ultimatelife.common.config.ModConfig;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Sliding window rate limiter used by the server side packet dispatcher.
 *
 * <p>Packet spam protection is a server responsibility: a client can send as many custom payloads as
 * it likes, so every server-bound packet is checked against this limiter before its handler runs.
 * Entries are evicted lazily (when a window is fully expired) and during a periodic sweep, so no
 * tick handler and no event subscription are needed.</p>
 *
 * <p>Instances are thread safe: Forge dispatches payloads on the netty threads.</p>
 */
public final class PacketRateLimiter {

    private static final long WINDOW_NANOS = TimeUnit.SECONDS.toNanos(1);
    private static final long SWEEP_INTERVAL_NANOS = TimeUnit.MINUTES.toNanos(1);

    private final Map<UUID, ArrayDeque<Long>> windows = new HashMap<>();
    private long lastSweep;

    public PacketRateLimiter() {
        this.lastSweep = System.nanoTime();
    }

    /**
     * @return {@code true} when the player is still inside its budget, {@code false} when the packet
     *         must be dropped.
     */
    public synchronized boolean tryAccept(final UUID player) {
        final long now = System.nanoTime();
        sweep(now);

        final ArrayDeque<Long> window = windows.computeIfAbsent(player, key -> new ArrayDeque<>());
        dropExpired(window, now);

        final int budget = ModConfig.maxServerPacketsPerSecond();
        if (window.size() >= budget) {
            return false;
        }

        window.addLast(now);
        return true;
    }

    /** Frees the state of a disconnected player immediately instead of waiting for the next sweep. */
    public synchronized void forget(final UUID player) {
        windows.remove(player);
    }

    public synchronized int trackedPlayers() {
        return windows.size();
    }

    private void sweep(final long now) {
        if (now - lastSweep < SWEEP_INTERVAL_NANOS) {
            return;
        }

        lastSweep = now;
        windows.entrySet().removeIf(entry -> {
            dropExpired(entry.getValue(), now);
            return entry.getValue().isEmpty();
        });
    }

    private static void dropExpired(final ArrayDeque<Long> window, final long now) {
        while (!window.isEmpty() && now - window.peekFirst() > WINDOW_NANOS) {
            window.pollFirst();
        }
    }
}
