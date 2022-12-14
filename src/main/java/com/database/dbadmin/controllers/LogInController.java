package com.database.dbadmin.controllers;

import com.database.dbadmin.Main;
import com.database.dbadmin.dao.EmployeeDao;
import com.database.dbadmin.models.Employee;
import com.database.dbadmin.models.Role;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class LogInController {
    @FXML
    private TextField userLogin;

    @FXML
    private PasswordField password;

    @FXML
    private Label wrongData;

    @FXML
    private Button input;

    public void logIn() {
        EmployeeDao employeeDao = new EmployeeDao();
        Employee employee;
        if (userLogin.getText().isEmpty() || password.getText().isEmpty()){
            wrongData.setText("Incorrect data");
            wrongData.setTextFill(Color.RED);
        } else if ((employee = employeeDao.getEmployee(userLogin.getText(), password.getText())) != null){
            Main main = new Main();
            if (employee.getRole_id() == Role.ADMIN) {
                main.changeScene("main.fxml");
            } else {
                main.changeScene("employeeWorkSpace.fxml");
            }
        } else {
            wrongData.setText("User not found. Check login and password");
            wrongData.setTextFill(Color.RED);
        }
    }
}