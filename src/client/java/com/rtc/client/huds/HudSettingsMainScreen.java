package com.rtc.client.huds;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class HudSettingsMainScreen extends SettingsScreen {

    public HudSettingsMainScreen(Screen parent) {
        super(parent, Component.translatable("rthuds.settings.main.title"));
    }

    @Override
    protected int listWidth() {
        return 400;
    }

    @Override
    protected void buildRows(List<Row> out) {
        out.add(new TitleRow(Component.translatable("rthuds.settings.section.appearance")));
        out.add(new ColorRow(Component.translatable("rthuds.settings.accent"),
                () -> UiStyle.ACCENT, v -> UiStyle.ACCENT = v,
                () -> UiStyle.ACCENT_CHROMA, v -> UiStyle.ACCENT_CHROMA = v));
        out.add(new CycleRow(Component.translatable("rthuds.settings.theme"),
                () -> Component.translatable("rthuds.theme." + UiStyle.theme.name().toLowerCase(Locale.ROOT)),
                () -> UiStyle.setTheme(UiStyle.Theme.values()[(UiStyle.theme.ordinal() + 1) % UiStyle.Theme.values().length])));

        out.add(new TitleRow(Component.translatable("rthuds.settings.section.general")));
        out.add(new ToggleRow(Component.translatable("rthuds.settings.hidegui"),
                () -> HudConfig.showHideGui, v -> HudConfig.showHideGui = v));
        out.add(new ToggleRow(Component.translatable("rthuds.settings.debuggui"),
                () -> HudConfig.showDebugGui, v -> HudConfig.showDebugGui = v));

        out.add(new TitleRow(Component.translatable("rthuds.settings.section.huds")));
        List<Row> cells = new ArrayList<>();
        for (HudElement e : HudRegistry.ELEMENTS) {
            cells.add(new HudRow(e.title, () -> e.enabled, () -> e.enabled = !e.enabled,
                    () -> open(new HudElementSettingsScreen(this, e)), e::resetAll));
        }
        cells.add(new HudRow(Component.translatable("rthuds.editor.armor"),
                () -> ArmorHudConfig.enabled, () -> ArmorHudConfig.enabled = !ArmorHudConfig.enabled,
                () -> open(new ArmorHudSettingsScreen(this)), ArmorHudConfig::reset));
        cells.add(new HudRow(Component.translatable("rthuds.effects.title"),
                () -> EffectsHudConfig.enabled, () -> EffectsHudConfig.enabled = !EffectsHudConfig.enabled,
                () -> open(new EffectsHudSettingsScreen(this)), EffectsHudConfig::reset));
        for (int i = 0; i < cells.size(); i += 2) {
            out.add(new PairRow(cells.get(i), i + 1 < cells.size() ? cells.get(i + 1) : null));
        }
    }
}
