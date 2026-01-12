package com.tehbeard.fabric.quickaction.ui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.gui.DrawContext;

public class MinedeckScreen extends CottonClientScreen {

    public MinedeckScreen(GuiDescription description) {
        super(description);
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


}
