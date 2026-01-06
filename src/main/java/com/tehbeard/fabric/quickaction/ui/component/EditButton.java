package com.tehbeard.fabric.quickaction.ui.component;


import io.github.cottonmc.cotton.gui.widget.data.InputResult;

public class EditButton extends TextButton {

    public EditButton() {
        super("✎", (click, doubled) -> {

            return InputResult.PROCESSED;
        });
    }
}
