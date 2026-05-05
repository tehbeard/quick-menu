package com.tehbeard.fabric.fastaction.ui;

import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.ui.panel.ConfigMenu;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.tehbeard.fabric.fastaction.FastAction;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

public class ConfigEntry extends WWidget {

    public static final Identifier TEXTURE_BUTTON = Identifier.fromNamespaceAndPath("fastactions", "textures/btn_normal.png");
    public static final Identifier TEXTURE_BUTTON_HOVER = Identifier.fromNamespaceAndPath("fastactions", "textures/btn_hover.png");

    public static final Identifier TEXTURE_WRENCH = Identifier.fromNamespaceAndPath("fastactions", "textures/wrench.png");

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
        tooltip.add(Component.literal("Config"));
    }


    @Override
    public void paint(GuiGraphicsExtractor context, int x, int y, int mouseX, int mouseY) {

        var isHovered = isWithinBounds(mouseX,mouseY) || getHost().isFocused(this);
        // Doesn't render texture...
        // (RenderPipeline pipeline, Identifier sprite, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight)
            context.blit(
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
            context.blit(
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
            context.requestCursor(CursorTypes.POINTING_HAND);
        }

    }

    @Override
    public InputResult onMouseDown(MouseButtonEvent click, boolean doubled) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        Minecraft.getInstance()
            .setScreenAndShow(
                new MinedeckScreen(new ConfigMenu())
                    .onRemoved(() -> {
                        try {
                            ActionConfig.getConfig().save(FastAction.getConfigFile());
                            // TODO - Switch back to menu screen, can't do so direct from here though.
                        } catch (IOException e) {
                            FastAction.LOGGER.error(e.toString());
                        }
                    })
            );
        return InputResult.PROCESSED;
    }
}
