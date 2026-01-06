package com.tehbeard.fabric.quickaction.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tehbeard.fabric.quickaction.data.action.IActionTask;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class ActionButton {

    public static final Codec<ActionButton> CODEC = RecordCodecBuilder.create( inst ->
        inst.group(
            Codec.STRING.fieldOf("name").forGetter(ActionButton::getName),
            IActionTask.TASK_CODEC.listOf().fieldOf("tasks").forGetter(ActionButton::getTasks),
            ItemStack.CODEC.fieldOf("icon").forGetter(ActionButton::getIcon),
            Codec.STRING.xmap(
                str -> InputUtil.fromTranslationKey(str),
                key -> key.getTranslationKey()
            ).optionalFieldOf("keybind").forGetter(btn -> Optional.ofNullable(btn.getKeybind()))
        ).apply(inst, (name, tasks, icon, keybind) -> {
            var btn = new ActionButton();
            btn.setName(name);
            btn.setTasks(tasks);
            btn.setIcon(icon);
            keybind.ifPresent(btn::setKeybind);
            return btn;
        })
    );

    private String name;
    private List<IActionTask> tasks;
    private ItemStack Icon;

    //    public List<Integer> keybind; // TODO - better way to store this??
    public InputUtil.Key keybind;


    private boolean alreadyPressed = false; // transient latch to prevent repeated activation.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IActionTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<IActionTask> tasks) {
        this.tasks = tasks;
    }

    public ItemStack getIcon() {
        return Icon;
    }

    public void setIcon(ItemStack icon) {
        Icon = icon;
    }

    public InputUtil.Key getKeybind() {
        return keybind;
    }

    public void setKeybind(InputUtil.Key keybind) {
        this.keybind = keybind;
    }

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
