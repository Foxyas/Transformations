package net.foxyas.transformations.network.packets;

import net.foxyas.transformations.Transformation;
import net.foxyas.transformations.Transformations;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record TransformationDataSync(int entityId, @Nullable ResourceKey<Transformation> transformationKey) implements CustomPacketPayload {

    public static final ResourceLocation NULL_LOC = ResourceLocation.fromNamespaceAndPath("null", "null");
    public static final Type<TransformationDataSync> TYPE = new Type<>(Transformations.resourceLoc("server_board_sync_transformation_data"));

    public TransformationDataSync(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readResourceLocation());
    }

    private TransformationDataSync(int entityId, ResourceLocation loc){
        this(entityId, loc.equals(NULL_LOC) ? null : ResourceKey.create(Transformations.TRANSFORMATION_REGISTRY, loc));
    }

    public static final StreamCodec<FriendlyByteBuf, TransformationDataSync> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeVarInt(packet.entityId);
        buf.writeResourceLocation(packet.transformationKey != null ? packet.transformationKey.location() : NULL_LOC);
    }, TransformationDataSync::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
