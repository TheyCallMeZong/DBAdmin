package com.database.dbadmin.models;

import lombok.Data;

@Data
public class Employee {
    private Long employee_id;

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

    public Employee(String login, String password, String name, String email, int age, String phoneNumber) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.email = email;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    public Employee(){

    }

    public void setRole_id(String role){
        this.role_id = Role.getRole(role);
    }

    public void setRole_id(Role role){
        this.role_id = role;
    }

    @Override
    public String toString(){
        return getName();
    }
}
