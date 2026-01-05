package xyz.imcodist.quickmenu.data.command_actions;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class DelayActionData extends BaseActionData {

    public long ticks = 20;

    @Override
    public String getJsonType() {
        return "delay";
    }
    @Override
    public String getJsonValue() {
        return String.format("%s", ticks);
    }

    @Override
    public String getTypeString() { return "DLY"; }
    @Override
    public String getString() {
        return String.format("%s", ticks);
    }

    @Override
    public long run() {
        // TODO - rework this function to allow delays
        return ticks;
    }

    public Text getText() { return Text.literal("Wait: %s ticks".formatted(ticks)).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.DARK_GRAY))); }
}
