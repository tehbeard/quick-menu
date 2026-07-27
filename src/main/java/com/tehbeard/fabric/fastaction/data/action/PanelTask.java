package com.tehbeard.fabric.fastaction.data.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.ui.MinedeckScreen;
import com.tehbeard.fabric.fastaction.ui.panel.MainPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class PanelTask implements IActionTask {

    public static final MapCodec<PanelTask> CODEC = RecordCodecBuilder.mapCodec(inst ->
        inst.group(
            Identifier.CODEC.fieldOf("target").forGetter(PanelTask::getTarget)
        ).apply(inst, PanelTask::new)
    );

    public Identifier target;

    public PanelTask() {
    }

    public PanelTask(Identifier target) {
        this.target = target;
    }

    public Identifier getTarget() {
        return target;
    }

    public void setTarget(Identifier target) {
        this.target = target;
    }

    @Override
    public String type() {
        return "panel";
    }

    @Override
    public Component description() {
        return Component.literal("Panel: %s".formatted(ActionConfig.getConfig().getTabs().stream().filter(tab -> tab.getId().equals(target)).findFirst().get().getName() ));
    }

    @Override
    public long run() {
        Minecraft.getInstance().gui.setScreen(
            new MinedeckScreen(
                new MainPanel(
                    ActionConfig.getConfig().getTabs()
                        .stream().filter( t -> t.getId().equals(target))
                        .findFirst()
                        .orElseGet(() -> ActionConfig.getConfig().getContextualDefaultTab())
                )
            )
        );
        return 0;
    }

    @Override
    public TaskType<?> getType() {
        return TaskType.PANEL_TASK;
    }
}
