package ru.oleha.utils;

import java.util.ArrayList;

public class Storage {
    private static ArrayList<BuyItems> buyItems = new ArrayList<>();
    private static Settings settings = new Settings();
    public static ArrayList<BuyItems> getBuyItems() {
        return buyItems;
    }
    public static Settings getSettings() {
        return settings;
    }
}
