package net.foxyas.transformations;

import com.google.common.collect.ImmutableMultimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.transformations.util.CodecUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.*;
import org.jetbrains.annotations.NotNull;

public record Transformation(ImmutableMultimap<Holder<Attribute>, AttributeModifier> modifiers, ResourceLocation modelId) {

    public static final Codec<Transformation> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    CodecUtil.ATTRIBUTE_MAP_CODEC.fieldOf("attributes").forGetter(a -> a.modifiers),
                    ResourceLocation.CODEC.fieldOf("modelId").forGetter(a -> a.modelId)
            ).apply(builder, Transformation::new));

    //ModelProperties modelProperties; <- contains colors, texture id, model id, etc. (model looked up on client & loaded from assets?) 
    /*Todo: [Foxyas: The Client will get the model ID of the tranformation and then load the modoel with such name,
        the Texture will need to be added via an resourcepack or it could have an cache folder that would
         save the image from the server to the client but idk if that would be the best option]*/
    //List<Ability> abilities;

    public static void addModifiers(@NotNull ServerPlayer holder, @NotNull Transformation transfurType){
        AttributeMap map = holder.getAttributes();
        AttributeInstance[] instance = new AttributeInstance[1];
        transfurType.modifiers.asMap().forEach((attribute, modifiers) -> {
            if(!map.hasAttribute(attribute)){
                Transformations.LOGGER.error("Attempted to add form modifier to non existing attribute {}. Form: {}", attribute, transfurType);
                return;
            }

            if(attribute == Attributes.MAX_HEALTH){
                float maxHealthO = holder.getMaxHealth();
                instance[0] = map.getInstance(attribute);
                for(AttributeModifier modifier : modifiers){
                    instance[0].addTransientModifier(modifier);
                }
                float diff = holder.getMaxHealth() - maxHealthO;
                if(diff > 0) holder.setHealth(holder.getHealth() + diff);
                return;
            }

            instance[0] = map.getInstance(attribute);
            for(AttributeModifier modifier : modifiers){
                instance[0].addTransientModifier(modifier);
            }
        });
    }

    public static void removeModifiers(@NotNull ServerPlayer holder, @NotNull Transformation transfurType){
        AttributeMap map = holder.getAttributes();
        AttributeInstance[] instance = new AttributeInstance[1];
        transfurType.modifiers.asMap().forEach((attribute, modifiers) -> {
            if(!map.hasAttribute(attribute)) return;

            if(attribute == Attributes.MAX_HEALTH){
                float maxHealthO = holder.getMaxHealth();
                instance[0] = map.getInstance(attribute);
                for(AttributeModifier modifier : modifiers){
                    instance[0].removeModifier(modifier);
                }
                if(holder.getMaxHealth() - maxHealthO < 0) holder.setHealth(holder.getMaxHealth());
                return;
            }

            instance[0] = map.getInstance(attribute);
            for(AttributeModifier modifier : modifiers){
                instance[0].removeModifier(modifier);
            }
        });
    }
}
