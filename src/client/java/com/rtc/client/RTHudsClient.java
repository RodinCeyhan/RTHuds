package com.rtc.client;

import com.rtc.client.armor.ArmorHUD;
import com.rtc.client.config.KeyBindings;
import com.rtc.client.gui.RTHudsConfigScreen;
import com.rtc.client.hud.ConfigMigration;
import com.rtc.client.hud.HudRenderer;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;


@SuppressWarnings("unused")
public class RTHudsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ConfigMigration.migrateOnce();
        MidnightConfig.init("rthuds", RTHudsConfigScreen.class);

        KeyBindings.register();
        HudRenderer.register();
        ArmorHUD.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            if (KeyBindings.toggleHud.consumeClick()) {
                RTHudsConfigScreen.showHud = !RTHudsConfigScreen.showHud;
                RTHudsConfigScreen.write("rthuds");
                boolean newState = RTHudsConfigScreen.showHud;
                Component stateText = Component.translatable(newState ? "rthuds.option.on" : "rthuds.option.off");
                ChatFormatting color = newState ? ChatFormatting.GREEN : ChatFormatting.RED;
                Component coloredState = stateText.copy().withStyle(Style.EMPTY.withColor(color).withBold(true));
                Component prefix = Component.translatable("rthuds.settings.hud").withStyle(ChatFormatting.WHITE).append(Component.literal(": "));
                Component fullMessage = Component.empty().append(prefix).append(coloredState);
                mc.player.displayClientMessage(fullMessage, true);
            }

            if (KeyBindings.openConfig.consumeClick()) {
                Screen configScreen = MidnightConfig.getScreen(client.screen, "rthuds");
                client.setScreen(configScreen);
            }

            if (KeyBindings.toggleArmorHud.consumeClick()) {
                RTHudsConfigScreen.ArmorHUD = !RTHudsConfigScreen.ArmorHUD;
                RTHudsConfigScreen.write("rthuds");
                boolean newState = RTHudsConfigScreen.ArmorHUD;
                Component stateText = Component.translatable(newState ? "rthuds.option.on" : "rthuds.option.off");
                ChatFormatting color = newState ? ChatFormatting.GREEN : ChatFormatting.RED;
                Component coloredState = stateText.copy().withStyle(Style.EMPTY.withColor(color).withBold(true));
                Component prefix = Component.translatable("rthuds.settings.armorhud").withStyle(ChatFormatting.WHITE).append(Component.literal(": "));
                Component fullMessage = Component.empty().append(prefix).append(coloredState);
                mc.player.displayClientMessage(fullMessage, true);
            }
        });
    }
}
