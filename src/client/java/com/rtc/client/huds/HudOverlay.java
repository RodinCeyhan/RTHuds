package com.rtc.client.huds;

import com.rtc.client.RTHudsClient;
import net.minecraft.client.Minecraft;

//? if >=1.21.6 {
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
//?} else {
/*import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
 */
//?}

//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
 */
//?}

@SuppressWarnings({"SameParameterValue", "unused"})
public final class HudOverlay {
    private HudOverlay() {
    }

    //? if >=1.21.11 {
    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(RTHudsClient.MOD_ID, path);
    }
//?} else if >=1.21.6 {
    /*    private static ResourceLocation id(String path) { return ResourceLocation.fromNamespaceAndPath(RTHudsClient.MOD_ID, path); }
     */
//?}

    public static void register() {
//? if >=1.21.6 {
        HudElementRegistry.addLast(id("hud"), (graphics, tickCounter) -> render(new HudCanvas(graphics)));
//?} else {
        /*        HudRenderCallback.EVENT.register((graphics, tickCounter) -> render(new HudCanvas(graphics)));
         */
//?}
    }

    private static void render(HudCanvas c) {
        Minecraft mc = Minecraft.getInstance();

        boolean editorOpen = VersionCompat.isScreenOpen(mc, HudEditorScreen.class);
        if (editorOpen) return;

        boolean forceHidden = HudConfig.HideGui() && VersionCompat.isGuiForcedHidden(mc);
        if (forceHidden) return;

        for (HudElement e : HudRegistry.ELEMENTS) {
            if (!e.enabled || !e.visible.getAsBoolean()) continue;
            e.measure();
            e.clampToScreen();
            e.draw(c, 0x66000000, e.textShadow, false);
        }
        ArmorHudRenderer.render(c, mc);
        EffectsHudRenderer.render(c, mc);
    }
}