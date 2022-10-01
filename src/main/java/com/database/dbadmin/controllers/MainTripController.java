package com.database.dbadmin.controllers;

import com.database.dbadmin.Main;
import com.database.dbadmin.dao.TripDao;
import com.database.dbadmin.models.Route;
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
import java.util.Set;

public class MainTripController {
    public static String routeName;

    @FXML
    public TableView<Route> table;

    @FXML
    public TableColumn<Route, String> routeColumn;

    @FXML
    public TableColumn<Route, Void> chooseColumn;

    private TripDao tripDao;

    private ObservableList<Route> observableList;

    @FXML
    void initialize() {
        routeColumn.setCellValueFactory(new PropertyValueFactory<>("routeName"));
        tripDao = new TripDao();
        Set<Route> routeSet = tripDao.getRoutes();
        observableList = FXCollections.observableList(routeSet.stream().toList());
        table.getItems().addAll(observableList);
        addChooseColumn();
    }

    private void addChooseColumn(){
        Callback<TableColumn<Route, Void>, TableCell<Route, Void>> cellFactory = new Callback<>() {
            Main main = new Main();
            @Override
            public TableCell<Route, Void> call(final TableColumn<Route, Void> param) {
                final TableCell<Route, Void> cell = new TableCell<Route, Void>() {
                    private final Button btn = new Button("choose");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            try {
                                routeName = getTableView().getItems().get(getIndex()).getRouteName();
                                main.openStage("confirm.fxml", 700, 400);
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
        chooseColumn.setCellFactory(cellFactory);
    }
}