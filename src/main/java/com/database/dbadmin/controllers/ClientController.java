package com.database.dbadmin.controllers;

import com.database.dbadmin.Main;
import com.database.dbadmin.dao.ClientDao;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import java.io.IOException;

public class ClientController {
    private ClientDao clientDao;

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
    private Text err;

    private Main main;

    public ClientController(){
        clientDao = new ClientDao();
    }

    @FXML
    public void registrationClient() {
        if(!clientDao.add(fullName.getText(), issued.getText(), seriesAndNumberOfPassport.getText(),
                birth.getValue(), dateOfIssue.getValue())){
            err.setText("Incorrect data");
            return;
        }
        main = new Main();
        try {
            main.openStage("mainTrip.fxml", 561, 400);
        } catch (IOException e) {
            System.out.println("error in registrationClient");
        }
    }
}
