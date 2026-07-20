package com.tehbeard.fabric.fastaction.data;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tehbeard.fabric.fastaction.data.action.IActionTask;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.lwjgl.sdl.SDLMouse;

public class ActionButton {

    public static final Codec<ActionButton> CODEC = RecordCodecBuilder.create(inst ->
        inst.group(
            Codec.STRING.fieldOf("name").forGetter(ActionButton::getName),
            IActionTask.TASK_CODEC.listOf().fieldOf("tasks").forGetter(ActionButton::getTasks),
            ItemStackTemplate.CODEC.fieldOf("icon").forGetter(ActionButton::getIcon),
            Codec.STRING.xmap(
                str -> InputConstants.getKey(str),
                key -> key.getName()
            ).optionalFieldOf("keybind").forGetter(btn -> Optional.ofNullable(btn.getKeybind()))
        ).apply(inst, (name, tasks, icon, keybind) -> {
            var btn = new ActionButton();
            btn.setName(name);
            btn.setTasks(new ArrayList<>(tasks));
            btn.setIcon(icon);
            keybind.ifPresent(btn::setKeybind);
            return btn;
        })
    );

    private String name = "";
    private List<IActionTask> tasks = new ArrayList<>();
    private ItemStackTemplate icon = new ItemStackTemplate(
        Items.KNOWLEDGE_BOOK,
        1
    );

    public InputConstants.Key keybind = null;


    private boolean alreadyPressed = false; // transient latch to prevent repeated activation.

    public String getName() {
        return name;
    }

    public ActionButton setName(String name) {
        this.name = name;
        return this;
    }

    public List<IActionTask> getTasks() {
        return tasks;
    }

    public ActionButton setTasks(List<IActionTask> tasks) {
        this.tasks = tasks;
        return this;
    }

    public ItemStackTemplate getIcon() {
        return icon;
    }

    public ActionButton setIcon(ItemStackTemplate icon) {
        this.icon = icon;
        return this;
    }

    public InputConstants.Key getKeybind() {
        return keybind;
    }

    public ActionButton setKeybind(InputConstants.Key keybind) {
        this.keybind = keybind;
        return this;
    }

    public void handleKeybind() {
        if (keybind == null) {
            return;
        } // Exit if no keybind set.
//
        var client = Minecraft.getInstance();
        var handle = client.getWindow();
        if (this.keybind.getType() == InputConstants.Type.KEYBOARD) {
            if (InputConstants.isKeyDown(keybind.getValue())) {
                if (!alreadyPressed) {
                    alreadyPressed = true;
                    run(true);
                }
            } else {
                alreadyPressed = false;
            }
        } else if (this.keybind.getType() == InputConstants.Type.MOUSE) {
            var state = SDLMouse.nSDL_GetMouseState(0, 0);
            if( (state & this.getKeybind().getValue()) != 0 )
            {
                if (!alreadyPressed) {
                    alreadyPressed = true;
                    run(true);
                }
            } else {
                alreadyPressed = false;
            }
        }
    }

    public void run(boolean isKeybind) {
        if ( isKeybind ) {
            Minecraft client = Minecraft.getInstance();

            if (client != null && client.player != null) {
                client.player.sendOverlayMessage(Component.nullToEmpty("Ran action \"" + name + "\""));
            }
        }

        new ActionButtonExecutorContext(tasks, 0).run();
    }
}
