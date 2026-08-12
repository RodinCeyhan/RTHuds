package com.rtc.client.huds;

import com.rtc.client.config.ModConfig;
//? if >=26.1.2{
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
//?} else {
/*import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
 */
//?}
import net.minecraft.client.Minecraft;
//? if >=26.1.2 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?} else {
/*import net.minecraft.client.gui.GuiGraphics;
 */
//?}
import net.minecraft.client.DeltaTracker;
//? if >=1.21.8 {
import net.minecraft.client.renderer.RenderPipelines;
//?} else if >=1.21.3 {
/*import net.minecraft.client.renderer.RenderType;*/
//?}
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
 */
//?}
import net.minecraft.network.chat.Component;

//? if >=26.1.2{
import static com.rtc.client.RTHudsClient.ARMOR_ID;
//?}

@SuppressWarnings({"unused", "UnnecessaryLocalVariable", "SpellCheckingInspection", "CommentedOutCode"})
public class ArmorHUD {

    //? if >=1.21.11 {
    private static final Identifier BLACK_EMPTY_HELMET = Identifier.fromNamespaceAndPath("rthuds", "textures/helmet.png");
    private static final Identifier BLACK_EMPTY_CHEST = Identifier.fromNamespaceAndPath("rthuds", "textures/chestplate.png");
    private static final Identifier BLACK_EMPTY_LEGS = Identifier.fromNamespaceAndPath("rthuds", "textures/leggings.png");
    private static final Identifier BLACK_EMPTY_BOOTS = Identifier.fromNamespaceAndPath("rthuds", "textures/boots.png");
    private static final Identifier BLACK_EMPTY_HAND = Identifier.fromNamespaceAndPath("rthuds", "textures/sword.png");
    private static final Identifier BLACK_EMPTY_OFFHAND = Identifier.fromNamespaceAndPath("rthuds", "textures/shield.png");
    private static final Identifier WHITE_EMPTY_HELMET = Identifier.fromNamespaceAndPath("rthuds", "textures/white_helmet.png");
    private static final Identifier WHITE_EMPTY_CHEST = Identifier.fromNamespaceAndPath("rthuds", "textures/white_chestplate.png");
    private static final Identifier WHITE_EMPTY_LEGS = Identifier.fromNamespaceAndPath("rthuds", "textures/white_leggings.png");
    private static final Identifier WHITE_EMPTY_BOOTS = Identifier.fromNamespaceAndPath("rthuds", "textures/white_boots.png");
    private static final Identifier WHITE_EMPTY_HAND = Identifier.fromNamespaceAndPath("rthuds", "textures/white_sword.png");
    private static final Identifier WHITE_EMPTY_OFFHAND = Identifier.fromNamespaceAndPath("rthuds", "textures/white_shield.png");
    private static final Identifier[] BLACK_EMPTY_TEXTURES = {BLACK_EMPTY_HELMET, BLACK_EMPTY_CHEST, BLACK_EMPTY_LEGS, BLACK_EMPTY_BOOTS, BLACK_EMPTY_HAND, BLACK_EMPTY_OFFHAND};
    private static final Identifier[] WHITE_EMPTY_TEXTURES = {WHITE_EMPTY_HELMET, WHITE_EMPTY_CHEST, WHITE_EMPTY_LEGS, WHITE_EMPTY_BOOTS, WHITE_EMPTY_HAND, WHITE_EMPTY_OFFHAND};
    //?} else {
    /*private static final ResourceLocation BLACK_EMPTY_HELMET = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/helmet.png");
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
    private static final ResourceLocation WHITE_EMPTY_OFFHAND = ResourceLocation.fromNamespaceAndPath("rthuds", "textures/white_shield.png");private static final ResourceLocation[] BLACK_EMPTY_TEXTURES = {BLACK_EMPTY_HELMET, BLACK_EMPTY_CHEST, BLACK_EMPTY_LEGS, BLACK_EMPTY_BOOTS, BLACK_EMPTY_HAND, BLACK_EMPTY_OFFHAND};
    private static final ResourceLocation[] WHITE_EMPTY_TEXTURES = {WHITE_EMPTY_HELMET, WHITE_EMPTY_CHEST, WHITE_EMPTY_LEGS, WHITE_EMPTY_BOOTS, WHITE_EMPTY_HAND, WHITE_EMPTY_OFFHAND};
     */
    //?}

    private static final EquipmentSlot[] SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};

    private static final int BASE_ICON_SIZE = 16;
    private static final int BASE_GAP = 2;
    private static final int PADDING = 2;
    private static boolean registered = false;


    public static void register() {
        //? if >=26.1.2{
        if (!registered) {
            HudElementRegistry.addLast(ARMOR_ID, ArmorHUD::render);
            registered = true;
        }
        //?} else {
        /* HudRenderCallback.EVENT.register(ArmorHUD::render);
         */
        //?}
    }

    //? if >=26.1.2 {
    private static void render(GuiGraphicsExtractor g, DeltaTracker deltaTracker) {
        //?} else {
        /*private static void render(GuiGraphics g, DeltaTracker deltaTracker) {
         */
        //?}

        Minecraft mc = Minecraft.getInstance();

        if (!ModConfig.ArmorHud()) return;

        //? if =26.2 {
        if (ModConfig.HideGui() && mc.gui.hud.isHidden()) return;
        //?} else {
        /*if (ModConfig.HideGui() && mc.options.hideGui) return;
         */
        //?}

        if (ModConfig.DebugGui() && mc.getDebugOverlay().showDebugScreen()) return;

        Player player = mc.player;
        if (player == null) return;

        int maxSlots = ModConfig.armorHudHandItems() ? 6 : 4;
        boolean showEmpty = ModConfig.armorHudEmpytSlots();

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

        float scale = ModConfig.armorHudScale();
        int step = Math.round((BASE_ICON_SIZE + BASE_GAP) * scale);

        int[] basePos = getBasePosition(mc, step, visibleSlots);
        int x = basePos[0];
        int y = basePos[1];

        if (ModConfig.armorHudBackground() != null && !ModConfig.armorHudBackground().name().equals("NONE")) {
            boolean hudRight = ModConfig.armorHudLocation().name().startsWith("RIGHT");
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

            int bgColor = switch (ModConfig.armorHudBackground().name()) {
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

            if (ModConfig.armorHudBackground() == ModConfig.HudBackground.NONE) {
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

        switch (ModConfig.armorHudLocation()) {
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

    //? if >=26.1.2 {
    private static void renderSlot(GuiGraphicsExtractor g, Minecraft mc, ItemStack stack, Identifier emptyTexture, int x, int y, float scale) {
    //?} else if >=1.21.11 {
    /*private static void renderSlot(GuiGraphics g, Minecraft mc, ItemStack stack, Identifier emptyTexture, int x, int y, float scale) {*/
    //?} else {
    /*private static void renderSlot(GuiGraphics g, Minecraft mc, ItemStack stack, ResourceLocation emptyTexture, int x, int y, float scale) {*/
    //?}

        var pose = g.pose();

        //? if >=1.21.8 {
        pose.pushMatrix();
        pose.translate(x, y);
        pose.scale(scale, scale);
        pose.translate(-x, -y);
        //?} else {
        /*pose.pushPose();
        pose.translate(x, y, 0);
        pose.scale(scale, scale, 1.0F);
        pose.translate(-x, -y, 0);
         */
        //?}

        if (stack.isEmpty()) {
            //? if >=1.21.8 {
            g.blit(RenderPipelines.GUI_TEXTURED, emptyTexture, x, y, 0, 0, 16, 16, 16, 16);
            //?} else if >=1.21.3 {
             /*g.blit(RenderType::guiTextured, emptyTexture, x, y, 0, 0, 16, 16, 16, 16);*/
            //?} else {
            /*g.blit(emptyTexture, x, y, 0, 0, 16, 16, 16, 16);*/
            //?}
        } else {
            //? if >=26.1.2 {
            g.item(stack, x, y);
            //?} else {
            /*g.renderItem(stack, x, y);*/
            //?}
            if (ModConfig.armorHudBar()) {
                //? if >=26.1.2 {
                g.itemDecorations(mc.font, stack, x, y);
                //?} else {
                /*g.renderItemDecorations(mc.font, stack, x, y);*/
                //?}
            }
        }

        //? if >=1.21.8 {
        pose.popMatrix();
        //?} else {
        /*pose.popPose();*/
        //?}

        String durability = getDurabilityText(stack);
        int inttextColor = ModConfig.get().armorHudHealthColor;
        if (!durability.isEmpty()) {
            var line = Component.literal(durability).withColor(inttextColor);
            int textWidth = mc.font.width(line);
            int textX = getTextX(x, textWidth, scale);
            int textY = y + Math.round((BASE_ICON_SIZE * scale - mc.font.lineHeight) / 2f);

            //? if >=26.1.2 {
            g.text(mc.font, line, textX, textY, 0xFFFFFFFF, ModConfig.ArmorTextShadow());
            //?} else {
            /*g.drawString(mc.font, line, textX, textY, 0xFFFFFFFF, ModConfig.ArmorTextShadow());*/
            //?}
        }
    }

    private static String getDurabilityText(ItemStack stack) {
        if (!stack.isDamageableItem()) return "";
        int max = stack.getMaxDamage();
        int dmg = stack.getDamageValue();
        int remaining = max - dmg;

        return switch (ModConfig.armorHudHealth()) {
            case NONE -> "";
            case REMAINING -> String.valueOf(remaining);
            case DAMAGE -> String.valueOf(dmg);
            case PERCENTAGE -> (int) ((remaining / (float) max) * 100) + "%";
            case REMAININGMAX -> remaining + "/" + max;
        };
    }

    private static int getTextX(int x, int textWidth, float scale) {
        boolean hudRight = ModConfig.armorHudLocation().name().startsWith("RIGHT");
        int offset = Math.round((BASE_ICON_SIZE + 4) * scale);
        return hudRight ? x - textWidth - 2 : x + offset;
    }
}