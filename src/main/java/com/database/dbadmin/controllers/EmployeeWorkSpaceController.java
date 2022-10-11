package com.database.dbadmin.controllers;

import com.database.dbadmin.Main;
import com.database.dbadmin.dao.TripDao;
import com.database.dbadmin.models.Route;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Set;

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

    public void logout(ActionEvent actionEvent) {
        main = new Main();
        main.changeScene("authorization.fxml");
    }

    public void openAvailableTrips(ActionEvent actionEvent) {
        main =  new Main();
        try {
            main.openStage("availableTrips.fxml");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
