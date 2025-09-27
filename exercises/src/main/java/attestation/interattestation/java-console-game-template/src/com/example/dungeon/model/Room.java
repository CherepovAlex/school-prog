package com.example.dungeon.model;

import java.util.*;

public class Room {                  // класс для комнаты
    private final String name;       // наименование комнаты
    private final String description;// описание комнаты
    private final Map<String, Room> neighbors = new HashMap<>(); // соседние комнаты (связи)
    private final List<Item> items = new ArrayList<>();          // предметы в комнате
    private Monster monster;                                     // монстр в комнате

    private final Map<String, String> lockedExits = new HashMap<>(); // фиксируем запертые двери (doorId -> dir)

    public Room(String name, String description) {
        this.name = name;               // наименоание
        this.description = description; // описание
    }

    public Map<String, String> getLockedExits() {
        return lockedExits;
    }

    public void addLockedExit(String dir, Room target, String doorId) { // добавляем закрытую дверь
        neighbors.put(dir, target);
        lockedExits.put(doorId, dir);
    }

    public String getName() {
        return name;
    }

    public Map<String, Room> getNeighbors() {
        return neighbors;
    }

    public List<Item> getItems() {
        return items;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster m) {
        this.monster = m;
    }

    public String describe() {          // метод для описания комнаты
        StringBuilder sb = new StringBuilder(name + ": " + description); // начало описания: имя + описание для комнаты

        if (!items.isEmpty()) {     // если есть предметы, то
            sb.append("\nПредметы: ")       // добавляем предметы
                    .append(String.join(", ", items.stream()
                            .map(Item::getName)
                            .toList()));
        }

        if (monster != null) {  // если есть монстр
            sb.append("\nВ комнате монстр: ")   // добавляем монстра
                    .append(monster.getName())
                    .append(" (ур. ")
                    .append(monster.getLevel())
                    .append(")");
        }

        if (!neighbors.isEmpty()) {     // если есть связи (выходы)
            sb.append("\nВыходы: ")     // добавляем все выходы
                    .append(String.join(", ", neighbors.keySet()));
        }

        if (!lockedExits.isEmpty()) { // добавление информации о запертых дверях
            sb.append("\nЗапертые двери: ");
            List<String> lockedDoors = new ArrayList<>();
            for (String dir : lockedExits.values()) {
                lockedDoors.add(dir + " (заперто)");
            }
            sb.append(String.join(", ", lockedDoors));
        }

        return sb.toString();       // возвращаем собранное описание
    }
}
