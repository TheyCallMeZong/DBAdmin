module com.database.dbadmin {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;
    requires javax.persistence;
    requires org.jetbrains.annotations;

    opens com.database.dbadmin to javafx.fxml;
    exports com.database.dbadmin;
    exports com.database.dbadmin.database;
    opens com.database.dbadmin.database to javafx.fxml;
    exports com.database.dbadmin.controllers;
    opens com.database.dbadmin.controllers to javafx.fxml;
}