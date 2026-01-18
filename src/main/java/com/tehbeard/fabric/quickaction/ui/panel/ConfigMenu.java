package com.tehbeard.fabric.quickaction.ui.panel;

import com.tehbeard.fabric.quickaction.data.ActionConfig;
import com.tehbeard.fabric.quickaction.ui.ItemstackEntry;
import com.tehbeard.fabric.quickaction.ui.component.WSelectButton;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ConfigMenu extends LightweightGuiDescription {

    public ConfigMenu() {
        setUseDefaultRootBackground(false);
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(274, 175);

        root.setInsets(Insets.NONE);

        root.setBackgroundPainter(BackgroundPainter.createNinePatch(
            new Texture(Identifier.of("quickmenu", "textures/background_darker.png")),
            builder -> builder.cornerSize(8).cornerUv(0.33f)
        ));

        WPlainPanel header = new WPlainPanel();
        header.setInsets(Insets.NONE);
        header.setBackgroundPainter(BackgroundPainter.createNinePatch(
            new Texture(Identifier.of("quickmenu", "textures/background_header.png")),
            builder -> builder.cornerSize(8).cornerUv(0.33f)
        ));
        root.add(header, 0,0,root.getWidth(),24);

        WLabel label = new WLabel(Text.literal("Quick Actions"), 0xFF_FFFFFF);
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        label.setVerticalAlignment(VerticalAlignment.CENTER);
        header.add(label, (root.getWidth() / 2),3);


        WPlainPanel scrollPanelContents = new WPlainPanel();

        // TODO - Size selector
        addRow(scrollPanelContents, 0, "Size",
            new WSelectButton<>(
                Arrays.asList(
                    ActionConfig.Size.values()
                ),
                ActionConfig.getConfig().getSize(),
                size -> Text.literal(size.getLabel()).append(" (%s x %s)".formatted(size.getRowSize(), size.getRowCount())),
                size -> ActionConfig.getConfig().setSize(size)
            )
        );

        // TODO - Actions in tooltip
        var tglActions = new WToggleButton();
        tglActions.setToggle(ActionConfig.getConfig().isActionsInTooltip());
        tglActions.setOnToggle( a -> ActionConfig.getConfig().setActionsInTooltip(a));
        addRow(scrollPanelContents,
            20, "Show action in tooltip",
            tglActions
        );

        // TODO - close on action
        var tglClose = new WToggleButton();
        tglClose.setToggle(ActionConfig.getConfig().isCloseOnAction());
        tglClose.setOnToggle( a -> ActionConfig.getConfig().setCloseOnAction(a));
        addRow(scrollPanelContents,
            40, "Close menu on action",
            tglClose
        );

        addRow(scrollPanelContents,
            20, "Show action in tooltip",
            tglActions
        );

        WScrollPanel scrollWrapper = new WScrollPanel(scrollPanelContents);
        scrollWrapper.getVerticalScrollBar().addPainters();
        root.add(scrollWrapper, 17, 27, root.getWidth() - (17 + 7), root.getHeight() - (27 + 5));


//        WSprite icon = new WSprite(Identifier.of("quickmenu","textures/search_icon.png"));
//        root.add(icon, 17 + 7, 5 + 7);
//        icon.setSize(12,12);

        root.validate(this);
    }

    public void addRow(WPlainPanel panel, int y, String label, WWidget widget) {
        panel.add(new WLabel(Text.literal(label).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.WHITE)))), 5, 5 + 7 + y);
        panel.add(widget, 120, 5 + y, widget.getWidth(), widget.getHeight());
    }

    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }
}
