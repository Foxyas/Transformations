package net.foxyas.transformations.init;

import net.foxyas.transformations.entities.player.data.TransformationData;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static net.foxyas.transformations.Transformations.MODID;

public class TransformationAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    public static final DeferredHolder<AttachmentType<?>,AttachmentType<TransformationData>> TRANSFORMATION =
        ATTACHMENTS.register("transformation", () ->
            AttachmentType.builder(TransformationData::new)
                .serialize(TransformationData.CODEC) // Optional
                .copyOnDeath()
                .build());
}
