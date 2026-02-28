package com.rtc.client.gui;

import eu.midnightdust.lib.config.MidnightConfig;

public class RTHudsConfigScreen extends MidnightConfig {

    public static final String General = "General";
    public static final String Appearance = "Appearance";
    public static final String Color = "Color";
    public static final String Impression = "Impression";

    @Entry(category = General)
    public static boolean showHud = true;
    @Entry(category = General)
    public static boolean ArmorHUD = true;
    @Entry(category = General)
    public static boolean showF1 = true;
    @Entry(category = General)
    public static boolean showDebug = true;

    @Entry(category = Appearance)
    public static Layout layout = Layout.LAYOUT_1;

    public enum Layout {LAYOUT_1, LAYOUT_2,}

    @Entry(category = Appearance)
    public static ArmorHUDLocation ArmorLocation = ArmorHUDLocation.LEFTCENTER;

    public enum ArmorHUDLocation {LEFTUP, LEFTCENTER, LEFTBOTTOM, RIGHTUP, RIGHTCENTER, RIGHTBOTTOM}

    @Entry(category = Appearance)
    public static boolean textShadow = true;
    @Entry(category = Appearance)
    public static boolean ArmorTextShadow = true;

    @Entry(category = Appearance)
    public static ArmorDurabilityEnum ArmorHealthDurability = ArmorDurabilityEnum.REMAINING;

    public enum ArmorDurabilityEnum {NONE, REMAINING, DAMAGE, PERCENTAGE, REMAINGINGandMAX}

    @Entry(category = Appearance)
    public static boolean ArmorDurabilityBar = true;
    @Entry(category = Appearance)
    public static boolean ShowEmpytSlot = true;
    @Entry(category = Appearance)
    public static boolean showHandItems = true;

    @Entry(category = Appearance, isSlider = true, min = 0, max = 6)
    public static int decimalPlaces = 6;
    @Entry(category = Appearance, isSlider = true, min = 0.50f, max = 1.5f)
    public static float ArmorScale = 1.0f;

    @Entry(category = Appearance)
    public static BackgroundStyle backgroundStyle = BackgroundStyle.LIGHT;

    public enum BackgroundStyle {NONE, LIGHT, FULL}

    @Entry(category = Appearance)
    public static ArmorBackgroundStyle armorBackgroundStyle = ArmorBackgroundStyle.LIGHT;

    public enum ArmorBackgroundStyle {NONE, LIGHT, FULL}

    @Entry(category = Appearance, isSlider = true, min = 0, max = 100)
    public static int hudXPercent = 0;
    @Entry(category = Appearance, isSlider = true, min = 0, max = 100)
    public static int hudYPercent = 0;

    @Entry(category = Color, width = 7, min = 7, isColor = true)
    public static String xyzColor = "#ffffff";

    @Entry(category = Color, width = 7, min = 7, isColor = true)
    public static String yawColor = "#ffffff";

    @Entry(category = Color, width = 7, min = 7, isColor = true)
    public static String pitchColor = "#ffffff";

    @Entry(category = Color, width = 7, min = 7, isColor = true)
    public static String directionColor = "#ffffff";

    @Entry(category = Color, width = 7, min = 7, isColor = true)
    public static String fpsColor = "#ffffff";

    @Entry(category = Color, width = 7, min = 7, isColor = true)
    public static String netherCoordColor = "#ffffff";

    @Entry(category = Color, width = 7, min = 7, isColor = true)
    public static String ArmorTextColor = "#ffffff";

    @Entry(category = Impression)
    public static boolean showCoords = true;

    @Entry(category = Impression)
    public static boolean showYawPitch = true;

    @Entry(category = Impression)
    public static boolean showDirection = true;

    @Entry(category = Impression)
    public static boolean showFPS = true;

    @Entry(category = Impression)
    public static boolean toggleNetherCoordinateConversion = true;


}
