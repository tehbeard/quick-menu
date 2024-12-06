package xyz.imcodist.quickmenu.data.command_actions;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

public class CommandActionData extends BaseActionData {
    public String command = "";

    @Override
    public String getJsonType() {
        return "cmd";
    }
    @Override
    public String getJsonValue() {
        return command;
    }

    @Override
    public String getTypeString() { return "CMD"; }
    @Override
    public String getString() {
        return command;
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
}
