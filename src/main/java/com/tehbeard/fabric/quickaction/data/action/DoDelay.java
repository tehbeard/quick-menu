package com.tehbeard.fabric.quickaction.data.action;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class DoDelay implements IAction {

    public long ticks = 20;

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
