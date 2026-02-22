package com.tehbeard.fabric.fastaction.ui;

import com.tehbeard.fabric.fastaction.data.ActionButton;
import com.tehbeard.fabric.fastaction.data.ActionConfig;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class MoveEntry extends ActionEntry {

    //    public static final Identifier TEXTURE_BUTTON = Identifier.of("fastactions", "textures/switcher_buttons.png");
    public static final Identifier TEXTURE_BUTTON = Identifier.of("fastactions", "textures/btn_normal.png");
    public static final Identifier TEXTURE_BUTTON_HOVER = Identifier.of("fastactions", "textures/btn_hover.png");

    public static final Identifier TEXTURE_LEFT_ARROW = Identifier.of("minecraft", "textures/gui/sprites/transferable_list/unselect.png");
    public static final Identifier TEXTURE_RIGHT_ARROW = Identifier.of("minecraft", "textures/gui/sprites/transferable_list/select.png");

    /**
     * TODO: Refactor to accept a left click and right click function.
     *
     * @param data
     * @param onClick
     */
    public MoveEntry(ActionButton data, BiConsumer<Click, Boolean> onClick) {
        super(data, onClick);
    }

    boolean isLeft;


    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {

        super.paint(context, x, y, mouseX, mouseY);

        var isHovered = isWithinBounds(mouseX, mouseY) || getHost().isFocused(this);

        this.isLeft = isHovered && (mouseX < (getWidth() / 2));


        if (isHovered) {

            context.drawTexture(
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
    public InputResult onMouseDown(Click click, boolean doubled) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.ui(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        this.onClick.accept(click, this.isLeft);

        return InputResult.PROCESSED;
    }
}
