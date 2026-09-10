/**
 * Client/server packet records.
 *
 * <p>Reserved for Phase 2 and later: every packet of the mod lives here as an immutable record that
 * implements {@link net.minecraft.network.protocol.common.custom.CustomPacketPayload} and exposes its
 * {@code Type} and {@code StreamCodec}. Packets are registered from
 * {@code com.ultimatelife.common.network.ModNetwork#register()} - a packet that is never registered
 * does not exist as far as the network layer is concerned.</p>
 *
 * <p>Empty in Phase 1 by design (no packet is needed yet).</p>
 */
package com.ultimatelife.common.network.packets;
