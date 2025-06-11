package net.foxyas.transformationsAPI.transformations;

import net.foxyas.transformationsAPI.init.TransformationsInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class TransformationInstance {

    private final Player player;
    public Transformation transformation = null;

    public TransformationInstance(Player player, Transformation transformation) {
        this.player = player;
        this.transformation = transformation;
    }

    ;

    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }

    public Player getPlayer() {
        return player;
    }

    public Transformation getTransformation() {
        return transformation;
    }

    public CompoundTag additionalData(CompoundTag tag) {
        CompoundTag data = new CompoundTag();
        data.putString("TransformationId", transformation.getName().toString());
        tag.put("TransformationData", data);

        return tag;
    }

    public void readFromTag(CompoundTag tag) {
        CompoundTag compundTag = tag.getCompound("TransformationData").orElse(new CompoundTag());
        if (compundTag.contains("TransformationId") && compundTag.getString("TransformationId").isPresent()) {
            ResourceLocation id = ResourceLocation.tryParse(compundTag.getString("TransformationId").get());
            if (id != null) {
                // Obtém o Registry do Transformation via RegistryAccess do level do player
                this.transformation = TransformationsInit.TRANSFORMATIONS_REGISTRY.get().getValue(id);
            }
        }
    }
}
