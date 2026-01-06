package com.tehbeard.fabric.quickaction.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the config
 */
public class ActionConfig {

    public static final Identifier DEFAULT_TAB = Identifier.of("quickaction","default");

    public static final Codec<ActionConfig> CODEC = RecordCodecBuilder.create( inst ->
        inst.group(
            ActionTab.CODEC.listOf().fieldOf("tabs").forGetter(ActionConfig::getTabs),
            Codec.unboundedMap(
                Codec.STRING,
                Identifier.CODEC
            ).fieldOf("defaultTabs").forGetter(ActionConfig::getDefaultTabs),
            Identifier.CODEC.fieldOf("defaultTab").forGetter(ActionConfig::getDefaultTab),
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

    private Size size = Size.SIX;

    public enum Size {
        SIX(3,2),
        FIFTEEN(5,3),
        THIRTY_TWO(8, 4);

        private final int rowSize;
        private final int rowCount;

        Size(int rowSize, int rowCount) {
            this.rowSize = rowSize;
            this.rowCount = rowCount;
        }

        public int getRowSize() {
            return rowSize;
        }

        public int getRowCount() {
            return rowCount;
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

    public Identifier getDefaultTab() {
        return defaultTab;
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
}
