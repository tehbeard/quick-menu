package com.tehbeard.fabric.fastaction.ui.panel;

import com.tehbeard.fabric.fastaction.data.ActionButton;
import com.tehbeard.fabric.fastaction.ui.ActionEntry;
import com.tehbeard.fabric.fastaction.ui.component.PanelWithHeader;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.network.chat.Component;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * TODO: Redo as a list, try to figure an approach for category vs. keybind split
 * TODO: Implement search filtering
 * TODO: Sort label alignment.
 */
public class DuplicatePanelDialog extends LightweightGuiDescription {


    public DuplicatePanelDialog(Consumer<Optional<String>> onClick) {
        setUseDefaultRootBackground(false);
        PanelWithHeader root = new PanelWithHeader("Duplicate Panel", 160, 86, true);
        setRootPanel(root);

        var panelName = new WTextField(Component.literal("Name"));
        panelName.setMaxLength(256);

        panelName.setSize(150, 18);
        root.add(panelName, 5, 30);
//        var icon = new ActionEntry(data,(m,d) -> {}).hideTooltip();
//        root.add(icon, 67,42);

        var confirmButton = new WButton(Component.literal("Yes"));
        confirmButton.setSize(44, 18);
        confirmButton.setOnClick( () -> {
            if(!panelName.getText().isBlank())
            {
                onClick.accept(Optional.of(panelName.getText()));
            }
        });
        root.add(confirmButton, 5, 66);

        var cancelButton = new WButton(Component.literal("Cancel"));
        cancelButton.setSize(44, 18);
        cancelButton.setOnClick( () -> onClick.accept(Optional.empty()));
        root.add(cancelButton, 111, 66);



        root.validate(this);
    }



    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }


}
