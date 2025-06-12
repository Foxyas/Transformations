package net.foxyas.transformations.client.cmrs;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.fml.ModLoader;
import net.foxyas.transformations.client.cmrs.event.RegisterModelDefinitionsEvent;
import net.foxyas.transformations.client.cmrs.geom.ModelDefinition;
import net.foxyas.transformations.client.cmrs.geom.ModelPart;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.NoSuchElementException;

@ParametersAreNonnullByDefault
public final class ModelDefinitionCache {

    private static ModelDefinitionCache instance;
    private final HashMap<ModelLayerLocation, ModelDefinition> modelCache = new HashMap<>();

    public static void init(){
        if(instance != null) throw new IllegalStateException("ModelDefinitionCache already initialized!");
        instance = new ModelDefinitionCache();
    }

    public static ModelDefinitionCache getInstance() {
        if(instance == null) throw new IllegalStateException("ModelDefinitionCache not initialized!");
        return instance;
    }

    private ModelDefinitionCache(){
        ModLoader.postEvent(new RegisterModelDefinitionsEvent(modelCache));
    }

    public ModelPart bake(ModelLayerLocation location){
        ModelDefinition model = modelCache.get(location);
        if(model == null) throw new NoSuchElementException("No model found for key " + location);
        return model.bake();
    }
}