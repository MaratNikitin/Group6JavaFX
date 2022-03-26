/**
 * Sample Skeleton for 'main.fxml' Controller Class
 */

package group6.travelexperts;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="Tables"
    private ToggleGroup Tables; // Value injected by FXMLLoader

    @FXML // fx:id="btnExit"
    private Button btnExit; // Value injected by FXMLLoader

    @FXML // fx:id="btnSave"
    private Button btnOpen; // Value injected by FXMLLoader

    @FXML // fx:id="radioPackages"
    private RadioButton radioPackages; // Value injected by FXMLLoader

    @FXML // fx:id="radioProducts"
    private RadioButton radioProducts; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert Tables != null : "fx:id=\"Tables\" was not injected: check your FXML file 'main.fxml'.";
        assert btnExit != null : "fx:id=\"btnExit\" was not injected: check your FXML file 'main.fxml'.";
        assert btnOpen != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'main.fxml'.";
        assert radioPackages != null : "fx:id=\"radioPackages\" was not injected: check your FXML file 'main.fxml'.";
        assert radioProducts != null : "fx:id=\"radioProducts\" was not injected: check your FXML file 'main.fxml'.";

        // when 'Exit' button is clicked, the app closes:
        btnExit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // before closing the app, a user is asked to confirm the closure
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("App closure confirmation");
                alert.setContentText("Press 'Ok' button if you want to close the app, " +
                        "and 'Cancel' otherwise");
                Optional<ButtonType> result = alert.showAndWait();
                // the app will be closed only if closure was confirmed by a user:
                if (result.get() == ButtonType.OK) { // true if Ok button was pressed
                    Stage stage = (Stage) btnExit.getScene().getWindow();
                    stage.close(); // this leads to closing the app
                } // end of if statement

            } // end of handle method
        }); // end of click event handler
    }
}
