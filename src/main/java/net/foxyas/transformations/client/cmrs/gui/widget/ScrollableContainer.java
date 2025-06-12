package net.foxyas.transformations.client.cmrs.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.transformations.client.cmrs.api.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class ScrollableContainer extends WidgetContainer {

    protected float scroll;
    protected final RoundedRectWidget scrollBar = new RoundedRectWidget(){
        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
            scroll += (float) dragY / (ScrollableContainer.this.getHeight() * .8f);
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    };
    private float actualHeight;

    public void setActualHeight(float height){
        actualHeight = height;
    }

    public boolean isScrollEnabled(){
        return actualHeight > getHeight();
    }

    @Override
    public void init() {
        super.init();

        scrollBar.setSize(16, 50)
                .setRoundingRadius(8)
                .setOrigin(getWidth() * .45f, getHeight() * -.4f, 10);
        scrollBar.rebuildMesh();
    }

    float scroll1;

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(!isVisible() || children.isEmpty()) return;

        scroll1 += scroll;
        scroll = 0;
        if(scroll1 < 0) scroll1 = 0;
        if(scroll1 > 1) scroll1 = 1;

        //Finish buffers before scissoring
        Minecraft.getInstance().renderBuffers().bufferSource().endLastBatch();

        PoseStack stack = guiGraphics.pose();
        MatrixStack.push(stack);
        MatrixStack.translate(stack, origin);
        MatrixStack.scale(stack, scale);

        //apply scissors
        guiGraphics.enableScissor((int) -getWidth() / 2, (int) -getHeight() / 2, (int) getWidth() / 2, (int) getHeight() / 2);

        MatrixStack.push(stack);
        if(isScrollEnabled()) stack.translate(0, -scroll1 * (actualHeight - getHeight()), 0);

        mouseX = (int) scaleX(mouseX);
        mouseY = (int) scaleY(mouseY);

        for(Widget widget : children.reversed()){
            widget.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        MatrixStack.pop(stack);
        if(isScrollEnabled()) {
            stack.translate(0, scroll1 * getHeight() * .8f, 0);
            scrollBar.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        //Finish buffers before finishing scissoring
        Minecraft.getInstance().renderBuffers().bufferSource().endLastBatch();
        guiGraphics.disableScissor();
        MatrixStack.pop(stack);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        hovering = isMouseOver(mouseX, mouseY);
        if(!isHovering()){
            for(Widget widget : children) widget.mouseMoved(Double.NaN, Double.NaN);
            return;
        }

        mouseX = scaleX(mouseX);
        mouseY = scaleY(mouseY + scroll1 * (actualHeight - getHeight()));

        boolean consumed = false;
        for(Widget widget : children){
            if(consumed) {//Tell widget to unHover
                if(widget.isHovering()) widget.mouseMoved(Double.NaN, Double.NaN);
            } else {
                widget.mouseMoved(mouseX, mouseY);
                consumed = widget.isHovering();
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(isScrollEnabled() && scrollBar.isMouseOver(scaleX(mouseX), scaleY(mouseY - scroll1 * getHeight() * .8f))){
            setFocused(scrollBar);
            if (button == 0) {
                this.setDragging(true);
            }
            return true;
        }

        return super.mouseClicked(mouseX, mouseY + scroll1 * (actualHeight - getHeight()), button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY + scroll1 * (actualHeight - getHeight()), button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if(isScrollEnabled() && scrollBar.isMouseOver(scaleX(mouseX), scaleY(mouseY - scroll1 * getHeight() * .8f))){
            scrollBar.mouseDragged(scaleX(mouseX), scaleY(mouseY - scroll1 * getHeight() * .8f), button, dragX, dragY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY + scroll1 * (actualHeight - getHeight()), button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if(isScrollEnabled()) {
            scroll -= (float) scrollY / 10;
            if (getFocused() == scrollBar) {
                setFocused(null);
                setDragging(false);
            }
        }

        return super.mouseScrolled(mouseX, mouseY + scroll1 * (actualHeight - getHeight()), scrollX, scrollY);
    }
}
