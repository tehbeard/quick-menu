package com.tehbeard.fabric.fastaction.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class CommandTask implements IActionTask {

    public static final MapCodec<CommandTask> CODEC = RecordCodecBuilder.mapCodec(inst ->
        inst.group(
            Codec.STRING.fieldOf("command").forGetter(CommandTask::getCommand)
        ).apply(inst, CommandTask::new)
    );

    public CommandTask(String command) {
        this.command = command;
    }

    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String type() {
        return "command";
    }

    @Override
    public Component description() {
        return Component.literal("Run: " + command).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_GRAY)));
    }

    @Override
    public long run() {
        Minecraft client = Minecraft.getInstance();
        if (client == null) return 0;

        LocalPlayer player = client.player;
        if (player == null) return 0;

        // Run the command.
        String commandToRun = command;

        if (commandToRun != null) {
            if (commandToRun.startsWith("/")) {
                commandToRun = commandToRun.substring(1);
                player.connection.sendCommand(commandToRun);
            } else {
                if (commandToRun.length() >= 256) {
                    commandToRun = commandToRun.substring(0, 256);
                }
                player.connection.sendChat(commandToRun);
            }
        }
        return 0;
    }

    @Override
    public TaskType<?> getType() {
        return TaskType.COMMAND_TASK;
    }
}
