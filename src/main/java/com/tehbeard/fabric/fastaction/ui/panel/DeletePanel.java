package com.tehbeard.fabric.fastaction.ui.panel;

import com.tehbeard.fabric.fastaction.FastAction;
import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.data.ActionTab;
import com.tehbeard.fabric.fastaction.ui.*;
import com.tehbeard.fabric.fastaction.ui.component.TextButton;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.util.List;

public class DeletePanel extends AbstractActionGui {
    public DeletePanel(ActionTab currentTab) {
        super(
            "Delete Mode",
            currentTab,
            (panel,data) -> new ActionEntry(data, (click, dbl) -> {

                MinedeckScreen.pushCurrent(
                    new ConfirmDeleteDialog(data, didDelete -> {
                        MinedeckScreen.popCurrent();
                        if(didDelete)
                        {
                            currentTab.getButtons()
                                .remove(data);
                            panel.updateItems();
                            try {
                                ActionConfig.getConfig().save(FastAction.getConfigFile());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            panel.updateItems();
                        }
                    })
                );

            }),
            List.of(
                new TextButton("❌", (click, dbl) -> {
                    Minecraft.getInstance().gui.setScreen(new MinedeckScreen(new EditPanel(currentTab)));
                    return InputResult.PROCESSED;
                }, TextButton.staticTooltip("Exit Delete Mode"))
            ),
            List.of(
            )
        );
    }
}
