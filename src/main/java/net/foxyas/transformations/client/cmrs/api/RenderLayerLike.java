package net.foxyas.transformations.client.cmrs.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public interface RenderLayerLike {

    <E extends LivingEntity> void render(@NotNull E livingEntity, @NotNull LivingEntityRenderState state,
                                         @NotNull CustomModel<E> model, @NotNull PoseStack matrixStack,
                                         @NotNull MultiBufferSource buffer, int packedLight);
}