package com.tehbeard.fabric.quickaction.ui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xyz.imcodist.quickmenu.data.ActionButtonData;
import xyz.imcodist.quickmenu.other.ActionButtonDataHandler;
import com.tehbeard.fabric.quickaction.ui.component.EditButton;

public class MainGui extends LightweightGuiDescription {

    public MainGui() {
        setUseDefaultRootBackground(false);
//        WGridPanel root = new WGridPanel();
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(180, 114); // TODO - Load this from config
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
        WLabel label = new WLabel(Text.literal("Quick Menu"), 0xFF_FFFFFF);
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        label.setVerticalAlignment(VerticalAlignment.CENTER);

        header.add(label, 80,3);

        WLabel editButton = new EditButton();
        editButton.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        editButton.setVerticalAlignment(VerticalAlignment.CENTER);
        header.add(editButton, 155,3);

        root.add(header, 0,0,180,24);


        WGridPanel scrollPanelContents = new WGridPanel(26);
        scrollPanelContents.setGaps(4,2);

        int posX = 0;
        int posY = 0;

        for(ActionButtonData data : ActionButtonDataHandler.actions)
        {
            ActionEntry actionWidget = new ActionEntry(data);
            scrollPanelContents.add(actionWidget, posX, posY,1,1);
            posX++;
            if(posX == 5)
            {
                posX = 0;
                posY++;
            }
        };

        WScrollPanel scrollWrapper = new WScrollPanel(scrollPanelContents);
        scrollWrapper.getVerticalScrollBar().addPainters();
        root.add(scrollWrapper, 17, 27, root.getWidth() - (17 + 7), root.getHeight() - (27 + 5));

        root.validate(this);
    }
}
