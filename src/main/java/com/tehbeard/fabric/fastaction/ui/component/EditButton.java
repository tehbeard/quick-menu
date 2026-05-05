package com.tehbeard.fabric.fastaction.ui.component;


import com.tehbeard.fabric.fastaction.ui.MainScreen;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.Minecraft;

public class EditButton extends TextButton {

    public EditButton(boolean isEditMode) {
        super(isEditMode? "❌" : "✎", (click, doubled) -> {
            Minecraft.getInstance().setScreenAndShow(new MainScreen(!isEditMode));
            return InputResult.PROCESSED;
        }, TextButton.staticTooltip("Edit Actions"));
    }
}
