package com.rtc.client.huds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Locale;

public final class ArmorHudSettingsScreen extends SettingsScreen {

    public ArmorHudSettingsScreen(Screen parent) {
        super(parent, Component.translatable("rthuds.settings.armor.title"));
    }

    @Override
    protected boolean hasPreview() {
        return true;
    }

    private static Component slot(String key) {
        return Component.translatable("rthuds.armor.slot." + key);
    }

    private static Component enumText(String group, Enum<?> value) {
        return Component.translatable("rthuds.enum." + group + "." + value.name().toLowerCase(Locale.ROOT));
    }

    private static <E extends Enum<E>> E next(E[] values, E current) {
        return values[(current.ordinal() + 1) % values.length];
    }

    @Override
    protected void buildRows(List<Row> out) {
        out.add(new TitleRow(Component.translatable("rthuds.armor.section.pieces")));
        out.add(new ToggleRow(slot("helmet"), () -> ArmorHudConfig.showHelmet, v -> ArmorHudConfig.showHelmet = v));
        out.add(new ToggleRow(slot("chest"), () -> ArmorHudConfig.showChest, v -> ArmorHudConfig.showChest = v));
        out.add(new ToggleRow(slot("legs"), () -> ArmorHudConfig.showLegs, v -> ArmorHudConfig.showLegs = v));
        out.add(new ToggleRow(slot("boots"), () -> ArmorHudConfig.showBoots, v -> ArmorHudConfig.showBoots = v));
        out.add(new ToggleRow(slot("mainhand"), () -> ArmorHudConfig.showMainHand, v -> ArmorHudConfig.showMainHand = v));
        out.add(new ToggleRow(slot("offhand"), () -> ArmorHudConfig.showOffHand, v -> ArmorHudConfig.showOffHand = v));

        out.add(new TitleRow(Component.translatable("rthuds.armor.section.look")));
        out.add(new CycleRow(Component.translatable("rthuds.armor.orientation"),
                () -> enumText("orientation", ArmorHudConfig.orientation),
                () -> ArmorHudConfig.orientation = next(ArmorHudConfig.Orientation.values(), ArmorHudConfig.orientation)));
        out.add(new CycleRow(Component.translatable("rthuds.armor.background"),
                () -> enumText("bg", ArmorHudConfig.bgStyle),
                () -> ArmorHudConfig.bgStyle = next(ArmorHudConfig.BgStyle.values(), ArmorHudConfig.bgStyle)));
        out.add(new CycleRow(Component.translatable("rthuds.armor.healthmode"),
                () -> enumText("healthmode", ArmorHudConfig.healthMode),
                () -> ArmorHudConfig.healthMode = next(ArmorHudConfig.HealthMode.values(), ArmorHudConfig.healthMode)));
        out.add(new CycleRow(Component.translatable("rthuds.armor.healthpos"),
                () -> enumText("healthpos", ArmorHudConfig.healthPos),
                () -> ArmorHudConfig.healthPos = next(ArmorHudConfig.HealthPos.values(), ArmorHudConfig.healthPos)));
        out.add(new ToggleRow(Component.translatable("rthuds.armor.showcount"), () -> ArmorHudConfig.showCount, v -> ArmorHudConfig.showCount = v));
        out.add(new ToggleRow(Component.translatable("rthuds.armor.durabilitybar"), () -> ArmorHudConfig.showDurabilityBar, v -> ArmorHudConfig.showDurabilityBar = v));
        out.add(new ToggleRow(Component.translatable("rthuds.armor.showempty"), () -> ArmorHudConfig.showEmptySlots, v -> ArmorHudConfig.showEmptySlots = v));
        out.add(new ToggleRow(Component.translatable("rthuds.armor.whiteempty"), () -> ArmorHudConfig.whiteEmptyIcons, v -> ArmorHudConfig.whiteEmptyIcons = v));
        out.add(new ToggleRow(Component.translatable("rthuds.armor.panel"), () -> ArmorHudConfig.showPanel, v -> ArmorHudConfig.showPanel = v));
        out.add(new ColorRow(Component.translatable("rthuds.settings.bgcolor"),
                () -> ArmorHudConfig.panelColor, v -> ArmorHudConfig.panelColor = v, null, null, true));
        out.add(new ToggleRow(Component.translatable("rthuds.settings.textshadow"), () -> ArmorHudConfig.textShadow, v -> ArmorHudConfig.textShadow = v));

        out.add(new TitleRow(Component.translatable("rthuds.armor.section.color")));
        out.add(new ColorRow(Component.translatable("rthuds.armor.textcolor"),
                () -> ArmorHudConfig.textColor, v -> ArmorHudConfig.textColor = v,
                () -> ArmorHudConfig.textChroma, v -> ArmorHudConfig.textChroma = v));
        out.add(new ToggleRow(Component.translatable("rthuds.armor.dynamiccolor"),
                () -> ArmorHudConfig.dynamicColor, v -> ArmorHudConfig.dynamicColor = v));

        out.add(new TitleRow(Component.translatable("rthuds.armor.section.warning")));
        out.add(new StepSliderRow(Component.translatable("rthuds.armor.warnpercent"), 1, 99,
                () -> ArmorHudConfig.warnPercent, v -> ArmorHudConfig.warnPercent = v,
                v -> Component.literal(v + "%")));
        out.add(new CycleRow(Component.translatable("rthuds.armor.warnsound"),
                () -> enumText("warnsound", ArmorHudConfig.warnSound),
                () -> {
                    ArmorHudConfig.warnSound = next(ArmorHudConfig.WarnSound.values(), ArmorHudConfig.warnSound);
                    if (ArmorHudConfig.warnSound.soundPath != null) {
                        VersionCompat.playUiSound(ArmorHudConfig.warnSound.soundPath, 1.0f);
                    }
                }));
    }

    @Override
    protected void drawPreview(HudCanvas c, int x, int y, int w, int h) {
        float os = ArmorHudConfig.scale;
        try {
            var layout = ArmorHudRenderer.computeFreeLayout(0, 0, width, height);
            if (layout.w > w - 8 || layout.h > h - 8) {
                ArmorHudConfig.scale = os * Math.min((w - 8f) / layout.w, (h - 8f) / layout.h);
                layout = ArmorHudRenderer.computeFreeLayout(0, 0, width, height);
            }
            layout = ArmorHudRenderer.computeFreeLayout(x + (w - layout.w) / 2, y + (h - layout.h) / 2, width, height);
            ArmorHudRenderer.draw(c, Minecraft.getInstance(), layout);
        } finally {
            ArmorHudConfig.scale = os;
        }
    }
}
