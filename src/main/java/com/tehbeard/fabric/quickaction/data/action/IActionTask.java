package com.tehbeard.fabric.quickaction.data.action;

import com.mojang.serialization.Codec;
import net.minecraft.text.Text;

public interface IActionTask {

    // TODO - Codec registry for all available IAction types.
    Codec<IActionTask> TASK_CODEC = TaskType.REGISTRY.getCodec()
        .dispatch("type",IActionTask::getType, TaskType::codec);

    String type();
    Text description();

    long run();

    TaskType<?> getType();
}
