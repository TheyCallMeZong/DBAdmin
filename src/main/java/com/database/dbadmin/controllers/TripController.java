package com.database.dbadmin.controllers;

import com.database.dbadmin.dao.TripDao;
import com.database.dbadmin.models.Country;
import com.database.dbadmin.models.Hotel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TripController {

    @FXML
    private ComboBox<String> countries;

    private ObservableList<String> countriesObservableList;

    private ObservableList<String> citiesObservableList;

    private ObservableList<Hotel> hotelsObservableList;

    private TripDao tripDao;

    @FXML
    public void initialize(){
        tripDao = new TripDao();
        Set<Country> countryList = tripDao.getCountries();
        countriesObservableList = FXCollections.observableArrayList();
        for (Country country : countryList){
            countriesObservableList.add(country.getCountry());
        }

        countries.setItems(countriesObservableList);
    }

    public void tripButton() {

    }
}
