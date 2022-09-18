package com.database.dbadmin.controllers;

import com.database.dbadmin.dao.EmployeeDao;
import com.database.dbadmin.models.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class EmployeeEditController {
    @FXML
    public TextField employeeName;
    @FXML
    public TextField employeeEmail;
    @FXML
    public TextField employeePhoneNumber;
    @FXML
    public TextField age;
    @FXML
    public TextField login;
    @FXML
    public PasswordField password;
    @FXML
    public Label wrongData;

    private EmployeeDao employeeDao;

    @FXML
    void initialize(){
        employeeDao = new EmployeeDao();
        Employee employee = EmployeeShowController.employee;
        employeeName.setText(employee.getName());
        employeeEmail.setText(employee.getEmail());
        login.setText(employee.getLogin());
        age.setText(String.valueOf(employee.getAge()));
        employeePhoneNumber.setText(employee.getPhoneNumber());
    }

    public void edit(){
        try {
            String password;
            boolean flag = false;
            if (this.password.getText().isEmpty()){
                flag = true;
                password = EmployeeShowController.employee.getPassword();
            } else {
                password = this.password.getText().trim();
            }
            Employee employee = new Employee(login.getText().trim(), password.trim(),
                    employeeName.getText(), employeeEmail.getText(),
                    Integer.parseInt(age.getText().trim()), employeePhoneNumber.getText());
            employee.setId(EmployeeShowController.employee.getId());
            if (!employeeDao.updateEmployee(employee, flag)){
                showWrongText();
            } else {
                wrongData.setText("Good");
                wrongData.setTextFill(Color.GREEN);
            }
        } catch (NumberFormatException ex){
            showWrongText();
        }
    }

    private void showWrongText(){
        wrongData.setText("Incorrect data");
        wrongData.setTextFill(Color.RED);
    }
}
