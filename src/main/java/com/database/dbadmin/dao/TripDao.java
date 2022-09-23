package com.database.dbadmin.dao;

import com.database.dbadmin.database.TripPostgresSql;
import com.database.dbadmin.models.City;
import com.database.dbadmin.models.Country;
import com.database.dbadmin.models.Hotel;
import java.util.Set;

public class TripDao {
    private TripPostgresSql tripPostgresSql;

    public TripDao(){
        tripPostgresSql = TripPostgresSql.getInstance();
    }

    public Set<Country> getCountries(){
        return tripPostgresSql.getCountries();
    }

    public Set<City> getCities(String country){
        return tripPostgresSql.getCities(country);
    }

    public Set<Hotel> getHotels(City city){
        return tripPostgresSql.getHotels(city);
    }
}
