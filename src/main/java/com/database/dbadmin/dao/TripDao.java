package com.database.dbadmin.dao;

import com.database.dbadmin.database.TripPostgresSql;
import com.database.dbadmin.models.*;

import java.util.Set;
import java.sql.Date;

public class TripDao {
    private TripPostgresSql tripPostgresSql;

    public TripDao(){
        tripPostgresSql = TripPostgresSql.getInstance();
    }

    public Set<Country> getCountries(Date date){
        return tripPostgresSql.getCountries(date);
    }

    public Set<City> getCities(String country){
        return tripPostgresSql.getCities(country);
    }

    public Set<Hotel> getHotels(String city){
        return tripPostgresSql.getHotels(city);
    }

   /* public Set<Date> getRoutePoints(){
        return tripPostgresSql.getRoutePoints();
    }*/

    public Set<Route> getRoutes(){
        return tripPostgresSql.getRoutes();
    }

    public Set<RoutePoint> getRoutePoints(String routeName){
        return tripPostgresSql.getRoutePoints(routeName);
    }

    public Set<Room> getHotelInformation(Hotel hotel){
        return tripPostgresSql.getHotelInformation(hotel);
    }

    public boolean createTrip(){
        return tripPostgresSql.createTrip();
    }
}
