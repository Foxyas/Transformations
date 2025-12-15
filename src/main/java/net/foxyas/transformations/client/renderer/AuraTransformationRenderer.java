package net.foxyas.transformations.client.renderer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.transformations.entities.player.data.TransformationData;
import net.foxyas.transformations.init.TransformationRendererTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

public class AuraTransformationRenderer implements TransformationRenderer {

    // ─────────────────────────────────────────────
    // CODEC
    // ─────────────────────────────────────────────
    public static final MapCodec<AuraTransformationRenderer> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.INT.optionalFieldOf("rate", 4).forGetter(r -> r.rate),
                    ParticleTypes.CODEC.fieldOf("particle").forGetter(r -> r.particle)
            ).apply(instance, AuraTransformationRenderer::new));

    // ─────────────────────────────────────────────
    // DATA
    // ─────────────────────────────────────────────
    private final int rate;
    private final ParticleOptions particle;

    public AuraTransformationRenderer(int rate, ParticleOptions particle) {
        this.rate = rate;
        this.particle = particle;
    }

    // ─────────────────────────────────────────────
    // RENDER LOGIC
    // ─────────────────────────────────────────────
    @Override
    public TransformationRendererType<?> getType() {
        return TransformationRendererTypes.AURA.get();
    }

    @Override
    public void renderPost(
            RenderPlayerEvent.Post event,
            Player player,
            TransformationData transformationData
    ) {
        Level level = player.level();

        spawnParticles(
                level,
                particle,
                player.getEyePosition(),
                new Vec3(0.25, 0.25, 0.25),
                new Vec3(0, -0.5, 0),
                rate,
                0.25f
        );
    }

    // ─────────────────────────────────────────────
    // UTILS
    // ─────────────────────────────────────────────
    public static void spawnParticles(
            Level level,
            ParticleOptions particleOptions,
            Vec3 position,
            Vec3 offset,
            Vec3 motion,
            int count,
            float speed
    ) {
        RandomSource random = level.getRandom();

        for (int i = 0; i < count; ++i) {
            double x = random.nextGaussian() * offset.x;
            double y = random.nextGaussian() * offset.y;
            double z = random.nextGaussian() * offset.z;

            level.addParticle(
                    particleOptions,
                    true,
                    true,
                    position.x + x,
                    position.y + y,
                    position.z + z,
                    motion.x * speed,
                    motion.y * speed,
                    motion.z * speed
            );
        }
    }

    @Override
    public String toString() {
        return "AuraTransformationRenderer[" +
                TransformationRendererTypes.RENDERERS.getRegistry().get().getId(getType()) +
                ", rate=" + rate +
                ", particle=" + particle.getType() +
                "]";
    }
}
