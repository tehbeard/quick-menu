package com.tehbeard.fabric.fastaction.ui;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.tehbeard.fabric.fastaction.data.ActionButton;
import com.tehbeard.fabric.fastaction.data.ActionConfig;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

public class ActionEntry extends WWidget {

    public static final Identifier TEXTURE_BUTTON = Identifier.fromNamespaceAndPath("fastactions", "textures/btn_normal.png");
    public static final Identifier TEXTURE_BUTTON_HOVER = Identifier.fromNamespaceAndPath("fastactions", "textures/btn_hover.png");

    protected final ActionButton data;

    protected final BiConsumer<MouseButtonEvent,Boolean> onClick;

    private boolean hideTooltip = false;

    public ActionEntry(ActionButton data, BiConsumer<MouseButtonEvent,Boolean> onClick) {
        height = 26;
        width = 26;
        this.data = data;
        this.onClick = onClick;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    public ActionEntry hideTooltip()
    {
        hideTooltip = true;
        return this;
    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        if(data !=null) {
            tooltip.add(Component.literal(data.getName()));
            var kb = data.getKeybind();
            if (kb != null) {
                tooltip.add(Component.literal("Trigger: ").append(kb.getDisplayName()));
            }
            if(ActionConfig.getConfig().isActionsInTooltip()) {
                data.getTasks().forEach(a -> tooltip.add(a.description()));
            }
        } else {
            tooltip.add(Component.literal("Add Action"));
        }
    }

    @Override
    public void paint(GuiGraphics context, int x, int y, int mouseX, int mouseY) {

        var isHovered = isWithinBounds(mouseX,mouseY) || getHost().isFocused(this);

        var isLeft = isHovered && ( mouseX < (getWidth() /2));
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
            context.renderFakeItem(
                data.getIcon(),
                x + 5, y + 5
            );

        if(isHovered ) {
            context.requestCursor(CursorTypes.POINTING_HAND);
        }


    }

    @Override
    public InputResult onMouseDown(MouseButtonEvent click, boolean doubled) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        this.onClick.accept(click, doubled);

        return InputResult.PROCESSED;
    }
}
