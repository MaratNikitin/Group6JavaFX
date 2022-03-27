module group6.travelexperts {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens group6.travelexperts to javafx.fxml;
    exports group6.travelexperts;
}