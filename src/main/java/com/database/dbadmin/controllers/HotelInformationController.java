package com.database.dbadmin.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import com.database.dbadmin.dao.TripDao;
import com.database.dbadmin.models.Hotel;
import com.database.dbadmin.models.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class HotelInformationController {
    private TripDao tripDao;

    @FXML
    private TableColumn<Room, Integer> bedroom;

    @FXML
    private Text hotelName;

    @FXML
    private TableColumn<Room, Double> price;

    @FXML
    private TableView<Room> table;

    private ObservableList<Room> roomObservableList;

    @FXML
    void initialize() {
        tripDao = new TripDao();
        Set<Room> rooms = tripDao.getHotelInformation(RouteInformationController.hotel);
        bedroom.setCellValueFactory(new PropertyValueFactory<>("numberOfBedRooms"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        roomObservableList = FXCollections.observableArrayList(rooms);
        table.setItems(roomObservableList);
        hotelName.setText(RouteInformationController.hotel.getName());
    }
}
