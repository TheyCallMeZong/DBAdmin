package com.database.dbadmin.models;

import lombok.Data;

@Data
public class City {
    private Long id;
    private String name;

    private Country country;

    public City(String name) {
        this.name = name;
    }

    public City() {
    }
}
