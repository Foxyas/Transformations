package net.foxyas.transformations.client.renderer;

import net.foxyas.transformations.entities.player.data.TransformationData;
import net.foxyas.transformations.init.TransformationRenderers;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

public class AuraTransformationRenderer implements TransformationRenderer {

    public AuraTransformationRenderer() {
    }

    @Override
    public boolean cancelVanillaPlayerRender(RenderPlayerEvent.Pre event) {
        return TransformationRenderer.super.cancelVanillaPlayerRender(event);
    }

    @Override
    public void renderPost(RenderPlayerEvent.Post event, Player player, TransformationData transformationData) {
        TransformationRenderer.super.renderPost(event, player, transformationData);
        Level level = player.level();
        spawnParticles(level, ParticleTypes.PORTAL, player.getEyePosition(), new Vec3(0.25f, 0.25f, 0.25f), new Vec3(0, -0.5, 0), 4, 0.25f);
        player.displayClientMessage(Component.literal("HEY IS IS BEING CALLED!!!!!!!!!!"), true);
    }

    public static void spawnParticles(Level level, ParticleOptions particleOptions, Vec3 position, Vec3 offset, Vec3 motion, int count, float speed) {
        // Enviar as partículas
        double XV = motion.x(), YV = motion.y(), ZV = motion.z();
        RandomSource random = level.getRandom();

        for (int i = 0; i < count; ++i) {
            double x = random.nextGaussian() * offset.x;
            double y = random.nextGaussian() * offset.y;
            double z = random.nextGaussian() * offset.z;

            level.addParticle(particleOptions,
                    true,
                    true,
                    position.x() + x,
                    position.y() + y,
                    position.z() + z,
                    XV * speed,
                    YV * speed,
                    ZV * speed);
        }
    }

    @Override
    public void renderPre(RenderPlayerEvent.Pre event, Player player, TransformationData transformationData) {
        TransformationRenderer.super.renderPre(event, player, transformationData);
    }

    @Override
    public String toString() {
        return "AuraTransformationRenderer[" + TransformationRenderers.RENDERERS.getRegistry().get().getId(this) + "]";
    }
}

