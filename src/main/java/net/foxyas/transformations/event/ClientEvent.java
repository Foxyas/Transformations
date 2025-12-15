package net.foxyas.transformations.event;

import net.foxyas.transformations.Transformations;
import net.foxyas.transformations.client.cmrs.CMRS;
import net.foxyas.transformations.client.renderer.TransformationRenderer;
import net.foxyas.transformations.client.renderer.TransformationRendererManager;
import net.foxyas.transformations.entities.player.data.TransformationData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

import java.util.List;

@EventBusSubscriber(modid = Transformations.MODID, value = Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public static void onPlayerRenderPre(RenderPlayerEvent.Pre event) {
        var playerRenderState = event.getRenderState();

        LivingEntity renderData = playerRenderState.getRenderData(CMRS.LIVING);
        if (!(renderData instanceof Player player)) return;

        TransformationData transformationData = TransformationData.of(player);
        if (transformationData.getForm() == null) return;

        List<? extends TransformationRenderer> renderers = TransformationRendererManager.get(
                transformationData.getForm()
        );

        boolean cancel = false;

        for (TransformationRenderer renderer : renderers) {
            if (renderer.cancelVanillaPlayerRender(event)) {
                cancel = true;
            }
            renderer.renderPre(event, player, transformationData);
        }

        if (cancel) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerRenderPost(RenderPlayerEvent.Post event) {
        var playerRenderState = event.getRenderState();

        LivingEntity renderData = playerRenderState.getRenderData(CMRS.LIVING);
        if (!(renderData instanceof Player player)) return;

        TransformationData transformationData = TransformationData.of(player);
        if (transformationData.getForm() == null) return;

        List<? extends TransformationRenderer> renderers = TransformationRendererManager.get(transformationData.getForm());

        for (TransformationRenderer renderer : renderers) {
            renderer.renderPost(event, player, transformationData);
        }
    }
}
