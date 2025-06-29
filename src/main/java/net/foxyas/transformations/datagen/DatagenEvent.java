package net.foxyas.transformations.datagen;

import com.google.common.collect.ImmutableMultimap;
import com.mojang.serialization.Lifecycle;
import net.foxyas.transformations.Transformation;
import net.foxyas.transformations.Transformations;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Transformations.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DatagenEvent {

    private static final ResourceKey<Transformation> TEST_TRANSFORMATION = key("test");
    private static final ResourceKey<Transformation> HYPNO_CAT = key("hypno_cat");
    private static final ResourceKey<Transformation> TEST_PLAYER = key("simple_player");

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event){
        event.createDatapackRegistryObjects(
                new RegistrySetBuilder()
                        .add(Transformations.TRANSFORMATION_REGISTRY, Lifecycle.stable(), context -> {
                            context.register(TEST_TRANSFORMATION, new Transformation(ImmutableMultimap.of(), Transformations.resourceLoc("test")));
                            context.register(HYPNO_CAT, new Transformation(ImmutableMultimap.of(), Transformations.resourceLoc("hypno_cat")));
                            context.register(TEST_PLAYER, new Transformation(ImmutableMultimap.of(), Transformations.resourceLoc("player")));
                        })
        );
    }

    private static ResourceKey<Transformation> key(String path){
        return ResourceKey.create(Transformations.TRANSFORMATION_REGISTRY, Transformations.resourceLoc(path));
    }

    //Todo: The path right now is too long and the right path should be like -> data/<ModID>/Transformations/file.json
    //Idk if it would be possible to change it tho
}
