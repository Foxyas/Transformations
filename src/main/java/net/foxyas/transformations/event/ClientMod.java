package net.foxyas.transformations.event;

import net.foxyas.transformations.Transformations;
import net.foxyas.transformations.client.cmrs.event.RegisterBuiltInModelsEvent;
import net.foxyas.transformations.client.models.HypnoCatModel;
import net.foxyas.transformations.client.models.TestModel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = Transformations.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientMod {

    @SubscribeEvent
    public static void onRegisterModels(RegisterBuiltInModelsEvent event){
        event.registerModelSupplier(Transformations.resourceLoc("test"), TestModel::new);
        event.registerModelSupplier(Transformations.resourceLoc("hypno_cat"), HypnoCatModel::new);
    }
}
