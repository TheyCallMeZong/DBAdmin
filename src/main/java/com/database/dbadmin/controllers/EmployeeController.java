package com.database.dbadmin.controllers;

import com.database.dbadmin.dao.EmployeeDao;
import com.database.dbadmin.models.Employee;
import com.database.dbadmin.models.Role;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class EmployeeController {
    @FXML
    public TextField name;
    @FXML
    public TextField email;
    @FXML
    public TextField phoneNumber;
    @FXML
    public TextField age;
    @FXML
    public TextField login;
    @FXML
    public PasswordField password;
    @FXML
    public TextField employeeName;

    @FXML
    public TextField employeeEmail;

    @FXML
    public TextField employeePhoneNumber;

    @FXML
    public TextField employeeAge;

    @FXML
    public TextField employeeLogin;

    @FXML
    public PasswordField employeePassword;

    @FXML
    private Label wrongData;

    private EmployeeDao employeeDao;

    @FXML
    void initialize() {
        employeeDao = new EmployeeDao();
    }

    public void addEmployeeToDb() {
        try {
            Employee employee = new Employee(employeeLogin.getText().trim(), employeePassword.getText().trim(),
                    employeeName.getText(), employeeEmail.getText(),
                    Integer.parseInt(employeeAge.getText().trim()), employeePhoneNumber.getText());
            employee.setRole_id(Role.USER);
            if (employeeDao.addEmployee(employee)){
                wrongData.setText("Good");
                wrongData.setTextFill(Color.GREEN);
            } else {
                incorrectData();
            }
        } catch (NumberFormatException exception){
            incorrectData();
        }
    }

    private void incorrectData(){
        wrongData.setText("Incorrect data");
        wrongData.setTextFill(Color.RED);
    }
}
