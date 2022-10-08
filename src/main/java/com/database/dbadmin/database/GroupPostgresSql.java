package com.database.dbadmin.database;

import com.database.dbadmin.models.Group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupPostgresSql {
    private PostgresSqlConnect postgresSqlConnect;

    private GroupPostgresSql(){
        postgresSqlConnect = PostgresSqlConnect.getInstance();
    }

    private static GroupPostgresSql instance;

    public static GroupPostgresSql getInstance(){
        return instance == null ? new GroupPostgresSql() : instance;
    }

    public Integer createGroup(){
        String queryGroup = "INSERT INTO \"group\" (group_name) VALUES (?) RETURNING group_id";
        Integer id = -1;
        try(PreparedStatement preparedStatement = postgresSqlConnect.connection.prepareStatement(queryGroup)){
            preparedStatement.setInt(1, getLastGroupName() + 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                id = resultSet.getInt("group_id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public Group getGroupByRouteName(String routeName){
        String query = "SELECT t.group_id FROM \"group\" INNER JOIN trip t ON route_id = " +
                "(SELECT route_id FROM route WHERE route_name=?);";
        Group g = null;
        try(PreparedStatement preparedStatement = postgresSqlConnect.connection.prepareStatement(query)) {
            preparedStatement.setString(1, routeName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                g = new Group(resultSet.getLong("group_id"));
            }
            return g;
        } catch (SQLException e) {
            System.out.println("err in getGroup");
        }
        return g;
    }

    private Integer getLastGroupName(){
        String query = "SELECT group_name FROM \"group\" ORDER BY group_name DESC LIMIT 1;";
        int id = -1;
        try(PreparedStatement preparedStatement = postgresSqlConnect.connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                id = resultSet.getInt("group_name");
            }
        } catch (SQLException e) {
            System.out.println("err in getGroup");
        }
        return id;
    }
}
