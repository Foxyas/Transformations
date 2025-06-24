package net.foxyas.transformations.client.cmrs.properties;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.transformations.client.cmrs.api.CustomModel;
import net.foxyas.transformations.client.cmrs.api.MatrixStack;
import net.foxyas.transformations.client.cmrs.api.RenderLayer;
import net.foxyas.transformations.client.cmrs.geom.ModelPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import org.jetbrains.annotations.NotNull;
//This is bad but it works
public class VanillaHumanoidArmor implements RenderLayer {

    private static VanillaHumanoidArmor instance;
    private static EquipmentLayerRenderer r;

    private static HumanoidModel<?> outer;
    private static HumanoidModel<?> inner;

    public static VanillaHumanoidArmor getInstance(){
        if(instance == null) instance = new VanillaHumanoidArmor();
        return instance;
    }

    @Override
    public <E extends LivingEntity> void render(@NotNull E livingEntity, @NotNull LivingEntityRenderState state, @NotNull CustomModel<E> model, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int packedLight) {
        renderArmorPiece(livingEntity ,model, matrixStack, buffer, getEquipmentIfRenderable(livingEntity, EquipmentSlot.CHEST), EquipmentSlot.CHEST, packedLight);
        renderArmorPiece(livingEntity ,model, matrixStack, buffer, getEquipmentIfRenderable(livingEntity, EquipmentSlot.LEGS), EquipmentSlot.LEGS, packedLight);
        renderArmorPiece(livingEntity ,model, matrixStack, buffer, getEquipmentIfRenderable(livingEntity, EquipmentSlot.FEET), EquipmentSlot.FEET, packedLight);
        renderArmorPiece(livingEntity ,model, matrixStack, buffer, getEquipmentIfRenderable(livingEntity, EquipmentSlot.HEAD), EquipmentSlot.HEAD, packedLight);
    }

    private static ItemStack getEquipmentIfRenderable(LivingEntity entity, EquipmentSlot slot) {
        ItemStack itemstack = entity.getItemBySlot(slot);
        return HumanoidArmorLayer.shouldRender(itemstack, slot) ? itemstack.copy() : ItemStack.EMPTY;
    }

    private static EquipmentLayerRenderer equipmentRenderer(){
        if(r == null) r = new EquipmentLayerRenderer(Minecraft.getInstance().getEntityRenderDispatcher().equipmentAssets, Minecraft.getInstance().getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET));
        return r;
    }

    private static HumanoidModel<?> outer(){
        if(outer == null) outer = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
        return outer;
    }

    private static HumanoidModel<?> inner(){
        if(inner == null) inner = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        return inner;
    }

    private <E extends LivingEntity> void renderArmorPiece(@NotNull E livingEntity, CustomModel<?> model, PoseStack poseStack, MultiBufferSource bufferSource, ItemStack armorItem, EquipmentSlot slot, int packedLight) {
        Equippable equippable = armorItem.get(DataComponents.EQUIPPABLE);
        if(equippable == null) return;

        HumanoidModel<?> armor = slot == EquipmentSlot.LEGS ? inner() : outer();
        copyPropertiesTo(model, armor);
        setPartVisibility(armor, slot);
        EquipmentClientInfo.LayerType equipmentclientinfo$layertype = slot == EquipmentSlot.LEGS
                ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS
                : EquipmentClientInfo.LayerType.HUMANOID;
        MatrixStack.push(poseStack);

        armor.root().getAllParts().forEach(p -> {
            p.x *= -1;
            p.y *= -1;
            if(p == armor.root()) p.y += 24;

            p.xRot *= -1;
            p.yRot *= -1;
        });

        armor.body.y -= 12;//HumanoidAnim is made for models with body.y = 12. vanilla uses 24 so either make a new anim or have this thing here
        if(livingEntity.isCrouching()) {//i'll consider changing the body pivot as it looks a bit incorrectly in third person while crouching & attacking
            armor.body.z -= 6;
            armor.body.y += 2;
        }

        poseStack.scale(-1, -1, 1);
        poseStack.translate(0, -1.5, 0);

        equipmentRenderer()
                .renderLayers(equipmentclientinfo$layertype, equippable.assetId().orElseThrow(), armor, armorItem, poseStack, bufferSource, packedLight);
        MatrixStack.pop(poseStack);
    }

    private void copyPropertiesTo(CustomModel<?> model, HumanoidModel<?> armor){
        ModelPart root = model._root();
        root.copyTo(armor.root());
        root.getDirectChild("head").copyTo(armor.head);
        root.getDirectChild("body").copyTo(armor.body);
        root.getDirectChild("right_arm").copyTo(armor.rightArm);
        root.getDirectChild("left_arm").copyTo(armor.leftArm);
        root.getDirectChild("right_leg").copyTo(armor.rightLeg);
        root.getDirectChild("left_leg").copyTo(armor.leftLeg);
    }

    protected void setPartVisibility(HumanoidModel<?> model, EquipmentSlot slot) {
        model.setAllVisible(false);
        switch (slot) {
            case HEAD:
                model.head.visible = true;
                model.hat.visible = true;
                break;
            case CHEST:
                model.body.visible = true;
                model.rightArm.visible = true;
                model.leftArm.visible = true;
                break;
            case LEGS:
                model.body.visible = true;
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
                break;
            case FEET:
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
        }
    }
}
