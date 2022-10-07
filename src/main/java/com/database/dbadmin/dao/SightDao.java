package com.database.dbadmin.dao;

import com.database.dbadmin.database.SightPostgresSql;
import com.database.dbadmin.models.City;
import com.database.dbadmin.models.Sight;

import java.util.Set;

public class SightDao {
    private SightPostgresSql sightPostgresSql;

    public SightDao(){
        sightPostgresSql = SightPostgresSql.getInstance();
    }

    public Set<Sight> getAllSight(City city){
        return sightPostgresSql.getAllSightFromCityById(city.getId());
    }
}
