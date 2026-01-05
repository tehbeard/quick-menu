package com.tehbeard.fabric.quickaction.data.action;

import net.minecraft.text.Text;

public interface IAction {

    String type();
    Text description();

    long run();
}
