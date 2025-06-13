package net.foxyas.transformations.network.packets;

import net.foxyas.transformations.client.cmrs.CMRS;
import net.foxyas.transformations.client.cmrs.CustomModelManager;
import net.foxyas.transformations.client.cmrs.network.ClientboundRemovePlayerModelPacket;
import net.foxyas.transformations.client.cmrs.network.ClientboundSetBuiltInPlayerModelPacket;
import net.foxyas.transformations.client.cmrs.network.ModelSetReason;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class TransformationDataSync implements CustomPacketPayload {

    public static final Type<TransformationDataSync> TYPE = new Type<>(CMRS.resourceLoc("server_board_sync_transformation_data"));
    public final int entityId;
    public final ResourceLocation modelId;
    public final boolean usePriority;
    public final int priority;


    public TransformationDataSync(int entityId, ResourceLocation modelId, boolean usePriority, int priority) {
        this.entityId = entityId;
        this.modelId = modelId;
        this.usePriority = usePriority;
        this.priority = priority;
    }

    public TransformationDataSync(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readResourceLocation(), buf.readBoolean(), buf.readInt());
    }

    public static void handlePacket(TransformationDataSync packet, @NotNull IPayloadContext context){
        context.enqueueWork(() -> {
            Player player = context.player();
            //Todo: Logic Part of the data
        });
    }

    public static final StreamCodec<FriendlyByteBuf, TransformationDataSync> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeVarInt(packet.entityId);
        buf.writeResourceLocation(packet.modelId);
        buf.writeBoolean(packet.usePriority);
        if(packet.usePriority) buf.writeVarInt(packet.priority);
    }, TransformationDataSync::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
