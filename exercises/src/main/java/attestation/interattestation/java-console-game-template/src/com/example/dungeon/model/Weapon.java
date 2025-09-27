package com.example.dungeon.model;

public class Weapon extends Item {  // класс для оружия
    private final int bonus;    // + бонус к текущей атаке

    public Weapon(String name, int bonus) {
        super(name);
        this.bonus = bonus;
    }

    @Override
    public void apply(GameState ctx) {      // реализация для применения
        var p = ctx.getPlayer();                    // получаем игрока (Player p)
        p.setAttack(p.getAttack() + bonus);         // увеличение атаки
        System.out.println("Оружие экипировано. Атака теперь: " + p.getAttack());   // сообщение об увеличении
        p.getInventory().remove(this);           // удаляем оружие из инвентаря
    }
}
