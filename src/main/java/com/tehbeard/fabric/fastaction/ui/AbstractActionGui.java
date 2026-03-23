package com.tehbeard.fabric.fastaction.ui;

import com.tehbeard.fabric.fastaction.data.ActionButton;
import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.ui.component.PanelWithHeader;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.fabricmc.fabric.api.util.TriState;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents an action panel.
 * Takes a title,
 * list of menu buttons
 * list of actions
 * list of additional actions
 */
public abstract class AbstractActionGui extends LightweightGuiDescription {

    private BiFunction<AbstractActionGui,ActionButton, WWidget> btnMaker;
    private List<WWidget> additionalActions;

    private WGridPanel scrollPanelContents;

    public AbstractActionGui(
        String heading,
        BiFunction<AbstractActionGui,ActionButton, WWidget> btnMaker,
        List<WWidget> headingButtons,
        List<WWidget> additionalActions
    ) {
        this.btnMaker = btnMaker;
        this.additionalActions = additionalActions;

        setUseDefaultRootBackground(false);
        PanelWithHeader root = new PanelWithHeader(
            heading,
            ActionConfig.getConfig().getSize().getWidth(),
            ActionConfig.getConfig().getSize().getHeight(),
            false);
        setRootPanel(root);

        int offset = 12;
        for( var btn: headingButtons) {
            root.add(btn, root.getWidth() - offset, 8);
            offset += btn.getWidth() + 2;
        }


//        var rotateButton = new TextButton("\uD83D\uDD04 ↕🗑", (click, doubleClick) -> InputResult.PROCESSED);
//        rotateButton.setVerticalAlignment(VerticalAlignment.CENTER);
//
//        root.add(rotateButton, 8, 3);

        scrollPanelContents = new WGridPanel(26);
        scrollPanelContents.setGaps(4, 2);

        updateItems();


        WScrollPanel scrollWrapper = new WScrollPanel(scrollPanelContents);
        scrollWrapper.getVerticalScrollBar().addPainters();
        scrollWrapper.setSize(root.getWidth() - (17 + 7), root.getHeight() - (27 + 5));
        root.add(scrollWrapper, 17, 27);

        root.validate(this);
    }

    public void updateItems() {
        var currentItems = scrollPanelContents.streamChildren().toList();
        for (WWidget currentItem : currentItems) {
            scrollPanelContents.remove(currentItem);
        }
        scrollPanelContents.setSize(4, 4);
        var perRow = ActionConfig.getConfig().getSize().getRowSize();
        int posX = 0;
        int posY = 0;


        for (ActionButton data : ActionConfig.getConfig().getDefaultTab().getButtons()) {
            var actionWidget = this.btnMaker.apply(this,data);

            scrollPanelContents.add(actionWidget, posX, posY, 1, 1);
            posX++;
            if (posX == perRow) {
                posX = 0;
                posY++;
            }
        }
        ;
        for( var addButton: additionalActions)
        {
            scrollPanelContents.add(addButton, posX, posY, 1, 1);
            posX++;
            if (posX == perRow) {
                posX = 0;
                posY++;
            }
        }

        scrollPanelContents.validate(this);
        Optional.ofNullable(scrollPanelContents.getParent()).ifPresent(WPanel::layout);
    }

    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }
}
