package com.example.dungeon.core;

public final class WorldInfo {
    private static final StringBuilder log = new StringBuilder();

    static {    // статический блок инициализации
        log.append("[static init WorldInfo]\n");            // запись в лог
        ClassLoader cl = WorldInfo.class.getClassLoader();  // получаем загрузчик классов
        log.append("ClassLoader: ").append(cl).append("\n");// запись инфо в лог
        if (cl != null) log.append("Parent: ").append(cl.getParent()).append("\n"); // запись родительского загрузчика
    }

    public static void touch(String who) {  // метод для ведения логирования
        log.append("touched by ").append(who).append("\n"); // запись в лог
    }

    private WorldInfo() {
    }
}
