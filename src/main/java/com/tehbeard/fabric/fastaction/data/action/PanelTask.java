package com.tehbeard.fabric.fastaction.data.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tehbeard.fabric.fastaction.ui.MainGui;
import com.tehbeard.fabric.fastaction.ui.MinedeckScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
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
        return Component.literal("Panel: %s".formatted("[TODO IMPLEMENT]")).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_GRAY)));
    }

    @Override
    public long run() {
//        TODO - Open screen on selected panel.
        Minecraft.getInstance().gui.setScreen(
            new MinedeckScreen(
                new MainGui(false) // TODO - Pass thru the panel id to use.
            )
        );
        return 0;
    }

    @Override
    public TaskType<?> getType() {
        return TaskType.PANEL_TASK;
    }
}
