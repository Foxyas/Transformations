package net.foxyas.transformations.client.cmrs.properties;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.transformations.client.cmrs.api.CustomModel;
import net.foxyas.transformations.client.cmrs.api.MatrixStack;
import net.foxyas.transformations.client.cmrs.api.RenderLayerLike;
import net.foxyas.transformations.client.cmrs.model.PoseTransform;
import net.foxyas.transformations.client.cmrs.util.StreamCodecUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ElytraAnimationState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
//probably broken
public final class VanillaElytra implements RenderLayerLike {

    public static final StreamCodec<FriendlyByteBuf, VanillaElytra> CODEC = StreamCodec.of((buffer, vanillaElytra) -> {
            StreamCodecUtils.writeOptionally(vanillaElytra.transform, !vanillaElytra.transform.isEmpty(), buffer, PoseTransform.CODEC);
            StreamCodecUtils.writeOptionally(vanillaElytra.transformFlying, !vanillaElytra.transformFlying.isEmpty(), buffer, PoseTransform.CODEC);
            StreamCodecUtils.writeOptionally(vanillaElytra.transformCrouching, !vanillaElytra.transformCrouching.isEmpty(), buffer, PoseTransform.CODEC);
            boolean babyTransform = vanillaElytra.babyTransform;
            buffer.writeBoolean(babyTransform);
            if(!babyTransform) return;
            StreamCodecUtils.writeOptionally(vanillaElytra.bTransform, !vanillaElytra.bTransform.isEmpty(), buffer, PoseTransform.CODEC);
            StreamCodecUtils.writeOptionally(vanillaElytra.bTransformFlying, !vanillaElytra.bTransformFlying.isEmpty(), buffer, PoseTransform.CODEC);
            StreamCodecUtils.writeOptionally(vanillaElytra.bTransformCrouching, !vanillaElytra.bTransformCrouching.isEmpty(), buffer, PoseTransform.CODEC);
    }, buffer -> {
            PoseTransform transform = StreamCodecUtils.readOptionally(buffer, PoseTransform.CODEC);
            PoseTransform transformFlying = StreamCodecUtils.readOptionally(buffer, PoseTransform.CODEC);
            PoseTransform transformCrouching = StreamCodecUtils.readOptionally(buffer, PoseTransform.CODEC);

            return !buffer.readBoolean() ? new VanillaElytra(transform, transformFlying, transformCrouching)
                    : new VanillaElytra(transform, transformFlying, transformCrouching, true,
                        StreamCodecUtils.readOptionally(buffer, PoseTransform.CODEC),
                        StreamCodecUtils.readOptionally(buffer, PoseTransform.CODEC),
                        StreamCodecUtils.readOptionally(buffer, PoseTransform.CODEC));
            }
    );

    public static final ResourceLocation WINGS_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/elytra.png");
    private final ModelPart root;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final PoseTransform transform;
    private final PoseTransform transformFlying;
    private final PoseTransform transformCrouching;
    private boolean babyTransform;
    private final PoseTransform bTransform;
    private final PoseTransform bTransformFlying;
    private final PoseTransform bTransformCrouching;

    public VanillaElytra(){
        this(null, null, null);
    }

    public VanillaElytra(@Nullable PoseTransform transform, @Nullable PoseTransform transformFlying, @Nullable PoseTransform transformCrouching){
        this(transform, transformFlying, transformCrouching, false, null, null, null);
    }

    public VanillaElytra(@Nullable PoseTransform transform, @Nullable PoseTransform transformFlying, @Nullable PoseTransform transformCrouching, boolean babyTransform, @Nullable PoseTransform bTransform, @Nullable PoseTransform bTransformFlying, @Nullable PoseTransform bTransformCrouching){
        root = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.ELYTRA);
        rightWing = root.getChild("right_wing");
        leftWing = root.getChild("left_wing");

        this.transform = transform == null ? new PoseTransform() : transform;
        this.transformFlying = transformFlying == null ? new PoseTransform() : transformFlying;
        this.transformCrouching = transformCrouching == null ? new PoseTransform() : transformCrouching;
        this.babyTransform = babyTransform;
        this.bTransform = bTransform == null ? new PoseTransform() : bTransform;
        this.bTransformFlying = bTransformFlying == null ? new PoseTransform() : bTransformFlying;
        this.bTransformCrouching = bTransformCrouching == null ? new PoseTransform() : bTransformCrouching;
    }

    @Override
    public <E extends LivingEntity> void render(@NotNull E livingEntity, @NotNull LivingEntityRenderState state, @NotNull CustomModel<E> model, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int packedLight) {
        ItemStack stack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if(!stack.has(DataComponents.GLIDER)) return;

        ResourceLocation texture;
        if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {
            PlayerSkin playerskin = abstractclientplayer.getSkin();
            if (playerskin.elytraTexture() != null) {
                texture = playerskin.elytraTexture();
            } else if (playerskin.capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                texture = playerskin.capeTexture();
            } else {
                texture = WINGS_LOCATION;
            }
        } else {
            texture = WINGS_LOCATION;
        }

        MatrixStack.push(matrixStack);
        matrixStack.scale(-1, -1, 1);//flip since vanilla model is intended to be flipped but CustomModel is not
        matrixStack.translate(0, -1.501, 0.125);

        boolean b = livingEntity.isBaby() && babyTransform;
        PoseTransform transform = livingEntity.isFallFlying() ? b ? bTransformFlying : transformFlying
                : livingEntity.isCrouching() ? b ? bTransformCrouching : transformCrouching
                : b ? bTransform : this.transform;
        transform.apply(matrixStack);

        setupAnim(livingEntity);

        root.render(matrixStack, ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(texture), stack.hasFoil()), packedLight, OverlayTexture.NO_OVERLAY);

        MatrixStack.pop(matrixStack);
    }

    private void setupAnim(LivingEntity entity){
        rightWing.resetPose();
        leftWing.resetPose();
        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(entity.level().tickRateManager().runsNormally());
        ElytraAnimationState state = entity.elytraAnimationState;

        leftWing.y = entity.isCrouching() ? 3.0F : 0.0F;
        leftWing.xRot = state.getRotX(partialTick);
        leftWing.zRot = state.getRotZ(partialTick);
        leftWing.yRot = state.getRotY(partialTick);
        rightWing.yRot = -leftWing.yRot;
        rightWing.y = leftWing.y;
        rightWing.xRot = leftWing.xRot;
        rightWing.zRot = -leftWing.zRot;
    }
}