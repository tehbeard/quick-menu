package com.tehbeard.fabric.quickaction.data;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tehbeard.fabric.quickaction.data.action.CommandTask;
import com.tehbeard.fabric.quickaction.data.action.DelayTask;
import com.tehbeard.fabric.quickaction.data.action.KeybindTask;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import xyz.imcodist.quickmenu.QuickMenu;
import xyz.imcodist.quickmenu.data.ActionButtonDataJSON;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.List;

public class ActionConfigMigrator {

    /**
     * Converts quick menu entry to minedeck
     * @param action
     * @return
     */
    public static ActionButton migrateActionButton(ActionButtonDataJSON action)
    {
        var button = new ActionButton();
        button.setName(action.name);
        if (action.icon != null) {
            button.setIcon(
                new ItemStack(Registries.ITEM.get(Identifier.of(action.icon)))
            );
        }

        if (!action.keybind.isEmpty()) {
//                InputUtil.fromKeyCode()
            button.setKeybind(InputUtil.fromKeyCode(new KeyInput(action.keybind.get(0), action.keybind.get(1), 0)));
        }

        action.actions.forEach(
            task -> {
                switch (task.get(0)) {
                    case "cmd" -> button.getTasks().add(
                        new CommandTask(task.get(1))
                    );
                    case "delay" -> button.getTasks().add(
                        new DelayTask(Long.parseLong(task.get(1)))
                    );
                    case "key" -> button.getTasks().add(
                        new KeybindTask(task.get(1))
                    );
                }
            }
        );
        return button;
    }
    public static ActionConfig convertQuickMenuToActionConfig(List<ActionButtonDataJSON> oldConfig) {
        var cfg = new ActionConfig();

        var tab = new ActionTab();
        tab.setName("Default");

        oldConfig.forEach(action -> {
            var button = migrateActionButton(action);
            tab.getButtons().add(button);
        });
        cfg.getTabs().add(tab);


        return cfg;
    }

    public static void attemptMigrate() {
        File oldFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "quickmenu_data.json");
        File newFile = QuickMenu.getConfigFile();
        var gson = new GsonBuilder().setPrettyPrinting().create();
        Type listType = new TypeToken<List<ActionButtonDataJSON>>() {
        }.getType();

        // Load the json.
        if (oldFile.exists() && !newFile.exists()) {
            try (FileReader fileReader = new FileReader(oldFile)) {
                List<ActionButtonDataJSON> actionDataJSONS = gson.fromJson(fileReader, listType);

                var newCfg = convertQuickMenuToActionConfig(actionDataJSONS);
                Files.writeString(
                    newFile.toPath(),
                    gson.toJson(newCfg.encode())
                );

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}
