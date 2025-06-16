package net.foxyas.transformations.event;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.*;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.StartTracking;

@EventBusSubscriber
public class CommonEvent {

    // Called when a player joins the world (for initialization/sync)
    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // TODO: Sync transformation data on player join
        }
    }

    // Called when a player leaves the world (for cleanup)
    @SubscribeEvent
    public static void onPlayerLeaveWorld(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // TODO: Remove model clientside when player leaves the server
        }
    }

    // Called after the player respawns
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            // TODO: Sync transformation data on player respawn
        }
    }

    // Called when the player changes dimensions
    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // TODO: Sync transformation data on dimension change
        }
    }

    // Called when another player starts tracking this player (i.e. comes into view)
    @SubscribeEvent
    public static void onPlayerStartTracking(StartTracking event) {
        if (event.getTarget() instanceof ServerPlayer tracked && event.getEntity() instanceof ServerPlayer tracker) {
            // TODO: Send transformation data of tracked to tracker
        }
    }
}//TODO sync data on playerJoin, change dimension, respawn and PlayerEvent.StartTracking
//TODO Also remove model clientside on server leave
