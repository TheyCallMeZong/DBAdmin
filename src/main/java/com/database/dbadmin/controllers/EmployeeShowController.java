package com.database.dbadmin.controllers;

import com.database.dbadmin.Main;
import com.database.dbadmin.dao.EmployeeDao;
import com.database.dbadmin.models.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import java.io.IOException;

public class EmployeeShowController {
    public static Employee employee;

    @FXML
    public TableView<Employee> table;

    @FXML
    public TableColumn<Employee, Long> id;

    @FXML
    public TableColumn<Employee, String> name;

    @FXML
    public TableColumn<Employee, String> email;

    @FXML
    public TableColumn<Employee, String> phoneNumber;

    @FXML
    public TableColumn<Employee, Void> edit;

    @FXML
    public TableColumn<Employee, Void> delete;

    private ObservableList<Employee> observableList;

    private final EmployeeDao employeeDao;

    private final Main main;

    public EmployeeShowController() {
        main = new Main();
        this.employeeDao = new EmployeeDao();
    }

    @FXML
    void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setMaxWidth(40);
        id.setMinWidth(40);
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setMaxWidth(140);
        name.setMinWidth(140);
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        email.setMaxWidth(150);
        email.setMinWidth(150);
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneNumber.setMaxWidth(179);
        phoneNumber.setMinWidth(179);
        table.setEditable(true);
        observableList = FXCollections.observableList(employeeDao.getAllEmployee());
        addDeleteButtonToTable();
        addEditButtonToTable();
        table.getItems().addAll(observableList);
    }

    private void addEditButtonToTable(){
        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Employee, Void> call(final TableColumn<Employee, Void> param) {
                final TableCell<Employee, Void> cell = new TableCell<Employee, Void>() {
                    private final Button btn = new Button("edit");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            employee = getTableView().getItems().get(getIndex());
                            try {
                                main.openStage("edit.fxml", 350, 344);
                            } catch (IOException e) {
                                System.err.println("err");
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        edit.setMaxWidth(70);
        edit.setMinWidth(70);
        edit.setCellFactory(cellFactory);
    }

    private void addDeleteButtonToTable(){
        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Employee, Void> call(final TableColumn<Employee, Void> param) {
                final TableCell<Employee, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("delete");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Employee data = getTableView().getItems().get(getIndex());
                            employeeDao.deleteEmployee(data.getId());
                            observableList.remove(data);
                            table.setItems(observableList);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        delete.setMinWidth(90);
        delete.setMaxWidth(90);
        delete.setCellFactory(cellFactory);
    }

    public void update(ActionEvent event) {
        observableList.clear();
        observableList.addAll(employeeDao.getAllEmployee());
        table.setItems(observableList);
    }
}
