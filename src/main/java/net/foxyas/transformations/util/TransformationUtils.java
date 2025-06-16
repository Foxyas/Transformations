package net.foxyas.transformations.util;

import net.foxyas.transformations.Transformation;
import net.foxyas.transformations.Transformations;
import net.foxyas.transformations.entities.player.data.TransformationData;
import net.foxyas.transformations.init.TransformationAttachments;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utility class for managing a Player's Transformation.
 */
@ParametersAreNonnullByDefault
public final class TransformationUtils {

    private TransformationUtils() {
        // Utility class – no instantiation
    }

    /**
     * Sets the Transformation for the given player and syncs it if on the server side.
     *
     * @param player         the player whose transformation to set
     * @param transformation the transformation ResourceKey to assign (nullable to clear)
     */
    public static void setTransformation(Player player, @Nullable ResourceKey<Transformation> transformation) {
        //Todo: make it truly set the player form

        player.getData(TransformationAttachments.TRANSFORMATION).setForm(transformation);
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


    /**
     * Returns the Transformation instance associated with the given ResourceKey.
     *
     * @param res   The ResourceKey pointing to a transformation (usually created via ResourceLocation).
     * @param level The LevelAccessor (e.g. ServerLevel) to retrieve registry access from.
     * @return The Transformation if found, or null otherwise.
     */
    @Nullable
    public static Transformation getTransformationViaRes(LevelAccessor level, ResourceKey<Transformation> res) {
        RegistryAccess access = level.registryAccess();
        Optional<Holder.Reference<Transformation>> optional = access.lookupOrThrow(Transformations.TRANSFORMATION_REGISTRY).get(res);
        // Ignore invalid keys
        return optional.map(Holder.Reference::value).orElse(null);
    }

    /**
     * Return The Transformation Itself via an ID and level
     *
     * @param id    TransformationID [File name of transformation json file]
     * @param level LevelAccessor from where the code will search for the transformation [Recommended an ServerLevel]
     */
    @Nullable
    public static Transformation getTransformationViaID(LevelAccessor level, ResourceLocation id) {
        RegistryAccess access = level.registryAccess();
        var formId = ResourceKey.create(Transformations.TRANSFORMATION_REGISTRY, id);
        Optional<Holder.Reference<Transformation>> optional = access.get(formId);
        //ignore invalid key
        return optional.map(Holder.Reference::value).orElse(null);
    }


    public static boolean isTransformationValid(LevelAccessor levelAccessor, Player player) {
        if (levelAccessor.getServer() == null) {
            return false;
        }
        ResourceKey<Transformation> transformation = getTransformation(player);
        if (transformation == null) {
            return false;
        }

        return levelAccessor.getServer().registryAccess()
                .lookupOrThrow(Transformations.TRANSFORMATION_REGISTRY)
                .containsKey(transformation);
    }
}
