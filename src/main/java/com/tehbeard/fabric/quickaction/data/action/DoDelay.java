package com.tehbeard.fabric.quickaction.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class DoDelay implements IAction {

    public static final Codec<DoDelay> CODEC = RecordCodecBuilder.create(inst ->
        inst.group(
            Codec.LONG.fieldOf("delay").forGetter(DoDelay::getTicks)
        ).apply(inst, DoDelay::new)
    );

    public long ticks = 20;

    public DoDelay() {
    }

    public DoDelay(long ticks) {
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
    public Text description() {
        return Text.literal("Wait: %s ticks".formatted(ticks)).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.DARK_GRAY)));
    }

    @Override
    public long run() {
        return ticks;
    }
}
