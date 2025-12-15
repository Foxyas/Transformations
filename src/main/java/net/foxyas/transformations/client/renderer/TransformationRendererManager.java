package net.foxyas.transformations.client.renderer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.foxyas.transformations.event.CollectTransformationRenderersEvent;
import net.foxyas.transformations.transformation.Transformation;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.function.Supplier;

public final class TransformationRendererManager {

    private static final Multimap<ResourceKey<Transformation>, Supplier<? extends TransformationRenderer>> RENDERERS = ArrayListMultimap.create();

    private TransformationRendererManager() {}

    public static void bake(CollectTransformationRenderersEvent event) {
        RENDERERS.putAll(event.getRenderers());
    }

    public static List<? extends TransformationRenderer> get(ResourceKey<Transformation> key) {
        return RENDERERS.get(key).stream()
                .map(Supplier::get)
                .toList();
    }
}

