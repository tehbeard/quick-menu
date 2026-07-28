package com.tehbeard.fabric.fastaction.ui.panel;

import com.tehbeard.fabric.fastaction.FastAction;
import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.data.ActionTab;
import com.tehbeard.fabric.fastaction.ui.MinedeckScreen;
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
import net.minecraft.network.chat.contents.objects.AtlasSprite;
import net.minecraft.network.chat.contents.objects.ObjectInfos;
import net.minecraft.resources.Identifier;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

public class PanelsMenu extends LightweightGuiDescription {

    class PanelsRow extends WPixelPanel {

        private ActionTab tab;

        private WLabel name;
        private WButton view;
        private WButton delete;
        private WButton setDefault;

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
            view.setSize(36, 18);
            delete = new WButton(Component.literal("Delete")).setOnClick(() -> {
                MinedeckScreen.pushCurrent(new ConfirmDeleteGenericDialog("Delete " + tab.getName(), yes -> {
                    if(yes)
                    {
                        ActionConfig.getConfig().getTabs().removeIf( t -> t.getId().equals(tab.getId()));
                        listPanel.layout();
                        try {
                            ActionConfig.getConfig().save(FastAction.getConfigFile());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    MinedeckScreen.popCurrent();
                }));
            });
            delete.setSize(36, 18);

            setDefault = new WButton(Component.literal("Set Default")) {
                @Override
                public void addTooltip(TooltipBuilder tooltip) {
                    tooltip.add(Component.literal("Currently default for:"));

                    if(ActionConfig.getConfig().getFallbackTabId().equals(tab.getId()))
                    {
                        tooltip.add(Component.literal("Default panel"));
                    }
                    ActionConfig.getConfig().getDefaultTabs()
                        .forEach( (key, id) -> {
                            if(id.equals(tab.getId()))
                            {
                                if(key.startsWith("mp-"))
                                {
                                    tooltip.add(
                                        Component.object(
                                            new AtlasSprite(Identifier.parse("minecraft:gui"), Identifier.parse("minecraft:icon/link"))
                                        ).append(" " + key.substring(3))
                                    );
                                } else {
                                    tooltip.add(
                                        Component.object(
                                            new AtlasSprite(Identifier.parse("minecraft:gui"), Identifier.parse("minecraft:icon/accessibility"))
                                        ).append(" " + key.substring(3))
                                    );
                                }
                            }
                        });
                }

                @Override
                public InputResult onClick(MouseButtonEvent click, boolean doubled) {
                    var currentWorld = ActionConfig.getConfig().getCurrentWorld();
                    ActionConfig.getConfig().setDefaultTab(currentWorld, tab.getId());
                    try {
                        ActionConfig.getConfig().save(FastAction.getConfigFile());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return InputResult.PROCESSED;
                }
            };
            setDefault.setSize(60, 18);

            this.add(name, 0,4);
            this.add(view, 100, 0);
            this.add(delete, 138, 0);

            this.add(setDefault, 176, 0);
        }

        public void setTab(ActionTab tab)
        {
            this.tab = tab;
            this.name.setText(Component.literal(tab.getName()));
        }
    }
    WListPanel listPanel;
    public PanelsMenu() {
        setUseDefaultRootBackground(false);
        PanelWithHeader root = new PanelWithHeader("Panels", 274, 175, true);
        setRootPanel(root);



        this.listPanel = new WListPanel<>(ActionConfig.getConfig().getTabs(), PanelsRow::new, (ActionTab action, PanelsRow row) -> {
            row.setTab(action);
//            button.setOnClick(action.fn);
        });

        listPanel.setSize(264,108);
        root.add(listPanel, 5,27);

        var newPanelButton = new WButton(Component.literal("New Panel")){
            @Override
            public InputResult onClick(MouseButtonEvent click, boolean doubled) {

                MinedeckScreen.pushCurrent(
                    new PanelNameDialog("New Panel", "New Panel", opt -> {
                        opt.ifPresent( name -> {
                            var newId = ActionConfig.getConfig().generateId(name);
                            var newTab = new ActionTab(newId);
                            newTab.setName(name);

                            ActionConfig.getConfig().getTabs().add(newTab);
                            try {
                                ActionConfig.getConfig().save(FastAction.getConfigFile());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            listPanel.layout();
                            MinedeckScreen.popCurrent();
                        });
                    })
                );

                try {
                    ActionConfig.getConfig().save(FastAction.getConfigFile());
                } catch (IOException e) {
                    FastAction.LOGGER.error(e.toString());
                }
                return InputResult.PROCESSED;
            }
        };
        newPanelButton.setSize(60, 18);

        root.add(newPanelButton, 209, 152);
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
