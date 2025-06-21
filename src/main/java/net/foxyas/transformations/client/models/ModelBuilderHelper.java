package net.foxyas.transformations.client.models;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.foxyas.transformations.client.cmrs.api.ModelPropertyRegistry;
import net.foxyas.transformations.client.cmrs.model.PartTransform;
import net.foxyas.transformations.client.cmrs.model.PoseTransform;
import net.foxyas.transformations.client.cmrs.properties.*;
import net.foxyas.transformations.client.cmrs.util.Int2ObjArrayMap;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import org.joml.Vector3f;

public class ModelBuilderHelper {

    public static void addTextureMap(ModelPropertyMapImpl map, ResourceLocation TEXTURE) {
        map.addLast(ModelPropertyRegistry.REMAP_UV.get(), Unit.INSTANCE);
        map.addLast(ModelPropertyRegistry.TEXTURES.get(), new Textures(Util.make(new Int2ObjArrayMap<>(2), m -> {
            m.put(0, Texture.fromAsset(TEXTURE, 2));
        })));
    }

    public static void addHeadMap(ModelPropertyMapImpl map, String modelPartName) {
        map.addLast(ModelPropertyRegistry.HEAD.get(), modelPartName);
    }


    public static void addFPArmsMap(ModelPropertyMapImpl map) {
        map.addLast(ModelPropertyRegistry.FP_ARMS.get(), new FPArms(
                "right_arm", new PartTransform(false, new Vector3f(-6, 1.5f, -2), false, new Vector3f(Float.POSITIVE_INFINITY, Mth.DEG_TO_RAD * 5, Mth.DEG_TO_RAD * 180), true, new Vector3f(-.1f)),
                "left_arm", new PartTransform(false, new Vector3f(5, 1.5f, 0), false, new Vector3f(Float.POSITIVE_INFINITY, Mth.DEG_TO_RAD * -5, Mth.DEG_TO_RAD * 180), true, new Vector3f(-.1f))
        ));
    }

    public static void addFPArmsMap(ModelPropertyMapImpl map, Vector3f RightArmPose, Vector3f LeftArmPose, Vector3f RightArmRotation, Vector3f LeftArmRotation) {
        //Right arm -> new Vector3f(-6, 1.5f, -2), new Vector3f(Float.POSITIVE_INFINITY, Mth.DEG_TO_RAD * 5, Mth.DEG_TO_RAD * 180);
        //Left arm -> new Vector3f(5, 1.5f, 0), new Vector3f(Float.POSITIVE_INFINITY, Mth.DEG_TO_RAD * -5, Mth.DEG_TO_RAD * 180);

        //Todo: Check this And make it more right ok 0SeNiA0?

        map.addLast(ModelPropertyRegistry.FP_ARMS.get(), new FPArms(
                "right_arm", new PartTransform(false, RightArmPose, false, RightArmRotation, true, new Vector3f(-.1f)),
                "left_arm", new PartTransform(false, LeftArmPose, false, LeftArmRotation, true, new Vector3f(-.1f))
        ));
    }

    public static void addItemInHandLayer(ModelPropertyMapImpl map, Vector3f rightArmPosTransform, Vector3f leftArmPosTransform, Vector3f rightArmPosTransform2, Vector3f leftArmPosTransform2) {
        map.addLast(ModelPropertyRegistry.ITEM_IN_HAND.get(), new ItemInHandLayer(
                "right_arm", new PoseTransform(rightArmPosTransform, null, rightArmPosTransform2),
                "left_arm", new PoseTransform(leftArmPosTransform, null, leftArmPosTransform2)
        ));
    }

    public static void addItemInHandLayer(ModelPropertyMapImpl map) {
        map.addLast(ModelPropertyRegistry.ITEM_IN_HAND.get(), new ItemInHandLayer(
                "right_arm", new PoseTransform(new Vector3f(1/16f, 0, 0), null, new Vector3f(-1, -1, 1)),
                "left_arm", new PoseTransform(new Vector3f(-1/16f, 0, 0), null, new Vector3f(-1, -1, 1))
        ));
    }

    public static void addArmorMap(ModelPropertyMapImpl map) {
        map.addLast(ModelPropertyRegistry.ARMOR.get(), new Armor(Util.make(new Int2ObjectArrayMap<>(), m -> {
            m.put(2, EquipmentSlot.HEAD);
            m.put(3, EquipmentSlot.CHEST);
            m.put(4, EquipmentSlot.LEGS);
            m.put(5, EquipmentSlot.FEET);
        }), new Int2ObjectArrayMap<>(0)));
    }

    public static void addGlowMap(ModelPropertyMapImpl map) {
        map.addLast(ModelPropertyRegistry.GLOW.get(), new Glow(Util.make(new Int2IntArrayMap(), m -> {
            m.put(1, 0);
        })));
    }

    public static void addVanillaStuffMap(ModelPropertyMapImpl map){
        map.addLast(ModelPropertyRegistry.VANILLA_ELYTRA.get(), new VanillaElytra());
        map.addLast(ModelPropertyRegistry.TRIDENT_SPIN_EFFECT.get(), new TridentSpinEffect());
    }
}
