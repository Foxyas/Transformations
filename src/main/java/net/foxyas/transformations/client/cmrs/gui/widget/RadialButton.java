package net.foxyas.transformations.client.cmrs.gui.widget;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.transformations.client.cmrs.util.Consumer4;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.foxyas.transformations.client.cmrs.api.MatrixStack;
import net.foxyas.transformations.client.cmrs.geom.ModelPart;
import net.foxyas.transformations.client.cmrs.geom.Reusable;
import net.foxyas.transformations.client.cmrs.gui.GuiMeshGenerator;
import org.apache.commons.lang3.function.ToBooleanBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.ToIntFunction;

public class RadialButton extends Widget {

    protected ModelPart.Mesh outline;
    protected ModelPart.Mesh inside;
    protected float radOffset;
    protected float radSize, radStep;
    protected float radius, thickness, outlineThickness;
    protected boolean extendClickAreaOutsideRadius;
    protected boolean extendClickAreaInsideRadius;

    protected ToIntFunction<RadialButton> outlineColor;
    public static final int defOutlineColor = Color.BLACK.getRGB();
    protected ToIntFunction<RadialButton> insideColor;
    public static final int defInsideColor = ARGB.color(196, Color.GRAY.getRGB());
    protected BiConsumer<RadialButton, PoseStack> renderTransform;
    protected Consumer4<RadialButton, GuiGraphics, Float, Float> renderIcon;
    protected ToBooleanBiFunction<RadialButton, Integer> onClick;

    /**
     * No need to rebuild mesh after this call.
     */
    public RadialButton setOrigin(float x, float y, float z){
        super.setOrigin(x, y, z);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RadialButton setScale(float x, float y, float z) {
        super.setScale(x, y, z);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RadialButton setRotation(float radOffset){
        this.radOffset = radOffset % Mth.TWO_PI;
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public RadialButton setSize(float radSize, float radStep){
        this.radSize = Mth.clamp(radSize, 0, Mth.TWO_PI);
        this.radStep = radStep;
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public RadialButton setRadius(float radius){
        this.radius = radius;
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public RadialButton setThickness(float thickness, float outlineThickness){
        this.thickness = thickness;
        this.outlineThickness = outlineThickness;
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RadialButton setExtendClickAreaOutside(boolean extend){
        this.extendClickAreaOutsideRadius = extend;
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RadialButton setExtendClickAreaInside(boolean extend){
        this.extendClickAreaInsideRadius = extend;
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RadialButton setOutlineColorFunc(@Nullable ToIntFunction<RadialButton> outlineColor){
        this.outlineColor = outlineColor;
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RadialButton setInsideColorFunc(@Nullable ToIntFunction<RadialButton> insideColor){
        this.insideColor = insideColor;
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RadialButton setRenderTransform(BiConsumer<RadialButton, PoseStack> renderTransform){
        this.renderTransform = renderTransform;
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RadialButton setRenderIcon(Consumer4<RadialButton, GuiGraphics, Float, Float> renderIcon){
        this.renderIcon = renderIcon;
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     * @param onClick onClick callback parameters: radialButton, clickButton. Return true if click is consumed.
     */
    public RadialButton setOnClick(ToBooleanBiFunction<RadialButton, Integer> onClick){
        this.onClick = onClick;
        return this;
    }

    public void rebuildMesh(){
        ImmutableList.Builder<ModelPart.VertexData> builder = new ImmutableList.Builder<>();
        List<ModelPart.Quad> quads = new ArrayList<>();

        GuiMeshGenerator.genArcMesh(builder, quads, radius - outlineThickness, outlineThickness, radSize, radStep);
        GuiMeshGenerator.genArcMesh(builder, quads, radius - thickness, outlineThickness, radSize, radStep);

        if(radSize != Mth.TWO_PI) {
            GuiMeshGenerator.genRay(builder, quads, 0, radius - thickness, radius, outlineThickness, false);
            GuiMeshGenerator.genRay(builder, quads, radSize, radius - thickness, radius, outlineThickness, true);
        }

        outline = new ModelPart.Mesh(builder.build(), quads.toArray(new ModelPart.Quad[0]), 0);

        builder = new ImmutableList.Builder<>();
        quads = new ArrayList<>();

        GuiMeshGenerator.genArcMesh(builder, quads, radius - thickness + outlineThickness, thickness - outlineThickness, radSize, radStep);

        inside = new ModelPart.Mesh(builder.build(), quads.toArray(new ModelPart.Quad[0]), 0);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(!isVisible() || (outline == null && inside == null)) return;

        PoseStack stack = guiGraphics.pose();
        MatrixStack.push(stack);
        MatrixStack.translate(stack, origin);
        MatrixStack.scale(stack, scale);

        if(renderTransform != null) renderTransform.accept(this, stack);
        MatrixStack.push(stack);
        stack.mulPose(Reusable.QUATERNION.get().identity().rotateZ(radOffset));
        PoseStack.Pose matrix = stack.last();
        VertexConsumer consumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.gui());

        if(inside != null){//Draw inside first so that it doesn't overlap the outline
            inside.resetTransform();
            inside.compile(matrix, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, insideColor != null ? insideColor.applyAsInt(this) : defInsideColor);
        }

        if(outline != null){
            outline.resetTransform();
            outline.compile(matrix, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, outlineColor != null ? outlineColor.applyAsInt(this) : defOutlineColor);
        }
        MatrixStack.pop(stack);

        if(renderIcon != null) {
            Minecraft.getInstance().renderBuffers().bufferSource().endLastBatch();
            float radCenter = radOffset + radSize / 2;
            float radiusCenter = thickness / 2 + (radius - thickness);
            renderIcon.accept(this, guiGraphics, Mth.cos(radCenter) * radiusCenter, Mth.sin(radCenter) * radiusCenter);
        }
        MatrixStack.pop(stack);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return isHovering() && onClick != null && onClick.applyAsBoolean(this, button);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if(isClickThrough()) return false;
        float mouseRad = (float) Math.atan((mouseY - origin.y) / (mouseX - origin.x));
        float maxRad = radOffset + radSize;

        if(mouseX < origin.x) mouseRad += Mth.PI;
        if(mouseX > origin.x && (mouseY < origin.y || maxRad > Mth.TWO_PI)) mouseRad += Mth.TWO_PI;

        return (mouseRad >= radOffset && mouseRad <= maxRad && checkDistance(mouseX, mouseY, mouseRad))
                || (radOffset > maxRad && (mouseRad >= radOffset || mouseRad <= maxRad) && checkDistance(mouseX, mouseY, mouseRad));
    }

    protected boolean checkDistance(double mouseX, double mouseY, float mouseRad){
        if(extendClickAreaOutsideRadius && extendClickAreaInsideRadius) return true;

        float mouseDistSqr = Vector2f.distanceSquared(0, 0, (float) (mouseX - origin.x), (float) (mouseY - origin.y));
        float scaledRadiusSqr = Vector2f.distanceSquared(0, 0, Mth.cos(mouseRad) * radius * scale.x, Mth.sin(mouseRad) * radius * scale.y);
        float scaledInnerSqr = Vector2f.distanceSquared(0, 0, Mth.cos(mouseRad) * (radius - thickness) * scale.x, Mth.sin(mouseRad) * (radius - thickness) * scale.y);

        if(extendClickAreaOutsideRadius && mouseDistSqr >= scaledInnerSqr) return true;
        if(extendClickAreaInsideRadius && mouseDistSqr <= scaledRadiusSqr) return true;
        return mouseDistSqr <= scaledRadiusSqr && mouseDistSqr >= scaledInnerSqr;
    }
}
