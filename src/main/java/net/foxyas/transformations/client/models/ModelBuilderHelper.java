package net.foxyas.transformations.client.models;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.foxyas.transformations.client.cmrs.api.ModelPropertyMap;
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
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class ModelBuilderHelper {

    public static void setRemapUV(ModelPropertyMap map){
        map.addLast(ModelPropertyRegistry.REMAP_UV.get(), Unit.INSTANCE);
    }

    public static void addTexture(ModelPropertyMap map, ResourceLocation texture) {
        map.addLast(ModelPropertyRegistry.TEXTURES.get(), new Textures(Util.make(new Int2ObjArrayMap<>(2), m -> {
            m.put(0, Texture.fromAsset(texture, 1));
        })));
    }

    public static void addTexture(ModelPropertyMap map, ResourceLocation texture, float scale) {
        map.addLast(ModelPropertyRegistry.TEXTURES.get(), new Textures(Util.make(new Int2ObjArrayMap<>(2), m -> {
            m.put(0, Texture.fromAsset(texture, scale));
        })));
    }

    public static void addHeadMap(ModelPropertyMap map, String modelPartName) {
        map.addLast(ModelPropertyRegistry.HEAD.get(), modelPartName);
    }


    public static void addFPArms(ModelPropertyMap map) {
        map.addLast(ModelPropertyRegistry.FP_ARMS.get(), new FPArms(
                "right_arm", new PartTransform(false, new Vector3f(-6, 1.5f, -2), false, new Vector3f(Float.POSITIVE_INFINITY, Mth.DEG_TO_RAD * 5, Mth.DEG_TO_RAD * 180), true, new Vector3f(-.1f)),
                "left_arm", new PartTransform(false, new Vector3f(5, 1.5f, 0), false, new Vector3f(Float.POSITIVE_INFINITY, Mth.DEG_TO_RAD * -5, Mth.DEG_TO_RAD * 180), true, new Vector3f(-.1f))
        ));
    }

    public static void addFPArms(ModelPropertyMap map, String rightArm, @Nullable PartTransform rightArmTransform, String leftArm, @Nullable PartTransform leftArmTransform) {
        map.addLast(ModelPropertyRegistry.FP_ARMS.get(), new FPArms(
                rightArm, rightArmTransform,
                leftArm, leftArmTransform
        ));
    }

    public static void addItemInHandLayer(ModelPropertyMap map) {
        map.addLast(ModelPropertyRegistry.ITEM_IN_HAND.get(), new ItemInHandLayer(
                "right_arm", new PoseTransform(new Vector3f(1/16f, 0, 0), null, new Vector3f(-1, -1, 1)),
                "left_arm", new PoseTransform(new Vector3f(-1/16f, 0, 0), null, new Vector3f(-1, -1, 1))
        ));
    }

    public static void addItemInHandLayer(ModelPropertyMap map, String rightArm, @Nullable PoseTransform rightArmTransform, String leftArm, @Nullable PoseTransform leftArmTransform) {
        map.addLast(ModelPropertyRegistry.ITEM_IN_HAND.get(), new ItemInHandLayer(
                rightArm, rightArmTransform,
                leftArm, leftArmTransform
        ));
    }

    public static void addVanillaHumanoidArmor(ModelPropertyMap map){
        map.addLast(ModelPropertyRegistry.VANILLA_ARMOR.get(), VanillaHumanoidArmor.getInstance());
    }

    public static void addHumanoidArmor(ModelPropertyMap map) {
        map.addLast(ModelPropertyRegistry.ARMOR.get(), new Armor(Util.make(new Int2ObjectArrayMap<>(), m -> {
            m.put(2, EquipmentSlot.HEAD);
            m.put(3, EquipmentSlot.CHEST);
            m.put(4, EquipmentSlot.LEGS);
            m.put(5, EquipmentSlot.FEET);
        }), new Int2ObjectArrayMap<>(0)));
    }

    public static void addGlow(ModelPropertyMap map) {
        map.addLast(ModelPropertyRegistry.GLOW.get(), new Glow(Util.make(new Int2IntArrayMap(), m -> {
            m.put(1, 0);
        })));
    }

    public static void addVanillaStuffMap(ModelPropertyMap map){
        map.addLast(ModelPropertyRegistry.VANILLA_ELYTRA.get(), new VanillaElytra());
        map.addLast(ModelPropertyRegistry.TRIDENT_SPIN_EFFECT.get(), new TridentSpinEffect());
    }
}
