package com.rtc.client.gui;

import com.rtc.client.RTHudsClient;
import com.rtc.client.util.Log;
import dev.isxander.yacl3.api.NameableEnum;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.io.*;

@SuppressWarnings("SpellCheckingInspection")
public class ModConfig {

    public static ConfigClassHandler<ModConfig> INSTANCE = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(Identifier.fromNamespaceAndPath(RTHudsClient.MOD_ID, "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("RTHuds").resolve("rthuds.json")).build()).build();

    // General Settings
    @SerialEntry public boolean infoHud = true;
    @SerialEntry public boolean armorHud = true;
    @SerialEntry public boolean showHideGui = true;
    @SerialEntry public boolean showDebugGui = true;
    public static boolean InfoHud() {return ModConfig.INSTANCE.instance().infoHud;}
    public static boolean ArmorHud() {return ModConfig.INSTANCE.instance().armorHud;}
    public static boolean HideGui() {return ModConfig.INSTANCE.instance().showHideGui;}
    public static boolean DebugGui() {return ModConfig.INSTANCE.instance().showDebugGui;}

    // Appearance Settings
    @SerialEntry public boolean infoHudTextShadow = true;
    @SerialEntry public boolean armorTextShadow = true;
    @SerialEntry public InfoHudDirection infoHudDirection = InfoHudDirection.HORIZONTAL;
    @SerialEntry public HudBackground infoHudBackground = HudBackground.LIGHT;
    @SerialEntry public HudBackground armorHudBackground = HudBackground.LIGHT;
    @SerialEntry public int infoHudPosX = 0;
    @SerialEntry public int infoHudPosY = 0;
    @SerialEntry public boolean armorHudBar = true;
    @SerialEntry public boolean armorHudEmpytSlots = true;
    @SerialEntry public boolean armorHudHanditems = true;
    @SerialEntry public int infoHudDecimalPlaces = 6;
    @SerialEntry public float armorHudScale = 1.0f;
    @SerialEntry public ArmorHudHealth armorHudHealth = ArmorHudHealth.REMAINING;
    @SerialEntry public ArmorHudLocation armorHudLocation = ArmorHudLocation.LEFTCENTER;
    public enum InfoHudDirection implements NameableEnum {
        HORIZONTAL("hud.direction.horizontal"),
        VERTICAL("hud.direction.vertical");

        private final String key;

        InfoHudDirection(String key) {
            this.key = key;
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable(key);
        }
    }
    public enum HudBackground implements NameableEnum {
        NONE("hud.background.none"),
        LIGHT("hud.background.light"),
        FULL("hud.background.full");

        private final String key;

        HudBackground(String key) {
            this.key = key;
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable(key);
        }
    }
    public enum ArmorHudHealth implements NameableEnum {
        REMAINING("hud.healthtext.remaining"),
        DAMAGE("hud.healthtext.damage"),
        NONE("hud.healthtext.none"),
        PERCENTAGE("hud.healthtext.percentage"),
        REMAININGMAX("hud.healthtext.remainingandmax");

        private final String key;

        ArmorHudHealth(String key) {
            this.key = key;
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable(key);
        }
    }
    public enum ArmorHudLocation implements NameableEnum {
        LEFTUP("rthuds.settings.location.leftup"),
        LEFTCENTER("rthuds.settings.location.leftcenter"),
        LEFTBOTTOM("rthuds.settings.location.leftdown"),
        RIGHTUP("rthuds.settings.location.rightup"),
        RIGHTCENTER("rthuds.settings.location.rightcenter"),
        RIGHTBOTTOM("rthuds.settings.location.rightdown");

        private final String key;

        ArmorHudLocation(String key) {
            this.key = key;
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable(key);
        }
    }
    public static boolean InfoTextShadow() {return ModConfig.INSTANCE.instance().infoHudTextShadow;}
    public static boolean ArmorTextShadow() {return ModConfig.INSTANCE.instance().armorTextShadow;}
    public static InfoHudDirection infoHudDirection() {return ModConfig.INSTANCE.instance().infoHudDirection;}
    public static HudBackground infoHudBackground() {return ModConfig.INSTANCE.instance().infoHudBackground;}
    public static HudBackground armorHudBackground() {return ModConfig.INSTANCE.instance().armorHudBackground;}
    public static int infoHudPosX() {return ModConfig.INSTANCE.instance().infoHudPosX;}
    public static int infoHudPosY() {return ModConfig.INSTANCE.instance().infoHudPosY;}
    public static boolean armorHudBar() {return ModConfig.INSTANCE.instance().armorHudBar;}
    public static boolean armorHudEmpytSlots() {return ModConfig.INSTANCE.instance().armorHudEmpytSlots;}
    public static boolean armorHudHandItems() {return ModConfig.INSTANCE.instance().armorHudHanditems;}
    public static int infoHudDecimalPlaces() {return ModConfig.INSTANCE.instance().infoHudDecimalPlaces;}
    public static float armorHudScale() {return ModConfig.INSTANCE.instance().armorHudScale;}
    public static ArmorHudHealth armorHudHealth() {return ModConfig.INSTANCE.instance().armorHudHealth;}
    public static ArmorHudLocation armorHudLocation() {return ModConfig.INSTANCE.instance().armorHudLocation;}

    // Color Settings
    public static final int defaultcoordinatesColor = -1;
    @SerialEntry public int coordinatesColor = defaultcoordinatesColor;

    public static final int defaultyawColor = -1;
    @SerialEntry public int yawColor = defaultyawColor;

    public static final int defaultpitchColor = -1;
    @SerialEntry public int pitchColor = defaultpitchColor;

    public static final int defaultdirectionColor = -1;
    @SerialEntry public int directionColor = defaultdirectionColor;

    public static final int defaultfpsColor = -1;
    @SerialEntry public int fpsColor = defaultfpsColor;

    public static final int defaultcoordinateConverterColor = -1;
    @SerialEntry public int coordinateConverterColor = defaultcoordinateConverterColor;

    public static final int defaultarmorHudHealthColor = -1;
    @SerialEntry public int armorHudHealthColor = defaultarmorHudHealthColor;

    // Impression Settings
    @SerialEntry public boolean showCoordinates = true;
    @SerialEntry public boolean showYaw = true;
    @SerialEntry public boolean showPitch = true;
    @SerialEntry public boolean showDirection = true;
    @SerialEntry public boolean showFPS = true;
    @SerialEntry public boolean showCoordinatesConverter = true;

    public static boolean showCoordinates() {return ModConfig.INSTANCE.instance().showCoordinates;}
    public static boolean showYaw() {return ModConfig.INSTANCE.instance().showYaw;}
    public static boolean showPitch() {return ModConfig.INSTANCE.instance().showPitch;}
    public static boolean showDirection() {return ModConfig.INSTANCE.instance().showDirection;}
    public static boolean showFPS() {return ModConfig.INSTANCE.instance().showFPS;}
    public static boolean showCoordinatesConverter() {return ModConfig.INSTANCE.instance().showCoordinatesConverter;}


    public static void save() {
        try {
            INSTANCE.save();
        } catch (Exception e) {
            Log.error("[RTHUDS] An error occurred while saving the configuration!", e);
        }
    }

    public static ModConfig get() {
        return INSTANCE.instance();
    }

    public enum keyHudType {INFO, ARMOR}

    public static boolean getHud(keyHudType type) {
        return switch (type) {
            case INFO -> INSTANCE.instance().infoHud;
            case ARMOR -> INSTANCE.instance().armorHud;
        };
    }

    public static void setHud(keyHudType type, boolean value) {
        switch (type) {
            case INFO -> INSTANCE.instance().infoHud = value;
            case ARMOR -> INSTANCE.instance().armorHud = value;
        }
    }

    public static boolean toggleHud(keyHudType type) {
        boolean newState = !getHud(type);
        setHud(type, newState);
        save();
        return newState;
    }
}
