package com.tehbeard.fabric.quickaction.ui.panel;

import com.tehbeard.fabric.quickaction.data.action.CommandTask;
import com.tehbeard.fabric.quickaction.data.action.DelayTask;
import com.tehbeard.fabric.quickaction.data.action.IActionTask;
import com.tehbeard.fabric.quickaction.data.action.KeybindTask;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import xyz.imcodist.quickmenu.other.KeybindHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(110, 110);
        root.setInsets(Insets.NONE);

        root.setBackgroundPainter(BackgroundPainter.createNinePatch(
            new Texture(Identifier.of("quickmenu", "textures/background_darker.png")),
            builder -> builder.cornerSize(8).cornerUv(0.33f)
        ));

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

        root.add(listPanel, 5,5,100,100);

        root.validate(this);
    }



    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }


}
