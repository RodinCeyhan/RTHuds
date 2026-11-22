package com.rtc.client.huds;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public final class HudElement {
    public static final int PAD = 3;
    public static final float MIN_SCALE = 0.5f;
    public static final float MAX_SCALE = 3.0f;
    public static final float SCALE_STEP = 0.1f;

    public final String id;
    public final Component title;
    public final Supplier<Component> label;
    public final Supplier<Component> value;
    public BooleanSupplier visible = () -> true;

    public CoordFormatter coordFormatter;

    public final int defaultX, defaultY;
    public int x, y, w, h;
    public float scale = 1.0f;
    public boolean enabled = true;
    public boolean textShadow = true;
    public boolean showBackground = true;
    public static final int DEFAULT_BG_COLOR = 0x66000000;
    public int bgColor = DEFAULT_BG_COLOR;
    public int textColor = 0xFFFFFFFF;
    public int valueColor = 0xFFFFFFFF;
    public boolean textChroma = false;
    public boolean valueChroma = false;

    public HudElement(String id, int x, int y, Supplier<Component> label, Supplier<Component> value) {
        this.id = id;
        this.title = Component.translatable("rthuds.hudname." + id);
        this.defaultX = x;
        this.defaultY = y;
        this.x = x;
        this.y = y;
        this.label = label;
        this.value = value;
    }

    private Component composed() {
        int tc = textChroma ? ChromaUtil.color() : textColor;
        int vc = valueChroma ? ChromaUtil.color() : valueColor;
        return Component.empty()
                .append(label.get().copy().withColor(tc))
                .append(value.get().copy().withColor(vc));
    }

    public void measure() {
        var font = Minecraft.getInstance().font;
        int rawW = font.width(composed()) + PAD * 2;
        int rawH = font.lineHeight + PAD * 2 - 1;
        w = Math.round(rawW * scale);
        h = Math.round(rawH * scale);
    }

    public void clampToScreen() {
        var win = Minecraft.getInstance().getWindow();
        x = Math.max(0, Math.min(x, Math.max(0, win.getGuiScaledWidth() - w)));
        y = Math.max(0, Math.min(y, Math.max(0, win.getGuiScaledHeight() - h)));
    }

    public boolean contains(double mx, double my) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    public void draw(HudCanvas c, int bg, boolean shadow, boolean forceBg) {
        if (forceBg) {
            c.rect(x, y, x + w, y + h, bg);
        } else if (showBackground) {
            c.rect(x, y, x + w, y + h, bgColor);
        }
        if (scale == 1.0f) {
            c.text(composed(), x + PAD, y + PAD, 0xFFFFFFFF, shadow);
        } else {
            c.textScaled(composed(), x + Math.round(PAD * scale), y + Math.round(PAD * scale), 0xFFFFFFFF, shadow, scale);
        }
    }

    public void resetPosition() {
        x = defaultX;
        y = defaultY;
        scale = 1.0f;
    }

    public void resetAll() {
        resetPosition();
        textShadow = true;
        showBackground = true;
        bgColor = DEFAULT_BG_COLOR;
        textColor = 0xFFFFFFFF;
        valueColor = 0xFFFFFFFF;
        textChroma = false;
        valueChroma = false;
        if (coordFormatter != null) coordFormatter.setDecimalPlaces(CoordFormatter.DEFAULT);
    }

    public void setScale(float newScale) {
        scale = clampScale(newScale);
    }

    public static float clampScale(float value) {
        return Math.max(MIN_SCALE, Math.min(MAX_SCALE, value));
    }

    public static float stepScale(float current, int direction) {
        return clampScale(Math.round((current + direction * SCALE_STEP) * 10f) / 10f);
    }
}