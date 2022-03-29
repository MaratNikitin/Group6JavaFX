/*
Author: Marat Nikitin;
Co-Author: , contribution: ;
Workshop #6 (JavaFX),
PROJ-207 Threaded Project, Stage 3, Workshop #6 (JavaFX),
OOSD program, SAIT, March-May 2022;
This app allows doing CRUD operations in select tables of the 'travelexperts' MySQL database
    using a friendly JavaFX GUI.
This is the controller class responsible for the 'Packages' window.
*/

package group6.travelexperts;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PackageController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAddPackage"
    private Button btnAddPackage; // Value injected by FXMLLoader

    @FXML // fx:id="colPackageId"
    private TableColumn<Package, Integer> colPackageId; // Value injected by FXMLLoader

    @FXML // fx:id="colPkgAgencyCommission"
    private TableColumn<Package, Double> colPkgAgencyCommission; // Value injected by FXMLLoader

    @FXML // fx:id="colPkgBasePrice"
    private TableColumn<Package, Double> colPkgBasePrice; // Value injected by FXMLLoader

    @FXML // fx:id="colPkgDesc"
    private TableColumn<Package, String> colPkgDesc; // Value injected by FXMLLoader

    @FXML // fx:id="colPkgEndDate"
    private TableColumn<Package, String> colPkgEndDate; // Value injected by FXMLLoader

    @FXML // fx:id="colPkgName"
    private TableColumn<Package, String> colPkgName; // Value injected by FXMLLoader

    @FXML // fx:id="colPkgStartDate"
    private TableColumn<Package, String> colPkgStartDate; // Value injected by FXMLLoader

    @FXML // fx:id="tvPackages"
    private TableView<Package> tvPackages; // Value injected by FXMLLoader

    // this observable list, used for populating TableView, is created at class level to make it accessible everywhere:
    private ObservableList<Package> data = FXCollections.observableArrayList();

    // this mode variable is either "edit" or "add":
    private String mode = "edit";

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        colPackageId.setCellValueFactory(new PropertyValueFactory<Package, Integer>("packageId"));
        colPkgName.setCellValueFactory(new PropertyValueFactory<Package, String>("pkgName"));
        colPkgStartDate.setCellValueFactory(new PropertyValueFactory<Package, String>("pkgStartDate"));
        colPkgEndDate.setCellValueFactory(new PropertyValueFactory<Package, String>("pkgEndDate"));
        colPkgDesc.setCellValueFactory(new PropertyValueFactory<Package, String>("pkgDesc"));
        colPkgBasePrice.setCellValueFactory(new PropertyValueFactory<Package, Double>("pkgBasePrice"));
        colPkgAgencyCommission.setCellValueFactory(new PropertyValueFactory<Package, Double>("pkgAgencyCommission"));

        // Populating the TableView with data from the DB using data binding:
        tvPackages.setItems(data);

        getPackages(); // getting packages from DB and loading them to TableView

        // when a package is selected in the TableView:
        tvPackages.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Package>() {
            @Override
            public void changed(ObservableValue<? extends Package> observableValue, Package aPackage, Package t1) {
                if (tvPackages.getSelectionModel().isSelected(tvPackages.getSelectionModel().getSelectedIndex())) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            mode = "edit";
                            openDialog(t1, mode); // custom method
                        }
                    });
                }
            }
        }); // end of event listener

        // when "Add a New Package" button is clicked:
        btnAddPackage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //loading the dialog in "add" mode. No agent is needed.
                mode = "add";
                openDialog(null, mode);
            }
        });


    } // end of initialize() method

    // method for opening a new dialog window:
    private void openDialog(Package t1, String mode) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit_packages.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PackageEditController dialogController = fxmlLoader.<PackageEditController>getController();
        dialogController.passModeToDialog(mode);
        if (mode.equals("edit")) {
            // if a package is edited, pass it to the dialog:
            try {
                dialogController.processPackage(t1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // opening the "Add/Modify/Delete a Package" modal window:
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Add/Modify/Delete a Package");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

        // running this method again is needed for refreshing the TableView:
        getPackages();
    }

    // method getting packages from the DB:
    private void getPackages() {
        // clearing the observable list prior to adding the data:
        data.clear();

        // preparing connecting to the database:
        String user = "group6";
        String password = "group6";
        String url = "jdbc:mysql://localhost:3306/travelexperts";

        try {
            // connecting to the DB:
            Connection conn = DriverManager.getConnection(url, user, password); // establishing DB connection
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select PackageId, PkgName, DATE_FORMAT(PkgStartDate, \"%Y-%m-%d\"), " +
                    "DATE_FORMAT(PkgEndDate, \"%Y-%m-%d\"), PkgDesc, PkgBasePrice, PkgAgencyCommission from packages");
            while (rs.next())
            {
                data.add(new Package(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getDouble(6), rs.getDouble(7)));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // end of getPackages() method:

}
