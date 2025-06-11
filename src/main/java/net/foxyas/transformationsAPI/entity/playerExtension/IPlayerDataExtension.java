package net.foxyas.transformationsAPI.entity.playerExtension;

import net.foxyas.transformationsAPI.init.TransformationsInit;
import net.foxyas.transformationsAPI.network.TransformationNetworkHandler;
import net.foxyas.transformationsAPI.network.packets.SyncTransformationPacket;
import net.foxyas.transformationsAPI.transformations.Transformation;
import net.foxyas.transformationsAPI.transformations.TransformationInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public interface IPlayerDataExtension {
    @Nullable TransformationInstance getCurrentTransformation();

    void setCurrentTransformation(@Nullable Transformation transformation);

    // Novo campo temporário para salvar o ID
    void setPendingTransformationId(@Nullable String id);

    @Nullable String getPendingTransformationId();

    static void saveTransformationData(Player player, CompoundTag tag) {
        IPlayerDataExtension ext = (IPlayerDataExtension) player;
        TransformationInstance current = ext.getCurrentTransformation();
        CompoundTag data = new CompoundTag();
        if (current != null) {
            data.putString("TransformationId", current.getTransformation().getName().toString());
        } else {
            data.putString("TransformationId", "changed_addon:empty"); // ou outro valor padrão
        }
        tag.put("TransformationData", data);
    }

    static void loadTransformationData(Player player, CompoundTag tag) {
        if (!tag.contains("TransformationData")) return;
        if (tag.getCompound("TransformationData").isPresent()) {
            CompoundTag data = tag.getCompound("TransformationData").get();

            if (data.contains("TransformationId")) {
                if (data.getString("TransformationId").isPresent()) {
                    String id = data.getString("TransformationId").get();
                    ((IPlayerDataExtension) player).setPendingTransformationId(id);
                }
            }
        }
    }

    // Método utilitário para aplicar a transformação no momento certo (como no PlayerTickEvent)
    static void applyPendingTransformation(Player player) {
        IPlayerDataExtension ext = (IPlayerDataExtension) player;
        String idStr = ext.getPendingTransformationId();
        if (idStr == null) return;

        ResourceLocation id = ResourceLocation.tryParse(idStr);
        if (id == null) return;

        // Aqui o registry já deve estar disponível
        Transformation transformation = TransformationsInit.TRANSFORMATIONS_REGISTRY.get().getValue(id);

        if (transformation != null) {
            ext.setCurrentTransformation(transformation);

            // Envia para o cliente (se for servidor)
            if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                TransformationNetworkHandler.TRANSFORMATION_PACKET.send(
                        new SyncTransformationPacket(transformation.getName()),
                        serverPlayer.connection.getConnection()
                );
            }
        }

        ext.setPendingTransformationId(null); // limpar após aplicar
    }
}
