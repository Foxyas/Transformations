package net.foxyas.transformationsAPI.network;

import net.foxyas.transformationsAPI.network.packets.SyncTransformationPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkContext;
import net.minecraftforge.network.NetworkProtocol;
import net.minecraftforge.network.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.foxyas.transformationsAPI.TransformationsApi.MODID;

public class TransformationNetworkHandler {
    public static final ChannelBuilder PACKET_HANDLER = ChannelBuilder
            .named(resource("transformations_network"))
            .networkProtocolVersion(1)
            .optional()
            .acceptedVersions((status, version) -> true);

    // Criação do canal
    public static final SimpleChannel TRANSFORMATION_PACKET = PACKET_HANDLER
            .simpleChannel()
            .play()
            .clientbound()
            .add(SyncTransformationPacket.class, SyncTransformationPacket.STREAM_CODEC, (TransformationNetworkHandler::handleSyncTransformation))
            .build();

    public static void register() {
    }

    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    // Handler do pacote
    private static void handleSyncTransformation(SyncTransformationPacket packet, CustomPayloadEvent.Context context) {
        // Processa o pacote no lado do servidor
        // Exemplo: context.getSender() retorna o ServerPlayer
        packet.handle(context.getSender());
        context.setPacketHandled(true);
    }
}