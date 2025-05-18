package xyz.imcodist.quickmenu.data;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import xyz.imcodist.quickmenu.QuickMenu;
import xyz.imcodist.quickmenu.data.command_actions.BaseActionData;
import xyz.imcodist.quickmenu.data.command_actions.CommandActionData;
import xyz.imcodist.quickmenu.data.command_actions.DelayActionData;
import xyz.imcodist.quickmenu.data.command_actions.KeybindActionData;
import xyz.imcodist.quickmenu.other.ActionButtonDelayHandler;
import xyz.imcodist.quickmenu.other.ModConfigModel;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class ActionButtonData {
    public String name;
    public ArrayList<BaseActionData> actions = new ArrayList<>();
    public ItemStack icon;
    public ArrayList<Integer> keybind = new ArrayList<>();

    public boolean keyPressed = false;

    public ActionButtonDataJSON toJSON() {
        ActionButtonDataJSON jsonData = new ActionButtonDataJSON();

        jsonData.name = name;
        jsonData.actions = new ArrayList<>();

        jsonData.keybind = keybind;

        actions.forEach((action) -> {
            ArrayList<String> actionArray = new ArrayList<>();
            actionArray.add(action.getJsonType());
            actionArray.add(action.getJsonValue());

            jsonData.actions.add(actionArray);
        });

        if (icon != null) {
            if (icon.getRegistryEntry().getKey().isPresent()) {
                jsonData.icon = icon.getRegistryEntry().getKey().get().getValue().toString();
            }

            String cmdString = "";

            CustomModelDataComponent customModelDataComponent = icon.getOrDefault(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelDataComponent.DEFAULT);
            if (!customModelDataComponent.strings().isEmpty()) cmdString = customModelDataComponent.strings().getFirst();

            jsonData.customModelData = cmdString;
        }

        return jsonData;
    }

    public static ActionButtonData fromJSON(ActionButtonDataJSON json) {
        ActionButtonData data = new ActionButtonData();

        data.name = json.name;
        data.actions = new ArrayList<>();

        data.keybind = json.keybind;

        json.actions.forEach((actionArray) -> {
            BaseActionData actionData = getActionDataType(actionArray.get(0), actionArray.get(1));
            data.actions.add(actionData);
        });

        if (json.icon != null) {
            data.icon = new ItemStack(Registries.ITEM.get(Identifier.of(json.icon)));

            CustomModelDataValues customModelDataValues = new CustomModelDataValues(json.customModelData);
            data.icon.set(DataComponentTypes.CUSTOM_MODEL_DATA, customModelDataValues.getComponent());
        }

        return data;
    }

    private static BaseActionData getActionDataType(String type, String value) {
        switch (type) {
            case "base" -> {
                return new BaseActionData();
            }
            case "cmd" -> {
                CommandActionData commandActionData = new CommandActionData();
                commandActionData.command = value;
                return commandActionData;
            }
            case "key" -> {
                KeybindActionData keybindActionData = new KeybindActionData();
                keybindActionData.keybindTranslationKey = value;
                return keybindActionData;
            }
            case "delay" -> {
                var delayAction = new DelayActionData();
                delayAction.ticks = Math.max(0, Long.parseLong(value));
                return delayAction;
            }
        }

        return null;
    }

    public InputUtil.Key getKey() {
        if (keybind.size() < 4) return null;
        return InputUtil.fromKeyCode(keybind.get(0), keybind.get(1));
    }

    public void run() {
        run(false);
    }

    public void run(boolean isKeybind) {
        // Show run message.
        ModConfigModel.DisplayRunText displayRunText = QuickMenu.CONFIG.displayRunText();
        if (displayRunText == ModConfigModel.DisplayRunText.ALWAYS || displayRunText == ModConfigModel.DisplayRunText.KEYBIND_ONLY && isKeybind) {
            MinecraftClient client = MinecraftClient.getInstance();

            if (client != null && client.player != null) {
                client.player.sendMessage(Text.of("Ran action \"" + name + "\""), true);
            }
        }
//        MinecraftClient.getInstance()

        // Run the buttons action.
//        actions.forEach(BaseActionData::run);
        new ActionButtonDataContext(new ArrayList<>(actions), 0).run();
    }

    public static class ActionButtonDataContext {

        private final List<BaseActionData> actions;
        private long delay;

        public ActionButtonDataContext(
            List<BaseActionData> actions,
            long delay
        ) {
            this.actions = actions;
            this.delay = delay;
        }

        public void run() {
            for (var idx = 0; idx < actions.size(); idx++) {
                var action = actions.get(idx);
                var nextDelay = action.run();
                if (nextDelay > 0) {
                    // TODO : Envelope the remaining actions in a timer.
                    var nextSet = new ActionButtonDataContext(actions.subList(idx + 1, actions.size()), nextDelay);
                    // TODO - Create a timer construct that listens
                    ActionButtonDelayHandler.INSTANCE.add(nextSet);
                    return;
                }
            }
        }

        public long getDelay() {
            return delay;
        }

        public void decrementDelay()
        {
            delay = Math.max(0, delay - 1);
        }

        public boolean isReady()
        {
            return delay == 0;
        }


    }

    public static class CustomModelDataValues {
        public List<String> stringList;
        public List<Float> floatList = List.of();

        public CustomModelDataValues(String cmdStr) {
            stringList = List.of(cmdStr);

            try{
                floatList = List.of(Float.parseFloat(cmdStr));
            } catch (Exception ignored) {}
        }

        public CustomModelDataComponent getComponent() {
            return new CustomModelDataComponent(
                this.floatList, List.of(),
                this.stringList, List.of()
            );
        }
    }
}

