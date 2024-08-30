package ru.oleha.utils;

public class BuyItems {
    private String itemName;
    private int minPrice;
    private int minStack;

    public BuyItems() {

    }

    public BuyItems(String itemName, int minPrice, int minStack) {
        this.itemName = itemName;
        this.minPrice = minPrice;
        this.minStack = minStack;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMinStack() {
        return minStack;
    }

    public void setMinStack(int minStack) {
        this.minStack = minStack;
    }

    @Override
    public String toString() {
        return "BuyItems{" +
                "itemName='" + itemName + '\'' +
                ", minPrice=" + minPrice +
                ", minStack=" + minStack +
                '}';
    }
}
