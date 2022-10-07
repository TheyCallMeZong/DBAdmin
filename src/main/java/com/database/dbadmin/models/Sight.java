package com.database.dbadmin.models;

import lombok.Data;

@Data
public class Sight {
    private Long id;
    private String name;

    public Sight(String name) {
        this.name = name;
    }

    public Sight() {

    }
}
