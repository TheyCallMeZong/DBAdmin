package com.database.dbadmin.controllers;

import com.database.dbadmin.Main;
import com.database.dbadmin.dao.TripDao;
import com.database.dbadmin.models.Route;
import com.database.dbadmin.models.RoutePoint;
import com.database.dbadmin.models.Trip;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class AvailableTripsController {
    public Label employee;
    public static String route;

    @FXML
    public TextField penalty;

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
        addPenalty();
        table.setItems(routePointObservableList);
        employee.setText("Employee: " + tripDao.getEmployee(routeName.getValue()));
    }

    private void addPenalty() {
        Trip trip = tripDao.getTrip(routeName.getValue());
        penalty.setText(String.valueOf(trip.getPenalty()));
        penalty.textProperty().addListener((observable, oldValue, newValue) -> {
            tripDao.updatePenalty(newValue, trip.getId());
        });
    }

    public void viewClients(ActionEvent actionEvent) {
        Main main = new Main();
        try {
            route = routeName.getValue();
            main.openStage("clientsInRoute.fxml");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
