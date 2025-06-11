package net.foxyas.transformations.util;

import com.google.common.collect.ImmutableMultimap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.minecraft.core.Holder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodecUtil {

    public static final Codec<ImmutableMultimap<Holder<Attribute>, AttributeModifier>> ATTRIBUTE_MAP_CODEC = Codec.of(
       new Encoder<>() {
        @Override
        public <T> DataResult<T> encode(ImmutableMultimap<Holder<Attribute>, AttributeModifier> input, DynamicOps<T> ops, T prefix) {
            Map<T, T> attrMap = new HashMap<>();

            input.asMap().forEach((attribute, modifier) -> {
                DataResult<T> attr = Attribute.CODEC.encode(attribute, ops, prefix);
                if(attr.isError()) return;//eat entries that failed to encode

                DataResult<T> mod = ExtraCodecs.compactListCodec(AttributeModifier.CODEC).encode((List<AttributeModifier>) modifier, ops, prefix);
                if(mod.isError()) return;

                attrMap.put(attr.getOrThrow(), mod.getOrThrow());
            });

            return new DataResult.Success<>(ops.createMap(attrMap), Lifecycle.stable());
        }
    }, new Decoder<>() {
        @Override
        public <T> DataResult<Pair<ImmutableMultimap<Holder<Attribute>, AttributeModifier>, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getMap(input).flatMap(map -> {
                ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> attributeMap = ImmutableMultimap.builder();

                map.entries().forEach(pair -> {
                    DataResult<Pair<Holder<Attribute>, T>> attr = Attribute.CODEC.decode(ops, pair.getFirst());
                    if(attr.isError()) return;//eat entries that failed to decode

                    DataResult<Pair<List<AttributeModifier>, T>> mod = ExtraCodecs.compactListCodec(AttributeModifier.CODEC).decode(ops, pair.getSecond());
                    if(mod.isError()) return;

                    attributeMap.putAll(attr.getOrThrow().getFirst(), mod.getOrThrow().getFirst());
                });

                return new DataResult.Success<>(new Pair<>(attributeMap.build(), input), Lifecycle.stable());
            });
        }
    });
}
