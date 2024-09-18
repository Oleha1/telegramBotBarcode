package ru.oleha;

import ru.oleha.utils.ItemInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.Collection;

public class Debug {
    private static final String jsonName = "debug.json";
    private static File file = new File("./" + jsonName);
    public static void log(Collection<ItemInfo> buyItems) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file.toPath()), "UTF-8"))) {
            for (ItemInfo itemInfo: buyItems) {
                writer.write("itemName: " + itemInfo.getName().replace("\n","") + " itemStack: " + itemInfo.getStack() + " itemPrise: " + itemInfo.getPrice() + " rarity: " + itemInfo.getRarity());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
