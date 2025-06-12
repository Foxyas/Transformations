package net.foxyas.transformations.entities.player;

import net.foxyas.transformations.Transformation;
import net.foxyas.transformations.Transformations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface IPlayerDataExtender {

    void setPlayerTransformation(Transformation transformation);

    Player getSelf(); //Util Method

    TransformationInstance getCurrentTransformation();

    default void addTransformationData(Player player, CompoundTag tag) {
        IPlayerDataExtender ext = (IPlayerDataExtender) player;
        TransformationInstance current = ext.getCurrentTransformation();
        CompoundTag data = new CompoundTag();
        if (current != null && current.getTransformationName() != null) {
            data.putString("TransformationId", current.getTransformationName().toString());
        } else {
            data.putString("TransformationId", Transformations.resourceLocString("empty")); // fall bacl
        }
        tag.put("TransformationData", data);
    }

    default void readTransformationData(Player player,CompoundTag tag) {
        IPlayerDataExtender ext = (IPlayerDataExtender) player;
        if (!tag.contains("TransformationData")) return;
        if (tag.getCompound("TransformationData").isPresent()) {
            CompoundTag data = tag.getCompound("TransformationData").get();

            if (data.contains("TransformationId")) {
                if (data.getString("TransformationId").isPresent()) {
                    ResourceLocation id = ResourceLocation.tryParse(data.getString("TransformationId").get());
                    if (id == null) return;

                    // Aqui o registry já deve estar disponível
                    Transformation transformation = null;//Todo: An Way to Grab an Transformation based on the ID saved on the player data

                    if (transformation != null) {
                        ext.setPlayerTransformation(transformation);

                        // Envia para o cliente (se for servidor)
                        if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                            //Todo: Sync Packet to keep the client and server about the transformation of the player
                            /* Example:
                                TransformationNetworkHandler.TRANSFORMATION_PACKET.send(
                                        new SyncTransformationPacket(transformation.getName()),
                                        serverPlayer.connection.getConnection()
                                );
                            */


                        }
                    }
                }
            }
        }
    }


    //Todo: Make an Server Player mixin to implement such interface
}
