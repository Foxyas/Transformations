package net.foxyas.transformations.process;

import net.foxyas.transformations.Transformation;
import net.foxyas.transformations.entities.player.IPlayerDataExtender;
import net.foxyas.transformations.entities.player.TransformationInstance;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

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
}
