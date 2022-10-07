package com.database.dbadmin.controllers;

import com.database.dbadmin.dao.SightDao;
import com.database.dbadmin.models.Sight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Set;

public class SightController {
    @FXML
    public TableView<Sight> table;

    @FXML
    public TableColumn<Sight, String> sightsColumn;

    private SightDao sightDao;

    @FXML
    void initialize(){
        sightDao = new SightDao();
        sightsColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        Set<Sight> sightSet = sightDao.getAllSight(RouteInformationController.cityName);
        ObservableList<Sight> strings = FXCollections.observableArrayList(sightSet);
        table.setItems(strings);
    }
}
