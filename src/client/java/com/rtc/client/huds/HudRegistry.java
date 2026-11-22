package com.rtc.client.huds;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.rtc.client.RTHudsClient.MOD_ID;

@SuppressWarnings({"CallToPrintStackTrace", "ExtractMethodRecommender"})
public final class HudRegistry {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json");

    public static final List<HudElement> ELEMENTS = new ArrayList<>();

    private HudRegistry() {
    }

    public static void add(HudElement e) {
        ELEMENTS.add(e);
    }

    public static void save() {
        try {
            JsonObject root = new JsonObject();
            root.addProperty("_accent", UiStyle.ACCENT);
            root.addProperty("_accentChroma", UiStyle.ACCENT_CHROMA);
            root.addProperty("_theme", UiStyle.theme.name());
            root.addProperty("_infoHud", HudConfig.infoHud);
            root.addProperty("_hideGui", HudConfig.showHideGui);
            root.addProperty("_debugGui", HudConfig.showDebugGui);
            for (HudElement e : ELEMENTS) {
                JsonObject o = new JsonObject();
                o.addProperty("x", e.x);
                o.addProperty("y", e.y);
                o.addProperty("enabled", e.enabled);
                o.addProperty("textShadow", e.textShadow);
                o.addProperty("showBackground", e.showBackground);
                o.addProperty("bgColor", e.bgColor);
                o.addProperty("textColor", e.textColor);
                o.addProperty("valueColor", e.valueColor);
                o.addProperty("textChroma", e.textChroma);
                o.addProperty("valueChroma", e.valueChroma);
                o.addProperty("scale", e.scale);
                if (e.coordFormatter != null) {
                    o.addProperty("decimalPlaces", e.coordFormatter.getDecimalPlaces());
                }
                root.add(e.id, o);
            }
            JsonObject armor = new JsonObject();
            armor.addProperty("enabled", ArmorHudConfig.enabled);
            armor.addProperty("showHelmet", ArmorHudConfig.showHelmet);
            armor.addProperty("showChest", ArmorHudConfig.showChest);
            armor.addProperty("showLegs", ArmorHudConfig.showLegs);
            armor.addProperty("showBoots", ArmorHudConfig.showBoots);
            armor.addProperty("showMainHand", ArmorHudConfig.showMainHand);
            armor.addProperty("showOffHand", ArmorHudConfig.showOffHand);
            armor.addProperty("showCount", ArmorHudConfig.showCount);
            armor.addProperty("showDurabilityBar", ArmorHudConfig.showDurabilityBar);
            armor.addProperty("whiteEmptyIcons", ArmorHudConfig.whiteEmptyIcons);
            armor.addProperty("showEmptySlots", ArmorHudConfig.showEmptySlots);
            armor.addProperty("showPanel", ArmorHudConfig.showPanel);
            armor.addProperty("panelColor", ArmorHudConfig.panelColor);
            armor.addProperty("healthMode", ArmorHudConfig.healthMode.name());
            armor.addProperty("healthPos", ArmorHudConfig.healthPos.name());
            armor.addProperty("orientation", ArmorHudConfig.orientation.name());
            armor.addProperty("bgStyle", ArmorHudConfig.bgStyle.name());
            armor.addProperty("x", ArmorHudConfig.x);
            armor.addProperty("y", ArmorHudConfig.y);
            armor.addProperty("textShadow", ArmorHudConfig.textShadow);
            armor.addProperty("textColor", ArmorHudConfig.textColor);
            armor.addProperty("textChroma", ArmorHudConfig.textChroma);
            armor.addProperty("dynamicColor", ArmorHudConfig.dynamicColor);
            armor.addProperty("warnPercent", ArmorHudConfig.warnPercent);
            armor.addProperty("warnSound", ArmorHudConfig.warnSound.name());
            armor.addProperty("hotbarSnapped", ArmorHudConfig.hotbarSnapped);
            armor.addProperty("scale", ArmorHudConfig.scale);
            root.add("_armor", armor);
            root.add("_effects", saveEffects());
            Files.writeString(CONFIG, GSON.toJson(root));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static JsonObject saveEffects() {
        JsonObject o = new JsonObject();
        o.addProperty("enabled", EffectsHudConfig.enabled);
        o.addProperty("hideVanilla", EffectsHudConfig.hideVanilla);
        o.addProperty("orientation", EffectsHudConfig.orientation.name());
        o.addProperty("sortMode", EffectsHudConfig.sortMode.name());
        o.addProperty("showPanel", EffectsHudConfig.showPanel);
        o.addProperty("panelColor", EffectsHudConfig.panelColor);
        o.addProperty("showIcon", EffectsHudConfig.showIcon);
        o.addProperty("showName", EffectsHudConfig.showName);
        o.addProperty("showDuration", EffectsHudConfig.showDuration);
        o.addProperty("showBar", EffectsHudConfig.showBar);
        o.addProperty("textColor", EffectsHudConfig.textColor);
        o.addProperty("textChroma", EffectsHudConfig.textChroma);
        o.addProperty("textShadow", EffectsHudConfig.textShadow);
        o.addProperty("dynamicColor", EffectsHudConfig.dynamicColor);
        o.addProperty("hideAmbient", EffectsHudConfig.hideAmbient);
        o.addProperty("showBeneficial", EffectsHudConfig.showBeneficial);
        o.addProperty("showHarmful", EffectsHudConfig.showHarmful);
        o.addProperty("scale", EffectsHudConfig.scale);
        o.addProperty("x", EffectsHudConfig.x);
        o.addProperty("y", EffectsHudConfig.y);
        return o;
    }

    private static void loadEffects(JsonObject o) {
        if (o.has("enabled")) EffectsHudConfig.enabled = o.get("enabled").getAsBoolean();
        if (o.has("hideVanilla")) EffectsHudConfig.hideVanilla = o.get("hideVanilla").getAsBoolean();
        try {
            if (o.has("orientation"))
                EffectsHudConfig.orientation = EffectsHudConfig.Orientation.valueOf(o.get("orientation").getAsString());
            if (o.has("sortMode"))
                EffectsHudConfig.sortMode = EffectsHudConfig.SortMode.valueOf(o.get("sortMode").getAsString());
        } catch (IllegalArgumentException ignored) {
        }
        if (o.has("showPanel")) EffectsHudConfig.showPanel = o.get("showPanel").getAsBoolean();
        if (o.has("panelColor")) EffectsHudConfig.panelColor = o.get("panelColor").getAsInt();
        if (o.has("showIcon")) EffectsHudConfig.showIcon = o.get("showIcon").getAsBoolean();
        if (o.has("showName")) EffectsHudConfig.showName = o.get("showName").getAsBoolean();
        if (o.has("showDuration")) EffectsHudConfig.showDuration = o.get("showDuration").getAsBoolean();
        if (o.has("showBar")) EffectsHudConfig.showBar = o.get("showBar").getAsBoolean();
        if (o.has("textColor")) EffectsHudConfig.textColor = o.get("textColor").getAsInt();
        if (o.has("textChroma")) EffectsHudConfig.textChroma = o.get("textChroma").getAsBoolean();
        if (o.has("textShadow")) EffectsHudConfig.textShadow = o.get("textShadow").getAsBoolean();
        if (o.has("dynamicColor")) EffectsHudConfig.dynamicColor = o.get("dynamicColor").getAsBoolean();
        if (o.has("hideAmbient")) EffectsHudConfig.hideAmbient = o.get("hideAmbient").getAsBoolean();
        if (o.has("showBeneficial")) EffectsHudConfig.showBeneficial = o.get("showBeneficial").getAsBoolean();
        if (o.has("showHarmful")) EffectsHudConfig.showHarmful = o.get("showHarmful").getAsBoolean();
        if (o.has("scale")) EffectsHudConfig.setScale(o.get("scale").getAsFloat());
        if (o.has("x")) EffectsHudConfig.x = o.get("x").getAsInt();
        if (o.has("y")) EffectsHudConfig.y = o.get("y").getAsInt();
    }

    public static void load() {
        try {
            if (!Files.exists(CONFIG)) return;
            JsonObject root = JsonParser.parseString(Files.readString(CONFIG)).getAsJsonObject();
            if (root.has("_accent")) UiStyle.ACCENT = root.get("_accent").getAsInt();
            if (root.has("_accentChroma")) UiStyle.ACCENT_CHROMA = root.get("_accentChroma").getAsBoolean();
            if (root.has("_theme")) {
                try {
                    UiStyle.setTheme(UiStyle.Theme.valueOf(root.get("_theme").getAsString()));
                } catch (IllegalArgumentException ignored) {
                }
            }
            if (root.has("_infoHud")) HudConfig.infoHud = root.get("_infoHud").getAsBoolean();
            if (root.has("_hideGui")) HudConfig.showHideGui = root.get("_hideGui").getAsBoolean();
            if (root.has("_debugGui")) HudConfig.showDebugGui = root.get("_debugGui").getAsBoolean();
            for (HudElement e : ELEMENTS) {
                if (!root.has(e.id)) continue;
                JsonObject o = root.getAsJsonObject(e.id);
                e.x = o.get("x").getAsInt();
                e.y = o.get("y").getAsInt();
                e.enabled = o.get("enabled").getAsBoolean();
                if (o.has("textShadow")) e.textShadow = o.get("textShadow").getAsBoolean();
                if (o.has("showBackground")) e.showBackground = o.get("showBackground").getAsBoolean();
                if (o.has("bgColor")) e.bgColor = o.get("bgColor").getAsInt();
                if (o.has("textColor")) e.textColor = o.get("textColor").getAsInt();
                if (o.has("valueColor")) e.valueColor = o.get("valueColor").getAsInt();
                if (o.has("textChroma")) e.textChroma = o.get("textChroma").getAsBoolean();
                if (o.has("valueChroma")) e.valueChroma = o.get("valueChroma").getAsBoolean();
                if (o.has("decimalPlaces") && e.coordFormatter != null) {
                    e.coordFormatter.setDecimalPlaces(o.get("decimalPlaces").getAsInt());
                }
                if (o.has("scale")) e.scale = HudElement.clampScale(o.get("scale").getAsFloat());
            }
            if (root.has("_effects")) loadEffects(root.getAsJsonObject("_effects"));
            if (root.has("_armor")) {
                JsonObject armor = root.getAsJsonObject("_armor");
                if (armor.has("enabled")) ArmorHudConfig.enabled = armor.get("enabled").getAsBoolean();
                if (armor.has("showHelmet")) ArmorHudConfig.showHelmet = armor.get("showHelmet").getAsBoolean();
                if (armor.has("showChest")) ArmorHudConfig.showChest = armor.get("showChest").getAsBoolean();
                if (armor.has("showLegs")) ArmorHudConfig.showLegs = armor.get("showLegs").getAsBoolean();
                if (armor.has("showBoots")) ArmorHudConfig.showBoots = armor.get("showBoots").getAsBoolean();
                if (armor.has("showMainHand")) ArmorHudConfig.showMainHand = armor.get("showMainHand").getAsBoolean();
                if (armor.has("showOffHand")) ArmorHudConfig.showOffHand = armor.get("showOffHand").getAsBoolean();
                if (armor.has("showCount")) ArmorHudConfig.showCount = armor.get("showCount").getAsBoolean();
                if (armor.has("showDurabilityBar"))
                    ArmorHudConfig.showDurabilityBar = armor.get("showDurabilityBar").getAsBoolean();
                if (armor.has("whiteEmptyIcons"))
                    ArmorHudConfig.whiteEmptyIcons = armor.get("whiteEmptyIcons").getAsBoolean();
                if (armor.has("showEmptySlots"))
                    ArmorHudConfig.showEmptySlots = armor.get("showEmptySlots").getAsBoolean();
                if (armor.has("showPanel")) ArmorHudConfig.showPanel = armor.get("showPanel").getAsBoolean();
                if (armor.has("panelColor")) ArmorHudConfig.panelColor = armor.get("panelColor").getAsInt();
                if (armor.has("healthMode"))
                    ArmorHudConfig.healthMode = ArmorHudConfig.HealthMode.valueOf(armor.get("healthMode").getAsString());
                if (armor.has("healthPos"))
                    ArmorHudConfig.healthPos = ArmorHudConfig.HealthPos.valueOf(armor.get("healthPos").getAsString());
                if (armor.has("orientation"))
                    ArmorHudConfig.orientation = ArmorHudConfig.Orientation.valueOf(armor.get("orientation").getAsString());
                if (armor.has("bgStyle"))
                    ArmorHudConfig.bgStyle = ArmorHudConfig.BgStyle.valueOf(armor.get("bgStyle").getAsString());
                if (armor.has("x")) ArmorHudConfig.x = armor.get("x").getAsInt();
                if (armor.has("y")) ArmorHudConfig.y = armor.get("y").getAsInt();
                if (armor.has("textShadow")) ArmorHudConfig.textShadow = armor.get("textShadow").getAsBoolean();
                if (armor.has("textColor")) ArmorHudConfig.textColor = armor.get("textColor").getAsInt();
                if (armor.has("textChroma")) ArmorHudConfig.textChroma = armor.get("textChroma").getAsBoolean();
                if (armor.has("dynamicColor")) ArmorHudConfig.dynamicColor = armor.get("dynamicColor").getAsBoolean();
                if (armor.has("warnPercent"))
                    ArmorHudConfig.warnPercent = Math.max(1, Math.min(99, armor.get("warnPercent").getAsInt()));
                if (armor.has("warnSound")) {
                    try {
                        ArmorHudConfig.warnSound = ArmorHudConfig.WarnSound.valueOf(armor.get("warnSound").getAsString());
                    } catch (IllegalArgumentException ignored) {
                    }
                }
                if (armor.has("hotbarSnapped"))
                    ArmorHudConfig.hotbarSnapped = armor.get("hotbarSnapped").getAsBoolean();
                if (armor.has("scale")) ArmorHudConfig.setScale(armor.get("scale").getAsFloat());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}