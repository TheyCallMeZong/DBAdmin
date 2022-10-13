package com.database.dbadmin.controllers;

import com.database.dbadmin.Main;
import com.database.dbadmin.dao.ClientDao;
import com.database.dbadmin.models.Client;
import com.database.dbadmin.models.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Optional;

public class ClientsInRouteController {
    public static Client client;
    private Main main;
    @FXML
    public TableView<Client> table;

    @FXML
    public TableColumn<Client, Long> id;

    @FXML
    public TableColumn<Client, String> name;

    @FXML
    public TableColumn<Client, String> surname;

    @FXML
    public TableColumn<Client, String> passportSeries;

    @FXML
    public TableColumn<Client, String> passportNumber;

    @FXML
    public TableColumn<Client, Void> edit;

    @FXML
    public TableColumn<Client, Void> delete;

    private ClientDao clientDao;

    private ObservableList<Client> clients;

    private String route;

    @FXML
    public void initialize(){
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        passportSeries.setCellValueFactory(new PropertyValueFactory<>("passportSeries"));
        passportNumber.setCellValueFactory(new PropertyValueFactory<>("passportNumber"));
        clientDao = new ClientDao();
        route = AvailableTripsController.route;
        clients = FXCollections.observableArrayList(clientDao.getAllClientsFromRoute(route));
        table.setItems(clients);
        main = new Main();
        addDeleteButtonToTable();
        addEditButtonToTable();

    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<Client, Void>, TableCell<Client, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Client, Void> call(final TableColumn<Client, Void> param) {
                final TableCell<Client, Void> cell = new TableCell<Client, Void>() {
                    private final Button btn = new Button("delete");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            client = getTableView().getItems().get(getIndex());
                            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
                            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);
                            Alert alert = new Alert(Alert.AlertType.WARNING,
                                    "Are you sure you want to delete the client?",
                                    yes,
                                    no);

                            alert.setTitle("Date format warning");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.get() == yes){
                                clientDao.deleteClient(client);
                                clients.remove(client);
                                table.setItems(clients);
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
        delete.setMaxWidth(70);
        delete.setMinWidth(70);
        delete.setCellFactory(cellFactory);
    }

    private void addEditButtonToTable() {
        Callback<TableColumn<Client, Void>, TableCell<Client, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Client, Void> call(final TableColumn<Client, Void> param) {
                final TableCell<Client, Void> cell = new TableCell<Client, Void>() {
                    private final Button btn = new Button("edit");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            client = getTableView().getItems().get(getIndex());
                            try {
                                main.openStage("clientEdit.fxml", 447, 400);
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

    public void updateClients(ActionEvent actionEvent) {
        clients.clear();
        clients.addAll(clientDao.getAllClientsFromRoute(route));
        table.setItems(clients);
    }
}
