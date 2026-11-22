package com.rtc.client.huds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Locale;

public final class EffectsHudSettingsScreen extends SettingsScreen {

    public EffectsHudSettingsScreen(Screen parent) {
        super(parent, Component.translatable("rthuds.settings.effects.title"));
    }

    @Override
    protected boolean hasPreview() {
        return true;
    }

    private static Component enumText(String group, Enum<?> value) {
        return Component.translatable("rthuds.enum." + group + "." + value.name().toLowerCase(Locale.ROOT));
    }

    private static <E extends Enum<E>> E next(E[] values, E current) {
        return values[(current.ordinal() + 1) % values.length];
    }

    @Override
    protected void buildRows(List<Row> out) {
        out.add(new ToggleRow(Component.translatable("rthuds.effects.hidevanilla"),
                () -> EffectsHudConfig.hideVanilla, v -> EffectsHudConfig.hideVanilla = v));

        out.add(new TitleRow(Component.translatable("rthuds.armor.section.look")));
        out.add(new CycleRow(Component.translatable("rthuds.armor.orientation"),
                () -> enumText("orientation", EffectsHudConfig.orientation),
                () -> EffectsHudConfig.orientation = next(EffectsHudConfig.Orientation.values(), EffectsHudConfig.orientation)));
        out.add(new CycleRow(Component.translatable("rthuds.effects.sort"),
                () -> enumText("effectsort", EffectsHudConfig.sortMode),
                () -> EffectsHudConfig.sortMode = next(EffectsHudConfig.SortMode.values(), EffectsHudConfig.sortMode)));
        out.add(new ToggleRow(Component.translatable("rthuds.effects.showicon"), () -> EffectsHudConfig.showIcon, v -> EffectsHudConfig.showIcon = v));
        out.add(new ToggleRow(Component.translatable("rthuds.effects.showname"), () -> EffectsHudConfig.showName, v -> EffectsHudConfig.showName = v));
        out.add(new ToggleRow(Component.translatable("rthuds.effects.showduration"), () -> EffectsHudConfig.showDuration, v -> EffectsHudConfig.showDuration = v));
        out.add(new ToggleRow(Component.translatable("rthuds.effects.showbar"), () -> EffectsHudConfig.showBar, v -> EffectsHudConfig.showBar = v));

        out.add(new TitleRow(Component.translatable("rthuds.settings.background")));
        out.add(new ToggleRow(Component.translatable("rthuds.armor.panel"), () -> EffectsHudConfig.showPanel, v -> EffectsHudConfig.showPanel = v));
        out.add(new ColorRow(Component.translatable("rthuds.settings.bgcolor"),
                () -> EffectsHudConfig.panelColor, v -> EffectsHudConfig.panelColor = v, null, null, true));

        out.add(new TitleRow(Component.translatable("rthuds.armor.section.color")));
        out.add(new ColorRow(Component.translatable("rthuds.armor.textcolor"),
                () -> EffectsHudConfig.textColor, v -> EffectsHudConfig.textColor = v,
                () -> EffectsHudConfig.textChroma, v -> EffectsHudConfig.textChroma = v));
        out.add(new ToggleRow(Component.translatable("rthuds.settings.textshadow"), () -> EffectsHudConfig.textShadow, v -> EffectsHudConfig.textShadow = v));
        out.add(new ToggleRow(Component.translatable("rthuds.effects.dynamiccolor"), () -> EffectsHudConfig.dynamicColor, v -> EffectsHudConfig.dynamicColor = v));

        out.add(new TitleRow(Component.translatable("rthuds.effects.section.filters")));
        out.add(new ToggleRow(Component.translatable("rthuds.effects.beneficial"), () -> EffectsHudConfig.showBeneficial, v -> EffectsHudConfig.showBeneficial = v));
        out.add(new ToggleRow(Component.translatable("rthuds.effects.harmful"), () -> EffectsHudConfig.showHarmful, v -> EffectsHudConfig.showHarmful = v));
        out.add(new ToggleRow(Component.translatable("rthuds.effects.hideambient"), () -> EffectsHudConfig.hideAmbient, v -> EffectsHudConfig.hideAmbient = v));
    }

    @Override
    protected void drawPreview(HudCanvas c, int x, int y, int w, int h) {
        float os = EffectsHudConfig.scale;
        try {
            var layout = EffectsHudRenderer.computeFreeLayout(0, 0, width, height, true);
            if (layout.w > w - 8 || layout.h > h - 8) {
                EffectsHudConfig.scale = os * Math.min((w - 8f) / layout.w, (h - 8f) / layout.h);
                layout = EffectsHudRenderer.computeFreeLayout(0, 0, width, height, true);
            }
            layout = EffectsHudRenderer.computeFreeLayout(x + (w - layout.w) / 2, y + (h - layout.h) / 2, width, height, true);
            EffectsHudRenderer.draw(c, Minecraft.getInstance(), layout);
        } finally {
            EffectsHudConfig.scale = os;
        }
    }
}
