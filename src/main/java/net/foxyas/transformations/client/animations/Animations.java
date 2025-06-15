package net.foxyas.transformations.client.animations;

import net.foxyas.transformations.client.cmrs.animation.AnimationDefinition;
import net.foxyas.transformations.client.cmrs.animation.AnimationChannel;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public interface Animations {

    AnimationDefinition EAR_ANIM = AnimationDefinition.Builder.withLength(4.0F).looping()
            .addAnimation("left_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), net.minecraft.client.animation.AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), net.minecraft.client.animation.AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), net.minecraft.client.animation.AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("right_ear", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), net.minecraft.client.animation.AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), net.minecraft.client.animation.AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), net.minecraft.client.animation.AnimationChannel.Interpolations.LINEAR)
            ))
            .build();

    AnimationDefinition TAIL_CAT = AnimationDefinition.Builder.withLength(4f).looping()
            .addAnimation("tail",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(7.5f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("tail0",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("tail1",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("tail2",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(5f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("tail3",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(5f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM)))
            .addAnimation("tail4",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2f, KeyframeAnimations.degreeVec(2.5f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(4f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    net.minecraft.client.animation.AnimationChannel.Interpolations.CATMULLROM))).build();
}
