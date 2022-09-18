package com.database.dbadmin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    public static Stage stg;

    @Override
    public void start(Stage stage) throws IOException {
        stg = stage;
        openStage("authorization.fxml", stage, 700, 400);
    }

    public void changeScene(String fxml){
        try {
            Parent pane = FXMLLoader.load(getClass().getResource(fxml));
            stg.getScene().setRoot(pane);
        } catch (IOException ex){
            System.err.println("err in change scene");
        }
    }

    private void openStage(String fxml, Stage stage, int width, int height) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        stage.setTitle("Travel company");
        stage.setResizable(false);
        stage.setScene(new Scene(fxmlLoader.load(), width, height));
        stage.show();
    }

    public void openStage(String fxml) throws IOException {
        openStage(fxml, new Stage(), 700, 400);
    }

    public void openStage(String fxml, int width, int height) throws IOException {
        openStage(fxml, new Stage(), width, height);
    }

    public static void main(String[] args){
        launch();
    }
}