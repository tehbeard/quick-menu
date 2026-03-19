package com.tehbeard.fabric.fastaction.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
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
    public Component description() {
        return Component.literal("KB: ").setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_GRAY))).append(Component.translatable(keybindTranslationKey));
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
