package com.tehbeard.fabric.fastaction.data.action;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public record TaskType<T extends IActionTask>(MapCodec<T> codec) {
    public static final Registry<TaskType<?>> REGISTRY = new SimpleRegistry<>(
        RegistryKey.ofRegistry(Identifier.of("quickaction", "task_types")), Lifecycle.stable());
    public static TaskType<CommandTask> COMMAND_TASK = register("command", CommandTask.CODEC);
    public static TaskType<DelayTask> DELAY_TASK = register("delay", DelayTask.CODEC);
    public static TaskType<KeybindTask> KEYBIND_TASK = register("keybind", KeybindTask.CODEC);
    public static TaskType<PanelTask> PANEL_TASK = register("panel", PanelTask.CODEC);

    public static <T extends IActionTask> TaskType<T> register(String id, MapCodec<T> taskType) {
        return Registry.register(REGISTRY, Identifier.of("quickaction", id), new TaskType<>(taskType));
    }
}
