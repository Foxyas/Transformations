package net.foxyas.transformations.entities.player.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class TransformationData {
    private ResourceLocation currentForm;

    public TransformationData(){
    }

    public void setForm(ResourceLocation formId) {
        this.currentForm = formId;
    }

    public ResourceLocation getForm() {
        return currentForm;
    }
    public static final Codec<TransformationData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.optionalFieldOf("form").forGetter(data -> Optional.ofNullable(data.currentForm))
    ).apply(instance, opt -> {
        TransformationData data = new TransformationData();
        data.setForm(opt.orElse(null));
        return data;
    }));

}
