package net.foxyas.transformations.client.cmrs.api;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.world.entity.LivingEntity;
import net.foxyas.transformations.client.cmrs.model.RenderStack;

public interface ModelLayer {

    IntSet renderIds();

    void setupRenderStack(CustomModel<?> model, LivingEntity entity, RenderStack stack, BufferSourceAccess access);
}