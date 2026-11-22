package com.rtc.client.huds;

import com.rtc.client.RTHudsClient;

//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
 */
//?}

public final class ArmorHudTextures {
    private ArmorHudTextures() {}

//? if >=1.21.11 {
    public static final Identifier BLACK_EMPTY_HELMET = Identifier.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/helmet.png");
    public static final Identifier BLACK_EMPTY_CHEST = Identifier.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/chestplate.png");
    public static final Identifier BLACK_EMPTY_LEGS = Identifier.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/leggings.png");
    public static final Identifier BLACK_EMPTY_BOOTS = Identifier.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/boots.png");
    public static final Identifier BLACK_EMPTY_HAND = Identifier.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/sword.png");
    public static final Identifier BLACK_EMPTY_OFFHAND = Identifier.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/shield.png");
    public static final Identifier WHITE_EMPTY_HELMET = Identifier.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/white_helmet.png");
    public static final Identifier WHITE_EMPTY_CHEST = Identifier.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/white_chestplate.png");
    public static final Identifier WHITE_EMPTY_LEGS = Identifier.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/white_leggings.png");
    public static final Identifier WHITE_EMPTY_BOOTS = Identifier.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/white_boots.png");
    public static final Identifier WHITE_EMPTY_HAND = Identifier.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/white_sword.png");
    public static final Identifier WHITE_EMPTY_OFFHAND = Identifier.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/white_shield.png");
    public static final Identifier[] BLACK_EMPTY_TEXTURES = {BLACK_EMPTY_HELMET, BLACK_EMPTY_CHEST, BLACK_EMPTY_LEGS, BLACK_EMPTY_BOOTS, BLACK_EMPTY_HAND, BLACK_EMPTY_OFFHAND};
    public static final Identifier[] WHITE_EMPTY_TEXTURES = {WHITE_EMPTY_HELMET, WHITE_EMPTY_CHEST, WHITE_EMPTY_LEGS, WHITE_EMPTY_BOOTS, WHITE_EMPTY_HAND, WHITE_EMPTY_OFFHAND};
//?} else {
/*    public static final ResourceLocation BLACK_EMPTY_HELMET = ResourceLocation.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/helmet.png");
    public static final ResourceLocation BLACK_EMPTY_CHEST = ResourceLocation.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/chestplate.png");
    public static final ResourceLocation BLACK_EMPTY_LEGS = ResourceLocation.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/leggings.png");
    public static final ResourceLocation BLACK_EMPTY_BOOTS = ResourceLocation.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/boots.png");
    public static final ResourceLocation BLACK_EMPTY_HAND = ResourceLocation.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/sword.png");
    public static final ResourceLocation BLACK_EMPTY_OFFHAND = ResourceLocation.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/shield.png");
    public static final ResourceLocation WHITE_EMPTY_HELMET = ResourceLocation.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/white_helmet.png");
    public static final ResourceLocation WHITE_EMPTY_CHEST = ResourceLocation.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/white_chestplate.png");
    public static final ResourceLocation WHITE_EMPTY_LEGS = ResourceLocation.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/white_leggings.png");
    public static final ResourceLocation WHITE_EMPTY_BOOTS = ResourceLocation.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/white_boots.png");
    public static final ResourceLocation WHITE_EMPTY_HAND = ResourceLocation.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/white_sword.png");
    public static final ResourceLocation WHITE_EMPTY_OFFHAND = ResourceLocation.fromNamespaceAndPath(RTHudsClient.MOD_ID, "textures/white_shield.png");
    public static final ResourceLocation[] BLACK_EMPTY_TEXTURES = {BLACK_EMPTY_HELMET, BLACK_EMPTY_CHEST, BLACK_EMPTY_LEGS, BLACK_EMPTY_BOOTS, BLACK_EMPTY_HAND, BLACK_EMPTY_OFFHAND};
    public static final ResourceLocation[] WHITE_EMPTY_TEXTURES = {WHITE_EMPTY_HELMET, WHITE_EMPTY_CHEST, WHITE_EMPTY_LEGS, WHITE_EMPTY_BOOTS, WHITE_EMPTY_HAND, WHITE_EMPTY_OFFHAND};
 */
//?}
}