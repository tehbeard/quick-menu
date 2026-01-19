package com.tehbeard.fabric.quickaction.ui.component;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

// TODO - Add methods to set widgets left/right of header.
public class PanelWithHeader extends WPixelPanel {

    public PanelWithHeader(String heading, int width, int height, boolean darker) {
        this.setSize(width, height);
        this.setInsets(Insets.NONE);

        this.setBackgroundPainter(BackgroundPainter.createNinePatch(
            new Texture(Identifier.of("quickmenu", darker ? "textures/background_darker.png" : "textures/background.png")),
            builder -> builder.cornerSize(8).cornerUv(0.33f)
        ));

        WPixelPanel header = new WPixelPanel();
        header.setInsets(Insets.NONE);
        header.setBackgroundPainter(BackgroundPainter.createNinePatch(
            new Texture(Identifier.of("quickmenu", "textures/background_header.png")),
            builder -> builder.cornerSize(8).cornerUv(0.33f)
        ));
        header.setSize(this.getWidth(),24);
        this.add(header, 0,0);

        WLabel label = new WLabel(Text.literal(heading), 0xFF_FFFFFF);
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        label.setVerticalAlignment(VerticalAlignment.CENTER);

        header.add(label, (this.getWidth() / 2) - 9,3);
    }


}
