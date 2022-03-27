/*
Author: Marat Nikitin;
Co-Author: , contribution: ;
Workshop #6 (JavaFX),
PROJ-207 Threaded Project, Stage 3, Workshop #6 (JavaFX),
OOSD program, SAIT, March-May 2022;
This app allows doing CRUD operations in select tables of the 'travelexperts' MySQL database
    using a friendly JavaFX GUI.
This is the controller class responsible for the 'Add/Edit/Delete Packages' window.
*/

package group6.travelexperts;

import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.text.DateFormat;

public class PackageEditController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnDeletePkg"
    private Button btnDeletePkg; // Value injected by FXMLLoader

    @FXML // fx:id="btnSavePkgChanges"
    private Button btnSavePkgChanges; // Value injected by FXMLLoader

    @FXML // fx:id="lblAddDeletePkg"
    private Label lblAddDeletePkg; // Value injected by FXMLLoader

    @FXML // fx:id="lblSelectAgentID"
    private Label lblPackageID; // Value injected by FXMLLoader

    @FXML // fx:id="lblSelectAgentID1"
    private Label lblSelectAgentID1; // Value injected by FXMLLoader

    @FXML // fx:id="lblSelectAgentID11"
    private Label lblSelectAgentID11; // Value injected by FXMLLoader

    @FXML // fx:id="lblSelectAgentID111"
    private Label lblSelectAgentID111; // Value injected by FXMLLoader

    @FXML // fx:id="lblSelectAgentID1111"
    private Label lblSelectAgentID1111; // Value injected by FXMLLoader

    @FXML // fx:id="lblSelectAgentID11111"
    private Label lblSelectAgentID11111; // Value injected by FXMLLoader

    @FXML // fx:id="lblSelectAgentID111111"
    private Label lblSelectAgentID111111; // Value injected by FXMLLoader

    @FXML // fx:id="txtBasePrice"
    private TextField txtBasePrice; // Value injected by FXMLLoader

    @FXML // fx:id="txtCommission"
    private TextField txtCommission; // Value injected by FXMLLoader

    @FXML // fx:id="txtDescription"
    private TextField txtDescription; // Value injected by FXMLLoader

    @FXML // fx:id="txtEndDate"
    private TextField txtEndDate; // Value injected by FXMLLoader

    @FXML // fx:id="txtPackageID"
    private TextField txtPackageID; // Value injected by FXMLLoader

    @FXML // fx:id="txtPackageName"
    private TextField txtPackageName; // Value injected by FXMLLoader

    @FXML // fx:id="txtStartDate"
    private TextField txtStartDate; // Value injected by FXMLLoader

    private String mode;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        //depending on the mode, this will be an edit or add
        btnSavePkgChanges.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                btnSaveClicked(mouseEvent);
            }
        });

        // when the "Delete Package" button is clicked:
        btnDeletePkg.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            // preparing connection parameters:
            public void handle(MouseEvent mouseEvent) {
                String user = "group6";
                String password = "group6";
                String url = "jdbc:mysql://localhost:3306/travelexperts";

                try {
                    Connection conn = DriverManager.getConnection(url, user, password);

                    String sql = "DELETE FROM `packages` WHERE PackageId=?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, Integer.parseInt(txtPackageID.getText()));

                    // before closing the app, a user is asked to confirm the closure
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("DELETE CONFIRMATION");
                    alert.setContentText("Press 'Ok' button if you want to delete the package, " +
                            "and 'Cancel' otherwise");
                    Optional<ButtonType> result = alert.showAndWait();
                    // the delete will be executed only if it was confirmed by a user:
                    if (result.get() == ButtonType.OK) { // true if Ok button was pressed
                        int numRows = stmt.executeUpdate();
                    } // end of if statement

                    conn.close(); // mission accomplished

                    // closing the modal window:
                    Node node = (Node) mouseEvent.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();

                } catch (SQLIntegrityConstraintViolationException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Delete failed");
                    alert.setContentText("A package cannot be deleted because of a DB's foreign key constraint");
                    alert.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    } // end of initialize();

    // method for passing mode to the dialog window
    public void passModeToDialog(String mode) {
        this.mode = mode;

        //display the mode on the dialog
        lblAddDeletePkg.setText("Current task: " + mode + " a package");

        //if this is add mode, hide the delete button, as there is nothing to delete
        if (mode.equals("add"))
        {
            btnDeletePkg.setVisible(false);
            // since PackageId is an auto-generated primary key, it's intentionally hidden:
            txtPackageID.setVisible(false);
            lblPackageID.setVisible(false);
        }
    }

    // method for populating text fields with variable values of a package to be edited or deleted:
    public void processPackage(Package p) throws ParseException {
        txtPackageID.setText(p.getPackageId() + "");
        txtPackageName.setText(p.getPkgName());

        // formatting Package Start Date:
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(p.getPkgStartDate());
        String formattedStartDate = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
        txtStartDate.setText(formattedStartDate);

        // formatting Package End Date:
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(p.getPkgEndDate());
        String formattedEndDate = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
        txtEndDate.setText(formattedEndDate);

        txtDescription.setText(p.getPkgDesc());
        txtBasePrice.setText(p.getPkgBasePrice() + "");
        txtCommission.setText(p.getPkgAgencyCommission() + "");
    }

    // this method is executed when the "Save Changes" button is clicked:
    private void btnSaveClicked(MouseEvent mouseEvent) {
        String user = "group6";
        String password = "group6";
        String url = "jdbc:mysql://localhost:3306/travelexperts";

        try {
            // establishing connection to the DB:
            Connection conn = DriverManager.getConnection(url, user, password);

            String sql = null; // sql string value to be executed in the DB
            // if the mode is "edit", do an update; else, do an insert:
            if (mode.equals("edit")) {
                sql = "UPDATE `packages` SET `PkgName`=?,`PkgStartDate`=?,`PkgEndDate`=?,`PkgDesc`=?," +
                        "`PkgBasePrice`=?,`PkgAgencyCommission`=? WHERE PackageId=?";
            }
            else
            { // if not 'edit', it must be 'add' mode:
                sql = "INSERT INTO `packages`(`PackageId`, `PkgName`, `PkgStartDate`, `PkgEndDate`, `PkgDesc`, `PkgBasePrice`, " +
                        "`PkgAgencyCommission`) VALUES (null, ?,?,?,?,?,?)";
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtPackageName.getText());
            stmt.setString(2, txtStartDate.getText());
            stmt.setString(3, txtEndDate.getText());
            stmt.setString(4, txtDescription.getText());
            stmt.setString(5, txtBasePrice.getText());
            stmt.setString(6, txtCommission.getText());

            //if we are in "edit" mode there is the seventh parameter insert to do to set the PackageId:
            if (mode.equals("edit")) {
                stmt.setInt(7, Integer.parseInt(txtPackageID.getText()));
            }
            int numRows = stmt.executeUpdate();
            conn.close(); // mission accomplished

            // closing the modal window:
            Node node = (Node) mouseEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();

        } //end of try
        catch (SQLException e) {
            e.printStackTrace();
        } // end of catch

    } // end of btnSaveClicked()
} // end of PackageEditController
