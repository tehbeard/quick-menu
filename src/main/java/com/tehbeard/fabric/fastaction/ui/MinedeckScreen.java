package com.tehbeard.fabric.fastaction.ui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.impl.VisualLogger;
import io.github.cottonmc.cotton.gui.widget.WPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;

public class MinedeckScreen extends CottonClientScreen {


    protected static Stack<MinedeckScreen> priorScreens = new Stack<>();

    protected Runnable onRemove;

    public MinedeckScreen(GuiDescription description) {
        super(description);
    }

    public MinedeckScreen onRemoved(@NotNull Runnable onRemove) {
        this.onRemove = onRemove;
        return this;
    }

    @Override
    public void removed() {
        super.removed();
    }

    public static void pushCurrent(GuiDescription description) {
        if (Minecraft.getInstance().gui.screen() instanceof MinedeckScreen s) {
            priorScreens.push(s);
            Minecraft.getInstance().setScreenAndShow(new MinedeckScreen(description));
//            s.push(description);
        }
    }

    public static void popCurrent() {
        if (Minecraft.getInstance().gui.screen() instanceof MinedeckScreen s) {
            if (s.onRemove != null) {
                s.onRemove.run();
            }
            if(!priorScreens.isEmpty()) {
                Minecraft.getInstance().setScreenAndShow(priorScreens.pop());
            }
        }
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }


    @Override
    protected void extractBlurredBackground(GuiGraphicsExtractor graphics) {
        // Do not apply blur
    }


    @Override
    protected void extractMenuBackground(GuiGraphicsExtractor graphics) {
        // Do not darken screen
    }


    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float partialTicks) {


        priorScreens.forEach(
            screen -> {
                var entry = screen.getDescription();
                var panel = entry.getRootPanel();
                // HACK - This uses a hack to position all windows centered, might need to in future cache the left/top values when the stack is made.
                panel.paint(context, (width / 2) - (panel.getWidth() / 2), (height / 2) - (panel.getHeight() / 2), -1, -1);
            }
        );
        super.extractRenderState(context, mouseX, mouseY, partialTicks);

        if (description != null) {
            WPanel root = description.getRootPanel();
            if (root != null) {
                WWidget hitChild = root.hit(mouseX - left, mouseY - top);
                if (hitChild != null) hitChild.renderTooltip(context, left, top, mouseX - left, mouseY - top);
            }
        }

        VisualLogger.render(context);
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if (input.isEscape() && !priorScreens.isEmpty()) {
            popCurrent();
            return true;
        }
        if(input.isEscape())
        {
            if (this.onRemove != null) {
                this.onRemove.run();
            }
        }
        return super.keyPressed(input);
    }
}
