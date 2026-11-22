package com.rtc.client.huds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//? if >=26.1.2 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?} else {
/*import net.minecraft.client.gui.GuiGraphics;
 */
//?}

//? if >=1.21.9 {
import net.minecraft.client.input.MouseButtonEvent;
import org.jspecify.annotations.NonNull;
//?}

@SuppressWarnings({"SameParameterValue"})
public final class HudEditorScreen extends Screen {
    private static final int SNAP = 2;
    private static final int TOUCH = 3;
    private static final int GAP = 2;
    private static final int GUIDE = 0xFF00E5FF;
    private static final int DRAG_THRESHOLD = 4;

    private static final int LABEL_H = 12;
    private static final int LABEL_PAD = 3;
    private static final int LABEL_BTN = 10;

    private record Rect(int x, int y, int w, int h) {
    }

    private record Label(int x, int y, int w, int gearX, int minusX, int plusX, int btnY, String text) {
        boolean overGear(double mx, double my) {
            return UiStyle.hit(mx, my, gearX, btnY, LABEL_BTN, LABEL_BTN);
        }

        boolean overMinus(double mx, double my) {
            return UiStyle.hit(mx, my, minusX, btnY, LABEL_BTN, LABEL_BTN);
        }

        boolean overPlus(double mx, double my) {
            return UiStyle.hit(mx, my, plusX, btnY, LABEL_BTN, LABEL_BTN);
        }
    }

    private HudElement dragging;
    private boolean draggingArmor;
    private boolean draggingEffects;
    private double grabX, grabY;
    private double pressX, pressY;
    private boolean armorMoved;
    private boolean armorSnapArmed;
    private final List<Integer> guidesX = new ArrayList<>();
    private final List<Integer> guidesY = new ArrayList<>();

    private int settingsButtonX, settingsButtonY, settingsButtonW, settingsButtonH;
    private int resetButtonX, resetButtonY, resetButtonW, resetButtonH;

    public HudEditorScreen() {
        super(Component.translatable("rthuds.editor.title"));
    }

    private void layoutButtons() {
        int gearSize = 20;
        settingsButtonX = width - gearSize - 10;
        settingsButtonY = 10;
        settingsButtonW = gearSize;
        settingsButtonH = gearSize;
        Component reset = Component.translatable("rthuds.editor.reset");
        resetButtonW = font.width(reset) + 12;
        resetButtonH = gearSize;
        resetButtonX = settingsButtonX - resetButtonW - 8;
        resetButtonY = settingsButtonY;
    }

    private Label labelFor(Rect box, String title, float scale) {
        String text = title + "  " + String.format(Locale.US, "%.1fx", scale);
        int w = LABEL_PAD + font.width(text) + LABEL_PAD + 3 * LABEL_BTN + 4 + LABEL_PAD;
        int x = Math.max(0, Math.min(box.x(), width - w));
        int y = box.y() - LABEL_H - 2;
        if (y < 0) y = box.y() + box.h() + 2;
        int plusX = x + w - LABEL_PAD - LABEL_BTN;
        int minusX = plusX - 2 - LABEL_BTN;
        int gearX = minusX - 2 - LABEL_BTN;
        return new Label(x, y, w, gearX, minusX, plusX, y + (LABEL_H - LABEL_BTN) / 2, text);
    }

    private Rect rectOf(HudElement e) {
        return new Rect(e.x, e.y, e.w, e.h);
    }

    private Rect rectOf(ArmorHudRenderer.Layout l) {
        return new Rect(l.x, l.y, l.w, l.h);
    }

    private Label labelOf(HudElement e) {
        return labelFor(rectOf(e), e.title.getString(), e.scale);
    }

    private EffectsHudRenderer.Layout effectsLayout() {
        return EffectsHudRenderer.computeLayout(width, height, true);
    }

    private Rect rectOf(EffectsHudRenderer.Layout l) {
        return new Rect(l.x, l.y, l.w, l.h);
    }

    private Label effectsLabel(EffectsHudRenderer.Layout l) {
        return labelFor(rectOf(l), Component.translatable("rthuds.effects.title").getString(), EffectsHudConfig.scale);
    }

    private Label armorLabel() {
        return labelFor(rectOf(ArmorHudRenderer.computeLayout(width, height)),
                Component.translatable("rthuds.editor.armor").getString(), ArmorHudConfig.scale);
    }

    private void drawLabel(HudCanvas c, Label l, int mx, int my) {
        c.rect(l.x, l.y, l.x + l.w, l.y + LABEL_H, UiStyle.LABEL_BG);
        c.border(l.x, l.y, l.w, LABEL_H, UiStyle.BORDER);
        c.text(l.text, l.x + LABEL_PAD, l.y + (LABEL_H - UiStyle.FONT_H) / 2 + 1, UiStyle.TEXT, true);
        drawGearButton(c, l.gearX, l.btnY, l.overGear(mx, my));
        drawStepButton(c, l.minusX, l.btnY, "-", l.overMinus(mx, my));
        drawStepButton(c, l.plusX, l.btnY, "+", l.overPlus(mx, my));
    }

    private void drawGearButton(HudCanvas c, int x, int y, boolean hover) {
        c.rect(x, y, x + LABEL_BTN, y + LABEL_BTN, hover ? UiStyle.accentDim() : UiStyle.WIDGET_BG);
        c.border(x, y, LABEL_BTN, LABEL_BTN, hover ? UiStyle.currentAccent() : UiStyle.BORDER);
        c.icon("settings", x + 1, y + 1, 8, 8);
    }

    private void drawStepButton(HudCanvas c, int x, int y, String glyph, boolean hover) {
        c.rect(x, y, x + LABEL_BTN, y + LABEL_BTN, hover ? UiStyle.accentDim() : UiStyle.WIDGET_BG);
        c.border(x, y, LABEL_BTN, LABEL_BTN, hover ? UiStyle.currentAccent() : UiStyle.BORDER);
        c.text(glyph, x + (LABEL_BTN - c.textWidth(glyph)) / 2 + 1, y + (LABEL_BTN - UiStyle.FONT_H) / 2 + 1, UiStyle.TEXT, false);
    }

    private void drawScreen(HudCanvas c, int mx, int my) {
        Component hint = Component.translatable("rthuds.editor.hint");
        c.text(hint, (width - font.width(hint)) / 2, 6, 0xFFAAAAAA, true);

        for (HudElement e : HudRegistry.ELEMENTS) {
            e.measure();
            if (e != dragging) e.clampToScreen();
        }

        for (HudElement e : HudRegistry.ELEMENTS) {
            int outline = e == dragging ? 0xFF55FF55 : e.contains(mx, my) ? 0xFFFFFF55 : 0xFFFFFFFF;
            e.draw(c, 0x88000000, true, true);
            c.border(e.x - 1, e.y - 1, e.w + 2, e.h + 2, outline);
        }

        Minecraft mc = Minecraft.getInstance();
        var armor = ArmorHudRenderer.computeLayout(width, height);
        if (armor.snapped) {
            int hbLeft = ArmorHudRenderer.hotbarLeft(width);
            int hbTop = ArmorHudRenderer.hotbarTop(height);
            c.rect(hbLeft, hbTop, ArmorHudRenderer.hotbarRight(width), height, 0x3355AAFF);
        }
        ArmorHudRenderer.draw(c, mc, armor);
        int armorOutline = draggingArmor ? 0xFF55FF55
                : armor.snapped ? 0xFF55AAFF
                : UiStyle.hit(mx, my, armor.x, armor.y, armor.w, armor.h) ? 0xFFFFFF55
                : 0xFFFFAA00;
        c.border(armor.x - 2, armor.y - 2, armor.w + 4, armor.h + 4, armorOutline);

        var effects = effectsLayout();
        if (effects.w > 0) {
            EffectsHudRenderer.draw(c, mc, effects);
            int effOutline = draggingEffects ? 0xFF55FF55
                    : UiStyle.hit(mx, my, effects.x, effects.y, effects.w, effects.h) ? 0xFFFFFF55 : 0xFF55DDFF;
            c.border(effects.x - 2, effects.y - 2, effects.w + 4, effects.h + 4, effOutline);
        }

        for (HudElement e : HudRegistry.ELEMENTS) drawLabel(c, labelOf(e), mx, my);
        drawLabel(c, armorLabel(), mx, my);
        if (effects.w > 0) drawLabel(c, effectsLabel(effects), mx, my);

        layoutButtons();
        boolean hoverReset = UiStyle.hit(mx, my, resetButtonX, resetButtonY, resetButtonW, resetButtonH);
        c.rect(resetButtonX, resetButtonY, resetButtonX + resetButtonW, resetButtonY + resetButtonH,
                hoverReset ? UiStyle.accentDim() : UiStyle.WIDGET_BG);
        c.border(resetButtonX, resetButtonY, resetButtonW, resetButtonH, hoverReset ? UiStyle.ACCENT : UiStyle.BORDER);
        c.text(Component.translatable("rthuds.editor.reset"), resetButtonX + 6,
                resetButtonY + (resetButtonH - UiStyle.FONT_H) / 2, UiStyle.TEXT, true);

        boolean hoverGear = UiStyle.hit(mx, my, settingsButtonX, settingsButtonY, settingsButtonW, settingsButtonH);
        UiStyle.drawIconButtonFrame(c, settingsButtonX, settingsButtonY, settingsButtonW, hoverGear);
        int gearIconSize = 9;
        int gearIconOffset = UiStyle.centerOffset(settingsButtonW, gearIconSize);
        c.icon("settings", settingsButtonX + gearIconOffset, settingsButtonY + gearIconOffset + 1, gearIconSize, gearIconSize);

        if (dragging != null || draggingArmor || draggingEffects) {
            for (int gx : guidesX) c.rect(gx, 0, gx + 1, height, GUIDE);
            for (int gy : guidesY) c.rect(0, gy, width, gy + 1, GUIDE);
        }
    }

    //? if >=26.1.2 {
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor g, int mx, int my, float delta) {
        super.extractRenderState(g, mx, my, delta);
        drawScreen(new HudCanvas(g), mx, my);
    }
//?} else {
/*    @Override
    public void render(GuiGraphics g, int mx, int my, float delta) {
        super.render(g, mx, my, delta);
        drawScreen(new HudCanvas(g), mx, my);
    }
 */
//?}

    private void resetAll() {
        for (HudElement e : HudRegistry.ELEMENTS) {
            e.resetPosition();
            e.clampToScreen();
        }
        ArmorHudConfig.x = ArmorHudConfig.DEFAULT_POS;
        ArmorHudConfig.y = ArmorHudConfig.DEFAULT_POS;
        ArmorHudConfig.scale = 1.0f;
        ArmorHudConfig.hotbarSnapped = false;
        EffectsHudConfig.x = EffectsHudConfig.DEFAULT_POS;
        EffectsHudConfig.y = EffectsHudConfig.DEFAULT_POS;
        EffectsHudConfig.scale = 1.0f;
        HudRegistry.save();
    }

    private void openScreen(Screen screen) {
//? if >=26.2 {
        Minecraft.getInstance().gui.setScreen(screen);
//?} else {
        /*        Minecraft.getInstance().setScreen(screen);
         */
//?}
    }

    private boolean onPress(double mx, double my, int button) {
        if (!VersionCompat.isLeftClick(button)) return false;
        layoutButtons();

        if (UiStyle.hit(mx, my, resetButtonX, resetButtonY, resetButtonW, resetButtonH)) {
            resetAll();
            return true;
        }
        if (UiStyle.hit(mx, my, settingsButtonX, settingsButtonY, settingsButtonW, settingsButtonH)) {
            openScreen(new HudSettingsMainScreen(this));
            return true;
        }

        List<HudElement> list = HudRegistry.ELEMENTS;
        for (HudElement e : list) {
            Label l = labelOf(e);
            if (l.overGear(mx, my)) {
                openScreen(new HudElementSettingsScreen(this, e));
                return true;
            }
            int dir = l.overMinus(mx, my) ? -1 : l.overPlus(mx, my) ? 1 : 0;
            if (dir == 0) continue;
            e.scale = HudElement.stepScale(e.scale, dir);
            e.measure();
            e.clampToScreen();
            HudRegistry.save();
            return true;
        }
        Label al = armorLabel();
        if (al.overGear(mx, my)) {
            openScreen(new ArmorHudSettingsScreen(this));
            return true;
        }
        int armorDir = al.overMinus(mx, my) ? -1 : al.overPlus(mx, my) ? 1 : 0;
        if (armorDir != 0) {
            ArmorHudConfig.scale = HudElement.stepScale(ArmorHudConfig.scale, armorDir);
            HudRegistry.save();
            return true;
        }

        var eff = effectsLayout();
        if (eff.w > 0) {
            Label el = effectsLabel(eff);
            if (el.overGear(mx, my)) {
                openScreen(new EffectsHudSettingsScreen(this));
                return true;
            }
            int effDir = el.overMinus(mx, my) ? -1 : el.overPlus(mx, my) ? 1 : 0;
            if (effDir != 0) {
                EffectsHudConfig.scale = HudElement.stepScale(EffectsHudConfig.scale, effDir);
                HudRegistry.save();
                return true;
            }
            if (UiStyle.hit(mx, my, eff.x, eff.y, eff.w, eff.h)) {
                draggingEffects = true;
                grabX = mx - eff.x;
                grabY = my - eff.y;
                return true;
            }
        }

        var layout = ArmorHudRenderer.computeLayout(width, height);
        if (UiStyle.hit(mx, my, layout.x, layout.y, layout.w, layout.h)) {
            draggingArmor = true;
            armorMoved = false;
            armorSnapArmed = !ArmorHudConfig.hotbarSnapped;
            pressX = mx;
            pressY = my;
            grabX = mx - layout.x;
            grabY = my - layout.y;
            return true;
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            HudElement e = list.get(i);
            if (!e.contains(mx, my)) continue;
            dragging = e;
            grabX = mx - e.x;
            grabY = my - e.y;
            return true;
        }
        return false;
    }

    private boolean onDrag(double mx, double my) {
        if (draggingEffects) {
            dragEffects(mx, my);
            return true;
        }
        if (draggingArmor) {
            dragArmor(mx, my);
            return true;
        }
        if (dragging == null) return false;

        int rawX = (int) Math.round(mx - grabX);
        int rawY = (int) Math.round(my - grabY);
        if (VersionCompat.isShiftDown()) {
            guidesX.clear();
            guidesY.clear();
            dragging.x = clamp(rawX, width - dragging.w);
            dragging.y = clamp(rawY, height - dragging.h);
        } else {
            List<Rect> others = new ArrayList<>();
            for (HudElement o : HudRegistry.ELEMENTS) if (o != dragging) others.add(rectOf(o));
            others.add(rectOf(ArmorHudRenderer.computeLayout(width, height)));
            addEffectsRect(others);
            int[] p = snap(rectOf(dragging), rawX, rawY, others);
            dragging.x = p[0];
            dragging.y = p[1];
        }
        return true;
    }

    private void dragArmor(double mx, double my) {
        if (!armorMoved) {
            if (Math.hypot(mx - pressX, my - pressY) < DRAG_THRESHOLD) return;
            armorMoved = true;
        }
        var free = ArmorHudRenderer.computeFreeLayout(0, 0, width, height);
        double gx = Math.min(grabX, free.w);
        double gy = Math.min(grabY, free.h);
        int rawX = clamp((int) Math.round(mx - gx), width - free.w);
        int rawY = clamp((int) Math.round(my - gy), height - free.h);

        var candidate = ArmorHudRenderer.computeFreeLayout(rawX, rawY, width, height);
        boolean near = ArmorHudRenderer.nearHotbar(candidate, width, height);
        if (!near) armorSnapArmed = true;

        if (near && armorSnapArmed) {
            ArmorHudConfig.hotbarSnapped = true;
            ArmorHudConfig.x = rawX;
            ArmorHudConfig.y = rawY;
            guidesX.clear();
            guidesY.clear();
            return;
        }
        ArmorHudConfig.hotbarSnapped = false;
        if (VersionCompat.isShiftDown()) {
            guidesX.clear();
            guidesY.clear();
            ArmorHudConfig.x = rawX;
            ArmorHudConfig.y = rawY;
        } else {
            List<Rect> others = new ArrayList<>();
            for (HudElement o : HudRegistry.ELEMENTS) others.add(rectOf(o));
            addEffectsRect(others);
            int[] p = snap(new Rect(rawX, rawY, free.w, free.h), rawX, rawY, others);
            ArmorHudConfig.x = p[0];
            ArmorHudConfig.y = p[1];
        }
    }

    private void addEffectsRect(List<Rect> others) {
        var eff = effectsLayout();
        if (eff.w > 0) others.add(rectOf(eff));
    }

    private void dragEffects(double mx, double my) {
        var free = EffectsHudRenderer.computeFreeLayout(0, 0, width, height, true);
        int rawX = clamp((int) Math.round(mx - grabX), width - free.w);
        int rawY = clamp((int) Math.round(my - grabY), height - free.h);
        if (VersionCompat.isShiftDown()) {
            guidesX.clear();
            guidesY.clear();
            EffectsHudConfig.x = rawX;
            EffectsHudConfig.y = rawY;
            return;
        }
        List<Rect> others = new ArrayList<>();
        for (HudElement o : HudRegistry.ELEMENTS) others.add(rectOf(o));
        others.add(rectOf(ArmorHudRenderer.computeLayout(width, height)));
        int[] p = snap(new Rect(rawX, rawY, free.w, free.h), rawX, rawY, others);
        EffectsHudConfig.x = p[0];
        EffectsHudConfig.y = p[1];
    }

    private boolean onRelease() {
        boolean was = dragging != null || draggingArmor || draggingEffects;
        dragging = null;
        draggingArmor = false;
        draggingEffects = false;
        armorMoved = false;
        guidesX.clear();
        guidesY.clear();
        if (was) HudRegistry.save();
        return was;
    }

    private static int clamp(int value, int max) {
        return Math.max(0, Math.min(value, Math.max(0, max)));
    }

    //? if >=1.21.9 {
    @Override
    public boolean mouseClicked(MouseButtonEvent e, boolean doubleClick) {
        return onPress(e.x(), e.y(), e.button()) || super.mouseClicked(e, doubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent e, double dx, double dy) {
        return onDrag(e.x(), e.y()) || super.mouseDragged(e, dx, dy);
    }

    @Override
    public boolean mouseReleased(@NonNull MouseButtonEvent e) {
        return onRelease() || super.mouseReleased(e);
    }
//?} else {
/*    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        return onPress(mx, my, button) || super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        return onDrag(mx, my) || super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        return onRelease() || super.mouseReleased(mx, my, button);
    }
 */
//?}

    @Override
    public void onClose() {
        HudRegistry.save();
        super.onClose();
    }

    private int[] snap(Rect d, int rawX, int rawY, List<Rect> others) {
        guidesX.clear();
        guidesY.clear();
        int sw = width, sh = height;
        int x = rawX, y = rawY;
        boolean fixed = false;

        for (Rect o : others) {
            boolean near = rawX <= o.x() + o.w() + TOUCH && rawX + d.w() >= o.x() - TOUCH
                    && rawY <= o.y() + o.h() + TOUCH && rawY + d.h() >= o.y() - TOUCH;
            if (!near) continue;

            int dRight = Math.abs(rawX - (o.x() + o.w()));
            int dLeft = Math.abs(o.x() - (rawX + d.w()));
            int dBelow = Math.abs(rawY - (o.y() + o.h()));
            int dAbove = Math.abs(o.y() - (rawY + d.h()));

            if (Math.min(dRight, dLeft) <= Math.min(dBelow, dAbove)) {
                boolean right = dRight <= dLeft;
                x = right ? o.x() + o.w() + GAP : o.x() - d.w() - GAP;
                guidesX.add(right ? o.x() + o.w() : o.x());
                int[] cand = {o.y(), o.y() + (o.h() - d.h()) / 2, o.y() + o.h() - d.h()};
                int i = closest(rawY, cand);
                y = cand[i];
                guidesY.add(i == 0 ? o.y() : i == 1 ? o.y() + o.h() / 2 : o.y() + o.h());
            } else {
                boolean below = dBelow <= dAbove;
                y = below ? o.y() + o.h() + GAP : o.y() - d.h() - GAP;
                guidesY.add(below ? o.y() + o.h() : o.y());
                int[] cand = {o.x(), o.x() + (o.w() - d.w()) / 2, o.x() + o.w() - d.w()};
                int i = closest(rawX, cand);
                x = cand[i];
                guidesX.add(i == 0 ? o.x() : i == 1 ? o.x() + o.w() / 2 : o.x() + o.w());
            }
            fixed = true;
            break;
        }

        if (!fixed) {
            List<Integer> tx = new ArrayList<>(List.of(0, sw / 2, sw));
            List<Integer> ty = new ArrayList<>(List.of(0, sh / 2, sh));
            for (Rect o : others) {
                tx.add(o.x());
                tx.add(o.x() + o.w() / 2);
                tx.add(o.x() + o.w());
                ty.add(o.y());
                ty.add(o.y() + o.h() / 2);
                ty.add(o.y() + o.h());
            }
            int[] ax = alignAxis(rawX, d.w(), tx);
            int[] ay = alignAxis(rawY, d.h(), ty);
            if (ax != null) {
                x = ax[0];
                guidesX.add(ax[1]);
            }
            if (ay != null) {
                y = ay[0];
                guidesY.add(ay[1]);
            }
        }

        return new int[]{clamp(x, sw - d.w()), clamp(y, sh - d.h())};
    }

    private static int[] alignAxis(int raw, int size, List<Integer> targets) {
        int[] mine = {0, size / 2, size};
        int bestDelta = Integer.MAX_VALUE, bestTarget = 0;
        for (int t : targets)
            for (int m : mine) {
                int delta = t - (raw + m);
                if (Math.abs(delta) <= SNAP && Math.abs(delta) < Math.abs(bestDelta)) {
                    bestDelta = delta;
                    bestTarget = t;
                }
            }
        return bestDelta == Integer.MAX_VALUE ? null : new int[]{raw + bestDelta, bestTarget};
    }

    private static int closest(int value, int[] cand) {
        int best = 0;
        for (int i = 1; i < cand.length; i++)
            if (Math.abs(cand[i] - value) < Math.abs(cand[best] - value)) best = i;
        return best;
    }
}
