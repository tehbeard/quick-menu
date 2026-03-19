package com.tehbeard.fabric.fastaction.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class DelayTask implements IActionTask {

    public static final MapCodec<DelayTask> CODEC = RecordCodecBuilder.mapCodec(inst ->
        inst.group(
            Codec.LONG.fieldOf("delay").forGetter(DelayTask::getTicks)
        ).apply(inst, DelayTask::new)
    );

    public long ticks = 20;

    public DelayTask(long ticks) {
        this.ticks = ticks;
    }

    public long getTicks() {
        return ticks;
    }

    public void setTicks(long ticks) {
        this.ticks = ticks;
    }

    @Override
    public String type() {
        return "delay";
    }

    @Override
    public Component description() {
        return Component.literal("Wait: %s ticks".formatted(ticks)).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_GRAY)));
    }

    @Override
    public long run() {
        return ticks;
    }

    @Override
    public TaskType<?> getType() {
        return TaskType.DELAY_TASK;
    }
}
