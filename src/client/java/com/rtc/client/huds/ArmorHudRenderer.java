package com.rtc.client.huds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class ArmorHudRenderer {
    private ArmorHudRenderer() {
    }

    private static final int BOTTOM_SCREEN_MARGIN = 6;
    private static final int HOTBAR_GAP = 8;
    private static final int DEFAULT_RIGHT_MARGIN = 20;

    public enum Slot {MAINHAND, HELMET, CHEST, OFFHAND, LEGS, BOOTS}

    public static int icon() {
        return Math.round(16 * ArmorHudConfig.scale);
    }

    private static int bgPad() {
        return switch (ArmorHudConfig.bgStyle) {
            case NONE -> 0;
            case GLASS -> 1;
            case HOTBAR -> 2;
            case NEON -> 3;
        };
    }

    public static int gap() {
        return Math.max(1, Math.round((2 + bgPad()) * ArmorHudConfig.scale));
    }

    private static int textGap() {
        return Math.max(2, Math.round((3 + bgPad()) * ArmorHudConfig.scale));
    }

    private static int textHeight() {
        return Math.round(Minecraft.getInstance().font.lineHeight * ArmorHudConfig.scale);
    }

    private static int textWidth(String s) {
        return Math.round(Minecraft.getInstance().font.width(s) * ArmorHudConfig.scale);
    }

    private static boolean isVisible(Slot s) {
        return switch (s) {
            case HELMET -> ArmorHudConfig.showHelmet;
            case CHEST -> ArmorHudConfig.showChest;
            case LEGS -> ArmorHudConfig.showLegs;
            case BOOTS -> ArmorHudConfig.showBoots;
            case MAINHAND -> ArmorHudConfig.showMainHand;
            case OFFHAND -> ArmorHudConfig.showOffHand;
        };
    }

    private static ItemStack stackFor(LocalPlayer p, Slot s) {
        if (p == null) return ItemStack.EMPTY;
        return switch (s) {
            case HELMET -> p.getItemBySlot(EquipmentSlot.HEAD);
            case CHEST -> p.getItemBySlot(EquipmentSlot.CHEST);
            case LEGS -> p.getItemBySlot(EquipmentSlot.LEGS);
            case BOOTS -> p.getItemBySlot(EquipmentSlot.FEET);
            case MAINHAND -> p.getMainHandItem();
            case OFFHAND -> p.getOffhandItem();
        };
    }

    private static int textureIndex(Slot s) {
        return switch (s) {
            case HELMET -> 0;
            case CHEST -> 1;
            case LEGS -> 2;
            case BOOTS -> 3;
            case MAINHAND -> 4;
            case OFFHAND -> 5;
        };
    }

    private static Object emptyTexture(Slot s) {
        var arr = ArmorHudConfig.whiteEmptyIcons ? ArmorHudTextures.WHITE_EMPTY_TEXTURES : ArmorHudTextures.BLACK_EMPTY_TEXTURES;
        return arr[textureIndex(s)];
    }

    private static String healthText(ItemStack stack) {
        if (ArmorHudConfig.healthPos == ArmorHudConfig.HealthPos.NONE || stack.isEmpty() || !stack.isDamageableItem()) {
            return null;
        }
        int max = stack.getMaxDamage();
        if (max <= 0) return null;
        int dmg = stack.getDamageValue();
        int remaining = max - dmg;
        return switch (ArmorHudConfig.healthMode) {
            case REMAINING -> String.valueOf(remaining);
            case DAMAGE -> String.valueOf(dmg);
            case PERCENTAGE -> Math.round((remaining / (float) max) * 100) + "%";
            case REMAINING_MAX -> remaining + "/" + max;
        };
    }

    private static final Slot[] FREE_ORDER = {Slot.HELMET, Slot.CHEST, Slot.LEGS, Slot.BOOTS, Slot.MAINHAND, Slot.OFFHAND};

    public static List<Slot> visibleSlotsFreeOrder() {
        LocalPlayer p = Minecraft.getInstance().player;
        List<Slot> out = new ArrayList<>();
        for (Slot s : FREE_ORDER) if (shown(s, p)) out.add(s);
        return out;
    }

    private static boolean shown(Slot s, LocalPlayer p) {
        return isVisible(s) && (ArmorHudConfig.showEmptySlots || p == null || !stackFor(p, s).isEmpty());
    }

    public static int hotbarLeft(int screenW) {
        return screenW / 2 - 91;
    }

    public static int hotbarRight(int screenW) {
        return screenW / 2 + 91;
    }

    public static int hotbarTop(int screenH) {
        return screenH - 22;
    }

    public static int hotbarBottom(int screenH) {
        return screenH;
    }

    public static boolean nearHotbar(Layout box, int screenW, int screenH) {
        int left = hotbarLeft(screenW) - 40;
        int right = hotbarRight(screenW) + 40;
        int top = hotbarTop(screenH) - 10;
        return box.y + box.h >= top && box.x <= right && box.x + box.w >= left;
    }

    public static final class Placed {
        public final Slot slot;
        public int x, y;
        public final String text;
        public int textX, textY;
        final ItemStack stack;

        Placed(Slot slot, ItemStack stack, String text) {
            this.slot = slot;
            this.stack = stack;
            this.text = text;
        }
    }

    public static final class Layout {
        public int x, y, w, h;
        public boolean snapped;
        public final List<Placed> placed = new ArrayList<>();
        public final List<int[]> panels = new ArrayList<>();

        void translate(int dx, int dy) {
            x += dx;
            y += dy;
            for (int[] r : panels) {
                r[0] += dx;
                r[1] += dy;
            }
            for (Placed p : placed) {
                p.x += dx;
                p.y += dy;
                p.textX += dx;
                p.textY += dy;
            }
        }
    }

    public static Layout computeLayout(int screenW, int screenH) {
        if (ArmorHudConfig.hotbarSnapped) {
            return computeHotbarLayout(screenW, screenH);
        }
        boolean defaultX = ArmorHudConfig.x == ArmorHudConfig.DEFAULT_POS;
        boolean defaultY = ArmorHudConfig.y == ArmorHudConfig.DEFAULT_POS;
        Layout L = computeFreeLayout(defaultX ? 0 : ArmorHudConfig.x, defaultY ? 0 : ArmorHudConfig.y, screenW, screenH);
        if (defaultX || defaultY) {
            L.translate(defaultX ? screenW - L.w - DEFAULT_RIGHT_MARGIN - L.x : 0,
                    defaultY ? (screenH - L.h) / 2 - L.y : 0);
        }
        return L;
    }

    public static Layout computeFreeLayout(int ax, int ay, int screenW, int screenH) {
        Layout L = new Layout();
        boolean vertical = ArmorHudConfig.orientation == ArmorHudConfig.Orientation.VERTICAL;
        flow(L, visibleSlotsFreeOrder(), vertical, ArmorHudConfig.healthPos);
        L.translate(clamp(ax, screenW - L.w), clamp(ay, screenH - L.h));
        return L;
    }

    private static int clamp(int v, int max) {
        return Math.max(0, Math.min(v, Math.max(0, max)));
    }

    private static Layout computeHotbarLayout(int screenW, int screenH) {
        Layout L = new Layout();
        L.snapped = true;
        int hbLeft = hotbarLeft(screenW);
        int hbRight = hotbarRight(screenW);
        int hbBottom = hotbarBottom(screenH) - BOTTOM_SCREEN_MARGIN;
        int gap = Math.max(1, Math.round(HOTBAR_GAP * ArmorHudConfig.scale));

        LocalPlayer player = Minecraft.getInstance().player;
        List<Slot> left = new ArrayList<>();
        if (shown(Slot.MAINHAND, player)) left.add(Slot.MAINHAND);
        if (shown(Slot.HELMET, player)) left.add(Slot.HELMET);
        if (shown(Slot.CHEST, player)) left.add(Slot.CHEST);
        List<Slot> right = new ArrayList<>();
        if (shown(Slot.OFFHAND, player)) right.add(Slot.OFFHAND);
        if (shown(Slot.LEGS, player)) right.add(Slot.LEGS);
        if (shown(Slot.BOOTS, player)) right.add(Slot.BOOTS);

        var pos = ArmorHudConfig.healthPos;
        boolean side = pos == ArmorHudConfig.HealthPos.LEFT || pos == ArmorHudConfig.HealthPos.RIGHT;
        var leftPos = side ? ArmorHudConfig.HealthPos.LEFT : pos;
        var rightPos = side ? ArmorHudConfig.HealthPos.RIGHT : pos;

        Layout lc = new Layout();
        flow(lc, left, true, leftPos);
        lc.translate(hbLeft - gap - lc.w, hbBottom - lc.h);
        Layout rc = new Layout();
        flow(rc, right, true, rightPos);
        rc.translate(hbRight + gap, hbBottom - rc.h);

        List<Layout> parts = new ArrayList<>();
        if (!left.isEmpty()) parts.add(lc);
        if (!right.isEmpty()) parts.add(rc);
        if (parts.isEmpty()) {
            L.x = hbLeft - gap - icon();
            L.y = hbBottom - icon();
            L.w = icon();
            L.h = icon();
            return L;
        }
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
        for (Layout part : parts) {
            L.placed.addAll(part.placed);
            L.panels.addAll(part.panels);
            minX = Math.min(minX, part.x);
            minY = Math.min(minY, part.y);
            maxX = Math.max(maxX, part.x + part.w);
            maxY = Math.max(maxY, part.y + part.h);
        }
        L.x = minX;
        L.y = minY;
        L.w = maxX - minX;
        L.h = maxY - minY;
        return L;
    }

    private static void flow(Layout L, List<Slot> slots, boolean vertical, ArmorHudConfig.HealthPos pos) {
        LocalPlayer player = Minecraft.getInstance().player;
        int I = icon(), G = gap(), TG = textGap(), TH = textHeight();

        List<Placed> cells = new ArrayList<>();
        int maxTw = 0;
        for (Slot s : slots) {
            ItemStack st = stackFor(player, s);
            String text = healthText(st);
            cells.add(new Placed(s, st, text));
            if (text != null) maxTw = Math.max(maxTw, textWidth(text));
        }
        boolean hasText = maxTw > 0 && pos != ArmorHudConfig.HealthPos.NONE;

        int cellW = I, cellH = I, iconX = 0, iconY = 0;
        if (hasText) {
            switch (pos) {
                case TOP -> {
                    cellW = Math.max(I, maxTw);
                    cellH = TH + TG + I;
                    iconX = (cellW - I) / 2;
                    iconY = TH + TG;
                }
                case BOTTOM -> {
                    cellW = Math.max(I, maxTw);
                    cellH = I + TG + TH;
                    iconX = (cellW - I) / 2;
                }
                case LEFT -> {
                    cellW = maxTw + TG + I;
                    iconX = maxTw + TG;
                }
                case RIGHT -> cellW = I + TG + maxTw;
                default -> {
                }
            }
        }

        int cursor = 0;
        for (Placed c : cells) {
            int ox = vertical ? 0 : cursor;
            int oy = vertical ? cursor : 0;
            c.x = ox + iconX;
            c.y = oy + iconY;
            if (c.text != null && hasText) {
                int tw = textWidth(c.text);
                switch (pos) {
                    case TOP -> {
                        c.textX = ox + (cellW - tw) / 2;
                        c.textY = oy;
                    }
                    case BOTTOM -> {
                        c.textX = ox + (cellW - tw) / 2;
                        c.textY = oy + I + TG;
                    }
                    case LEFT -> {
                        c.textX = ox + maxTw - tw;
                        c.textY = oy + (I - TH) / 2;
                    }
                    case RIGHT -> {
                        c.textX = ox + I + TG;
                        c.textY = oy + (I - TH) / 2;
                    }
                    default -> {
                    }
                }
            }
            L.placed.add(c);
            cursor += (vertical ? cellH : cellW) + G;
        }
        int count = cells.size();
        int span = count == 0 ? 0 : cursor - G;
        L.x = 0;
        L.y = 0;
        L.w = count == 0 ? I : (vertical ? cellW : span);
        L.h = count == 0 ? I : (vertical ? span : cellH);
        if (count > 0) L.panels.add(new int[]{0, 0, L.w, L.h});
    }

    public static void render(HudCanvas c, Minecraft mc) {
        if (!ArmorHudConfig.enabled) return;
        if (mc.player == null) return;
        draw(c, mc, computeLayout(mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight()));
    }

    public static void draw(HudCanvas c, Minecraft mc, Layout layout) {
        LocalPlayer p = mc.player;
        if (ArmorHudConfig.showPanel) {
            int pad = Math.max(2, Math.round(3 * ArmorHudConfig.scale));
            for (int[] r : layout.panels) {
                c.rect(r[0] - pad, r[1] - pad, r[0] + r[2] + pad, r[1] + r[3] + pad, ArmorHudConfig.panelColor);
            }
        }
        for (Placed pl : layout.placed) drawSlot(c, p, pl);
    }

    private static void drawSlot(HudCanvas c, LocalPlayer p, Placed pl) {
        ItemStack stack = pl.stack;
        int x = pl.x, y = pl.y;
        int size = icon();

        drawBackground(c, x, y, size);

        if (stack.isEmpty()) {
            c.iconRaw(emptyTexture(pl.slot), x, y, size, size);
            return;
        }

        if (ArmorHudConfig.scale == 1.0f) {
            c.item(p, stack, x, y);
        } else {
            c.itemScaled(p, stack, x, y, ArmorHudConfig.scale);
        }

        if (ArmorHudConfig.showCount && stack.getCount() > 1) {
            String cnt = String.valueOf(stack.getCount());
            float s = ArmorHudConfig.scale;
            int tx = x + Math.round((17 - Minecraft.getInstance().font.width(cnt)) * s);
            int ty = y + Math.round(9 * s);
            c.textScaled(cnt, tx, ty, 0xFFFFFFFF, true, s);
        }
        if (ArmorHudConfig.showDurabilityBar && stack.isDamageableItem() && stack.getDamageValue() > 0) {
            drawDurabilityBar(c, stack, x, y, size);
        }
        if (pl.text != null) {
            c.textScaled(pl.text, pl.textX, pl.textY, healthColor(stack), ArmorHudConfig.textShadow, ArmorHudConfig.scale);
        }
    }

    private static int healthColor(ItemStack stack) {
        int base = ArmorHudConfig.textChroma ? ChromaUtil.color() : ArmorHudConfig.textColor;
        if (!ArmorHudConfig.dynamicColor || !stack.isDamageableItem() || stack.getMaxDamage() <= 0) return base;
        float fraction = 1f - (float) stack.getDamageValue() / stack.getMaxDamage();
        if (fraction > 0.75f) return base;
        if (fraction > 0.5f) return 0xFFFFFF55;
        if (fraction > 0.25f) return 0xFFFFAA00;
        return 0xFFFF5555;
    }

    private static void drawBackground(HudCanvas c, int x, int y, int size) {
        switch (ArmorHudConfig.bgStyle) {
            case GLASS -> {
                c.rect(x - 1, y - 1, x + size + 1, y + size + 1, 0x55202020);
                c.rect(x - 1, y - 1, x + size + 1, y, 0x33FFFFFF);
                c.border(x - 1, y - 1, size + 2, size + 2, 0x66FFFFFF);
            }
            case NEON -> {
                int accent = UiStyle.currentAccent();
                c.rect(x - 1, y - 1, x + size + 1, y + size + 1, 0x33000000);
                c.border(x - 1, y - 1, size + 2, size + 2, accent);
                c.border(x - 2, y - 2, size + 4, size + 4, (accent & 0xFFFFFF) | 0x55000000);
                c.border(x - 3, y - 3, size + 6, size + 6, (accent & 0xFFFFFF) | 0x22000000);
            }
            case HOTBAR -> c.hotbarSlotBackground(x, y, size);
            case NONE -> {
            }
        }
    }

    private static void drawDurabilityBar(HudCanvas c, ItemStack stack, int x, int y, int size) {
        int max = stack.getMaxDamage();
        if (max <= 0) return;
        int dmg = stack.getDamageValue();
        float frac = 1f - (float) dmg / (float) max;
        int barY = y + size - 2;
        int barW = size - 2;
        c.rect(x + 1, barY, x + 1 + barW, barY + 1, 0xFF000000);
        int fillW = Math.round(barW * frac);
        int color = frac > 0.5f ? 0xFF55FF55 : frac > 0.2f ? 0xFFFFAA00 : 0xFFFF5555;
        if (fillW > 0) c.rect(x + 1, barY, x + 1 + fillW, barY + 1, color);
    }

    public static boolean hidesVanillaOffhand() {
        return ArmorHudConfig.enabled && ArmorHudConfig.hotbarSnapped && ArmorHudConfig.showOffHand;
    }

    private static final net.minecraft.world.item.Item[] WARN_LAST_ITEM = new net.minecraft.world.item.Item[Slot.values().length];
    private static final boolean[] WARNED = new boolean[Slot.values().length];

    public static void tickWarning(Minecraft mc) {
        LocalPlayer p = mc.player;
        if (p == null || !ArmorHudConfig.enabled || ArmorHudConfig.warnSound == ArmorHudConfig.WarnSound.NONE) return;
        for (Slot s : Slot.values()) {
            int i = s.ordinal();
            ItemStack st = isVisible(s) ? stackFor(p, s) : ItemStack.EMPTY;
            var item = st.isEmpty() ? null : st.getItem();
            if (item != WARN_LAST_ITEM[i]) {
                WARN_LAST_ITEM[i] = item;
                WARNED[i] = false;
            }
            boolean low = !st.isEmpty() && st.isDamageableItem() && st.getMaxDamage() > 0
                    && (st.getMaxDamage() - st.getDamageValue()) * 100f / st.getMaxDamage() < ArmorHudConfig.warnPercent;
            if (low && !WARNED[i]) {
                WARNED[i] = true;
                VersionCompat.playUiSound(ArmorHudConfig.warnSound.soundPath, 1.0f);
            } else if (!low) {
                WARNED[i] = false;
            }
        }
    }
}
