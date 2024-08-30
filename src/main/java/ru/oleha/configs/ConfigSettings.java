package ru.oleha.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.oleha.utils.Storage;
import ru.oleha.utils.Settings;

import java.io.*;
import java.lang.reflect.Type;

public class ConfigSettings {
    private static final String jsonName = "settings.json";
    private static File file;
    public static void init() {
        file = new File("./"+jsonName);
        try {
            if (file.createNewFile()) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.setPrettyPrinting().create();
                Settings settings = new Settings();
                gson.toJson(settings);
                setKeyIDStart(3657);
                setKeyIDExit(3665);
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<Settings>(){}.getType();
                    Settings settings = gson.fromJson(reader,type);
                    if (settings == null) {
                        System.err.println("Файл конфигурации '" + jsonName + "' пуст или содержит некорректные данные.");
                    } else {
                        Storage.getSettings().setKeyIDStart(settings.getKeyIDStart());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("ConfigSettings");
            System.out.println(e.fillInStackTrace());
            System.err.println("Ошибка при создании файла");
        }
    }
    public static void setLogs(boolean b) {
        Settings settings = read();
        settings.setLogs(b);
        write(settings);
    }
    public static void setLoop(boolean b) {
        Settings settings = read();
        settings.setLoop(b);
        write(settings);
    }
    public static void setKeyIDStart(int keyID) {
        Settings settings = read();
        settings.setKeyIDStart(keyID);
        write(settings);
    }
    public static void setKeyIDExit(int keyID) {
        Settings settings = read();
        settings.setKeyIDExit(keyID);
        write(settings);
    }
    public static int getKeyIDStart() {
        return read().getKeyIDStart();
    }
    public static int getKeyIDExit() {
        return read().getKeyIDExit();
    }
    public static boolean isLoop() {
        return read().isLoop();
    }
    public static boolean isLogs() {
        return read().isLogs();
    }
    private static Settings read() {
        Settings settings = new Settings();
        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Settings>() {}.getType();
            settings = gson.fromJson(reader, type);
            if (settings == null) {
                settings = new Settings();
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла JSON: " + e.getMessage());
        }
        return settings;
    }

    private static void write(Settings settings) {
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(settings, writer);
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл JSON: " + e.getMessage());
        }
    }
}
