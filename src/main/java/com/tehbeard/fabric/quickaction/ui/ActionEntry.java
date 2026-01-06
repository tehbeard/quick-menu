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

public class ActionEntry extends WWidget {

//    public static final Identifier TEXTURE_BUTTON = Identifier.of("quickmenu", "textures/switcher_buttons.png");
    public static final Identifier TEXTURE_BUTTON = Identifier.of("quickmenu", "textures/btn_normal.png");
    public static final Identifier TEXTURE_BUTTON_HOVER = Identifier.of("quickmenu", "textures/btn_hover.png");

    private ActionButtonData data;
    public ActionEntry(ActionButtonData data) {
        height = 26;
        width = 26;
        this.data = data;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
            tooltip.add(Text.literal(data.name));
            var kb = data.getKey();
            if(kb != null)
            {
                tooltip.add(Text.literal("Trigger: ").append(kb.getLocalizedText()));
            }
            data.actions.forEach( action -> {
                tooltip.add(action.getText());
            });
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

        context.drawItemWithoutEntity(
            data.icon,
            x+5,y+5
        );

        if(isHovered ) {
            context.setCursor(StandardCursors.POINTING_HAND);
        }

//        context.drawGuiTexture(
//            RenderPipelines.GUI_TEXTURED,
//            TEXTURE_BUTTON,
////            textures.get(enabled, hovered || isFocused()),
//            x,
//            y,
//            getWidth(),
//            getHeight()
//        );
    }

    @Override
    public InputResult onMouseDown(Click click, boolean doubled) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        data.run();
        MinecraftClient.getInstance().setScreen(null);
        return InputResult.PROCESSED;
    }
}
