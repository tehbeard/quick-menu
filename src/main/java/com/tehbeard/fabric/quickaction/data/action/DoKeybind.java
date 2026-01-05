package com.tehbeard.fabric.quickaction.data.action;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import xyz.imcodist.quickmenu.other.KeybindHandler;

public class DoKeybind implements IAction {

    public String keybindTranslationKey = "";

    @Override
    public String type() {
        return "keybind";
    }

    @Override
    public Text description() {
        return Text.literal("KB: ").setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.DARK_GRAY))).append(Text.translatable(keybindTranslationKey));
    }

    @Override
    public long run() {
        KeybindHandler.pressKey(keybindTranslationKey);
        KeyBindingHelper.getBoundKeyOf(null).getTranslationKey();
        return 0;
    }
}
