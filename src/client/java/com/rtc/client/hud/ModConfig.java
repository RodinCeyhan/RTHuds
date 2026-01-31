package com.rtc.client.hud;

public class ModConfig {
    public boolean textShadow = true;
    public boolean showF1 = false;
    public boolean showDebug = false;
    public boolean showCoords = true;
    public boolean showYawPitch = true;
    public boolean showDirection = true;
    public boolean showFPS = true;
    public boolean showHud = true;
    public int decimalPlaces = 3;
    public boolean toggleNetherCoordinateConversion = true;
    public enum BackgroundStyle {NONE, LIGHT, FULL}
    public BackgroundStyle backgroundStyle = BackgroundStyle.NONE;
    public enum Layout {LAYOUT_1, LAYOUT_2,}
    public Layout layout = Layout.LAYOUT_1;
    public enum HudColor {
        YELLOW(0xFFFF55),
        WHITE(0xFFFFFF),
        GREEN(0x55FF55),
        RED(0xFF5555),
        BLUE(0x5555FF),
        DARK_BLUE(0x0000AA),
        CYAN(0x55FFFF),
        ORANGE(0xFFAA00),
        PURPLE(0xAA00AA),
        PINK(0xFF55FF);
        public final int color;
        HudColor(int color) {
            this.color = color;
        }
    }
    public HudColor xyzColor = HudColor.WHITE;
    public HudColor yawColor = HudColor.WHITE;
    public HudColor pitchColor = HudColor.WHITE;
    public HudColor directionColor = HudColor.WHITE;
    public HudColor fpsColor = HudColor.WHITE;
    public HudColor netherCoordColor = HudColor.WHITE;
    public enum HUDLocation {LEFTUP, LEFTDOWN, RIGHTUP, RIGHTDOWN, CENTERUP}
    public HUDLocation ScreenLocation = HUDLocation.LEFTUP;
    public boolean ArmorHUD = true;
    public boolean ArmorDurabilityBar = true;
    public boolean ShowEmpytSlot = true;
    public enum ArmorHUDLocation {LEFTUP, LEFTCENTER, LEFTBOTTOM, RIGHTUP, RIGHTCENTER, RIGHTBOTTOM}
    public ArmorHUDLocation ArmorLocation = ArmorHUDLocation.LEFTCENTER;
    public float ArmorScale = 1.0f;
    public enum ArmorDurabilityEnum {NONE, REMAINING, DAMAGE, PERCENTAGE, REMAINGINGandMAX}
    public ArmorDurabilityEnum ArmorHealthDurability = ArmorDurabilityEnum.REMAINING;
    public HudColor ArmorTextColor = HudColor.WHITE;
    public boolean ArmorTextShadow = true;
    public boolean showHandItems = true;
}