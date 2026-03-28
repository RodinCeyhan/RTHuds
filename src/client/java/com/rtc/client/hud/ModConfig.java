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
    public int decimalPlaces = 6;
    public boolean toggleNetherCoordinateConversion = true;
    public enum BackgroundStyle {NONE, LIGHT, FULL}
    public BackgroundStyle backgroundStyle = BackgroundStyle.LIGHT;
    public enum Layout {LAYOUT_1, LAYOUT_2,}
    public Layout layout = Layout.LAYOUT_1;
    public boolean ArmorHUD = true;
    public boolean ArmorDurabilityBar = true;
    public boolean ShowEmpytSlot = true;
    public enum ArmorHUDLocation {LEFTUP, LEFTCENTER, LEFTBOTTOM, RIGHTUP, RIGHTCENTER, RIGHTBOTTOM}
    public ArmorHUDLocation ArmorLocation = ArmorHUDLocation.LEFTCENTER;
    public float ArmorScale = 1.0f;
    public enum ArmorDurabilityEnum {NONE, REMAINING, DAMAGE, PERCENTAGE, REMAINGINGandMAX}
    public ArmorDurabilityEnum ArmorHealthDurability = ArmorDurabilityEnum.REMAINING;
    public boolean ArmorTextShadow = true;
    public boolean showHandItems = true;
    public int hudXPercent = 0;
    public int hudYPercent = 0;
    public enum ArmorBackgroundStyle {NONE, LIGHT, FULL}
    public ArmorBackgroundStyle armorBackgroundStyle = ArmorBackgroundStyle.NONE;
}
