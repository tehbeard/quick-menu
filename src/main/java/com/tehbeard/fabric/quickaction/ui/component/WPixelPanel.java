package com.tehbeard.fabric.quickaction.ui.component;

import io.github.cottonmc.cotton.gui.widget.WPanelWithInsets;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Insets;

public class WPixelPanel extends WPanelWithInsets {
    /**
     * Adds a new widget to this panel.
     *
     * <p>If the widget {@linkplain WWidget#canResize() can be resized},
     * it will be resized to (18, 18).
     *
     * @param w the widget
     * @param x the X position
     * @param y the Y position
     */
    public void add(WWidget w, int x, int y) {
        children.add(w);
        w.setParent(this);
        w.setLocation(insets.left() + x, insets.top() + y);
        expandToFit(w, insets);
        //valid = false;
    }

    public WPixelPanel setInsets(Insets insets) {
        super.setInsets(insets);
        return this;
    }
}
