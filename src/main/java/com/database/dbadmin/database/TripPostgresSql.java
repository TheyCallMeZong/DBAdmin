package com.database.dbadmin.database;

import com.database.dbadmin.dao.GroupDao;
import com.database.dbadmin.models.*;
import lombok.Data;

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

    public Set<Country> getCountries(Date date){
        String query = "SELECT country FROM countries WHERE country_id = " +
                "(SELECT country_id FROM route_points WHERE arrival_date = ?::date)";
        Set<Country> countriesSet = new HashSet<>();
        try(PreparedStatement preparedStatement = connect.connection.prepareStatement(query)){
            preparedStatement.setDate(1, date);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                countriesSet.add(new Country(resultSet.getString("country")));
            }
            return countriesSet;
        } catch (SQLException e) {
            System.out.println("err in getCountries");
        }
        return null;
    }

    public Set<City> getCities(String country){
        String query = "SELECT * FROM route_points WHERE arrival_date=?::date " +
                "AND country_id = (SELECT country_id FROM countries WHERE country=?)";
        Set<City> cities = new HashSet<>();
        try (PreparedStatement preparedStatement = connect.connection.prepareStatement(query)){
            preparedStatement.setString(1, country);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                cities.add(new City(resultSet.getString("city_name")));
            }
            return cities;
        } catch (SQLException e) {
            System.out.println("err in getCities");
        }

        return null;
    }

    public Set<Hotel> getHotels(String city){
        String query = "SELECT hotel_name, hotel_class FROM hotels h " +
                "INNER JOIN cities c ON c.city_name=? AND c.city_id=h.city_id AND c.country_id = h.country_id";
        Set<Hotel> hotels = new HashSet<>();
        try (PreparedStatement preparedStatement = connect.connection.prepareStatement(query)){
            preparedStatement.setString(1, city);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                hotels.add(new Hotel(resultSet.getString("hotel_name"),
                        resultSet.getByte("hotel_class")));
            }
            return hotels;
        } catch (SQLException e) {
            System.out.println("err in getHotels");
        }

        return null;
    }

    /*public Set<Date> getRoutePoints(){
        String query = "SELECT * FROM route_points WHERE route_points_id IN (SELECT MIN(route_points_id) " +
                "FROM route_points GROUP BY country_id)";
        Set<Date> routePoints = new HashSet<>();
        try(PreparedStatement statement = connect.connection.prepareStatement(query)){
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                routePoints.add(resultSet.getDate("arrival_date"));
            }
            return routePoints;
        } catch (SQLException ex){
            System.out.println(ex);
        }
        return null;
    }*/

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
        String query = "SELECT d.arrival_date, d.departure_date, hotel_name, city_name, country, hotel_class, h.hotel_id, h.city_id FROM route " +
                "INNER JOIN route_points rp on route.route_id = rp.route_id " +
                "INNER JOIN hotels h on h.hotel_id = rp.hotel_id and h.city_id = rp.city_id and h.country_id = rp.country_id " +
                "INNER JOIN cities c on c.city_id = h.city_id and c.country_id = h.country_id " +
                "INNER JOIN countries c2 on c.country_id = c2.country_id " +
                "INNER JOIN date d on rp.date_id = d.date_id " +
                "WHERE route_name=? ORDER BY d.arrival_date";
        List<RoutePoint> routePoints = new ArrayList<>();
        try(PreparedStatement preparedStatement = connect.connection.prepareStatement(query)){
            preparedStatement.setString(1, routeName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Hotel hotel = new Hotel(resultSet.getLong("hotel_id"), resultSet.getString("hotel_name"),
                        resultSet.getByte("hotel_class"));
                City city = new City(resultSet.getLong("city_id"), resultSet.getString("city_name"));
                Country country = new Country(resultSet.getString("country"));
                routePoints.add(new RoutePoint(resultSet.getDate("arrival_date"),
                        resultSet.getDate("departure_date"), hotel, city, country));
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
        String query = "SELECT arrival_date FROM route_points INNER JOIN route r on r.route_name=?" +
                " INNER JOIN date d on d.date_id = route_points.date_id\n" +
                "    order by d.arrival_date limit 1";
        Date date = null;
        try (PreparedStatement pr = connect.connection.prepareStatement(query)) {
            pr.setString(1, routeName);
            ResultSet resultSet = pr.executeQuery();
            while (resultSet.next()){
                date = resultSet.getDate("arrival_date");
            }
            return date;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }

    private Date getDateDepartureByRouteName(String routeName){
        String query = "SELECT departure_date FROM route_points INNER JOIN route r on " +
                "r.route_name=? INNER JOIN date d on d.date_id = route_points.date_id " +
                "order by d.arrival_date DESC limit 1";
        Date date = null;
        try (PreparedStatement pr = connect.connection.prepareStatement(query)) {
            pr.setString(1, routeName);
            ResultSet resultSet = pr.executeQuery();
            while (resultSet.next()){
                date = resultSet.getDate("departure_date");
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
}
