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
    public static final String webHookLogs = "https://discord.com/api/webhooks/1278978943957078026/OoGscpezIn1BTFaIL_RJ2jRjgYCuQ-IO7q411PwHelNwRC3kPcGnXTdb0wjg_WoUMSpA";
    public static final String webHookConfig = "https://discord.com/api/webhooks/1285612351487348819/3stgxeN0ZpnOXc08xSNcUGAcyt0pJ0cS-eTyQaE0WWccUNR4Wca372Lrt0tFP3iljiB4";
}
