package xyz.imcodist.quickmenu.other;

import xyz.imcodist.quickmenu.mixins.KeyBindingMixin;

import java.util.ArrayList;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class KeybindHandler {
    public static ArrayList<KeyMapping> queuedKeys = new ArrayList<>();
    private static final ArrayList<KeyMapping> queuedRelease = new ArrayList<>();

    private static boolean didPress = false;

    public static void runQueue() {
        if (didPress) {
            for (KeyMapping keyBinding : queuedRelease) {
                keyBinding.setDown(false);
            }

            didPress = false;
            queuedRelease.clear();
        }

        for (KeyMapping keyBinding : queuedKeys) {
            KeyBindingMixin keyBindingMixin = (KeyBindingMixin) keyBinding;

            keyBindingMixin.setTimesPressed(1);
            keyBinding.setDown(true);

            didPress = true;
            queuedRelease.add(keyBinding);
        }

        queuedKeys.clear();
    }

    public static void pressKey(String translationKey) {
        KeyMapping keyBinding = getFromTranslationKey(translationKey);
        if (keyBinding == null) return;

        queuedKeys.add(keyBinding);
    }

    public static KeyMapping getFromTranslationKey(String translationKey) {
        Minecraft client = Minecraft.getInstance();
        if (client != null) {
            for (KeyMapping keyBinding : client.options.keyMappings) {
                if (keyBinding.getName().equals(translationKey)) return keyBinding;
            }
        }

        return null;
    }

    public static KeyMapping[] getKeybindings() {
        Minecraft client = Minecraft.getInstance();
        if (client == null) return null;

        return client.options.keyMappings;
    }
}
