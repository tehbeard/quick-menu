package com.tehbeard.fabric.fastaction.ui.component;

import com.tehbeard.fabric.fastaction.data.ActionButton;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import com.mojang.blaze3d.platform.InputConstants;
import com.tehbeard.fabric.fastaction.FastAction;


public class KeybindSelectButton extends WButton {

    private ActionButton data;

    private boolean isDetecting = false;

    public KeybindSelectButton(@NotNull ActionButton data) {
        this.data = data;
        updateState();
    }

    @Override
    public InputResult onClick(MouseButtonEvent click, boolean doubled) {
        if(!isDetecting) {
            isDetecting = true;
            updateState();
            getHost().requestFocus(this);
        } else {
            data.setKeybind(
                InputConstants.Type.MOUSE.getOrCreate(click.button())
            );
            isDetecting = false;
            updateState();
        }
        return InputResult.PROCESSED;
    }

    @Override
    public InputResult onKeyPressed(KeyEvent input) {
        var logger = FastAction.LOGGER;
        logger.info("Key pressed!");
        if(isDetecting)
        {
            int key = input.key();
            int keyCode = input.keycode();
            int modifiers = input.modifiers();
            //
            if (key != InputConstants.KEY_ESCAPE) {

                data.setKeybind(
                    InputConstants.getKey(new KeyEvent(
                        key,
                        keyCode,
                        modifiers)
                    )
                );
            } else {
                data.setKeybind(null);
            }

            isDetecting = false;
            updateState();
            return InputResult.PROCESSED;
        }
        return super.onKeyPressed(input);
    }

    private void updateState()
    {
        Component label = Component.literal("Not Bound");
        if(data.getKeybind() != null)
        {
            label = data.getKeybind().getDisplayName();
        }
        if(isDetecting)
        {

            setLabel(
                Component.literal("> ")
                    .append(label)
                    .append(Component.literal(" <"))
            );
        } else {
            setLabel(label);
        }
    }
}
