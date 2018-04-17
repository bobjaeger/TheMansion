package com.den.jaeger.themansion.game_feature.model;

public class RoomModel {
    private int id;
    private String roomName;

    public RoomModel(int id, String roomName) {
        this.id = id;
        this.roomName = roomName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}