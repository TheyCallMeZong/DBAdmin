package com.database.dbadmin.models;

import lombok.Data;

@Data
public class Room {
    private Long id;
    private int numberOfBedRooms;
    private double price;

    public Room(int numberOfBedRooms, double price) {
        this.numberOfBedRooms = numberOfBedRooms;
        this.price = price;
    }
}
