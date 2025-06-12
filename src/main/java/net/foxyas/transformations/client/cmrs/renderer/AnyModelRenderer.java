package net.foxyas.transformations.client.cmrs.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import net.foxyas.transformations.client.cmrs.api.CustomModel;
import org.jetbrains.annotations.NotNull;

public class AnyModelRenderer <E extends LivingEntity, S extends RenderStateSidestep, M extends EntityModel<S> & CustomModel<E>> extends CustomModelRenderer<E, S, M> {

    public AnyModelRenderer(@NotNull EntityRendererProvider.Context context, float shadowRadius) {
        super(context, null, shadowRadius);
    }

    public void setModel(M model){
        this.model = model;
    }

    public void render(M model, @NotNull E entity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        setModel(model);
        S state = createRenderState(entity, partialTicks);
        super.render(state, poseStack, buffer, packedLight);
        setModel(null);
    }
}
