package com.tehbeard.fabric.fastaction.data.action;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tehbeard.fabric.fastaction.ui.MainGui;
import com.tehbeard.fabric.fastaction.ui.MinedeckScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

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
    public Text description() {
        return Text.literal("Panel: %s".formatted("[TODO IMPLEMENT]")).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.DARK_GRAY)));
    }

    @Override
    public long run() {
//        TODO - Open screen on selected panel.
        MinecraftClient.getInstance().setScreen(
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
