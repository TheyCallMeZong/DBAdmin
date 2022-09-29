package com.database.dbadmin.models;

public enum Role {
    ADMIN(1),
    USER(2),
    TRAVEL_AGENT(3);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static Role getRole(String role){
        if (role.equals("ADMIN")){
            return ADMIN;
        } else if (role.equals("USER")) {
            return USER;
        } else if (role.equals("TRAVEL_AGENT")) {
            return TRAVEL_AGENT;
        }
        return null;
    }
}
