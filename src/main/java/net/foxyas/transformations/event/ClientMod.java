package net.foxyas.transformations.event;

import net.foxyas.transformations.Transformations;
import net.foxyas.transformations.client.cmrs.event.RegisterBuiltInModelsEvent;
import net.foxyas.transformations.client.models.HypnoCatModel;
import net.foxyas.transformations.client.models.SimplePlayerModel;
import net.foxyas.transformations.client.models.TestModel;
import net.foxyas.transformations.client.renderer.TransformationRendererManager;
import net.foxyas.transformations.init.TransformationRendererTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import static net.foxyas.transformations.datagen.DatagenEvent.TEST_AURA;

@EventBusSubscriber(modid = Transformations.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientMod {

    @SubscribeEvent
    public static void onRegisterModels(RegisterBuiltInModelsEvent event){
        event.registerModelSupplier(Transformations.resourceLoc("test"), TestModel::new);
        event.registerModelSupplier(Transformations.resourceLoc("hypno_cat"), HypnoCatModel::new);
        event.registerModelSupplier(Transformations.resourceLoc("player"), SimplePlayerModel::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        CollectTransformationRenderersEvent collect = new CollectTransformationRenderersEvent();

        ModLoader.postEvent(collect);

        TransformationRendererManager.bake(collect);
    }

    @SubscribeEvent
    public static void onRegisterModels(CollectTransformationRenderersEvent event){
        event.registerRenderer(TEST_AURA, TransformationRendererTypes.AURA.get()::createDefault);
    }
}
