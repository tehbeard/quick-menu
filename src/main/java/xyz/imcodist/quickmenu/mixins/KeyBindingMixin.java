package xyz.imcodist.quickmenu.mixins;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyMapping.class)
public interface KeyBindingMixin {
    @Accessor("clickCount")
    void setTimesPressed(int timesPressed);
}
