package com.tehbeard.fabric.fastaction.ui;

import com.tehbeard.fabric.fastaction.data.ActionButton;
import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.ui.component.EditButton;
import com.tehbeard.fabric.fastaction.ui.component.PanelWithHeader;
import com.tehbeard.fabric.fastaction.ui.panel.ButtonEditor;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.*;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import com.tehbeard.fabric.fastaction.FastAction;

import java.io.IOException;
import java.util.Optional;

public class MainGui extends LightweightGuiDescription {

    private boolean isEditMode;

    public MainGui(boolean isEditMode) {
        this.isEditMode = isEditMode;
        setUseDefaultRootBackground(false);
        PanelWithHeader root = new PanelWithHeader(
            "Quick Menu",
            ActionConfig.getConfig().getSize().getWidth(),
            ActionConfig.getConfig().getSize().getHeight(),
            false);
        setRootPanel(root);

        WLabel editButton = new EditButton(isEditMode);
        editButton.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        editButton.setVerticalAlignment(VerticalAlignment.CENTER);
        root.add(editButton, root.getWidth() - 25, 3);


        WGridPanel scrollPanelContents = new WGridPanel(26);
        scrollPanelContents.setGaps(4, 2);

        updateItems(scrollPanelContents);


        WScrollPanel scrollWrapper = new WScrollPanel(scrollPanelContents);
        scrollWrapper.getVerticalScrollBar().addPainters();
        scrollWrapper.setSize(root.getWidth() - (17 + 7), root.getHeight() - (27 + 5));
        root.add(scrollWrapper, 17, 27);

        root.validate(this);
    }

    protected void updateItems(WGridPanel panel) {
        var currentItems = panel.streamChildren().toList();
        for (WWidget currentItem : currentItems) {
            panel.remove(currentItem);
        }
        panel.setSize(4, 4);
        var perRow = ActionConfig.getConfig().getSize().getRowSize();
        int posX = 0;
        int posY = 0;


        for (ActionButton data : ActionConfig.getConfig().getDefaultTab().getButtons()) {
            ActionEntry actionWidget = new ActionEntry(data, (click, dbl) -> {
                if (click.button() == GLFW.GLFW_MOUSE_BUTTON_2 && isEditMode) {
                    ActionConfig.getConfig().getDefaultTab().getButtons().remove(data);
                    try {
                        ActionConfig.getConfig().save(FastAction.getConfigFile());
                        updateItems(panel);
                    } catch (IOException e) {
                        FastAction.LOGGER.error(e.toString());
                    }

                } else if (!isEditMode) {
                    if (ActionConfig.getConfig().isCloseOnAction()) {
                        Minecraft.getInstance().setScreen(null);
                    }
                    data.run(false);
                } else {
                    // TODO - Open edit mode
                    Minecraft.getInstance().setScreen(
                        new MinedeckScreen(new ButtonEditor(data)).onRemoved(() -> {
                            try {
                                ActionConfig.getConfig().save(FastAction.getConfigFile());
                            } catch (IOException e) {
                                FastAction.LOGGER.error(e.toString());
                            }
                        })
                    );
                }
            });
            panel.add(actionWidget, posX, posY, 1, 1);
            posX++;
            if (posX == perRow) {
                posX = 0;
                posY++;
            }
        }
        ;

        if (isEditMode) {
            ActionEntry actionWidget = new ActionEntry(null, (click, dbl) -> {
                var newData = new ActionButton().setName("");
                ActionConfig.getConfig().getDefaultTab().getButtons().add(newData);
                Minecraft.getInstance().setScreen(new MinedeckScreen(new ButtonEditor(
                    newData
                )).onRemoved(() -> {
                    try {
                        ActionConfig.getConfig().save(FastAction.getConfigFile());
                    } catch (IOException e) {
                        FastAction.LOGGER.error(e.toString());
                    }
                }));
            });
            panel.add(actionWidget, posX, posY, 1, 1);
            posX++;
            if (posX == perRow) // TODO - Pull value from config
            {
                posX = 0;
                posY++;
            }
            ConfigEntry configWidget = new ConfigEntry();
            panel.add(configWidget, posX, posY, 1, 1);
        }

        panel.validate(this);
        Optional.ofNullable(panel.getParent()).ifPresent(WPanel::layout);
    }

    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }
}
