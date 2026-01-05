package com.tehbeard.fabric.quickaction.data.action;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import xyz.imcodist.quickmenu.other.KeybindHandler;

public class DoPanel implements IAction {

    public Identifier target;

    @Override
    public String type() {
        return "panel";
    }

    @Override
    public Text description() {
        return Text.literal("Panel: %s".formatted("[TODO IMPLEMENT]")).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.DARK_GRAY)));
    }

    @Override
    public long run() {
//        TODO - Open screen on selected panel.
//        KeybindHandler.pressKey(keybindTranslationKey);
        return 0;
    }
}
