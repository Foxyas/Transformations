package net.foxyas.transformations.network.packets;

import net.foxyas.transformations.Transformation;
import net.foxyas.transformations.Transformations;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;

public record TransformationDataSync(int entityId, ResourceKey<Transformation> transformationKey) implements CustomPacketPayload {

    public static final Type<TransformationDataSync> TYPE = new Type<>(Transformations.resourceLoc("server_board_sync_transformation_data"));

    //public final boolean usePriority;         //this stuff (<--) should probably be inside the Transformation itself
    //public final int priority;

    public TransformationDataSync(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readResourceKey(Transformations.TRANSFORMATION_REGISTRY));
    }

    public static final StreamCodec<FriendlyByteBuf, TransformationDataSync> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeVarInt(packet.entityId);
        buf.writeResourceKey(packet.transformationKey);
        //buf.writeBoolean(packet.usePriority);
        //if(packet.usePriority) buf.writeVarInt(packet.priority);
    }, TransformationDataSync::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
