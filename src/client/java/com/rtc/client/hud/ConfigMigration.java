package com.rtc.client.hud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rtc.client.utilities.HudConfig;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;

@SuppressWarnings("CatchMayIgnoreException")
public class ConfigMigration {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void migrateOnce() {
        File oldConfig = new File("config", "rthuds.json");
        if (!oldConfig.exists()) return;

        try (FileReader reader = new FileReader(oldConfig)) {
            ModConfig old = GSON.fromJson(reader, ModConfig.class);

            ModConfig current = HudConfig.configManager.getConfig();

            Field[] fields = ModConfig.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(old);
                field.set(current, value);
            }

        } catch (Exception e) {}

        HudConfig.configManager.save();
    }
}