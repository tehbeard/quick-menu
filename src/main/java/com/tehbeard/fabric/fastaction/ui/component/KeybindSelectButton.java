package com.tehbeard.fabric.fastaction.ui.component;

import com.tehbeard.fabric.fastaction.data.ActionButton;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.gui.Click;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import com.tehbeard.fabric.fastaction.FastAction;


public class KeybindSelectButton extends WButton {

    private ActionButton data;

    private boolean isDetecting = false;

    public KeybindSelectButton(@NotNull ActionButton data) {
        this.data = data;
        updateState();
    }

    @Override
    public InputResult onClick(Click click, boolean doubled) {
        if(!isDetecting) {
            isDetecting = true;
            updateState();
            getHost().requestFocus(this);
        } else {
            data.setKeybind(
                InputUtil.Type.MOUSE.createFromCode(click.button())
            );
            isDetecting = false;
            updateState();
        }
        return InputResult.PROCESSED;
    }

    @Override
    public InputResult onKeyPressed(KeyInput input) {
        var logger = FastAction.LOGGER;
        logger.info("Key pressed!");
        if(isDetecting)
        {
            int keyCode = input.key();
            int scanCode = input.scancode();
            int modifiers = input.modifiers();

            if (keyCode != GLFW.GLFW_KEY_ESCAPE) {

                data.setKeybind(
                    InputUtil.fromKeyCode(new KeyInput(
                        keyCode,
                        scanCode, modifiers)
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
        Text label = Text.literal("Not Bound");
        if(data.getKeybind() != null)
        {
            label = data.getKeybind().getLocalizedText();
        }
        if(isDetecting)
        {

            setLabel(
                Text.literal("> ")
                    .append(label)
                    .append(Text.literal(" <"))
            );
        } else {
            setLabel(label);
        }
    }
}
