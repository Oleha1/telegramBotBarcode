package ru.oleha;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import ru.oleha.api.DiscordWebhook;
import ru.oleha.configs.ConfigSettings;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class KeyHandler implements NativeKeyListener {
    static final DateTimeFormatter DAYS = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == ConfigSettings.getKeyIDStart()) {
            try {
                main.loop.setSelected(!main.loop.isSelected());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getKeyCode() == ConfigSettings.getKeyIDExit()) {
            try {
                DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
                embedObject.setTitle("Бот для покупки предметов был закрыт");
                embedObject.setColor(new Color(0xFF0000));
                String daysAndTime = String.format("[%s : %s]", LocalDateTime.now().format(DAYS),LocalDateTime.now().format(TIME));
                embedObject.addField("Время: " + daysAndTime,"",false);
                DiscordWebhook discordWebhook = new DiscordWebhook("https://discord.com/api/webhooks/1278978943957078026/OoGscpezIn1BTFaIL_RJ2jRjgYCuQ-IO7q411PwHelNwRC3kPcGnXTdb0wjg_WoUMSpA");
                discordWebhook.addEmbed(embedObject);
                discordWebhook.execute();
                GlobalScreen.removeNativeKeyListener(new KeyHandler());
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException | IOException ex) {
                throw new RuntimeException(ex);
            }
            System.exit(0);
        }
//        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
//            try {
//                GlobalScreen.unregisterNativeHook();
//            } catch (NativeHookException nativeHookException) {
//                nativeHookException.printStackTrace();
//            }
//        }
    }

//    public void nativeKeyReleased(NativeKeyEvent e) {
//        System.out.println("nativeKeyReleased Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
//    }
//
//    public void nativeKeyTyped(NativeKeyEvent e) {
//        System.out.println("nativeKeyTyped Key Typed: " + e.getKeyText(e.getKeyCode()));
//    }
}