package com.tehbeard.fabric.fastaction.ui.component;

import com.mojang.blaze3d.platform.InputConstants;
import com.tehbeard.fabric.fastaction.FastAction;
import com.tehbeard.fabric.fastaction.data.ActionButton;
import com.tehbeard.fabric.fastaction.ui.MinedeckScreen;
import com.tehbeard.fabric.fastaction.ui.panel.PanelsMenu;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;


public class OpenPanelsButton extends WButton {


    public OpenPanelsButton() {
        super(Component.literal("View Panels"));
    }

    @Override
    public InputResult onClick(MouseButtonEvent click, boolean doubled) {
        MinedeckScreen.pushCurrent(new PanelsMenu());
        return InputResult.PROCESSED;
    }
}
