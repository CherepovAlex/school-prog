package com.example.dungeon.core;

import com.example.dungeon.model.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SaveLoad {
    private static final Path SAVE = Paths.get("save.txt");      // Путь к файлу сохранения
    private static final Path SCORES = Paths.get("scores.csv");  // Путь к файлу очков

    private static List<Room> allRooms = new ArrayList<>();

    public static void setAllRooms(List<Room> rooms) {
        allRooms = new ArrayList<>(rooms);
    }

    public static void save(GameState s) {      // метод для сохранения

        try (BufferedWriter w = Files.newBufferedWriter(SAVE)) {    // создаём writer
            Player p = s.getPlayer();                               // получаем игрока
            // записываем статы игрока в файл и добавляем очки
            w.write("player;" + p.getName() + ";" + p.getHp() + ";" + p.getAttack() + ";" + s.getScore());
            w.newLine();                                            // переходим на новую строку
            // формируем инвентарь игрока
            String inv = p.getInventory().stream()
                    .map(i -> {
                        if (i instanceof Key key) {
                            return i.getClass().getSimpleName() + ":" + i.getName() + ":" + key.getDoorId();
                        } else {
                            return i.getClass().getSimpleName() + ":" + i.getName();
                        }
                    })
                    .collect(Collectors.joining(","));
            w.write("inventory;" + (inv.isEmpty() ? "empty" : inv));    // записываем инвентарь в файл
            w.newLine();                        // переходим на новую строку
            //записываем текущую комнату
            w.write("current_room;" + s.getCurrent().getName());
            w.newLine();                        // переходим на новую строку
            // сохраняем состояние комнат (итемы и монстры))
            for (Room room : allRooms) {
                saveRoomState(w, room);
            }
            System.out.println("Сохранено в " + SAVE.toAbsolutePath()); // сообщаем о записи в файл по пути
            writeScore(p.getName(), s.getScore());  // записываем текущие очки
        } catch (IOException e) {               // обрабатываем ошибку, если будет
            throw new UncheckedIOException("Не удалось сохранить игру", e);
        }
    }

    private static void saveRoomState(BufferedWriter w, Room room) throws IOException {

        // сохраняем базовую информацию о комнате
        w.write("room;" + room.getName() + ";" + room.describe().replace(";", ","));
        w.newLine();

        // сохраняем монстра в комнате (если есть)
        if (room.getMonster() != null) {
            Monster m = room.getMonster();
            w.write("room_monster;" + room.getName() + ";" + m.getName() + ";" + m.getLevel() + ";" + m.getHp());
            w.newLine();
        }

        // сохраняем предметы в комнате (если есть)
        if (!room.getItems().isEmpty()) {
            String roomItems = room.getItems().stream()
                    .map(i -> i.getClass().getSimpleName() + ":" + i.getName())
                    .collect(Collectors.joining(","));
            w.write("room_items;" + room.getName() + ";" + roomItems);
            w.newLine();
        }

        // сохраняем связи в конате (выходы)
        if (!room.getNeighbors().isEmpty()) {
            String exits = room.getNeighbors().entrySet().stream()
                    .map(entry -> entry.getKey() + ":" + entry.getValue().getName())
                    .collect(Collectors.joining(","));
            w.write("room_exits;" + room.getName() + ";" + exits);
            w.newLine();
        }

        // сохраняем запертые двери
        if (!room.getLockedExits().isEmpty()) {
            String lockedDoors = room.getLockedExits().entrySet().stream()
                    .map(entry -> entry.getKey() + ":" + entry.getValue())
                    .collect(Collectors.joining(","));
            w.write("room_locked_exits;" + room.getName() + ";" + lockedDoors);
            w.newLine();
        }
    }

    public static void load(GameState s) {          // метод для загрузки
        if (!Files.exists(SAVE)) {                  // проверяем наличие файла по нашему пути
            System.out.println("Сохранение не найдено.");   // пишем, если не нашли
            return;
        }
        try (BufferedReader r = Files.newBufferedReader(SAVE)) {    // создаём reader

            Map<String, String> map = new HashMap<>();              // мапа для данных игрока
            Map<String, Room> roomData = new HashMap<>();              // мапа для состояния комнаты
            String curRoomName = "";
            String line;

            while ((line = r.readLine()) != null) {    // читаем запись
                String[] parts = line.split(";", 2);     // разделяем на ключ-значение для мапы
                if (parts.length < 2) continue;

                String key = parts[0];
                String value = parts[1];

                switch (key) {
                    case "player":
                    case "inventory":
                    case "current_room":
                        map.put(key, value);
                        if (key.equals("current_room")) {
                            curRoomName = value;
                        }
                        break;
                    // создаём или находим комнату
                    case "room":
                        String[] roomParts = value.split(";", 3);
                        if (roomParts.length >= 2) {
                            Room room = roomData.getOrDefault(roomParts[1], new Room(roomParts[1],
                                    roomParts.length >= 3 ? roomParts[2] : ""));
                            roomData.put(roomParts[1], room);
                        }
                        break;
                    case "room_monster":
                    case "room_items":
                    case "room_exits":
                    case "room_locked_exits": // эти данные обрабатываем после создания всех комнат
                        map.put(line, "");  // сохраняем всё в строку
                        break;
                }
            }

            getRoomConnections(map, roomData);  // связи между комнатами
            getRoomStates(map, roomData);       // состояния комнат (какие там предметы, монстры)

            loadPlayerData(s, map); // загружаем статы игрока
            loadInventory(s, map);  // загружаем его инвентарь

            if (roomData.containsKey(curRoomName)) { // делаем текущую комнату
                s.setCurrent(roomData.get(curRoomName));
            }

            setAllRooms(new ArrayList<>(roomData.values())); // сохраняем все комнаты

            System.out.println("Игра загружена успешно.");      // если удачно, то сообщаем
        } catch (IOException e) {       // обрабатываем ошибку, если будет
            throw new UncheckedIOException("Не удалось загрузить игру", e);
        }
    }

    private static void getRoomConnections(Map<String, String> data, Map<String, Room> roomData) {
        for (String key : data.keySet()) {
            if (key.startsWith("room_exits;")) {
                String[] parts = key.split(";");
                if (parts.length >= 3) {
                    String roomName = parts[1];
                    Room room = roomData.get(roomName);
                    if (room != null) {
                        String[] exits = parts[2].split(",");
                        for (String exit : exits) {
                            String[] exitParts = exit.split(":");
                            if (exitParts.length == 2) {
                                String direction = exitParts[0];
                                String targetroomName = exitParts[1];
                                Room targetRoom = roomData.get(targetroomName);
                                if (targetRoom != null) {
                                    room.getNeighbors().put(direction, targetRoom);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void getRoomStates(Map<String, String> data, Map<String, Room> roomData) {
        for (String key : data.keySet()) {
            try {
                if (key.startsWith("room_monster;")) {
                    String[] parts = key.split(";");
                    if (parts.length >= 5) {
                        Room room = roomData.get(parts[1]);
                        if (room != null) {
                            try {
                                Monster monster = new Monster(parts[2],
                                        Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
                                room.setMonster(monster);
                            } catch (NumberFormatException e) {
                                System.err.println("Ошибка парсинга данных монстра: " + key);
                            }
                        }
                    }
                } else if (key.startsWith("room_items;")) {
                    String[] parts = key.split(";");
                    if (parts.length >= 3) {
                        Room room = roomData.get(parts[1]);
                        if (room != null) {
                            room.getItems().clear();
                            if (!parts[2].isEmpty()) {
                                for (String item : parts[2].split(",")) {
                                    String[] itemParts = item.split(":", 2);
                                    if (itemParts.length == 2) {
                                        switch (itemParts[0]) {
                                            case "Potion" -> room.getItems().add(new Potion(itemParts[1], 5));
                                            case "Key" -> room.getItems().add(new Key(itemParts[1]));
                                            case "Weapon" -> room.getItems().add(new Weapon(itemParts[1], 3));
                                            default -> {
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (key.startsWith("room_locked_exits;")) {
                    String[] parts = key.split(";");
                    if (parts.length >= 3) {
                        Room room = roomData.get(parts[1]);
                        if (room != null) {
                            room.getLockedExits().clear();
                            if (!parts[2].isEmpty()) {
                                for (String lock : parts[2].split(",")) {
                                    String[] lockParts = lock.split(":");
                                    if (lockParts.length == 2) {
                                        room.getLockedExits().put(lockParts[0], lockParts[1]);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Ошибка загрузки состояния комнаты: " + key);
            }
        }
    }

    private static void loadPlayerData(GameState s, Map<String, String> data) {
        Player p = s.getPlayer();
        String playerData = data.getOrDefault("player", "player;Hero;10;3;0");
        String[] pp = playerData.split(";");

        p.setName(pp[1]);                       // устанавливаем имя игрока
        p.setHp(Integer.parseInt(pp[2]));       // устанавлиаем HP игрока
        p.setAttack(Integer.parseInt(pp[3]));   // устанавливаем атаку игрока

        if (pp.length > 4) {
            s.addScore(Integer.parseInt(pp[4]) - s.getScore()); // делаем счёт
        }
    }

    private static void loadInventory(GameState s, Map<String, String> data) {
        Player p = s.getPlayer();
        p.getInventory().clear();

        String inv = data.getOrDefault("inventory", ""); // получаем инвентарь
        if (!inv.isBlank() && !inv.equals("empty")) {       // если не пустой, то обрабатываем
            for (String tok : inv.split(",")) {
                String[] t = tok.split(":", 2);                 // сплит на тип и имя
                if (t.length < 2) continue;                         // если не полная запись, то не трогаем
                switch (t[0]) {                                     // обработка по типу
                    case "Potion" -> p.getInventory().add(new Potion(t[1], 5));    // + зелье
                    case "Key" -> {                                                     // + ключ
                        String doorId = t.length >= 3 ? t[2] : "default_door";
                        p.getInventory().add(new Key(t[1], doorId));
                    }
                    case "Weapon" -> p.getInventory().add(new Weapon(t[1], 3));   // + оружие
                    default -> {    // другие не трогаем
                    }
                }
            }
        }
    }

    public static void printScores() {      // метод для показа очков
        if (!Files.exists(SCORES)) {        // проверяем наличие файла по нашему пути
            System.out.println("Пока нет результатов.");    // пишем, если не нашли
            return;
        }

        try (BufferedReader r = Files.newBufferedReader(SCORES)) { // создаём reader
            System.out.println("=".repeat(50));
            System.out.println("           Таблица лидеров (топ-10):");       // пишем заголовок
            System.out.println("=".repeat(50));

            List<Score> scores = r.lines()
                    .skip(1)                                                 // пропускаем заголовок
                    .map(l -> {
                        String[] parts = l.split(",", 3);
                        if (parts.length == 3) {
                            try {
                                return new Score(parts[1], Integer.parseInt(parts[2]), LocalDateTime.parse(parts[0]));  // сплит и считываем очки
                            } catch (Exception e) {
                                return null;
                            }
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(Score::score).reversed())   // сортируем
                    .limit(10)  //берём первые 10
                    .toList();

            if (scores.isEmpty()) {
                System.out.println("           Пока нет записей");
            } else {
                System.out.printf("%-3s %-20s %-10s %-15s%n", "№", "Игрок", "Очки", "Дата");
                System.out.println("-".repeat(50));

                for (int i = 0; i < scores.size(); i++) {
                    Score score = scores.get(i);
                    String date = score.ts().toLocalDate().toString();
                    System.out.printf("%-3d %-20s %-10d %-15s%n", i + 1, score.player(), score.score(), date);
                }
            }
            System.out.println("=".repeat(50));
        } catch (IOException e) {   // обрабатываем ошибку, если будет
            System.err.println("Ошибка чтения результатов: " + e.getMessage());
        }
    }

    public static void writeScore(String player, int score) {  // метод для записи очков

        if (score <= 0) {
            return;
        } // не сохраняем нулевые и очки меньше нуля

        try {
            boolean header = !Files.exists(SCORES); // проверяем нужен ли зоголовок

            try (BufferedWriter w = Files.newBufferedWriter(SCORES,       // создаем writer
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (header) { // если заголовок нужен
                    w.write("ts,player,score"); // записываем заголовок
                    w.newLine();    // переходим к новой строке
                }              // если нет, то записываем данные
                w.write(LocalDateTime.now() + "," + player.replace(",", "") + "," + score);
                w.newLine();        // переходим к новой строке
            }
        } catch (IOException e) {   // обрабатываем ошибку, если будет
            System.err.println("Не удалось записать очки: " + e.getMessage());
        }
    }

    private record Score(String player, int score, LocalDateTime ts) {    // вспомогательный рекорд для очков

        private Score(String player, int score) {
            this(player, score, LocalDateTime.now());
        }
    }
}
