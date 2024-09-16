package ru.oleha.thread;

import ru.oleha.api.DiscordWebhook;
import ru.oleha.utils.ItemInfo;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ThreadDiscordLogs extends Thread {
    static final String webHook = "https://discord.com/api/webhooks/1278978943957078026/OoGscpezIn1BTFaIL_RJ2jRjgYCuQ-IO7q411PwHelNwRC3kPcGnXTdb0wjg_WoUMSpA";
    static final DateTimeFormatter DAYS = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    String text;
    Color color;
    ItemInfo itemInfo;

    public ThreadDiscordLogs(String text, Color color, ItemInfo itemInfo) {
        this.text = text;
        this.color = color;
        this.itemInfo = itemInfo;
    }

    @Override
    public void run() {
        try {
            String name = itemInfo.getName().replace("\n","");
            int price = itemInfo.getPrice();
            int stack = itemInfo.getStack();
            DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
            embedObject.setColor(this.color);
            embedObject.setTitle(this.text);
            embedObject.addField("Название:", name, false);
            embedObject.addField("Цена:", String.valueOf(price), false);
            embedObject.addField("Количество:", String.valueOf(stack), false);
            String daysAndTime = String.format("[%s : %s]", LocalDateTime.now().format(DAYS), LocalDateTime.now().format(TIME));
            embedObject.addField("Время:", daysAndTime, false);
            DiscordWebhook discordWebhook = new DiscordWebhook(webHook);
            discordWebhook.addEmbed(embedObject);
            discordWebhook.execute();
        } catch (Exception e) {
            e.printStackTrace();  // Log the full stack trace
            throw new RuntimeException("Error sending Discord webhook", e);
        }
    }
}
