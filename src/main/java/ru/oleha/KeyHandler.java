package ru.oleha;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import ru.oleha.api.DiscordWebhook;
import ru.oleha.configs.ConfigSettings;
import ru.oleha.thread.ThreadStart;

import java.awt.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class KeyHandler implements NativeKeyListener {
    static final DateTimeFormatter DAYS = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == ConfigSettings.getKeyIDStart()) {
            try {
                if (main.threadStart == null || !main.threadStart.isAlive()) {
                    main.threadStart = new ThreadStart();
                    main.threadStart.start();
                } else {
                    main.threadStart.stopRunning();
                    main.threadStart = null;
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getKeyCode() == ConfigSettings.getKeyIDExit()) {
            try {
                DiscordWebhook discordWebhook = main.setupDiscordWebhook("Бот для покупки предметов был закрыт",new Color(0x00FFFF));
                discordWebhook.execute();
                GlobalScreen.removeNativeKeyListener(new KeyHandler());
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException | IOException ex) {
                throw new RuntimeException(ex);
            }
            System.exit(0);
        }
    }
}