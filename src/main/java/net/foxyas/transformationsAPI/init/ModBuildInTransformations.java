package net.foxyas.transformationsAPI.init;

import net.foxyas.transformationsAPI.TransformationsApi;
import net.foxyas.transformationsAPI.transformations.Transformation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

import static net.foxyas.transformationsAPI.init.TransformationsInit.TRANSFORMATIONS;

public class ModBuildInTransformations {

    public static final RegistryObject<Transformation> FORM_DEV =
            TRANSFORMATIONS.register("form_dev_transformation", () ->
                    new Transformation(ResourceLocation.tryParse(TransformationsApi.MODID + ":dev_transformation")));
}
