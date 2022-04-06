/*
Author: Marat Nikitin;
Co-Author: Arvin San Juan, contribution: Data Validation Implement;
Workshop #6 (JavaFX),
PROJ-207 Threaded Project, Stage 3, Workshop #6 (JavaFX),
OOSD program, SAIT, March-May 2022;
This app allows doing CRUD operations in select tables of the 'travelexperts' MySQL database
    using a friendly JavaFX GUI.
This is the controller class responsible for the 'Add/Edit/Delete Packages' window.
*/

package group6.travelexperts;

import javafx.beans.value.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.*;

import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.Date;
import java.util.*;

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

    @FXML // fx:id="lblPkgName"
    private Label lblPkgName; // Value injected by FXMLLoader

    @FXML // fx:id="lblPkgStartDate"
    private Label lblPkgStartDate; // Value injected by FXMLLoader

    @FXML // fx:id="lblPkgStartDateAlert"
    private Label lblPkgStartDateAlert; // Value injected by FXMLLoader

    @FXML // fx:id="lblPkgEndDate"
    private Label lblPkgEndDate; // Value injected by FXMLLoader

    @FXML // fx:id="lblPkgEndDateAlert"
    private Label lblPkgEndDateAlert; // Value injected by FXMLLoader

    @FXML // fx:id="lblPkgPrice"
    private Label lblPkgPrice; // Value injected by FXMLLoader

    @FXML // fx:id="lblPkgPriceAlert"
    private Label lblPkgPriceAlert; // Value injected by FXMLLoader

    @FXML // fx:id="lblPkgDescription"
    private Label lblPkgDescription; // Value injected by FXMLLoader

    @FXML // fx:id="lblPkgCommission"
    private Label lblPkgCommission; // Value injected by FXMLLoader

    @FXML // fx:id="lblPkgCommissionAlert"
    private Label lblPkgCommissionAlert; // Value injected by FXMLLoader


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
                    // to delete will be executed only if it was confirmed by a user:
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

        txtPackageName.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    lblPkgName.setVisible(false);
                }
                else
                {
                    if (txtPackageName.getText().length() > 50)
                    {
                        System.out.println("The Package Name must be less than or equal 50 characters.");
                        lblPkgName.setVisible(true);
                    }
                    else
                    {
                        System.out.println("The Package Name is " + txtPackageName.getText());
                        lblPkgName.setVisible(false);
                    }
                }
            }
        });

        txtStartDate.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    lblPkgStartDate.setVisible(false);
                }
                else
                {
                    if (txtStartDate.getText()  != "")
                    {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        //To make strict date format validation
                        formatter.setLenient(false);
                        Date parsedDate = null;
                        try {
                            parsedDate = formatter.parse(txtStartDate.getText());
                            System.out.println("StartDate is " + formatter.format(parsedDate));
                            lblPkgStartDate.setVisible(false);
                        } catch (ParseException e) {
                            lblPkgStartDate.setVisible(true);
                        }
                    }
                }
                checkDates();
            }
        });

        txtEndDate.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    lblPkgEndDate.setVisible(false);
                }
                else
                {
                    if (txtEndDate.getText() != "")
                    {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        //To make strict date format validation
                        formatter.setLenient(false);
                        Date parsedDate = null;
                        try {
                            parsedDate = formatter.parse(txtEndDate.getText());
                            System.out.println("EndDate is " + formatter.format(parsedDate));
                            lblPkgEndDate.setVisible(false);
                        } catch (ParseException e) {
                            lblPkgEndDate.setVisible(true);
                        }
                    }
                }
                checkDates();
            }
        });

        txtDescription.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    lblPkgDescription.setVisible(false);
                }
                else
                {
                    if (txtDescription.getText().length() > 50)
                    {
                        lblPkgDescription.setVisible(true);
                    }
                    else
                    {
                        if (txtDescription.getText() != "")
                        {
                            System.out.println("The Description is " + txtDescription.getText());
                        }
                        lblPkgDescription.setVisible(false);
                    }
                }
            }
        });

        //accept only decimal
        txtBasePrice.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtBasePrice.setText(oldValue);
                }
            }
        });

        txtBasePrice.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    lblPkgPrice.setVisible(false);
                }
                else
                {
                    if (txtBasePrice.getText() != "")
                    {
                        double res = Math.signum(Double.parseDouble(txtBasePrice.getText()));
                        if (res == 1.0)
                        {
                            System.out.println("The Base Price is " + Double.parseDouble(txtBasePrice.getText()));
                            lblPkgPrice.setVisible(false);
                        }
                        else
                        {
                            lblPkgPrice.setVisible(true);
                        }
                    }
                }
                checkPriceCommission();
            }
        });

        //accept only decimal
        txtCommission.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtCommission.setText(oldValue);
                }
            }
        });

        txtCommission.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue)
                {
                    lblPkgCommission.setVisible(false);
                }
                else
                {
                    if (txtCommission.getText() != "")
                    {
                        double res = Math.signum(Double.parseDouble(txtCommission.getText()));
                        if (res == 1.0)
                        {
                            System.out.println("The Commission is " + Double.parseDouble(txtCommission.getText()));
                            lblPkgCommission.setVisible(false);
                        }
                        else
                        {
                            lblPkgCommission.setVisible(true);
                        }
                    }
                }
                checkPriceCommission();
            }
        });

        //alert messages sets as not visible
        lblPkgName.setVisible(false);
        lblPkgStartDate.setVisible(false);
        lblPkgStartDateAlert.setVisible(false);
        lblPkgEndDate.setVisible(false);
        lblPkgEndDateAlert.setVisible(false);
        lblPkgPrice.setVisible(false);
        lblPkgPriceAlert.setVisible(false);
        lblPkgDescription.setVisible(false);
        lblPkgCommission.setVisible(false);
        lblPkgCommissionAlert.setVisible(false);

    } // end of initialize();

    //check dates
    public void checkDates(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date StartDate = sdf.parse(txtStartDate.getText());
            Date EndDate = sdf.parse(txtEndDate.getText());
            if (StartDate.after(EndDate))
            {
                lblPkgStartDateAlert.setVisible(true);
                lblPkgEndDateAlert.setVisible(true);
            }
            else
            {
                lblPkgStartDateAlert.setVisible(false);
                lblPkgEndDateAlert.setVisible(false);
            }
            //StartDate.compareTo(EndDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //check Base Price Commission
    public void checkPriceCommission(){
        Double BasePrice = Double.parseDouble(txtBasePrice.getText());
        Double Commission = Double.parseDouble(txtCommission.getText());
        if(BasePrice > Commission)
        {
            lblPkgPriceAlert.setVisible(false);
            lblPkgCommissionAlert.setVisible(false);
        }
        else
        {
            lblPkgPriceAlert.setVisible(true);
            lblPkgCommissionAlert.setVisible(true);
        }
    }

    // method for passing mode to the dialog window
    public void passModeToDialog(String mode) {
        this.mode = mode;

        //display the mode on the dialog
        lblAddDeletePkg.setText("Current task: " + mode + " a package");

        //if this is adding mode, hide the delete button, as there is nothing to delete
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
        txtBasePrice.setText(String.valueOf(p.getPkgBasePrice()));
        txtCommission.setText(String.valueOf(p.getPkgAgencyCommission()));
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

            //filter text fields if empty
            if (!txtPackageName.getText().isEmpty()
                    && !txtStartDate.getText().isEmpty()
                    && !txtEndDate.getText().isEmpty()
                    && !txtDescription.getText().isEmpty()
                    && !txtBasePrice.getText().isEmpty()
                    && !txtCommission.getText().isEmpty()
            )
            {
                checkDates();
                checkPriceCommission();
                if (lblPkgName.isVisible()
                        || lblPkgStartDate.isVisible()
                        || lblPkgStartDateAlert.isVisible()
                        || lblPkgEndDate.isVisible()
                        || lblPkgEndDateAlert.isVisible()
                        || lblPkgPriceAlert.isVisible()
                        || lblPkgDescription.isVisible()
                        || lblPkgCommission.isVisible()
                        || lblPkgCommissionAlert.isVisible()
                )
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Save failed");
                    alert.setContentText("Please check the validated textfields.");
                    alert.showAndWait();
                }
                else
                {
                    lblPkgName.setVisible(false);
                    lblPkgStartDate.setVisible(false);
                    lblPkgEndDate.setVisible(false);
                    lblPkgPrice.setVisible(false);
                    lblPkgDescription.setVisible(false);
                    lblPkgCommission.setVisible(false);

                    int numRows = stmt.executeUpdate();
                    conn.close(); // mission accomplished

                    // closing the modal window:
                    Node node = (Node) mouseEvent.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();
                }
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Save failed");
                alert.setContentText("Please fill up the fields first.");
                alert.showAndWait();
            }

        } //end of try
        catch (SQLException e) {
            e.printStackTrace();
        } // end of catch

    } // end of btnSaveClicked()



} // end of PackageEditController
