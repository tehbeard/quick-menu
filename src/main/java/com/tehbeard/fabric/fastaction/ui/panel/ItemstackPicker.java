package com.tehbeard.fabric.fastaction.ui.panel;

import com.tehbeard.fabric.fastaction.ui.ItemstackEntry;
import com.tehbeard.fabric.fastaction.ui.component.WPixelPanel;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.Optional;
import java.util.function.Consumer;

public class ItemstackPicker  extends LightweightGuiDescription {

    private Consumer<ItemStack> onSelect;
    public ItemstackPicker(Consumer<ItemStack> onSelect) {
        setUseDefaultRootBackground(false);
//        WGridPanel root = new WGridPanel();
        WPixelPanel root = new WPixelPanel();
        setRootPanel(root);
        root.setSize(274, 175);
        this.onSelect = onSelect;
        int perRow = 8;

        root.setInsets(Insets.NONE);

        root.setBackgroundPainter(BackgroundPainter.createNinePatch(
            new Texture(Identifier.fromNamespaceAndPath("fastactions", "textures/background_darker.png")),
            builder -> builder.cornerSize(8).cornerUv(0.33f)
        ));

        WGridPanel scrollPanelContents = new WGridPanel(26);
        scrollPanelContents.setGaps(4,2);

        // TODO - Method to fill out scrollPanelContents
        updateItems(scrollPanelContents, null);

        WScrollPanel scrollWrapper = new WScrollPanel(scrollPanelContents);
        scrollWrapper.getVerticalScrollBar().addPainters();
        scrollWrapper.setSize(root.getWidth() - (17 + 7), root.getHeight() - (27 + 5));
        root.add(scrollWrapper, 17, 27);

        WTextField searchField = new WTextField(Component.literal(""));
        searchField.setChangedListener( str -> {
            // TODO - Filter search list.
            updateItems(scrollPanelContents, str);
        });

        searchField.setSize(root.getWidth() - (15 + 48 + 7), 16);
        root.add(searchField,48, 5);

        WSprite icon = new WSprite(Identifier.fromNamespaceAndPath("fastactions","textures/search_icon.png"));
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

        var items = BuiltInRegistries.ITEM.stream().filter( i -> (
            filter == null || filter.isEmpty() || i.getName().getString().toLowerCase().contains(filter)
                )
        ).toList();

        for(Item item : items)
        {
            var data = item.getDefaultInstance();
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
