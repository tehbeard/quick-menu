package com.tehbeard.fabric.fastaction.ui.panel;

import com.tehbeard.fabric.fastaction.FastAction;
import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.ui.*;
import com.tehbeard.fabric.fastaction.ui.component.TextButton;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.util.List;

public class DeletePanel extends AbstractActionGui {
    public DeletePanel() {
        super(
            "Delete Mode",
            (panel,data) -> new ActionEntry(data, (click, dbl) -> {

                MinedeckScreen.pushCurrent(
                    new ConfirmDeleteDialog(data, didDelete -> {
                        MinedeckScreen.popCurrent();
                        if(didDelete)
                        {
                            ActionConfig.getConfig().getDefaultTab().getButtons()
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
//                Minecraft.getInstance().setScreen(
//                    new MinedeckScreen(new ButtonEditor(data)).onRemoved(() -> {
//                        try {
//                            ActionConfig.getConfig().save(FastAction.getConfigFile());
//                        } catch (IOException e) {
//                            FastAction.LOGGER.error(e.toString());
//                        }
//                    })
//                );
            }),
            List.of(
                new TextButton("❌", (click, dbl) -> {
                    Minecraft.getInstance().setScreen(new MinedeckScreen(new EditPanel()));
                    return InputResult.PROCESSED;
                }, TextButton.staticTooltip("Exit Delete Mode"))
            ),
            List.of(
            )
        );
    }
}
