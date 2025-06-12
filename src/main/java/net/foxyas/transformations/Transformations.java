package net.foxyas.transformations;

import com.mojang.logging.LogUtils;
import net.foxyas.transformations.client.cmrs.api.ModelPropertyRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(Transformations.MODID)
public class Transformations {

    public static final String MODID = "transformations";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceKey<Registry<Transformation>> TRANSFORMATION_REGISTRY = ResourceKey.createRegistryKey(resourceLoc("transformation"));

    public static ResourceLocation resourceLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static String resourceLocString(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path).toString();
    }

    public static ResourceLocation textureLoc(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID,"textures/" + path + ".png");
    }

    public Transformations(IEventBus modEventBus, ModContainer modContainer) {

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        if(FMLEnvironment.dist.isClient()) ModelPropertyRegistry.PROPERTIES.register(modEventBus);//Server doesn't crash
    }
}
