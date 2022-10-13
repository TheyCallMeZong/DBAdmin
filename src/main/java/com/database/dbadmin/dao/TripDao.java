package com.database.dbadmin.dao;

import com.database.dbadmin.database.TripPostgresSql;
import com.database.dbadmin.models.*;
import javafx.beans.Observable;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

public class TripDao {
    private TripPostgresSql tripPostgresSql;

    public TripDao(){
        tripPostgresSql = TripPostgresSql.getInstance();
    }

    public Set<Route> getRoutes(){
        return tripPostgresSql.getRoutes();
    }

    public List<RoutePoint> getRoutePoints(String routeName){
        return tripPostgresSql.getRoutePoints(routeName);
    }

    public boolean addClientToTrip(String routeName, Long employeeId){
        return tripPostgresSql.addClientToGroup(routeName, employeeId);
    }

    public String getEmployee(String value) {
        return tripPostgresSql.getEmployee(value);
    }

    public void deleteTrip(Long groupId) {
        tripPostgresSql.deleteTrip(groupId);
    }

    public Trip getTrip(String value) {
        return tripPostgresSql.getTrip(value);
    }

    public void updatePenalty(String x, Long id) {
        Trip trip = new Trip();
        trip.setPenalty(Double.parseDouble(x));
        trip.setId(id);
        tripPostgresSql.updatePenalty(trip);
    }
}
