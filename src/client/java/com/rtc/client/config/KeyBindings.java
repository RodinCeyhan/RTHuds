package com.rtc.client.config;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import net.minecraft.resources.Identifier;

public class KeyBindings {

    public static final KeyMapping.Category RTHUDS_CATEGORY = KeyMapping.Category.register(Identifier.parse("key.categories.rthuds"));

    public static KeyMapping toggleHud;
    public static KeyMapping openConfig;
    public static KeyMapping toggleArmorHud;

    public static void register() {
        toggleHud = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.rthuds.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                RTHUDS_CATEGORY
        ));

        openConfig = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.rthuds.settignsmenu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                RTHUDS_CATEGORY
        ));

        toggleArmorHud = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.rthuds.togglearmor",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                RTHUDS_CATEGORY
        ));
    }
}