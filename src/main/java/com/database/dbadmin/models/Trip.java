package com.database.dbadmin.models;

import lombok.Data;
import java.util.Date;

@Data
public class Trip {
    private Long id;
    private double penalty;
    private Date arrivalDate;
    private Date departureDate;
    private Employee employee;
    private Group group;
    private Route route;
}