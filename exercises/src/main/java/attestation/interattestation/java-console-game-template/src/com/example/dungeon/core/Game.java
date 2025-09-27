package com.example.dungeon.core;

import com.example.dungeon.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final GameState state = new GameState();    // Создание состояния игры
    private final Map<String, Command> commands = new LinkedHashMap<>();    // Карта команд
    private final List<Room> allRooms = new ArrayList<>();  // добавляем список всех комнат

    static {
        WorldInfo.touch("Game");    // Вызов метода для логирования
    }

    public Game() {
        registerCommands(); // Регистрация всех команд
        bootstrapWorld();   // Создание игрового мира
    }

    private void registerCommands() {   // Метод регистрации команд

        commands.put("help", (ctx, a) -> System.out.println("Команды: " +
                String.join(", ", commands.keySet()))); // Команда помощи

        commands.put("gc-stats", (ctx, a) -> {  // Команда статистики памяти

            System.out.println("=".repeat(60));
            System.out.println("                Статистика памяти JVM");
            System.out.println("=".repeat(60));

            showMemoryStats("Текущее состояние");

            System.out.println("\nВызываем System.gc()...");
            long beforeUsed = getUsedMemory();
            long startTime = System.currentTimeMillis();

            System.gc();

            try { // работает GC
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            long endTime = System.currentTimeMillis();
            long afterUsed = getUsedMemory();
            long freedMemory = beforeUsed - afterUsed;

            System.out.printf("Сборка мусора заняла: %d мс\n", endTime - startTime);
            System.out.printf("Освобождено памяти: %,d байт (%.2f МБ)\n", freedMemory, freedMemory / (1024.0 * 1024.0));

            showMemoryStats("После сборки мусора");
            System.out.println("=".repeat(60));
        });

        commands.put("alloc", (ctx, a) -> {
            if (a.isEmpty()) {
                throw new InvalidCommandException("Укажите количество объектов: alloc <количество>");
            }

            try {
                int count = Integer.parseInt(a.getFirst());
                if (count <= 0 || count > 100000) {
                    throw new InvalidCommandException("Количество должно быть от 1 до 100000");
                }

                System.out.println("Создаём " + count + " тестовых объектов...");

                List<Object> garbage = new ArrayList<>(); // генерируем различные игровые сущности
                long startTime = System.currentTimeMillis();

                for (int i = 0; i < count; i++) {
                    if (i % 4 == 0) {
                        garbage.add(new Potion("Зелье для теста " + i, i % 10 + 1));
                    } else if (i % 4 == 1) {
                        garbage.add(new Weapon("Оружие для теста " + i, i % 5 + 1));
                    } else if (i % 4 == 2) {
                        garbage.add(new Key("Ключ для теста " + i, "door_" + i));
                    } else {
                        garbage.add(new Monster("Монстр для теста " + i, i % 3 + 1, i % 20 + 10));
                    }

                    if (count > 1000 && i > 0) {
                        int step = Math.max(1, count / 10); // защита от деления на 0
                        if (i % step == 0) {
                            System.out.printf("Создано %d%% объектов...\n", (i * 100) / count);
                        }
                    }
                }

                long endTime = System.currentTimeMillis();
                long allTime = endTime - startTime;

                System.out.printf("Создание %d объектов заняло: %d мс\n", count, allTime);
                System.out.printf("Среднее время создания одного объекта: %.3f мс\n", (double) allTime / count);

                showMemoryStats("После создания объектов"); // показ статистики памяти после создания

                System.out.println("\nОсвобождаем ссылки на объекты..."); // очищаем список, создавая мусор для GC
                garbage.clear();

                System.out.println("Вызываем System.gc()..."); // принудительно вызываем GC для показа
                long beforeGC = getUsedMemory();
                System.gc();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                long afterGC = getUsedMemory();
                System.out.printf("Память освобождена: %,d байт (%.2f МБ)\n",
                        beforeGC - afterGC, (beforeGC - afterGC) / (1024.0 * 1024.0));

                showMemoryStats("После сборки мусора");

            } catch (NumberFormatException e) {
                throw new InvalidCommandException("Некорректное число: " + a.getFirst());
            }
        });

        commands.put("look", (ctx, a) ->
                System.out.println(ctx.getCurrent().describe())); // Команда осмотра комнаты

        commands.put("move", (ctx, a) -> {  // Команда перемещения
            if (a.isEmpty()) {  // проверка корректности выбора направления или наше исключение
                throw new InvalidCommandException("Укажите направление: move (north/south/east/west)");
            }

            String dir = a.getFirst().toLowerCase(Locale.ROOT);     // получаем первый элемент списка

            if (!Arrays.asList("north", "south", "east", "west").contains(dir)) {
                throw new InvalidCommandException("Некорректное направление: " + dir +
                        ". Используйте: north, south, east, west");
            }

            Room curRoom = ctx.getCurrent();                        // получаем комнату
            Map<String, Room> neighbors = curRoom.getNeighbors();   // и направления

            if (!neighbors.containsKey(dir)) {      // проверяем введённое направление и наличие выхода
                throw new InvalidCommandException("Нет выхода в направление: " + dir);
            }

            if (curRoom.getLockedExits().containsValue(dir)) { // проверка закрытой двери
                String doorId = curRoom.getLockedExits().entrySet().stream() // ищем по id
                        .filter(entry -> entry.getValue().equals(dir))
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse("");

                throw new InvalidCommandException("Дверь в " + dir + " заперта. Нужен ключ для двери '" + doorId + "'");
            }

            Room newCurrentRoom = neighbors.get(dir);   // получаем комнату, куда переместились
            ctx.setCurrent(newCurrentRoom);             // делаем текущей

            System.out.println("Вы перешли в: " + newCurrentRoom.getName()); // сообщаем об успехе
            System.out.println(newCurrentRoom.describe());
        });

        commands.put("take", (ctx, a) -> {  // Команда взятия предмета
            if (a.isEmpty()) {  // проверка, что наименование предмета имеется
                throw new InvalidCommandException("Укажите наименование предмета: take (item name)");
            }

            String itemName = String.join(" ", a); // соединияем все наименования в строку

            if (itemName.trim().isEmpty()) {
                throw new InvalidCommandException("Название предмета не может быть пустым");
            }

            Room curRoom = ctx.getCurrent();            // получаем комнату
            List<Item> roomItems = curRoom.getItems();  // и предметы в ней

            Optional<Item> foundItem = roomItems.stream()   // ищем предмет в комнате, даже если null
                    .filter(i -> i.getName().equalsIgnoreCase(itemName))
                    .findFirst();

            if (foundItem.isEmpty()) {  // проверяем, что предмент найден, иначе исключение
                throw new InvalidCommandException("Предмет <" + itemName + "> не найден в этой комнате." +
                        "Доступные предметы: " + roomItems.stream().map(Item::getName)
                        .collect(Collectors.joining(",")));
            }

            Item item = foundItem.get();    // получаем из него предмет
            ctx.getPlayer().getInventory().add(item);   // кладём предмет в инвентарь игрока
            roomItems.remove(item);     // убираем предмет из комнаты

            System.out.println("Взято: " + item.getName());   // сообщаем, что взяли предмет

        });

        commands.put("inventory", (ctx, a) -> { // Команда инвентаря

            List<Item> inventory = ctx.getPlayer().getInventory();  // получаем инвеентарь игрока

            if (inventory.isEmpty()) {  // проверка, что инвентарь не пуст
                System.out.println("Инвентарь пуст");
                return;
            }

            Map<String, Map<String, Long>> groupOfItems = inventory.stream() // группируем предметы
                    .collect(Collectors.groupingBy(
                            i -> i.getClass().getSimpleName(),  // по типу
                            Collectors.groupingBy(
                                    Item::getName,                  // внутри типа по наименованию
                                    Collectors.counting()           // считаем количество
                            )
                    ));

            groupOfItems.entrySet().stream()            // выводим текущий инвентарь
                    .sorted(Map.Entry.comparingByKey()) // сортируем по по типу
                    .forEach(entry -> { // разбиваем на коллекции ключи и значения для вывода
                        String itemClass = entry.getKey();
                        Map<String, Long> items = entry.getValue();
                        // выводим предмет и количество
                        items.entrySet().stream()
                                .sorted(Map.Entry.comparingByKey()) // сортируем по имени в инвентаре
                                .forEach(i ->
                                        System.out.println("- " + itemClass + " (" + i.getValue() + "): " +
                                                i.getKey()));
                    });
        });

        commands.put("use", (ctx, a) -> {   // Команда использования предмета

            if (a.isEmpty()) {
                throw new InvalidCommandException("Укажите наименование предмета: use (item name)");
            }

            String itemName = String.join(" ", a); // соединяем все аргументы

            if (itemName.trim().isEmpty()) {
                throw new InvalidCommandException("Название предмета не может быть пустым");
            }

            Player player = ctx.getPlayer();               // получаем текущего игрока
            List<Item> inventory = player.getInventory();  // и его инвентарь

            Optional<Item> foundItem = inventory.stream() // ищем предмет в инвентаре
                    .filter(i -> i.getName().equalsIgnoreCase(itemName))
                    .findFirst();

            if (foundItem.isEmpty()) {
                throw new InvalidCommandException("Предмет <" + itemName + "> не найден в этой комнате." +
                        "Инвентарь: " + (inventory.isEmpty() ? "пуст" : inventory.stream().map(Item::getName)
                        .collect(Collectors.joining(","))));
            }

            Item item = foundItem.get();

            item.apply(ctx);
        });

        commands.put("fight", (ctx, a) -> { // Команда боя

            if (!a.isEmpty()) {
                throw new InvalidCommandException("Команда fight не требует аргументов");
            }

            Room curRoom = ctx.getCurrent();
            Monster monster = curRoom.getMonster();

            if (monster == null) {
                throw new InvalidCommandException("В комнате нет монстра для боя");
            }

            Player player = ctx.getPlayer();

            if (player.getHp() <= 0) {
                throw new InvalidCommandException("Игрок мертв и не может сражаться");
            }

            if (monster.getHp() <= 0) {
                throw new InvalidCommandException("Монстр уже побеждён");
            }

            while (player.getHp() > 0 && monster.getHp() > 0) { // цикл боя по шагам, по очереди

                // бьёт игрок
                int playerDamage = player.getAttack();
                monster.setHp(monster.getHp() - playerDamage);
                System.out.println("Вы бьёте " + monster.getName() + " на " + playerDamage + ". HP монстра : " +
                        Math.max(0, monster.getHp()));

                // проверяем убит ли монстр
                if (monster.getHp() <= 0) {
                    System.out.println("Вы победили: " + monster.getName() + "!");

                    if (Math.random() > 0.5) {
                        Potion loot = new Potion("Зелье здоровья", 10);
                        curRoom.getItems().add(loot);
                        System.out.println("Из монстра выпало: " + loot.getName());
                    }

                    curRoom.setMonster(null); // убираем монстра из комнаты
                    ctx.addScore(10);      // добавляем очки победителю
                    break;                    // выходим из цикла после победы
                }

                // бьёт монстр
                int monsterDamage = monster.getLevel(); // простой урон - у монстра уровень
                player.setHp(player.getHp() - monsterDamage);
                System.out.println("Монстр отвечает на " + monsterDamage + ". Ваше HP: " +
                        Math.max(0, player.getHp()));

                if (player.getHp() <= 0) { // кто победил
                    saveScoreExit(ctx); // сохраняем очки перед выходом
                    System.out.println("Вы погибли! Игра окончена. Ваши очки: " + ctx.getScore());
                    System.exit(0);
                }
            }
        });

        commands.put("save", (ctx, a) -> {
            SaveLoad.setAllRooms(allRooms); // устанавливаем список комнат перед сохранением
            SaveLoad.save(ctx);     // Команда сохранения
        });

        commands.put("load", (ctx, a) -> { // обновляем список комнат после загрузки
            try {
                SaveLoad.load(ctx);     // Команда загрузки
            } catch (Exception e) {
                System.out.println("Ошибка загрузки: " + e.getMessage());
            }
        });

        commands.put("scores", (ctx, a) -> SaveLoad.printScores()); // Команда показа очков

        commands.put("score", (ctx, a) -> {             // команда показа текущих очков
            System.out.println("Ваши текущие очки: " + ctx.getScore());
        });

        commands.put("stats", (ctx, a) -> {
            Player player = ctx.getPlayer();
            System.out.println("=== Статистика игрока ===");
            System.out.println("Имя: " + player.getName());
            System.out.println("HP: " + player.getHp());
            System.out.println("Атака: " + player.getAttack());
            System.out.println("Очки: " + ctx.getScore());
            System.out.println("Посещено комнат: " + ctx.getVisitedRoomsCount());
        });

        commands.put("exit", (ctx, a) -> {   // Команда выхода
            if (ctx.getScore() > 0) {
                System.out.println("Ваши текущие очки: " + ctx.getScore());
                System.out.print("Вы уверены, что хотите выйти? (да/нет): ");

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    String response = reader.readLine().trim().toLowerCase();

                    if (response.equals("да") || response.equals("y") || response.equals("yes")) {
                        saveScoreExit(ctx);
                        System.out.println("Пока! Ваши очки сохранены.");
                        System.exit(0);
                    } else {
                        System.out.println("Продолжаем игру!");
                        return;
                    }
                } catch (IOException e) {
                    saveScoreExit(ctx);
                    System.out.println("Пока!");
                    System.exit(0);
                }
            } else {
                System.out.println("Пока!");
                System.exit(0);                                 // Завершение программы
            }
        });
    }

    // метод для получения используемой памяти в байтах
    private long getUsedMemory() {
        Runtime rt = Runtime.getRuntime();
        return rt.totalMemory() - rt.freeMemory();
    }

    // метод для показа статистики памяти
    private void showMemoryStats(String title) {
        Runtime rt = Runtime.getRuntime();

        long free = rt.freeMemory();
        long total = rt.totalMemory();
        long used = total - free;
        long max = rt.maxMemory();

        double usedMb = used / (1024.0 * 1024.0);
        double totalMb = total / (1024.0 * 1024.0);
        double maxMb = max / (1024.0 * 1024.0);
        double loadPercent = (double) used / total * 100;

        System.out.println(title + ":");
        System.out.printf("  Используется: %,d байт (%.2f МБ)\n", used, usedMb);
        System.out.printf("  Свободно:    %,d байт (%.2f МБ)\n", free, free / (1024.0 * 1024.0));
        System.out.printf("  Всего:       %,d байт (%.2f МБ)\n", total, totalMb);
        System.out.printf("  Максимум:    %,d байт (%.2f МБ)\n", max, maxMb);
        System.out.printf("  Загрузка:    %.1f%%\n", loadPercent);
    }

    private void saveScoreExit(GameState ctx) {
        try {
            Player player = ctx.getPlayer();
            int score = ctx.getScore();

            if (score > 0) {
                SaveLoad.writeScore(player.getName(), score);
                System.out.println("Ваши очки (" + score + ") сохранены в таблице лидеров.");
            }
        } catch (Exception e) {
            System.err.println("Не удалось сохранить очки: " + e.getMessage());
        }
    }

    private void bootstrapWorld() {     // Создание игрового мира

        Player hero = new Player("Герой", 20, 5);   // Создаём игрока
        state.setPlayer(hero);      // устанавливаем игроку состояние

        Room square = new Room("Площадь", "Каменная площадь с фонтаном.");  // создаём 1ую комнату
        Room forest = new Room("Лес", "Шелест листвы и птичий щебет.");     // 2ая комната
        Room cave = new Room("Пещера", "Темно и сыро.");                    // 3я комната
        Room grot = new Room("Грот", "Комната с сундуком с вещами");

        square.getNeighbors().put("north", forest);                     // связываем комнаты 1 и 2 = север
        forest.getNeighbors().put("south", square);                     // связываем комнаты 2 и 1 = юг
        forest.getNeighbors().put("east", cave);                        // связываем комнаты 2 и 3 = восток
        cave.getNeighbors().put("west", forest);                        // связываем комнаты 3 и 2 = запад

        cave.addLockedExit("north", grot, "grot_door");    // запертая дверь из пещеры в грот
        grot.getNeighbors().put("south", cave); // обратный ход

        forest.getItems().add(new Potion("Малое зелье", 5));    // добавляем предмет в 2ую комнату
        cave.getItems().add(new Key("Старый ключ", "grot_door")); // добавим ключ от грота

        grot.getItems().add(new Weapon("Двуручный меч", 10)); // добавляем вещи
        grot.getItems().add(new Potion("Эликсир жизни", 25));

        forest.setMonster(new Monster("Волк", 1, 8));       // добавляем монстра в 2ую комнату
        grot.setMonster(new Monster("Охранник сундука", 3, 15)); // и в грот

        // сохраняем все комнаты в список
        allRooms.add(square);
        allRooms.add(forest);
        allRooms.add(cave);
        allRooms.add(grot); // добавление новой комнаты

        SaveLoad.setAllRooms(allRooms); // передаём комнаты в SaveLoad
        state.setCurrent(square);   // устанавливаем начала в 1ой комнате
    }

    public void run() {     // основная цикл программы

        System.out.println("DungeonMini. 'help' — команды."); // приветствие

        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {    // читаем ввод

            while (true) {  // читаем весь ввод пока true

                System.out.print("> ");         // приглашаем ввести команду
                String line = in.readLine();    // считываем команду
                if (line == null) break;        // выходим, если пусто
                line = line.trim();             // удаляем пробелы ввода
                if (line.isEmpty()) continue;   // пропускам пустые строки, если будут

                List<String> parts = Arrays.asList(line.split("\s+"));  // разделяем на слова
                String cmd = parts.getFirst().toLowerCase(Locale.ROOT);       // первое слово - команда
                List<String> args = parts.subList(1, parts.size());           // остальные слова - доступные действия
                Command c = commands.get(cmd);                                // ищем команды

                try {   // проверяем, чтобы команда была
                    if (c == null) throw new InvalidCommandException("Неизвестная команда: " + cmd);
                    c.execute(state, args);     // выполняем команду
                    state.addScore(1);      // увеличиваем счетчик команд
                } catch (InvalidCommandException e) {                   // обрабатываем ошибку команды
                    System.out.println("Ошибка: " + e.getMessage());    // выводим сообщение об ошибке
                } catch (Exception e) {                                 // обрабатываем другие ошибки команд + вывод
                    saveScoreExit(state);
                    System.out.println("Непредвиденная ошибка: " + e.getClass().getSimpleName() + ": "
                            + e.getMessage());
                }
            }
        } catch (IOException e) {   // обрабатываем ошибки ввода/вывода
            saveScoreExit(state);
            System.out.println("Ошибка ввода/вывода: " + e.getMessage());
        } finally { // гарантируем сохранение очков при любом выходе из игры
            saveScoreExit(state);
        }
    }
}
