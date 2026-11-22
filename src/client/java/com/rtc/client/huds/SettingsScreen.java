package com.rtc.client.huds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

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

public abstract class SettingsScreen extends Screen {
    private static final int MARGIN = 8;
    private static final int HEADER_H = 28;
    private static final int PREVIEW_W = 150;
    private static final int PREVIEW_MIN_H = 120;
    private static final int GAP = 10;
    private static final int SCROLLBAR_W = 3;
    private static final int SCROLL_STEP = 18;
    private static final int BACK_W = 9, BACK_H = 10;

    private static final int ROW_H = UiStyle.ROW_H_SMALL;
    private static final int BOX_H = UiStyle.ROW_H_SMALL - UiStyle.ROW_GAP;
    private static final int SLIDER_H = 8;

    private final Screen parent;
    private final List<Row> rows = new ArrayList<>();
    private Row active;
    private int scroll;

    private int px, py, pw, ph;
    private int lx, ly, lw, lh;
    private int contentH;
    private boolean previewShown;
    private int prx, pry, prw, prh;

    protected SettingsScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
    }

    protected abstract void buildRows(List<Row> out);

    protected int listWidth() {
        return 300;
    }

    protected boolean hasPreview() {
        return false;
    }

    protected void drawPreview(HudCanvas c, int x, int y, int w, int h) {
    }

    @Override
    protected void init() {
        rows.clear();
        active = null;
        buildRows(rows);
        layout();
    }

    @Override
    public void onClose() {
        goBack();
    }

    protected void goBack() {
//? if >=26.2 {
        Minecraft.getInstance().gui.setScreen(parent);
//?} else {
        /*        Minecraft.getInstance().setScreen(parent);
         */
//?}
    }

    protected void open(Screen screen) {
//? if >=26.2 {
        Minecraft.getInstance().gui.setScreen(screen);
//?} else {
        /*        Minecraft.getInstance().setScreen(screen);
         */
//?}
    }

    private void layout() {
        previewShown = hasPreview()
                && width - 2 * MARGIN >= listWidth() + 2 * UiStyle.PADDING + GAP + PREVIEW_W;
        int extra = previewShown ? GAP + PREVIEW_W : 0;
        pw = Math.min(listWidth() + 2 * UiStyle.PADDING + extra, width - 2 * MARGIN);
        int listW = pw - 2 * UiStyle.PADDING - extra;

        contentH = 0;
        for (Row r : rows) contentH += r.h();

        int desired = HEADER_H + contentH + UiStyle.PADDING;
        if (previewShown) desired = Math.max(desired, HEADER_H + PREVIEW_MIN_H + UiStyle.PADDING);
        ph = Math.min(desired, height - 2 * MARGIN);
        px = (width - pw) / 2;
        py = (height - ph) / 2;

        lx = px + UiStyle.PADDING;
        ly = py + HEADER_H;
        lh = ph - HEADER_H - UiStyle.PADDING;
        int maxScroll = Math.max(0, contentH - lh);
        scroll = Math.max(0, Math.min(scroll, maxScroll));
        lw = listW;
        int rowW = maxScroll > 0 ? lw - SCROLLBAR_W - 3 : lw;

        int cy = ly - scroll;
        for (Row r : rows) {
            r.x = lx;
            r.y = cy;
            r.w = rowW;
            r.arrange();
            boolean fullyVisible = cy >= ly && cy + r.h() <= ly + lh;
            r.place(fullyVisible);
            cy += r.h();
        }

        prx = lx + lw + GAP;
        pry = ly;
        prw = PREVIEW_W;
        prh = lh;
    }

    private boolean inView(double my) {
        return my >= ly && my <= ly + lh;
    }

    private void drawAll(HudCanvas c, int mx, int my) {
        layout();
        c.rect(0, 0, width, height, UiStyle.DIM);
        UiStyle.drawPanelFrame(c, px, py, pw, ph);
        c.plainIcon("cancel", px + UiStyle.PADDING, py + 10, BACK_W, BACK_H);
        c.text(title, px + UiStyle.PADDING + BACK_W + 6, py + 10, 0xFFFFFFFF, true);

        c.clip(lx, ly, lw, lh);
        for (Row r : rows) {
            if (r.y + r.h() < ly || r.y > ly + lh) continue;
            r.draw(c, mx, my);
        }
        c.endClip();

        int maxScroll = Math.max(0, contentH - lh);
        if (maxScroll > 0) {
            int trackX = lx + lw - SCROLLBAR_W;
            c.rect(trackX, ly, trackX + SCROLLBAR_W, ly + lh, 0x33FFFFFF);
            int barH = Math.max(12, lh * lh / contentH);
            int barY = ly + (int) ((long) (lh - barH) * scroll / maxScroll);
            c.rect(trackX, barY, trackX + SCROLLBAR_W, barY + barH, UiStyle.currentAccent());
        }

        if (previewShown) {
            c.rect(prx, pry, prx + prw, pry + prh, UiStyle.PREVIEW_FRAME);
            c.border(prx, pry, prw, prh, UiStyle.BORDER);
            Component caption = Component.translatable("rthuds.ui.preview");
            c.text(caption, prx + 6, pry + 5, UiStyle.TEXT_DIM, true);
            int ix = prx + 4, iy = pry + 18, iw = prw - 8, ih = prh - 22;
            c.rect(ix, iy, ix + iw, iy + ih, UiStyle.PREVIEW_BG);
            c.clip(ix, iy, iw, ih);
            drawPreview(c, ix, iy, iw, ih);
            c.endClip();
        }
    }

    //? if >=26.1.2 {
    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor g, int mx, int my, float delta) {
        drawAll(new HudCanvas(g), mx, my);
        super.extractRenderState(g, mx, my, delta);
    }
//?} else {
/*    @Override
    public void render(GuiGraphics g, int mx, int my, float delta) {
        drawAll(new HudCanvas(g), mx, my);
        super.render(g, mx, my, delta);
    }
 */
//?}

    private boolean onPress(double mx, double my, int button) {
        if (!VersionCompat.isLeftClick(button)) return false;
        layout();
        if (UiStyle.hit(mx, my, px + UiStyle.PADDING - 2, py + 8, BACK_W + 6, BACK_H + 6)) {
            goBack();
            return true;
        }
        if (!inView(my) || mx < lx || mx > lx + lw) return false;
        for (Row r : rows) {
            if (r.press(mx, my)) {
                active = r;
                return true;
            }
        }
        return false;
    }

    private boolean onDrag(double mx) {
        if (active == null) return false;
        active.drag(mx);
        return true;
    }

    private boolean onRelease() {
        if (active == null) return false;
        active.release();
        active = null;
        return true;
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double scrollX, double scrollY) {
        if (contentH > lh && mx >= lx && mx <= lx + lw && inView(my)) {
            scroll -= (int) Math.round(scrollY * SCROLL_STEP);
            layout();
            return true;
        }
        return super.mouseScrolled(mx, my, scrollX, scrollY);
    }

    //? if >=1.21.9 {
    @Override
    public boolean mouseClicked(@NonNull MouseButtonEvent e, boolean doubleClick) {
        return onPress(e.x(), e.y(), e.button()) || super.mouseClicked(e, doubleClick);
    }

    @Override
    public boolean mouseDragged(@NonNull MouseButtonEvent e, double dx, double dy) {
        return onDrag(e.x()) || super.mouseDragged(e, dx, dy);
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
        return onDrag(mx) || super.mouseDragged(mx, my, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        return onRelease() || super.mouseReleased(mx, my, button);
    }
 */
//?}

    protected abstract class Row {
        int x, y, w;

        abstract int h();

        abstract void draw(HudCanvas c, int mx, int my);

        void arrange() {
        }

        void place(boolean visible) {
        }

        boolean press(double mx, double my) {
            return false;
        }

        void drag(double mx) {
        }

        void release() {
        }

        boolean over(double mx, double my) {
            return inView(my) && UiStyle.hit(mx, my, x, y, w, BOX_H);
        }

        int centerY(int contentH) {
            return y + UiStyle.centerOffset(BOX_H, contentH);
        }
    }

    protected final class TitleRow extends Row {
        private final Component text;

        protected TitleRow(Component text) {
            this.text = text;
        }

        @Override
        int h() {
            return 18;
        }

        @Override
        void draw(HudCanvas c, int mx, int my) {
            c.text(text, x + 2, y + 6, UiStyle.TEXT_DIM, true);
            c.rect(x + c.textWidth(text) + 8, y + 10, x + w, y + 11, UiStyle.BORDER);
        }
    }

    protected final class ToggleRow extends Row {
        private final Component label;
        private final BooleanSupplier get;
        private final Consumer<Boolean> set;

        protected ToggleRow(Component label, BooleanSupplier get, Consumer<Boolean> set) {
            this.label = label;
            this.get = get;
            this.set = set;
        }

        @Override
        int h() {
            return ROW_H;
        }

        @Override
        void draw(HudCanvas c, int mx, int my) {
            boolean hover = over(mx, my);
            UiStyle.drawRow(c, x, y, w, BOX_H, hover);
            c.text(label, x + 6, centerY(UiStyle.FONT_H), UiStyle.TEXT, true);
            int tx = x + w - UiStyle.TOGGLE_W_SMALL - 4;
            UiStyle.drawToggle(c, tx, centerY(UiStyle.TOGGLE_H_SMALL), UiStyle.TOGGLE_W_SMALL, UiStyle.TOGGLE_H_SMALL, get.getAsBoolean(), hover);
        }

        @Override
        boolean press(double mx, double my) {
            if (!over(mx, my)) return false;
            set.accept(!get.getAsBoolean());
            HudRegistry.save();
            return true;
        }
    }

    protected final class CycleRow extends Row {
        private final Component label;
        private final Supplier<Component> value;
        private final Runnable next;

        protected CycleRow(Component label, Supplier<Component> value, Runnable next) {
            this.label = label;
            this.value = value;
            this.next = next;
        }

        @Override
        int h() {
            return ROW_H;
        }

        @Override
        void draw(HudCanvas c, int mx, int my) {
            UiStyle.drawRow(c, x, y, w, BOX_H, over(mx, my));
            c.text(label, x + 6, centerY(UiStyle.FONT_H), UiStyle.TEXT, true);
            Component v = Component.literal("< ").append(value.get()).append(" >");
            c.text(v, x + w - c.textWidth(v) - 6, centerY(UiStyle.FONT_H), UiStyle.currentAccent(), true);
        }

        @Override
        boolean press(double mx, double my) {
            if (!over(mx, my)) return false;
            next.run();
            HudRegistry.save();
            return true;
        }
    }

    protected final class StepSliderRow extends Row {
        private final Component label;
        private final int min, max;
        private final IntSupplier get;
        private final IntConsumer set;
        private final IntFunction<Component> display;

        protected StepSliderRow(Component label, int min, int max, IntSupplier get, IntConsumer set, IntFunction<Component> display) {
            this.label = label;
            this.min = min;
            this.max = max;
            this.get = get;
            this.set = set;
            this.display = display;
        }

        @Override
        int h() {
            return ROW_H + 14;
        }

        private int trackX() {
            return x + 6;
        }

        private int trackW() {
            return w - 12;
        }

        private int trackY() {
            return y + BOX_H + 3;
        }

        @Override
        void draw(HudCanvas c, int mx, int my) {
            UiStyle.drawRow(c, x, y, w, BOX_H, false);
            c.text(label, x + 6, centerY(UiStyle.FONT_H), UiStyle.TEXT, true);
            Component v = display.apply(get.getAsInt());
            c.text(v, x + w - c.textWidth(v) - 6, centerY(UiStyle.FONT_H), UiStyle.currentAccent(), true);
            UiStyle.drawColorSlider(c, trackX(), trackY() + 3, trackW(), SLIDER_H,
                    (get.getAsInt() - min) * 255 / Math.max(1, max - min), UiStyle.currentAccent());
        }

        @Override
        boolean press(double mx, double my) {
            if (!inView(my) || !UiStyle.hit(mx, my, trackX() - 2, trackY(), trackW() + 4, 16)) return false;
            drag(mx);
            return true;
        }

        @Override
        void drag(double mx) {
            int v = min + (int) Math.round((mx - trackX()) / (double) trackW() * (max - min));
            set.accept(Math.max(min, Math.min(max, v)));
        }

        @Override
        void release() {
            HudRegistry.save();
        }
    }

    protected final class ColorRow extends Row {
        private static final int BODY_H = 86;
        private static final int ALPHA_EXTRA = 18;
        private static final int SWATCH = 16;
        private static final int HEX_W = 70;
        private static final int CHROMA_H = 16;

        private final Component label;
        private final IntSupplier get;
        private final IntConsumer set;
        private final BooleanSupplier chroma;
        private final Consumer<Boolean> setChroma;
        private final boolean alpha;
        private final EditBox hex;
        private boolean open;
        private boolean suppress;
        private int dragChannel = -1;

        protected ColorRow(Component label, IntSupplier get, IntConsumer set, BooleanSupplier chroma, Consumer<Boolean> setChroma) {
            this(label, get, set, chroma, setChroma, false);
        }

        protected ColorRow(Component label, IntSupplier get, IntConsumer set, BooleanSupplier chroma,
                           Consumer<Boolean> setChroma, boolean alpha) {
            this.label = label;
            this.get = get;
            this.set = set;
            this.chroma = chroma;
            this.setChroma = setChroma;
            this.alpha = alpha;
            this.hex = new EditBox(font, 0, 0, alpha ? HEX_W + 14 : HEX_W, 16, Component.translatable("rthuds.ui.hex"));
            hex.setMaxLength(alpha ? 8 : 6);
            hex.setValue(toHex(get.getAsInt()));
            hex.setResponder(this::onHexTyped);
            hex.setVisible(false);
            addRenderableWidget(hex);
        }

        private String toHex(int argb) {
            return alpha
                    ? String.format("%06X%02X", argb & 0xFFFFFF, (argb >>> 24) & 0xFF)
                    : String.format("%06X", argb & 0xFFFFFF);
        }

        private void onHexTyped(String raw) {
            if (suppress) return;
            int max = alpha ? 8 : 6;
            String cleaned = raw.replaceAll("[^0-9a-fA-F]", "");
            if (cleaned.length() > max) cleaned = cleaned.substring(0, max);
            if (!cleaned.equals(raw)) {
                suppress = true;
                int cursor = hex.getCursorPosition();
                hex.setValue(cleaned);
                hex.setCursorPosition(Math.min(cursor, cleaned.length()));
                suppress = false;
            }
            if (cleaned.length() == 6) {
                int rgb = Integer.parseInt(cleaned, 16);
                int a = alpha ? (get.getAsInt() >>> 24) & 0xFF : 0xFF;
                set.accept((a << 24) | rgb);
                HudRegistry.save();
            } else if (alpha && cleaned.length() == 8) {
                long v = Long.parseLong(cleaned, 16);
                set.accept((int) (((v & 0xFF) << 24) | (v >>> 8)));
                HudRegistry.save();
            }
        }

        @Override
        int h() {
            return open ? ROW_H + BODY_H + (alpha ? ALPHA_EXTRA : 0) : ROW_H;
        }

        private int channels() {
            return alpha ? 4 : 3;
        }

        private int bodyY() {
            return y + ROW_H;
        }

        private int chromaX() {
            return x + 6 + SWATCH + 8 + hex.getWidth() + 8;
        }

        private int trackX() {
            return x + 6 + 14;
        }

        private int trackW() {
            return Math.max(40, Math.min(180, w - 6 - 14 - 34));
        }

        private int sliderY(int i) {
            return bodyY() + 30 + i * 18;
        }

        @Override
        void place(boolean visible) {
            hex.setX(x + 6 + SWATCH + 8);
            hex.setY(bodyY() + 4);
            hex.setVisible(open && visible);
        }

        private void swatch(HudCanvas c, int sx, int sy, int size, int argb) {
            if (alpha) c.rect(sx, sy, sx + size, sy + size, 0xFF808080);
            c.rect(sx, sy, sx + size, sy + size, argb);
            c.border(sx, sy, size, size, UiStyle.BORDER);
        }

        @Override
        void draw(HudCanvas c, int mx, int my) {
            int color = get.getAsInt();
            int shown = chroma != null && chroma.getAsBoolean() ? ChromaUtil.color() : color;
            UiStyle.drawRow(c, x, y, w, BOX_H, inView(my) && UiStyle.hit(mx, my, x, y, w, BOX_H));
            c.text(Component.literal(open ? "- " : "+ ").append(label), x + 6, centerY(UiStyle.FONT_H), UiStyle.TEXT, true);
            swatch(c, x + w - 6 - 12, y + 3, 12, shown);
            if (!open) return;

            int by = bodyY();
            swatch(c, x + 6, by + 4, SWATCH, shown);
            if (chroma != null) {
                UiStyle.drawChromaButton(c, chromaX(), by + 4, CHROMA_H, chroma.getAsBoolean(),
                        UiStyle.hit(mx, my, chromaX(), by + 4, UiStyle.chromaButtonWidth(c), CHROMA_H));
            }
            String[] names = {"R", "G", "B", "A"};
            int[] fills = {0xFFFF5555, 0xFF55FF55, 0xFF5577FF, 0xFFCCCCCC};
            int[] vals = {(color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >>> 24) & 0xFF};
            for (int i = 0; i < channels(); i++) {
                int sy = sliderY(i);
                c.text(names[i], x + 6, sy, UiStyle.TEXT_DIM, true);
                UiStyle.drawColorSlider(c, trackX(), sy, trackW(), SLIDER_H, vals[i], fills[i]);
                c.textScaled(String.valueOf(vals[i]), trackX() + trackW() + 8, sy - 1, UiStyle.TEXT, true, 0.8f);
            }
        }

        @Override
        boolean press(double mx, double my) {
            if (!inView(my)) return false;
            if (UiStyle.hit(mx, my, x, y, w, BOX_H)) {
                open = !open;
                return true;
            }
            if (!open) return false;
            if (chroma != null && UiStyle.hit(mx, my, chromaX(), bodyY() + 4, UiStyle.chromaButtonWidth(), CHROMA_H)) {
                setChroma.accept(!chroma.getAsBoolean());
                HudRegistry.save();
                return true;
            }
            for (int i = 0; i < channels(); i++) {
                if (UiStyle.hit(mx, my, trackX() - 2, sliderY(i) - 3, trackW() + 4, SLIDER_H + 6)) {
                    dragChannel = i;
                    drag(mx);
                    return true;
                }
            }
            return false;
        }

        @Override
        void drag(double mx) {
            if (dragChannel < 0) return;
            int v = Math.max(0, Math.min(255, (int) Math.round((mx - trackX()) / (double) trackW() * 255)));
            int color = get.getAsInt();
            int a = alpha ? (color >>> 24) & 0xFF : 0xFF;
            int r = (color >> 16) & 0xFF, g = (color >> 8) & 0xFF, b = color & 0xFF;
            switch (dragChannel) {
                case 0 -> r = v;
                case 1 -> g = v;
                case 2 -> b = v;
                default -> a = v;
            }
            int argb = (a << 24) | (r << 16) | (g << 8) | b;
            set.accept(argb);
            suppress = true;
            hex.setValue(toHex(argb));
            suppress = false;
        }

        @Override
        void release() {
            dragChannel = -1;
            HudRegistry.save();
        }
    }

    protected final class HudRow extends Row {
        private final Component title;
        private final BooleanSupplier enabled;
        private final Runnable toggle;
        private final Runnable openSettings;
        private final Runnable reset;

        protected HudRow(Component title, BooleanSupplier enabled, Runnable toggle, Runnable openSettings, Runnable reset) {
            this.title = title;
            this.enabled = enabled;
            this.toggle = toggle;
            this.openSettings = openSettings;
            this.reset = reset;
        }

        @Override
        int h() {
            return ROW_H;
        }

        private int toggleX() {
            return x + w - UiStyle.TOGGLE_W_SMALL - 4;
        }

        private int gearX() {
            return toggleX() - UiStyle.ICON_BTN_SMALL - 5;
        }

        private int resetX() {
            return gearX() - UiStyle.ICON_BTN_SMALL - 3;
        }

        private int gearY() {
            return centerY(UiStyle.ICON_BTN_SMALL);
        }

        @Override
        void draw(HudCanvas c, int mx, int my) {
            UiStyle.drawRow(c, x, y, w, BOX_H, over(mx, my));
            c.clip(x + 4, y, Math.max(0, resetX() - x - 8), BOX_H);
            c.text(title, x + 6, centerY(UiStyle.FONT_H), UiStyle.TEXT, true);
            c.endClip();

            boolean hoverReset = inView(my) && UiStyle.hit(mx, my, resetX(), gearY(), UiStyle.ICON_BTN_SMALL, UiStyle.ICON_BTN_SMALL);
            UiStyle.drawIconButtonFrame(c, resetX(), gearY(), UiStyle.ICON_BTN_SMALL, hoverReset);
            int resetIcon = 9;
            int resetOff = UiStyle.centerOffset(UiStyle.ICON_BTN_SMALL, resetIcon);
            c.icon("reset", resetX() + resetOff, gearY() + resetOff, resetIcon, resetIcon);

            boolean hoverGear = inView(my) && UiStyle.hit(mx, my, gearX(), gearY(), UiStyle.ICON_BTN_SMALL, UiStyle.ICON_BTN_SMALL);
            UiStyle.drawIconButtonFrame(c, gearX(), gearY(), UiStyle.ICON_BTN_SMALL, hoverGear);
            int icon = 9;
            int off = UiStyle.centerOffset(UiStyle.ICON_BTN_SMALL, icon);
            c.icon("settings", gearX() + off, gearY() + off + 1, icon, icon);

            boolean hoverToggle = inView(my) && UiStyle.hit(mx, my, toggleX(), centerY(UiStyle.TOGGLE_H_SMALL), UiStyle.TOGGLE_W_SMALL, UiStyle.TOGGLE_H_SMALL);
            UiStyle.drawToggle(c, toggleX(), centerY(UiStyle.TOGGLE_H_SMALL), UiStyle.TOGGLE_W_SMALL, UiStyle.TOGGLE_H_SMALL, enabled.getAsBoolean(), hoverToggle);
        }

        @Override
        boolean press(double mx, double my) {
            if (!inView(my)) return false;
            if (UiStyle.hit(mx, my, resetX(), gearY(), UiStyle.ICON_BTN_SMALL, UiStyle.ICON_BTN_SMALL)) {
                reset.run();
                HudRegistry.save();
                return true;
            }
            if (UiStyle.hit(mx, my, gearX(), gearY(), UiStyle.ICON_BTN_SMALL, UiStyle.ICON_BTN_SMALL)) {
                openSettings.run();
                return true;
            }
            if (UiStyle.hit(mx, my, toggleX(), centerY(UiStyle.TOGGLE_H_SMALL), UiStyle.TOGGLE_W_SMALL, UiStyle.TOGGLE_H_SMALL)) {
                toggle.run();
                HudRegistry.save();
                return true;
            }
            return false;
        }
    }

    protected final class PairRow extends Row {
        private final Row a, b;

        protected PairRow(Row a, Row b) {
            this.a = a;
            this.b = b;
        }

        @Override
        int h() {
            return ROW_H;
        }

        @Override
        void arrange() {
            int half = (w - 6) / 2;
            a.x = x;
            a.y = y;
            a.w = half;
            a.arrange();
            if (b != null) {
                b.x = x + half + 6;
                b.y = y;
                b.w = half;
                b.arrange();
            }
        }

        @Override
        void place(boolean visible) {
            a.place(visible);
            if (b != null) b.place(visible);
        }

        @Override
        void draw(HudCanvas c, int mx, int my) {
            a.draw(c, mx, my);
            if (b != null) b.draw(c, mx, my);
        }

        @Override
        boolean press(double mx, double my) {
            return a.press(mx, my) || (b != null && b.press(mx, my));
        }
    }
}
