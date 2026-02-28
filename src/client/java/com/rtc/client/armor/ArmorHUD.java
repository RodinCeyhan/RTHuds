package com.rtc.client.armor;

import com.rtc.client.gui.RTHudsConfigScreen;
import com.rtc.client.utilities.HudConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

@SuppressWarnings({"unused", "UnnecessaryLocalVariable"})
public class ArmorHUD {

    private static final ResourceLocation BLACK_EMPTY_HELMET = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/helmet.png");
    private static final ResourceLocation BLACK_EMPTY_CHEST = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/chestplate.png");
    private static final ResourceLocation BLACK_EMPTY_LEGS = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/leggings.png");
    private static final ResourceLocation BLACK_EMPTY_BOOTS = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/boots.png");
    private static final ResourceLocation BLACK_EMPTY_HAND = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/sword.png");
    private static final ResourceLocation BLACK_EMPTY_OFFHAND = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/shield.png");

    private static final ResourceLocation WHITE_EMPTY_HELMET = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/white_helmet.png");
    private static final ResourceLocation WHITE_EMPTY_CHEST = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/white_chestplate.png");
    private static final ResourceLocation WHITE_EMPTY_LEGS = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/white_leggings.png");
    private static final ResourceLocation WHITE_EMPTY_BOOTS = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/white_boots.png");
    private static final ResourceLocation WHITE_EMPTY_HAND = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/white_sword.png");
    private static final ResourceLocation WHITE_EMPTY_OFFHAND = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/white_shield.png");

    private static final EquipmentSlot[] SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};

    private static final ResourceLocation[] BLACK_EMPTY_TEXTURES = {BLACK_EMPTY_HELMET, BLACK_EMPTY_CHEST, BLACK_EMPTY_LEGS, BLACK_EMPTY_BOOTS, BLACK_EMPTY_HAND, BLACK_EMPTY_OFFHAND};

    private static final ResourceLocation[] WHITE_EMPTY_TEXTURES = {WHITE_EMPTY_HELMET, WHITE_EMPTY_CHEST, WHITE_EMPTY_LEGS, WHITE_EMPTY_BOOTS, WHITE_EMPTY_HAND, WHITE_EMPTY_OFFHAND};

    private static final int BASE_ICON_SIZE = 16;
    private static final int BASE_GAP = 2;
    private static final int PADDING = 2;

    public static void register() {
        HudRenderCallback.EVENT.register(ArmorHUD::render);
    }

    private static void render(GuiGraphics g, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();

        if (!RTHudsConfigScreen.ArmorHUD) return;
        if (RTHudsConfigScreen.showF1 && mc.options.hideGui) return;
        if (RTHudsConfigScreen.showDebug && mc.getDebugOverlay().showDebugScreen()) return;

        Player player = mc.player;
        if (player == null) return;

        int maxSlots = RTHudsConfigScreen.showHandItems ? 6 : 4;
        boolean showEmpty = RTHudsConfigScreen.ShowEmpytSlot;

        int visibleSlots = 0;
        int maxTextWidth = 0;

        for (int i = 0; i < maxSlots; i++) {
            ItemStack stack = player.getItemBySlot(SLOTS[i]);
            if (showEmpty || !stack.isEmpty()) {
                visibleSlots++;

                String durability = getDurabilityText(stack);
                if (!durability.isEmpty()) {
                    maxTextWidth = Math.max(maxTextWidth, mc.font.width(durability));
                }
            }
        }

        if (visibleSlots == 0) return;

        float scale = RTHudsConfigScreen.ArmorScale;
        int step = Math.round((BASE_ICON_SIZE + BASE_GAP) * scale);

        int[] basePos = getBasePosition(mc, step, visibleSlots);
        int x = basePos[0];
        int y = basePos[1];

        if (RTHudsConfigScreen.armorBackgroundStyle != null && !RTHudsConfigScreen.armorBackgroundStyle.name().equals("NONE")) {
            boolean hudRight = RTHudsConfigScreen.ArmorLocation.name().startsWith("RIGHT");
            int scaledIconSize = Math.round(BASE_ICON_SIZE * scale);
            int textOffset = Math.round((BASE_ICON_SIZE + 4) * scale);

            int bgX;
            int bgWidth;

            if (maxTextWidth > 0) {
                if (hudRight) {
                    bgX = x - maxTextWidth - 2 - PADDING;
                    bgWidth = maxTextWidth + 2 + scaledIconSize + (PADDING * 2);
                } else {
                    bgX = x - PADDING;
                    bgWidth = textOffset + maxTextWidth + (PADDING * 2);
                }
            } else {
                bgX = x - PADDING;
                bgWidth = scaledIconSize + (PADDING * 2);
            }

            int bgY = y - PADDING;
            int bgHeight = ((visibleSlots - 1) * step) + scaledIconSize + (PADDING * 2);

            int bgColor = switch (RTHudsConfigScreen.armorBackgroundStyle.name()) {
                case "LIGHT" -> 0x80000000;
                case "FULL" -> 0xFF000000;
                default -> 0;
            };

            if (bgColor != 0) {
                g.fill(bgX, bgY, bgX + bgWidth, bgY + bgHeight, bgColor);
            }
        }
        for (int i = 0; i < maxSlots; i++) {
            EquipmentSlot slot = SLOTS[i];
            ItemStack stack = player.getItemBySlot(slot);

            if (stack.isEmpty() && !showEmpty) {
                continue;
            }

            if (RTHudsConfigScreen.armorBackgroundStyle == RTHudsConfigScreen.ArmorBackgroundStyle.NONE) {
                renderSlot(g, mc, stack, BLACK_EMPTY_TEXTURES[i], x, y, scale);
            } else {
                renderSlot(g, mc, stack, WHITE_EMPTY_TEXTURES[i], x, y, scale);
            }

            y += step;
        }
    }

    private static int[] getBasePosition(Minecraft mc, int step, int visibleSlots) {
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();

        int totalWidth = step;
        int totalHeight = step * visibleSlots;

        int x = 0;
        int y = 0;

        switch (RTHudsConfigScreen.ArmorLocation) {
            case LEFTUP -> {
                x = 4 + PADDING;
                y = 4 + PADDING;
            }
            case LEFTCENTER -> {
                x = 4 + PADDING;
                y = (sh - totalHeight) / 2;
            }
            case LEFTBOTTOM -> {
                x = 4 + PADDING;
                y = sh - totalHeight - 4 - PADDING;
            }
            case RIGHTUP -> {
                x = sw - totalWidth - 4 - PADDING;
                y = 4 + PADDING;
            }
            case RIGHTCENTER -> {
                x = sw - totalWidth - 4 - PADDING;
                y = (sh - totalHeight) / 2;
            }
            case RIGHTBOTTOM -> {
                x = sw - totalWidth - 4 - PADDING;
                y = sh - totalHeight - 4 - PADDING;
            }
        }
        return new int[]{x, y};
    }

    private static void renderSlot(GuiGraphics g, Minecraft mc, ItemStack stack, ResourceLocation emptyTexture, int x, int y, float scale) {
        var config = HudConfig.configManager.getConfig();
        var pose = g.pose();

        pose.pushPose();
        pose.translate(x, y,0);
        pose.scale(scale, scale, 1.0F);
        pose.translate(-x, -y, 0);

        if (stack.isEmpty()) {
            g.blit(RenderType::guiTextured, emptyTexture, x, y, 0, 0, 16, 16, 16, 16);
        } else {
            g.renderItem(stack, x, y);
            if (RTHudsConfigScreen.ArmorDurabilityBar) {
                g.renderItemDecorations(mc.font, stack, x, y);
            }
        }
        pose.popPose();

        String durability = getDurabilityText(stack);
        int inttextColor = Integer.parseInt(RTHudsConfigScreen.ArmorTextColor.replace("#", ""), 16);
        if (!durability.isEmpty()) {
            var line = Component.literal(durability).withColor(inttextColor);
            int textWidth = mc.font.width(line);
            int textX = getTextX(x, textWidth, scale);
            int textY = y + Math.round((BASE_ICON_SIZE * scale - mc.font.lineHeight) / 2f);

            g.drawString(mc.font, line, textX, textY, 0xFFFFFFFF, RTHudsConfigScreen.ArmorTextShadow);
        }
    }

    private static String getDurabilityText(ItemStack stack) {
        if (!stack.isDamageableItem()) return "";
        int max = stack.getMaxDamage();
        int dmg = stack.getDamageValue();
        int remaining = max - dmg;

        return switch (RTHudsConfigScreen.ArmorHealthDurability) {
            case NONE -> "";
            case REMAINING -> String.valueOf(remaining);
            case DAMAGE -> String.valueOf(dmg);
            case PERCENTAGE -> (int) ((remaining / (float) max) * 100) + "%";
            case REMAINGINGandMAX -> remaining + "/" + max;
        };
    }

    private static int getTextX(int x, int textWidth, float scale) {
        boolean hudRight = RTHudsConfigScreen.ArmorLocation.name().startsWith("RIGHT");
        int offset = Math.round((BASE_ICON_SIZE + 4) * scale);
        return hudRight ? x - textWidth - 2 : x + offset;
    }
}