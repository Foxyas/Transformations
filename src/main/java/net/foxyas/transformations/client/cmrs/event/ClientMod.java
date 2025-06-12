package net.foxyas.transformations.client.cmrs.event;

import net.foxyas.transformations.client.cmrs.CustomModelManager;
import net.foxyas.transformations.client.cmrs.ModelDefinitionCache;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientMod {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        ModelDefinitionCache.init();
        CustomModelManager.init();
    }
}