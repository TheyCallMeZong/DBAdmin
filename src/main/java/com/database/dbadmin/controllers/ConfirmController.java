package com.database.dbadmin.controllers;

import com.database.dbadmin.dao.ClientDao;
import com.database.dbadmin.dao.TripDao;
import com.database.dbadmin.models.Client;
import com.database.dbadmin.models.RoutePoint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

public class ConfirmController {
    @FXML
    private DatePicker birth;

    @FXML
    private DatePicker dateOfIssue;

    @FXML
    private TextField fullName;

    @FXML
    private TextField issuedBy;

    @FXML
    private ListView<RoutePoint> listView;

    @FXML
    private Text route;

    @FXML
    private TextField seriesAndNumberOfPassport;

    private TripDao tripDao;

    private ObservableList<RoutePoint> routeObservableList;

    @FXML
    void initialize(){
        tripDao = new TripDao();
        Set<RoutePoint> routePointSet = tripDao.getRoutePoints(MainTripController.routeName);
        routeObservableList = FXCollections.observableArrayList(routePointSet);
        listView.setItems(routeObservableList);
        route.setText(MainTripController.routeName);
        Client client = ClientDao.client;
        fullName.setText(client.getName() + " " + client.getSurname() + " " + client.getPatronymic());
        seriesAndNumberOfPassport.setText(client.getPassportSeries() + "" + client.getPassportNumber());
        issuedBy.setText(client.getPassportIssued());
        birth.setValue(convertToLocalDateViaInstant(client.getBirth()));
        dateOfIssue.setValue(convertToLocalDateViaInstant(client.getDateOfIssue()));
    }

    public void accept(ActionEvent actionEvent) {

    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }
}
