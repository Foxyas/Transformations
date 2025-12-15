package net.foxyas.transformations.client.renderer;

import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;

public abstract class TransformationRendererType<T extends TransformationRenderer> {

    private final MapCodec<T> codec;
    private final Supplier<TransformationRenderer> defaultInstance;

    public TransformationRendererType(MapCodec<T> codec, Supplier<TransformationRenderer> defaultInstance) {
        this.codec = codec;
        this.defaultInstance = defaultInstance;
    }

    public MapCodec<T> codec() {
        return codec;
    }

    public T createDefault() {
        return (T) defaultInstance.get();
    }
}
