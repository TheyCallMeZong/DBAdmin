package com.database.dbadmin.controllers;

import com.database.dbadmin.dao.TripDao;
import com.database.dbadmin.models.Route;
import com.database.dbadmin.models.RoutePoint;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class AvailableTripsController {
    @FXML
    private TableColumn<RoutePoint, Date> arrivalDate;

    @FXML
    private TableColumn<RoutePoint, String> cities;

    @FXML
    private TableColumn<RoutePoint, String> country;

    @FXML
    private TableColumn<RoutePoint, Date> departureDate;

    @FXML
    private TableColumn<RoutePoint, Byte> hotelClass;

    @FXML
    private TableColumn<RoutePoint, String> hotels;

    @FXML
    private ComboBox<String> routeName;

    @FXML
    private TableView<RoutePoint> table;

    private ObservableList<String> routeObservableList;

    private TripDao tripDao;

    private ObservableList<RoutePoint> routePointObservableList;

    @FXML
    void initialize(){
        tripDao = new TripDao();
        Set<Route> routeSet = tripDao.getRoutes();
        routeObservableList = FXCollections.observableArrayList();
        for (Route route : routeSet){
            routeObservableList.add(route.getRouteName());
        }

        routeName.setItems(routeObservableList);
        routeName.setOnAction(x -> setRoutePoints());
    }

    private void setRoutePoints() {
        List<RoutePoint> routePoints = tripDao.getRoutePoints(routeName.getValue());
        cities.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().getCity().getName()));
        hotels.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().getHotel().getName()));
        arrivalDate.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().getArrivalDate()));
        departureDate.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().getDepartureDate()));
        hotelClass.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().getHotel().getHotelClass()));
        country.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().getCountry().getCountry()));
        routePointObservableList = FXCollections.observableArrayList(routePoints.stream().toList());
        table.setItems(routePointObservableList);
    }
}
