package com.tehbeard.fabric.fastaction.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class ChatTask implements IActionTask {

    public static final MapCodec<ChatTask> CODEC = RecordCodecBuilder.mapCodec(inst ->
        inst.group(
            Codec.STRING.fieldOf("chat").forGetter(ChatTask::getMessage)
        ).apply(inst, ChatTask::new)
    );

    public ChatTask(String message) {
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String type() {
        return "chat";
    }

    @Override
    public Component description() {
        return Component.literal("Chat: " + message).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_GRAY)));
    }

    @Override
    public long run() {
        if (message != null) {
            Minecraft.getInstance().gui.openChatScreen(ChatComponent.ChatMethod.MESSAGE);
            var s = Minecraft.getInstance().gui.screen();
            if( s instanceof ChatScreen chatScreen) {
                chatScreen.insertText(message, true);
            }
            // Broken and does not work, forces ChatMethod.MESSAGE
            // Minecraft.getInstance().gui.openChatAndAddText(ChatComponent.ChatMethod.MESSAGE, message);
        }
        return 0;
    }

    @Override
    public TaskType<?> getType() {
        return TaskType.CHAT_TASK;
    }
}
