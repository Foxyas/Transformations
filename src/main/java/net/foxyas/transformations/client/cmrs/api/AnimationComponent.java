package net.foxyas.transformations.client.cmrs.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.foxyas.transformations.client.cmrs.geom.ModelPart;

public abstract class AnimationComponent {

    //REGISTRY_CODEC -> lookUp the builtIn animations in animation component registry

    public abstract <E extends LivingEntity> void animate(ModelPart root, E entity, LivingEntityRenderState state, PoseStack poseStack, float partialTick);

    /*public static final class Dynamic extends AnimationComponent {

        public static final StreamCodec<FriendlyByteBuf, Dynamic> CODEC = StreamCodec.composite(
                AnimationDefinition.CODEC,
                dynamic -> dynamic.definition,
                Dynamic::new
        );

        private final AnimationDefinition definition;

        public Dynamic(AnimationDefinition definition){
            this.definition = definition;
        }

        @Override
        public void animate(ModelPart root, AnimationContext context) {
            //KeyframeAnimator.animate(root, context);
        }
    }*/

    //public static class AnimationContext {}
}