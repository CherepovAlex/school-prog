package com.example.dungeon.model;

import java.util.*;

public class Player extends Entity {        // класс для игрока
    private int attack;     // атака игрока
    private final List<Item> inventory = new ArrayList<>();     // инвентарь игрока

    public Player(String name, int hp, int attack) {
        super(name, hp);
        this.attack = attack;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public List<Item> getInventory() {
        return inventory;
    }
}
