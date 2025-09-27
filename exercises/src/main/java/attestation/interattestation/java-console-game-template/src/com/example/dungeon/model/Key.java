package com.example.dungeon.model;

public class Key extends Item {     // класс для ключа
    private final String doorId;    // id двери
    public Key(String name, String doorId) {
        super(name);
        this.doorId = doorId;
    }

    public Key(String name) {
        this(name, "defaultDoor");  // сохраним оригинальный вариант также рабочим
    }

    public String getDoorId() {
        return doorId;
    }

    @Override
    public void apply(GameState ctx) {  // реализация для применения
        Room curRoom = ctx.getCurrent();

        // чек закрытой двери
        if (curRoom.getLockedExits().containsKey(doorId)) {
            String dir = curRoom.getLockedExits().get(doorId);
            Room targetRoom = curRoom.getNeighbors().get(dir);

            if (targetRoom != null) {
                curRoom.getLockedExits().remove(doorId);
                System.out.println("Вы использовали ключ '" + getName() + "' и открыли дверь в " + dir + "!");
                ctx.getPlayer().getInventory().remove(this); // убираем использованный ключ
            }
        } else {
            System.out.println("Ключ звенит, но подходящей двери для него здесь нет."); // сообщение о применении
        }
    }
}
