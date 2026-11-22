package com.rtc.client;

import com.rtc.client.command.HudsCommand;
import com.rtc.client.huds.*;
import com.rtc.client.keys.KeyBindings;
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
    public static final Identifier ARMOR_ID = Identifier.fromNamespaceAndPath("rthuds", "armor");
    //?}

    @Override
    public void onInitializeClient() {
        KeyBindings.register();
        HudsCommand.register();
        InfoHudElements.register();
        HudRegistry.load();
        HudOverlay.register();


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            ArmorHudRenderer.tickWarning(mc);

            if (KeyBindings.toggleHud.consumeClick()) {
                HudConfig.infoHud = !HudConfig.InfoHud();
                boolean state = HudConfig.InfoHud();
                sendHudMessage("rthuds.settings.hud", state);
            }

            if (KeyBindings.openConfig.consumeClick()) {
                if (client.player != null) {
                    openEditor(mc);
                }
            }

            if (KeyBindings.toggleArmorHud.consumeClick()) {
                ArmorHudConfig.enabled = !ArmorHudConfig.enabled;
                HudRegistry.save();
                sendHudMessage("rthuds.settings.armorhud", ArmorHudConfig.enabled);
            }

        });
    }


    public static void openEditor(Minecraft mc) {
        mc.execute(() -> {
//? if >=26.2 {
            mc.gui.setScreen(new HudEditorScreen());
//?} else {
            /*        mc.setScreen(new HudEditorScreen());
             */
//?}
        });
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
}
