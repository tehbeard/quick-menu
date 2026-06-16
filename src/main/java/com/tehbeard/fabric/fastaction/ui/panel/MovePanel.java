package com.tehbeard.fabric.fastaction.ui.panel;

import com.tehbeard.fabric.fastaction.FastAction;
import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.ui.*;
import com.tehbeard.fabric.fastaction.ui.component.TextButton;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.util.List;

public class MovePanel extends AbstractActionGui {
    public MovePanel() {
        super(
            "Move Mode",
            (panel,data) -> new MoveEntry(data, (click, isLeft) -> {
                // TODO: Move the data entry one to the left (-1 idx) if true, or right (+1 idx)
                var btns = ActionConfig.getConfig().getDefaultTab().getButtons();
                var currentIdx = btns.indexOf(data);
                if(currentIdx == -1)
                {
                    return;
                }
                if(isLeft && currentIdx > 0)
                {
                    currentIdx--;
                    btns.remove(data);
                    btns.add(currentIdx, data);
                } else if(!isLeft && currentIdx < (btns.size()-1) ) {
                    currentIdx++;
                    btns.remove(data);
                    btns.add(currentIdx, data);
                }
                try {
                    ActionConfig.getConfig().save(FastAction.getConfigFile());
                    panel.updateItems();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }),
            List.of(
                new TextButton("❌", (click, dbl) -> {
                    Minecraft.getInstance().gui.setScreen(new MinedeckScreen(new MainPanel()));
                    return InputResult.PROCESSED;
                }, TextButton.staticTooltip("Exit Move Mode"))
            ),
            List.of(
            )
        );
    }
}
