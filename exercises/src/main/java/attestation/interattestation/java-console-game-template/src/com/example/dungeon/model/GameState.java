package com.example.dungeon.model;

import java.util.HashSet;
import java.util.Set;

public class GameState {    // класс для состояния игры
    private Player player;  // текущий игрок
    private Room current;   // текущая комната
    private int score;      // счет игры

    private Set<String> visitedRooms = new HashSet<>();

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player p) {
        this.player = p;
    }

    public Room getCurrent() {
        return current;
    }

    public void setCurrent(Room r) {
        this.current = r;
        if (r != null) {
            visitedRooms.add(r.getName());
            checkRoomVisited(r.getName()); // проверяем посещение
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int d) {   // метод для добавления очков
        this.score += d;
    }

    public void checkRoomVisited(String roomName) {
        visitedRooms.add(roomName);
    }

    public int getVisitedRoomsCount() {
        return visitedRooms.size();
    }

    public boolean hasVisitedRoom(String roomName) {
        return visitedRooms.contains(roomName);
    }
}
