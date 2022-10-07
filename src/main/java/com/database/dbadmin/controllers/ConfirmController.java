package com.database.dbadmin.controllers;

import com.database.dbadmin.dao.ClientDao;
import com.database.dbadmin.dao.EmployeeDao;
import com.database.dbadmin.dao.TripDao;
import com.database.dbadmin.models.Client;
import com.database.dbadmin.models.Employee;
import com.database.dbadmin.models.Role;
import com.database.dbadmin.models.RoutePoint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ConfirmController {
    @FXML
    public ComboBox<Employee> employee;
    @FXML
    private DatePicker birth;

    @FXML
    private DatePicker dateOfIssue;

    @FXML
    private TextField fullName;

    @FXML
    private TextField issuedBy;

    @FXML
    private ListView<RoutePoint> listView;

    @FXML
    private Text route;

    @FXML
    private TextField seriesAndNumberOfPassport;

    private TripDao tripDao;
    private ClientDao clientDao;

    private ObservableList<RoutePoint> routeObservableList;

    @FXML
    void initialize(){
        tripDao = new TripDao();
        List<RoutePoint> routePointSet = tripDao.getRoutePoints(MainTripController.routeName);
        routeObservableList = FXCollections.observableArrayList(routePointSet);
        listView.setItems(routeObservableList);
        route.setText(MainTripController.routeName);
        Client client = ClientDao.client;
        fullName.setText(client.getName() + " " + client.getSurname() + " " + (client.getPatronymic() == null ? "" : client.getPatronymic()));
        seriesAndNumberOfPassport.setText(client.getPassportSeries() + " " + client.getPassportNumber());
        issuedBy.setText(client.getPassportIssued());
        birth.setValue(convertToLocalDateViaInstant(client.getBirth()));
        dateOfIssue.setValue(convertToLocalDateViaInstant(client.getDateOfIssue()));
        setEmployee();
    }

    public void accept(ActionEvent actionEvent) {
        clientDao = new ClientDao();
        if (clientDao.add(fullName.getText(), issuedBy.getText(), seriesAndNumberOfPassport.getText(), birth.getValue(),
                dateOfIssue.getValue())){
            tripDao.addClientToTrip(route.getText());
            getMessageBox("Success!", "The client has been created successfully and will " +
                    "be on the road soon!", "Have a good flight!");
        } else {
            getMessageBox("Error!", "An error has occurred", "Contact the administrator or check data");
        }
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    private void getMessageBox(String title, String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                //TODO: сделать скачивание билета (обычная PDFка)
                System.out.println("Pressed OK.");
            }
        });
    }

    private void setEmployee() {
        EmployeeDao employeeDao = new EmployeeDao();
        List<Employee> employeeList = employeeDao.getAllEmployee();
        ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();
        for (Employee employee : employeeList){
            if(employee.getRole_id() == Role.TRAVEL_AGENT) {
                employeeObservableList.add(employee);
            }
        }
        this.employee.setItems(employeeObservableList);
    }
}
