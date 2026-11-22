package com.rtc.client.mixin;

import com.rtc.client.huds.ArmorHudRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

//? if >=26.2 {
import net.minecraft.client.gui.Hud;
//?} else {
/*import net.minecraft.client.gui.Gui;
 */
//?}

//? if >=26.2 {
@Mixin(Hud.class)
//?} else {
/*@Mixin(Gui.class)
 */
//?}
public abstract class HotbarOffhandMixin {

//? if >=26.1.2 {
    @Redirect(method = "extractItemHotbar", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 0))
//?} else {
    /*@Redirect(method = "renderItemHotbar", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 0))
     */
//?}
    private boolean rthuds$hideOffhandBackground(ItemStack stack) {
        return ArmorHudRenderer.hidesVanillaOffhand() || stack.isEmpty();
    }

//? if >=26.1.2 {
    @Redirect(method = "extractItemHotbar", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 1))
//?} else {
    /*@Redirect(method = "renderItemHotbar", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 1))
     */
//?}
    private boolean rthuds$hideOffhandItem(ItemStack stack) {
        return ArmorHudRenderer.hidesVanillaOffhand() || stack.isEmpty();
    }
}
