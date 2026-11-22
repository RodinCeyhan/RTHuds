package com.rtc.client.huds;

public class HudConfig {
    public static boolean infoHud = true;
    public static boolean showHideGui = true;
    public static boolean showDebugGui = false;

    public static boolean showCoordinates = true;
    public static boolean showYaw = true;
    public static boolean showPitch = true;
    public static boolean showDirection = true;
    public static boolean showFPS = true;
    public static boolean showCoordinatesConverter = true;
    public static boolean showCPS = true;
    public static boolean showBiome = true;

    public static boolean showBiome() {return showBiome;}
    public static boolean InfoHud() {return infoHud;}
    public static boolean HideGui() {return showHideGui;}
    public static boolean DebugGui() {return showDebugGui;}
    public static boolean showCoordinates() {return showCoordinates;}
    public static boolean showYaw() {return showYaw;}
    public static boolean showPitch() {return showPitch;}
    public static boolean showDirection() {return showDirection;}
    public static boolean showFPS() {return showFPS;}
    public static boolean showCoordinatesConverter() {return showCoordinatesConverter;}
    public static boolean showCPS() {return showCPS;}
}