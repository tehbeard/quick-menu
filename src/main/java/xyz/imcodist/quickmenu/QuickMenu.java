package xyz.imcodist.quickmenu;

import io.github.cottonmc.cotton.gui.impl.client.LibGuiClient;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.util.InputUtil;
import xyz.imcodist.quickmenu.other.*;
import com.tehbeard.fabric.quickaction.ui.MainScreen;

import java.io.File;

public class QuickMenu implements ModInitializer {

    private static boolean menuKeyPressed = false;

    @Override
    public void onInitialize() {
        // Initialize the mods keybinds and data handler.
        ModKeybindings.initialize();
        ActionButtonDataHandler.initialize();
        var cfg = LibGuiClient.loadConfig();
        cfg.darkMode = true;
        LibGuiClient.saveConfig(cfg);

        // START new config
        var file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "quickaction.json");


        // END new config

//        ClientTickEvents.START_CLIENT_TICK.register(ActionButtonDelayHandler.INSTANCE);
        // On the end of each tick check to see if a keybind has been pressed.
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            ActionButtonDelayHandler.INSTANCE.doDelayChecks();
            // Check for menu open keybind.
            if (ModKeybindings.menuOpenKeybinding.isPressed()) {
                if (!menuKeyPressed) {
                    client.setScreen(new MainScreen(false));
                }
                menuKeyPressed = true;
            } else if (client.currentScreen == null) {
                menuKeyPressed = false;
            }

            // Check for action buttons keybinds.
            // I really dont like this.
            if (client.currentScreen == null) {
                ActionButtonDataHandler.actions.forEach((actionButtonData) -> {
                    boolean run = false;
                    if (actionButtonData.keybind.size() < 4) return;

                    if (actionButtonData.keybind.get(3) == 0) {
                        // Key press.
                        InputUtil.Key key = actionButtonData.getKey();
                        if (key == null) return;

                        var handle = client.getWindow();
                        if (InputUtil.isKeyPressed(handle, key.getCode())) {
                            if (!actionButtonData.keyPressed) run = true;
                            actionButtonData.keyPressed = true;
                        } else {
                            actionButtonData.keyPressed = false;
                        }
                    } else {
                        // Mouse press.
                        // TODO: Allow buttons greater then 2 to be bound.
                        int mouseButton = actionButtonData.keybind.get(0);
                        boolean pressed = false;

                        switch (mouseButton) {
                            case 0 -> pressed = client.mouse.wasLeftButtonClicked();
                            case 1 -> pressed = client.mouse.wasRightButtonClicked();
                            case 2 -> pressed = client.mouse.wasMiddleButtonClicked();
                        }

                        if (pressed) run = true;
                    }

                    if (run) {
                        actionButtonData.run(true);
                    }
                });
            }

            KeybindHandler.runQueue();
        });
    }
}