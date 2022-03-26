module group6.travelexperts {
    requires javafx.controls;
    requires javafx.fxml;


    opens group6.travelexperts to javafx.fxml;
    exports group6.travelexperts;
}