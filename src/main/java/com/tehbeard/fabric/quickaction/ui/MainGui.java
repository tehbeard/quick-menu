package com.tehbeard.fabric.quickaction.ui;

import com.tehbeard.fabric.quickaction.ui.component.TextButton;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xyz.imcodist.quickmenu.data.ActionButtonData;
import xyz.imcodist.quickmenu.other.ActionButtonDataHandler;
import com.tehbeard.fabric.quickaction.ui.component.EditButton;

public class MainGui extends LightweightGuiDescription {

    public MainGui(boolean isEditMode) {
        setUseDefaultRootBackground(false);
//        WGridPanel root = new WGridPanel();
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(180, 114); // TODO - Load this from config, calculating off of the size
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

        WLabel editButton = new EditButton(isEditMode);
        editButton.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        editButton.setVerticalAlignment(VerticalAlignment.CENTER);
        header.add(editButton, 155,3);

        if(isEditMode)
        {
            var configBtn = new TextButton("\uD83D\uDD27", (click, doubleClick) -> {
                // TODO - Open config menu
//                MinecraftClient.getInstance().setScreen(new MainScreenCotton(false));
                return InputResult.PROCESSED;
            });
            configBtn.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            configBtn.setVerticalAlignment(VerticalAlignment.CENTER);
            header.add(configBtn, 145,3);
        }

        root.add(header, 0,0,180,24);


        WGridPanel scrollPanelContents = new WGridPanel(26);
        scrollPanelContents.setGaps(4,2);

        int posX = 0;
        int posY = 0;

        for(ActionButtonData data : ActionButtonDataHandler.actions)
        {
            ActionEntry actionWidget = new ActionEntry(data, isEditMode);
            scrollPanelContents.add(actionWidget, posX, posY,1,1);
            posX++;
            if(posX == 5) // TODO - Pull value from config
            {
                posX = 0;
                posY++;
            }
        };

        if(isEditMode)
        {
            ActionEntry actionWidget = new ActionEntry(null, true);
            scrollPanelContents.add(actionWidget, posX, posY,1,1);
        }



        WScrollPanel scrollWrapper = new WScrollPanel(scrollPanelContents);
        scrollWrapper.getVerticalScrollBar().addPainters();
        root.add(scrollWrapper, 17, 27, root.getWidth() - (17 + 7), root.getHeight() - (27 + 5));

        root.validate(this);
    }
}
