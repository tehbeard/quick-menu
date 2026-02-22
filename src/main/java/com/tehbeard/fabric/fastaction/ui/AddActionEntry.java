package com.tehbeard.fabric.fastaction.ui;

import com.tehbeard.fabric.fastaction.data.ActionButton;
import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.ui.panel.ButtonEditor;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import com.tehbeard.fabric.fastaction.FastAction;

import java.io.IOException;

public class AddActionEntry extends WWidget {


    public static final Identifier TEXTURE_ADD_BUTTON = Identifier.of("quickmenu", "textures/btn_plus_normal.png");
    public static final Identifier TEXTURE_ADD_BUTTON_HOVER = Identifier.of("quickmenu", "textures/btn_plus_hover.png");


    /**
     * TODO: Refactor to accept a left click and right click function.
     */
    public AddActionEntry() {
        height = 26;
        width = 26;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        tooltip.add(Text.literal("Add Action"));
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {

        var isHovered = isWithinBounds(mouseX, mouseY) || getHost().isFocused(this);

        // Doesn't render texture...
        // (RenderPipeline pipeline, Identifier sprite, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight)
        context.drawTexture(
            RenderPipelines.GUI_TEXTURED,
            isHovered ? TEXTURE_ADD_BUTTON_HOVER : TEXTURE_ADD_BUTTON,
            x,
            y,
            0,
            0,
            getWidth(),
            getHeight(),
            26,
            26
        );

    }

    @Override
    public InputResult onMouseDown(Click click, boolean doubled) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.ui(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        var newData = new ActionButton().setName("");
        ActionConfig.getConfig().getDefaultTab().getButtons().add(newData); // TODO - Switch to current tab when multiple tabs added.
        MinecraftClient.getInstance().setScreen(new MinedeckScreen(new ButtonEditor(
            newData
        )).onRemoved(() -> {
            try {
                ActionConfig.getConfig().save(FastAction.getConfigFile());
            } catch (IOException e) {
                FastAction.LOGGER.error(e.toString());
            }
        }));

        return InputResult.PROCESSED;
    }
}
