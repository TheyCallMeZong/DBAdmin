package com.database.dbadmin.dao;

import com.database.dbadmin.database.GroupPostgresSql;
import com.database.dbadmin.models.Group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDao {
    private GroupPostgresSql groupPostgresSql;

    public GroupDao(){
        groupPostgresSql = GroupPostgresSql.getInstance();
    }

    public Integer createGroup(){
        return groupPostgresSql.createGroup();
    }

    public Group getGroupByRouteName(String routeName){
        return groupPostgresSql.getGroupByRouteName(routeName);
    }
}
