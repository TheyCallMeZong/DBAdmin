package com.database.dbadmin.controllers;

import com.database.dbadmin.Main;
import com.database.dbadmin.database.EmployeePostgresSql;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class LogInController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField userLogin;

    @FXML
    private PasswordField password;

    @FXML
    private Label wrongData;

    @FXML
    private Button input;

    public void logIn(ActionEvent actionEvent) {
        EmployeePostgresSql employeePostgresSql = EmployeePostgresSql.getInstance();
        if (userLogin.getText().isEmpty() || password.getText().isEmpty()){
            wrongData.setText("Incorrect data");
            wrongData.setTextFill(Color.RED);
        } else if (employeePostgresSql.getEmployee(userLogin.getText(), password.getText()) != null){
            Main main = new Main();
            main.changeScene("main.fxml");
            employeePostgresSql.readEmployeeFromDb();
        } else {
            wrongData.setText("User not found. Check login and password");
            wrongData.setTextFill(Color.RED);
        }
    }
}