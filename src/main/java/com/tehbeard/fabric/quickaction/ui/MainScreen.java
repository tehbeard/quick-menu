package com.tehbeard.fabric.quickaction.ui;

import com.tehbeard.fabric.quickaction.data.ActionConfig;
import xyz.imcodist.quickmenu.QuickMenu;

import java.io.IOException;

public class MainScreen extends MinedeckScreen {
    public MainScreen(boolean isEditMode) {
        super(new MainGui(isEditMode));
    }



}
