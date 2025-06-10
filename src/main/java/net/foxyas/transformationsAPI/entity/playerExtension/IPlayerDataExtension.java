package net.foxyas.transformationsAPI.entity.playerExtension;

import net.foxyas.transformationsAPI.init.TransformationsInit;
import net.foxyas.transformationsAPI.transformations.Transformation;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

public interface IPlayerDataExtension {
    @Nullable Transformation getCurrentTransformation();
    void setCurrentTransformation(@Nullable Transformation transformation);

    // Chame no evento de save/load player data
    static void saveTransformationData(Player player, CompoundTag tag) {
        IPlayerDataExtension trans = (IPlayerDataExtension) player;
        Transformation current = trans.getCurrentTransformation();
        if (current != null) {
            tag.putString("TransformationId", current.getName().toString());
        }
    }

    static void loadTransformationData(Player player, CompoundTag tag) {
        if (tag.contains("TransformationId") && tag.getString("TransformationId").isPresent()) {
            ResourceLocation id = ResourceLocation.tryParse(tag.getString("TransformationId").get());
            if (id != null) {
                // Obtém o Registry do Transformation via RegistryAccess do level do player
                Transformation transformation = TransformationsInit.TRANSFORMATIONS.getRegistryKey().getOrThrow(player).get().getValue(id);
                if (transformation != null) {
                    ((IPlayerDataExtension) player).setCurrentTransformation(transformation);
                }
            }
        }
    }

}