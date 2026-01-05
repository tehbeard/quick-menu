package xyz.imcodist.quickmenu.data.command_actions;

import net.minecraft.text.Text;

public class BaseActionData {
    public String getJsonType() {
        return "base";
    }
    public String getJsonValue() {
        return "";
    }

    public String getTypeString() { return "ACT"; }
    public String getString() {
        return "uh oh why are you seeing this";
    }

    public long run() {
        return 0;
    }

    public Text getText() { return Text.literal("N/A"); }
}
