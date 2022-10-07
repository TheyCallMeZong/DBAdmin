package com.database.dbadmin.models;

import lombok.Data;

import java.util.Set;

@Data
public class City {
    private Long id;
    private String name;

    private Country country;

    private Set<Hotel> hotelSet;

    public City(String name) {
        this.name = name;
    }

    public City() {
    }

    public City(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public City(String name, Country country, Set<Hotel> hotelSet) {
        this.name = name;
        this.country = country;
        this.hotelSet = hotelSet;
    }
}
