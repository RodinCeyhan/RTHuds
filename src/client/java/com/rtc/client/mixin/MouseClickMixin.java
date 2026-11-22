package com.rtc.client.mixin;

import com.rtc.client.huds.ClickTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=1.21.9 {
import net.minecraft.client.input.MouseButtonInfo;
//?} else {
/*// 1.21.9 öncesinde bu sınıf yok; metot onPress(long, int, int, int) adıyla bulunuyor
 */
//?}

@Mixin(MouseHandler.class)
public abstract class MouseClickMixin {

    private static boolean rthuds$prevLeft, rthuds$prevRight;

//? if >=1.21.9 {
    @Inject(method = "onButton", at = @At("HEAD"))
    private void rthuds$before(long handle, MouseButtonInfo info, int action, CallbackInfo ci) {
        rthuds$capturePrev();
    }

    @Inject(method = "onButton", at = @At("TAIL"))
    private void rthuds$after(long handle, MouseButtonInfo info, int action, CallbackInfo ci) {
        rthuds$detect();
    }
//?} else {
/*    @Inject(method = "onPress", at = @At("HEAD"))
    private void rthuds$before(long handle, int button, int action, int mods, CallbackInfo ci) {
        rthuds$capturePrev();
    }

    @Inject(method = "onPress", at = @At("TAIL"))
    private void rthuds$after(long handle, int button, int action, int mods, CallbackInfo ci) {
        rthuds$detect();
    }
 */
//?}

    private static void rthuds$capturePrev() {
        MouseHandler mh = Minecraft.getInstance().mouseHandler;
        rthuds$prevLeft = mh.isLeftPressed();
        rthuds$prevRight = mh.isRightPressed();
    }

    private static void rthuds$detect() {
        MouseHandler mh = Minecraft.getInstance().mouseHandler;
        boolean nowLeft = mh.isLeftPressed();
        boolean nowRight = mh.isRightPressed();
        long now = System.currentTimeMillis();

        if (nowLeft && !rthuds$prevLeft) ClickTracker.LEFT.addLast(now);
        if (nowRight && !rthuds$prevRight) ClickTracker.RIGHT.addLast(now);
    }
}