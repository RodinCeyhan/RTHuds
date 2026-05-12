package com.rtc.client.hud;

import com.rtc.client.gui.ModConfig;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.rtc.client.RTHudsClient.HUD_ID;

@SuppressWarnings("SpellCheckingInspection")
public class HudRenderer {

    private static DecimalFormat dfX, dfY, dfZ;
    private static int lastDecimalPlaces = -1;
    private static boolean registered = false;

    public static void register() {
        if (!registered) {
            HudElementRegistry.addLast(HUD_ID, HudRenderer::onRender);
            registered = true;
        }
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
            places = Math.clamp(decimalPlaces, 0, 5);
        }

        StringBuilder pattern = new StringBuilder("0");
        if (places > 0) {
            pattern.append(".");
            pattern.repeat("0", places);
        }

        return new DecimalFormat(pattern.toString());
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static void onRender(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        if (!ModConfig.InfoHud()) return;

        Minecraft mc = Minecraft.getInstance();
        if (ModConfig.HideGui() && mc.options.hideGui) return;
        if (ModConfig.DebugGui() && mc.getDebugOverlay().showDebugScreen()) return;

        LocalPlayer player = mc.player;
        if (player == null) return;

        updateFormatters(ModConfig.infoHudDecimalPlaces());

        var camera = mc.gameRenderer.getMainCamera();
        float yaw = Mth.wrapDegrees(camera.yRot());
        float pitch = camera.xRot();

        List<Component> lines = new ArrayList<>();
        int valueColor = 0xFFFFFF;

        String xText = dfX.format(player.getX());
        String yText = dfY.format(player.getY());
        String zText = dfZ.format(player.getZ());

        ModConfig options = ModConfig.get();
        int intxyzColor = options.coordinatesColor;
        int intyawColor = options.yawColor;
        int intpitchColor = options.pitchColor;
        int intdirectionColor = options.directionColor;
        int intfpsColor = options.fpsColor;
        int intnetherColor = options.coordinateConverterColor;


        switch (ModConfig.infoHudDirection()) {
            case HORIZONTAL:
                MutableComponent line = Component.empty();
                boolean needsSeparator = false;

                if (ModConfig.showCoordinates()) {
                    line.append(Component.literal("XYZ: ").withColor(intxyzColor))
                            .append(Component.literal(xText).withColor(valueColor))
                            .append(Component.literal(" / ").withColor(intxyzColor))
                            .append(Component.literal(yText).withColor(valueColor))
                            .append(Component.literal(" / ").withColor(intxyzColor))
                            .append(Component.literal(zText).withColor(valueColor));
                    needsSeparator = true;
                }

                if (ModConfig.showYaw()) {
                    if (needsSeparator) line.append(Component.literal(" | ").withColor(valueColor));
                    line.append(Component.literal("Yaw: ").withColor(intyawColor))
                            .append(Component.literal(String.format(Locale.US, "%.1f", yaw)).withColor(valueColor));
                    needsSeparator = true;
                }

                if (ModConfig.showPitch()) {
                    if (needsSeparator) {
                        if (ModConfig.showCoordinates() && !ModConfig.showYaw()) line.append(Component.literal(" | ").withColor(valueColor));
                        line.append(Component.literal(" Pitch: ").withColor(intpitchColor))
                                .append(Component.literal(String.format(Locale.US, "%.1f", pitch)).withColor(valueColor));
                    }
                }

                if (ModConfig.showDirection()) {
                    if (needsSeparator) line.append(Component.literal(" | ").withColor(valueColor));
                    line.append(Component.translatable("rthuds.hud.direction").withColor(intdirectionColor))
                            .append(getDirection(player).copy().setStyle(Style.EMPTY.withColor(valueColor)));
                    needsSeparator = true;
                }

                if (ModConfig.showFPS()) {
                    if (needsSeparator) line.append(Component.literal(" | ").withColor(valueColor));
                    line.append(Component.literal("FPS: ").withColor(intfpsColor))
                            .append(Component.literal(String.valueOf(mc.getFps())).withColor(valueColor));
                }

                if (!line.getString().isEmpty()) lines.add(line);
                addNetherCoords(lines, player, valueColor, intnetherColor);
                break;

            case VERTICAL:
                if (ModConfig.showCoordinates()) {
                    lines.add(Component.literal("X: ").withColor(intxyzColor).append(Component.literal(xText).withColor(valueColor)));
                    lines.add(Component.literal("Y: ").withColor(intxyzColor).append(Component.literal(yText).withColor(valueColor)));
                    lines.add(Component.literal("Z: ").withColor(intxyzColor).append(Component.literal(zText).withColor(valueColor)));
                }
                if (ModConfig.showYaw()) {
                    lines.add(Component.literal("Yaw: ").withColor(intyawColor).append(Component.literal(String.format(Locale.US, "%.1f", yaw)).withColor(valueColor)));
                }
                if (ModConfig.showPitch()) {
                    lines.add(Component.literal("Pitch: ").withColor(intpitchColor).append(Component.literal(String.format(Locale.US, "%.1f", pitch)).withColor(valueColor)));
                }
                if (ModConfig.showDirection()) {
                    lines.add(Component.translatable("rthuds.hud.direction").withColor(intdirectionColor)
                            .append(Component.literal(" "))
                            .append(getDirection(player).copy().setStyle(Style.EMPTY.withColor(valueColor))));
                }
                if (ModConfig.showFPS()) {
                    lines.add(Component.literal("FPS: ").withColor(intfpsColor).append(Component.literal(String.valueOf(mc.getFps())).withColor(valueColor)));
                }
                addNetherCoords(lines, player, valueColor, intnetherColor);
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

        double xPercent = Mth.clamp(ModConfig.infoHudPosX(), 0, 100);
        double yPercent = Mth.clamp(ModConfig.infoHudPosY(), 0, 100);

        int startX = (int) ((screenWidth - maxWidth) * (xPercent / 100.0));
        int startY = (int) ((screenHeight - totalHeight) * (yPercent / 100.0));

        int backgroundColor = switch (ModConfig.infoHudBackground()) {
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

            guiGraphics.text(mc.font, lineItem, lineX, lineY, 0xFFFFFFFF, ModConfig.InfoTextShadow());
        }
    }

    private static void addNetherCoords(List<Component> lines, LocalPlayer player, int valueColor, int intnetherColor) {
        if (!ModConfig.showCoordinatesConverter()) return;

        boolean isOverworld = player.level().dimension() == Level.OVERWORLD;
        boolean isNether = player.level().dimension() == Level.NETHER;

        if (!isOverworld && !isNether) return;

        double factor = isOverworld ? 1.0 / 8.0 : 8.0;
        String prefixKey = isOverworld ? "rthuds.text.nether" : "rthuds.text.overworld";

        String xConv = dfX.format(player.getX() * factor);
        String yConv = dfY.format(player.getY());
        String zConv = dfZ.format(player.getZ() * factor);

        lines.add(Component.translatable(prefixKey).withColor(intnetherColor)
                .append(Component.literal(" "))
                .append(Component.literal(xConv).withColor(valueColor))
                .append(Component.literal(" / ").withColor(intnetherColor))
                .append(Component.literal(yConv).withColor(valueColor))
                .append(Component.literal(" / ").withColor(intnetherColor))
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