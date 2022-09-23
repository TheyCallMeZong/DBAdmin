package com.database.dbadmin.database;

import com.database.dbadmin.models.City;
import com.database.dbadmin.models.Country;
import com.database.dbadmin.models.Hotel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public Set<Country> getCountries(){
        String query = "SELECT country FROM countries";
        Set<Country> countriesSet = new HashSet<>();
        try(PreparedStatement preparedStatement = connect.connection.prepareStatement(query)){
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
        String query = "SELECT city_name FROM cities LEFT JOIN countries c on c.country=?";
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

    public Set<Hotel> getHotels(City city){
        String query = "SELECT hotel_name, hotel_class FROM hotels " +
                "LEFT JOIN cities c ON city_name=? AND c.country_id=?";
        Set<Hotel> hotels = new HashSet<>();
        try (PreparedStatement preparedStatement = connect.connection.prepareStatement(query)){
            preparedStatement.setString(1, city.getName());
            preparedStatement.setLong(2, city.getCountry().getId());
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
}
