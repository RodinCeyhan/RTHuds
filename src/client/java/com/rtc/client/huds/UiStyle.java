package com.rtc.client.huds;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

@SuppressWarnings({"MathClampMigration", "SameParameterValue"})
public final class UiStyle {
    private UiStyle() {
    }

    public static final int PADDING = 14;
    public static final int ROW_GAP = 4;

    public static int ACCENT = 0xFF4C8DFF;
    public static boolean ACCENT_CHROMA = false;

    public static final int ROW_H_SMALL = 22;
    public static final int TOGGLE_W_SMALL = 28;
    public static final int TOGGLE_H_SMALL = 14;
    public static final int ICON_BTN_SMALL = 16;
    public static final int FONT_H = 9;

    public enum Theme {
        DARK(0xF0141414, 0x1AFFFFFF, 0x33FFFFFF, 0xFF3A3A3A, 0xFFE8E8E8, 0xFFAAAAAA,
                0xFF1E1E1E, 0xFF2A2A2A, 0xFF202020, 0xFF2E2E2E, 0xFF1A1A1A, 0xCC101010, 0x99000000, false),
        LIGHT(0xF0F2F2F2, 0x1A000000, 0x33000000, 0xFFB4B4B4, 0xFF1E1E1E, 0xFF5A5A5A,
                0xFFE2E2E2, 0xFFC4C4C4, 0xFFD6D6D6, 0xFFD8D8D8, 0xFFE8E8E8, 0xCCF2F2F2, 0x55000000, false),
        GLASS(0x88202830, 0x22FFFFFF, 0x44FFFFFF, 0x88FFFFFF, 0xFFFFFFFF, 0xFFD0D0D0,
                0x55000000, 0x55000000, 0x55000000, 0x55000000, 0x44FFFFFF, 0x88000000, 0x40000000, false),
        FROSTED(0xB0DCE6F2, 0x33FFFFFF, 0x66FFFFFF, 0xAAFFFFFF, 0xFF1A2430, 0xFF485868,
                0x66FFFFFF, 0x55AAB8C8, 0x66FFFFFF, 0x55FFFFFF, 0x88FFFFFF, 0xAAE8EEF5, 0x50000000, false),
        LIQUID_GLASS(0x66405070, 0x30FFFFFF, 0x55FFFFFF, 0xCCFFFFFF, 0xFFFFFFFF, 0xFFD8E4F4,
                0x44FFFFFF, 0x33FFFFFF, 0x33FFFFFF, 0x33FFFFFF, 0x66FFFFFF, 0x66203050, 0x30000000, true),
        MIDNIGHT(0xF00B1020, 0x1A8FA8FF, 0x338FA8FF, 0xFF26324F, 0xFFDDE6FF, 0xFF8A9AC0,
                0xFF141B30, 0xFF1E2744, 0xFF10162A, 0xFF182038, 0xFF0A0F1E, 0xCC070B18, 0x99000000, false),
        OCEAN(0xF0082A33, 0x1A7FE0FF, 0x337FE0FF, 0xFF1D5566, 0xFFDDF6FF, 0xFF8AB8C8,
                0xFF0E3A46, 0xFF165060, 0xFF0A2E38, 0xFF0F3C48, 0xFF062028, 0xCC051C24, 0x99000000, false),
        FOREST(0xF0102418, 0x1A8FFFA0, 0x338FFFA0, 0xFF2E5A3A, 0xFFE0FFE8, 0xFF90B89A,
                0xFF173424, 0xFF214A30, 0xFF132C1E, 0xFF1B3A28, 0xFF0C1C12, 0xCC09160E, 0x99000000, false),
        SUNSET(0xF02A1420, 0x1AFFB080, 0x33FFB080, 0xFF6A3A3A, 0xFFFFEADD, 0xFFC8A090,
                0xFF3A1E28, 0xFF52303A, 0xFF301822, 0xFF40222C, 0xFF200E16, 0xCC1C0C12, 0x99000000, false),
        AMOLED(0xFF000000, 0x1AFFFFFF, 0x33FFFFFF, 0xFF2A2A2A, 0xFFF0F0F0, 0xFF9A9A9A,
                0xFF0C0C0C, 0xFF1A1A1A, 0xFF101010, 0xFF141414, 0xFF000000, 0xCC000000, 0xCC000000, false);

        final int panelBg, rowBg, rowBgHover, border, text, textDim;
        final int widgetBg, widgetOff, trackBg, previewBg, previewFrame, labelBg, dim;
        final boolean highlight;

        Theme(int panelBg, int rowBg, int rowBgHover, int border, int text, int textDim,
              int widgetBg, int widgetOff, int trackBg, int previewBg, int previewFrame, int labelBg, int dim,
              boolean highlight) {
            this.panelBg = panelBg;
            this.rowBg = rowBg;
            this.rowBgHover = rowBgHover;
            this.border = border;
            this.text = text;
            this.textDim = textDim;
            this.widgetBg = widgetBg;
            this.widgetOff = widgetOff;
            this.trackBg = trackBg;
            this.previewBg = previewBg;
            this.previewFrame = previewFrame;
            this.labelBg = labelBg;
            this.dim = dim;
            this.highlight = highlight;
        }
    }

    public static Theme theme = Theme.DARK;

    public static int PANEL_BG, ROW_BG, ROW_BG_HOVER, BORDER, TEXT, TEXT_DIM;
    public static int WIDGET_BG, WIDGET_OFF, TRACK_BG, PREVIEW_BG, PREVIEW_FRAME, LABEL_BG, DIM;

    static {
        setTheme(Theme.DARK);
    }

    public static void setTheme(Theme t) {
        theme = t;
        PANEL_BG = t.panelBg;
        ROW_BG = t.rowBg;
        ROW_BG_HOVER = t.rowBgHover;
        BORDER = t.border;
        TEXT = t.text;
        TEXT_DIM = t.textDim;
        WIDGET_BG = t.widgetBg;
        WIDGET_OFF = t.widgetOff;
        TRACK_BG = t.trackBg;
        PREVIEW_BG = t.previewBg;
        PREVIEW_FRAME = t.previewFrame;
        LABEL_BG = t.labelBg;
        DIM = t.dim;
    }

    private static final Component CHROMA_LABEL = Component.translatable("rthuds.ui.chroma");

    public static int currentAccent() {
        return ACCENT_CHROMA ? ChromaUtil.color() : ACCENT;
    }

    public static int accentDim() {
        return darken(currentAccent(), 0.55f);
    }

    private static int darken(int argb, float factor) {
        int a = (argb >>> 24) & 0xFF;
        int r = (int) (((argb >> 16) & 0xFF) * factor);
        int g = (int) (((argb >> 8) & 0xFF) * factor);
        int b = (int) ((argb & 0xFF) * factor);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int centerOffset(int containerH, int contentH) {
        return Math.max(0, (containerH - contentH) / 2);
    }

    public static void drawRoundedBox(HudCanvas c, int x, int y, int w, int h, int fillColor, int cutBg) {
        c.rect(x, y, x + w, y + h, fillColor);
        c.rect(x, y, x + 1, y + 1, cutBg);
        c.rect(x + w - 1, y, x + w, y + 1, cutBg);
        c.rect(x, y + h - 1, x + 1, y + h, cutBg);
        c.rect(x + w - 1, y + h - 1, x + w, y + h, cutBg);
    }

    public static void drawPanelFrame(HudCanvas c, int x, int y, int w, int h) {
        c.rect(x, y, x + w, y + h, PANEL_BG);
        c.border(x, y, w, h, currentAccent());
        c.border(x + 1, y + 1, w - 2, h - 2, 0x33000000);
        c.rect(x + PADDING, y + 24, x + w - PADDING, y + 25, BORDER);
        if (theme.highlight) {
            c.rect(x + 2, y + 2, x + w - 2, y + 4, 0x55FFFFFF);
            c.rect(x + 2, y + 4, x + w - 2, y + 8, 0x22FFFFFF);
        }
    }

    public static void drawRow(HudCanvas c, int x, int y, int w, int h, boolean hover) {
        drawRoundedBox(c, x, y, w, h, hover ? ROW_BG_HOVER : ROW_BG, PANEL_BG);
        c.border(x, y, w, h, BORDER);
    }

    public static void drawToggle(HudCanvas c, int x, int y, int w, int h, boolean on, boolean hover) {
        int bg = on ? currentAccent() : WIDGET_OFF;
        drawRoundedBox(c, x, y, w, h, bg, PANEL_BG);
        c.border(x, y, w, h, hover ? 0xFFFFFFFF : BORDER);
        int knobSize = h - 4;
        int knobX = on ? x + w - knobSize - 2 : x + 2;
        drawRoundedBox(c, knobX, y + 2, knobSize, knobSize, 0xFFFFFFFF, bg);
        c.border(knobX, y + 2, knobSize, knobSize, 0xFF000000);
    }

    public static void drawIconButtonFrame(HudCanvas c, int x, int y, int size, boolean hover) {
        drawRoundedBox(c, x, y, size, size, hover ? accentDim() : WIDGET_BG, PANEL_BG);
        c.border(x, y, size, size, hover ? currentAccent() : BORDER);
    }

    public static void drawColorSlider(HudCanvas c, int trackX, int y, int width, int height, int value, int fillColor) {
        c.rect(trackX, y, trackX + width, y + height, TRACK_BG);
        c.border(trackX, y, width, height, BORDER);
        int fillW = Math.round(width * (value / 255F));
        if (fillW > 0) c.rect(trackX, y, trackX + fillW, y + height, fillColor);

        int knobW = 5;
        int extend = 3;
        int knobX = trackX + Math.max(0, Math.min(fillW - knobW / 2, width - knobW));
        c.rect(knobX, y - extend, knobX + knobW, y + height + extend, 0xFFFFFFFF);
        c.border(knobX, y - extend, knobW, height + extend * 2, 0xFF000000);
    }

    public static int chromaButtonWidth() {
        return Minecraft.getInstance().font.width(CHROMA_LABEL) + 8;
    }

    public static int chromaButtonWidth(HudCanvas c) {
        return c.textWidth(CHROMA_LABEL) + 8;
    }

    public static void drawChromaButton(HudCanvas c, int x, int y, int h, boolean on, boolean hover) {
        int w = chromaButtonWidth(c);
        c.rect(x, y, x + w, y + h, on ? currentAccent() : WIDGET_OFF);
        c.border(x, y, w, h, hover ? 0xFFFFFFFF : BORDER);
        int textW = c.textWidth(CHROMA_LABEL);
        c.text(CHROMA_LABEL, x + (w - textW) / 2, y + (h - FONT_H) / 2, on ? 0xFFFFFFFF : TEXT, true);
    }

    public static boolean hit(double mx, double my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}
