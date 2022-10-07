package com.database.dbadmin.database;

import com.database.dbadmin.models.Sight;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SightPostgresSql {
    private final PostgresSqlConnect postgresSqlConnect;

    private SightPostgresSql() {
        postgresSqlConnect = PostgresSqlConnect.getInstance();
    }

    private static SightPostgresSql instance;

    public static SightPostgresSql getInstance(){
        return instance == null ? new SightPostgresSql() : instance;
    }

    public Set<Sight> getAllSightFromCityById(Long id) {
        String query = "SELECT memorial_name FROM memorials INNER JOIN memorial_excursion_programm mep " +
                "ON memorials.memorial_id = mep.memorial_id " +
                "INNER JOIN excursion_program ep ON ep.excursion_program_id = mep.excursion_id WHERE city_id=?";
        Set<Sight> sightSet = new HashSet<>();
        try(PreparedStatement preparedStatement = postgresSqlConnect.connection.prepareStatement(query)){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                sightSet.add(new Sight(resultSet.getString("memorial_name")));
            }
            return sightSet;
        } catch (SQLException e) {
            System.out.println("err in getAllSightFromCityById");
        }
        return null;
    }
}
