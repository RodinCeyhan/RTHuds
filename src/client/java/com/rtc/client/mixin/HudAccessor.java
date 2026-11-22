package com.rtc.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//? if >=26.2 {
import net.minecraft.client.gui.Hud;
//?} else {
/*import net.minecraft.client.gui.Gui;
 */
//?}

//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
 */
//?}

//? if >=26.2 {
@Mixin(Hud.class)
//?} else {
/*@Mixin(Gui.class)
 */
//?}
public interface HudAccessor {
    @Accessor("HOTBAR_SPRITE")
//? if >=1.21.11 {
    static Identifier getHotbarSprite() {
//?} else {
    /*static ResourceLocation getHotbarSprite() {
     */
//?}
        throw new AssertionError();
    }
}
