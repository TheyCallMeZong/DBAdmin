package com.database.dbadmin.database;

import com.database.dbadmin.models.*;

import java.sql.*;
import java.util.HashSet;
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

    public Set<RoutePoint> getRoutePoints(String routeName){
        String query = "SELECT arrival_date, departure_date, hotel_name, city_name, country, hotel_class, h.hotel_id FROM route " +
                "INNER JOIN route_points rp on route.route_id = rp.route_id " +
                "INNER JOIN hotels h on h.hotel_id = rp.hotel_id and h.city_id = rp.city_id and h.country_id = rp.country_id " +
                "INNER JOIN cities c on c.city_id = h.city_id and c.country_id = h.country_id " +
                "INNER JOIN countries c2 on c.country_id = c2.country_id " +
                "WHERE route_name=? ORDER BY arrival_date";
        Set<RoutePoint> routePoints = new HashSet<>();
        try(PreparedStatement preparedStatement = connect.connection.prepareStatement(query)){
            preparedStatement.setString(1, routeName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Hotel hotel = new Hotel(resultSet.getLong("hotel_id"), resultSet.getString("hotel_name"),
                        resultSet.getByte("hotel_class"));
                City city = new City(resultSet.getString("city_name"));
                Country country = new Country(resultSet.getString("country"));
                routePoints.add(new RoutePoint(resultSet.getDate("arrival_date"), resultSet.getDate("departure_date"),
                                hotel, city, country));
            }
            return routePoints;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean createTrip(){

        return false;
    }
}
