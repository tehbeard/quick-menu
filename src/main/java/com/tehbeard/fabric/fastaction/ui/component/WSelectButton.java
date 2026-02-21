package com.tehbeard.fabric.fastaction.ui.component;

import io.github.cottonmc.cotton.gui.widget.WButton;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class WSelectButton<T> extends WButton {

    protected T value;

    public WSelectButton(
        List<T> options,
        T defaultValue,
        Function<T, Text> labelFn,
        Consumer<T> onSelect
    ) {
        this.value = defaultValue;

        this.setSize(100, 18);

        this.setLabel( labelFn.apply(defaultValue));
        setOnClick(() -> {
            var pos = (options.indexOf(this.value) + 1);
            if(pos == options.size()) { pos = 0; }
            this.value = options.get(pos);
            onSelect.accept(this.value);
            this.setLabel(labelFn.apply(value));
        });
    }

}
