package com.tehbeard.fabric.fastaction.ui.component;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

public class TextButton extends WLabel {

    private final BiFunction<MouseButtonEvent, Boolean, InputResult> fn;
    private final Consumer<TooltipBuilder> tooltipFn;

    public static Consumer<TooltipBuilder> staticTooltip(String text) {
        return builder -> builder.add(Component.literal(text));
    }
//    private String
    public TextButton(
        String txt,
        BiFunction<MouseButtonEvent, Boolean, InputResult> fn,
        Consumer<TooltipBuilder> tooltipFn
        ) {

        super(Component.literal(txt), 0xFF_FFFFFF);
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
    public void paint(GuiGraphicsExtractor context, int x, int y, int mouseX, int mouseY) {
        super.paint(context, x, y, mouseX, mouseY);
        var hovered = isFocused() || isWithinBounds(mouseX,mouseY);

        if(hovered)
        {
            context.requestCursor(CursorTypes.POINTING_HAND);
        }
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Override
    public InputResult onMouseDown(MouseButtonEvent click, boolean doubled) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        return fn.apply(click, doubled);
    }
}
