package net.foxyas.transformations.client.cmrs.renderer;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;

import java.util.function.Function;

public class ExtraRenderTypes {

    public static final RenderPipeline GLOW_SOLID_P = RenderPipeline.builder(RenderPipelines.MATRICES_COLOR_FOG_SNIPPET)
            .withLocation("pipeline/eyes").withVertexShader("core/entity").withFragmentShader("core/entity")
            .withShaderDefine("EMISSIVE").withShaderDefine("NO_OVERLAY").withShaderDefine("NO_CARDINAL_LIGHTING")
            .withSampler("Sampler0").withoutBlend().withDepthWrite(true).withColorWrite(true)
            .withVertexFormat(DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS).build();

    public static final Function<ResourceLocation, RenderType> GLOW_SOLID = Util.memoize((tex) -> RenderType.create(
            "eyes",
            1536,
            false, true,
            GLOW_SOLID_P,
            RenderType.CompositeState.builder()
                    .setTextureState(new RenderStateShard.TextureStateShard(tex, TriState.FALSE, false))
                    .createCompositeState(false)));
}
