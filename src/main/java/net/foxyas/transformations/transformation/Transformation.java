package net.foxyas.transformations.transformation;

import com.google.common.collect.ImmutableMultimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.transformations.Transformations;
import net.foxyas.transformations.client.renderer.TransformationRenderer;
import net.foxyas.transformations.client.renderer.TransformationRendererType;
import net.foxyas.transformations.init.TransformationRendererTypes;
import net.foxyas.transformations.util.CodecUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.*;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Transformation {

    public final Optional<ImmutableMultimap<Holder<Attribute>, AttributeModifier>> modifiers;
    public final Optional<TransformationRenderer> renderer;
    public final Optional<ResourceLocation> modelId;


    public Transformation(Optional<ImmutableMultimap<Holder<Attribute>, AttributeModifier>> modifiers,
                          Optional<ResourceLocation> modelId) {
        this.modifiers = modifiers;
        this.modelId = modelId;
        this.renderer = Optional.empty();
    }

    public Transformation(Optional<ImmutableMultimap<Holder<Attribute>, AttributeModifier>> modifiers,
                          Optional<ResourceLocation> modelId, Optional<TransformationRenderer> transformationRenderer) {
        this.modifiers = modifiers;
        this.modelId = modelId;
        this.renderer = transformationRenderer;
    }

    public Holder<Transformation> getHolder() {
        return Holder.direct(this);
    }

    public static final Codec<Transformation> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    CodecUtil.ATTRIBUTE_MAP_CODEC.optionalFieldOf("attributes").forGetter(a -> a.modifiers),
                    ResourceLocation.CODEC.optionalFieldOf("modelId").forGetter(a -> a.modelId),
                    TransformationRendererTypes.CODEC.optionalFieldOf("renderer").forGetter(a -> a.renderer)
            ).apply(builder, Transformation::new));

    public static void addModifiers(@NotNull ServerPlayer holder, @NotNull Transformation transformation) {
        AttributeMap map = holder.getAttributes();
        AttributeInstance[] instance = new AttributeInstance[1];
        transformation.modifiers.ifPresent((attributes) ->
                attributes.asMap().forEach((attribute, modifiers) -> {
                    if (!map.hasAttribute(attribute)) {
                        Transformations.LOGGER.error("Attempted to add form modifier to non existing attribute {}. Form: {}", attribute, transformation);
                        return;
                    }

                    if (attribute == Attributes.MAX_HEALTH) {
                        float maxHealthO = holder.getMaxHealth();
                        instance[0] = map.getInstance(attribute);
                        for (AttributeModifier modifier : modifiers) {
                            if (instance[0] != null) {
                                instance[0].addTransientModifier(modifier);
                            }
                        }
                        float diff = holder.getMaxHealth() - maxHealthO;
                        if (diff > 0) holder.setHealth(holder.getHealth() + diff);
                        return;
                    }

                    instance[0] = map.getInstance(attribute);
                    for (AttributeModifier modifier : modifiers) {
                        if (instance[0] != null) {
                            instance[0].addTransientModifier(modifier);
                        }
                    }
                })
        );
    }

    public static void removeModifiers(@NotNull ServerPlayer holder, @NotNull Transformation transformation) {
        AttributeMap map = holder.getAttributes();
        AttributeInstance[] instance = new AttributeInstance[1];
        transformation.modifiers.ifPresent((attributes) ->
                attributes.asMap().forEach((attribute, modifiers) -> {
                    if (!map.hasAttribute(attribute)) return;

                    if (attribute == Attributes.MAX_HEALTH) {
                        float maxHealthO = holder.getMaxHealth();
                        instance[0] = map.getInstance(attribute);
                        for (AttributeModifier modifier : modifiers) {
                            if (instance[0] != null) {
                                instance[0].removeModifier(modifier);
                            }
                        }
                        if (holder.getMaxHealth() - maxHealthO < 0) holder.setHealth(holder.getMaxHealth());
                        return;
                    }

                    instance[0] = map.getInstance(attribute);
                    for (AttributeModifier modifier : modifiers) {
                        if (instance[0] != null) {
                            instance[0].removeModifier(modifier);
                        }
                    }
                })
        );
    }
}
