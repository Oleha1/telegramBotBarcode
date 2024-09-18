package ru.oleha.utils;

import java.awt.image.BufferedImage;

public class ItemInfo {
    private final String name;
    private final int price;
    private final int stack;
    private final Rarity rarity;
    private final int x;
    private final int y;
    private final BufferedImage image;

    public ItemInfo(String name, int price, int stack,Rarity rarity, int x, int y, BufferedImage image) {
        this.name = name;
        this.price = price;
        this.stack = stack;
        this.rarity = rarity;
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStack() {
        return stack;
    }
    public Rarity getRarity() {
        return rarity;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "ItemInfo{" +
                "name='" + name.replace("\n","") + '\'' +
                ", price=" + price +
                ", stack=" + stack +
                '}';
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ItemInfo itemInfo = (ItemInfo) o;
//        return price == itemInfo.price && stack == itemInfo.stack && x == itemInfo.x && y == itemInfo.y && Objects.equals(name, itemInfo.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name, price, stack, x, y);
//    }
}
