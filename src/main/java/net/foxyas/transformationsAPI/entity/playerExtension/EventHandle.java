package net.foxyas.transformationsAPI.entity.playerExtension;

import net.foxyas.transformationsAPI.init.TransformationsInit;
import net.foxyas.transformationsAPI.process.ProcessTransformation;
import net.foxyas.transformationsAPI.transformations.Transformation;
import net.foxyas.transformationsAPI.transformations.TransformationInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.foxyas.transformationsAPI.entity.playerExtension.IPlayerDataExtension.loadTransformationData;

@Mod.EventBusSubscriber
public class EventHandle {

    /*@SubscribeEvent
    public static void eventInject(PlayerEvent.PlayerLoggedInEvent event) {
        loadTransformationData(event.getEntity(), event.getEntity().saveWithoutId(new CompoundTag()));
    }*/

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide()) return;

        IPlayerDataExtension data = (IPlayerDataExtension) event.player;
        String pendingId = data.getPendingTransformationId();

        if (pendingId != null) {
            ResourceLocation id = ResourceLocation.tryParse(pendingId);
            if (id != null) {
                if (!(event.player instanceof ServerPlayer serverPlayer)) return;

                Transformation transformation = TransformationsInit.TRANSFORMATIONS_REGISTRY.get().getValue(id);

                if (transformation != null) {
                    data.setCurrentTransformation(transformation);
                    data.setPendingTransformationId(null); // limpar depois de aplicar
                }
            }
        }
    }

    @SubscribeEvent
    public static void debug(ServerChatEvent event) {
        TransformationInstance transformation = ProcessTransformation.getPlayerTransform(event.getPlayer());
        if (transformation == null) {
            return;
        }
        event.getPlayer().displayClientMessage(Component.literal(transformation.getTransformation().getName().toString()), false);
    }
}