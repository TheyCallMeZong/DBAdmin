package com.database.dbadmin.controllers;

import com.database.dbadmin.Main;
import com.database.dbadmin.dao.TripDao;
import com.database.dbadmin.models.Hotel;
import com.database.dbadmin.models.RoutePoint;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

public class RouteInformationController {
    public static Hotel hotel;

    @FXML
    public TableView<RoutePoint> table;

    @FXML
    public TableColumn<RoutePoint, String> city1;

    @FXML
    public TableColumn<RoutePoint, Date> dateArrival;

    @FXML
    public TableColumn<RoutePoint, Date> departureDate;

    @FXML
    public TableColumn<RoutePoint, String> hotel1;

    @FXML
    public TableColumn<RoutePoint, Byte> hotelClass;

    @FXML
    public TableColumn<RoutePoint, String> country;

    private TripDao tripDao;

    private ObservableList<RoutePoint> observableList;

    @FXML
    void initialize() {
        tripDao = new TripDao();
        Set<RoutePoint> routePoints = tripDao.getRoutePoints(MainTripController.routeName);
        city1.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().getCity().getName()));
        hotel1.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().getHotel().getName()));
        dateArrival.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().getArrivalDate()));
        departureDate.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().getDepartureDate()));
        hotelClass.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().getHotel().getHotelClass()));
        country.setCellValueFactory(x -> new SimpleObjectProperty<>(x.getValue().getCountry().getCountry()));
        observableList = FXCollections.observableArrayList(routePoints.stream().toList());
        table.setItems(observableList);
    }
}
