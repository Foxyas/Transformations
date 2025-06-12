package net.foxyas.transformations.client.cmrs.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.transformations.client.cmrs.RenderUtil;
import net.foxyas.transformations.client.cmrs.api.CustomModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomModelRenderer<E extends LivingEntity, S extends RenderStateSidestep, M extends EntityModel<S> & CustomModel<E>> extends LivingEntityRenderer<E, S, M> {

    protected final EntityRendererProvider.Context context;//Kinda useless. Mostly everything can be statically obtained from Minecraft

    public CustomModelRenderer(@NotNull EntityRendererProvider.Context context, @Nullable M model, float shadowRadius) {
        super(context, model, shadowRadius);
        this.context = context;
    }

//TODO stuck arrows -> getRandomVertex instead of getRandomCube
    @Override
    public void render(@NotNull S state, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        if(model == null){
            RenderUtil.renderLoadingAnim(state.entity, poseStack, state.partialTick, packedLight);
            //no name tag if no model.......
            return;
        }

        super.render(state, poseStack, buffer, packedLight);
    }

    public void renderHand(@NotNull E entity, @NotNull PoseStack stack, int packedLight, @NotNull HumanoidArm arm){
        if(model != null) model.renderHand(entity, stack, packedLight, arm);
    }

    @Override
    protected void setupRotations(@NotNull S entity, @NotNull PoseStack poseStack, float yBodyRot, float scale) {
        super.setupRotations(entity, poseStack, yBodyRot, scale);//TODO replace with AnimationComponents
    }

    protected boolean shouldShowName(@NotNull E entity, double dist) {
        return super.shouldShowName(entity, dist)
                && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull S livingEntityRenderState) {
        return model.getTexture();
    }

    @Override
    public void extractRenderState(@NotNull E p_entity, S reusedState, float partialTick) {
        reusedState.entity = p_entity;
        super.extractRenderState(p_entity, reusedState, partialTick);
    }

    @Override
    public @NotNull S createRenderState() {
        return (S) new RenderStateSidestep();
    }
}