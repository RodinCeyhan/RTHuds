package com.rtc.client.config;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static KeyMapping toggleHud;
    public static KeyMapping openConfig;
    public static KeyMapping toggleArmorHud;

    public static void register() {
        toggleHud = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.rthuds.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "key.categories.rthuds"
        ));

        openConfig = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.rthuds.settignsmenu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "key.categories.rthuds"
        ));

        toggleArmorHud = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.rthuds.togglearmor",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                "key.categories.rthuds"
        ));
    }
}