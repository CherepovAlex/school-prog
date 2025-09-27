package com.example.dungeon.model;

public class Monster extends Entity {       // класс для монстра
    private int level;      // уровень монстра

    public Monster(String name, int level, int hp) {
        super(name, hp);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
