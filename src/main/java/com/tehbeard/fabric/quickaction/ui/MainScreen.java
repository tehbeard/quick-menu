package com.tehbeard.fabric.quickaction.ui;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.gui.DrawContext;

public class MainScreen extends CottonClientScreen {
    public MainScreen(boolean isEditMode) {
        super(new MainGui(isEditMode));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void applyBlur(DrawContext context) {
//        super.applyBlur(context);
    }

    @Override
    protected void renderDarkening(DrawContext context) {
//        super.renderDarkening(context);
    }
}
