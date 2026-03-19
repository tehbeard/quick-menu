package com.tehbeard.fabric.fastaction.ui.panel;

import com.tehbeard.fabric.fastaction.ui.component.PanelWithHeader;
import com.tehbeard.fabric.fastaction.ui.component.WPixelPanel;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import xyz.imcodist.quickmenu.other.KeybindHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * TODO: Redo as a list, try to figure an approach for category vs. keybind split
 * TODO: Implement search filtering
 * TODO: Sort label alignment.
 */
public class KeybindPicker extends LightweightGuiDescription {

    private static final int ELEMENT_SIZE = 20;

    private Consumer<String> onSelect;
    // TODO - Passthru a handler for when a keybind is selected.
    public KeybindPicker(Consumer<String> onSelect) {
        setUseDefaultRootBackground(false);
        PanelWithHeader root = new PanelWithHeader("Select a keybind", 274, 142, true);
        setRootPanel(root);

        WPixelPanel scrollPanelContents = new WPixelPanel();

        KeyMapping[] keyBindings = KeybindHandler.getKeybindings();

        WScrollPanel scrollWrapper = new WScrollPanel(scrollPanelContents);
        scrollWrapper.getVerticalScrollBar().addPainters();
        scrollWrapper.setSize( root.getWidth() - (17 + 7), root.getHeight() - (27 + 5));
        root.add(scrollWrapper, 17, 27);

        if(keyBindings != null)
        {
            Map<String, ArrayList<KeyMapping>> sortedKeybindings = new HashMap<>();
            for (KeyMapping keyBinding : keyBindings) {
                String category = keyBinding.getCategory().id().toLanguageKey("key.category"); // TODO - Handle this better

                if (!sortedKeybindings.containsKey(category)) {
                    sortedKeybindings.put(category, new ArrayList<>());
                }

                sortedKeybindings.get(category).add(keyBinding);
            }

            var yOffset = 0;

            for (String category : sortedKeybindings.keySet()) {
                scrollPanelContents.add(
                    new KeybindCategory(Component.translatable(category).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.WHITE)))),
                    0,ELEMENT_SIZE * yOffset++
                );

                for (KeyMapping keyBinding : sortedKeybindings.get(category)) {

                    scrollPanelContents.add(
                        new KeybindEntry(Component.translatable(keyBinding.getName()).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.WHITE))), () -> {
                            onSelect.accept(keyBinding.getName());
                        }),
                        0, ELEMENT_SIZE * yOffset++
                    );
                }
            }

        }


        root.validate(this);
    }



    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }

    static class KeybindCategory extends WPixelPanel {

        public WLabel label;

        public KeybindCategory(Component txt) {
            label = new WLabel(txt);

            label.setHorizontalAlignment(HorizontalAlignment.CENTER);
            this.setSize(200, 16);
            this.add(label, getWidth() / 2, 4);
        }
    }

    static class KeybindEntry extends WPixelPanel {

        public WButton select;
        public WLabel label;

        public KeybindEntry(Component txt, Runnable fn) {
            select = new WButton(Component.literal(" > "));
            select.setOnClick(fn);
            label = new WLabel(txt);

            this.add(label, 2, 6);
            this.add(select, 200, 2);
            this.setSize(200, ELEMENT_SIZE);
        }
    }
}
