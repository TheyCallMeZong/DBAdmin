package com.database.dbadmin.controllers;

import com.database.dbadmin.dao.TripDao;
import com.database.dbadmin.models.City;
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

    @FXML
    private ComboBox<String> city1;

    @FXML
    private ComboBox<String> hotel1;

    private ObservableList<String> countriesObservableList;

    private ObservableList<String> citiesObservableList;

    private ObservableList<String> hotelsObservableList;

    private TripDao tripDao;

    private Country country;

    @FXML
    public void initialize(){
        tripDao = new TripDao();
        Set<Country> countryList = tripDao.getCountries();
        countriesObservableList = FXCollections.observableArrayList();
        for (Country country : countryList){
            countriesObservableList.add(country.getCountry());
        }

        countries.setItems(countriesObservableList);
        countries.setOnAction(x -> setCity1());
    }

    public void tripButton() {

    }

    private void setCity1(){
        Set<City> cities = tripDao.getCities(countries.getValue());
        citiesObservableList = FXCollections.observableArrayList();
        for (City city : cities){
            citiesObservableList.add(city.getName());
        }

        city1.setItems(citiesObservableList);
        city1.setOnAction(x -> setHotel1());
    }

    private void setHotel1(){
        Set<Hotel> hotels = tripDao.getHotels(city1.getValue());
        hotelsObservableList = FXCollections.observableArrayList();
        for (Hotel hotel : hotels){
            hotelsObservableList.add(hotel.getName());
        }

        hotel1.setItems(hotelsObservableList);
    }
}
