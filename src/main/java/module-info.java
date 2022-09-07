module com.database.dbadmin {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.database.dbadmin to javafx.fxml;
    exports com.database.dbadmin;
}