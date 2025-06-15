package net.foxyas.transformations.client.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.foxyas.transformations.client.cmrs.api.AnimationComponent;
import net.foxyas.transformations.client.cmrs.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class SwimAnim extends AnimationComponent {

    private static SwimAnim instance;

    public static SwimAnim getInstance() {
        if(instance == null) instance = new SwimAnim();
        return instance;
    }

    @Override
    public <E extends LivingEntity> void animate(ModelPart root, E entity, LivingEntityRenderState state, PoseStack poseStack, float partialTick) {
        float swimAmount = entity.getSwimAmount(partialTick);
        if(!entity.isFallFlying() && swimAmount > 0.0F) {
            float f3 = entity.isInWater() || entity.isInFluidType((fluidType, height) -> entity.canSwimInFluidType(fluidType)) ? -90.0F - entity.getXRot() : -90.0F;
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(swimAmount, 0.0F, f3)));
            if (entity.isVisuallySwimming()) {
                poseStack.translate(0.0F, -1.0F, 0.3F);
            }
        }
    }
}