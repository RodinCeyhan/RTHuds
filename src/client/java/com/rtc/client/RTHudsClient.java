package com.rtc.client;

import com.rtc.client.armor.ArmorHUD;
import com.rtc.client.config.KeyBindings;
import com.rtc.client.gui.ConfigScreen;
import com.rtc.client.gui.ModConfig;
import com.rtc.client.hud.HudRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class RTHudsClient implements ClientModInitializer {
    public static final String MOD_ID = "rthuds";

    public static final Identifier HUD_ID = Identifier.fromNamespaceAndPath("rthuds", "hud");
    public static final Identifier ARMOR_ID = Identifier.fromNamespaceAndPath("rthuds", "armor");
    private static boolean settingsMenu = false;

    @Override
    public void onInitializeClient() {
        ModConfig.INSTANCE.load();
        KeyBindings.register();
        HudRenderer.register();
        ArmorHUD.register();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> ModConfig.INSTANCE.save()));

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                literal("rthuds").executes(context -> {
                    settingsMenu = true;
                    return 0;
                })
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            if (KeyBindings.toggleHud.consumeClick()) {
                boolean state = ModConfig.toggleHud(ModConfig.keyHudType.INFO);
                sendHudMessage("rthuds.settings.hud", state);
            }

            if (KeyBindings.openConfig.consumeClick()) {
                if (client.player != null) {
                    client.setScreen(ConfigScreen.create(client.screen));
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
            client.setScreen(ConfigScreen.create(client.screen));
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
            mc.player.sendOverlayMessage(fullMessage);
        }
    }

    public static void onConfigSaved(ModConfig config) {
    }
}
