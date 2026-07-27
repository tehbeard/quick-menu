package com.tehbeard.fabric.fastaction.ui.panel;

import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.data.action.*;
import com.tehbeard.fabric.fastaction.ui.component.PanelWithHeader;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.function.Consumer;

/**
 * Lets selecting a particular panel.
 */
public class PanelPicker extends LightweightGuiDescription {

    record PanelPickerEntry(String label, String tooltip, Runnable fn){}

    public PanelPicker(Consumer<Identifier> selectedPanel) {
        setUseDefaultRootBackground(false);
        PanelWithHeader root = new PanelWithHeader("Select Panel", 140, 110, true);
        setRootPanel(root);

        List<PanelPickerEntry> list = ActionConfig.getConfig().getTabs().stream().map( tab -> new PanelPickerEntry(
            tab.getName(),
            "Contains %s actions".formatted(tab.getButtons().size()),
            () -> selectedPanel.accept(tab.getId()))).toList();


        var listPanel = new WListPanel<>(list, WButton::new, (PanelPickerEntry action, WButton button) -> {
            button.setLabel(Component.literal(action.label));
            // TODO - Find way to adjust the tooltip, may need custom button
            button.setOnClick(action.fn);
        });

        listPanel.setSize(130,85);
        root.add(listPanel, 5,27);

        root.validate(this);
    }



    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }


}
