package com.database.dbadmin.models;

import lombok.Data;

@Data
public class Hotel {
    private Long id;
    private String name;
    private byte hotelClass;

    public Hotel(String name, byte hotelClass) {
        this.name = name;
        this.hotelClass = hotelClass;
    }
}
