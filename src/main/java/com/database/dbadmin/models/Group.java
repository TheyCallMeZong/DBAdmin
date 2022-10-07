package com.database.dbadmin.models;

import lombok.Data;

import java.util.Set;

@Data
public class Group {
    private Long id;
    private Integer groupName;
    private Set<Client> clients;

    public Group(Integer groupName) {
        this.groupName = groupName;
    }

    public Group(){

    }

    public Group(Long id) {
        this.id = id;
    }

    public Group(Long id, Integer groupName) {
        this.id = id;
        this.groupName = groupName;
    }
}
