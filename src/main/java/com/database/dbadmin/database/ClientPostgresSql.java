package com.database.dbadmin.database;

import com.database.dbadmin.dao.TripDao;
import com.database.dbadmin.models.Client;
import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public List<Client> getAllClientsFromRoute(String route) {
        String query = "SELECT name, surname, passport_series, passport_number, c.client_id, patronymic, birth, passport_issued, date_issue" +
                " FROM trip INNER JOIN client_group cg on trip.group_id = cg.group_id INNER JOIN \"group\" g on g.group_id = cg.group_id INNER JOIN clients c on c.client_id = cg.client_id\n" +
                "    INNER JOIN route r on r.route_id = trip.route_id WHERE r.route_name=?";
        List<Client> clients = new ArrayList<>();
        try(PreparedStatement preparedStatement = clientPostgresSql.connection.prepareStatement(query)) {
            preparedStatement.setString(1, route);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Client client = new Client();
                client.setId(resultSet.getLong("client_id"));
                client.setName(resultSet.getString("name"));
                client.setSurname(resultSet.getString("surname"));
                client.setPassportSeries(resultSet.getString("passport_series"));
                client.setPassportNumber(resultSet.getString("passport_number"));
                client.setPatronymic(resultSet.getString("patronymic"));
                client.setBirth(resultSet.getDate("birth"));
                client.setDateOfIssue(resultSet.getDate("date_issue"));
                client.setPassportIssued(resultSet.getString("passport_issued"));
                clients.add(client);
            }
            return clients;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void deleteClient(Long id) {
        Long groupId = getGroup(id);
        deleteFromClientGroup(id);
        deleteFromClients(id);
        check(groupId);
    }

    private void deleteFromClientGroup(Long id){
        String query = "DELETE FROM client_group where client_id=?";
        try(PreparedStatement preparedStatement = clientPostgresSql.connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Long getGroup(Long id) {
        String queryToGetGroupId = "SELECT group_id FROM client_group WHERE client_id=?";
        Long groupId = -1L;
        try (PreparedStatement preparedStatement = clientPostgresSql.connection.prepareStatement(queryToGetGroupId)){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                groupId = resultSet.getLong("group_id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return groupId;
    }

    private void deleteFromClients(Long id){
        String queryToDeleteFromClients = "DELETE FROM clients where client_id=?";
        try (PreparedStatement preparedStatement = clientPostgresSql.connection.prepareStatement(queryToDeleteFromClients)){
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void check(Long groupId){
        String check = "SELECT * FROM client_group WHERE group_id=?";
        try (PreparedStatement preparedStatement = clientPostgresSql.connection.prepareStatement(check)) {
            preparedStatement.setLong(1, groupId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                TripDao tripDao = new TripDao();
                tripDao.deleteTrip(groupId);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateClient(Client client) {
        String query = "UPDATE clients SET name=?, surname=?, patronymic=?, passport_number=?, passport_series=?, passport_issued=?,\n" +
                "                   date_issue=?, birth=? WHERE client_id=?";

        try (PreparedStatement preparedStatement = clientPostgresSql.connection.prepareStatement(query)){
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getSurname());
            preparedStatement.setString(3, client.getPatronymic());
            preparedStatement.setString(4, client.getPassportNumber());
            preparedStatement.setString(5, client.getPassportSeries());
            preparedStatement.setString(6, client.getPassportIssued());
            preparedStatement.setDate(7, (Date) client.getDateOfIssue());
            preparedStatement.setDate(8, (Date) client.getBirth());
            preparedStatement.setLong(9, client.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
