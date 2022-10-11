package com.database.dbadmin.database;

import com.database.dbadmin.dao.GroupDao;
import com.database.dbadmin.models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TripPostgresSql {
    private PostgresSqlConnect connect;

    private TripPostgresSql(){
        connect = PostgresSqlConnect.getInstance();
    }

    private static TripPostgresSql instance;

    public static TripPostgresSql getInstance(){
        return instance == null ? new TripPostgresSql() : instance;
    }

    private GroupDao groupDao;

    public Set<Route> getRoutes() {
        String query = "SELECT route_name FROM route";
        Set<Route> routeSet = new HashSet<>();
        try (PreparedStatement preparedStatement = connect.connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                routeSet.add(new Route(resultSet.getString("route_name")));
            }
            return routeSet;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public List<RoutePoint> getRoutePoints(String routeName){
        String query = "SELECT date_arrival, date_departure, hotel_name, city_name, country, hotel_class, h.hotel_id, h.city_id FROM route " +
                "INNER JOIN route_points rp on route.route_id = rp.route_id " +
                "INNER JOIN hotels h on h.hotel_id = rp.hotel_id and h.city_id = rp.city_id and h.country_id = rp.country_id " +
                "INNER JOIN cities c on c.city_id = h.city_id and c.country_id = h.country_id " +
                "INNER JOIN countries c2 on c.country_id = c2.country_id " +
                "WHERE route_name=? ORDER BY date_arrival";
        List<RoutePoint> routePoints = new ArrayList<>();
        try(PreparedStatement preparedStatement = connect.connection.prepareStatement(query)){
            preparedStatement.setString(1, routeName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Hotel hotel = new Hotel(resultSet.getLong("hotel_id"), resultSet.getString("hotel_name"),
                        resultSet.getByte("hotel_class"));
                City city = new City(resultSet.getLong("city_id"), resultSet.getString("city_name"));
                Country country = new Country(resultSet.getString("country"));
                routePoints.add(new RoutePoint(resultSet.getDate("date_arrival"),
                        resultSet.getDate("date_departure"), hotel, city, country));
            }
            return routePoints;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean addClientToGroup(String routeName, Long employeeId){
        groupDao = new GroupDao();
        Group group = groupDao.getGroupByRouteName(routeName);
        if (group != null) {
            String query = "INSERT INTO client_group (client_id, group_id) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connect.connection.prepareStatement(query)) {
                preparedStatement.setLong(1, ClientPostgresSql.client_id);
                preparedStatement.setLong(2, group.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            Integer groupId = groupDao.createGroup();
            String query = "INSERT INTO client_group (client_id, group_id) VALUES (?, ?)";
             try(PreparedStatement preparedStatement = connect.connection.prepareStatement(query)) {
                 preparedStatement.setLong(1, ClientPostgresSql.client_id);
                 preparedStatement.setInt(2, groupId);
                 preparedStatement.executeUpdate();
                 createTrip(groupId, routeName, employeeId);
          } catch (SQLException e) {
              System.err.println(e.getMessage());
          }
        }
        return false;
    }

    private void createTrip(int groupId, String routeName, Long employeeId){
        String query = "INSERT INTO trip(employee_id, group_id, penalty, departure_date, arrival_date, route_id, sum) " +
                "VALUES (?, ?, ?, ?::date, ?::date, ?, ?);";
        try(PreparedStatement preparedStatement = connect.connection.prepareStatement(query)){
            Date arrivalDate = getDateArrivalByRouteName(routeName);
            Date departureDate = getDateDepartureByRouteName(routeName);
            preparedStatement.setLong(1, employeeId);
            preparedStatement.setLong(2, groupId);
            preparedStatement.setDouble(3, 0);
            preparedStatement.setDate(4, departureDate);
            preparedStatement.setDate(5, arrivalDate);
            preparedStatement.setLong(6, getRouteId(routeName));
            preparedStatement.setDouble(7, 0);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("err in createTrip");
        }
    }

    private Date getDateArrivalByRouteName(String routeName){
        String query = "SELECT date_arrival FROM route_points INNER JOIN route r on r.route_name=? " +
                "order by date_arrival limit 1";
        Date date = null;
        try (PreparedStatement pr = connect.connection.prepareStatement(query)) {
            pr.setString(1, routeName);
            ResultSet resultSet = pr.executeQuery();
            while (resultSet.next()){
                date = resultSet.getDate("date_arrival");
            }
            return date;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }

    private Date getDateDepartureByRouteName(String routeName){
        String query = "SELECT date_departure FROM route_points INNER JOIN route r on " +
                "r.route_name=? " +
                "order by date_departure DESC limit 1";
        Date date = null;
        try (PreparedStatement pr = connect.connection.prepareStatement(query)) {
            pr.setString(1, routeName);
            ResultSet resultSet = pr.executeQuery();
            while (resultSet.next()){
                date = resultSet.getDate("date_departure");
            }
            return date;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }

    private Long getRouteId(String routeName){
        String query = "SELECT route_id FROM route WHERE route_name=?";
        Long id = -1L;
        try(PreparedStatement preparedStatement = connect.connection.prepareStatement(query)) {
            preparedStatement.setString(1, routeName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                id = resultSet.getLong("route_id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public String getEmployee(String value) {
        String query = "SELECT name FROM trip INNER JOIN employee e on e.employee_id = trip.employee_id INNER JOIN route r on r.route_id = trip.route_id AND r.route_name=?";
        String name = null;
        try(PreparedStatement preparedStatement = connect.connection.prepareStatement(query)) {
            preparedStatement.setString(1, value);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                name = resultSet.getString("name");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return name;
    }

    public void deleteTrip(Long groupId) {
        String query = "DELETE FROM trip where group_id=?";
        try (PreparedStatement preparedStatement = connect.connection.prepareStatement(query)) {
            preparedStatement.setLong(1, groupId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
