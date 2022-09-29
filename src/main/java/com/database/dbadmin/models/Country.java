package com.database.dbadmin.models;

import lombok.Data;

import java.util.Set;

@Data
public class Country {
    private Long id;
    private String country;

    private Set<City> citySet;

    public Country(String country) {
        this.country = country;
    }

    public Country(String country, Set<City> citySet) {
        this.country = country;
        this.citySet = citySet;
    }

    public Country() {
    }
}
