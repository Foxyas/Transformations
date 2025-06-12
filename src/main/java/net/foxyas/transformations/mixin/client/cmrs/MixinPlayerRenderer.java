package net.foxyas.transformations.mixin.client.cmrs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.transformations.client.cmrs.CMRS;
import net.foxyas.transformations.client.cmrs.CustomModelManager;
import net.foxyas.transformations.client.cmrs.model.UniversalCustomModel;
import net.foxyas.transformations.client.cmrs.renderer.CustomModelRenderer;
import net.foxyas.transformations.client.cmrs.renderer.DynamicModelRenderer;
import net.foxyas.transformations.client.cmrs.renderer.RenderStateSidestep;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRenderer extends LivingEntityRenderer<AbstractClientPlayer, PlayerRenderState, PlayerModel> {

    @Unique
    private CustomModelRenderer<LivingEntity, RenderStateSidestep, UniversalCustomModel<LivingEntity, RenderStateSidestep>> cmrs$renderer;

    public MixinPlayerRenderer(EntityRendererProvider.Context context, PlayerModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(at = @At("RETURN"),method = "<init>")
    private void onInit(EntityRendererProvider.Context context, boolean p_174558_, CallbackInfo ci){
        cmrs$renderer = new DynamicModelRenderer<>(context, player -> CustomModelManager.getInstance().getModel((AbstractClientPlayer) player), .5f);
    }

    @Inject(at = @At("HEAD"),
            method = "render(Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            cancellable = true)
    private void onRender(PlayerRenderState state, PoseStack stack, MultiBufferSource source, int packedLight, CallbackInfo ci){
        AbstractClientPlayer player = (AbstractClientPlayer) state.getRenderData(CMRS.LIVING);
        if(cmrs$hasNoCustomModel(player)) return;
        cmrs$renderer.render(cmrs$renderer.createRenderState(player, state.partialTick), stack, source, packedLight);

        ci.cancel();
    }

    @Inject(at = @At(value = "HEAD"),method = "renderRightHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;ZLnet/minecraft/client/player/AbstractClientPlayer;)V",
            cancellable = true)
    private void onRenderRightHand(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ResourceLocation skinTexture, boolean isSleeveVisible, AbstractClientPlayer player, CallbackInfo ci){
        if(cmrs$hasNoCustomModel(player)) return;
        cmrs$renderer.renderHand(player, poseStack, packedLight, HumanoidArm.RIGHT);
        ci.cancel();
    }

    @Inject(at = @At(value = "HEAD"), method = "renderLeftHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;ZLnet/minecraft/client/player/AbstractClientPlayer;)V",
            cancellable = true)
    private void onRenderLeftHand(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ResourceLocation skinTexture, boolean isSleeveVisible, AbstractClientPlayer player, CallbackInfo ci){
        if(cmrs$hasNoCustomModel(player)) return;
        cmrs$renderer.renderHand(player, poseStack, packedLight, HumanoidArm.LEFT);
        ci.cancel();
    }

    @Unique
    private boolean cmrs$hasNoCustomModel(@NotNull AbstractClientPlayer player){
        return !CustomModelManager.getInstance().hasCustomModel(player);
    }
}