package com.rtc.client.hud;

import com.rtc.client.utilities.HudConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import com.rtc.client.hud.ModConfig.HudColor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("ALL")
public class HudRenderer {

    private static DecimalFormat dfX, dfY, dfZ;
    private static int lastDecimalPlaces = -1;

    public static void register() {
        HudRenderCallback.EVENT.register(HudRenderer::onRender);
    }

    private static void updateFormatters(int decimalPlaces) {
        if (decimalPlaces == lastDecimalPlaces && dfX != null) return;

        dfX = createFormat(decimalPlaces, Axis.X);
        dfY = createFormat(decimalPlaces, Axis.Y);
        dfZ = createFormat(decimalPlaces, Axis.Z);
        lastDecimalPlaces = decimalPlaces;
    }

    private static DecimalFormat createFormat(int decimalPlaces, Axis axis) {
        int places;

        if (decimalPlaces == 6) {
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
        var config = HudConfig.configManager.getConfig();
        if (!config.showHud) return;

        Minecraft mc = Minecraft.getInstance();
        if (config.showF1 && mc.options.hideGui) return;
        if (config.showDebug && mc.getDebugOverlay().showDebugScreen()) return;

        LocalPlayer player = mc.player;
        if (player == null) return;

        updateFormatters(config.decimalPlaces);

        var camera = mc.gameRenderer.getMainCamera();
        float yaw = Mth.wrapDegrees(camera.getYRot());
        float pitch = camera.getXRot();

        List<Component> lines = new ArrayList<>();
        int valueColor = HudColor.WHITE.color;

        String xText = dfX.format(player.getX());
        String yText = dfY.format(player.getY());
        String zText = dfZ.format(player.getZ());

        switch (config.layout) {
            case LAYOUT_1:
                MutableComponent line = Component.empty();
                boolean needsSeparator = false;

                if (config.showCoords) {
                    line.append(Component.literal("XYZ: ").withColor(config.xyzColor.color))
                            .append(Component.literal(xText).withColor(valueColor))
                            .append(Component.literal(" / ").withColor(config.xyzColor.color))
                            .append(Component.literal(yText).withColor(valueColor))
                            .append(Component.literal(" / ").withColor(config.xyzColor.color))
                            .append(Component.literal(zText).withColor(valueColor));
                    needsSeparator = true;
                }

                if (config.showYawPitch) {
                    if (needsSeparator) line.append(Component.literal(" | ").withColor(valueColor));
                    line.append(Component.literal("Yaw: ").withColor(config.yawColor.color))
                            .append(Component.literal(String.format(Locale.US, "%.1f", yaw)).withColor(valueColor))
                            .append(Component.literal(" Pitch: ").withColor(config.pitchColor.color))
                            .append(Component.literal(String.format(Locale.US, "%.1f", pitch)).withColor(valueColor));
                    needsSeparator = true;
                }

                if (config.showDirection) {
                    if (needsSeparator) line.append(Component.literal(" | ").withColor(valueColor));
                    line.append(Component.translatable("rthuds.hud.direction").withColor(config.directionColor.color))
                            .append(getDirection(player).copy().setStyle(Style.EMPTY.withColor(valueColor)));
                    needsSeparator = true;
                }

                if (config.showFPS) {
                    if (needsSeparator) line.append(Component.literal(" | ").withColor(valueColor));
                    line.append(Component.literal("FPS: ").withColor(config.fpsColor.color))
                            .append(Component.literal(String.valueOf(mc.getFps())).withColor(valueColor));
                }

                if (!line.getString().isEmpty()) lines.add(line);
                addNetherCoords(lines, player, config, valueColor);
                break;

            case LAYOUT_2:
                if (config.showCoords) {
                    lines.add(Component.literal("X: ").withColor(config.xyzColor.color).append(Component.literal(xText).withColor(valueColor)));
                    lines.add(Component.literal("Y: ").withColor(config.xyzColor.color).append(Component.literal(yText).withColor(valueColor)));
                    lines.add(Component.literal("Z: ").withColor(config.xyzColor.color).append(Component.literal(zText).withColor(valueColor)));
                }
                if (config.showYawPitch) {
                    lines.add(Component.literal("Yaw: ").withColor(config.yawColor.color).append(Component.literal(String.format(Locale.US, "%.1f", yaw)).withColor(valueColor)));
                    lines.add(Component.literal("Pitch: ").withColor(config.pitchColor.color).append(Component.literal(String.format(Locale.US, "%.1f", pitch)).withColor(valueColor)));
                }
                if (config.showDirection) {
                    lines.add(Component.translatable("rthuds.hud.direction").withColor(config.directionColor.color)
                            .append(Component.literal(" "))
                            .append(getDirection(player).copy().setStyle(Style.EMPTY.withColor(valueColor))));
                }
                if (config.showFPS) {
                    lines.add(Component.literal("FPS: ").withColor(config.fpsColor.color).append(Component.literal(String.valueOf(mc.getFps())).withColor(valueColor)));
                }
                addNetherCoords(lines, player, config, valueColor);
                break;
        }

        if (lines.isEmpty()) return;

        int textHeight = mc.font.lineHeight;
        int lineSpacing = 2;
        int bgPadding = 2;

        int maxWidth = lines.stream().mapToInt(mc.font::width).max().orElse(0);
        int totalHeight = (lines.size() * textHeight) + ((lines.size() - 1) * lineSpacing);

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        double xPercent = Mth.clamp(config.hudXPercent, 0, 100);
        double yPercent = Mth.clamp(config.hudYPercent, 0, 100);

        int startX = (int) ((screenWidth - maxWidth) * (xPercent / 100.0));
        int startY = (int) ((screenHeight - totalHeight) * (yPercent / 100.0));

        int backgroundColor = switch (config.backgroundStyle) {
            case LIGHT -> 0x80000000;
            case FULL -> 0xFF000000;
            default -> 0;
        };

        if (backgroundColor != 0) {
            guiGraphics.fill(startX - bgPadding, startY - bgPadding, startX + maxWidth + bgPadding, startY + totalHeight + bgPadding, backgroundColor);
        }

        for (int i = 0; i < lines.size(); i++) {
            Component lineItem = lines.get(i);
            int lineWidth = mc.font.width(lineItem);
            int lineY = startY + (i * (textHeight + lineSpacing));
            int lineX;

            if (xPercent >= 60) {
                lineX = startX + (maxWidth - lineWidth);
            } else if (xPercent >= 40) {
                lineX = startX + (maxWidth - lineWidth) / 2;
            } else {
                lineX = startX;
            }

            guiGraphics.drawString(mc.font, lineItem, lineX, lineY, 0xFFFFFFFF, config.textShadow);
        }
    }

    private static void addNetherCoords(List<Component> lines, LocalPlayer player, ModConfig config, int valueColor) {
        if (!config.toggleNetherCoordinateConversion || player.level() == null) return;

        boolean isOverworld = player.level().dimension() == Level.OVERWORLD;
        boolean isNether = player.level().dimension() == Level.NETHER;

        if (!isOverworld && !isNether) return;

        double factor = isOverworld ? 1.0 / 8.0 : 8.0;
        String prefixKey = isOverworld ? "rthuds.text.nether" : "rthuds.text.overworld";

        String xConv = dfX.format(player.getX() * factor);
        String yConv = dfY.format(player.getY());
        String zConv = dfZ.format(player.getZ() * factor);

        lines.add(Component.translatable(prefixKey).withColor(config.netherCoordColor.color)
                .append(Component.literal(" "))
                .append(Component.literal(xConv).withColor(valueColor))
                .append(Component.literal(" / ").withColor(config.netherCoordColor.color))
                .append(Component.literal(yConv).withColor(valueColor))
                .append(Component.literal(" / ").withColor(config.netherCoordColor.color))
                .append(Component.literal(zConv).withColor(valueColor)));
    }

    private static Component getDirection(LocalPlayer player) {
        float yaw = Mth.positiveModulo(player.getYRot(), 360.0F);

        if (yaw >= 337.5 || yaw < 22.5) return Component.translatable("rthuds.hud.direction.south");
        if (yaw < 67.5) return Component.translatable("rthuds.hud.direction.southwest");
        if (yaw < 112.5) return Component.translatable("rthuds.hud.direction.west");
        if (yaw < 157.5) return Component.translatable("rthuds.hud.direction.northwest");
        if (yaw < 202.5) return Component.translatable("rthuds.hud.direction.north");
        if (yaw < 247.5) return Component.translatable("rthuds.hud.direction.northeast");
        if (yaw < 292.5) return Component.translatable("rthuds.hud.direction.east");

        return Component.translatable("rthuds.hud.direction.southeast");
    }

    public enum Axis {
        X, Y, Z
    }
}