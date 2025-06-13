package net.foxyas.transformations;

import com.google.common.collect.ImmutableMultimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.transformations.util.CodecUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class Transformation {

    public static final Codec<Transformation> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    CodecUtil.ATTRIBUTE_MAP_CODEC.fieldOf("attributes").forGetter(a -> a.modifiers)
            ).apply(builder, Transformation::new));

    protected final ImmutableMultimap<Holder<Attribute>, AttributeModifier> modifiers;
    //ModelProperties modelProperties; <- contains colors, texture id, model id, etc. (model looked up on client & loaded from assets?) 
    /*Todo: [Foxyas: The Client will get the model ID of the tranformation and then load the modoel with such name,
        the Texture will need to be added via an resourcepack or it could have an cache folder that would
         save the image from the server to the client but idk if that would be the best option]*/
    //List<Ability> abilities;

    public Transformation(ImmutableMultimap<Holder<Attribute>, AttributeModifier> modifiers){
        this.modifiers = modifiers;
    }
}
