package com.tehbeard.fabric.fastaction.data.action;

import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;

public interface IActionTask {

    Codec<IActionTask> TASK_CODEC = TaskType.REGISTRY.byNameCodec()
        .dispatch("type",IActionTask::getType, TaskType::codec);

    String type();
    Component description();

    long run();

    TaskType<?> getType();
}
