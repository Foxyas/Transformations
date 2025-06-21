package net.foxyas.transformations.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.foxyas.transformations.Transformations;
import net.foxyas.transformations.client.animations.Animations;
import net.foxyas.transformations.client.animations.FallFlyingAnim;
import net.foxyas.transformations.client.animations.HumanoidAnim;
import net.foxyas.transformations.client.animations.SwimAnim;
import net.foxyas.transformations.client.cmrs.animation.AnimationChannel;
import net.foxyas.transformations.client.cmrs.animation.AnimationDefinition;
import net.foxyas.transformations.client.cmrs.animation.KeyframeAnimator;
import net.foxyas.transformations.client.cmrs.api.ModelPropertyRegistry;
import net.foxyas.transformations.client.cmrs.geom.CubeUV;
import net.foxyas.transformations.client.cmrs.geom.GroupBuilder;
import net.foxyas.transformations.client.cmrs.geom.GroupDefinition;
import net.foxyas.transformations.client.cmrs.geom.ModelDefinition;
import net.foxyas.transformations.client.cmrs.model.PartTransform;
import net.foxyas.transformations.client.cmrs.model.PoseTransform;
import net.foxyas.transformations.client.cmrs.model.UniversalCustomModel;
import net.foxyas.transformations.client.cmrs.properties.*;
import net.foxyas.transformations.client.cmrs.renderer.RenderStateSidestep;
import net.foxyas.transformations.client.cmrs.util.Int2ObjArrayMap;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.List;

public class SimplePlayerModel<E extends LivingEntity, S extends RenderStateSidestep> extends UniversalCustomModel<E, S> {

    private static final ResourceLocation TEXTURE = Transformations.textureLoc("player_model");

    public SimplePlayerModel() {
        super(bodyLayer().bake(), Util.make(new ModelPropertyMapImpl(), map -> {
            ModelBuilderHelper.addTextureMap(map, TEXTURE);
            ModelBuilderHelper.addHeadMap(map, "head");
            ModelBuilderHelper.addFPArmsMap(map);
            ModelBuilderHelper.addItemInHandLayer(map);
            ModelBuilderHelper.addArmorMap(map);
            ModelBuilderHelper.addGlowMap(map);
            ModelBuilderHelper.addVanillaStuffMap(map);
        }), List.of(HumanoidAnim.getInstance(), FallFlyingAnim.getInstance(), SwimAnim.getInstance()));
    }


    @Override
    public void setupAnim(@NotNull E entity, @NotNull LivingEntityRenderState state, @NotNull PoseStack poseStack) {
        super.setupAnim(entity, state, poseStack);
        //float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(entity.level().tickRateManager().runsNormally());
    }

    public static ModelDefinition bodyLayer() {
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();

        GroupDefinition root = groupDefinition.addOrReplaceChild("root", GroupBuilder.create());
        GroupDefinition head = root.addOrReplaceChild("head", GroupBuilder.create(), PartPose.offset(0, 24, 0));
        GroupDefinition hat = head.addOrReplaceChild("hat", GroupBuilder.create());
        hat.addOrReplaceChild("headwear", GroupBuilder.create()
                .addBox(-4f, 24f, -4f, 8, 8, 8, new Vector3f(0.5f), new CubeUV().up(48, 8, 40, 0).south(64, 16, 56, 8).east(40, 16, 32, 8).down(56, 0, 48, 8).west(56, 16, 48, 8).north(48, 16, 40, 8)), PartPose.offset(0, -24, 0));
        head.addOrReplaceChild("head_i0", GroupBuilder.create()
                .addBox(-4f, 24f, -4f, 8, 8, 8, new CubeUV().up(16, 8, 8, 0).south(32, 16, 24, 8).east(8, 16, 0, 8).down(24, 0, 16, 8).west(24, 16, 16, 8).north(16, 16, 8, 8)), PartPose.offset(0, -24, 0));
        GroupDefinition body = root.addOrReplaceChild("body", GroupBuilder.create(), PartPose.offset(0, 24, 0));
        GroupDefinition jacket = body.addOrReplaceChild("jacket", GroupBuilder.create());
        jacket.addOrReplaceChild("jacket_i0", GroupBuilder.create()
                .addBox(-4f, 12f, -2f, 8, 12, 4, new Vector3f(0.25f), new CubeUV().up(28, 36, 20, 32).south(40, 48, 32, 36).east(20, 48, 16, 36).down(36, 32, 28, 36).west(32, 48, 28, 36).north(28, 48, 20, 36)), PartPose.offset(0, -24, 0));
        body.addOrReplaceChild("body_i0", GroupBuilder.create()
                .addBox(-4f, 12f, -2f, 8, 12, 4, new CubeUV().up(28, 20, 20, 16).south(40, 32, 32, 20).east(20, 32, 16, 20).down(36, 16, 28, 20).west(32, 32, 28, 20).north(28, 32, 20, 20)), PartPose.offset(0, -24, 0));
        GroupDefinition left_arm = root.addOrReplaceChild("left_arm", GroupBuilder.create(), PartPose.offset(-5, 22, 0));
        GroupDefinition left_sleeve = left_arm.addOrReplaceChild("left_sleeve", GroupBuilder.create());
        left_sleeve.addOrReplaceChild("left_sleeve_i0", GroupBuilder.create()
                .addBox(-8f, 12f, -2f, 4, 12, 4, new Vector3f(0.25f), new CubeUV().up(56, 52, 52, 48).south(64, 64, 60, 52).east(52, 64, 48, 52).down(60, 48, 56, 52).west(60, 64, 56, 52).north(56, 64, 52, 52)), PartPose.offset(5, -22, 0));
        left_arm.addOrReplaceChild("left_arm_i0", GroupBuilder.create()
                .addBox(-8f, 12f, -2f, 4, 12, 4, new CubeUV().up(40, 52, 36, 48).south(48, 64, 44, 52).east(36, 64, 32, 52).down(44, 48, 40, 52).west(44, 64, 40, 52).north(40, 64, 36, 52)), PartPose.offset(5, -22, 0));
        GroupDefinition right_arm = root.addOrReplaceChild("right_arm", GroupBuilder.create(), PartPose.offset(5, 22, 0));
        GroupDefinition right_sleeve = right_arm.addOrReplaceChild("right_sleeve", GroupBuilder.create());
        right_sleeve.addOrReplaceChild("right_sleeve_i0", GroupBuilder.create()
                .addBox(4f, 12f, -2f, 4, 12, 4, new Vector3f(0.25f), new CubeUV().up(48, 36, 44, 32).south(56, 48, 52, 36).east(44, 48, 40, 36).down(52, 32, 48, 36).west(52, 48, 48, 36).north(48, 48, 44, 36)), PartPose.offset(-5, -22, 0));
        right_arm.addOrReplaceChild("right_arm_i0", GroupBuilder.create()
                .addBox(4f, 12f, -2f, 4, 12, 4, new CubeUV().up(48, 20, 44, 16).south(56, 32, 52, 20).east(44, 32, 40, 20).down(52, 16, 48, 20).west(52, 32, 48, 20).north(48, 32, 44, 20)), PartPose.offset(-5, -22, 0));
        GroupDefinition left_leg = root.addOrReplaceChild("left_leg", GroupBuilder.create(), PartPose.offset(-2, 12, 0));
        GroupDefinition left_pants = left_leg.addOrReplaceChild("left_pants", GroupBuilder.create());
        left_pants.addOrReplaceChild("left_pants_i0", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 4, 12, 4, new Vector3f(0.25f), new CubeUV().up(8, 52, 4, 48).south(16, 64, 12, 52).east(4, 64, 0, 52).down(12, 48, 8, 52).west(12, 64, 8, 52).north(8, 64, 4, 52)), PartPose.offset(2, -12, 0));
        left_leg.addOrReplaceChild("left_leg_i0", GroupBuilder.create()
                .addBox(-4f, 0f, -2f, 4, 12, 4, new CubeUV().up(24, 52, 20, 48).south(32, 64, 28, 52).east(20, 64, 16, 52).down(28, 48, 24, 52).west(28, 64, 24, 52).north(24, 64, 20, 52)), PartPose.offset(2, -12, 0));
        GroupDefinition right_leg = root.addOrReplaceChild("right_leg", GroupBuilder.create(), PartPose.offset(2, 12, 0));
        GroupDefinition right_pants = right_leg.addOrReplaceChild("right_pants", GroupBuilder.create());
        right_pants.addOrReplaceChild("right_pants_i0", GroupBuilder.create()
                .addBox(0f, 0f, -2f, 4, 12, 4, new Vector3f(0.25f), new CubeUV().up(8, 36, 4, 32).south(16, 48, 12, 36).east(4, 48, 0, 36).down(12, 32, 8, 36).west(12, 48, 8, 36).north(8, 48, 4, 36)), PartPose.offset(-2, -12, 0));
        right_leg.addOrReplaceChild("right_leg_i0", GroupBuilder.create()
                .addBox(0f, 0f, -2f, 4, 12, 4, new CubeUV().up(8, 20, 4, 16).south(16, 32, 12, 20).east(4, 32, 0, 20).down(12, 16, 8, 20).west(12, 32, 8, 20).north(8, 32, 4, 20)), PartPose.offset(-2, -12, 0));

        return ModelDefinition.create(modelBuilder, 1, 1);
    }
}
