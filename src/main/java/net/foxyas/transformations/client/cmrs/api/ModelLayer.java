package net.foxyas.transformations.client.cmrs.api;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.foxyas.transformations.client.cmrs.model.RenderStack;
import net.foxyas.transformations.client.cmrs.model.Texture;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public interface ModelLayer {

    IntSet renderIds();

    void verifyTextures(List<Texture> textures);

    boolean shouldRenderInFirstPerson();

    void setupRenderStack(CustomModel<?> model, LivingEntity entity, RenderStack stack, BufferSourceAccess access);
}