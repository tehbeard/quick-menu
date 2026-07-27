package com.tehbeard.fabric.fastaction.ui.panel;

import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.data.ActionTab;
import com.tehbeard.fabric.fastaction.ui.MinedeckScreen;
import com.tehbeard.fabric.fastaction.ui.component.OpenPanelsButton;
import com.tehbeard.fabric.fastaction.ui.component.PanelWithHeader;
import com.tehbeard.fabric.fastaction.ui.component.WPixelPanel;
import com.tehbeard.fabric.fastaction.ui.component.WSelectButton;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.Arrays;

public class PanelsMenu extends LightweightGuiDescription {

    class PanelsRow extends WPixelPanel {

        private ActionTab tab;

        private WLabel name;
        private WButton view;
        private WButton delete;

        public PanelsRow()
        {
            this.setSize(264, 18);
            this.setInsets(Insets.NONE);
            name = new WLabel(Component.literal("N/A"));
            view = new WButton(Component.literal("View")) {
                @Override
                public InputResult onClick(MouseButtonEvent click, boolean doubled) {
                    MinedeckScreen.popCurrent();
                    MinedeckScreen.popCurrent();
                    Minecraft.getInstance().gui.setScreen(
                        new MinedeckScreen(new MainPanel(tab))
                    );
                    return super.onClick(click, doubled);
                }
            };
            view.setSize(54, 18);
            delete = new WButton(Component.literal("Delete"));
            delete.setSize(54, 18);
            this.add(name, 0,2);
            this.add(view, 130, 0);
            this.add(delete, 190, 0);
        }

        public void setTab(ActionTab tab)
        {
            this.tab = tab;
            this.name.setText(Component.literal(tab.getName()));
        }
    }
    public PanelsMenu() {
        setUseDefaultRootBackground(false);
        PanelWithHeader root = new PanelWithHeader("Panels", 274, 175, true);
        setRootPanel(root);



        var listPanel = new WListPanel<>(ActionConfig.getConfig().getTabs(), PanelsRow::new, (ActionTab action, PanelsRow row) -> {
            row.setTab(action);
//            button.setOnClick(action.fn);
        });

        listPanel.setSize(264,120);
        root.add(listPanel, 5,27);

        root.validate(this);
    }

    public void addRow(WPixelPanel panel, int y, String label, WWidget widget) {
        panel.add(new WLabel(Component.literal(label).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.WHITE)))), 5, 5 + 7 + y);
        panel.add(widget, 120, 5 + y);
    }

    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }
}
