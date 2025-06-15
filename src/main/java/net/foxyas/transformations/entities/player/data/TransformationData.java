package net.foxyas.transformations.entities.player.data;

import com.mojang.serialization.DataResult;
import net.foxyas.transformations.Transformation;
import net.foxyas.transformations.Transformations;
import net.foxyas.transformations.init.TransformationAttachments;
import net.foxyas.transformations.network.packets.TransformationDataSync;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.network.PacketDistributor;
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

    public static TransformationData of(@NotNull Player player){
        return player.getData(TransformationAttachments.TRANSFORMATION);
    }

    public void setForm(@Nullable ResourceKey<Transformation> formId) {
        if(currentForm == formId
                || currentForm != null && formId != null && currentForm.location().equals(formId.location())) return;

        Level level = holder.level();
        if(level.isClientSide){
            this.currentForm = formId;
            return;
        }

        RegistryAccess access = level.registryAccess();
        Transformation transformO = null, transform = null;
        Optional<Holder.Reference<Transformation>> optional;//Potential issue: datapack is updated at runtime -> a transformation might be removed without removing the attributes (remove attributes before server reloads datapacks & reapply after Or save attributes here to remove as necessary?)
                                                            //same might also happen to models on client (old not removed as transformation no longer exists)
        if(currentForm != null){
            optional = access.get(currentForm);
            if(optional.isPresent()) transformO = optional.get().value();
        }

        if(formId != null){
            optional = access.get(formId);
            if(optional.isPresent()) transform = optional.get().value();
        }

        if(transformO == transform) return;//catch a case where one or both of the keys are not bound

        if(transformO != null) {
            Transformation.removeModifiers((ServerPlayer) holder, transformO);
        }

        if(transform != null){
            Transformation.addModifiers((ServerPlayer) holder, transform);
        }

        this.currentForm = formId;
        PacketDistributor.sendToPlayer((ServerPlayer) holder, new TransformationDataSync(holder.getId(), currentForm));
    }

    public void syncPlayers(){
        if(holder.level().isClientSide) return;
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(holder, new TransformationDataSync(holder.getId(), currentForm));
    }

    public void syncPlayer(ServerPlayer receiver){
        PacketDistributor.sendToPlayer(receiver, new TransformationDataSync(holder.getId(), currentForm));
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
//Do not use setForm() here -> will silently fail on world join
            data.currentForm = ResourceKey.create(Transformations.TRANSFORMATION_REGISTRY, result.getOrThrow());
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
