package ru.oleha;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class main {
    public static void main(String[] args) {
        String botToken = "7965017237:AAGIJfQFZVjurLjy15bABpovF_SJiFunUTM";
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new BotHandler(botToken));
            System.out.println("MyAmazingBot successfully started!");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}