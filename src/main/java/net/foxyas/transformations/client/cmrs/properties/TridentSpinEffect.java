package net.foxyas.transformations.client.cmrs.properties;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.transformations.client.cmrs.api.CustomModel;
import net.foxyas.transformations.client.cmrs.api.MatrixStack;
import net.foxyas.transformations.client.cmrs.api.RenderLayerLike;
import net.foxyas.transformations.client.cmrs.model.PoseTransform;
import net.foxyas.transformations.client.cmrs.util.StreamCodecUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TridentSpinEffect implements RenderLayerLike {

    public static final StreamCodec<FriendlyByteBuf, TridentSpinEffect> CODEC = StreamCodec.of((buffer, value) -> {
            StreamCodecUtils.writeOptionally(value.transform, !value.transform.isEmpty(), buffer, PoseTransform.CODEC);
    }, buffer ->
            new TridentSpinEffect(StreamCodecUtils.readOptionally(buffer, PoseTransform.CODEC))
    );

    private final ModelPart root;
    private final ModelPart[] boxes = new ModelPart[2];
    private final PoseTransform transform;

    public TridentSpinEffect(){
        this(null);
    }

    public TridentSpinEffect(@Nullable PoseTransform transform){
        root = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_SPIN_ATTACK);
        boxes[0] = root.getChild("box0");
        boxes[1] = root.getChild("box1");
        this.transform = transform == null ? new PoseTransform() : transform;
    }

    @Override
    public <E extends LivingEntity> void render(@NotNull E livingEntity, @NotNull LivingEntityRenderState state, @NotNull CustomModel<E> model, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int packedLight) {
        if (!livingEntity.isAutoSpinAttack()) return;
        MatrixStack.push(matrixStack);
        matrixStack.scale(-1, -1, 1);
        matrixStack.translate(0, -1.501, 0);
        transform.apply(matrixStack);
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(SpinAttackEffectLayer.TEXTURE));

        for (int i = 0; i < boxes.length; i++) {
            boxes[i].resetPose();
            float f = livingEntity.tickCount * -(45 + (i + 1) * 5);
            boxes[i].yRot = Mth.wrapDegrees(f) * (float) (Math.PI / 180.0);
        }

        root.render(matrixStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        MatrixStack.pop(matrixStack);
    }
}