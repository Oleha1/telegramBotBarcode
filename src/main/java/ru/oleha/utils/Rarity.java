package ru.oleha.utils;

public enum Rarity {
    COMMON("Обычный"),
    UNCOMMON("Необычный"),
    SPECIAL("Особый"),
    RARE("Редкий"),
    EXCEPTIONAL("Исключительный"),
    LEGENDARY("Легендарный");
    private String rarity;

    Rarity(String rarity) {
        this.rarity = rarity;
    }

    public String getRusName() {
        return rarity;
    }
}
