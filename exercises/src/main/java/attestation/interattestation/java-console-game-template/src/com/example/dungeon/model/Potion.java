package com.example.dungeon.model;

public class Potion extends Item {      // класс для зелья
    private final int heal;         // сколько лечит в HP (здоровье)

    public Potion(String name, int heal) {
        super(name);
        this.heal = heal;
    }

    @Override
    public void apply(GameState ctx) {      // реализация для применения
        Player p = ctx.getPlayer();                 // получаем игрока
        p.setHp(p.getHp() + heal);                  // на сколько лечим в HP
        System.out.println("Выпито зелье: +" + heal + " HP. Текущее HP: " + p.getHp()); // сообщаем о лечении
        p.getInventory().remove(this);           // удаляем зелье из листа (инвентаря)
    }
}
