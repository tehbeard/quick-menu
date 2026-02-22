package com.tehbeard.fabric.fastaction.ui.component;

import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class TextButton extends WLabel {

    private final BiFunction<Click, Boolean, InputResult> fn;
    private final Consumer<TooltipBuilder> tooltipFn;

    public static Consumer<TooltipBuilder> staticTooltip(String text) {
        return builder -> builder.add(Text.literal(text));
    }
//    private String
    public TextButton(
        String txt,
        BiFunction<Click, Boolean, InputResult> fn,
        Consumer<TooltipBuilder> tooltipFn
        ) {

        super(Text.literal(txt), 0xFF_FFFFFF);
        setSize(8,8);
        this.fn = fn;
        this.tooltipFn = tooltipFn;
    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        if(tooltipFn != null) {
            tooltipFn.accept(tooltip);
        }
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        super.paint(context, x, y, mouseX, mouseY);
        var hovered = isFocused() || isWithinBounds(mouseX,mouseY);

        if(hovered)
        {
            context.setCursor(StandardCursors.POINTING_HAND);
        }
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Override
    public InputResult onMouseDown(Click click, boolean doubled) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.ui(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        return fn.apply(click, doubled);
    }
}
