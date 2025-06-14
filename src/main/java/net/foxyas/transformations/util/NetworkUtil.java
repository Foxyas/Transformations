package net.foxyas.transformations.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class NetworkUtil {

    /**
     * Sends a packet to a specific player (server to client).
     */
    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload packet) {
        player.connection.send(packet);
    }

    /**
     * Sends a packet to all players connected to the server (server to clients).
     */
    public static void sendToAllPlayers(MinecraftServer server, CustomPacketPayload packet) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            sendToPlayer(player, packet);
        }
    }

    /**
     * Sends a packet to all players in a specific level (world).
     */
    public static void sendToAllInLevel(ServerLevel level, CustomPacketPayload packet) {
        for (ServerPlayer player : level.players()) {
            sendToPlayer(player, packet);
        }
    }

    /**
     * Sends a packet from the client to the server (used on client side).
     */
    public static void sendToServer(CustomPacketPayload packet) {
        if (Minecraft.getInstance().getConnection() != null) {
            Minecraft.getInstance().getConnection().send(packet);
        }
    }
}