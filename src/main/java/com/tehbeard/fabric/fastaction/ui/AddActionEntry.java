package com.tehbeard.fabric.fastaction.ui;

import com.tehbeard.fabric.fastaction.data.ActionButton;
import com.tehbeard.fabric.fastaction.data.ActionConfig;
import com.tehbeard.fabric.fastaction.ui.panel.ButtonEditor;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
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

public class AddActionEntry extends WWidget {


    public static final Identifier TEXTURE_ADD_BUTTON = Identifier.fromNamespaceAndPath("fastactions", "textures/btn_plus_normal.png");
    public static final Identifier TEXTURE_ADD_BUTTON_HOVER = Identifier.fromNamespaceAndPath("fastactions", "textures/btn_plus_hover.png");


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
        tooltip.add(Component.literal("Add Action"));
    }

    @Override
    public void paint(GuiGraphicsExtractor context, int x, int y, int mouseX, int mouseY) {

        var isHovered = isWithinBounds(mouseX, mouseY) || getHost().isFocused(this);

        // Doesn't render texture...
        // (RenderPipeline pipeline, Identifier sprite, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight)
        context.blit(
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
    public InputResult onMouseDown(MouseButtonEvent click, boolean doubled) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        var newData = new ActionButton().setName("");
        ActionConfig.getConfig().getDefaultTab().getButtons().add(newData); // TODO - Switch to current tab when multiple tabs added.
        Minecraft.getInstance().gui.setScreen(new MinedeckScreen(new ButtonEditor(
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
