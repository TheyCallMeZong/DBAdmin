package com.database.dbadmin.models;

import lombok.Data;

import java.util.Set;

@Data
public class Hotel {
    private Long id;
    private String name;
    private byte hotelClass;

    public Hotel(String name, byte hotelClass) {
        this.name = name;
        this.hotelClass = hotelClass;
    }
    public Hotel(Long id, String name, byte hotelClass) {
        this.id = id;
        this.name = name;
        this.hotelClass = hotelClass;
    }
}
