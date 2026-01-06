package com.tehbeard.fabric.quickaction.data.action;

import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TaskTypes {

    public static TaskType<CommandTask> COMMAND_TASK = register("command", CommandTask.CODEC);
    public static TaskType<DelayTask> DELAY_TASK = register("delay", DelayTask.CODEC);
    public static TaskType<KeybindTask> KEYBIND_TASK = register("keybind", KeybindTask.CODEC);
    public static TaskType<PanelTask> PANEL_TASK = register("panel", PanelTask.CODEC);


    public static <T extends IActionTask> TaskType<T> register(String id, MapCodec<T> taskType) {
        return Registry.register(TaskType.REGISTRY, Identifier.of("quickaction", id), new TaskType<>(taskType));
    }
}
