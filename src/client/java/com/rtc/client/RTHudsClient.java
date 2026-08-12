package com.rtc.client;

import com.rtc.client.command.HudsCommand;
import com.rtc.client.huds.ArmorHUD;
import com.rtc.client.keys.KeyBindings;
import com.rtc.client.gui.ConfigScreen;
import com.rtc.client.config.ModConfig;
import com.rtc.client.huds.InfoHUD;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
//? if >=26.1.2 {
import net.minecraft.resources.Identifier;
//?}

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class RTHudsClient implements ClientModInitializer {
    public static final String MOD_ID = "rthuds";

    //? if >=26.1.2 {
    public static final Identifier HUD_ID = Identifier.fromNamespaceAndPath("rthuds", "hud");
    public static final Identifier ARMOR_ID = Identifier.fromNamespaceAndPath("rthuds", "armor");
    //?}
    public static boolean settingsMenu = false;

    @Override
    public void onInitializeClient() {
        ModConfig.INSTANCE.load();
        KeyBindings.register();
        InfoHUD.register();
        ArmorHUD.register();
        HudsCommand.register();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> ModConfig.INSTANCE.save()));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            if (KeyBindings.toggleHud.consumeClick()) {
                boolean state = ModConfig.toggleHud(ModConfig.keyHudType.INFO);
                sendHudMessage("rthuds.settings.hud", state);
            }

            if (KeyBindings.openConfig.consumeClick()) {
                if (client.player != null) {
                    //? if =26.2 {
                    client.gui.setScreen(ConfigScreen.create(client.gui.screen()));
                    //?} else {
                    /*client.setScreen(ConfigScreen.create(client.screen));
                     */
                    //?}
                }
            }

            if (KeyBindings.toggleArmorHud.consumeClick()) {
                boolean state = ModConfig.toggleHud(ModConfig.keyHudType.ARMOR);
                sendHudMessage("rthuds.settings.armorhud", state);
            }

        });

        ClientTickEvents.END_CLIENT_TICK.register(this::settingsMenuTick);
    }

    private void settingsMenuTick(Minecraft minecraft) {
        if (settingsMenu) {
            settingsMenu = false;
            Minecraft client = Minecraft.getInstance();
            //? if =26.2 {
            client.gui.setScreen(ConfigScreen.create(client.gui.screen()));
            //?} else {
            /*client.setScreen(ConfigScreen.create(client.screen));
             */
            //?}
        }
    }

    private void sendHudMessage(String key, boolean state) {
        Minecraft mc = Minecraft.getInstance();
        Component stateText = Component.translatable(state ? "rthuds.option.on" : "rthuds.option.off");
        ChatFormatting color = state ? ChatFormatting.GREEN : ChatFormatting.RED;

        Component coloredState = stateText.copy()
                .withStyle(Style.EMPTY.withColor(color).withBold(true));

        Component prefix = Component.translatable(key)
                .withStyle(ChatFormatting.WHITE)
                .append(Component.literal(": "));

        Component fullMessage = Component.empty()
                .append(prefix)
                .append(coloredState);

        if (mc.player != null) {
            //? if >=26.1.2 {
            mc.player.sendOverlayMessage(fullMessage);
            //?} else {
            /* mc.player.displayClientMessage(fullMessage, true);
             */
            //?}
        }
    }

    public static void onConfigSaved(ModConfig config) {
    }
}
