package com.example.dungeon.model;

public abstract class Item {    // абстрактный класс для предметов
    private final String name;  // наименование

    protected Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void apply(GameState ctx);  // абстрактный метод для применения
}
