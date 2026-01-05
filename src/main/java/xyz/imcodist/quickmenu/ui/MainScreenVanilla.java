package xyz.imcodist.quickmenu.ui;


import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

import java.time.Instant;

public class MainScreenVanilla extends Screen {

    public MainScreenVanilla() {
        super(Text.literal("Test Screen"));
    }

    protected MainScreenVanilla(Text title) {
        super(title);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void applyBlur(DrawContext context) {
//        super.applyBlur(context);
    }

    @Override
    protected void renderDarkening(DrawContext context) {
//        super.renderDarkening(context);
    }

    @Override
    protected void init() {
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.literal("Hello World"), (btn) -> {
            // When the button is clicked, we can display a toast to the screen.
            this.client.getToastManager().add(
                SystemToast.create(this.client,
                    SystemToast.Type.NARRATOR_TOGGLE,
                    Text.literal("Hello World!"),
//                    Text.literal("This is a toast.")
                    Text.literal(Instant.now().toString())
                )
            );
        }).dimensions(40, 40, 120, 20).build();
        // x, y, width, height
        // It's recommended to use the fixed height of 20 to prevent rendering issues with the button
        // textures.

        // Register the button widget.
        this.addDrawableChild(buttonWidget);

//        ScrollableLayoutWidget layoutWidget = new ScrollableLayoutWidget();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
//        context.drawText(
//            this.textRenderer,
//            "Special Button",
//            40, 40 /* - this.font.lineHeight - 10 */, 0xFFFFFFFF, true);
    }

    //    @Override
//    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
//        super.render(context, mouseX, mouseY, delta);
//
//        // Minecraft doesn't have a "label" widget, so we'll have to draw our own text.
//        // We'll subtract the font height from the Y position to make the text appear above the button.
//        // Subtracting an extra 10 pixels will give the text some padding.
//        // textRenderer, text, x, y, color, hasShadow
//        context.drawString(this.font, "Special Button", 40, 40 - this.font.lineHeight - 10, 0xFFFFFFFF, true);
//    }
}
