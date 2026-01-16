package com.tehbeard.fabric.quickaction.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class CommandTask implements IActionTask {

    public static final MapCodec<CommandTask> CODEC = RecordCodecBuilder.mapCodec(inst ->
        inst.group(
            Codec.STRING.fieldOf("command").forGetter(CommandTask::getCommand)
        ).apply(inst, CommandTask::new)
    );

    public CommandTask() {
    }

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
    public Text description() {
        return Text.literal("Run: " + command).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.DARK_GRAY)));
    }

    @Override
    public long run() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return 0;

        ClientPlayerEntity player = client.player;
        if (player == null) return 0;

        // Run the command.
        String commandToRun = command;

        if (commandToRun != null) {
            if (commandToRun.startsWith("/")) {
                commandToRun = commandToRun.substring(1);
                player.networkHandler.sendChatCommand(commandToRun);
            } else {
                if (commandToRun.length() >= 256) {
                    commandToRun = commandToRun.substring(0, 256);
                }
                player.networkHandler.sendChatMessage(commandToRun);
            }
        }
        return 0;
    }

    @Override
    public TaskType<?> getType() {
        return TaskType.COMMAND_TASK;
    }
}
