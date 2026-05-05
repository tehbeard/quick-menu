package com.tehbeard.fabric.fastaction.ui.panel;

import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.ui.*;
import com.tehbeard.fabric.fastaction.ui.component.TextButton;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import com.tehbeard.fabric.fastaction.FastAction;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;

public class EditPanel extends AbstractActionGui {
    public EditPanel() {
        super(
            "Edit Mode",
            (panel,data) -> new ActionEntry(data, (click, dbl) -> {
                Minecraft.getInstance().setScreenAndShow(
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
                new TextButton("❌", (click, dbl) -> {
                    Minecraft.getInstance().setScreenAndShow(new MinedeckScreen(new MainPanel()));
                    return InputResult.PROCESSED;
                }, TextButton.staticTooltip("Exit Edit Mode")),
                new TextButton("🗑", (click, dbl) -> {
                    Minecraft.getInstance().setScreenAndShow(new MinedeckScreen(new DeletePanel()));
                    return InputResult.PROCESSED;
                }, TextButton.staticTooltip("Delete Actions")),
                new TextButton("🔁", (click, dbl) -> {
                    Minecraft.getInstance().setScreenAndShow(new MinedeckScreen(new MovePanel()));
                    return InputResult.PROCESSED;
                }, TextButton.staticTooltip("Move Actions"))
            ),
            List.of(
                new AddActionEntry(),
                new ConfigEntry()
            )
        );
    }
}
