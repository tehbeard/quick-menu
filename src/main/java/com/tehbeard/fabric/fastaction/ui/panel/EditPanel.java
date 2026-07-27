package com.tehbeard.fabric.fastaction.ui.panel;

import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.data.ActionTab;
import com.tehbeard.fabric.fastaction.ui.*;
import com.tehbeard.fabric.fastaction.ui.component.TextButton;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import com.tehbeard.fabric.fastaction.FastAction;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;

public class EditPanel extends AbstractActionGui {
    public EditPanel(ActionTab currentTab) {
        super(
            "Edit Mode",
            currentTab,
            (panel,data) -> new ActionEntry(data, (click, dbl) -> {
                Minecraft.getInstance().gui.setScreen(
                    new MinedeckScreen(new ButtonEditor(data)).onRemoved(() -> {
                        try {
                            ActionConfig.getConfig().save(FastAction.getConfigFile());
                        } catch (IOException e) {
                            FastAction.LOGGER.error(e.toString());
                        }
                    })
                );
            }),
            List.of(
                new TextButton("⧉", (click, dbl) -> {
                    MinedeckScreen.pushCurrent(new PanelNameDialog("Duplicate Panel", null, name -> {
                        MinedeckScreen.popCurrent();
                    }));
                    return InputResult.PROCESSED;
                }, TextButton.staticTooltip("Duplicate panel")),
                new TextButton("❌", (click, dbl) -> {
                    Minecraft.getInstance().gui.setScreen(new MinedeckScreen(new MainPanel(currentTab)));
                    return InputResult.PROCESSED;
                }, TextButton.staticTooltip("Exit Edit Mode")),
                new TextButton("🗑", (click, dbl) -> {
                    Minecraft.getInstance().gui.setScreen(new MinedeckScreen(new DeletePanel(currentTab)));
                    return InputResult.PROCESSED;
                }, TextButton.staticTooltip("Delete Actions")),
                new TextButton("🔁", (click, dbl) -> {
                    Minecraft.getInstance().gui.setScreen(new MinedeckScreen(new MovePanel(currentTab)));
                    return InputResult.PROCESSED;
                }, TextButton.staticTooltip("Move Actions")),
                new TextButton("✎", (click, dbl) -> {
                    MinedeckScreen.pushCurrent(new PanelNameDialog("Rename Panel", currentTab.getName(), name -> {
                        name.ifPresent( n -> {
                            currentTab.setName(n);
                            try {
                                ActionConfig.getConfig().save(FastAction.getConfigFile());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                        MinedeckScreen.popCurrent();
                    }));
                    return InputResult.PROCESSED;
                }, TextButton.staticTooltip("Rename panel"))
            ),
            List.of(
                new AddActionEntry(currentTab),
                new ConfigEntry()
            )
        );
    }
}
