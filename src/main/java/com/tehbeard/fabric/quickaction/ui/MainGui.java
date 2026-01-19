package com.tehbeard.fabric.quickaction.ui;

import com.tehbeard.fabric.quickaction.data.ActionButton;
import com.tehbeard.fabric.quickaction.data.ActionConfig;
import com.tehbeard.fabric.quickaction.ui.component.EditButton;
import com.tehbeard.fabric.quickaction.ui.component.PanelWithHeader;
import com.tehbeard.fabric.quickaction.ui.component.TextButton;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.*;
import net.fabricmc.fabric.api.util.TriState;

public class MainGui extends LightweightGuiDescription {

    public MainGui(boolean isEditMode) {
        setUseDefaultRootBackground(false);
        var size = ActionConfig.getConfig().getSize();
        PanelWithHeader root = new PanelWithHeader("Quick Menu", size.getWidth(), size.getHeight(), false);
        setRootPanel(root);
        int perRow = size.getRowSize();

        WLabel editButton = new EditButton(isEditMode);
        editButton.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        editButton.setVerticalAlignment(VerticalAlignment.CENTER);
        root.add(editButton, root.getWidth() - 25,3);


        var rotateButton = new TextButton("\uD83D\uDD04 ↕",(click, doubleClick) -> InputResult.PROCESSED);
        rotateButton.setVerticalAlignment(VerticalAlignment.CENTER);

        root.add(rotateButton, 8, 3);

        WGridPanel scrollPanelContents = new WGridPanel(26);
        scrollPanelContents.setGaps(4,2);

        int posX = 0;
        int posY = 0;



        for(ActionButton data : ActionConfig.getConfig().getDefaultTab().getButtons())
        {
            ActionEntry actionWidget = new ActionEntry(data, isEditMode);
            scrollPanelContents.add(actionWidget, posX, posY,1,1);
            posX++;
            if(posX == perRow)
            {
                posX = 0;
                posY++;
            }
        };

        if(isEditMode)
        {
            ActionEntry actionWidget = new ActionEntry(null, true);
            scrollPanelContents.add(actionWidget, posX, posY,1,1);
            posX++;
            if(posX == perRow) // TODO - Pull value from config
            {
                posX = 0;
                posY++;
            }
            ConfigEntry configWidget = new ConfigEntry();
            scrollPanelContents.add(configWidget, posX, posY,1,1);
        }




        WScrollPanel scrollWrapper = new WScrollPanel(scrollPanelContents);
        scrollWrapper.getVerticalScrollBar().addPainters();
        scrollWrapper.setSize(root.getWidth() - (17 + 7), root.getHeight() - (27 + 5));
        root.add(scrollWrapper, 17, 27);

        root.validate(this);
    }

    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }
}
