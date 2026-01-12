package com.tehbeard.fabric.quickaction.ui.panel;

import com.tehbeard.fabric.quickaction.ui.ItemstackEntry;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import xyz.imcodist.quickmenu.other.KeybindHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * TODO: Redo as a list, try to figure an approach for category vs. keybind split
 * TODO: Implement search filtering
 * TODO: Sort label alignment.
 */
public class KeybindPicker extends LightweightGuiDescription {

    private static final int ELEMENT_SIZE = 20;

    public KeybindPicker() {
        setUseDefaultRootBackground(false);
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(274, 142);
        root.setInsets(Insets.NONE);

        root.setBackgroundPainter(BackgroundPainter.createNinePatch(
            new Texture(Identifier.of("quickmenu", "textures/background.png")),
            builder -> builder.cornerSize(8).cornerUv(0.33f)
        ));

        WPlainPanel scrollPanelContents = new WPlainPanel();

        // TODO - Method to fill out scrollPanelContents
//        updateItems(scrollPanelContents, null);
        KeyBinding[] keyBindings = KeybindHandler.getKeybindings();

        WScrollPanel scrollWrapper = new WScrollPanel(scrollPanelContents);
        scrollWrapper.getVerticalScrollBar().addPainters();
        root.add(scrollWrapper, 17, 27, root.getWidth() - (17 + 7), root.getHeight() - (27 + 5));

        if(keyBindings != null)
        {
            Map<String, ArrayList<KeyBinding>> sortedKeybindings = new HashMap<>();
            for (KeyBinding keyBinding : keyBindings) {
                String category = keyBinding.getCategory().id().toTranslationKey("key.category"); // TODO - Handle this better

                if (!sortedKeybindings.containsKey(category)) {
                    sortedKeybindings.put(category, new ArrayList<>());
                }

                sortedKeybindings.get(category).add(keyBinding);
            }

            var yOffset = 0;

            for (String category : sortedKeybindings.keySet()) {
                scrollPanelContents.add(
                    new KeybindCategory(Text.translatable(category).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.WHITE)))),
                    0,ELEMENT_SIZE * yOffset++
                );

                for (KeyBinding keyBinding : sortedKeybindings.get(category)) {

                    scrollPanelContents.add(
                        new KeybindEntry(Text.translatable(keyBinding.getId()).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.WHITE))), () -> {}),
                        0, ELEMENT_SIZE * yOffset++
                    );
//                    WLabel keyLabel = new WLabel(Text.translatable(keyBinding.getId()));
//                    WButton buttonSelect = new WButton(Text.translatable("menu.action_picker.select"));
//                    scrollPanelContents.add(keyLabel,0, yOffset, 10,1);
//                    scrollPanelContents.add(buttonSelect,10, yOffset++, 1,1);

//                    keybindsLayout.child(layout);
                }
            }

        }



        WTextField searchField = new WTextField(Text.literal(""));
//        searchField.setChangedListener( str -> {
//            // TODO - Filter search list.
//            updateItems(scrollPanelContents, str);
//        });
//
//        root.add(searchField,48, 5, root.getWidth() - (15 + 48 + 7), 16);

        WSprite icon = new WSprite(Identifier.of("quickmenu","textures/search_icon.png"));
        root.add(icon, 17 + 7, 5 + 7);
        icon.setSize(12,12);

        root.validate(this);
    }



    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }

    class KeybindCategory extends WPlainPanel {

        public WLabel label;

        public KeybindCategory(Text txt) {
            label = new WLabel(txt);

            label.setHorizontalAlignment(HorizontalAlignment.CENTER);
            this.setSize(200, 16);
            this.add(label, getWidth() / 2, 4);
        }
    }

    class KeybindEntry extends WPlainPanel {

        public WButton select;
        public WLabel label;

        public KeybindEntry(Text txt, Runnable fn) {
            select = new WButton(Text.translatable("menu.action_picker.select"));
            select.setOnClick(fn);
            label = new WLabel(txt);

            this.add(label, 2, 6);
            this.add(select, 200, 2);
            this.setSize(200, ELEMENT_SIZE);
        }
    }
}
