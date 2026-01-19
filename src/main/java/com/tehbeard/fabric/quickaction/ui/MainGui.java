package com.tehbeard.fabric.quickaction.ui;

import com.tehbeard.fabric.quickaction.data.ActionButton;
import com.tehbeard.fabric.quickaction.data.ActionConfig;
import com.tehbeard.fabric.quickaction.data.ActionConfigMigrator;
import com.tehbeard.fabric.quickaction.ui.component.EditButton;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.*;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xyz.imcodist.quickmenu.data.ActionButtonData;
import xyz.imcodist.quickmenu.other.ActionButtonDataHandler;

public class MainGui extends LightweightGuiDescription {

    public MainGui(boolean isEditMode) {
        setUseDefaultRootBackground(false);
//        WGridPanel root = new WGridPanel();
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        // TODO - Load from config
        // Small
//        root.setSize(124, 86);
//        int perRow = 3;
        // Medium
//        root.setSize(180, 114); // TODO - Load this from config, calculating off of the size
//        int perRow = 5;
        // Large
//        root.setSize(274, 142);
        var size = ActionConfig.getConfig().getSize();
        root.setSize(size.getWidth(), size.getHeight());
        int perRow = size.getRowSize();

        root.setInsets(Insets.NONE);

        root.setBackgroundPainter(BackgroundPainter.createNinePatch(
            new Texture(Identifier.of("quickmenu", "textures/background.png")),
            builder -> builder.cornerSize(8).cornerUv(0.33f)
        ));

        WPlainPanel header = new WPlainPanel();
        header.setInsets(Insets.NONE);
        header.setBackgroundPainter(BackgroundPainter.createNinePatch(
            new Texture(Identifier.of("quickmenu", "textures/background_header.png")),
            builder -> builder.cornerSize(8).cornerUv(0.33f)
        ));
        root.add(header, 0,0,root.getWidth(),24);

        WLabel label = new WLabel(Text.literal("Quick Actions"), 0xFF_FFFFFF);
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        label.setVerticalAlignment(VerticalAlignment.CENTER);

        header.add(label, (root.getWidth() / 2) - 9,3);

        WLabel editButton = new EditButton(isEditMode);
        editButton.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        editButton.setVerticalAlignment(VerticalAlignment.CENTER);
        header.add(editButton, root.getWidth() - 25,3);




        WGridPanel scrollPanelContents = new WGridPanel(26);
        scrollPanelContents.setGaps(4,2);

        int posX = 0;
        int posY = 0;



        for(ActionButton data : ActionConfig.getConfig().getDefaultTab().getButtons())
        {
            ActionEntry actionWidget = new ActionEntry(data, isEditMode);
            scrollPanelContents.add(actionWidget, posX, posY,1,1);
            posX++;
            if(posX == perRow) // TODO - Pull value from config
            {
                posX = 0;
                posY++;
            }
        };
        /*for(ActionButtonData data : ActionButtonDataHandler.actions)
        {
            ActionEntry actionWidget = new ActionEntry(ActionConfigMigrator.migrateActionButton(data.toJSON()), isEditMode);
            scrollPanelContents.add(actionWidget, posX, posY,1,1);
            posX++;
            if(posX == perRow) // TODO - Pull value from config
            {
                posX = 0;
                posY++;
            }
        }*/

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
        root.add(scrollWrapper, 17, 27, root.getWidth() - (17 + 7), root.getHeight() - (27 + 5));

        root.validate(this);
        label.setLocation((root.getWidth() / 2),3);
    }

    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }
}
