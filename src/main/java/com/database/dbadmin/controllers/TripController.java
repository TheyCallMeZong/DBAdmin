package com.database.dbadmin.controllers;

import com.database.dbadmin.dao.TripDao;
import com.database.dbadmin.models.City;
import com.database.dbadmin.models.Country;
import com.database.dbadmin.models.Hotel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Deprecated
public class TripController {
    @FXML
    public DatePicker dateArrival;

    @FXML
    public DatePicker dateDeparture;

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
        dateArrival.setOnAction(x -> setCountries());
        //Callback<DatePicker, DateCell> dayCellFactory= this.getDayCellFactory();
        //dateArrival.setDayCellFactory(dayCellFactory);
    }

    public void tripButton() {

    }

    private void setCountries(){
        tripDao = new TripDao();
        Date date = Date.valueOf(dateArrival.getValue());
        Set<Country> countryList = tripDao.getCountries(date);
        countriesObservableList = FXCollections.observableArrayList();
        for (Country country : countryList){
            countriesObservableList.add(country.getCountry());
        }

        countries.setItems(countriesObservableList);
        countries.setOnAction(x -> setCity1());
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
            hotelsObservableList.add(hotel.getName() + " | class - " + hotel.getHotelClass() + "*");
        }

        hotel1.setItems(hotelsObservableList);
    }

    /*private Callback<DatePicker, DateCell> getDayCellFactory() {
        tripDao = new TripDao();
        Set<Date> dates = tripDao.getRoutePoints();
        List<LocalDate> localDates = new ArrayList<>();
        for (Date d : dates){
            localDates.add(d.toLocalDate());
        }
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        for (LocalDate date : localDates) {
                            if (item.getYear() == date.getYear()
                                && item.getMonth() == date.getMonth()
                                && item.getDayOfMonth() == date.getDayOfMonth()) {
                                setStyle("-fx-background-color: #00ff00;");
                            }
                        }
                    }
                };
            }
        };
        return dayCellFactory;
    }*/
}
