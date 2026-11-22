package com.rtc.client.huds;

import com.rtc.client.RTHudsClient;
import com.rtc.client.mixin.HudAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

//? if >=26.1.2 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
//?} else if >=1.21.6 {
/*import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
 */
//?} else if >=1.21.3 {
/*import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
 */
//?} else {
/*import net.minecraft.client.gui.GuiGraphics;
 */
//?}

//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
 */
//?}

public final class HudCanvas {
    //? if >=26.1.2 {
    private final GuiGraphicsExtractor g;

    public HudCanvas(GuiGraphicsExtractor g) {
        this.g = g;
    }

    public void text(String s, int x, int y, int color, boolean shadow) {
        g.text(Minecraft.getInstance().font, s, x, y, color, shadow);
    }

    public void text(Component c, int x, int y, int color, boolean shadow) {
        g.text(Minecraft.getInstance().font, c, x, y, color, shadow);
    }
//?} else {
/*    private final GuiGraphics g;

    public HudCanvas(GuiGraphics g) {
        this.g = g;
    }

    public void text(String s, int x, int y, int color, boolean shadow) {
        g.drawString(Minecraft.getInstance().font, s, x, y, color, shadow);
    }

    public void text(Component c, int x, int y, int color, boolean shadow) {
        g.drawString(Minecraft.getInstance().font, c, x, y, color, shadow);
    }
 */
//?}

    private static Object id(String path) {
//? if >=1.21.11 {
        return Identifier.fromNamespaceAndPath(RTHudsClient.MOD_ID, path);
//?} else {
        /*        return ResourceLocation.fromNamespaceAndPath(RTHudsClient.MOD_ID, path);
         */
//?}
    }

    private void blitSprite(Object texture, int x, int y, int w, int h) {
//? if >=1.21.11 {
        var t = (Identifier) texture;
//?} else {
        /*        var t = (ResourceLocation) texture;
         */
//?}
//? if >=1.21.6 {
        g.blitSprite(RenderPipelines.GUI_TEXTURED, t, x, y, w, h);
//?} else if >=1.21.3 {
        /*        g.blitSprite(RenderType::guiTextured, t, x, y, w, h);
         */
//?} else {
        /*        g.blitSprite(t, x, y, w, h);
         */
//?}
    }

    private void blitTexture(Object texture, int x, int y, int w, int h) {
//? if >=1.21.11 {
        var t = (Identifier) texture;
//?} else {
        /*        var t = (ResourceLocation) texture;
         */
//?}
//? if >=1.21.6 {
        g.blit(RenderPipelines.GUI_TEXTURED, t, x, y, 0F, 0F, w, h, w, h);
//?} else if >=1.21.3 {
        /*        g.blit(RenderType::guiTextured, t, x, y, 0F, 0F, w, h, w, h);
         */
//?} else {
        /*        g.blit(t, x, y, 0F, 0F, w, h, w, h);
         */
//?}
    }

    private void pushScaled(float x, float y, float scale) {
//? if >=1.21.6 {
        g.pose().pushMatrix();
        g.pose().translate(x, y);
        g.pose().scale(scale, scale);
//?} else {
        /*        g.pose().pushPose();
        g.pose().translate(x, y, 0F);
        g.pose().scale(scale, scale, 1F);
         */
//?}
    }

    private void popScaled() {
//? if >=1.21.6 {
        g.pose().popMatrix();
//?} else {
        /*        g.pose().popPose();
         */
//?}
    }

    public void rect(int x1, int y1, int x2, int y2, int color) {
        g.fill(x1, y1, x2, y2, color);
    }

    public void border(int x, int y, int w, int h, int color) {
        rect(x, y, x + w, y + 1, color);
        rect(x, y + h - 1, x + w, y + h, color);
        rect(x, y, x + 1, y + h, color);
        rect(x + w - 1, y, x + w, y + h, color);
    }

    public void icon(String name, int x, int y, int width, int height) {
        blitSprite(id(name), x, y, width, height);
    }

    public void plainIcon(String path, int x, int y, int width, int height) {
        blitTexture(id("textures/" + path + ".png"), x, y, width, height);
    }

    public void textScaled(String s, int x, int y, int color, boolean shadow, float scale) {
        pushScaled(x, y, scale);
        text(s, 0, 0, color, shadow);
        popScaled();
    }

    public void textScaled(Component c, int x, int y, int color, boolean shadow, float scale) {
        pushScaled(x, y, scale);
        text(c, 0, 0, color, shadow);
        popScaled();
    }

    public int textWidth(String s) {
        return Minecraft.getInstance().font.width(s);
    }

    public int textWidth(Component c) {
        return Minecraft.getInstance().font.width(c);
    }

    public void clip(int x, int y, int w, int h) {
        g.enableScissor(x, y, x + w, y + h);
    }

    public void endClip() {
        g.disableScissor();
    }

    public void iconRaw(Object texture, int x, int y, int w, int h) {
        blitTexture(texture, x, y, w, h);
    }

    public void effectIcon(Holder<MobEffect> effect, int x, int y, int size) {
//? if >=26.2 {
        blitSprite(net.minecraft.client.gui.Hud.getMobEffectSprite(effect), x, y, size, size);
//?} else if >=1.21.6 {
        /*        blitSprite(net.minecraft.client.gui.Gui.getMobEffectSprite(effect), x, y, size, size);
         */
//?} else if >=1.21.3 {
        /*        g.blitSprite(RenderType::guiTextured, Minecraft.getInstance().getMobEffectTextures().get(effect), x, y, size, size);
         */
//?} else {
        /*        g.blit(x, y, 0, size, size, Minecraft.getInstance().getMobEffectTextures().get(effect));
         */
//?}
    }

    public void item(Player player, ItemStack stack, int x, int y) {
//? if >=26.1.2 {
        g.item(player, stack, x, y, 0);
//?} else {
        /*        g.renderItem(player, stack, x, y, 0);
         */
//?}
    }

    public void itemScaled(Player player, ItemStack stack, int x, int y, float scale) {
        pushScaled(x, y, scale);
        item(player, stack, 0, 0);
        popScaled();
    }

    public void hotbarSlotBackground(int x, int y, int size) {
        var sprite = HudAccessor.getHotbarSprite();
//? if >=1.21.11 {
        var texture = Identifier.fromNamespaceAndPath(sprite.getNamespace(), "textures/gui/sprites/" + sprite.getPath() + ".png");
//?} else {
        /*        var texture = ResourceLocation.fromNamespaceAndPath(sprite.getNamespace(), "textures/gui/sprites/" + sprite.getPath() + ".png");
         */
//?}
        int d = size + 4;
        int dx = x - 2, dy = y - 2;
//? if >=1.21.6 {
        g.blit(RenderPipelines.GUI_TEXTURED, texture, dx, dy, 21F, 1F, d, d, 20, 20, 182, 22);
//?} else if >=1.21.3 {
        /*        g.blit(RenderType::guiTextured, texture, dx, dy, 21F, 1F, d, d, 20, 20, 182, 22);
         */
//?} else {
        /*        g.blit(texture, dx, dy, d, d, 21F, 1F, 20, 20, 182, 22);
         */
//?}
    }
}
