package net.foxyas.transformations.init;

import com.mojang.serialization.Codec;
import net.foxyas.transformations.Transformations;
import net.foxyas.transformations.client.renderer.AuraTransformationRenderer;
import net.foxyas.transformations.client.renderer.TransformationRenderer;
import net.foxyas.transformations.client.renderer.TransformationRendererType;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class TransformationRendererTypes {

    public static final ResourceKey<Registry<TransformationRendererType<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Transformations.MODID, "transformation_renderer"));
    public static final Registry<TransformationRendererType<?>> RENDERER_TYPE_REGISTRY = new RegistryBuilder<>(REGISTRY_KEY).sync(true).create();

    public static final DeferredRegister<TransformationRendererType<?>> RENDERERS = DeferredRegister.create(REGISTRY_KEY, Transformations.MODID);

    public static Codec<TransformationRenderer> CODEC = RENDERER_TYPE_REGISTRY
            .byNameCodec()
            .dispatch(
                    TransformationRenderer::getType,
                    TransformationRendererType::codec
            );

    public static final DeferredHolder<TransformationRendererType<?>, TransformationRendererType<AuraTransformationRenderer>> AURA =
            RENDERERS.register(
                    "aura",
                    () -> new TransformationRendererType<>(AuraTransformationRenderer.CODEC, () ->  new AuraTransformationRenderer(4, ParticleTypes.PORTAL)) {
                    }
            );
}
