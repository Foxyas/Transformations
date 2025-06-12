package net.foxyas.transformations.client.cmrs;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class CMRS {//TODO ItemOnHead is now inside of RenderState.headItem

    public static final String MODID = "cmrs";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ContextKey<LivingEntity> LIVING = new ContextKey<>(resourceLoc("living_entity"));

    @Contract("_ -> new")
    public static @NotNull ResourceLocation resourceLoc(String namespace){
        return ResourceLocation.fromNamespaceAndPath(MODID, namespace);
    }

    @Contract("_ -> new")
    public static @NotNull ResourceLocation textureLoc(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID,"textures/" + path + ".png");
    }
}