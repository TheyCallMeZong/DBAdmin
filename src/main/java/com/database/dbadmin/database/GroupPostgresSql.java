package com.database.dbadmin.database;

public class GroupPostgresSql {
    private PostgresSqlConnect postgresSqlConnect;

    private GroupPostgresSql(){
        postgresSqlConnect = PostgresSqlConnect.getInstance();
    }

    private static GroupPostgresSql instance;

    public static GroupPostgresSql getInstance(){
        return instance == null ? new GroupPostgresSql() : instance;
    }
}
