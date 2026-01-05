package xyz.imcodist.quickmenu.data.command_actions;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import xyz.imcodist.quickmenu.other.KeybindHandler;

public class KeybindActionData extends BaseActionData {
    public String keybindTranslationKey = "";

    @Override
    public String getJsonType() {
        return "key";
    }
    @Override
    public String getJsonValue() {
        return keybindTranslationKey;
    }

    @Override
    public String getTypeString() { return "KEY"; }
    @Override
    public String getString() {
        return Text.translatable(keybindTranslationKey).getString();
    }

    @Override
    public long run() {
        KeybindHandler.pressKey(keybindTranslationKey);
        return 0;
    }

    public Text getText() { return Text.literal("KB: " + getString()).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.DARK_GRAY))); }
}
