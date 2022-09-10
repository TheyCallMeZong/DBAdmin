package com.database.dbadmin.controllers;

import com.database.dbadmin.Main;
import com.database.dbadmin.database.PostgresSqlConnect;
import com.database.dbadmin.models.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.IOException;

public class MainController {
    @FXML
    private Button addUser;

    @FXML
    private Button admin;

    @FXML
    private Button allUsers;

    @FXML
    private Button deleteUser;

    @FXML
    private Button updateUser;

    @FXML
    private Button logout;

    private Main main;

    private ObservableList<Employee> observableList = FXCollections.observableArrayList();

    private PostgresSqlConnect postgresSqlConnect;

    public MainController(){
        main = new Main();
        postgresSqlConnect = PostgresSqlConnect.getInstance();
    }

    public void logout(ActionEvent event){
        main.changeScene("authorization.fxml");
    }

    public void addUser() throws IOException {
        main.openStage("addingEmployee.fxml");
    }

    public void deleteUser(){

    }

    public void editUser(){

    }

    public void getAllUsers(){
        TableView<Employee> table = new TableView<Employee>();
        TableColumn<Employee, String> nameColumn = new TableColumn("name");
        TableColumn<Employee, String> emailColumn = new TableColumn("email");
        TableColumn<Employee, Boolean> phoneNumberColumn = new TableColumn("phoneNumber");
        TableColumn<Employee, Button> editColumn = new TableColumn("edit");

    }

    public void openMenu(){

    }
}
