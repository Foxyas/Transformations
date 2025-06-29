package net.foxyas.transformations.client.cmrs.properties;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.foxyas.transformations.client.cmrs.api.BufferSourceAccess;
import net.foxyas.transformations.client.cmrs.api.CustomModel;
import net.foxyas.transformations.client.cmrs.api.ModelLayer;
import net.foxyas.transformations.client.cmrs.model.RenderStack;
import net.foxyas.transformations.client.cmrs.model.Texture;
import net.foxyas.transformations.client.cmrs.renderer.ExtraRenderTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public final class Glow implements ModelLayer {

    public static final StreamCodec<FriendlyByteBuf, Glow> CODEC = StreamCodec.of((buffer, glow) -> {
        Int2IntArrayMap map = glow.renderIdToTexture;
        buffer.writeVarInt(map.size());
        map.int2IntEntrySet().forEach(entry -> {
            buffer.writeVarInt(entry.getIntKey());
            buffer.writeVarInt(entry.getIntValue());
        });
    }, buffer -> {
        int size = buffer.readVarInt();
        Int2IntArrayMap map = new Int2IntArrayMap(size);
        for(int i = 0; i < size; i++) {
            map.put(buffer.readVarInt(), buffer.readVarInt());
        }
        return new Glow(map);
    });

    private final Int2IntArrayMap renderIdToTexture;

    public Glow(Int2IntArrayMap renderIdToTexture){
        this.renderIdToTexture = renderIdToTexture;
    }

    @Override
    public IntSet renderIds() {
        return renderIdToTexture.keySet();
    }

    @Override
    public void verifyTextures(List<Texture> textures) {
        renderIdToTexture.values().forEach(i -> {
            if(textures.size() <= i) throw new IllegalStateException("");
        });
    }

    @Override
    public boolean shouldRenderInFirstPerson() {
        return true;
    }

    @Override
    public void setupRenderStack(CustomModel<?> model, LivingEntity entity, RenderStack stack, BufferSourceAccess access) {
        access.cmrs$startSubBatch();

        renderIdToTexture.forEach((renderId, textureId) -> {
            Texture texture = model.getTexture(textureId);
            stack.getOrCreate(renderId).add()
                    .setUVRemapped(access.cmrs$getBuffer(ExtraRenderTypes.GLOW_SOLID.apply(texture.getLocation()), 0), texture);
        });
    }
}