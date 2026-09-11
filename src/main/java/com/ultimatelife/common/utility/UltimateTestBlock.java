package com.ultimatelife.common.utility;

import com.ultimatelife.common.network.ModNetwork;
import com.ultimatelife.common.network.packets.TestPingPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * <b>Temporary Phase 1 test block.</b>
 *
 * <p>Its only job is to make the foundation observable at runtime:
 * <ul>
 *   <li>registry - it appears in the game and in the creative tab;</li>
 *   <li>resources - blockstate, model, texture and loot table are produced by the data
 *       generator, so a missing file shows up immediately;</li>
 *   <li>network - an empty-hand right click sends {@link TestPingPayload} and the server
 *       replies, proving both directions plus the {@link PacketGuard} rate limiter.</li>
 * </ul>
 *
 * <p>The block itself is a plain cube with no block entity, no tick and no custom
 * behaviour, so deleting it later means deleting one class, one registry entry, one item
 * entry and the two test payloads.
 */
public class UltimateTestBlock extends Block {

    public UltimateTestBlock(final Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(final BlockState state, final Level level, final BlockPos pos,
                                               final Player player, final BlockHitResult hitResult) {
        // Client side: fire the request. The server decides whether it is honoured.
        if (level.isClientSide) {
            ModNetwork.sendToServer(new TestPingPayload(pos.asLong()));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }
}
