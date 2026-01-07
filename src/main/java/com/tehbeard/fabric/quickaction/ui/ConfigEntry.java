package com.tehbeard.fabric.quickaction.ui;

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
import xyz.imcodist.quickmenu.data.ActionButtonData;

public class ConfigEntry extends WWidget {

//    public static final Identifier TEXTURE_BUTTON = Identifier.of("quickmenu", "textures/switcher_buttons.png");
    public static final Identifier TEXTURE_BUTTON = Identifier.of("quickmenu", "textures/btn_normal.png");
    public static final Identifier TEXTURE_BUTTON_HOVER = Identifier.of("quickmenu", "textures/btn_hover.png");

    public static final Identifier TEXTURE_WRENCH = Identifier.of("quickmenu", "textures/wrench.png");

    public ConfigEntry() {
        height = 26;
        width = 26;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        tooltip.add(Text.literal("Config"));
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {

        var isHovered = isWithinBounds(mouseX,mouseY) || getHost().isFocused(this);
        // Doesn't render texture...
        // (RenderPipeline pipeline, Identifier sprite, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight)
            context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                isHovered ? TEXTURE_BUTTON_HOVER : TEXTURE_BUTTON,
                x,
                y,
                0,
                0,
                getWidth(),
                getHeight(),
                26,
                26
            );
            context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                TEXTURE_WRENCH,
                x+5,
                y+5,
                0,
                0,
                16,
                16,
                16,
                16
            );

        if(isHovered ) {
            context.setCursor(StandardCursors.POINTING_HAND);
        }

    }

    @Override
    public InputResult onMouseDown(Click click, boolean doubled) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

        // TODO - Load config screen
        return InputResult.PROCESSED;
    }
}
