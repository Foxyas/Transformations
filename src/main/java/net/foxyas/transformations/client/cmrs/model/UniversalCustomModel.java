package net.foxyas.transformations.client.cmrs.model;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectLinkedOpenHashMap;
import net.foxyas.transformations.client.cmrs.api.*;
import net.foxyas.transformations.client.cmrs.geom.ModelPart;
import net.foxyas.transformations.client.cmrs.properties.FPArms;
import net.foxyas.transformations.client.cmrs.properties.Glow;
import net.foxyas.transformations.client.cmrs.properties.ModelPropertyMapImpl;
import net.foxyas.transformations.client.cmrs.renderer.RenderStateSidestep;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class UniversalCustomModel<E extends LivingEntity, S extends RenderStateSidestep> extends EntityModel<S> implements NoYFlip, CustomModel<E> {

    protected final ModelPart root;
    protected final ModelPropertyMap propertyMap;
    protected final List<AnimationComponent> animations;
    protected RenderStack stack;

    public UniversalCustomModel(@NotNull ModelPart root, @NotNull Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> properties){
        this(root, properties, new ArrayList<>(1));
    }

    public UniversalCustomModel(@NotNull ModelPart root, @NotNull Reference2ObjectLinkedOpenHashMap<ModelPropertyType<?>, Object> properties, @NotNull List<AnimationComponent> animations){
        this(root, new ModelPropertyMapImpl(properties), animations);
    }

    public UniversalCustomModel(@NotNull ModelPart root, @NotNull ModelPropertyMap properties, @NotNull List<AnimationComponent> animations){
        super(new net.minecraft.client.model.geom.ModelPart(List.of(), Map.of()));
        this.root = root.getPart("root");
        verifyProperties(properties);
        this.propertyMap = properties;
        this.animations = animations;
    }

    protected void verifyProperties(@NotNull ModelPropertyMap properties){
        if(!properties.hasProperty(ModelPropertyRegistry.TEXTURES.get())) throw new IllegalStateException("Model has to have a texture property");

        IntOpenHashSet set = new IntOpenHashSet();
        properties.forEachModelLayer(layer ->
            layer.renderIds().forEach(id -> {
                if(!set.add(id)) throw new IllegalStateException("Repeated renderId: " + id);
            })
        );
    }

    public ModelPart _root(){
        return root;
    }

    public ModelPart getPart(@NotNull String name){
        return root.getPart(name);
    }

    public boolean hasProperty(@NotNull ModelPropertyType<?> propertyType){
        return propertyMap.hasProperty(propertyType);
    }

    public <P> P getProperty(@NotNull ModelPropertyType<P> type){
        return propertyMap.getProperty(type);
    }

    protected RenderStack getStack(){
        if(stack == null){
            stack = new RenderStack();
            stack.setRemap(hasProperty(ModelPropertyRegistry.REMAP_UV.get()));
        }

        return stack;
    }

    /**
     * Call on render thread after modifying model in editor(TODO). In fact do all modifications to the model on render thread(end of frame or start of frame) just in case. or even better: just stop rendering while applying changes
     */
    public void refreshModel(boolean properties){
        if(properties) {
            getStack().setRemap(hasProperty(ModelPropertyRegistry.REMAP_UV.get()));
            stack.clear();
        }
    }

    //@Override
    //public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer consumer, int packedLight, int packedOverlay, int color) {
    //    root.render(poseStack, consumer, packedLight, packedOverlay, color);
    //}

    @Override
    public void renderToBuffer(@NotNull E entity, @NotNull PoseStack poseStack, @Nullable Function<ResourceLocation, RenderType> suggestedRenderType, int packedLight, int packedOverlay, int color) {
        if(suggestedRenderType == null) return;

        BufferSourceAccess access = BufferSourceAccess.get();
        access.cmrs$finishBatched();
        prepareRenderStack(entity, suggestedRenderType, access);

        setModelProperties(entity);

        _root().render(poseStack, stack, packedLight, packedOverlay, color);

        access.cmrs$finishBatched();
    }

    protected void prepareRenderStack(E entity, Function<ResourceLocation, RenderType> suggestedRenderType, BufferSourceAccess access){
        getStack().reset();
        stack.setRenderTypeFunc(suggestedRenderType);
        propertyMap.forEachModelLayer(layer -> layer.setupRenderStack(this, entity, stack, access));
    }

    public void renderLayers(@NotNull E entity, @NotNull LivingEntityRenderState state, @NotNull PoseStack poseStack, int packedLight){
        BufferSourceAccess access = BufferSourceAccess.get();
        propertyMap.forEachRenderLayer(layer -> layer.render(entity, state, this, poseStack, access, packedLight));
    }

    public void setupAnim(@NotNull E entity, @NotNull LivingEntityRenderState state, @NotNull PoseStack poseStack) {
        root.getAllParts().forEach(ModelPart::resetPose);

        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(entity.level().tickRateManager().isEntityFrozen(entity));
        for(AnimationComponent anim : animations){
            anim.animate(_root(), entity, state, poseStack, partialTick);
        }
    }

    public void renderHand(@NotNull E entity, @NotNull PoseStack poseStack, int light, @NotNull HumanoidArm arm){
        FPArms fpArms = getProperty(ModelPropertyRegistry.FP_ARMS.get());
        if(fpArms == null) return;

        ModelPart part = fpArms.getTransformed(this, arm);
        if(part == null) return;

        BufferSourceAccess access = BufferSourceAccess.get();
        access.cmrs$finishBatched();

        getStack().reset();
        stack.setRenderTypeFunc(RenderType.ENTITY_CUTOUT);
        //Hardcoded Textures + Glow for first person rendering
        getProperty(ModelPropertyRegistry.TEXTURES.get()).setupRenderStack(this, entity, stack, access);
        Glow glow = getProperty(ModelPropertyRegistry.GLOW.get());
        if(glow != null) glow.setupRenderStack(this, entity, stack, access);
        setAllVisible(true, part);
        setDrawAll(true, part);
        part.render(poseStack, stack, light, OverlayTexture.NO_OVERLAY, -1);
        access.cmrs$finishBatched();
    }

    protected void setModelProperties(@NotNull E entity){
        setAllVisible(true);
        if(entity.isSpectator()){
            setDrawAll(false);

            ModelPart head = null;
            if(hasProperty(ModelPropertyRegistry.HEAD.get())) head = getPart(getProperty(ModelPropertyRegistry.HEAD.get()));
            if(head == null) head = getPart("head");
            if(head != null) setDrawAll(true, head);
        } else setDrawAll(true);
    }

    @Override
    public ResourceLocation getTexture() {
        return getProperty(ModelPropertyRegistry.TEXTURES.get()).firstTexture();
    }

    /**
     * Sets whether to draw all parts of the model
     */
    public void setDrawAll(boolean draw){
        setDrawAll(draw, root);
    }

    /**
     * Sets whether to draw all parts after provided part (including it)
     * @param part starting ModelPart
     */
    public void setDrawAll(boolean draw, ModelPart part){
        part.draw = draw;
        part.getAllChildParts().forEach(part0 -> part0.draw = draw);
    }

    /**
     * Sets visibility of all parts except for root.
     */
    public void setAllVisible(boolean visibility){
        root.getAllParts().filter(part -> part != root).forEach(child -> child.visible = visibility);
    }

    /**
     * Sets visibility of all children of provided modelPart.
     */
    public void setAllVisible(boolean visible, ModelPart part){
        part.visible = visible;
        part.getAllChildParts().forEach(child -> child.visible = visible);
    }
}