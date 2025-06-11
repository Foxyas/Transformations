package net.foxyas.transformations.event;

import net.foxyas.transformations.Transformation;
import net.foxyas.transformations.Transformations;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = Transformations.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonMod {

    @SubscribeEvent
    public static void onNewRegistry(DataPackRegistryEvent.NewRegistry event){//mb don't send all data to client(do they need modifiers? those will be added to players by server anyway)
        event.dataPackRegistry(Transformations.TRANSFORMATION_REGISTRY, Transformation.CODEC, Transformation.CODEC);
    }

    @SubscribeEvent
    public static void onRegisterPayload(RegisterPayloadHandlersEvent event){

    }
}
