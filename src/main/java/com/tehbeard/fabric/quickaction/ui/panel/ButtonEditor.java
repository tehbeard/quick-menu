package com.tehbeard.fabric.quickaction.ui.panel;

import com.tehbeard.fabric.quickaction.ui.IconEntry;
import com.tehbeard.fabric.quickaction.ui.MinedeckScreen;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import xyz.imcodist.quickmenu.other.KeybindHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Redo as a list, try to figure an approach for category vs. keybind split
 * TODO: Implement search filtering
 * TODO: Sort label alignment.
 */
public class ButtonEditor extends LightweightGuiDescription {

    private static final int ELEMENT_SIZE = 28;

    private int yOffset = 0;

    public ButtonEditor() {
        setUseDefaultRootBackground(false);
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(274, 142);
        root.setInsets(Insets.NONE);

        root.setBackgroundPainter(BackgroundPainter.createNinePatch(
            new Texture(Identifier.of("quickmenu", "textures/background.png")),
            builder -> builder.cornerSize(8).cornerUv(0.33f)
        ));

        WPlainPanel header = new WPlainPanel();
        header.setInsets(Insets.NONE);
        header.setBackgroundPainter(BackgroundPainter.createNinePatch(
            new Texture(Identifier.of("quickmenu", "textures/background_header.png")),
            builder -> builder.cornerSize(8).cornerUv(0.33f)
        ));
        root.add(header, 0,0,root.getWidth(),24);

        WLabel label = new WLabel(Text.literal("Action Editor"), 0xFF_FFFFFF);
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        label.setVerticalAlignment(VerticalAlignment.CENTER);

        header.add(label, (root.getWidth() / 2),3);


        WPlainPanel scrollPanelContents = new WPlainPanel();

        WScrollPanel scrollWrapper = new WScrollPanel(scrollPanelContents);
        scrollWrapper.getVerticalScrollBar().addPainters();
        root.add(scrollWrapper, 17, 27, root.getWidth() - (17 + 7), root.getHeight() - (27 + 5));

        WTextField name = new WTextField(Text.literal("Name"));
        name.setMaxLength(255);
        name.setSize(140,18);


        addRow(scrollPanelContents, "Name", name);
        final IconEntry iconSelect = new IconEntry(null, selector -> {
            MinedeckScreen.pushCurrent(
                new ItemstackPicker(item -> {
                    selector.setIcon(item);
                    MinedeckScreen.popCurrent();
                })
            );
        });

        addRow(scrollPanelContents, "Icon", iconSelect);

        WTextField model = new WTextField(Text.literal("custom model name"));
        model.setMaxLength(4096);
        model.setSize(140,18);

        model.setTextPredicate(str -> str.isEmpty() || !str.isBlank());
        model.setChangedListener( str -> {
            var is = iconSelect.getIcon();
            if(is != null)
            {
                if(!str.isBlank()) {
                    is.set(DataComponentTypes.ITEM_MODEL, Identifier.of(str));
                }else{
                    // HACK - reset stack
                    iconSelect.setIcon(is.getItem().getDefaultStack());
                }
            }
        });

        addRow(scrollPanelContents, "Custom Model", model);


        WButton keybindButton = new WButton(Text.literal("Not Bound"));
        keybindButton.setSize(100, keybindButton.getHeight());

        addRow(scrollPanelContents,"Keybind", keybindButton);

        root.validate(this);
    }

    public void addRow(WPlainPanel panel, String label, WWidget widget)
    {
        panel.add(new WLabel(Text.literal(label).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.WHITE)))),  5,5 + 7 + ELEMENT_SIZE * yOffset);
        panel.add(widget,  100, 5 + ELEMENT_SIZE * yOffset++, widget.getWidth(), widget.getHeight());
    }



    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }


}
