package com.tehbeard.fabric.fastaction.data;

import com.google.gson.*;
import com.tehbeard.fabric.fastaction.data.action.CommandTask;
import com.tehbeard.fabric.fastaction.data.action.DelayTask;
import com.tehbeard.fabric.fastaction.data.action.IActionTask;
import com.tehbeard.fabric.fastaction.data.action.KeybindTask;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import com.tehbeard.fabric.fastaction.FastAction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ActionConfigMigrator {


    private static IActionTask migrateTask(JsonArray task)
    {
        return switch (task.get(0).getAsString()) {
            case "cmd" -> new CommandTask(task.get(1).getAsString());
            case "delay" -> new DelayTask(Long.parseLong(task.get(1).getAsString()));
            case "key" -> new KeybindTask(task.get(1).getAsString());
            default -> throw new IllegalStateException("Unexpected value: " + task.get(0).getAsString());
        };
    }

    private static ActionButton migrateButton(JsonObject action)
    {
        var btn = new ActionButton();
        btn.setName(action.get("name").getAsString());
        action.getAsJsonArray("actions").asList().forEach(task -> {
            btn.getTasks().add(migrateTask(task.getAsJsonArray()));
        });
        btn.setIcon(
            Registries.ITEM.get(Identifier.of(action.get("icon").getAsString())).getDefaultStack()
        );
        var kb = action.getAsJsonArray("keybind").asList();
        if(!kb.isEmpty()) {
            btn.setKeybind(InputUtil.fromKeyCode(new KeyInput(kb.get(0).getAsInt(), kb.get(1).getAsInt(), 0)));
        }

        return btn;
    }


    /**
     * Converts quick menu entry to minedeck
     *
     */

    public static void migrate() throws IOException {
        File oldFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "quickmenu_data.json");
        File newFile = FastAction.getConfigFile();
        var gson = new GsonBuilder().setPrettyPrinting().create();

        // Load the json.
        if (oldFile.exists() && !newFile.exists()) {

                var rawCfg = JsonParser.parseString(
                    Files.readString(
                        oldFile.toPath()
                    )
                ).getAsJsonArray();
                var buttons = rawCfg.asList().stream().map(b -> migrateButton(b.getAsJsonObject())).toList();


                var cfg = new ActionConfig();

                var tab = new ActionTab();
                tab.setName("Default");

                buttons.forEach(action -> {
                    tab.getButtons().add(action);
                });
                cfg.getTabs().add(tab);

                    Files.writeString(
                        newFile.toPath(),
                        gson.toJson(cfg.encode())
                    );
        }
    }
}
