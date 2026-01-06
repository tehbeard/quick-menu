package com.tehbeard.fabric.quickaction.data.action;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public record TaskType<T extends IActionTask>(MapCodec<T> codec) {
    public static final Registry<TaskType<?>> REGISTRY = new SimpleRegistry<>(
        RegistryKey.ofRegistry(Identifier.of("quickaction", "task_types")), Lifecycle.stable());
}
