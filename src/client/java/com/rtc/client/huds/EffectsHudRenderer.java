package com.rtc.client.huds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class EffectsHudRenderer {
    private EffectsHudRenderer() {
    }

    private static final int DEFAULT_MARGIN = 6;
    private static final int SAMPLE_MAX = 6000;

    private static final Map<Holder<MobEffect>, Integer> MAX_SEEN = new HashMap<>();

    public static final class Cell {
        final MobEffectInstance effect;
        final Component name;
        final String duration;
        final float fraction;
        int x, y;

        Cell(MobEffectInstance effect, Component name, String duration, float fraction) {
            this.effect = effect;
            this.name = name;
            this.duration = duration;
            this.fraction = fraction;
        }
    }

    public static final class Layout {
        public int x, y, w, h;
        final List<Cell> cells = new ArrayList<>();
        int cellW, cellH, iconY, textX, textY0, barY;
        boolean icon;

        void translate(int dx, int dy) {
            x += dx;
            y += dy;
            for (Cell c : cells) {
                c.x += dx;
                c.y += dy;
            }
        }
    }

    public static boolean hidesVanilla() {
        return EffectsHudConfig.enabled && EffectsHudConfig.hideVanilla;
    }

    private static List<MobEffectInstance> samples() {
        return List.of(
                new MobEffectInstance(MobEffects.REGENERATION, 4800, 1),
                new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 450),
                new MobEffectInstance(MobEffects.NIGHT_VISION, 180),
                new MobEffectInstance(MobEffects.POISON, 20 * 60 * 90, 0));
    }

    private static boolean passesFilters(MobEffectInstance e) {
        if (!e.showIcon()) return false;
        if (e.isAmbient() && EffectsHudConfig.hideAmbient) return false;
        boolean beneficial = e.getEffect().value().isBeneficial();
        return beneficial ? EffectsHudConfig.showBeneficial : EffectsHudConfig.showHarmful;
    }

    private static Component nameOf(MobEffectInstance e) {
        var name = e.getEffect().value().getDisplayName().copy();
        int level = e.getAmplifier() + 1;
        if (level >= 2) {
            name.append(" ").append(level <= 10 ? Component.translatable("enchantment.level." + level) : Component.literal(String.valueOf(level)));
        }
        return name;
    }

    private static String format(MobEffectInstance e) {
        if (e.isInfiniteDuration()) return Component.translatable("rthuds.effects.infinite").getString();
        int seconds = Math.max(0, (e.getDuration() + 19) / 20);
        int h = seconds / 3600, m = (seconds % 3600) / 60, s = seconds % 60;
        return h > 0 ? String.format("%d:%02d:%02d", h, m, s) : String.format("%d:%02d", m, s);
    }

    private static int remainingSeconds(MobEffectInstance e) {
        return e.isInfiniteDuration() ? Integer.MAX_VALUE : Math.max(0, (e.getDuration() + 19) / 20);
    }

    private static List<MobEffectInstance> collect(Minecraft mc, boolean preview, boolean[] usingSamples) {
        List<MobEffectInstance> out = new ArrayList<>();
        LocalPlayer p = mc.player;
        Set<Holder<MobEffect>> active = new HashSet<>();
        if (p != null) {
            for (MobEffectInstance e : p.getActiveEffects()) {
                active.add(e.getEffect());
                if (passesFilters(e)) out.add(e);
            }
        }
        MAX_SEEN.keySet().retainAll(active);
        if (out.isEmpty() && preview) {
            for (MobEffectInstance e : samples()) if (passesFilters(e)) out.add(e);
            usingSamples[0] = true;
        } else {
            for (MobEffectInstance e : out) MAX_SEEN.merge(e.getEffect(), e.getDuration(), Math::max);
        }

        switch (EffectsHudConfig.sortMode) {
            case DURATION_ASC -> out.sort(Comparator.comparingInt(EffectsHudRenderer::remainingSeconds));
            case DURATION_DESC -> out.sort(Comparator.comparingInt(EffectsHudRenderer::remainingSeconds).reversed());
            case NAME -> out.sort(Comparator.comparing(e -> nameOf(e).getString()));
            default -> {
            }
        }
        return out;
    }

    public static Layout computeLayout(int screenW, int screenH, boolean preview) {
        boolean defaultX = EffectsHudConfig.x == EffectsHudConfig.DEFAULT_POS;
        boolean defaultY = EffectsHudConfig.y == EffectsHudConfig.DEFAULT_POS;
        Layout L = computeFreeLayout(defaultX ? 0 : EffectsHudConfig.x, defaultY ? 0 : EffectsHudConfig.y, screenW, screenH, preview);
        if (defaultX || defaultY) {
            L.translate(defaultX ? screenW - L.w - DEFAULT_MARGIN - L.x : 0, defaultY ? DEFAULT_MARGIN - L.y : 0);
        }
        return L;
    }

    public static Layout computeFreeLayout(int ax, int ay, int screenW, int screenH, boolean preview) {
        Minecraft mc = Minecraft.getInstance();
        var font = mc.font;
        float s = EffectsHudConfig.scale;
        int I = Math.round(18 * s), TG = Math.max(2, Math.round(4 * s)), TH = Math.round(font.lineHeight * s);
        int G = Math.max(2, Math.round(4 * s));
        int pad = EffectsHudConfig.showPanel ? Math.max(2, Math.round(4 * s)) : 0;
        int barH = Math.max(2, Math.round(3 * s)), barGap = Math.max(1, Math.round(2 * s));

        boolean[] samples = {false};
        List<MobEffectInstance> list = collect(mc, preview, samples);

        Layout L = new Layout();
        if (list.isEmpty()) {
            L.w = L.h = 0;
            return L;
        }
        int lines = (EffectsHudConfig.showName ? 1 : 0) + (EffectsHudConfig.showDuration ? 1 : 0);
        L.icon = EffectsHudConfig.showIcon || lines == 0;

        int maxTextW = 0;
        for (MobEffectInstance e : list) {
            Component name = nameOf(e);
            String dur = format(e);
            int max = samples[0] ? SAMPLE_MAX : Math.max(1, MAX_SEEN.getOrDefault(e.getEffect(), e.getDuration()));
            float fraction = e.isInfiniteDuration() ? 1f : Math.max(0f, Math.min(1f, e.getDuration() / (float) max));
            L.cells.add(new Cell(e, name, dur, fraction));
            if (EffectsHudConfig.showName) maxTextW = Math.max(maxTextW, Math.round(font.width(name) * s));
            if (EffectsHudConfig.showDuration) maxTextW = Math.max(maxTextW, Math.round(font.width(dur) * s));
        }

        int contentH = Math.max(L.icon ? I : 0, lines * TH);
        L.cellW = (L.icon ? I + (lines > 0 ? TG : 0) : 0) + (lines > 0 ? maxTextW : 0);
        L.cellH = contentH + (EffectsHudConfig.showBar ? barGap + barH : 0);
        L.iconY = (contentH - I) / 2;
        L.textX = L.icon ? I + TG : 0;
        L.textY0 = (contentH - lines * TH) / 2;
        L.barY = contentH + barGap;

        boolean vertical = EffectsHudConfig.orientation == EffectsHudConfig.Orientation.VERTICAL;
        int cursor = pad;
        for (Cell c : L.cells) {
            c.x = vertical ? pad : cursor;
            c.y = vertical ? cursor : pad;
            cursor += (vertical ? L.cellH : L.cellW) + G;
        }
        int span = cursor - G - pad;
        L.w = 2 * pad + (vertical ? L.cellW : span);
        L.h = 2 * pad + (vertical ? span : L.cellH);
        L.translate(clamp(ax, screenW - L.w), clamp(ay, screenH - L.h));
        return L;
    }

    private static int clamp(int v, int max) {
        return Math.max(0, Math.min(v, Math.max(0, max)));
    }

    public static void render(HudCanvas c, Minecraft mc) {
        if (!EffectsHudConfig.enabled || mc.player == null) return;
        draw(c, mc, computeLayout(mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight(), false));
    }

    public static void draw(HudCanvas c, Minecraft mc, Layout L) {
        if (L.cells.isEmpty()) return;
        float s = EffectsHudConfig.scale;
        int I = Math.round(18 * s), TH = Math.round(mc.font.lineHeight * s);
        int barH = Math.max(2, Math.round(3 * s));

        if (EffectsHudConfig.showPanel) {
            c.rect(L.x, L.y, L.x + L.w, L.y + L.h, EffectsHudConfig.panelColor);
        }
        int base = EffectsHudConfig.textChroma ? ChromaUtil.color() : EffectsHudConfig.textColor;
        for (Cell cell : L.cells) {
            if (L.icon) {
                c.effectIcon(cell.effect.getEffect(), cell.x, cell.y + L.iconY, I);
            }
            int ty = cell.y + L.textY0;
            if (EffectsHudConfig.showName) {
                c.textScaled(cell.name, cell.x + L.textX, ty, base, EffectsHudConfig.textShadow, s);
                ty += TH;
            }
            if (EffectsHudConfig.showDuration) {
                c.textScaled(cell.duration, cell.x + L.textX, ty, durationColor(cell.effect, base), EffectsHudConfig.textShadow, s);
            }
            if (EffectsHudConfig.showBar) {
                int by = cell.y + L.barY;
                c.rect(cell.x, by, cell.x + L.cellW, by + barH, 0x80000000);
                int fill = Math.round(L.cellW * cell.fraction);
                if (fill > 0) {
                    c.rect(cell.x, by, cell.x + fill, by + barH, 0xFF000000 | cell.effect.getEffect().value().getColor());
                }
            }
        }
    }

    private static int durationColor(MobEffectInstance e, int base) {
        if (!EffectsHudConfig.dynamicColor || e.isInfiniteDuration()) return base;
        int sec = remainingSeconds(e);
        if (sec <= 10) return 0xFFFF5555;
        if (sec <= 30) return 0xFFFFAA00;
        if (sec <= 60) return 0xFFFFFF55;
        return base;
    }
}
