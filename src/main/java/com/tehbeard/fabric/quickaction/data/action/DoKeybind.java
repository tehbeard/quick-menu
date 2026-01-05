package com.tehbeard.fabric.quickaction.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import xyz.imcodist.quickmenu.other.KeybindHandler;

public class DoKeybind implements IAction {

    public static final Codec<DoKeybind> CODEC = RecordCodecBuilder.create(inst ->
        inst.group(
            Codec.STRING.fieldOf("keybind").forGetter(DoKeybind::getKeybind)
        ).apply(inst, DoKeybind::new)
    );
    public String keybindTranslationKey = "";

    public DoKeybind() {
    }

    public DoKeybind(String keybindTranslationKey) {
        this.keybindTranslationKey = keybindTranslationKey;
    }


    public String getKeybind() {
        return keybindTranslationKey;
    }

    public void setKeybind(String keybindTranslationKey) {
        this.keybindTranslationKey = keybindTranslationKey;
    }

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
//        KeyBindingHelper.getBoundKeyOf(null).getTranslationKey();
        return 0;
    }
}
