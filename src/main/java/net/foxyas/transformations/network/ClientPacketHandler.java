package net.foxyas.transformations.network;

import net.foxyas.transformations.Transformation;
import net.foxyas.transformations.client.cmrs.CustomModelManager;
import net.foxyas.transformations.client.cmrs.network.ModelSetReason;
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
            assert minecraft.level != null;
            Entity entity = minecraft.level.getEntity(packet.entityId());
            if(!(entity instanceof Player player)) return;//TODO add err message?

            TransformationData data = player.getData(TransformationAttachments.TRANSFORMATION);
            ResourceKey<Transformation> key = data.getForm();
            RegistryAccess access = minecraft.getConnection().registryAccess();
            Optional<Holder.Reference<Transformation>> optional;
            Transformation transform;
            assert minecraft.player != null;
            if(key != null){
                optional = access.get(key);
                if(optional.isPresent()){
                    transform = optional.get().value();

                    CustomModelManager.getInstance().removePlayerModel(minecraft.player, transform.modelId(), 1);
                }
            }

            key = packet.transformationKey();
            data.setForm(key);
            if(key == null) return;

            optional = access.get(key);
            if (optional.isEmpty()) return;
            transform = optional.get().value();

            CustomModelManager.getInstance().setPlayerModel(minecraft.player, transform.modelId(), 1, false, ModelSetReason.MOD);
        });
    }
}
