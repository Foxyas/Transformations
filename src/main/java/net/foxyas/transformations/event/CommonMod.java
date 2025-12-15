package net.foxyas.transformations.event;

import net.foxyas.transformations.transformation.Transformation;
import net.foxyas.transformations.Transformations;
import net.foxyas.transformations.init.TransformationRenderers;
import net.foxyas.transformations.network.ClientPacketHandler;
import net.foxyas.transformations.network.packets.TransformationDataSync;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = Transformations.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonMod {

    @SubscribeEvent
    public static void onNewRegistry(DataPackRegistryEvent.NewRegistry event){//mb don't send all data to client(do they need modifiers? those will be added to players by server anyway)
        event.dataPackRegistry(Transformations.TRANSFORMATION_REGISTRY, Transformation.CODEC, Transformation.CODEC);
    }

    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        event.register(TransformationRenderers.RENDERERS.makeRegistry((registryBuilder -> {
            registryBuilder.sync(true);
        })));
    }

    @SubscribeEvent
    public static void onRegisterPayload(RegisterPayloadHandlersEvent event){
        PayloadRegistrar registrar = event.registrar(Transformations.MODID);
        registrar.playToClient(TransformationDataSync.TYPE, TransformationDataSync.CODEC,(payload,context) -> {
            ClientPacketHandler.INSTANCE.handlePacket(payload, context);
        });
    }
}
