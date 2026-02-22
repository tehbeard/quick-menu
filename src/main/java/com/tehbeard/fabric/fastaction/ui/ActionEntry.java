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

public class ActionEntry extends WWidget {

    public static final Identifier TEXTURE_BUTTON = Identifier.of("fastactions", "textures/btn_normal.png");
    public static final Identifier TEXTURE_BUTTON_HOVER = Identifier.of("fastactions", "textures/btn_hover.png");

    protected final ActionButton data;

    protected final BiConsumer<Click,Boolean> onClick;

    public ActionEntry(ActionButton data, BiConsumer<Click,Boolean> onClick) {
        height = 26;
        width = 26;
        this.data = data;
        this.onClick = onClick;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        if(data !=null) {
            tooltip.add(Text.literal(data.getName()));
            var kb = data.getKeybind();
            if (kb != null) {
                tooltip.add(Text.literal("Trigger: ").append(kb.getLocalizedText()));
            }
            if(ActionConfig.getConfig().isActionsInTooltip()) {
                data.getTasks().forEach(a -> tooltip.add(a.description()));
            }
        } else {
            tooltip.add(Text.literal("Add Action"));
        }
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {

        var isHovered = isWithinBounds(mouseX,mouseY) || getHost().isFocused(this);

        var isLeft = isHovered && ( mouseX < (getWidth() /2));
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
            context.drawItemWithoutEntity(
                data.getIcon(),
                x + 5, y + 5
            );

        if(isHovered ) {
            context.setCursor(StandardCursors.POINTING_HAND);
        }


    }

    @Override
    public InputResult onMouseDown(Click click, boolean doubled) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.ui(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        this.onClick.accept(click, doubled);

        return InputResult.PROCESSED;
    }
}
