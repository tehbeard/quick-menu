package com.tehbeard.fabric.fastaction.ui;

import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class IconEntry extends WWidget {

    public static final Identifier TEXTURE_BUTTON = Identifier.of("fastactions", "textures/btn_normal.png");
    public static final Identifier TEXTURE_BUTTON_HOVER = Identifier.of("fastactions", "textures/btn_hover.png");


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
            tooltip.add(Text.literal("No Item Selected."));
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
        if(icon != null) {
            context.drawItemWithoutEntity(
                icon,
                x + 5, y + 5
            );
        }

        if( isHovered ) {
            context.setCursor(StandardCursors.POINTING_HAND);
        }

    }

    @Override
    public InputResult onMouseDown(Click click, boolean doubled) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.ui(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        onClick.accept(this);
        return InputResult.PROCESSED;
    }
}
