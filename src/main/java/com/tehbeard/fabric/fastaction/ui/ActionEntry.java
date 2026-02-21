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

//    public static final Identifier TEXTURE_BUTTON = Identifier.of("quickmenu", "textures/switcher_buttons.png");
    public static final Identifier TEXTURE_BUTTON = Identifier.of("quickmenu", "textures/btn_normal.png");
    public static final Identifier TEXTURE_BUTTON_HOVER = Identifier.of("quickmenu", "textures/btn_hover.png");

    public static final Identifier TEXTURE_ADD_BUTTON = Identifier.of("quickmenu", "textures/btn_plus_normal.png");
    public static final Identifier TEXTURE_ADD_BUTTON_HOVER = Identifier.of("quickmenu", "textures/btn_plus_hover.png");


    public static final Identifier TEXTURE_LEFT_ARROW = Identifier.of("minecraft","textures/gui/sprites/transferable_list/unselect.png");
    public static final Identifier TEXTURE_RIGHT_ARROW = Identifier.of("minecraft","textures/gui/sprites/transferable_list/select.png");
    private final ActionButton data;

    private final BiConsumer<Click,Boolean> onClick;
    /**
     * TODO: Refactor to accept a left click and right click function.
     */
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
        if(data == null ) {
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
        } else {
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
        }

        if(isHovered ) {
            context.setCursor(StandardCursors.POINTING_HAND);

                context.drawTexture(
                    RenderPipelines.GUI_TEXTURED,
                    isLeft ? TEXTURE_LEFT_ARROW : TEXTURE_RIGHT_ARROW,
                    x+5 + (isLeft ? 0 : 3),
                    y+5,
                    0,0,
                    16,16,
                    16,
                    16
                );
//            ScreenDrawing.drawString(
//                context,
//                isLeft ? "L" : "R",
//                HorizontalAlignment.LEFT,
//                x + 5,
//                y + 5,
//                this.getWidth(),
//                0xFF_BCBCBC
//            );
        }


    }

    @Override
    public InputResult onMouseDown(Click click, boolean doubled) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.ui(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        this.onClick.accept(click, doubled);

        return InputResult.PROCESSED;
    }
}
