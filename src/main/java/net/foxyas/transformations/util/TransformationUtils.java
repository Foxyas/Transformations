package net.foxyas.transformations.util;

import net.foxyas.transformations.Transformation;
import net.foxyas.transformations.entities.player.data.TransformationData;
import net.foxyas.transformations.init.TransformationAttachments;
import net.foxyas.transformations.network.packets.TransformationDataSync;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for managing a Player's Transformation.
 */
public final class TransformationUtils {

    private TransformationUtils() {
        // Utility class – no instantiation
    }

    /**
     * Sets the Transformation for the given player and syncs it if on the server side.
     *
     * @param player           the player whose transformation to set
     * @param transformation   the transformation ResourceKey to assign (nullable to clear)
     * @param sync             whether to send a packet to clients (only works on server players)
     */
    public static void setTransformation(Player player, @Nullable ResourceKey<Transformation> transformation, boolean sync) {
        //Todo: make it truly set the player form

        player.getData(TransformationAttachments.TRANSFORMATION).setForm(transformation);
        if (sync && player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new TransformationDataSync(serverPlayer.getId(), transformation));
        }
    }

    /**
     * Gets the current Transformation of the given player.
     *
     * @param player the player to query
     * @return the ResourceKey of the player's current transformation, or null if none
     */
    @Nullable
    public static ResourceKey<Transformation> getTransformation(Player player) {
        TransformationData data = player.getData(TransformationAttachments.TRANSFORMATION);
        return data.getForm();
    }

    /**
     * Checks whether the player currently has a transformation.
     *
     * @param player the player to check
     * @return true if the player is transformed
     */
    public static boolean isTransformed(Player player) {
        return getTransformation(player) != null;
    }
}
