package com.tehbeard.fabric.fastaction.ui;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

public class IconEntry extends WWidget {

    public static final Identifier TEXTURE_BUTTON = Identifier.fromNamespaceAndPath("fastactions", "textures/btn_normal.png");
    public static final Identifier TEXTURE_BUTTON_HOVER = Identifier.fromNamespaceAndPath("fastactions", "textures/btn_hover.png");


    private ItemStack icon;
    private final Consumer<IconEntry> onClick;
    public IconEntry(ItemStack icon, Consumer<IconEntry> onClick) {
        height = 26;
        width = 26;
        this.icon = icon;
        this.onClick = onClick;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        if(icon != null)
        {
            tooltip.add(icon.getItemName());
        } else {
            tooltip.add(Component.literal("No Item Selected."));
        }
    }

    public void setIcon(ItemStack icon)
    {
        this.icon = icon;
    }

    public ItemStack getIcon() {
        return icon;
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
        if(icon != null) {
            context.fakeItem(
                icon,
                x + 5, y + 5
            );
        }

        if( isHovered ) {
            context.requestCursor(CursorTypes.POINTING_HAND);
        }

    }

    @Override
    public InputResult onMouseDown(MouseButtonEvent click, boolean doubled) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        onClick.accept(this);
        return InputResult.PROCESSED;
    }
}
