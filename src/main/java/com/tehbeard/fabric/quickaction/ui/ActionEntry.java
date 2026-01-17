package com.tehbeard.fabric.quickaction.ui;

import com.tehbeard.fabric.quickaction.data.ActionButton;
import com.tehbeard.fabric.quickaction.data.ActionConfig;
import com.tehbeard.fabric.quickaction.ui.panel.ButtonEditor;
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
import xyz.imcodist.quickmenu.QuickMenu;

import java.io.IOException;

public class ActionEntry extends WWidget {

//    public static final Identifier TEXTURE_BUTTON = Identifier.of("quickmenu", "textures/switcher_buttons.png");
    public static final Identifier TEXTURE_BUTTON = Identifier.of("quickmenu", "textures/btn_normal.png");
    public static final Identifier TEXTURE_BUTTON_HOVER = Identifier.of("quickmenu", "textures/btn_hover.png");

    public static final Identifier TEXTURE_ADD_BUTTON = Identifier.of("quickmenu", "textures/btn_plus_normal.png");
    public static final Identifier TEXTURE_ADD_BUTTON_HOVER = Identifier.of("quickmenu", "textures/btn_plus_hover.png");

    private ActionButton data;
    private boolean isEditMode;
    public ActionEntry(ActionButton data, boolean isEditMode) {
        height = 26;
        width = 26;
        this.data = data;
        this.isEditMode = isEditMode;
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
            data.getTasks().forEach( a -> tooltip.add(a.description()));
        } else {
            tooltip.add(Text.literal("Add Action"));
        }
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {

        var isHovered = isWithinBounds(mouseX,mouseY) || getHost().isFocused(this);
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
        }

    }

    @Override
    public InputResult onMouseDown(Click click, boolean doubled) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.ui(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        if(data != null) {
            if(!isEditMode) {
                MinecraftClient.getInstance().setScreen(null);
                data.run(false);
            } else {
                // TODO - Open edit mode
                MinecraftClient.getInstance().setScreen(
                    new MinedeckScreen(
                        new ButtonEditor(
                            data
                        )
                    ).onRemoved(() -> {
                        try {
                            ActionConfig.getConfig().save(QuickMenu.getConfigFile());
                        } catch (IOException e) {
                            QuickMenu.LOGGER.error(e.toString());
//                        throw new RuntimeException(e);
                        }
                    })
                );
            }
        } else {
            var newData = new ActionButton().setName("");
            ActionConfig.getConfig().getDefaultTab().getButtons().add(newData);
            MinecraftClient.getInstance().setScreen(new MinedeckScreen(new ButtonEditor(
                newData
            )).onRemoved(() -> {
                try {
                    ActionConfig.getConfig().save(QuickMenu.getConfigFile());
                } catch (IOException e) {
                    QuickMenu.LOGGER.error(e.toString());
//                        throw new RuntimeException(e);
                }
            }));


        }
        return InputResult.PROCESSED;
    }
}
