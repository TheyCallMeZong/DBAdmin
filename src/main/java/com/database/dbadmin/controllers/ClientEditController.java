package com.database.dbadmin.controllers;

import com.database.dbadmin.dao.ClientDao;
import com.database.dbadmin.models.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.util.Date;

public class ClientEditController {
    @FXML
    private DatePicker birth;

    @FXML
    private DatePicker dateOfIssue;

    @FXML
    private TextField fullName;

    @FXML
    private TextField issued;

    @FXML
    private TextField seriesAndNumberOfPassport;

    @FXML
    private Label text;

    private ClientDao clientDao;

    private Client client;

    @FXML
    public void initialize(){
        clientDao = new ClientDao();
        client = ClientsInRouteController.client;
        fullName.setText(client.getName() + " " + client.getSurname()
                + (client.getPatronymic() == null ? "" : " " + client.getPatronymic()));
        seriesAndNumberOfPassport.setText(client.getPassportSeries() + " " + client.getPassportNumber());
        issued.setText(client.getPassportIssued());
        birth.setValue(convertToLocalDateViaInstant(client.getBirth()));
        dateOfIssue.setValue(convertToLocalDateViaInstant(client.getDateOfIssue()));
    }

    public void editClient(ActionEvent actionEvent) {
        if (clientDao.updateClient(fullName.getText(), seriesAndNumberOfPassport.getText(),
                issued.getText(), birth.getValue(), dateOfIssue.getValue(), client.getId())){
            setText("client was updated", Color.GREEN);
        } else {
            setText("incorrect date", Color.RED);
        }
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    private void setText(String text, Color color){
        this.text.setText(text);
    }
}
