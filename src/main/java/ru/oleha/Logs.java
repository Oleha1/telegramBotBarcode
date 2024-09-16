package ru.oleha;

import ru.oleha.utils.ItemInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logs {
    private static final DateTimeFormatter DAYS = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final String jsonName = "logs.json";
    private static final File file = new File("./" + jsonName);
    public static void log(String text, ItemInfo itemInfo) {
        String daysAndTime = String.format("[%s : %s]", LocalDateTime.now().format(DAYS),LocalDateTime.now().format(TIME));
        String finalText = String.format("%s %s '%s' за '%s' в количестве '%s'",daysAndTime,text, itemInfo.getName().replace("\n",""),itemInfo.getPrice(),itemInfo.getStack());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(finalText);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
