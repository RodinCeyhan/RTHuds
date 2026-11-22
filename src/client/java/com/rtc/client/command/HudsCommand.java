package com.rtc.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.rtc.client.RTHudsClient;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;

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

    private static void command(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext commandBuildContext) {
        dispatcher.register(
                //? if >=26.1.2 {
                ClientCommands.literal("rthuds")
                        //?} else {
                        /*ClientCommandManager.literal("rthuds")*/
                        //?}
                        .executes(HudsCommand::showMenu)
        );
    }

    private static int showMenu(CommandContext<FabricClientCommandSource> context) {
        RTHudsClient.openEditor(Minecraft.getInstance());
        return 1;
    }
}