package com.tehbeard.fabric.fastaction.ui.panel;

import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.ui.component.PanelWithHeader;
import com.tehbeard.fabric.fastaction.ui.component.WPixelPanel;
import com.tehbeard.fabric.fastaction.ui.component.WSelectButton;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.Arrays;

public class ConfigMenu extends LightweightGuiDescription {

    public ConfigMenu() {
        setUseDefaultRootBackground(false);
        PanelWithHeader root = new PanelWithHeader("Config", 274, 175, true);
        setRootPanel(root);

        WPixelPanel scrollPanelContents = new WPixelPanel();

        // Size selector
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

        // Actions in tooltip
        var tglActions = new WToggleButton();
        tglActions.setToggle(ActionConfig.getConfig().isActionsInTooltip());
        tglActions.setOnToggle( a -> ActionConfig.getConfig().setActionsInTooltip(a));
        addRow(scrollPanelContents,
            20, "Show action in tooltip",
            tglActions
        );

        // Close menu on action
        var tglClose = new WToggleButton();
        tglClose.setToggle(ActionConfig.getConfig().isCloseOnAction());
        tglClose.setOnToggle( a -> ActionConfig.getConfig().setCloseOnAction(a));
        addRow(scrollPanelContents,
            40, "Close menu on action",
            tglClose
        );


        WScrollPanel scrollWrapper = new WScrollPanel(scrollPanelContents);
        scrollWrapper.getVerticalScrollBar().addPainters();
        scrollWrapper.setSize(root.getWidth() - (17 + 7), root.getHeight() - (27 + 5));
        root.add(scrollWrapper, 17, 27);

        root.validate(this);
    }

    public void addRow(WPixelPanel panel, int y, String label, WWidget widget) {
        panel.add(new WLabel(Text.literal(label).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.WHITE)))), 5, 5 + 7 + y);
        panel.add(widget, 120, 5 + y);
    }

    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }
}
