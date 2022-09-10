package com.database.dbadmin.controllers;

import com.database.dbadmin.database.EmployeePostgresSql;
import com.database.dbadmin.models.Employee;
import com.database.dbadmin.models.Role;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.security.NoSuchAlgorithmException;

public class EmployeeController {
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
    public Button btn;

    public void addEmployeeToDb() throws NoSuchAlgorithmException {
        EmployeePostgresSql employeePostgresSql = EmployeePostgresSql.getInstance();
        Employee employee = new Employee();
        employee.setName(employeeName.getText());
        employee.setEmail(employeeEmail.getText());
        employee.setPassword(employeePassword.getText());
        employee.setLogin(employeeLogin.getText());
        employee.setAge(Integer.parseInt(employeeAge.getText()));
        employee.setPhoneNumber(employeePhoneNumber.getText());
        employee.setRole_id(Role.USER);
        employeePostgresSql.writeToDb(employee);
    }
}
