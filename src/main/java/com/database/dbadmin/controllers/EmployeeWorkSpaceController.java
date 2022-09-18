package com.database.dbadmin.controllers;

import com.database.dbadmin.Main;
import javafx.event.ActionEvent;

import java.io.IOException;

public class EmployeeWorkSpaceController {
    private Main main;

    public void travelButton() {
        main = new Main();
        try {
            main.openStage("registrationClient.fxml", 441, 400);
        } catch (IOException e) {
            System.out.println("error in travelButton");
        }
    }

    public void registrationClient() {
        main = new Main();
        try {
            main.openStage("trip.fxml", 700, 380);
        } catch (IOException e) {
            System.out.println("error in registrationClient");
        }
    }


    public void tripButton() {
        main = new Main();
        try {
            main.openStage("check.fxml", 350, 415);
        } catch (IOException e) {
            System.out.println("error in tripButton");
        }
    }
}
