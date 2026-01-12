package xyz.imcodist.quickmenu.other;


public class ModConfigModel {
    public enum DisplayRunText {
        ALWAYS, KEYBIND_ONLY, NEVER
    }

    public int menuWidth = 180;
    public int menuHeight = 114;

    public int buttonsPerRow = 5;

    public boolean closeOnKeyReleased = false;

    public boolean hideEditIcon = false;

    public boolean closeOnAction = true;
    public boolean showActionsInTooltip = true;
    public DisplayRunText displayRunText = DisplayRunText.KEYBIND_ONLY;
}
