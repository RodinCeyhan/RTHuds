package com.rtc.client.huds;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.Locale;
import java.util.function.BooleanSupplier;

@SuppressWarnings("RedundantIfStatement")
public final class InfoHudElements {

    private InfoHudElements() {
    }

    public static void register() {
        Minecraft mc = Minecraft.getInstance();

        CoordFormatter coordsFmt = new CoordFormatter();
        HudElement coords = new HudElement("info_coords", 4, 28,
                () -> Component.translatable("rthuds.hud.xyz"),
                () -> {
                    var p = mc.player;
                    double x = p != null ? p.getX() : 0.0;
                    double y = p != null ? p.getY() : 0.0;
                    double z = p != null ? p.getZ() : 0.0;
                    return Component.literal(coordsFmt.x(x) + " / " + coordsFmt.y(y) + " / " + coordsFmt.z(z));
                });
        coords.coordFormatter = coordsFmt;
        coords.visible = infoVisible(mc, HudConfig::showCoordinates);
        HudRegistry.add(coords);

        HudElement cps = new HudElement("info_cps", 4, 60, () -> Component.translatable("rthuds.hud.cps"),
                () -> Component.literal(ClickTracker.cps(ClickTracker.LEFT) + " | " + ClickTracker.cps(ClickTracker.RIGHT)));
        cps.visible = infoVisible(mc, HudConfig::showCPS);
        HudRegistry.add(cps);

        HudElement yaw = new HudElement("info_yaw", 4, 92,
                () -> Component.translatable("rthuds.hud.yaw"),
                () -> Component.literal(String.format(Locale.US, "%.1f", mc.player != null ? VersionCompat.yaw(mc) : 0f)));
        yaw.visible = infoVisible(mc, HudConfig::showYaw);
        HudRegistry.add(yaw);

        HudElement pitch = new HudElement("info_pitch", 4, 124,
                () -> Component.translatable("rthuds.hud.pitch"),
                () -> Component.literal(String.format(Locale.US, "%.1f", mc.player != null ? VersionCompat.pitch(mc) : 0f)));
        pitch.visible = infoVisible(mc, HudConfig::showPitch);
        HudRegistry.add(pitch);

        HudElement direction = new HudElement("info_direction", 170, 28,
                () -> Component.translatable("rthuds.hud.direction").append(": "),
                () -> {
                    var p = mc.player;
                    return p == null ? Component.translatable("rthuds.hud.direction.north") : DirectionUtil.of(p);
                });
        direction.visible = infoVisible(mc, HudConfig::showDirection);
        HudRegistry.add(direction);

        HudElement fps = new HudElement("info_fps", 170, 60,
                () -> Component.translatable("rthuds.hud.fps"),
                () -> Component.literal(String.valueOf(mc.getFps())));
        fps.visible = infoVisible(mc, HudConfig::showFPS);
        HudRegistry.add(fps);

        CoordFormatter converterFmt = new CoordFormatter();
        HudElement converter = new HudElement("info_converter", 170, 92,
                () -> {
                    var p = mc.player;
                    boolean overworld = p == null || p.level().dimension() == Level.OVERWORLD;
                    String key = overworld ? "rthuds.text.nether" : "rthuds.text.overworld";
                    return Component.translatable(key).append(" ");
                },
                () -> {
                    var p = mc.player;
                    boolean overworld = p == null || p.level().dimension() == Level.OVERWORLD;
                    boolean nether = p != null && p.level().dimension() == Level.NETHER;
                    if (p != null && !overworld && !nether) return Component.literal("");
                    double factor = overworld ? 1.0 / 8.0 : 8.0;
                    double x = p != null ? p.getX() : 0.0;
                    double y = p != null ? p.getY() : 0.0;
                    double z = p != null ? p.getZ() : 0.0;
                    return Component.literal(converterFmt.x(x * factor) + " / " + converterFmt.y(y) + " / " + converterFmt.z(z * factor));
                });
        converter.coordFormatter = converterFmt;
        converter.visible = infoVisible(mc, HudConfig::showCoordinatesConverter);
        HudRegistry.add(converter);

        HudElement biome = new HudElement("info_biome", 170, 124,
                () -> Component.translatable("rthuds.hud.biome"),
                () -> {
                    var p = mc.player;
                    var level = p != null ? p.level() : null;
                    if (level == null) return Component.literal("-");
                    var biomeHolder = level.getBiome(p.blockPosition());
                    String name = biomeHolder.unwrapKey()
                            //? if >=1.21.11 {
                            .map(key -> key.identifier().getPath())
                            //?} else {
                            /*.map(key -> key.location().getPath())
                             *///?}
                            .orElse("unknown");
                    return Component.literal(name);
                });
        biome.visible = infoVisible(mc, HudConfig::showBiome);
        HudRegistry.add(biome);
    }

    public static BooleanSupplier infoVisible(Minecraft mc, BooleanSupplier subFlag) {
        return () -> {
            if (!HudConfig.InfoHud() || !subFlag.getAsBoolean()) return false;
            if (HudConfig.HideGui() && VersionCompat.isGuiForcedHidden(mc)) return false;
            if (!HudConfig.DebugGui() && mc.getDebugOverlay().showDebugScreen()) return false;
            return true;
        };
    }
}