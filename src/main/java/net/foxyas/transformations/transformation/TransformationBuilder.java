package net.foxyas.transformations.transformation;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TransformationBuilder {
    public ImmutableMultimap<Holder<Attribute>, AttributeModifier> modifiers;
    @Nullable
    public ResourceLocation modelId;

    public TransformationBuilder() {
    }

    public TransformationBuilder(ImmutableMultimap<Holder<Attribute>, AttributeModifier> modifiers, @Nullable ResourceLocation modelId) {
        this.modifiers = modifiers;
        this.modelId = modelId;
    }

    public TransformationBuilder(ImmutableMultimap<Holder<Attribute>, AttributeModifier> modifiers) {
        this.modifiers = modifiers;
    }

    public TransformationBuilder withModelId(@Nullable ResourceLocation modelId) {
        this.modelId = modelId;
        return this;
    }

    public TransformationBuilder withAttributeModifiers(ImmutableMultimap<Holder<Attribute>, AttributeModifier> modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public Transformation build() {
        return new Transformation(Optional.of(this.modifiers), Optional.ofNullable(this.modelId));
    }
}