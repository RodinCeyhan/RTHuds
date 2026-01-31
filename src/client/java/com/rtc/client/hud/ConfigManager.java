package com.rtc.client.hud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings({"FieldMayBeFinal", "CatchMayIgnoreException"})
public class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final String CONFIG_FILE_NAME = "rthuds.json";

    private ModConfig config;

    public ConfigManager() {
        this.config = load();
    }

    public ModConfig getConfig() {
        return config;
    }

    public void save() {
        File configFile = new File("config", CONFIG_FILE_NAME);
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {}
    }

    private ModConfig load() {
        File configFile = new File("config", CONFIG_FILE_NAME);
        if (!configFile.exists()) {
            return new ModConfig();
        }
        try (FileReader reader = new FileReader(configFile)) {
            return GSON.fromJson(reader, ModConfig.class);
        } catch (IOException e) {
            return new ModConfig();
        }
    }
}
