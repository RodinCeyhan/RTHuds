package com.rtc.client.huds;

public final class ArmorHudConfig {
    private ArmorHudConfig() {
    }

    public static final int DEFAULT_POS = -1;

    public static boolean enabled = true;

    public static boolean showHelmet = true;
    public static boolean showChest = true;
    public static boolean showLegs = true;
    public static boolean showBoots = true;
    public static boolean showMainHand = true;
    public static boolean showOffHand = true;

    public static boolean showCount = true;
    public static boolean showDurabilityBar = true;
    public static boolean whiteEmptyIcons = false;

    public static boolean showEmptySlots = true;

    public static final int DEFAULT_PANEL_COLOR = 0x66000000;
    public static boolean showPanel = false;
    public static int panelColor = DEFAULT_PANEL_COLOR;

    public enum HealthMode {REMAINING, DAMAGE, PERCENTAGE, REMAINING_MAX}

    public static HealthMode healthMode = HealthMode.REMAINING;

    public enum HealthPos {NONE, TOP, BOTTOM, LEFT, RIGHT}

    public static HealthPos healthPos = HealthPos.BOTTOM;

    public enum Orientation {HORIZONTAL, VERTICAL}

    public static Orientation orientation = Orientation.VERTICAL;

    public enum BgStyle {NONE, GLASS, NEON, HOTBAR}

    public static BgStyle bgStyle = BgStyle.GLASS;

    public static float scale = 1.0f;

    public static void setScale(float newScale) {
        scale = HudElement.clampScale(newScale);
    }

    public static int x = DEFAULT_POS;
    public static int y = DEFAULT_POS;
    public static boolean textShadow = true;
    public static int textColor = 0xFFFFFFFF;
    public static boolean textChroma = false;

    public static boolean dynamicColor = false;

    public static boolean hotbarSnapped = false;

    public enum WarnSound {
        NONE(null),
        PLING("block.note_block.pling"),
        BELL("block.note_block.bell"),
        BASS("block.note_block.bass"),
        ITEM_BREAK("entity.item.break"),
        ANVIL("block.anvil.land"),
        XP_ORB("entity.experience_orb.pickup");

        public final String soundPath;

        WarnSound(String soundPath) {
            this.soundPath = soundPath;
        }
    }

    public static final int DEFAULT_WARN_PERCENT = 20;
    public static int warnPercent = DEFAULT_WARN_PERCENT;
    public static WarnSound warnSound = WarnSound.PLING;

    public static void reset() {
        showHelmet = showChest = showLegs = showBoots = showMainHand = showOffHand = true;
        showCount = true;
        showDurabilityBar = true;
        whiteEmptyIcons = false;
        showEmptySlots = true;
        showPanel = false;
        panelColor = DEFAULT_PANEL_COLOR;
        healthMode = HealthMode.REMAINING;
        healthPos = HealthPos.BOTTOM;
        orientation = Orientation.VERTICAL;
        bgStyle = BgStyle.GLASS;
        scale = 1.0f;
        x = DEFAULT_POS;
        y = DEFAULT_POS;
        textShadow = true;
        textColor = 0xFFFFFFFF;
        textChroma = false;
        dynamicColor = false;
        hotbarSnapped = false;
        warnPercent = DEFAULT_WARN_PERCENT;
        warnSound = WarnSound.PLING;
    }
}
