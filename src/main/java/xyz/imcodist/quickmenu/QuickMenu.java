package xyz.imcodist.quickmenu;

import com.tehbeard.fabric.quickaction.data.ActionButton;
import com.tehbeard.fabric.quickaction.data.ActionButtonExecutor;
import com.tehbeard.fabric.quickaction.data.ActionConfig;
import com.tehbeard.fabric.quickaction.data.ActionConfigMigrator;
import com.tehbeard.fabric.quickaction.ui.MainScreen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.imcodist.quickmenu.other.*;

import java.io.File;
import java.io.IOException;

public class QuickMenu implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("quickmenu");
    private static boolean menuKeyPressed = false;

    public static File getConfigFile()
    {
        return new File(FabricLoader.getInstance().getConfigDir().toFile(), "quickaction.json");
    }
    @Override
    public void onInitialize() {
        // Initialize the mods keybinds and data handler.
        ActionConfigMigrator.attemptMigrate();
        try {
            ActionConfig.load(QuickMenu.getConfigFile());
        }catch(IOException ex)
        {
            LOGGER.error(ex.toString());
        }
        ModKeybindings.initialize();

//        ClientTickEvents.START_CLIENT_TICK.register(ActionButtonDelayHandler.INSTANCE);
        // On the end of each tick check to see if a keybind has been pressed.
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {

            ActionButtonExecutor.getInstance().tick();

            // Check for menu open keybind.
            if (ModKeybindings.menuOpenKeybinding.isPressed()) {
                if (!menuKeyPressed) {
                    var mainScreen = new MainScreen(false);
                    client.setScreen(
                        mainScreen
                    );
                }
                menuKeyPressed = true;
            } else if (client.currentScreen == null) {
                menuKeyPressed = false;
            }

            // Check for action buttons keybinds.
            // I really dont like this.
            if (client.currentScreen == null) {

                var handle = client.getWindow();
                ActionConfig.getConfig()
                    .getDefaultTab().getButtons()
                        .forEach(ActionButton::handleKeybind);
            }

            KeybindHandler.runQueue();
        });
    }
}