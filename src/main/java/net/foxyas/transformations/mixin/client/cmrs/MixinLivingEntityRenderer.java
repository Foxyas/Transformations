package net.foxyas.transformations.mixin.client.cmrs;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.transformations.client.cmrs.CMRS;
import net.foxyas.transformations.client.cmrs.api.CustomModel;
import net.foxyas.transformations.client.cmrs.api.NoYFlip;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<S>> extends EntityRenderer<T, S> {

    @Shadow
    protected M model;

    @Shadow
    public static int getOverlayCoords(LivingEntityRenderState renderState, float overlay) {
        return 0;
    }

    @Shadow protected abstract float getWhiteOverlayProgress(S renderState);

    @Shadow protected abstract boolean isBodyVisible(S renderState);

    @Unique
    protected final List<RenderLayer<S, M>> cmrs$noFlipLayers = new ArrayList<>();

    protected MixinLivingEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), method = "addLayer")
    private <E> boolean addNoYFlipLayer(List<E> instance, E e, Operation<Boolean> original) {
        if (!(e instanceof NoYFlip)) return original.call(instance, e);
        cmrs$noFlipLayers.add((RenderLayer<S, M>) e);
        return true;
    }

    @WrapWithCondition(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V", ordinal = 1),
            method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private boolean noFlip(PoseStack instance, float x, float y, float z) {
        return !(model instanceof NoYFlip);
    }

    @WrapWithCondition(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V", ordinal = 1),
            method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private boolean noTranslate(PoseStack instance, float x, float y, float z) {
        return !(model instanceof NoYFlip);
    }

    @WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;)V"),
            method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private boolean onSetupAnim(M instance, EntityRenderState renderState, @Local(argsOnly = true) PoseStack poseStack){
        if(!(model instanceof CustomModel<?>)) return true;
        ((CustomModel<T>)model).setupAnim((T) renderState.getRenderData(CMRS.LIVING), (LivingEntityRenderState) renderState, poseStack);
        return false;
    }
    
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getRenderType(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/renderer/RenderType;"),
            method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private RenderType renderCustomModel(RenderType original, @Local(argsOnly = true) S state, @Local(argsOnly = true) PoseStack poseStack, @Local(argsOnly = true) MultiBufferSource buffer, @Local(argsOnly = true) int packedLight){
        if(!(model instanceof CustomModel<?> custom)) return original;

        boolean visible = isBodyVisible(state);
        boolean translucent = !visible && !state.isInvisibleToPlayer;
        Function<ResourceLocation, RenderType> func;
        if(original == null){
            func = null;
        } else if(translucent){
            func = RenderType::itemEntityTranslucentCull;
        } else if(visible){
            func = model::renderType;
        } else func = state.appearsGlowing ? RenderType::outline : null;

        ((CustomModel<LivingEntity>)custom).renderToBuffer(state.getRenderData(CMRS.LIVING), poseStack, func, packedLight, getOverlayCoords(state, getWhiteOverlayProgress(state)), translucent ? 654311423 : -1);
        return null;
    }

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;shouldRenderLayers(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Z"),
            method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private boolean renderLayers(boolean original, @Local(argsOnly = true) PoseStack poseStack, @Local(argsOnly = true) MultiBufferSource bufferSource, @Local(argsOnly = true) S state, @Local(argsOnly = true) int light) {
        if (state instanceof PlayerRenderState pl && pl.isSpectator) return true;
        boolean noFlipModel = model instanceof NoYFlip;
        boolean noFlipLayers = !cmrs$noFlipLayers.isEmpty();

        if (noFlipLayers || model instanceof CustomModel<?>) {
            if (!noFlipModel) {
                poseStack.scale(-1, -1, 1);
                poseStack.translate(0, 1.501, 0);
            }
            if(model instanceof CustomModel<?> m) ((CustomModel<LivingEntity>)m).renderLayers(state.getRenderData(CMRS.LIVING), state, poseStack, light);
            if(noFlipLayers) cmrs$noFlipLayers.forEach(layer -> layer.render(poseStack, bufferSource, light, state, state.yRot, state.xRot));
        } else if (!noFlipModel) return original;

        poseStack.scale(-1, -1, 1);
        poseStack.translate(0, -1.501, 0);
        return false;
    }
}