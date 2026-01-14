package com.tehbeard.fabric.quickaction.ui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.impl.VisualLogger;
import io.github.cottonmc.cotton.gui.widget.WPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.minecraft.client.gui.DrawContext;

import java.util.Stack;

public class MinedeckScreen extends CottonClientScreen {

    protected Stack<GuiDescription> overlay = new Stack<>();

    public MinedeckScreen(GuiDescription description) {
        super(description);
    }

    public MinedeckScreen push(GuiDescription description)
    {
        this.overlay.push(this.description);
        this.description = description;
        return this;
    }

    public MinedeckScreen pop()
    {
        if(!this.overlay.isEmpty())
        {
            this.description = this.overlay.pop();
            this.reposition(width, height);
        }
        return this;
    }


    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void applyBlur(DrawContext context) {
        // Do not apply blur
    }

    @Override
    protected void renderDarkening(DrawContext context) {
        // Do not darken screen
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        // TODO - Render the overlay
        overlay.forEach(
            entry -> {
                var panel =  entry.getRootPanel();
                // TODO - This uses a hack to position all windows centered, might need to in future cache the left/top values when the stack is made.
                panel.paint(context, (width/2) - (panel.getWidth() / 2), (height/2) - (panel.getHeight() / 2), -1, -1);
            }
        );
        super.render(context, mouseX, mouseY, partialTicks);

        if (description!=null) {
            WPanel root = description.getRootPanel();
            if (root!=null) {
                WWidget hitChild = root.hit(mouseX-left, mouseY-top);
                if (hitChild!=null) hitChild.renderTooltip(context, left, top, mouseX-left, mouseY-top);
            }
        }

        VisualLogger.render(context);
    }


}
