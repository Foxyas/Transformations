package net.foxyas.transformations.network;

import net.foxyas.transformations.Transformation;
import net.foxyas.transformations.entities.player.data.TransformationData;
import net.foxyas.transformations.init.TransformationAttachments;
import net.foxyas.transformations.network.packets.TransformationDataSync;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class ClientPacketHandler {

    public static final ClientPacketHandler INSTANCE = new ClientPacketHandler();
    private final Minecraft minecraft = Minecraft.getInstance();

    private ClientPacketHandler(){}

    public void handlePacket(TransformationDataSync packet, @NotNull IPayloadContext context){
        context.enqueueWork(() -> {
            Entity entity = minecraft.level.getEntity(packet.entityId());
            if(!(entity instanceof Player player)) return;//TODO add err message?

            TransformationData data = player.getData(TransformationAttachments.TRANSFORMATION);
            ResourceKey<Transformation> old = data.getForm();
            data.setForm(packet.transformationKey());


            RegistryAccess access = minecraft.getConnection().registryAccess();
            Optional<Holder.Reference<Transformation>> optional = access.get(packet.transformationKey());
            if(optional.isEmpty()) return;

            Transformation transformation = optional.get().value();

            if(old != null){
                Optional<Holder.Reference<Transformation>> optionalO = access.get(packet.transformationKey());
                if(optionalO.isPresent()){
                    Transformation transformationO = optionalO.get().value();
                    //TODO remove model
                }
            }

            //TODO apply model
        });
    }
}
