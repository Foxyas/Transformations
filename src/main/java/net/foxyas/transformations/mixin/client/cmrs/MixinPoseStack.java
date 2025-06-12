package net.foxyas.transformations.mixin.client.cmrs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.transformations.client.cmrs.api.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(PoseStack.class)
public abstract class MixinPoseStack implements MatrixStack {

    @Shadow @Final private List<PoseStack.Pose> poses;

    @Shadow public abstract PoseStack.Pose last();

    @Shadow public abstract void pushPose();

    @Shadow private int lastIndex;
    @Unique
    private static final ThreadLocal<List<PoseStack.Pose>> cmrs$posePool = ThreadLocal.withInitial(ArrayList::new);

    @Override
    public void cmrs$push() {
        if(poses.size() > lastIndex) {
            pushPose();
            return;
        }
        PoseStack.Pose matrix = cmrs$posePool.get().removeLast();
        PoseStack.Pose last = last();

        matrix.pose().set(last.pose());
        matrix.normal().set(last.normal());
        matrix.trustedNormals = last.trustedNormals;

        poses.add(matrix);
        lastIndex++;
    }

    @Override
    public void cmrs$pop() {
        cmrs$posePool.get().add(poses.removeLast());
        lastIndex--;
    }
}
