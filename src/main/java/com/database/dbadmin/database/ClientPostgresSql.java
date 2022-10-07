package com.database.dbadmin.database;

import com.database.dbadmin.models.Client;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ClientPostgresSql {
    private final PostgresSqlConnect clientPostgresSql;

    private ClientPostgresSql() {
        clientPostgresSql = PostgresSqlConnect.getInstance();
    }

    private static ClientPostgresSql instance;

    public static ClientPostgresSql getInstance(){
        return instance == null ? new ClientPostgresSql() : instance;
    }

    public static Long client_id;

    public boolean addClient(Client client){
        String query = "INSERT INTO clients(name, surname, patronymic, birth, passport_series, passport_number, passport_issued, date_issue, photo) " +
                "VALUES(?, ?, ?, ?::date, ?, ?, ?, ?::date, ?) returning client_id";

        try (PreparedStatement preparedStatement = clientPostgresSql.connection.prepareStatement(query)) {
            Field[] fields = client.getClass().getDeclaredFields();
            for (int i = 1; i < fields.length; i++){
                Field field;
                field = client.getClass().getDeclaredField(fields[i].getName());
                field.setAccessible(true);
                Object result = field.get(client);
                preparedStatement.setString(i, result == null ? null : result.toString());
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                client_id = resultSet.getLong("client_id");
            }
            System.out.println("Successfully created");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
