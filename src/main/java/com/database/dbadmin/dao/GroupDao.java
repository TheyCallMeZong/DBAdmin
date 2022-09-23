package com.database.dbadmin.dao;

import com.database.dbadmin.database.GroupPostgresSql;

public class GroupDao {
    private GroupPostgresSql groupPostgresSql;

    public GroupDao(){
        groupPostgresSql = GroupPostgresSql.getInstance();
    }


}
