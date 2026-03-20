package com.tehbeard.fabric.fastaction.ui;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.tehbeard.fabric.fastaction.data.ActionButton;
import com.tehbeard.fabric.fastaction.data.ActionConfig;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

public class MoveEntry extends ActionEntry {


    public static final Identifier TEXTURE_LEFT_ARROW = Identifier.fromNamespaceAndPath("minecraft", "textures/gui/sprites/transferable_list/unselect.png");
    public static final Identifier TEXTURE_RIGHT_ARROW = Identifier.fromNamespaceAndPath("minecraft", "textures/gui/sprites/transferable_list/select.png");

    /**
     * TODO: Refactor to accept a left click and right click function.
     *
     * @param data
     * @param onClick
     */
    public MoveEntry(ActionButton data, BiConsumer<MouseButtonEvent, Boolean> onClick) {
        super(data, onClick);
    }

    boolean isLeft;


    @Override
    public void paint(GuiGraphicsExtractor context, int x, int y, int mouseX, int mouseY) {

        super.paint(context, x, y, mouseX, mouseY);

        var isHovered = isWithinBounds(mouseX, mouseY) || getHost().isFocused(this);

        this.isLeft = isHovered && (mouseX < (getWidth() / 2));


        if (isHovered) {
            context.requestCursor(CursorTypes.POINTING_HAND);

            context.blit(
                RenderPipelines.GUI_TEXTURED,
                this.isLeft ? TEXTURE_LEFT_ARROW : TEXTURE_RIGHT_ARROW,
                x + 5 + (isLeft ? 0 : 3),
                y + 5,
                0, 0,
                16, 16,
                16,
                16
            );
        }


    }

    @Override
    public InputResult onMouseDown(MouseButtonEvent click, boolean doubled) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        this.onClick.accept(click, this.isLeft);

        return InputResult.PROCESSED;
    }
}
