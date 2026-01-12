package com.tehbeard.fabric.quickaction.ui;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.gui.DrawContext;

public class MainScreen extends MinedeckScreen {
    public MainScreen(boolean isEditMode) {
        super(new MainGui(isEditMode));
    }

}
