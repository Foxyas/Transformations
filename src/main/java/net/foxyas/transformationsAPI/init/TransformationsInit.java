package net.foxyas.transformationsAPI.init;

import net.foxyas.transformationsAPI.TransformationsApi;
import net.foxyas.transformationsAPI.transformations.Transformation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TransformationsInit {

    public static final ResourceKey<Registry<Transformation>> TRANSFORMATION_REGISTRY_KEY =
            ResourceKey.createRegistryKey(TransformationsApi.resource("transformations"));

    public static final DeferredRegister<Transformation> TRANSFORMATIONS =
            DeferredRegister.create(TRANSFORMATION_REGISTRY_KEY, TransformationsApi.MODID);

    public static void register(IEventBus bus) {
        TRANSFORMATIONS.register(bus);
    }
}
