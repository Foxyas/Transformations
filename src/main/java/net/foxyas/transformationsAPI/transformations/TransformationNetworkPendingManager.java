package net.foxyas.transformationsAPI.transformations;

import net.foxyas.transformationsAPI.process.ProcessTransformation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// Classe utilitária
public class TransformationNetworkPendingManager {
    private static final Map<UUID, TransformationInstance> pending = new HashMap<>();

    public static void queueTransformation(ServerPlayer player, TransformationInstance instance) {
        pending.put(player.getUUID(), instance);
    }

    public static void trySend(ServerPlayer player) {
        TransformationInstance transformationInstance = pending.remove(player.getUUID());
        if (transformationInstance != null) {

        }
    }
}
