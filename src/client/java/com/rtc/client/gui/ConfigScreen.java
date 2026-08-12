package com.rtc.client.gui;

import com.rtc.client.config.ModConfig;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;

import dev.isxander.yacl3.gui.controllers.cycling.EnumController;
import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.minecraft.client.Minecraft;
//? if >=26.1.2 {
import net.minecraft.client.gui.GuiGraphicsExtractor;
//?} else {
/*import net.minecraft.client.gui.GuiGraphics;
 */
//?}
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import java.util.List;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
public class ConfigScreen {

    public static Screen create(Screen parent) {
        ModConfig defaults = ModConfig.INSTANCE.defaults();
        ModConfig options = ModConfig.get();
        Preview preview = new Preview();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("rthuds"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("general.settings"))
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("open.close.settings"))
                                .description(OptionDescription.of(Component.translatable("open.close.settings.description")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("infohud"))
                                        .description(OptionDescription.of(Component.translatable("infohud.description")))
                                        .binding(defaults.infoHud, () -> options.infoHud, val -> options.infoHud = val)
                                        .controller(opt -> BooleanControllerBuilder.create(opt)
                                                .formatValue(val -> val
                                                        ? Component.translatable("hud.on")
                                                        : Component.translatable("hud.off"))
                                                .coloured(true))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("armorhud"))
                                        .description(OptionDescription.of(Component.translatable("armorhud.description")))
                                        .binding(defaults.armorHud, () -> options.armorHud, val -> options.armorHud = val)
                                        .controller(opt -> BooleanControllerBuilder.create(opt)
                                                .formatValue(val -> val
                                                        ? Component.translatable("hud.on")
                                                        : Component.translatable("hud.off"))
                                                .coloured(true))
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("hide.settings"))
                                .description(OptionDescription.of(Component.translatable("hide.settings.description")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("hide.hidegui"))
                                        .description(OptionDescription.of(Component.translatable("hide.hidegui.description")))
                                        .binding(defaults.showHideGui, () -> options.showHideGui, val -> options.showHideGui = val)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("hide.debuggui"))
                                        .description(OptionDescription.of(Component.translatable("hide.debuggui.description")))
                                        .binding(defaults.showDebugGui, () -> options.showDebugGui, val -> options.showDebugGui = val)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("appearance.settings"))
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("appearance.settings.infohud"))
                                .description(OptionDescription.of(Component.translatable("appearance.settings.infohud.description")))
                                .option(Option.<ModConfig.InfoHudDirection>createBuilder()
                                        .name(Component.translatable("appearance.settings.infohud.direction"))
                                        .description(OptionDescription.of(Component.translatable("appearance.settings.infohud.direction.description")))
                                        .binding(defaults.infoHudDirection, () -> options.infoHudDirection, (value) -> options.infoHudDirection = value)
                                        .customController(opt -> new EnumController<>(opt, ModConfig.InfoHudDirection.class))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("appearance.settings.infohud.textshadow"))
                                        .description(OptionDescription.of(Component.translatable("appearance.settings.infohud.textshadow.description")))
                                        .binding(defaults.infoHudTextShadow, () -> options.infoHudTextShadow, val -> options.infoHudTextShadow = val)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<ModConfig.HudBackground>createBuilder()
                                        .name(Component.translatable("appearance.settings.infohud.background"))
                                        .description(OptionDescription.of(Component.translatable("appearance.settings.infohud.background.description")))
                                        .binding(defaults.infoHudBackground, () -> options.infoHudBackground, (value) -> options.infoHudBackground = value)
                                        .customController(opt -> new EnumController<>(opt, ModConfig.HudBackground.class))
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Component.translatable("appearance.settings.infohud.xposition"))
                                        .description(OptionDescription.of(Component.translatable("appearance.settings.infohud.xposition.description")))
                                        .binding(defaults.infoHudPosX, () -> options.infoHudPosX, val -> options.infoHudPosX = val)
                                        .controller(option -> IntegerSliderControllerBuilder.create(option)
                                                .range(0, 100)
                                                .step(1)
                                                .formatValue(val -> Component.translatable(val + "")))
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Component.translatable("appearance.settings.infohud.yposition"))
                                        .description(OptionDescription.of(Component.translatable("appearance.settings.infohud.yposition.description")))
                                        .binding(defaults.infoHudPosY, () -> options.infoHudPosY, val -> options.infoHudPosY = val)
                                        .controller(option -> IntegerSliderControllerBuilder.create(option)
                                                .range(0, 100)
                                                .step(1)
                                                .formatValue(val -> Component.translatable(val + "")))
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Component.translatable("appearance.settings.infohud.decimalplaces"))
                                        .description(OptionDescription.of(Component.translatable("appearance.settings.infohud.decimalplaces.description")))
                                        .binding(defaults.infoHudDecimalPlaces, () -> options.infoHudDecimalPlaces, val -> options.infoHudDecimalPlaces = val)
                                        .controller(option -> IntegerSliderControllerBuilder.create(option)
                                                .range(0, 6)
                                                .step(1)
                                                .formatValue(val -> Component.translatable(String.valueOf(val))))
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("appearance.settings.armorhud"))
                                .description(OptionDescription.of(Component.translatable("appearance.settings.armorhud.description")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("appearance.settings.armorhud.textshadow"))
                                        .description(OptionDescription.of(Component.translatable("appearance.settings.armorhud.textshadow.description")))
                                        .binding(defaults.armorTextShadow, () -> options.armorTextShadow, val -> options.armorTextShadow = val)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<ModConfig.HudBackground>createBuilder()
                                        .name(Component.translatable("appearance.settings.armorhud.background"))
                                        .description(OptionDescription.of(Component.translatable("appearance.settings.armorhud.background.description")))
                                        .binding(defaults.armorHudBackground, () -> options.armorHudBackground, (value) -> options.armorHudBackground = value)
                                        .customController(opt -> new EnumController<>(opt, ModConfig.HudBackground.class))
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("appearance.settings.armorhud.durabilitybar"))
                                        .description(OptionDescription.of(Component.translatable("appearance.settings.armorhud.durabilitybar.description")))
                                        .binding(defaults.armorHudBar, () -> options.armorHudBar, val -> options.armorHudBar = val)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("appearance.settings.armorhud.emptyslots"))
                                        .description(OptionDescription.of(Component.translatable("appearance.settings.armorhud.emptyslots.description")))
                                        .binding(defaults.armorHudEmpytSlots, () -> options.armorHudEmpytSlots, val -> options.armorHudEmpytSlots = val)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Component.translatable("appearance.settings.armorhud.handitems"))
                                        .description(OptionDescription.of(Component.translatable("appearance.settings.armorhud.handitems.description")))
                                        .binding(defaults.armorHudHanditems, () -> options.armorHudHanditems, val -> options.armorHudHanditems = val)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Float>createBuilder()
                                        .name(Component.translatable("appearance.settings.armorhud.hudscale"))
                                        .description(OptionDescription.of(Component.translatable("appearance.settings.armorhud.hudscale.description")))
                                        .binding(defaults.armorHudScale, () -> options.armorHudScale, val -> options.armorHudScale = val)
                                        .controller(option -> FloatSliderControllerBuilder.create(option)
                                                .range(0.5f, 1.5f)
                                                .step(0.01f)
                                                .formatValue(val -> Component.translatable(val + "")))
                                        .build())
                                .option(Option.<ModConfig.ArmorHudLocation>createBuilder()
                                        .name(Component.translatable("appearance.settings.armorhud.location"))
                                        .description(OptionDescription.of(Component.translatable("appearance.settings.armorhud.location.description")))
                                        .binding(defaults.armorHudLocation, () -> options.armorHudLocation, (value) -> options.armorHudLocation = value)
                                        .customController(opt -> new EnumController<>(opt, ModConfig.ArmorHudLocation.class))
                                        .build())
                                .option(Option.<ModConfig.ArmorHudHealth>createBuilder()
                                        .name(Component.translatable("appearance.settings.armorhud.textstyle"))
                                        .description(OptionDescription.of(Component.translatable("appearance.settings.armorhud.textstyle.description")))
                                        .binding(defaults.armorHudHealth, () -> options.armorHudHealth, (value) -> options.armorHudHealth = value)
                                        .customController(opt -> new EnumController<>(opt, ModConfig.ArmorHudHealth.class))
                                        .build())
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("color.settings"))
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("color.settings.infohud"))
                                .description(OptionDescription.createBuilder().customImage(preview).build())
                                .option(Option.<Color>createBuilder()
                                        .name(Component.translatable("color.settings.infohud.coordinates"))
                                        .description(OptionDescription.createBuilder().customImage(preview).build())
                                        .addListener((option, event) -> preview.coordsColor = fixAlpha(option.pendingValue().getRGB()))
                                        .binding(
                                                fromArgb(ModConfig.defaultcoordinatesColor),
                                                () -> fromArgb(options.coordinatesColor),
                                                val -> options.coordinatesColor = fixAlpha(val.getRGB())
                                        )
                                        .controller(option -> ColorControllerBuilder.create(option).allowAlpha(false))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Component.translatable("color.settings.infohud.yaw"))
                                        .description(OptionDescription.createBuilder().customImage(preview).build())
                                        .addListener((option, event) -> preview.yawColor = fixAlpha(option.pendingValue().getRGB()))
                                        .binding(
                                                fromArgb(ModConfig.defaultyawColor),
                                                () -> fromArgb(options.yawColor),
                                                val -> options.yawColor = fixAlpha(val.getRGB())
                                        )
                                        .controller(option -> ColorControllerBuilder.create(option).allowAlpha(false))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Component.translatable("color.settings.infohud.pitch"))
                                        .description(OptionDescription.createBuilder().customImage(preview).build())
                                        .addListener((option, event) -> preview.pitchColor = fixAlpha(option.pendingValue().getRGB()))
                                        .binding(
                                                fromArgb(ModConfig.defaultpitchColor),
                                                () -> fromArgb(options.pitchColor),
                                                val -> options.pitchColor = fixAlpha(val.getRGB())
                                        )
                                        .controller(option -> ColorControllerBuilder.create(option).allowAlpha(false))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Component.translatable("color.settings.infohud.direction"))
                                        .description(OptionDescription.createBuilder().customImage(preview).build())
                                        .addListener((option, event) -> preview.directionColor = fixAlpha(option.pendingValue().getRGB()))
                                        .binding(
                                                fromArgb(ModConfig.defaultdirectionColor),
                                                () -> fromArgb(options.directionColor),
                                                val -> options.directionColor = fixAlpha(val.getRGB())
                                        )
                                        .controller(option -> ColorControllerBuilder.create(option).allowAlpha(false))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Component.translatable("color.settings.infohud.fps"))
                                        .description(OptionDescription.createBuilder().customImage(preview).build())
                                        .addListener((option, event) -> preview.fpsColor = fixAlpha(option.pendingValue().getRGB()))
                                        .binding(
                                                fromArgb(ModConfig.defaultfpsColor),
                                                () -> fromArgb(options.fpsColor),
                                                val -> options.fpsColor = fixAlpha(val.getRGB())
                                        )
                                        .controller(option -> ColorControllerBuilder.create(option).allowAlpha(false))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Component.translatable("color.settings.infohud.converter"))
                                        .description(OptionDescription.createBuilder().customImage(preview).build())
                                        .addListener((option, event) -> preview.converterColor = fixAlpha(option.pendingValue().getRGB()))
                                        .binding(
                                                fromArgb(ModConfig.defaultcoordinateConverterColor),
                                                () -> fromArgb(options.coordinateConverterColor),
                                                val -> options.coordinateConverterColor = fixAlpha(val.getRGB())
                                        )
                                        .controller(option -> ColorControllerBuilder.create(option).allowAlpha(false))
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.translatable("color.settings.armorhud"))
                                .description(OptionDescription.createBuilder().customImage(preview).build())
                                .option(Option.<Color>createBuilder()
                                        .name(Component.translatable("color.settings.armorhud.textcolor"))
                                        .description(OptionDescription.createBuilder().customImage(preview).build())
                                        .addListener((option, event) -> preview.armorHudText = fixAlpha(option.pendingValue().getRGB()))
                                        .binding(
                                                fromArgb(ModConfig.defaultarmorHudHealthColor),
                                                () -> fromArgb(options.armorHudHealthColor),
                                                val -> options.armorHudHealthColor = fixAlpha(val.getRGB())
                                        )
                                        .controller(option -> ColorControllerBuilder.create(option).allowAlpha(false))
                                        .build())
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("display.settings"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("display.settings.coordinates"))
                                .description(OptionDescription.of(Component.translatable("display.settings.coordinates.description")))
                                .binding(defaults.showCoordinates, () -> options.showCoordinates, val -> options.showCoordinates = val)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("display.settings.yaw"))
                                .description(OptionDescription.of(Component.translatable("display.settings.yaw.direction")))
                                .binding(defaults.showYaw, () -> options.showYaw, val -> options.showYaw = val)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("display.settings.pitch"))
                                .description(OptionDescription.of(Component.translatable("display.settings.pitch.direction")))
                                .binding(defaults.showPitch, () -> options.showPitch, val -> options.showPitch = val)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("display.settings.direction"))
                                .description(OptionDescription.of(Component.translatable("display.settings.direction.description")))
                                .binding(defaults.showDirection, () -> options.showDirection, val -> options.showDirection = val)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("display.settings.fps"))
                                .description(OptionDescription.of(Component.translatable("display.settings.fps.direction")))
                                .binding(defaults.showFPS, () -> options.showFPS, val -> options.showFPS = val)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("display.settings.converter"))
                                .description(OptionDescription.of(Component.translatable("display.settings.converter.direction")))
                                .binding(defaults.showCoordinatesConverter, () -> options.showCoordinatesConverter, val -> options.showCoordinatesConverter = val)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())
                .save(() -> ModConfig.INSTANCE.save())
                .build()
                .generateScreen(parent);
    }

    private static Color fromArgb(int i) {
        return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF, (i >> 24) & 0xFF);
    }

    private static int fixAlpha(int color) {
        if (toAlpha.applyAsInt(color) < 4) {
            return withAlpha.applyAsInt(color, fromAlpha.applyAsInt(4));
        }
        return color;
    }

    private static final IntUnaryOperator toAlpha = (value) -> (value >> 24 & 255);
    private static final IntUnaryOperator fromAlpha = (value) -> (value * 16777216);
    private static final IntBinaryOperator withAlpha = (value, alpha) ->
            (value - (fromAlpha.applyAsInt(toAlpha.applyAsInt(value))) + alpha);

    private static class Preview implements ImageRenderer {

        ModConfig options = ModConfig.get();
        public int coordsColor = options.coordinatesColor;
        public int yawColor = options.yawColor;
        public int pitchColor = options.pitchColor;
        public int directionColor = options.directionColor;
        public int fpsColor = options.fpsColor;
        public int converterColor = options.coordinateConverterColor;
        public int armorHudText = options.armorHudHealthColor;

        @Override
        //? if >=26.1.2 {
        public int render(GuiGraphicsExtractor graphics, int x, int startY, int width, float delta) {
        //?} else {
        /*public int render(GuiGraphics graphics, int x, int startY, int width, float delta) {
        */
        //?}
            Minecraft mc = Minecraft.getInstance();

            int py = startY + 5;
            int valueColor = 0xFFFFFF;
            List<Component> lines = new ArrayList<>();

            lines.add(Component.literal("XYZ: ").withColor(coordsColor)
                    .append(Component.literal("0.000").withColor(valueColor))
                    .append(Component.literal(" / ").withColor(coordsColor))
                    .append(Component.literal("0.00000").withColor(valueColor))
                    .append(Component.literal(" / ").withColor(coordsColor))
                    .append(Component.literal("0.000").withColor(valueColor)));

            lines.add(Component.literal("Yaw:").withColor(yawColor).append(Component.literal(" 0.0").withColor(valueColor)));
            lines.add(Component.literal("Pitch: ").withColor(pitchColor).append(Component.literal("0.0").withColor(valueColor)));

            lines.add(Component.translatable("rthuds.hud.direction").withColor(directionColor).append(Component.translatable("rthuds.hud.direction.north").withColor(valueColor)));

            lines.add(Component.literal("FPS:").withColor(fpsColor).append(Component.literal(" 144").withColor(valueColor)));

            lines.add(Component.translatable("rthuds.text.nether").withColor(converterColor)
                    .append(Component.literal(" "))
                    .append(Component.literal("0.000").withColor(valueColor))
                    .append(Component.literal(" / ").withColor(converterColor))
                    .append(Component.literal("0.00000").withColor(valueColor))
                    .append(Component.literal(" / ").withColor(converterColor))
                    .append(Component.literal("0.000").withColor(valueColor)));


            ItemStack[] armorItems = new ItemStack[]{
                    new ItemStack(Items.DIAMOND_HELMET),
                    new ItemStack(Items.DIAMOND_CHESTPLATE),
                    new ItemStack(Items.DIAMOND_LEGGINGS),
                    new ItemStack(Items.DIAMOND_BOOTS)
            };

            int textHeight = mc.font.lineHeight;
            int lineSpacing = 2;
            int textBlockHeight = lines.size() * (textHeight + lineSpacing);
            int armorStartY = py + textBlockHeight + 6;
            int[] values = {363, 528, 495, 429};

            for (int i = 0; i < armorItems.length; i++) {
                int yOffset = armorStartY + (i * 20);

                String armorHealthText = "";

                switch (ModConfig.armorHudHealth()) {
                    case NONE -> armorHealthText = "None";
                    case DAMAGE -> armorHealthText = "0";
                    case PERCENTAGE -> armorHealthText = "%100";
                    case REMAINING -> armorHealthText = String.valueOf(values[i]);
                    case REMAININGMAX -> armorHealthText = values[i] + "/" + values[i];
                }

                //? if >=26.1.2 {
                graphics.item(armorItems[i], x, yOffset);
                //?} else {
                /*graphics.renderItem(armorItems[i], x, yOffset);*/
                //?}

                //?if >=26.1.2 {
                if (!armorHealthText.isEmpty()) {
                    graphics.text(Minecraft.getInstance().font, armorHealthText, x + 16 + 4, yOffset + 4, armorHudText, ModConfig.ArmorTextShadow());
                } else {
                    graphics.text(Minecraft.getInstance().font, "%100", x + 16 + 4, yOffset + 4, armorHudText, ModConfig.ArmorTextShadow());
                }
                //?} else {
                /*if (!armorHealthText.isEmpty()) {
                    graphics.drawString(Minecraft.getInstance().font, armorHealthText, x + 16 + 4, yOffset + 4, armorHudText, ModConfig.ArmorTextShadow());
                } else {
                    graphics.drawString(Minecraft.getInstance().font, "%100", x + 16 + 4, yOffset + 4, armorHudText, ModConfig.ArmorTextShadow());
                }*/
                //?}
            }

            for (int i = 0; i < lines.size(); i++) {
                Component lineItem = lines.get(i);
                int lineY = py + (i * (textHeight + lineSpacing));
                //?if >=26.1.2 {
                graphics.text(mc.font, lineItem, x, lineY, 0xFFFFFFFF, options.infoHudTextShadow);
                //?} else {
                /*graphics.drawString(mc.font, lineItem, x, lineY, 0xFFFFFFFF, options.infoHudTextShadow);*/
                //?}
            }

            return 20;
        }

        @Override
        public void close() {
        }
    }
}
