package com.rtc.client.keys;

//? if >=26.1.2 {
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
//?} else {
/*import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
 */
//?}
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?}

public class KeyBindings {

    //? if >=1.21.11 {
    public static final KeyMapping.Category RTHUDS_CATEGORY = KeyMapping.Category.register(Identifier.parse("key.categories.rthuds"));
    //?}

    public static KeyMapping toggleHud;
    public static KeyMapping openConfig;
    public static KeyMapping toggleArmorHud;

    public static void register() {
        //? if >=26.1.2 {
        toggleHud = KeyMappingHelper.registerKeyMapping(new KeyMapping(
        //?} else {
        /*toggleHud = KeyBindingHelper.registerKeyBinding(new KeyMapping(
         */
        //?}
                "key.rthuds.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                //? if >=1.21.11 {
                RTHUDS_CATEGORY
                //?} else {
                /*"key.categories.rthuds"*/
                //?}
        ));

        //? if >=26.1.2 {
        openConfig = KeyMappingHelper.registerKeyMapping(new KeyMapping(
        //?} else {
        /*openConfig = KeyBindingHelper.registerKeyBinding(new KeyMapping(
         */
        //?}
                "key.rthuds.settignsmenu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                //? if >=1.21.11 {
                RTHUDS_CATEGORY
                //?} else {
                /*"key.categories.rthuds"*/
                //?}
        ));

        //? if >=26.1.2 {
        toggleArmorHud = KeyMappingHelper.registerKeyMapping(new KeyMapping(
        //?} else {
        /*toggleArmorHud = KeyBindingHelper.registerKeyBinding(new KeyMapping(
         */
        //?}
                "key.rthuds.togglearmor",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                //? if >=1.21.11 {
                RTHUDS_CATEGORY
                //?} else {
                /*"key.categories.rthuds"*/
                //?}
        ));
    }
}