package net.foxyas.transformations.entities.player.data;

import com.mojang.serialization.DataResult;
import net.foxyas.transformations.Transformation;
import net.foxyas.transformations.Transformations;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TransformationData {

    public static final Serializer SERIALIZER = new Serializer();

    private final Player holder;
    private ResourceKey<Transformation> currentForm;

    public TransformationData(@NotNull IAttachmentHolder holder){
        if(!(holder instanceof Player player)) throw new IllegalArgumentException("Only players supported!");
        this.holder = player;
    }

    public void setForm(@Nullable ResourceKey<Transformation> formId) {
        Level level = holder.level();
        if(level.isClientSide){
            this.currentForm = formId;
            return;
        }

        RegistryAccess access = level.registryAccess();
        Optional<Holder.Reference<Transformation>> optional = access.get(formId);
        if(optional.isEmpty()) return;//ignore invalid key

        Transformation transformation = optional.get().value();
        this.currentForm = formId;

        //TODO apply attributes, sync
    }

    public @Nullable ResourceKey<Transformation> getForm() {
        return currentForm;
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, TransformationData> {

        @Override
        public @NotNull TransformationData read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
            TransformationData data = new TransformationData(holder);
            Optional<String> o = tag.getString("transformation");
            if(o.isEmpty()) return data;

            DataResult<ResourceLocation> result = ResourceLocation.read(o.get());
            if(result.isError()) return data;

            data.setForm(ResourceKey.create(Transformations.TRANSFORMATION_REGISTRY, result.getOrThrow()));
            return data;
        }

        @Override
        public @Nullable CompoundTag write(@NotNull TransformationData attachment, HolderLookup.@NotNull Provider provider) {
            CompoundTag tag = new CompoundTag();
            if (attachment.getForm() != null) {
                tag.putString("transformation", attachment.getForm().location().toString());
            }
            return tag;
        }
    }
}
