package net.foxyas.transformations.client.cmrs.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.transformations.client.cmrs.model.Texture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.foxyas.transformations.client.cmrs.geom.ModelPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface CustomModel <E extends LivingEntity> {

    ModelPart _root();

    ModelPart getPart(@NotNull String name);

    boolean hasProperty(@NotNull ModelPropertyType<?> type);

    <P> P getProperty(@NotNull ModelPropertyType<P> type);

    void renderToBuffer(@NotNull E entity, @NotNull PoseStack poseStack, @Nullable Function<ResourceLocation, RenderType> suggestedRenderType, int packedLight, int packedOverlay, int color);

    void renderLayers(@NotNull E entity, @NotNull LivingEntityRenderState state, @NotNull PoseStack poseStack, int packedLight);

    /**
     * For rendering hands in first person view.
     */
    void renderHand(@NotNull E entity, @NotNull PoseStack poseStack, int light, @NotNull HumanoidArm arm);

    void setupAnim(@NotNull E entity, @NotNull LivingEntityRenderState state, @NotNull PoseStack poseStack);

    ResourceLocation getTexture();

    Texture getTexture(int index);
}