package net.foxyas.transformationsAPI.network.packets;

import net.foxyas.transformationsAPI.init.TransformationsInit;
import net.foxyas.transformationsAPI.process.ProcessTransformation;
import net.foxyas.transformationsAPI.transformations.Transformation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SyncTransformationPacket {
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncTransformationPacket> STREAM_CODEC =
            StreamCodec.of(
                    (buf, pkt) -> buf.writeResourceLocation(pkt.transformationId), // encoder
                    (buf1) -> new SyncTransformationPacket(buf1.readResourceLocation()) // decoder
            );

    private final ResourceLocation transformationId;

    public SyncTransformationPacket(ResourceLocation id) {
        this.transformationId = id;
    }

    public ResourceLocation getTransformationId() {
        return transformationId;
    }

    public void handle(@Nullable ServerPlayer player) {
        // Aqui vai a lógica para aplicar a transformação no jogador
        if (player == null) {
            return;
        }
        System.out.println("Servidor recebeu transformação: " + transformationId + " de " + player.getName().getString());
        try {
            Transformation transformation = TransformationsInit.TRANSFORMATIONS_REGISTRY.get().getValue(transformationId);
            ProcessTransformation.TransformPlayer(player, transformation);
        } catch (Exception e) {
            throw e;
        }
    }
}
