package com.rtc.client.hud;

import com.rtc.client.utilities.HudConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.rtc.client.hud.ModConfig.HudColor;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

@SuppressWarnings("ALL")
public class HudRenderer {

    public static void register() {
        HudRenderCallback.EVENT.register(HudRenderer::onRender);
    }

    private static DecimalFormat createFormat(int decimalPlaces, Axis axis) {
        int places;

        if (decimalPlaces == 6) {
            // özel durum
            places = (axis == Axis.Y) ? 5 : 3;
        } else {
            places = Math.max(0, Math.min(decimalPlaces, 5));
        }

        StringBuilder pattern = new StringBuilder("0");
        if (places > 0) {
            pattern.append(".");
            pattern.append("0".repeat(places));
        }

        return new DecimalFormat(pattern.toString());
    }

    private static void onRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!HudConfig.configManager.getConfig().showHud) return;

        Minecraft mc = Minecraft.getInstance();

        if (HudConfig.configManager.getConfig().showF1 && mc.options.hideGui) return;
        if (HudConfig.configManager.getConfig().showDebug && mc.getDebugOverlay().showDebugScreen()) return;

        LocalPlayer player = mc.player;

        if (player == null) return;

        var camera = mc.gameRenderer.getMainCamera();
        float yaw = Mth.wrapDegrees(camera.yRot());
        float pitch = camera.xRot();

        List<Component> lines = new ArrayList<>();
        int valueColor = HudColor.WHITE.color;

        int decimalPlaces = HudConfig.configManager.getConfig().decimalPlaces;

        DecimalFormat dfX = createFormat(decimalPlaces, Axis.X);
        DecimalFormat dfY = createFormat(decimalPlaces, Axis.Y);
        DecimalFormat dfZ = createFormat(decimalPlaces, Axis.Z);

        String xText = dfX.format(player.getX());
        String yText = dfY.format(player.getY());
        String zText = dfZ.format(player.getZ());

        switch (HudConfig.configManager.getConfig().layout) {
            case LAYOUT_1:
                var line = Component.literal("");
                boolean needsSeparator = false;

                if (HudConfig.configManager.getConfig().showCoords) {
                    line.append(Component.literal("XYZ: ")
                                    .withColor(HudConfig.configManager.getConfig().xyzColor.color)
                                    .append(Component.literal(String.format("%s ", xText)).withColor(valueColor))
                                    .append(Component.literal("/").withColor(HudConfig.configManager.getConfig().xyzColor.color))
                                    .append(Component.literal(String.format(" %s ", yText)).withColor(valueColor))
                                    .append(Component.literal("/").withColor(HudConfig.configManager.getConfig().xyzColor.color)))
                            .append(Component.literal(String.format(" %s", zText)).withColor(valueColor));
                    needsSeparator = true;
                }

                if (HudConfig.configManager.getConfig().showYawPitch) {
                    if (needsSeparator) line.append(Component.literal(" | ").withColor(valueColor));
                    line.append(Component.literal("Yaw: ").withColor(HudConfig.configManager.getConfig().yawColor.color))
                            .append(Component.literal(String.format("%.1f", yaw)).withColor(valueColor))
                            .append(Component.literal(" Pitch: ").withColor(HudConfig.configManager.getConfig().pitchColor.color))
                            .append(Component.literal(String.format("%.1f", pitch)).withColor(valueColor));
                    needsSeparator = true;
                }

                if (HudConfig.configManager.getConfig().showDirection) {
                    if (needsSeparator) line.append(Component.literal(" | ").withColor(valueColor));
                    line.append(Component.translatable("rthuds.hud.direction").withColor(HudConfig.configManager.getConfig().directionColor.color))
                            .append(getDirection(player).copy().setStyle((Style.EMPTY.withColor(valueColor))));
                    needsSeparator = true;
                }

                if (HudConfig.configManager.getConfig().showFPS) {
                    if (needsSeparator) line.append(Component.literal(" | ").withColor(valueColor));
                    line.append(Component.literal("FPS: ").withColor(HudConfig.configManager.getConfig().fpsColor.color))
                            .append(Component.literal(String.valueOf(mc.getFps())).withColor(valueColor));
                    needsSeparator = true;
                }

                if (needsSeparator) lines.add(line);

                addNetherCoords(lines, player, valueColor);
                break;

            case LAYOUT_2:
                if (HudConfig.configManager.getConfig().showCoords) {
                    lines.add(Component.literal("X: ").withColor(HudConfig.configManager.getConfig().xyzColor.color).append(Component.literal(xText).withColor(valueColor)));
                    lines.add(Component.literal("Y: ").withColor(HudConfig.configManager.getConfig().xyzColor.color).append(Component.literal(yText).withColor(valueColor)));
                    lines.add(Component.literal("Z: ").withColor(HudConfig.configManager.getConfig().xyzColor.color).append(Component.literal(zText).withColor(valueColor)));
                }
                if (HudConfig.configManager.getConfig().showYawPitch) {
                    lines.add(Component.literal("Yaw: ").withColor(HudConfig.configManager.getConfig().yawColor.color).append(Component.literal(String.valueOf(yaw)).withColor(valueColor)));
                    lines.add(Component.literal("Pitch: ").withColor(HudConfig.configManager.getConfig().pitchColor.color).append(Component.literal(String.valueOf(pitch)).withColor(valueColor)));
                }
                if (HudConfig.configManager.getConfig().showDirection) {
                    lines.add(Component.translatable("rthuds.hud.direction").withColor(HudConfig.configManager.getConfig().directionColor.color)
                            .append(getDirection(player).copy().setStyle(Style.EMPTY.withColor(valueColor))));
                }
                if (HudConfig.configManager.getConfig().showFPS) {
                    lines.add(Component.literal("FPS: ").withColor(HudConfig.configManager.getConfig().fpsColor.color).append(Component.literal(String.valueOf(mc.getFps())).withColor(valueColor)));
                }
                addNetherCoords(lines, player, valueColor);
                break;

            default:
                break;
        }

        if (lines.isEmpty()) return;

        int textHeight = mc.font.lineHeight;
        int lineSpacing = 2;
        int screenPadding = 2;
        int bgPadding = 2;

        int maxWidth = 0;
        for (Component line : lines) {
            int currentWidth = mc.font.width(line);
            if (currentWidth > maxWidth) {
                maxWidth = currentWidth;
            }
        }

        int totalHeight = (lines.size() * textHeight) + ((lines.size() - 1) * lineSpacing);
        if (totalHeight < textHeight) totalHeight = textHeight;

        int startX = screenPadding;
        int startY = screenPadding;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        switch (HudConfig.configManager.getConfig().ScreenLocation) {
            case LEFTUP:
                startX = screenPadding;
                startY = screenPadding;
                break;
            case LEFTDOWN:
                startX = screenPadding;
                startY = screenHeight - totalHeight - screenPadding;
                break;
            case RIGHTUP:
                startX = screenWidth - maxWidth - screenPadding;
                startY = screenPadding;
                break;
            case RIGHTDOWN:
                startX = screenWidth - maxWidth - screenPadding;
                startY = screenHeight - totalHeight - screenPadding;
                break;
            case CENTERUP:
                startX = (screenWidth / 2) - (maxWidth / 2);
                startY = screenPadding;
                break;
        }

        int backgroundColor = 0;
        switch (HudConfig.configManager.getConfig().backgroundStyle) {
            case LIGHT:
                backgroundColor = 0x80000000;
                break;
            case FULL:
                backgroundColor = 0xFF000000;
                break;
            case NONE:
            default:
                break;
        }

        if (backgroundColor != 0) {
            guiGraphics.fill(startX - bgPadding, startY - bgPadding, startX + maxWidth + bgPadding, startY + totalHeight + bgPadding, backgroundColor);
        }

        for (int i = 0; i < lines.size(); i++) {
            Component line = lines.get(i);
            int lineWidth = mc.font.width(line);

            int lineY = startY + (i * (textHeight + lineSpacing));
            int lineX = startX;

            if (HudConfig.configManager.getConfig().ScreenLocation == ModConfig.HUDLocation.RIGHTUP ||
                    HudConfig.configManager.getConfig().ScreenLocation == ModConfig.HUDLocation.RIGHTDOWN) {
                lineX = startX + (maxWidth - lineWidth);
            } else if (HudConfig.configManager.getConfig().ScreenLocation == ModConfig.HUDLocation.CENTERUP) {
                lineX = startX + (maxWidth - lineWidth) / 2;
            }

            guiGraphics.drawString(mc.font, line, lineX, lineY, 0xFFFFFFFF, HudConfig.configManager.getConfig().textShadow);
        }
    }

    // Kod tekrarını önlemek için Nether koordinat eklemeyi metoda çevirdim
    private static void addNetherCoords(List<Component> lines, LocalPlayer player, int valueColor) {

        int decimalPlaces = HudConfig.configManager.getConfig().decimalPlaces;
        Minecraft mc = Minecraft.getInstance();
        // LocalPlayer player = mc.player;

        DecimalFormat dfX = createFormat(decimalPlaces, Axis.X);
        DecimalFormat dfY = createFormat(decimalPlaces, Axis.Y);
        DecimalFormat dfZ = createFormat(decimalPlaces, Axis.Z);

        String xText = dfX.format(player.getX());
        String yText = dfY.format(player.getY());
        String zText = dfZ.format(player.getZ());

        if (HudConfig.configManager.getConfig().toggleNetherCoordinateConversion) {
            if (player != null && player.level() != null) {
                if (player.level().dimension() == Level.OVERWORLD) {
                    double xNether = player.getX() / 8;
                    double yNether = player.getY();
                    double zNether = player.getZ() / 8;
                    lines.add(Component.translatable("rthuds.text.nether").withColor(HudConfig.configManager.getConfig().netherCoordColor.color)
                            .append(Component.literal(dfX.format(xNether)).withColor(valueColor))
                            .append(Component.literal(" / ").withColor(HudConfig.configManager.getConfig().netherCoordColor.color))
                            .append(Component.literal(dfY.format(yNether)).withColor(valueColor))
                            .append(Component.literal(" / ").withColor(HudConfig.configManager.getConfig().netherCoordColor.color))
                            .append(Component.literal(dfZ.format(zNether)).withColor(valueColor)));
                } else if (player.level().dimension() == Level.NETHER) {
                    double xOver = player.getX() * 8;
                    double yOver = player.getY();
                    double zOver = player.getZ() * 8;
                    lines.add(Component.translatable("rthuds.text.overworld").withColor(HudConfig.configManager.getConfig().netherCoordColor.color)
                            .append(Component.literal(dfX.format(xOver)).withColor(valueColor))
                            .append(Component.literal(" / ").withColor(HudConfig.configManager.getConfig().netherCoordColor.color))
                            .append(Component.literal(dfY.format(yOver)).withColor(valueColor))
                            .append(Component.literal(" / ").withColor(HudConfig.configManager.getConfig().netherCoordColor.color))
                            .append(Component.literal(dfZ.format(zOver)).withColor(valueColor)));
                }
            }
        }
    }

    private static Component getDirection(LocalPlayer player) {
        float yaw = player.getYRot() % 360;
        if (yaw < 0) yaw += 360;

        if (yaw >= 337.5 || yaw < 22.5) {
            return Component.translatable("rthuds.hud.direction.south");
        } else if (yaw >= 22.5 && yaw < 67.5) {
            return Component.translatable("rthuds.hud.direction.southwest");
        } else if (yaw >= 67.5 && yaw < 112.5) {
            return Component.translatable("rthuds.hud.direction.west");
        } else if (yaw >= 112.5 && yaw < 157.5) {
            return Component.translatable("rthuds.hud.direction.northwest");
        } else if (yaw >= 157.5 && yaw < 202.5) {
            return Component.translatable("rthuds.hud.direction.north");
        } else if (yaw >= 202.5 && yaw < 247.5) {
            return Component.translatable("rthuds.hud.direction.northeast");
        } else if (yaw >= 247.5 && yaw < 292.5) {
            return Component.translatable("rthuds.hud.direction.east");
        } else {
            return Component.translatable("rthuds.hud.direction.southeast");
        }
    }

    public enum Axis {
        X, Y, Z
    }

}