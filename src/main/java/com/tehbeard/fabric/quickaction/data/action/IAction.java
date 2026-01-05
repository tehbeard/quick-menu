package com.tehbeard.fabric.quickaction.data.action;

import net.minecraft.text.Text;

public interface IAction {

    // TODO - Codec registry for all available IAction types.

    String type();
    Text description();

    long run();
}
