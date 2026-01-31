package com.rtc.client.integration;

import com.rtc.client.gui.SettingsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;

public class RthudsModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return parent -> {
            Minecraft mc = Minecraft.getInstance();
            mc.setScreen(new SettingsScreen(parent));
            return mc.screen;
        };
    }
}
