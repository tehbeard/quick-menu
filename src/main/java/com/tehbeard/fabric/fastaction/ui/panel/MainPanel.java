package com.tehbeard.fabric.fastaction.ui.panel;

import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.ui.AbstractActionGui;
import com.tehbeard.fabric.fastaction.ui.ActionEntry;
import com.tehbeard.fabric.fastaction.ui.MainScreen;
import com.tehbeard.fabric.fastaction.ui.MinedeckScreen;
import com.tehbeard.fabric.fastaction.ui.component.TextButton;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public class MainPanel extends AbstractActionGui {
    public MainPanel() {
        super(
            "Fast Actions",
            data -> new ActionEntry(data, (click, dbl) -> {
                    if (ActionConfig.getConfig().isCloseOnAction()) {
                        MinecraftClient.getInstance().setScreen(null);
                    }
                    data.run(false);
            }),
            List.of(
                new TextButton("✎", (click, dbl) -> {
                    MinecraftClient.getInstance().setScreen(new MinedeckScreen(new EditPanel()));
                    return InputResult.PROCESSED;
                }, TextButton.staticTooltip("Edit Actions"))
            ),
            List.of()
        );
    }
}
