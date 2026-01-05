package com.tehbeard.fabric.quickaction.data;

import com.tehbeard.fabric.quickaction.data.action.IAction;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ActionButton {
    public String name;
    public List<IAction> actions;
    public ItemStack Icon;

    //    public List<Integer> keybind; // TODO - better way to store this??
    public InputUtil.Key keybind;


    private boolean alreadyPressed = false; // transient latch to prevent repeated activation.


    public void handleKeybind() {
        if (keybind == null) {
            return;
        } // Exit if no keybind set.
//
        var client = MinecraftClient.getInstance();
        var handle = client.getWindow();
        if (this.keybind.getCategory() == InputUtil.Type.KEYSYM) {
            if (InputUtil.isKeyPressed(handle, keybind.getCode())) {
                if(!alreadyPressed){
                    alreadyPressed = true;
                    run();
                }
            } else {
                alreadyPressed = false;
            }
        } else if( this.keybind.getCategory() == InputUtil.Type.MOUSE)
        {
            var pressed = false;
            switch (this.keybind.getCode()) {
                case 0 -> pressed = client.mouse.wasLeftButtonClicked();
                case 1 -> pressed = client.mouse.wasRightButtonClicked();
                case 2 -> pressed = client.mouse.wasMiddleButtonClicked();
            }
            if(pressed)
            {
                alreadyPressed = true;
                run();
            } else {
                alreadyPressed = false;
            }
        }
    }
    // TODO - Move keybind check logic in here.

    public void run()
    {

    }
}
