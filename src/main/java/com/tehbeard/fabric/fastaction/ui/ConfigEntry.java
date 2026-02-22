package com.tehbeard.fabric.fastaction.ui;

import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.ui.panel.ConfigMenu;
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
import com.tehbeard.fabric.fastaction.FastAction;

import java.io.IOException;

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
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.ui(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        MinecraftClient.getInstance()
            .setScreen(
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
