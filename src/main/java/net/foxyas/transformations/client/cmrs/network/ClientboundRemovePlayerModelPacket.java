package net.foxyas.transformations.client.cmrs.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.foxyas.transformations.client.cmrs.CMRS;
import org.jetbrains.annotations.NotNull;

public record ClientboundRemovePlayerModelPacket(int entityId, ResourceLocation modelId, boolean usePriority, int priority, ModelSetReason reason) implements CustomPacketPayload {

    public static final Type<ClientboundRemovePlayerModelPacket> TYPE = new Type<>(CMRS.resourceLoc("clientbound_remove_player_model"));

    public ClientboundRemovePlayerModelPacket(FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readResourceLocation(), buf.readBoolean(), buf);
    }

    public ClientboundRemovePlayerModelPacket(int entityId, ResourceLocation modelId, boolean priorityOnly, FriendlyByteBuf buf){
        this(entityId, modelId, priorityOnly, priorityOnly ? buf.readVarInt() : 0, buf.readEnum(ModelSetReason.class));
    }

    public static final StreamCodec<FriendlyByteBuf, ClientboundRemovePlayerModelPacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeVarInt(packet.entityId);
        buf.writeResourceLocation(packet.modelId);
        buf.writeBoolean(packet.usePriority);
        if(packet.usePriority) buf.writeVarInt(packet.priority);
        buf.writeEnum(packet.reason);
    }, ClientboundRemovePlayerModelPacket::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}