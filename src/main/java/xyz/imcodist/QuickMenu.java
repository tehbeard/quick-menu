package xyz.imcodist;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import xyz.imcodist.data.ButtonDataHandler;
import xyz.imcodist.other.ModConfig;
import xyz.imcodist.other.ModKeybindings;
import xyz.imcodist.ui.MainGameUI;

public class QuickMenu implements ModInitializer {
    public static final ModConfig CONFIG = ModConfig.createAndLoad();

    @Override
    public void onInitialize() {
        ModKeybindings.initialize();
        ButtonDataHandler.initialize();

        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            while (ModKeybindings.menuOpenKeybinding.wasPressed()) {
                client.setScreen(new MainGameUI());
            }
        });
    }
}