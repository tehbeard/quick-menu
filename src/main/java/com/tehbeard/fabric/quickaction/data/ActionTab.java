package com.tehbeard.fabric.quickaction.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ActionTab {

    public static final Codec<ActionTab> CODEC = RecordCodecBuilder.create( inst ->
        inst.group(
            Identifier.CODEC.fieldOf("id").forGetter(ActionTab::getId),
            Codec.STRING.fieldOf("name").forGetter(ActionTab::getName),
            ActionButton.CODEC.listOf().fieldOf("buttons").forGetter(ActionTab::getButtons)
        ).apply(inst,(id,name,buttons) -> {
            var tab = new ActionTab();
            tab.setId(id);
            tab.setName(name);
            tab.setButtons(new ArrayList<>(buttons));
            return tab;
        })
    );

    private Identifier id = ActionConfig.DEFAULT_TAB;
    private String name = "";

    private List<ActionButton> buttons = new ArrayList<>();


    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ActionButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<ActionButton> buttons) {
        this.buttons = buttons;
    }
}
