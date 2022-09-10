package com.database.dbadmin.models;

import lombok.Data;

@Data
public class Employee {
    private Long id;

    private String login;

    private String password;

    private String name;

    private String email;

    private int age;

    private String phoneNumber;

    private Role role_id;

    public Employee(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Employee(){

    }
}
