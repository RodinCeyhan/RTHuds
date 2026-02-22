package com.rtc.client.gui;

import java.util.Arrays;

import com.mojang.serialization.Codec;
import com.rtc.client.hud.ModConfig;
import com.rtc.client.utilities.HudConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

@SuppressWarnings("CodeBlock2Expr")
public class SettingsOptions {

    // General Settings
    public static final OptionInstance<Boolean> SHOW_DEBUG_SCREEN = OptionInstance.createBoolean(
            "rthuds.settings.showF3",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.settings.showF3.tooltip")),
            HudConfig.configManager.getConfig().showDebug,
            (newValue) -> {
                HudConfig.configManager.getConfig().showDebug = newValue;
                HudConfig.configManager.save();
            });
    public static final OptionInstance<Boolean> SHOW_HIDE_GUI = OptionInstance.createBoolean(
            "rthuds.settings.showF1",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.settings.showF1.tooltip")),
            HudConfig.configManager.getConfig().showF1,
            (newValue) -> {
                HudConfig.configManager.getConfig().showF1 = newValue;
                HudConfig.configManager.save();
            });
    public static final OptionInstance<Boolean> SHOW_HUD = OptionInstance.createBoolean(
            "rthuds.settings.hud",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.config.option.show_hud.tooltip")),
            HudConfig.configManager.getConfig().showHud,
            (newValue) -> {
                HudConfig.configManager.getConfig().showHud = newValue;
                HudConfig.configManager.save();
            });
    public static final OptionInstance<Boolean> SHOW_ARMOR_HUD = OptionInstance.createBoolean(
            "rthuds.settings.armorhud",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.armorsettings.hud.tooltip")),
            HudConfig.configManager.getConfig().ArmorHUD,
            (newValue) -> {
                HudConfig.configManager.getConfig().ArmorHUD = newValue;
                HudConfig.configManager.save();
            });

    // Appearance Settings
    public static final OptionInstance<Integer> ARMOR_BACKGROUND_STYLE = new OptionInstance<>(
            "rthuds.settings.background",
            (value) -> Tooltip.create(Component.translatable("rthuds.config.option.background_style.tooltip")),
            (caption, value) -> {
                ModConfig.ArmorBackgroundStyle style = ModConfig.ArmorBackgroundStyle.values()[value];
                return switch (style) {
                    case NONE ->
                            Component.translatable("rthuds.settings.background").append(Component.translatable("rthuds.settings.background.none"));
                    case LIGHT ->
                            Component.translatable("rthuds.settings.background").append(Component.translatable("rthuds.settings.background.light"));
                    case FULL ->
                            Component.translatable("rthuds.settings.background").append(Component.translatable("rthuds.settings.background.full"));
                };
            },
            new OptionInstance.IntRange(0, ModConfig.ArmorBackgroundStyle.values().length - 1),
            HudConfig.configManager.getConfig().armorBackgroundStyle.ordinal(),
            value -> {
                HudConfig.configManager.getConfig().armorBackgroundStyle = ModConfig.ArmorBackgroundStyle.values()[value];
                HudConfig.configManager.save();
            }
    );
    public static final OptionInstance<Boolean> INFOHUD_TEXT_SHADOW = OptionInstance.createBoolean(
            "rthuds.textshadow.info",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.config.option.text_shadow.tooltip")),
            HudConfig.configManager.getConfig().textShadow,
            (newValue) -> {
                HudConfig.configManager.getConfig().textShadow = newValue;
                HudConfig.configManager.save();
            });
    public static final OptionInstance<Boolean> ARMORHUD_TEXT_SHADOW = OptionInstance.createBoolean(
            "rthuds.textshadow.armor",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.config.option.text_shadow.tooltip")),
            HudConfig.configManager.getConfig().ArmorTextShadow,
            (newValue) -> {
                HudConfig.configManager.getConfig().ArmorTextShadow = newValue;
                HudConfig.configManager.save();
            });
    public static final OptionInstance<Integer> DECIMAL_PLACES = new OptionInstance<>(
            "rthuds.settings.decimal_places",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.config.option.decimal_places.tooltip")),
            (caption, value) -> {
                return (value == 6)
                        ? Component.translatable("rthuds.settings.decimal_places").append(Component.translatable("rthuds.settings.decimal_places.default"))
                        : Component.translatable("rthuds.settings.decimal_places").append(Component.literal(String.valueOf(value)));
            },
            new OptionInstance.IntRange(0, 6), HudConfig.configManager.getConfig().decimalPlaces,
            value -> {
                HudConfig.configManager.getConfig().decimalPlaces = value;
                HudConfig.configManager.save();
            }
    );
    public static final OptionInstance<Integer> INFO_BACKGROUND_STYLE = new OptionInstance<>(
            "rthuds.settings.background",
            (value) -> Tooltip.create(Component.translatable("rthuds.config.option.background_style.tooltip")),
            (caption, value) -> {
                ModConfig.BackgroundStyle style = ModConfig.BackgroundStyle.values()[value];
                return switch (style) {
                    case NONE ->
                            Component.translatable("rthuds.settings.background").append(Component.translatable("rthuds.settings.background.none"));
                    case LIGHT ->
                            Component.translatable("rthuds.settings.background").append(Component.translatable("rthuds.settings.background.light"));
                    case FULL ->
                            Component.translatable("rthuds.settings.background").append(Component.translatable("rthuds.settings.background.full"));
                };
            },
            new OptionInstance.IntRange(0, ModConfig.BackgroundStyle.values().length - 1),
            HudConfig.configManager.getConfig().backgroundStyle.ordinal(),
            value -> {
                HudConfig.configManager.getConfig().backgroundStyle = ModConfig.BackgroundStyle.values()[value];
                HudConfig.configManager.save();
            }
    );
    public static final OptionInstance<ModConfig.ArmorHUDLocation> ARMORHUD_LOCATION = new OptionInstance<>(
            "rthuds.settings.location.armor",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.settings.location.tooltip")),
            (caption, value) -> {
                return switch (value) {
                    case LEFTUP -> Component.translatable("rthuds.settings.location.leftup");
                    case LEFTCENTER -> Component.translatable("rthuds.settings.location.leftcenter");
                    case LEFTBOTTOM -> Component.translatable("rthuds.settings.location.leftdown");
                    case RIGHTUP -> Component.translatable("rthuds.settings.location.rightup");
                    case RIGHTCENTER -> Component.translatable("rthuds.settings.location.rightcenter");
                    case RIGHTBOTTOM -> Component.translatable("rthuds.settings.location.rightdown");
                };
            },
            new OptionInstance.Enum<>(
                    Arrays.asList(ModConfig.ArmorHUDLocation.values()),
                    Codec.INT.xmap(
                            i -> ModConfig.ArmorHUDLocation.values()[Math.min(i, ModConfig.ArmorHUDLocation.values().length - 1)],
                            c -> Arrays.asList(ModConfig.ArmorHUDLocation.values()).indexOf(c)
                    )
            ),
            HudConfig.configManager.getConfig().ArmorLocation,
            value -> {
                HudConfig.configManager.getConfig().ArmorLocation = value;
                HudConfig.configManager.save();
            }
    );
    public static final OptionInstance<Boolean> SHOW_DURABILITYBAR = OptionInstance.createBoolean(
            "rthuds.armorsettings.durabilitybar",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.armorsettings.durabilitybar.tooltip")),
            HudConfig.configManager.getConfig().ArmorDurabilityBar,
            (newValue) -> {
                HudConfig.configManager.getConfig().ArmorDurabilityBar = newValue;
                HudConfig.configManager.save();
            });
    public static final OptionInstance<Boolean> SHOW_EMPTYSLOTS = OptionInstance.createBoolean(
            "rthuds.armorsettings.showemptyslot",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.armorsettings.showemptyslot.tooltip")),
            HudConfig.configManager.getConfig().ShowEmpytSlot,
            (newValue) -> {
                HudConfig.configManager.getConfig().ShowEmpytSlot = newValue;
                HudConfig.configManager.save();
            });
    public static final OptionInstance<Boolean> SHOW_HANDITEMS = OptionInstance.createBoolean(
            "rthuds.armorsettings.showhanditem",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.armorsettings.showhanditem.tooltip")),
            HudConfig.configManager.getConfig().showHandItems,
            (newValue) -> {
                HudConfig.configManager.getConfig().showHandItems = newValue;
                HudConfig.configManager.save();
            });
    public static final OptionInstance<ModConfig.ArmorDurabilityEnum> ARMORHUD_HEALTHTEXT = new OptionInstance<>(
            "rthuds.armorsettings.menu.healthtext",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.armorsettings.armortext.tooltip")),
            (caption, value) -> {
                return switch (value) {
                    case NONE -> Component.translatable("rthuds.armorsettings.armortext.none");
                    case REMAINING -> Component.translatable("rthuds.armorsettings.armortext.remaining");
                    case DAMAGE -> Component.translatable("rthuds.armorsettings.armortext.damage");
                    case PERCENTAGE -> Component.translatable("rthuds.armorsettings.armortext.percentage");
                    case REMAINGINGandMAX -> Component.translatable("rthuds.armorsettings.armortext.remainingandmax");
                };
            },
            new OptionInstance.Enum<>(
                    Arrays.asList(ModConfig.ArmorDurabilityEnum.values()),
                    Codec.INT.xmap(
                            i -> ModConfig.ArmorDurabilityEnum.values()[Math.min(i, ModConfig.ArmorDurabilityEnum.values().length - 1)],
                            c -> Arrays.asList(ModConfig.ArmorDurabilityEnum.values()).indexOf(c)
                    )
            ),
            HudConfig.configManager.getConfig().ArmorHealthDurability,
            value -> {
                HudConfig.configManager.getConfig().ArmorHealthDurability = value;
                HudConfig.configManager.save();
            }
    );
    public static final OptionInstance<Double> ARMOR_SCALE = new OptionInstance<>(
            "rthuds.armorsettings.armorscale",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.armorsettings.armorscale.tooltip")),
            (component, value) -> {
                double actualValue = 0.5 + value;
                return component.copy().append(String.format(": %.2f", actualValue));
            },
            OptionInstance.UnitDouble.INSTANCE,
            (double) HudConfig.configManager.getConfig().ArmorScale - 0.5,
            value -> {
                HudConfig.configManager.getConfig().ArmorScale = (float) (0.5 + value);
                HudConfig.configManager.save();
            }
    );
    public static final OptionInstance<ModConfig.Layout> INFO_HUD_LAYOUT = new OptionInstance<>(
            "rthuds.config.option.layout",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.config.option.layout.tooltip")),
            (caption, value) -> {
                return switch (value) {
                    case LAYOUT_1 -> Component.translatable("rthuds.config.option.layout.LAYOUT_1");
                    case LAYOUT_2 -> Component.translatable("rthuds.config.option.layout.LAYOUT_2");
                };
            },
            new OptionInstance.Enum<>(
                    Arrays.asList(ModConfig.Layout.values()),
                    Codec.INT.xmap(
                            i -> ModConfig.Layout.values()[Math.min(i, ModConfig.Layout.values().length - 1)],
                            c -> Arrays.asList(ModConfig.Layout.values()).indexOf(c)
                    )
            ),
            HudConfig.configManager.getConfig().layout,
            value -> {
                HudConfig.configManager.getConfig().layout = value;
                HudConfig.configManager.save();
            }
    );
    public static final OptionInstance<Integer> INFO_HUD_X =
            new OptionInstance<>(
                    "rthuds.settings.hud_x",
                    OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.settings.hud_x.tooltip")),
                    (optionText, value) ->
                            Component.translatable("rthuds.settings.hud_x").append(Component.literal("%" + value)),
                    new OptionInstance.IntRange(0, 100),
                    HudConfig.configManager.getConfig().hudXPercent,
                    (newValue) -> {
                        HudConfig.configManager.getConfig().hudXPercent = newValue;
                        HudConfig.configManager.save();
                    }
            );

    public static final OptionInstance<Integer> INFO_HUD_Y =
            new OptionInstance<>(
                    "rthuds.settings.hud_y",
                    OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.settings.hud_y.tooltip")),
                    (optionText, value) ->
                            Component.translatable("rthuds.settings.hud_y").append(Component.literal("%" + value)),
                    new OptionInstance.IntRange(0, 100),
                    HudConfig.configManager.getConfig().hudYPercent,
                    (newValue) -> {
                        HudConfig.configManager.getConfig().hudYPercent = newValue;
                        HudConfig.configManager.save();
                    }
            );


    // Color Settings
    public static final OptionInstance<ModConfig.HudColor> COORDINATES_COLOR = new OptionInstance<>(
            "rthuds.settings.color.xyz",
            OptionInstance.noTooltip(),
            (caption, value) -> {
                return switch (value) {
                    case WHITE ->
                            Component.translatable("rthuds.config.option.color.WHITE").withStyle(ChatFormatting.WHITE);
                    case BLUE ->
                            Component.translatable("rthuds.config.option.color.BLUE").withStyle(ChatFormatting.BLUE);
                    case YELLOW ->
                            Component.translatable("rthuds.config.option.color.YELLOW").withStyle(ChatFormatting.YELLOW);
                    case CYAN ->
                            Component.translatable("rthuds.config.option.color.CYAN").withStyle(Style.EMPTY.withColor(0x55FFFF));
                    case GREEN ->
                            Component.translatable("rthuds.config.option.color.GREEN").withStyle(ChatFormatting.GREEN);
                    case RED -> Component.translatable("rthuds.config.option.color.RED").withStyle(ChatFormatting.RED);
                    case PINK ->
                            Component.translatable("rthuds.config.option.color.PINK").withStyle(ChatFormatting.LIGHT_PURPLE);
                    case ORANGE ->
                            Component.translatable("rthuds.config.option.color.ORANGE").withStyle(ChatFormatting.GOLD);
                    case PURPLE ->
                            Component.translatable("rthuds.config.option.color.PURPLE").withStyle(ChatFormatting.DARK_PURPLE);
                    case DARK_BLUE ->
                            Component.translatable("rthuds.config.option.color.DARK_BLUE").withStyle(ChatFormatting.DARK_BLUE);
                };
            },
            new OptionInstance.Enum<>(
                    Arrays.asList(ModConfig.HudColor.values()),
                    Codec.INT.xmap(
                            i -> ModConfig.HudColor.values()[Math.min(i, ModConfig.HudColor.values().length - 1)],
                            c -> Arrays.asList(ModConfig.HudColor.values()).indexOf(c)
                    )
            ),
            HudConfig.configManager.getConfig().xyzColor,
            value -> {
                HudConfig.configManager.getConfig().xyzColor = value;
                HudConfig.configManager.save();
            }
    );
    public static final OptionInstance<ModConfig.HudColor> YAW_COLOR = new OptionInstance<>(
            "rthuds.settings.color.yaw",
            OptionInstance.noTooltip(),
            (caption, value) -> {
                return switch (value) {
                    case WHITE ->
                            Component.translatable("rthuds.config.option.color.WHITE").withStyle(ChatFormatting.WHITE);
                    case BLUE ->
                            Component.translatable("rthuds.config.option.color.BLUE").withStyle(ChatFormatting.BLUE);
                    case YELLOW ->
                            Component.translatable("rthuds.config.option.color.YELLOW").withStyle(ChatFormatting.YELLOW);
                    case CYAN ->
                            Component.translatable("rthuds.config.option.color.CYAN").withStyle(Style.EMPTY.withColor(0x55FFFF));
                    case GREEN ->
                            Component.translatable("rthuds.config.option.color.GREEN").withStyle(ChatFormatting.GREEN);
                    case RED -> Component.translatable("rthuds.config.option.color.RED").withStyle(ChatFormatting.RED);
                    case PINK ->
                            Component.translatable("rthuds.config.option.color.PINK").withStyle(ChatFormatting.LIGHT_PURPLE);
                    case ORANGE ->
                            Component.translatable("rthuds.config.option.color.ORANGE").withStyle(ChatFormatting.GOLD);
                    case PURPLE ->
                            Component.translatable("rthuds.config.option.color.PURPLE").withStyle(ChatFormatting.DARK_PURPLE);
                    case DARK_BLUE ->
                            Component.translatable("rthuds.config.option.color.DARK_BLUE").withStyle(ChatFormatting.DARK_BLUE);
                };
            },
            new OptionInstance.Enum<>(
                    Arrays.asList(ModConfig.HudColor.values()),
                    Codec.INT.xmap(
                            i -> ModConfig.HudColor.values()[Math.max(0, Math.min(i, ModConfig.HudColor.values().length - 1))],
                            c -> Arrays.asList(ModConfig.HudColor.values()).indexOf(c)
                    )
            ),
            HudConfig.configManager.getConfig().yawColor,
            value -> {
                HudConfig.configManager.getConfig().yawColor = value;
                HudConfig.configManager.save();
            }
    );
    public static final OptionInstance<ModConfig.HudColor> PITCH_COLOR = new OptionInstance<>(
            "rthuds.settings.color.pitch",
            OptionInstance.noTooltip(),
            (caption, value) -> {
                return switch (value) {
                    case WHITE ->
                            Component.translatable("rthuds.config.option.color.WHITE").withStyle(ChatFormatting.WHITE);
                    case BLUE ->
                            Component.translatable("rthuds.config.option.color.BLUE").withStyle(ChatFormatting.BLUE);
                    case YELLOW ->
                            Component.translatable("rthuds.config.option.color.YELLOW").withStyle(ChatFormatting.YELLOW);
                    case CYAN ->
                            Component.translatable("rthuds.config.option.color.CYAN").withStyle(Style.EMPTY.withColor(0x55FFFF));
                    case GREEN ->
                            Component.translatable("rthuds.config.option.color.GREEN").withStyle(ChatFormatting.GREEN);
                    case RED -> Component.translatable("rthuds.config.option.color.RED").withStyle(ChatFormatting.RED);
                    case PINK ->
                            Component.translatable("rthuds.config.option.color.PINK").withStyle(ChatFormatting.LIGHT_PURPLE);
                    case ORANGE ->
                            Component.translatable("rthuds.config.option.color.ORANGE").withStyle(ChatFormatting.GOLD);
                    case PURPLE ->
                            Component.translatable("rthuds.config.option.color.PURPLE").withStyle(ChatFormatting.DARK_PURPLE);
                    case DARK_BLUE ->
                            Component.translatable("rthuds.config.option.color.DARK_BLUE").withStyle(ChatFormatting.DARK_BLUE);
                };
            },
            new OptionInstance.Enum<>(
                    Arrays.asList(ModConfig.HudColor.values()),
                    Codec.INT.xmap(
                            i -> ModConfig.HudColor.values()[Math.max(0, Math.min(i, ModConfig.HudColor.values().length - 1))],
                            c -> Arrays.asList(ModConfig.HudColor.values()).indexOf(c)
                    )
            ),
            HudConfig.configManager.getConfig().pitchColor,
            value -> {
                HudConfig.configManager.getConfig().pitchColor = value;
                HudConfig.configManager.save();
            }
    );
    public static final OptionInstance<ModConfig.HudColor> DIRECTION_COLOR = new OptionInstance<>(
            "rthuds.settings.color.direction",
            OptionInstance.noTooltip(),
            (caption, value) -> {
                return switch (value) {
                    case WHITE ->
                            Component.translatable("rthuds.config.option.color.WHITE").withStyle(ChatFormatting.WHITE);
                    case BLUE ->
                            Component.translatable("rthuds.config.option.color.BLUE").withStyle(ChatFormatting.BLUE);
                    case YELLOW ->
                            Component.translatable("rthuds.config.option.color.YELLOW").withStyle(ChatFormatting.YELLOW);
                    case CYAN ->
                            Component.translatable("rthuds.config.option.color.CYAN").withStyle(Style.EMPTY.withColor(0x55FFFF));
                    case GREEN ->
                            Component.translatable("rthuds.config.option.color.GREEN").withStyle(ChatFormatting.GREEN);
                    case RED -> Component.translatable("rthuds.config.option.color.RED").withStyle(ChatFormatting.RED);
                    case PINK ->
                            Component.translatable("rthuds.config.option.color.PINK").withStyle(ChatFormatting.LIGHT_PURPLE);
                    case ORANGE ->
                            Component.translatable("rthuds.config.option.color.ORANGE").withStyle(ChatFormatting.GOLD);
                    case PURPLE ->
                            Component.translatable("rthuds.config.option.color.PURPLE").withStyle(ChatFormatting.DARK_PURPLE);
                    case DARK_BLUE ->
                            Component.translatable("rthuds.config.option.color.DARK_BLUE").withStyle(ChatFormatting.DARK_BLUE);
                };
            },
            new OptionInstance.Enum<>(
                    Arrays.asList(ModConfig.HudColor.values()),
                    Codec.INT.xmap(
                            i -> ModConfig.HudColor.values()[Math.max(0, Math.min(i, ModConfig.HudColor.values().length - 1))],
                            c -> Arrays.asList(ModConfig.HudColor.values()).indexOf(c)
                    )
            ),
            HudConfig.configManager.getConfig().directionColor,
            value -> {
                HudConfig.configManager.getConfig().directionColor = value;
                HudConfig.configManager.save();
            }
    );
    public static final OptionInstance<ModConfig.HudColor> FPS_COLOR = new OptionInstance<>(
            "rthuds.settings.color.fps",
            OptionInstance.noTooltip(),
            (caption, value) -> {
                return switch (value) {
                    case WHITE ->
                            Component.translatable("rthuds.config.option.color.WHITE").withStyle(ChatFormatting.WHITE);
                    case BLUE ->
                            Component.translatable("rthuds.config.option.color.BLUE").withStyle(ChatFormatting.BLUE);
                    case YELLOW ->
                            Component.translatable("rthuds.config.option.color.YELLOW").withStyle(ChatFormatting.YELLOW);
                    case CYAN ->
                            Component.translatable("rthuds.config.option.color.CYAN").withStyle(Style.EMPTY.withColor(0x55FFFF));
                    case GREEN ->
                            Component.translatable("rthuds.config.option.color.GREEN").withStyle(ChatFormatting.GREEN);
                    case RED -> Component.translatable("rthuds.config.option.color.RED").withStyle(ChatFormatting.RED);
                    case PINK ->
                            Component.translatable("rthuds.config.option.color.PINK").withStyle(ChatFormatting.LIGHT_PURPLE);
                    case ORANGE ->
                            Component.translatable("rthuds.config.option.color.ORANGE").withStyle(ChatFormatting.GOLD);
                    case PURPLE ->
                            Component.translatable("rthuds.config.option.color.PURPLE").withStyle(ChatFormatting.DARK_PURPLE);
                    case DARK_BLUE ->
                            Component.translatable("rthuds.config.option.color.DARK_BLUE").withStyle(ChatFormatting.DARK_BLUE);
                };
            },
            new OptionInstance.Enum<>(
                    Arrays.asList(ModConfig.HudColor.values()),
                    Codec.INT.xmap(
                            i -> ModConfig.HudColor.values()[Math.max(0, Math.min(i, ModConfig.HudColor.values().length - 1))],
                            c -> Arrays.asList(ModConfig.HudColor.values()).indexOf(c)
                    )
            ),
            HudConfig.configManager.getConfig().fpsColor,
            value -> {
                HudConfig.configManager.getConfig().fpsColor = value;
                HudConfig.configManager.save();
            }
    );
    public static final OptionInstance<ModConfig.HudColor> NETHERCOORDS_COLOR = new OptionInstance<>(
            "rthuds.settings.color.nethercoords",
            OptionInstance.noTooltip(),
            (caption, value) -> {
                return switch (value) {
                    case WHITE ->
                            Component.translatable("rthuds.config.option.color.WHITE").withStyle(ChatFormatting.WHITE);
                    case BLUE ->
                            Component.translatable("rthuds.config.option.color.BLUE").withStyle(ChatFormatting.BLUE);
                    case YELLOW ->
                            Component.translatable("rthuds.config.option.color.YELLOW").withStyle(ChatFormatting.YELLOW);
                    case CYAN ->
                            Component.translatable("rthuds.config.option.color.CYAN").withStyle(Style.EMPTY.withColor(0x55FFFF));
                    case GREEN ->
                            Component.translatable("rthuds.config.option.color.GREEN").withStyle(ChatFormatting.GREEN);
                    case RED -> Component.translatable("rthuds.config.option.color.RED").withStyle(ChatFormatting.RED);
                    case PINK ->
                            Component.translatable("rthuds.config.option.color.PINK").withStyle(ChatFormatting.LIGHT_PURPLE);
                    case ORANGE ->
                            Component.translatable("rthuds.config.option.color.ORANGE").withStyle(ChatFormatting.GOLD);
                    case PURPLE ->
                            Component.translatable("rthuds.config.option.color.PURPLE").withStyle(ChatFormatting.DARK_PURPLE);
                    case DARK_BLUE ->
                            Component.translatable("rthuds.config.option.color.DARK_BLUE").withStyle(ChatFormatting.DARK_BLUE);
                };
            },
            new OptionInstance.Enum<>(
                    Arrays.asList(ModConfig.HudColor.values()),
                    Codec.INT.xmap(
                            i -> ModConfig.HudColor.values()[Math.max(0, Math.min(i, ModConfig.HudColor.values().length - 1))],
                            c -> Arrays.asList(ModConfig.HudColor.values()).indexOf(c)
                    )
            ),
            HudConfig.configManager.getConfig().netherCoordColor,
            value -> {
                HudConfig.configManager.getConfig().netherCoordColor = value;
                HudConfig.configManager.save();
            }
    );
    public static final OptionInstance<ModConfig.HudColor> ARMOR_HEALTH_COLOR = new OptionInstance<>(
            "rthuds.armorsettings.armortext.color",
            OptionInstance.noTooltip(),
            (caption, value) -> {
                return switch (value) {
                    case WHITE ->
                            Component.translatable("rthuds.config.option.color.WHITE").withStyle(ChatFormatting.WHITE);
                    case BLUE ->
                            Component.translatable("rthuds.config.option.color.BLUE").withStyle(ChatFormatting.BLUE);
                    case YELLOW ->
                            Component.translatable("rthuds.config.option.color.YELLOW").withStyle(ChatFormatting.YELLOW);
                    case CYAN ->
                            Component.translatable("rthuds.config.option.color.CYAN").withStyle(Style.EMPTY.withColor(0x55FFFF));
                    case GREEN ->
                            Component.translatable("rthuds.config.option.color.GREEN").withStyle(ChatFormatting.GREEN);
                    case RED -> Component.translatable("rthuds.config.option.color.RED").withStyle(ChatFormatting.RED);
                    case PINK ->
                            Component.translatable("rthuds.config.option.color.PINK").withStyle(ChatFormatting.LIGHT_PURPLE);
                    case ORANGE ->
                            Component.translatable("rthuds.config.option.color.ORANGE").withStyle(ChatFormatting.GOLD);
                    case PURPLE ->
                            Component.translatable("rthuds.config.option.color.PURPLE").withStyle(ChatFormatting.DARK_PURPLE);
                    case DARK_BLUE ->
                            Component.translatable("rthuds.config.option.color.DARK_BLUE").withStyle(ChatFormatting.DARK_BLUE);
                };
            },
            new OptionInstance.Enum<>(
                    Arrays.asList(ModConfig.HudColor.values()),
                    Codec.INT.xmap(
                            i -> ModConfig.HudColor.values()[Math.max(0, Math.min(i, ModConfig.HudColor.values().length - 1))],
                            c -> Arrays.asList(ModConfig.HudColor.values()).indexOf(c)
                    )
            ),
            HudConfig.configManager.getConfig().ArmorTextColor,
            value -> {
                HudConfig.configManager.getConfig().ArmorTextColor = value;
                HudConfig.configManager.save();
            }
    );

    // Impression Settings
    public static final OptionInstance<Boolean> SHOW_COORDINATES = OptionInstance.createBoolean(
            "rthuds.config.option.showcoord",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.config.option.showcoord.tooltip")),
            HudConfig.configManager.getConfig().showCoords,
            (newValue) -> {
                HudConfig.configManager.getConfig().showCoords = newValue;
                HudConfig.configManager.save();
            });
    public static final OptionInstance<Boolean> SHOW_YAWPITCH = OptionInstance.createBoolean(
            "rthuds.config.option.showYawPitch",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.config.option.showYawPitch.tooltip")),
            HudConfig.configManager.getConfig().showYawPitch,
            (newValue) -> {
                HudConfig.configManager.getConfig().showYawPitch = newValue;
                HudConfig.configManager.save();
            });
    public static final OptionInstance<Boolean> SHOW_DIRECTION = OptionInstance.createBoolean(
            "rthuds.config.option.showDirection",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.config.option.showDirection.tooltip")),
            HudConfig.configManager.getConfig().showDirection,
            (newValue) -> {
                HudConfig.configManager.getConfig().showDirection = newValue;
                HudConfig.configManager.save();
            });
    public static final OptionInstance<Boolean> SHOW_FPS = OptionInstance.createBoolean(
            "rthuds.config.option.showFPS",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.config.option.showFPS.tooltip")),
            HudConfig.configManager.getConfig().showFPS,
            (newValue) -> {
                HudConfig.configManager.getConfig().showFPS = newValue;
                HudConfig.configManager.save();
            });
    public static final OptionInstance<Boolean> SHOW_NETHER_COORDINATES = OptionInstance.createBoolean(
            "rthuds.settings.nethercoord",
            OptionInstance.cachedConstantTooltip(Component.translatable("rthuds.settings.nethercoord.tooltip")),
            HudConfig.configManager.getConfig().toggleNetherCoordinateConversion,
            (newValue) -> {
                HudConfig.configManager.getConfig().toggleNetherCoordinateConversion = newValue;
                HudConfig.configManager.save();
            });
}
