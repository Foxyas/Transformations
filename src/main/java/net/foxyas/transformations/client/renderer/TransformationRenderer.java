package net.foxyas.transformations.client.renderer;

import net.foxyas.transformations.entities.player.data.TransformationData;
import net.foxyas.transformations.transformation.Transformation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

public interface TransformationRenderer {

    /**
     * @return true if the renderer will cancel the normal render
     */
    default boolean cancelVanillaPlayerRender(RenderPlayerEvent.Pre event) {
        return false;
    }

    default void renderPre(RenderPlayerEvent.Pre event, Player player, TransformationData transformationData) {

    }

    default void renderPost(RenderPlayerEvent.Post event, Player player, TransformationData transformationData) {

    }
}

