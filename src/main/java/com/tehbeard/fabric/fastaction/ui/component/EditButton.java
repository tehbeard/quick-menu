package com.tehbeard.fabric.fastaction.ui.component;


import com.tehbeard.fabric.fastaction.ui.MainScreen;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.MinecraftClient;

public class EditButton extends TextButton {

    public EditButton(boolean isEditMode) {
        super(isEditMode? "❌" : "✎", (click, doubled) -> {
            MinecraftClient.getInstance().setScreen(new MainScreen(!isEditMode));
            return InputResult.PROCESSED;
        });
    }
}
