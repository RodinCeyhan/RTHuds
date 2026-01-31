package com.rtc.client.armor;

import com.rtc.client.utilities.HudConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

@SuppressWarnings({"unused", "UnnecessaryLocalVariable"})
public class ArmorHUD {

    private static final ResourceLocation EMPTY_HELMET = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/helmet.png");
    private static final ResourceLocation EMPTY_CHEST = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/chestplate.png");
    private static final ResourceLocation EMPTY_LEGS = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/leggings.png");
    private static final ResourceLocation EMPTY_BOOTS = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/boots.png");
    private static final ResourceLocation EMPTY_HAND = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/sword.png");
    private static final ResourceLocation EMPTY_OFFHAND = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/shield.png");

    private static final int BASE_ICON_SIZE = 16;
    private static final int BASE_GAP = 2;
    private static int SLOT_COUNT;

    public static void updateSlotCount() {
        SLOT_COUNT = HudConfig.configManager.getConfig().showHandItems ? 6 : 4;
    }

    public static int getSlotCount() {
        return SLOT_COUNT;
    }

    public static void register() {
        HudRenderCallback.EVENT.register(ArmorHUD::render);
    }

    private static void render(GuiGraphics g, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (!HudConfig.configManager.getConfig().ArmorHUD) return;
        if (HudConfig.configManager.getConfig().showF1 && mc.options.hideGui) return;
        if (HudConfig.configManager.getConfig().showDebug && mc.getDebugOverlay().showDebugScreen()) return;

        updateSlotCount();

        Player player = mc.player;
        if (player == null) return;

        float scale = HudConfig.configManager.getConfig().ArmorScale;
        int step = getStep(scale);

        int[] base = getBasePosition(mc, step);
        int x = base[0];
        int y = base[1];

        renderSlot(g, mc, player, EquipmentSlot.HEAD, EMPTY_HELMET, x, y, scale);
        int[] pos = move(x, y, step);
        x = pos[0];
        y = pos[1];

        renderSlot(g, mc, player, EquipmentSlot.CHEST, EMPTY_CHEST, x, y, scale);
        pos = move(x, y, step);
        x = pos[0];
        y = pos[1];

        renderSlot(g, mc, player, EquipmentSlot.LEGS, EMPTY_LEGS, x, y, scale);
        pos = move(x, y, step);
        x = pos[0];
        y = pos[1];

        renderSlot(g, mc, player, EquipmentSlot.FEET, EMPTY_BOOTS, x, y, scale);
        if (HudConfig.configManager.getConfig().showHandItems) {
            pos = move(x, y, step);
            x = pos[0];
            y = pos[1];

            renderSlot(g, mc, player, EquipmentSlot.MAINHAND, EMPTY_HAND, x, y, scale);
            pos = move(x, y, step);
            x = pos[0];
            y = pos[1];

            renderSlot(g, mc, player, EquipmentSlot.OFFHAND, EMPTY_OFFHAND, x, y, scale);
        }
    }

    private static int getStep(float scale) {
        return Math.round((BASE_ICON_SIZE + BASE_GAP) * scale);
    }

    private static int[] getBasePosition(Minecraft mc, int step) {
        int sw = mc.getWindow().getGuiScaledWidth();
        int sh = mc.getWindow().getGuiScaledHeight();


        int totalWidth = step;
        int totalHeight = step * SLOT_COUNT;

        int x = 0;
        int y = 0;

        switch (HudConfig.configManager.getConfig().ArmorLocation) {
            case LEFTUP -> {
                x = 4;
                y = 4;
            }
            case LEFTCENTER -> {
                x = 4;
                y = sh / 2 - totalHeight / 2;
            }
            case LEFTBOTTOM -> {
                x = 4;
                y = sh - totalHeight - 4;
            }
            case RIGHTUP -> {
                x = sw - totalWidth - 4;
                y = 4;
            }
            case RIGHTCENTER -> {
                x = sw - totalWidth - 4;
                y = sh / 2 - totalHeight / 2;
            }
            case RIGHTBOTTOM -> {
                x = sw - totalWidth - 4;
                y = sh - totalHeight - 4;
            }
        }
        return new int[]{x, y};
    }

    private static int[] move(int x, int y, int step) {
        y += step;
        return new int[]{x, y};
    }

    private static void renderSlot(GuiGraphics g, Minecraft mc, Player player,
                                   EquipmentSlot slot, ResourceLocation emptyTexture,
                                   int x, int y, float scale) {

        ItemStack stack = player.getItemBySlot(slot);

        var pose = g.pose();
        pose.pushPose();
        pose.translate(x, y, 0);
        pose.scale(scale, scale, 1.0F);
        pose.translate(-x, -y, 0);

        if (stack.isEmpty()) {
            if (HudConfig.configManager.getConfig().ShowEmpytSlot) {
                g.blit(emptyTexture, x, y, 0, 0, 16, 16, 16, 16);
            }
        } else {
            g.renderItem(stack, x, y);
            if (HudConfig.configManager.getConfig().ArmorDurabilityBar) {
                g.renderItemDecorations(mc.font, stack, x, y);
            }
        }
        pose.popPose();

        String durability = getDurabilityText(stack);
        var line = Component.literal(durability).withColor(HudConfig.configManager.getConfig().ArmorTextColor.color);

        if (!durability.isEmpty()) {
            int textWidth = mc.font.width(line);
            int textX = getTextX(x, textWidth, scale);
            int textY = y + Math.round((BASE_ICON_SIZE * scale - mc.font.lineHeight) / 2f);

            g.drawString(mc.font, line, textX, textY, 0xFFFFFFFF, HudConfig.configManager.getConfig().ArmorTextShadow);
        }
    }

    private static String getDurabilityText(ItemStack stack) {
        if (!stack.isDamageableItem()) return "";
        int max = stack.getMaxDamage();
        int dmg = stack.getDamageValue();
        int remaining = max - dmg;

        return switch (HudConfig.configManager.getConfig().ArmorHealthDurability) {
            case NONE -> "";
            case REMAINING -> String.valueOf(remaining);
            case DAMAGE -> String.valueOf(dmg);
            case PERCENTAGE -> (int) ((remaining / (float) max) * 100) + "%";
            case REMAINGINGandMAX -> remaining + "/" + max;
        };
    }

    private static int getTextX(int x, int textWidth, float scale) {
        boolean hudRight = HudConfig.configManager.getConfig().ArmorLocation.name().startsWith("RIGHT");
        int offset = Math.round((BASE_ICON_SIZE + 4) * scale);
        return hudRight ? x - textWidth - 2 : x + offset;
    }
}