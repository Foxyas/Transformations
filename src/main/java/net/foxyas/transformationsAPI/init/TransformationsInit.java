package net.foxyas.transformationsAPI.init;

import net.foxyas.transformationsAPI.TransformationsApi;
import net.foxyas.transformationsAPI.transformations.Transformation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;

import java.util.HashMap;
import java.util.function.Supplier;

public class TransformationsInit {
    public static final ResourceKey<Registry<Transformation>> REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(TransformationsApi.MODID, "transformations"));

    public static final DeferredRegister<Transformation> TRANSFORMATIONS =
            DeferredRegister.create(REGISTRY_KEY, TransformationsApi.MODID);

    // Exemplos de registros
    public static final RegistryObject<Transformation> FIRE_TRANSFORMATION =
            TRANSFORMATIONS.register("fire",
                    () -> new Transformation(ResourceLocation.fromNamespaceAndPath(TransformationsApi.MODID, "fire")));

    public static final RegistryObject<Transformation> ICE_TRANSFORMATION =
            TRANSFORMATIONS.register("ice",
                    () -> new Transformation(ResourceLocation.fromNamespaceAndPath(TransformationsApi.MODID, "ice")));

    public static void register(IEventBus eventBus) {
        // Registrar antes de qualquer outro conteúdo
        TRANSFORMATIONS.register(eventBus);

        // Registrar o evento para criar o registry
        eventBus.addListener(TransformationsInit::onNewRegistry);
    }

    private static void onNewRegistry(NewRegistryEvent event) {
        // Configuração do registry builder
        RegistryBuilder<Transformation> builder = new RegistryBuilder<Transformation>()
                .setName(REGISTRY_KEY.location())
                .setMaxID(Integer.MAX_VALUE - 1)
                .setDefaultKey(ResourceLocation.fromNamespaceAndPath(TransformationsApi.MODID, "empty"));

        // Criar o registry
        event.create(builder, registry -> {
            // Callback opcional após criação
        });
    }
}