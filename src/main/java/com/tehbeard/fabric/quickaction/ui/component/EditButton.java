package com.tehbeard.fabric.quickaction.ui.component;


import com.tehbeard.fabric.quickaction.ui.MainScreen;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.MinecraftClient;

public class EditButton extends TextButton {

    public EditButton(boolean isEditMode) {
        super("✎", (click, doubled) -> {
            // TODO - Render the edit screen.
            MinecraftClient.getInstance().setScreen(new MainScreen(!isEditMode));
            return InputResult.PROCESSED;
        });
    }
}
