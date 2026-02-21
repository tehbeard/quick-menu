package com.tehbeard.fabric.fastaction.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tehbeard.fabric.fastaction.data.action.IActionTask;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActionButton {

    public static final Codec<ActionButton> CODEC = RecordCodecBuilder.create(inst ->
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
            btn.setTasks(new ArrayList<>(tasks));
            btn.setIcon(icon);
            keybind.ifPresent(btn::setKeybind);
            return btn;
        })
    );

    private String name = "";
    private List<IActionTask> tasks = new ArrayList<>();
    private ItemStack Icon = Items.KNOWLEDGE_BOOK.getDefaultStack();

    public InputUtil.Key keybind = null;


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

    public ItemStack getIcon() {
        return Icon;
    }

    public ActionButton setIcon(ItemStack icon) {
        Icon = icon;
        return this;
    }

    public InputUtil.Key getKeybind() {
        return keybind;
    }

    public ActionButton setKeybind(InputUtil.Key keybind) {
        this.keybind = keybind;
        return this;
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
                if (!alreadyPressed) {
                    alreadyPressed = true;
                    run(true);
                }
            } else {
                alreadyPressed = false;
            }
        } else if (this.keybind.getCategory() == InputUtil.Type.MOUSE) {
            if(GLFW.glfwGetMouseButton(client.getWindow().getHandle(), this.getKeybind().getCode()) == GLFW.GLFW_PRESS)
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
            MinecraftClient client = MinecraftClient.getInstance();

            if (client != null && client.player != null) {
                client.player.sendMessage(Text.of("Ran action \"" + name + "\""), true);
            }
        }

        new ActionButtonExecutorContext(tasks, 0).run();
    }
}
