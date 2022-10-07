package com.database.dbadmin.models;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class RoutePoint {
    private Long id;
    private Date arrivalDate;
    private Date departureDate;
    private Hotel hotel;
    private City city;
    private Country country;

    private Set<Sight> sightSet;

    public RoutePoint(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public RoutePoint(Date arrivalDate, Date departureDate, Hotel hotel, City city, Country country) {
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.hotel = hotel;
        this.city = city;
        this.country = country;
    }

    @Override
    public String toString(){
        return "city name - '" + getCity().getName() + "' hotel name '" + getHotel().getName() + "' hotel class '" + getHotel().getHotelClass() + "*' arrival date -'" + getArrivalDate().toString() +
                "' departure date - '" + getDepartureDate().toString() + "'";
    }
}
