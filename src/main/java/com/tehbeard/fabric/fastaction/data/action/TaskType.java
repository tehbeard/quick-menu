package com.tehbeard.fabric.fastaction.data.action;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public record TaskType<T extends IActionTask>(MapCodec<T> codec) {
    public static final Registry<TaskType<?>> REGISTRY = new MappedRegistry<>(
        ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("fastaction", "task_types")), Lifecycle.stable());
    public static TaskType<CommandTask> COMMAND_TASK = register("command", CommandTask.CODEC);
    public static TaskType<DelayTask> DELAY_TASK = register("delay", DelayTask.CODEC);
    public static TaskType<KeybindTask> KEYBIND_TASK = register("keybind", KeybindTask.CODEC);
    public static TaskType<PanelTask> PANEL_TASK = register("panel", PanelTask.CODEC);
    public static TaskType<UrlTask> URL_TASK = register("url", UrlTask.CODEC);

    public static <T extends IActionTask> TaskType<T> register(String id, MapCodec<T> taskType) {
        return Registry.register(REGISTRY, Identifier.fromNamespaceAndPath("fastaction", id), new TaskType<>(taskType));
    }
}
