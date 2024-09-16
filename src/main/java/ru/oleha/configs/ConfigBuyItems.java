package ru.oleha.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.oleha.utils.BuyItems;
import ru.oleha.utils.Storage;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;

public class ConfigBuyItems {
    private static final String jsonName = "buyItems.json";
    private static File file;
    public static void init() {
        file = new File("./"+jsonName);
        try {
            if (file.createNewFile()) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                BuyItems configBuyItems = new BuyItems();
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), "UTF-8"))) {
                    gson.toJson(configBuyItems, writer);
                }
            } else {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), "UTF-8"))) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<BuyItems>>(){}.getType();
                    ArrayList<BuyItems> buyItemsMap = gson.fromJson(reader,type);
                    if (buyItemsMap == null) {
                              System.err.println("Файл конфигурации '" + jsonName + "' пуст или содержит некорректные данные.");
                    } else {
                        Storage.getBuyItems().addAll(buyItemsMap);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("ConfigBuyItems");
            System.err.println("Ошибка при создании файла");
            System.out.println(e.fillInStackTrace());
        }
    }
    public static int size() {
        return read().size();
    }
//    public static void addBuyItem(BuyItems buyItems) {
//        ArrayList<BuyItems> buyItemsMap = read();
//        buyItemsMap.add(buyItems);
//        write(buyItemsMap);
//    }
//    public static void removeBuyItem(String itemName) {
//        ArrayList<BuyItems> buyItemsMap = read();
//        Iterator<BuyItems> iterator = buyItemsMap.iterator();
//        while (iterator.hasNext()) {
//            BuyItems entry = iterator.next();
//            if (Objects.equals(entry.getItemName(), itemName)) {
//                iterator.remove();
//                break;
//            }
//        }
//        write(buyItemsMap);
//    }
    public static String getConfig() {
        String result = "";
        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type mapType = new TypeToken<ArrayList<BuyItems>>() {}.getType();
            result = gson.fromJson(reader, mapType).toString();
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла JSON: " + e.getMessage());
        }
        return result;
    }
    private static ArrayList<BuyItems> read() {
        ArrayList<BuyItems> craftDataMap = new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type mapType = new TypeToken<ArrayList<BuyItems>>() {}.getType();
            craftDataMap = gson.fromJson(reader, mapType);
            if (craftDataMap == null) {
                craftDataMap = new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла JSON: " + e.getMessage());
        }
        return craftDataMap;
    }

    private static void write(ArrayList<BuyItems> boxes) {
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(boxes, writer);
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл JSON: " + e.getMessage());
        }
    }
}
