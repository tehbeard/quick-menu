package com.tehbeard.fabric.quickaction.ui.panel;

import com.tehbeard.fabric.quickaction.data.action.CommandTask;
import com.tehbeard.fabric.quickaction.data.action.DelayTask;
import com.tehbeard.fabric.quickaction.data.action.IActionTask;
import com.tehbeard.fabric.quickaction.data.action.KeybindTask;
import com.tehbeard.fabric.quickaction.ui.component.PanelWithHeader;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

/**
 * TODO: Redo as a list, try to figure an approach for category vs. keybind split
 * TODO: Implement search filtering
 * TODO: Sort label alignment.
 */
public class ActionPicker extends LightweightGuiDescription {

    record ActionPickerEntry(String label, String tooltip, Runnable fn){}

    public ActionPicker(Consumer<IActionTask> newAction) {
        setUseDefaultRootBackground(false);
        PanelWithHeader root = new PanelWithHeader("New Task", 110, 110, true);
        setRootPanel(root);

        List<ActionPickerEntry> list = List.of(
            new ActionPickerEntry("Command", "Runs a chat command", () -> { newAction.accept(new CommandTask("")); }),
            new ActionPickerEntry("Keybind", "Activates a keybind", () -> { newAction.accept(new KeybindTask("")); }),
            new ActionPickerEntry("Delay", "Adds a delay between tasks", () -> { newAction.accept(new DelayTask(20)); })
        );

        var listPanel = new WListPanel<>(list, WButton::new, (ActionPickerEntry action, WButton button) -> {
            button.setLabel(Text.literal(action.label));
            // TODO - Find way to adjust the tooltip, may need custom button
            button.setOnClick(action.fn);
        });

        listPanel.setSize(100,85);
        root.add(listPanel, 5,27);

        root.validate(this);
    }



    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }


}
