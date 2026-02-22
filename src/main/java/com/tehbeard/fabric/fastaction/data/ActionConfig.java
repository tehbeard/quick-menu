package com.tehbeard.fabric.fastaction.data;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tehbeard.fabric.fastaction.FastAction;
import com.tehbeard.fabric.fastaction.data.action.KeybindTask;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Represents the config
 */
public class ActionConfig {

    public static final Identifier DEFAULT_TAB = Identifier.of("minedeck", "default");

    public static final Codec<ActionConfig> CODEC = RecordCodecBuilder.create(inst ->
        inst.group(
            ActionTab.CODEC.listOf().fieldOf("tabs").forGetter(ActionConfig::getTabs),
            Codec.unboundedMap(
                Codec.STRING,
                Identifier.CODEC
            ).fieldOf("defaultTabs").forGetter(ActionConfig::getDefaultTabs),
            Identifier.CODEC.fieldOf("defaultTab").forGetter(ActionConfig::getDefaultTabId),
            Codec.STRING.xmap(Size::valueOf, Enum::name).fieldOf("size").forGetter(ActionConfig::getSize)
        ).apply(inst, (
            tabs,
            defaultTabs,
            defaultTab,
            size
        ) -> {
            var cfg = new ActionConfig();
            cfg.setTabs(tabs);
            cfg.setDefaultTabs(defaultTabs);
            cfg.setDefaultTab(defaultTab);
            cfg.setSize(size);
            return cfg;
        }));

    // List of all action tabs
    private List<ActionTab> tabs = new ArrayList<>();

    // Mapping of sp_<name> or mp_<address> to the default tab for that world/server.
    private Map<String, Identifier> defaultTabs = new HashMap<>();

    // Default tab to use in all other situations
    private Identifier defaultTab = DEFAULT_TAB;

    private Size size = Size.FIFTEEN;

    private boolean actionsInTooltip = true;
    private boolean closeOnAction = true;

    public enum Size {
        SIX("Small", 3, 2,124, 86),
        FIFTEEN("Medium", 5, 3, 180, 114),
        THIRTY_TWO("Large", 8, 4, 274, 142);

        private final String label;
        private final int rowSize;
        private final int rowCount;

        private final int width;
        private final int height;

        Size(String label, int rowSize, int rowCount, int width, int height) {
            this.label = label;
            this.rowSize = rowSize;
            this.rowCount = rowCount;
            this.height = height;
            this.width = width;
        }

        public String getLabel() {
            return label;
        }

        public int getRowSize() {
            return rowSize;
        }

        public int getRowCount() {
            return rowCount;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public List<ActionTab> getTabs() {
        return tabs;
    }

    public void setTabs(List<ActionTab> tabs) {
        this.tabs = tabs;
    }

    public Map<String, Identifier> getDefaultTabs() {
        return defaultTabs;
    }

    public void setDefaultTabs(Map<String, Identifier> defaultTabs) {
        this.defaultTabs = defaultTabs;
    }

    public Identifier getDefaultTabId() {
        return defaultTab;
    }

    public ActionTab getDefaultTab()
    {
        return this.tabs.stream().filter( t -> t.getId().equals(getDefaultTabId())).findFirst().orElseThrow();
    }

    public void setDefaultTab(Identifier defaultTab) {
        this.defaultTab = defaultTab;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public boolean isActionsInTooltip() {
        return actionsInTooltip;
    }

    public void setActionsInTooltip(boolean actionsInTooltip) {
        this.actionsInTooltip = actionsInTooltip;
    }

    public boolean isCloseOnAction() {
        return closeOnAction;
    }

    public void setCloseOnAction(boolean closeOnAction) {
        this.closeOnAction = closeOnAction;
    }

    public JsonElement encode() {
        var logger = FastAction.LOGGER;

        var res = ActionConfig.CODEC.encodeStart(JsonOps.INSTANCE, this);
        return res.resultOrPartial(logger::error).orElseThrow();
    }

    public void save(File file) throws IOException {

        var gson = new GsonBuilder().setPrettyPrinting().create();
        Files.write(
            file.toPath(),
            gson.toJson(encode()).getBytes(StandardCharsets.UTF_8)
        );
    }

    private static ActionConfig config = null;

    public static void load(File file) throws IOException
    {
        if(file.exists())
        {
            var rawCfg = JsonParser.parseString(
                Files.readString(
                    file.toPath()
                )
            );

            var res = ActionConfig.CODEC.decode(JsonOps.INSTANCE, rawCfg);
            res.ifSuccess( p -> {
                config = p.getFirst();
            }).ifError( e -> {
                Logger.getGlobal().severe("FAILED TO LOAD CONFIG: " + e.message());
                System.exit(-100);
            });
        } else {
            config = getDefaultConfig();
            config.save(file);
        }

    }

    public static ActionConfig getConfig()
    {
        if(config == null)
        {
            throw new RuntimeException("Config not yet loaded");
        }
        return config;
    }

    public static ActionConfig getDefaultConfig() {
        var cfg = new ActionConfig();

        var tab = new ActionTab();
        tab.setId(ActionConfig.DEFAULT_TAB);
        cfg.getTabs().add(
            tab
        );

        var btn = new ActionButton();
        btn.setName("Open Vanilla Quick Actions");
        btn.setIcon(Items.KNOWLEDGE_BOOK.getDefaultStack());
        tab.getButtons().add(btn);

        btn.getTasks().add(
            new KeybindTask("key.quickActions")
        );
        return cfg;
    }
}
