package net.foxyas.transformations.client.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.foxyas.transformations.client.cmrs.api.AnimationComponent;
import net.foxyas.transformations.client.cmrs.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class FallFlyingAnim extends AnimationComponent {

    private static FallFlyingAnim instance;

    public static FallFlyingAnim getInstance() {
        if(instance == null) instance = new FallFlyingAnim();
        return instance;
    }

    @Override
    public <E extends LivingEntity> void animate(ModelPart root, E entity, LivingEntityRenderState state, PoseStack poseStack, float partialTick) {
        if(!entity.isFallFlying() || !(entity instanceof AbstractClientPlayer player)) return;

        if (!player.isAutoSpinAttack()) {
            float f2 = (float) player.getFallFlyingTicks() + entity.tickCount + partialTick;
            float f3 = Mth.clamp(f2 * f2 / 100.0F, 0.0F, 1.0F);
            poseStack.mulPose(Axis.XP.rotationDegrees(f3 * (-90.0F - entity.getViewXRot(partialTick))));
        }

        Vec3 vec3 = player.getViewVector(partialTick);
        Vec3 vec31 = player.getDeltaMovementLerped(partialTick);
        double d0 = vec31.horizontalDistanceSqr();
        double d1 = vec3.horizontalDistanceSqr();
        if (d0 > 0.0 && d1 > 0.0) {
            double d2 = (vec31.x * vec3.x + vec31.z * vec3.z) / Math.sqrt(d0 * d1);
            double d3 = vec31.x * vec3.z - vec31.z * vec3.x;
            poseStack.mulPose(Axis.YP.rotation((float)(Math.signum(d3) * Math.acos(d2))));
        }
    }
}