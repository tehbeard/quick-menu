package com.tehbeard.fabric.fastaction.ui.panel;

import com.tehbeard.fabric.fastaction.data.ActionButton;
import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.data.action.CommandTask;
import com.tehbeard.fabric.fastaction.data.action.DelayTask;
import com.tehbeard.fabric.fastaction.data.action.IActionTask;
import com.tehbeard.fabric.fastaction.data.action.KeybindTask;
import com.tehbeard.fabric.fastaction.ui.ActionEntry;
import com.tehbeard.fabric.fastaction.ui.component.PanelWithHeader;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;

/**
 * TODO: Redo as a list, try to figure an approach for category vs. keybind split
 * TODO: Implement search filtering
 * TODO: Sort label alignment.
 */
public class ConfirmDeleteDialog extends LightweightGuiDescription {


    public ConfirmDeleteDialog(ActionButton data, Consumer<Boolean> onClick) {
        setUseDefaultRootBackground(false);
        PanelWithHeader root = new PanelWithHeader("Delete this action?", 160, 86, true);
        setRootPanel(root);

        var icon = new ActionEntry(data,(m,d) -> {}).hideTooltip();
        root.add(icon, 67,42);

        var confirmButton = new WButton(Component.literal("Yes"));
        confirmButton.setSize(36, 18);
        confirmButton.setOnClick( () -> onClick.accept(true));
        root.add(confirmButton, 5, 46);

        var cancelButton = new WButton(Component.literal("No"));
        cancelButton.setSize(36, 18);
        cancelButton.setOnClick( () -> onClick.accept(false));
        root.add(cancelButton, 119, 46);



        root.validate(this);
    }



    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }


}
