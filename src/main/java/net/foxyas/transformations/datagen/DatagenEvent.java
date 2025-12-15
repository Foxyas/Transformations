package net.foxyas.transformations.datagen;

import com.google.common.collect.ImmutableMultimap;
import com.mojang.serialization.Lifecycle;
import net.foxyas.transformations.Transformations;
import net.foxyas.transformations.client.renderer.AuraTransformationRenderer;
import net.foxyas.transformations.init.TransformationRendererTypes;
import net.foxyas.transformations.transformation.Transformation;
import net.foxyas.transformations.transformation.TransformationBuilder;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = Transformations.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DatagenEvent {

    public static final ResourceKey<Transformation> TEST_TRANSFORMATION = key("test");
    public static final ResourceKey<Transformation> HYPNO_CAT = key("hypno_cat");
    public static final ResourceKey<Transformation> TEST_PLAYER = key("simple_player");
    public static final ResourceKey<Transformation> TEST_AURA = key("test_aura");

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event){
        event.createDatapackRegistryObjects(
                new RegistrySetBuilder()
                        .add(Transformations.TRANSFORMATION_REGISTRY, Lifecycle.stable(), context -> {
                            context.register(TEST_TRANSFORMATION, new TransformationBuilder(ImmutableMultimap.of(), Transformations.resourceLoc("test")).build());
                            context.register(HYPNO_CAT, new TransformationBuilder(ImmutableMultimap.of(), Transformations.resourceLoc("hypno_cat")).build());
                            context.register(TEST_PLAYER, new TransformationBuilder(ImmutableMultimap.of(), Transformations.resourceLoc("player")).build());
                            context.register(TEST_AURA, new TransformationBuilder(ImmutableMultimap.of()).withRenderer(new AuraTransformationRenderer(10, ParticleTypes.END_ROD)).build());
                        })
        );
    }

    private static ResourceKey<Transformation> key(String path){
        return ResourceKey.create(Transformations.TRANSFORMATION_REGISTRY, Transformations.resourceLoc(path));
    }

    //Todo: The path right now is too long and the right path should be like -> data/<ModID>/Transformations/file.json
    //Idk if it would be possible to change it tho
}
