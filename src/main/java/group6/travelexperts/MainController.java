/*
Author: Marat Nikitin;
Co-Author: , contribution: ;
Workshop #6 (JavaFX),
PROJ-207 Threaded Project, Stage 3, Workshop #6 (JavaFX),
OOSD program, SAIT, March-May 2022;
This app allows doing CRUD operations in select tables of the 'travelexperts' MySQL database
    using a friendly JavaFX GUI.
This is the controller class responsible for the Main window.
*/

package group6.travelexperts;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
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

        // when 'Open' button is clicked, selected table's window is opened:
        btnOpen.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(Tables.getSelectedToggle().equals(radioPackages)){ // true if 'Packages' option was selected
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("packages.fxml"));
                    Parent parent = null;
                    try {
                        parent = fxmlLoader.load();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    // opening the 'packages' window:
                    PackageController packageController = fxmlLoader.<PackageController>getController();
                    Scene scene = new Scene(parent);
                    Stage stage = new Stage();
                    String css = this.getClass().getResource("/css/styles.css").toExternalForm();
                    scene.getStylesheets().add(css); // applying an external stylesheet
                    stage.setTitle("Travel Packages"); // setting window's title
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.showAndWait();
                }
                else // it means that 'Products' radio option was selected
                {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("products.fxml"));
                    Parent parent = null;
                    try {
                        parent = fxmlLoader.load();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    // opening the 'products' window:
                    ProductsController productsController = fxmlLoader.<ProductsController>getController();
                    Scene scene = new Scene(parent);
                    Stage stage = new Stage();
                    String css = this.getClass().getResource("/css/styles.css").toExternalForm();
                    scene.getStylesheets().add(css); // applying an external stylesheet
                    stage.setTitle("Products"); // setting window's title
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(scene);
                    stage.showAndWait();
                } // end of else
            } // end of habdle()
        }); // end of 'btnOpen.setOnMouseClicked()'
    } // end of initialize()
} // end of MainController
