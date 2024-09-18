package ru.oleha.utils;

public class BuyItems {
    private String itemName;
    private int minPrice;
    private int minStack;
    private Rarity rarity;

    public BuyItems() {

    }

    public BuyItems(String itemName, int minPrice, int minStack) {
        this.itemName = itemName;
        this.minPrice = minPrice;
        this.minStack = minStack;
    }
    public BuyItems(String itemName, int minPrice, int minStack,Rarity rarity) {
        this.itemName = itemName;
        this.minPrice = minPrice;
        this.minStack = minStack;
        this.rarity = rarity;
    }

    public String getItemName() {
        return itemName;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMinStack() {
        return minStack;
    }

    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public String toString() {
        return "BuyItems{" +
                "itemName='" + itemName + '\'' +
                ", minPrice=" + minPrice +
                ", minStack=" + minStack +
                ", rarity=" + rarity +
                '}';
    }
}
