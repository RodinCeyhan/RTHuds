package com.rtc.client.huds;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class HudElementSettingsScreen extends SettingsScreen {
    private final HudElement target;

    public HudElementSettingsScreen(Screen parent, HudElement target) {
        super(parent, Component.translatable("rthuds.settings.element.title", target.title));
        this.target = target;
    }

    @Override
    protected boolean hasPreview() {
        return true;
    }

    @Override
    protected void buildRows(List<Row> out) {
        out.add(new ToggleRow(Component.translatable("rthuds.settings.textshadow"),
                () -> target.textShadow, v -> target.textShadow = v));
        out.add(new ToggleRow(Component.translatable("rthuds.settings.background"),
                () -> target.showBackground, v -> target.showBackground = v));
        out.add(new ColorRow(Component.translatable("rthuds.settings.bgcolor"),
                () -> target.bgColor, v -> target.bgColor = v, null, null, true));
        out.add(new ColorRow(Component.translatable("rthuds.settings.labelcolor"),
                () -> target.textColor, v -> target.textColor = v,
                () -> target.textChroma, v -> target.textChroma = v));
        out.add(new ColorRow(Component.translatable("rthuds.settings.valuecolor"),
                () -> target.valueColor, v -> target.valueColor = v,
                () -> target.valueChroma, v -> target.valueChroma = v));
        if (target.coordFormatter != null) {
            out.add(new StepSliderRow(Component.translatable("rthuds.settings.decimals"),
                    CoordFormatter.MIN, CoordFormatter.MAX,
                    () -> target.coordFormatter.getDecimalPlaces(),
                    v -> target.coordFormatter.setDecimalPlaces(v),
                    v -> v == CoordFormatter.DEFAULT
                            ? Component.translatable("rthuds.settings.decimals.default")
                            : Component.literal(String.valueOf(v))));
        }
    }

    @Override
    protected void drawPreview(HudCanvas c, int x, int y, int w, int h) {
        int ox = target.x, oy = target.y;
        float os = target.scale;
        try {
            target.measure();
            if (target.w > w - 8 || target.h > h - 8) {
                float fit = Math.min((w - 8f) / target.w, (h - 8f) / target.h);
                target.scale = os * fit;
                target.measure();
            }
            target.x = x + (w - target.w) / 2;
            target.y = y + (h - target.h) / 2;
            target.draw(c, 0x66000000, target.textShadow, false);
        } finally {
            target.x = ox;
            target.y = oy;
            target.scale = os;
            target.measure();
        }
    }
}
