package net.foxyas.transformations.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.jetbrains.annotations.NotNull;

public class renderHandle extends RenderLayer {
    public renderHandle(RenderLayerParent renderer) {
        super(renderer);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i, @NotNull EntityRenderState entityRenderState, float v, float v1) {
        if (entityRenderState instanceof PlayerRenderState playerRenderState) {
            //Todo: Render Handle
        }
    }

    //Foxyas Dev Note: Really mojang? removing the entity from the render method and not adding a get entity in the new? you want to screw modders?
}
