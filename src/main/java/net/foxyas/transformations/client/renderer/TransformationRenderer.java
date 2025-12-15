package net.foxyas.transformations.client.renderer;

import com.mojang.serialization.Codec;
import net.foxyas.transformations.entities.player.data.TransformationData;
import net.foxyas.transformations.init.TransformationRendererTypes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

import java.util.function.Supplier;

public interface TransformationRenderer {

    /**
     * @return true if the renderer will cancel the normal render
     */
    default boolean cancelVanillaPlayerRender(RenderPlayerEvent.Pre event) {
        return false;
    }

    TransformationRendererType<?> getType();

    @OnlyIn(Dist.CLIENT)
    default void renderPre(RenderPlayerEvent.Pre event, Player player, TransformationData transformationData) {

    }

    @OnlyIn(Dist.CLIENT)
    default void renderPost(RenderPlayerEvent.Post event, Player player, TransformationData transformationData) {

    }
}

