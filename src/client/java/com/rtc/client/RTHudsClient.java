package com.rtc.client;

import com.rtc.client.armor.ArmorHUD;
import com.rtc.client.config.KeyBindings;
import com.rtc.client.gui.SettingsScreen;
import com.rtc.client.hud.HudRenderer;
import com.rtc.client.utilities.HudConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import static com.rtc.client.gui.SettingsOptions.*;

@SuppressWarnings("unused")
public class RTHudsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        KeyBindings.register();
        HudRenderer.register();
        ArmorHUD.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            if (KeyBindings.toggleHud.consumeClick()) {
                HudConfig.configManager.getConfig().showHud = !HudConfig.configManager.getConfig().showHud;
                HudConfig.configManager.save();
                boolean newState = HudConfig.configManager.getConfig().showHud;
                SHOW_HUD.set(newState);
                Component stateText = Component.translatable(newState ? "rthuds.option.on" : "rthuds.option.off");
                ChatFormatting color = newState ? ChatFormatting.GREEN : ChatFormatting.RED;
                Component coloredState = stateText.copy().withStyle(Style.EMPTY.withColor(color).withBold(true));
                Component prefix = Component.translatable("rthuds.settings.hud").withStyle(ChatFormatting.WHITE).append(Component.literal(": "));
                Component fullMessage = Component.empty().append(prefix).append(coloredState);
                mc.player.displayClientMessage(fullMessage, true);
            }

            if (KeyBindings.openConfig.consumeClick()) {
                if (mc.screen != null) {
                    mc.setScreen(new SettingsScreen(mc.screen));
                } else {
                    mc.setScreen(new SettingsScreen(null));
                }
            }

            if (KeyBindings.toggleArmorHud.consumeClick()) {
                HudConfig.configManager.getConfig().ArmorHUD = !HudConfig.configManager.getConfig().ArmorHUD;
                HudConfig.configManager.save();
                boolean newState = HudConfig.configManager.getConfig().ArmorHUD;
                SHOW_ARMOR_HUD.set(newState);
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
