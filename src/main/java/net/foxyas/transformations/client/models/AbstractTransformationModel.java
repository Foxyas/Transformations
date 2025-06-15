package net.foxyas.transformations.client.models;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.foxyas.transformations.Transformations;
import net.foxyas.transformations.client.cmrs.api.ModelPropertyRegistry;
import net.foxyas.transformations.client.cmrs.geom.GroupDefinition;
import net.foxyas.transformations.client.cmrs.geom.ModelDefinition;
import net.foxyas.transformations.client.cmrs.model.PartTransform;
import net.foxyas.transformations.client.cmrs.model.PoseTransform;
import net.foxyas.transformations.client.cmrs.model.UniversalCustomModel;
import net.foxyas.transformations.client.cmrs.properties.*;
import net.foxyas.transformations.client.cmrs.renderer.RenderStateSidestep;
import net.foxyas.transformations.client.cmrs.util.Int2ObjArrayMap;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;

import java.util.List;

public abstract class AbstractTransformationModel  <E extends LivingEntity, S extends RenderStateSidestep> extends UniversalCustomModel<E, S> {

    private static final ResourceLocation TEXTURE = Transformations.textureLoc("model_name");

    public AbstractTransformationModel() {
        super(model().bake(), Util.make(new ModelPropertyMapImpl(), map -> {

            //TextureStuff
            map.addLast(ModelPropertyRegistry.REMAP_UV.get(), Unit.INSTANCE);
            map.addLast(ModelPropertyRegistry.TEXTURES.get(), new Textures(Util.make(new Int2ObjArrayMap<>(2), m -> {
                m.put(0, Texture.fromAsset(TEXTURE, 2));
            })));

            //Head
            map.addLast(ModelPropertyRegistry.HEAD.get(), "head");

            //UI stuff such hands in first person
            map.addLast(ModelPropertyRegistry.FP_ARMS.get(), new FPArms(
                    "right_arm", new PartTransform(false, new Vector3f(-6, 1.5f, -2), false, new Vector3f(Float.POSITIVE_INFINITY, Mth.DEG_TO_RAD * 5, Mth.DEG_TO_RAD * 180), true, new Vector3f(-.1f)),
                    "left_arm", new PartTransform(false, new Vector3f(5, 1.5f, 0), false, new Vector3f(Float.POSITIVE_INFINITY, Mth.DEG_TO_RAD * -5, Mth.DEG_TO_RAD * 180), true, new Vector3f(-.1f))
            ));
            map.addLast(ModelPropertyRegistry.ITEM_IN_HAND.get(), new ItemInHandLayer(
                    "right_arm", new PoseTransform(new Vector3f(1 / 16f, 0, 0), null, new Vector3f(-1, -1, 1)),
                    "left_arm", new PoseTransform(new Vector3f(-1 / 16f, 0, 0), null, new Vector3f(-1, -1, 1))
            ));

            //Armor
            map.addLast(ModelPropertyRegistry.ARMOR.get(), new Armor(Util.make(new Int2ObjectArrayMap<>(), m -> {
                m.put(2, EquipmentSlot.HEAD);
                m.put(3, EquipmentSlot.CHEST);
                m.put(4, EquipmentSlot.LEGS);
                m.put(5, EquipmentSlot.FEET);
            }), new Int2ObjectArrayMap<>(0)));

            //Glow Layer
            map.addLast(ModelPropertyRegistry.GLOW.get(), new Glow(Util.make(new Int2IntArrayMap(), m -> {
                m.put(1, 0);
            })));


            //Extra Layer Stuff
            map.addLast(ModelPropertyRegistry.VANILLA_ELYTRA.get(), new VanillaElytra());
            map.addLast(ModelPropertyRegistry.TRIDENT_SPIN_EFFECT.get(), new TridentSpinEffect());
        }), List.of());
    }

    //Example of a "model"
    public static ModelDefinition model(){
        ModelDefinition.Builder modelBuilder = new ModelDefinition.Builder();
        GroupDefinition groupDefinition = modelBuilder.getRoot();
        return ModelDefinition.create(modelBuilder, 1, 1);
    }

}