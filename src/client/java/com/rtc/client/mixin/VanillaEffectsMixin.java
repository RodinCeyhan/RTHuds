package com.rtc.client.mixin;

import com.rtc.client.huds.EffectsHudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >=26.2 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
//?} else if >=26.1.2 {
/*import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
 */
//?} else {
/*import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
 */
//?}
import net.minecraft.client.DeltaTracker;

//? if >=26.2 {
@Mixin(Hud.class)
//?} else {
/*@Mixin(Gui.class)
 */
//?}
public abstract class VanillaEffectsMixin {

//? if >=26.1.2 {
    @Inject(method = "extractEffects", at = @At("HEAD"), cancellable = true)
    private void rthuds$hideVanillaEffects(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
//?} else {
    /*@Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
    private void rthuds$hideVanillaEffects(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
     */
//?}
        if (EffectsHudRenderer.hidesVanilla()) ci.cancel();
    }
}
