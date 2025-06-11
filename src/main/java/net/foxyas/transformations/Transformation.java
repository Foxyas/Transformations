package net.foxyas.transformations;

import com.google.common.collect.ImmutableMultimap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.foxyas.transformations.util.CodecUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class Transformation {

    public static final Codec<Transformation> CODEC = Codec.of(
       new Encoder<>() {
        @Override
        public <T> DataResult<T> encode(Transformation input, DynamicOps<T> ops, T prefix) {

            ops.set(prefix, "attributes", CodecUtil.ATTRIBUTE_MAP_CODEC.encode(input.modifiers, ops, prefix).getOrThrow());

            return new DataResult.Success<>(prefix, Lifecycle.stable());
        }
    }, new Decoder<>() {
        @Override
        public <T> DataResult<Pair<Transformation, T>> decode(DynamicOps<T> ops, T input) {
            DataResult<T> attrMap = ops.get(input, "attributes");
            if(attrMap.isError()) {// eat error, return empty map
                return new DataResult.Success<>(new Pair<>(new Transformation(ImmutableMultimap.of()), input), Lifecycle.stable());
            }

            return new DataResult.Success<>(new Pair<>(new Transformation(CodecUtil.ATTRIBUTE_MAP_CODEC.decode(ops, attrMap.getOrThrow()).getOrThrow().getFirst()), input), Lifecycle.stable());
        }
    });

    protected final ImmutableMultimap<Holder<Attribute>, AttributeModifier> modifiers;
    //ModelProperties modelProperties; <- contains colors, texture id, model id, etc. (model looked up on client & loaded from assets?)
    //List<Ability> abilities;

    public Transformation(ImmutableMultimap<Holder<Attribute>, AttributeModifier> modifiers){
        this.modifiers = modifiers;
    }
}
