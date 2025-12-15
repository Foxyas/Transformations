package net.foxyas.transformations.event;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.foxyas.transformations.client.renderer.TransformationRenderer;
import net.foxyas.transformations.transformation.Transformation;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.event.IModBusEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CollectTransformationRenderersEvent extends Event implements IModBusEvent {

    public CollectTransformationRenderersEvent() {
    }

    private final Multimap<ResourceKey<Transformation>, Supplier<TransformationRenderer>> renderers = ArrayListMultimap.create();

    public void registerRenderer(ResourceKey<Transformation> transformation, Supplier<TransformationRenderer> renderer) {
        renderers.put(transformation, renderer);
    }

    public Multimap<ResourceKey<Transformation>, Supplier<TransformationRenderer>> getRenderers() {
        return renderers;
    }
}
