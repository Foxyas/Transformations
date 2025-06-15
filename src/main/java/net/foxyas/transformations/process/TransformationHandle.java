package net.foxyas.transformations.process;

import net.foxyas.transformations.Transformation;
import net.foxyas.transformations.Transformations;
import net.foxyas.transformations.entities.player.IPlayerDataExtender;
import net.foxyas.transformations.entities.player.TransformationInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.logging.Level;

public class TransformationHandle {

    @Nullable
    public static TransformationInstance getPlayerTransformation(Player player) {
        if (player instanceof IPlayerDataExtender iPlayerDataExtender) {
            iPlayerDataExtender.getCurrentTransformation();
        }

        return null;
    }

    public static void setPlayerTransformation(Player player, Transformation transformation) {
        if (player instanceof IPlayerDataExtender iPlayerDataExtender) {
            iPlayerDataExtender.setPlayerTransformation(transformation);
        }
    }

    @Nullable
    public static Transformation getTransformationViaID(LevelAccessor level, ResourceLocation id) {
        RegistryAccess access = level.registryAccess();
        var formId = ResourceKey.create(Transformations.TRANSFORMATION_REGISTRY, id);
        Optional<Holder.Reference<Transformation>> optional = access.get(formId);
        //ignore invalid key
        return optional.map(Holder.Reference::value).orElse(null);

    }
}
