package com.rtc.client.huds;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public final class DirectionUtil {
    private DirectionUtil() {}

    public static Component of(LocalPlayer player) {
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
}