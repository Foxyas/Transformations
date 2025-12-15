package net.foxyas.transformations.init;

import net.foxyas.transformations.Transformations;
import net.foxyas.transformations.client.renderer.AuraTransformationRenderer;
import net.foxyas.transformations.client.renderer.TransformationRenderer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TransformationRenderers {

    public static final ResourceKey<Registry<TransformationRenderer>> REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Transformations.MODID, "transformation_renderer"));

    public static final DeferredRegister<TransformationRenderer> RENDERERS =
            DeferredRegister.create(REGISTRY_KEY, Transformations.MODID);

    public static final DeferredHolder<TransformationRenderer, AuraTransformationRenderer> AURA =
            RENDERERS.register("aura", AuraTransformationRenderer::new);

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(Transformations.MODID, path);
    }
}
