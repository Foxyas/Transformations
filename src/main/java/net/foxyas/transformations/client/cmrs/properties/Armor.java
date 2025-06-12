package net.foxyas.transformations.client.cmrs.properties;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.foxyas.transformations.client.cmrs.api.BufferSourceAccess;
import net.foxyas.transformations.client.cmrs.api.CustomModel;
import net.foxyas.transformations.client.cmrs.api.ModelLayer;
import net.foxyas.transformations.client.cmrs.model.RenderStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public final class Armor implements ModelLayer {

    public static final StreamCodec<FriendlyByteBuf, Armor> CODEC = StreamCodec.of((buffer, dynArmor) -> {
        buffer.writeMap(dynArmor.idToType, ByteBufCodecs.VAR_INT, FriendlyByteBuf::writeEnum);
        buffer.writeMap(dynArmor.idToGlow, ByteBufCodecs.VAR_INT, FriendlyByteBuf::writeEnum);
    }, buffer -> new Armor(
            buffer.readMap(Int2ObjectArrayMap::new, ByteBufCodecs.VAR_INT, buf -> buf.readEnum(EquipmentSlot.class)),
            buffer.readMap(Int2ObjectArrayMap::new, ByteBufCodecs.VAR_INT, buf -> buf.readEnum(EquipmentSlot.class)))
    );

    private final Int2ObjectArrayMap<EquipmentSlot> idToType;//TODO combine maps
    private final Int2ObjectArrayMap<EquipmentSlot> idToGlow;
    private final IntSet renderIds;//don't save
    private final Function<EquipmentLayerRenderer.LayerTextureKey, ResourceLocation> layerTextureLookup = Util.memoize(key -> key.layer().getTextureLocation(key.layerType()));
    private final Function<EquipmentLayerRenderer.TrimSpriteKey, TextureAtlasSprite> trimSpriteLookup;

    public Armor(@NotNull Int2ObjectArrayMap<EquipmentSlot> types, @NotNull Int2ObjectArrayMap<EquipmentSlot> glow){
        this.idToType = types;
        this.idToGlow = glow;
        renderIds = new IntArraySet();
        renderIds.addAll(idToType.keySet());
        idToGlow.keySet().forEach(i -> {if(!renderIds.add(i)) throw new IllegalArgumentException("Repeated renderId in armor!");});

        TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET);
        trimSpriteLookup = Util.memoize(key -> atlas.getSprite(key.spriteId()));
    }

    @Override
    public IntSet renderIds() {
        return renderIds;
    }

    @Override
    public void setupRenderStack(CustomModel<?> model, LivingEntity entity, RenderStack stack, BufferSourceAccess access) {
        if(entity.isSpectator()) return;
        //access.cmrs$startSubBatch();

        EquipmentSlot slot;
        ItemStack itemStack;
        RenderStack.ParameterList list;
        for(Int2ObjectMap.Entry<EquipmentSlot> entry : idToType.int2ObjectEntrySet()){
            slot = entry.getValue();
            itemStack = entity.getItemBySlot(slot);

            if(!HumanoidArmorLayer.shouldRender(itemStack, slot) || entity.getEquipmentSlotForItem(itemStack) != slot) continue;
            list = stack.getOrCreate(entry.getIntKey());

            if(!itemStack.is(Items.WOLF_ARMOR) && slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR){//only way to differentiate wolf & horse
                setupArmorPiece(list, access, itemStack, slot, 64, 64);
            } else setupArmorPiece(list, access, itemStack, slot, 64, 32);//TODO add cracks?
        }
        //TODO add glow. where tho?
        //RenderStack.RenderParameters parameters;
        //for(Int2ObjectMap.Entry<EquipmentSlot> entry : idToGlow.int2ObjectEntrySet()){//Do the pass twice because glow buffer has to be created after everything else
        //    slot = entry.getValue();                                                  //Can be moved to setupArmor with getAndRefreshPooledBuffer but isn't used currently anyway
        //    itemStack = entity.getItemBySlot(slot);
        //
        //    if(!HumanoidArmorLayer.shouldRender(itemStack, slot) || entity.getEquipmentSlotForItem(itemStack) != slot) continue;
        //    parameters = stack.getOrCreate(entry.getIntKey()).add();
        //    if(slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR){
        //        parameters
        //            .setUVRemapped(access.cmrs$getBuffer(RenderType.eyes(animalArmor.getTexture()), 3), 64, animalArmor.getBodyType() == AnimalArmorItem.BodyType.EQUESTRIAN ? 64 : 32);
        //    } else parameters//wrap here too
        //            .setUVRemapped(access.cmrs$getBuffer(RenderType.eyes(ClientHooks.getArmorTexture(entity, itemStack, armor.getMaterial().value().layers().getFirst(), slot == EquipmentSlot.LEGS, slot)), 3), 64, 32);
        //}
    }
//TMP make List<List<ByteBufBuilder>> for batches -> first batch(list) is main texture, then overlay list, then tint, then glow
//TMP BufferSource: pushBatch() -> sets int to size of list, getBuffer(renderType, int relativeBatchIndex)
    void setupArmorPiece(RenderStack.ParameterList list, BufferSourceAccess access, ItemStack itemStack, EquipmentSlot slot, int texWidth, int texHeight){
        EquipmentClientInfo.LayerType layerType = slot == EquipmentSlot.LEGS ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS
                : EquipmentClientInfo.LayerType.HUMANOID;

        IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(itemStack);
        EquipmentAssetManager equipmentAssets = Minecraft.getInstance().getEntityRenderDispatcher().equipmentAssets;
        ResourceKey<EquipmentAsset> equipmentAsset = itemStack.get(DataComponents.EQUIPPABLE).assetId().orElseThrow();

        List<EquipmentClientInfo.Layer> list1 = equipmentAssets.get(equipmentAsset).getLayers(layerType);
        if(list1.isEmpty()) return;

        int i = extensions.getDefaultDyeColor(itemStack);
        boolean foil = itemStack.hasFoil();
        int idx = 0;

        ResourceLocation tex;
        for(EquipmentClientInfo.Layer layer : list1) {
            int j = extensions.getArmorLayerTintColor(itemStack, layer, idx, i);
            if (j != 0) {
                tex = ClientHooks.getArmorTexture(itemStack, layerType, layer, layerTextureLookup.apply(new EquipmentLayerRenderer.LayerTextureKey(layerType, layer)));

                list.add()
                        .setUVRemapped(maybeAddFoil(access, RenderType.armorCutoutNoCull(tex), 0, foil, 2), texWidth, texHeight)
                        .setColor(j).setOverlay(OverlayTexture.NO_OVERLAY);
                foil = false;
            }

            ++idx;
        }

        ArmorTrim armortrim = itemStack.get(DataComponents.TRIM);
        if (armortrim != null) {
            TextureAtlasSprite sprite = trimSpriteLookup.apply(new EquipmentLayerRenderer.TrimSpriteKey(armortrim, layerType, equipmentAsset));

            list.add()
                    .setUVRemapped(sprite.wrap(access.cmrs$getBuffer(Sheets.armorTrimsSheet((armortrim.pattern().value()).decal()), 1)), texWidth, texHeight)
                    .setOverlay(OverlayTexture.NO_OVERLAY);
        }
    }

    VertexConsumer maybeAddFoil(BufferSourceAccess access, RenderType renderType, int subBatchInex, boolean foil, int foilIndex){
        return foil
                ? VertexMultiConsumer.create(access.cmrs$getBuffer(RenderType.armorEntityGlint(), foilIndex), access.cmrs$getBuffer(renderType, subBatchInex))
                : access.cmrs$getBuffer(renderType, subBatchInex);
    }
}