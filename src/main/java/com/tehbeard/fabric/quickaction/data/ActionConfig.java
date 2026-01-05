package com.tehbeard.fabric.quickaction.data;

import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

/**
 * Represents the config
 */
public class ActionConfig {

    // List of all action tabs
    public List<ActionTab> tabs;

    // Mapping of sp_<name> or mp_<address> to the default tab for that world/server.
    public Map<String, Identifier> defaultTabs;

    // Default tab to use in all other situations
    public String defaultTab;

    public Size size;

    public enum Size {
        SIX(3,2),
        FIFTEEN(5,3),
        THIRTY_TWO(8, 4);

        private int rowSize;
        private int rowCount;

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
}
