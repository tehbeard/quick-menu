package com.tehbeard.fabric.quickaction.ui.panel;

import com.tehbeard.fabric.quickaction.ui.ItemstackEntry;
import com.tehbeard.fabric.quickaction.ui.MinedeckScreen;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.Consumer;

public class ItemstackPicker  extends LightweightGuiDescription {

    private Consumer<ItemStack> onSelect;
    public ItemstackPicker(Consumer<ItemStack> onSelect) {
        setUseDefaultRootBackground(false);
//        WGridPanel root = new WGridPanel();
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(274, 175);
        this.onSelect = onSelect;
        int perRow = 8;

        root.setInsets(Insets.NONE);

        root.setBackgroundPainter(BackgroundPainter.createNinePatch(
            new Texture(Identifier.of("quickmenu", "textures/background_darker.png")),
            builder -> builder.cornerSize(8).cornerUv(0.33f)
        ));

        WGridPanel scrollPanelContents = new WGridPanel(26);
        scrollPanelContents.setGaps(4,2);

        // TODO - Method to fill out scrollPanelContents
        updateItems(scrollPanelContents, null);

        WScrollPanel scrollWrapper = new WScrollPanel(scrollPanelContents);
        scrollWrapper.getVerticalScrollBar().addPainters();
        root.add(scrollWrapper, 17, 27, root.getWidth() - (17 + 7), root.getHeight() - (27 + 5));

        WTextField searchField = new WTextField(Text.literal(""));
        searchField.setChangedListener( str -> {
            // TODO - Filter search list.
            updateItems(scrollPanelContents, str);
        });

        root.add(searchField,48, 5, root.getWidth() - (15 + 48 + 7), 16);

        WSprite icon = new WSprite(Identifier.of("quickmenu","textures/search_icon.png"));
        root.add(icon, 17 + 7, 5 + 7);
        icon.setSize(12,12);

        root.validate(this);
    }

    protected void updateItems(WGridPanel panel, String filter)
    {
        var currentItems = panel.streamChildren().toList();
        for (WWidget currentItem : currentItems) {
            panel.remove(currentItem);
        }
        panel.setSize(4,4);

        int posX = 0;
        int posY = 0;
        final int perRow = 8;

        var items = Registries.ITEM.stream().filter( i -> (
            filter == null || filter.isEmpty() || i.getName().getString().toLowerCase().contains(filter)
                )
        ).toList();

        for(Item item : items)
        {
            var data = item.getDefaultStack();
            ItemstackEntry actionWidget = new ItemstackEntry(data, a ->{
                onSelect.accept(a);
            });
            panel.add(actionWidget, posX, posY,1,1);
            posX++;
            if(posX == perRow) // TODO - Pull value from config
            {
                posX = 0;
                posY++;
            }
        };
        panel.validate(this);
        Optional.ofNullable(panel.getParent()).ifPresent(WPanel::layout);
    }

    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }
}
