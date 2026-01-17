package com.tehbeard.fabric.quickaction.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import xyz.imcodist.quickmenu.other.KeybindHandler;

public class KeybindTask implements IActionTask {

    public static final MapCodec<KeybindTask> CODEC = RecordCodecBuilder.mapCodec(inst ->
        inst.group(
            Codec.STRING.fieldOf("keybind").forGetter(KeybindTask::getKeybind)
        ).apply(inst, KeybindTask::new)
    );
    public String keybindTranslationKey = "";

    public KeybindTask() {
    }

    public KeybindTask(String keybindTranslationKey) {
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
        return 0;
    }

    @Override
    public TaskType<?> getType() {
        return TaskType.KEYBIND_TASK;
    }
}
