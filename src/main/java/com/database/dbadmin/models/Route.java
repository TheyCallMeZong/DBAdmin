package com.database.dbadmin.models;

import lombok.Data;

import java.util.Set;

@Data
public class Route {
    private Long id;
    private String routeName;
    private Set<RoutePoint> routePoints;

    public Route(String routeName) {
        this.routeName = routeName;
    }

    public Route(Set<RoutePoint> routePoints){
        this.routePoints = routePoints;
    }
}
