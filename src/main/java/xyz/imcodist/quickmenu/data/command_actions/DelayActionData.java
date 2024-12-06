package xyz.imcodist.quickmenu.data.command_actions;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

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
}
