package com.tehbeard.fabric.fastaction;

import com.mojang.blaze3d.platform.InputConstants;
import com.tehbeard.fabric.fastaction.data.ActionButton;
import com.tehbeard.fabric.fastaction.data.ActionButtonExecutor;
import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.data.ActionConfigMigrator;
import com.tehbeard.fabric.fastaction.ui.MinedeckScreen;
import com.tehbeard.fabric.fastaction.ui.panel.MainPanel;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.imcodist.quickmenu.other.*;

import java.io.File;
import java.io.IOException;

public class FastAction implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("fastactions");
    private static boolean menuKeyPressed = false;

    private static KeyMapping menuOpenKeybinding;

    public static File getConfigFile()
    {
        return new File(FabricLoader.getInstance().getConfigDir().toFile(), "fastactions.json");
    }
    @Override
    public void onInitialize() {
        // Initialize the mods keybinds and data handler.
        try {
            ActionConfigMigrator.migrate();
        } catch(Exception ex)
        {
            LOGGER.error("Migration failed", ex);
            System.exit(-100);
        }
        try {
            ActionConfig.load(FastAction.getConfigFile());
        }catch(IOException ex)
        {
            LOGGER.error(ex.toString());
        }

        menuOpenKeybinding = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.fastaction.open",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            KeyMapping.Category.register(Identifier.parse("fastaction:all"))
        ));

//        ClientTickEvents.START_CLIENT_TICK.register(ActionButtonDelayHandler.INSTANCE);
        // On the end of each tick check to see if a keybind has been pressed.
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {

            ActionButtonExecutor.getInstance().tick();

            // Check for menu open keybind.
            if (menuOpenKeybinding.isDown()) {
                if (!menuKeyPressed) {
//                    var mainScreen = new MainScreen(false);
                    var mainScreen = new MinedeckScreen(new MainPanel());
                    client.setScreenAndShow(
                        mainScreen
                    );
                }
                menuKeyPressed = true;
            } else if (client.gui.screen() == null) {
                menuKeyPressed = false;
            }

            // Check for action buttons keybinds.
            // I really dont like this.
            if (client.gui.screen() == null) {
                ActionConfig.getConfig()
                    .getDefaultTab().getButtons()
                        .forEach(ActionButton::handleKeybind);
            }

            KeybindHandler.runQueue();
        });
    }
}