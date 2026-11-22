package com.rtc.client.huds;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;

public final class VersionCompat {
    private VersionCompat() {
    }

    public static float yaw(Minecraft mc) {
//? if >=26.2 {
        return Mth.wrapDegrees(mc.gameRenderer.mainCamera().yRot());
//?} else if >=1.21.11 {
        /*        return Mth.wrapDegrees(mc.gameRenderer.getMainCamera().yRot());
         */
//?} else {
        /*        return Mth.wrapDegrees(mc.gameRenderer.getMainCamera().getYRot());
         */
//?}
    }

    public static float pitch(Minecraft mc) {
//? if >=26.2 {
        return mc.gameRenderer.mainCamera().xRot();
//?} else if >=1.21.11 {
        /*        return mc.gameRenderer.getMainCamera().xRot();
         */
//?} else {
        /*        return mc.gameRenderer.getMainCamera().getXRot();
         */
//?}
    }

    public static boolean isGuiForcedHidden(Minecraft mc) {
//? if >=26.2 {
        return mc.gui.hud.isHidden();
//?} else {
        /*        return mc.options.hideGui;
         */
//?}
    }

    public static boolean isLeftClick(int button) {
//? if >=26.3 {
        return button == com.mojang.blaze3d.platform.InputConstants.MOUSE_BUTTON_LEFT;
//?} else {
        /*        return button == 0;
         */
//?}
    }

    public static boolean isShiftDown() {
//? if >=1.21.11 {
        return Minecraft.getInstance().hasShiftDown();
//?} else {
        /*        return net.minecraft.client.gui.screens.Screen.hasShiftDown();
         */
//?}
    }

    public static void playUiSound(String path, float pitch) {
//? if >=1.21.11 {
        var id = net.minecraft.resources.Identifier.withDefaultNamespace(path);
//?} else {
        /*        var id = net.minecraft.resources.ResourceLocation.withDefaultNamespace(path);
         */
//?}
        var event = net.minecraft.sounds.SoundEvent.createVariableRangeEvent(id);
        Minecraft.getInstance().getSoundManager().play(
                net.minecraft.client.resources.sounds.SimpleSoundInstance.forUI(event, pitch));
    }

    public static boolean isScreenOpen(Minecraft mc, Class<?> type) {
//? if >=26.2 {
        return type.isInstance(mc.gui.screen());
//?} else {
        /*        return type.isInstance(mc.screen);
         */
//?}
    }
}