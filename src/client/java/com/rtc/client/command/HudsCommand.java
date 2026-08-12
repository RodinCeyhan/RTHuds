package com.rtc.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import com.rtc.client.RTHudsClient;
import com.rtc.client.config.ModConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

//? if >=26.1.2 {
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
//?} else {
/*import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
*/
//?}

public class HudsCommand {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register(HudsCommand::command);
    }

    private static void command(
            CommandDispatcher<FabricClientCommandSource> dispatcher,
            CommandBuildContext commandBuildContext
    ) {
        dispatcher.register(
                //? if >=26.1.2 {
                ClientCommands.literal("rthuds")
                //?} else {
                /*ClientCommandManager.literal("rthuds")*/
                //?}
                        .executes(HudsCommand::showMenu)
                        .then(
                                //? if >=26.1.2 {
                                ClientCommands.argument("hudType", StringArgumentType.word())
                                //?} else {
                                /*ClientCommandManager.argument("hudType",StringArgumentType.word())*/
                                //?}
                                        .suggests(HudsCommand::suggestHudTypes)
                                        .then(
                                                //? if >=26.1.2 {
                                                ClientCommands.argument("toggle", StringArgumentType.word()
                                                )
                                                //?} else {
                                                /*ClientCommandManager.argument("toggle", StringArgumentType.word())*/
                                                //?}
                                                .suggests(HudsCommand::suggestToggle)
                                                .executes(HudsCommand::toggleHud)
                                        )
                        )
        );
    }

    private static CompletableFuture<Suggestions> suggestHudTypes(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(
                List.of("info", "armor"),
                builder
        );
    }

    private static CompletableFuture<Suggestions> suggestToggle(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(
                List.of("toggle"),
                builder
        );
    }

    private static int showMenu(CommandContext<FabricClientCommandSource> context) {
        RTHudsClient.settingsMenu = true;
        return 1;
    }

    private static int toggleHud(CommandContext<FabricClientCommandSource> context) {
        String hudType = StringArgumentType.getString(context, "hudType");
        String toggle = StringArgumentType.getString(context, "toggle");

        if (!toggle.equalsIgnoreCase("toggle")) {
            return 0;
        }

        switch (hudType.toLowerCase()) {
            case "info" -> ModConfig.toggleHud(ModConfig.keyHudType.INFO);
            case "armor" -> ModConfig.toggleHud(ModConfig.keyHudType.ARMOR);
        }

        return 1;
    }
}