package net.foxyas.transformations.client.models;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class ModelsMap {

    private static Map<ResourceLocation, EntityModel<? extends EntityRenderState>> ModelsMap = new HashMap<>();

    public static EntityModel<? extends EntityRenderState> getModelForRender(ResourceLocation modelLinkedID) {
        return ModelsMap.get(modelLinkedID);
    }

    public static EntityModel<? extends EntityRenderState> addModelForRender(ResourceLocation modelLinkedID, EntityModel<? extends EntityRenderState> model) {
        return ModelsMap.get(modelLinkedID);
    }
}
