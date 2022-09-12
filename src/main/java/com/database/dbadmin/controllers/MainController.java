package com.database.dbadmin.controllers;

import com.database.dbadmin.Main;
import com.database.dbadmin.dao.EmployeeDao;
import com.database.dbadmin.database.PostgresSqlConnect;
import javafx.event.ActionEvent;

import java.io.IOException;

public class MainController {
    private final Main main;

    public MainController(){
        main = new Main();
    }

    public void logout(ActionEvent event){
        main.changeScene("authorization.fxml");
    }

    public void addUser() throws IOException {
        main.openStage("addingEmployee.fxml");
    }

    public void getAllUsers() throws IOException{
        main.openStage("allEmployee.fxml");
    }

    public void openProfile(){

    }
}
