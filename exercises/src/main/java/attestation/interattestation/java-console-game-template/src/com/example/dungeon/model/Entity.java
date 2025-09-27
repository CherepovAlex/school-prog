package com.example.dungeon.model;

public abstract class Entity {  // абстрактный класс сущности
    private String name;    // имя
    private int hp;         // HP (здоровье)

    public Entity(String name, int hp) {
        this.name = name;
        this.hp = hp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
