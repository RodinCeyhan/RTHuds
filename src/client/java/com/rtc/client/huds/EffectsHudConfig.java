package com.rtc.client.huds;

public final class EffectsHudConfig {
    private EffectsHudConfig() {
    }

    public static final int DEFAULT_POS = -1;

    public enum Orientation {HORIZONTAL, VERTICAL}

    public enum SortMode {DEFAULT, DURATION_ASC, DURATION_DESC, NAME}

    public static final int DEFAULT_PANEL_COLOR = 0x66000000;

    public static boolean enabled = true;
    public static boolean hideVanilla = true;

    public static Orientation orientation = Orientation.VERTICAL;
    public static SortMode sortMode = SortMode.DEFAULT;

    public static boolean showPanel = true;
    public static int panelColor = DEFAULT_PANEL_COLOR;

    public static boolean showIcon = true;
    public static boolean showName = true;
    public static boolean showDuration = true;
    public static boolean showBar = true;

    public static int textColor = 0xFFFFFFFF;
    public static boolean textChroma = false;
    public static boolean textShadow = true;
    public static boolean dynamicColor = false;

    public static boolean hideAmbient = false;
    public static boolean showBeneficial = true;
    public static boolean showHarmful = true;

    public static float scale = 1.0f;
    public static int x = DEFAULT_POS;
    public static int y = DEFAULT_POS;

    public static void setScale(float newScale) {
        scale = HudElement.clampScale(newScale);
    }

    public static void reset() {
        hideVanilla = true;
        orientation = Orientation.VERTICAL;
        sortMode = SortMode.DEFAULT;
        showPanel = true;
        panelColor = DEFAULT_PANEL_COLOR;
        showIcon = true;
        showName = true;
        showDuration = true;
        showBar = true;
        textColor = 0xFFFFFFFF;
        textChroma = false;
        textShadow = true;
        dynamicColor = false;
        hideAmbient = false;
        showBeneficial = true;
        showHarmful = true;
        scale = 1.0f;
        x = DEFAULT_POS;
        y = DEFAULT_POS;
    }
}
