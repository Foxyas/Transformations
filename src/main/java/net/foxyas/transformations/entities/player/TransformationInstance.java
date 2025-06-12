package net.foxyas.transformations.entities.player;

import net.foxyas.transformations.Transformation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ConstantDynamic;

public class TransformationInstance {
    @NotNull
    public Transformation transformation;

    @NotNull
    public final Player player;

    public TransformationInstance(@NotNull Player player, @NotNull Transformation transformation) {
        this.player = player;
        this.transformation = transformation;
    }

    public @NotNull Transformation getTransformation() {
        return this.transformation;
    }

    @Nullable
    public ResourceLocation getTransformationName() {
        //Todo: make the id getter for the transformation ID

        return null;
    }

    public void setTransformation(@NotNull Transformation transformation) {
        this.transformation = transformation;
    }
}
