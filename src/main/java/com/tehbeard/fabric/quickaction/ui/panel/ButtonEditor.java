package com.tehbeard.fabric.quickaction.ui.panel;

import com.tehbeard.fabric.quickaction.data.ActionButton;
import com.tehbeard.fabric.quickaction.data.ActionConfig;
import com.tehbeard.fabric.quickaction.data.action.*;
import com.tehbeard.fabric.quickaction.ui.IconEntry;
import com.tehbeard.fabric.quickaction.ui.MinedeckScreen;
import com.tehbeard.fabric.quickaction.ui.component.KeybindSelectButton;
import com.tehbeard.fabric.quickaction.ui.component.PanelWithHeader;
import com.tehbeard.fabric.quickaction.ui.component.WPixelPanel;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.MergedComponentMap;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ButtonEditor extends LightweightGuiDescription {

    private static final int ELEMENT_SIZE = 28;

    private int yOffset = 0;

    private ActionButton data;

    public ButtonEditor(@NotNull ActionButton data) {
        setUseDefaultRootBackground(false);
        PanelWithHeader root = new PanelWithHeader("Action Editor", 274, 142, false);
        setRootPanel(root);

        WPixelPanel scrollPanelContents = new WPixelPanel();

        WScrollPanel scrollWrapper = new WScrollPanel(scrollPanelContents);
        scrollWrapper.getVerticalScrollBar().addPainters();
        scrollWrapper.setSize(root.getWidth() - (17 + 7), root.getHeight() - (27 + 5));
        root.add(scrollWrapper, 17, 27);

        WTextField name = new WTextField(Text.literal("Name"));
        name.setMaxLength(255);
        name.setSize(140,18);
        name.setText(data.getName());
        name.setChangedListener(data::setName);
        addRow(scrollPanelContents, "Name", name);

        WTextField model = new WTextField(Text.literal("namespace:id"));

        model.setMaxLength(4096);

        var components = (MergedComponentMap) data.getIcon().getComponents();
        if(components.hasChanged(DataComponentTypes.ITEM_MODEL)){
            model.setText(data.getIcon().get(DataComponentTypes.ITEM_MODEL).toString());
        }

        final IconEntry iconSelect = new IconEntry(data.getIcon(), selector -> {
            MinedeckScreen.pushCurrent(
                new ItemstackPicker(item -> {
                    selector.setIcon(item);
                    data.setIcon(item);
                    model.setText("");
                    MinedeckScreen.popCurrent();
                })
            );
        });
        model.setTextPredicate(str -> str.isEmpty() || !str.isBlank());
        model.setChangedListener( str -> {
            var is = iconSelect.getIcon();
            if(is != null)
            {
                if(!str.isBlank()) {
                    is.set(DataComponentTypes.ITEM_MODEL, Identifier.of(str));
                }else{
                    // HACK - reset stack
                    iconSelect.setIcon(is.getItem().getDefaultStack());
                    data.setIcon(is.getItem().getDefaultStack());
                }
            }
        });

        var iconRow = new WPixelPanel();
        iconRow.setSize(140, 26);

        iconRow.add(iconSelect,0,0);
        iconRow.add(model,28,3);
        model.setSize(112,18);

        addRow(scrollPanelContents, "Icon", iconRow);

        WButton keybindButton = new KeybindSelectButton(data);
        keybindButton.setSize(100, keybindButton.getHeight());
        addRow(scrollPanelContents,"Keybind", keybindButton);


        WLabel actionsLabel = new WLabel(Text.literal("Actions:").setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.WHITE))));
        scrollPanelContents.add(actionsLabel, 5, 5 + 7 + (ELEMENT_SIZE * yOffset++));

        WPlainPanel actions = new WPlainPanel();


        scrollPanelContents.add(actions, 5, 5 + 7 + ( ELEMENT_SIZE * yOffset));

        updateActionsList(data.getTasks(), actions);

        root.validate(this);
    }

    public void updateActionsList(List<IActionTask> actionList, WPlainPanel actions)
    {
        var currentItems = actions.streamChildren().toList();
        for (WWidget currentItem : currentItems) {
            actions.remove(currentItem);
        }

        var i = 0;
        for(var a : actionList)
        {
            final int pos = i;
            actions.add(new ActionRow(a, btn -> {
                switch (btn)
                {
                    case UP -> {
                        if(pos > 0)
                        {
                            actionList.remove(pos);
                            actionList.add(pos-1, a);
                            updateActionsList(actionList, actions);
                        }
                    }
                    case DOWN -> {
                        if(pos < (actionList.size() - 1))
                        {
                            actionList.remove(pos);
                            actionList.add(pos+1, a);
                            updateActionsList(actionList, actions);
                        }
                    }
                    case DELETE -> {
                        actionList.remove(pos);
                        updateActionsList(actionList, actions);
                    }
                }
            }),0, ELEMENT_SIZE * i++);

        }
        WButton addButton = new WButton(Text.literal("Add Action"));
        addButton.setOnClick(() -> {
            MinedeckScreen.pushCurrent(
                new ActionPicker( newAction -> {
                    actionList.add(newAction);
                    updateActionsList(actionList, actions);
                    MinedeckScreen.popCurrent();
                })
            );
        });
        actions.add(addButton, 50, (ELEMENT_SIZE * i) + 1, 120, 18);
        actions.setSize(220, ELEMENT_SIZE * (i+1));
        actions.validate(this);

    }

    public void addRow(WPixelPanel panel, String label, WWidget widget)
    {
        panel.add(new WLabel(Text.literal(label).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.WHITE)))),  5,5 + 7 + (ELEMENT_SIZE * yOffset));
        panel.add(widget,  100, 5 + (ELEMENT_SIZE * yOffset++));
    }


    enum RowButton {
        UP,
        DOWN,
        DELETE
    }
    class ActionRow extends WPixelPanel
    {
        public ActionRow(@NotNull IActionTask task, Consumer<RowButton> onClick) {

            this.add(new WLabel(Text.literal("%s:".formatted(StringUtils.capitalize(task.type()))).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.WHITE)))),0,5);

            // TODO - Change this based on the action type.
            // With command, can we autocomplete the string? (Not easily)
            // with delay, ensure numeric only
            // with panel, autocomplete panels? (Or show a list popup)
            // with keybind, show button to list popup.

            WWidget config = new WLabel(Text.literal("Unknown"));

            if(task instanceof CommandTask c) {
                var txt = new WTextField();
                txt.setMaxLength(256);
                txt.setText(c.getCommand());
                txt.setChangedListener(c::setCommand);
                config = txt;
            } else if (task instanceof DelayTask d)
            {
                var txt = new WTextField();
                txt.setMaxLength(256);
                txt.setText(String.valueOf(d.getTicks()));
                txt.setTextPredicate(str -> str.matches("^[1-9][0-9]*$"));
                txt.setChangedListener(str -> d.setTicks(str.isBlank() ? 0 : Long.parseLong(str)));
                config = txt;
            } else if(task instanceof KeybindTask k)
            {
                var btn = new WButton( k.getKeybind() == null ? Text.literal("Not Bound") : Text.translatable(k.getKeybind()));
                btn.setOnClick(() -> {
                    MinedeckScreen.pushCurrent(
                        new KeybindPicker( kb -> {
                            k.setKeybind(kb);
                            btn.setLabel(Text.translatable(k.getKeybind()));
                            MinedeckScreen.popCurrent();
                        })
                    );
                });
                config = btn;
            } else if(task instanceof PanelTask p)
            {
                var btn = new WButton(p.description());
                btn.setOnClick(() -> {
                    p.setTarget(ActionConfig.DEFAULT_TAB);
                    // TODO - Open panel to select a tab
                });
                config = btn;
            }
            config.setSize(120, 18);
            this.add(config,50, (config instanceof WButton) ? 1 : 0);

            this.add(new WButton(Text.literal("▲")).setOnClick(() -> onClick.accept(RowButton.UP)),172, 1);
            this.add(new WButton(Text.literal("▼")).setOnClick(() -> onClick.accept(RowButton.DOWN)),192, 1);
            this.add(new WButton(Text.literal("❌")).setOnClick(() -> onClick.accept(RowButton.DELETE)),212, 1);

            this.setSize(230, ELEMENT_SIZE);
        }

        @Override
        public boolean canResize() {
            return false;
        }
    }


    @Override
    public TriState isDarkMode() {
        return TriState.TRUE;
    }


}
