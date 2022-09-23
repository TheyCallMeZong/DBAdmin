package com.database.dbadmin.models;

import lombok.Data;

@Data
public class Country {
    private Long id;
    private String country;

    public Country(String country) {
        this.country = country;
    }
}
