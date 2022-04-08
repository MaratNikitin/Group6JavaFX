/*
Author: Rebecca Allan;
Co-Author: Arvin San Juan, contribution: Data Validation Implement;
Workshop #6 (JavaFX),
PROJ-207 Threaded Project, Stage 3, Workshop #6 (JavaFX),
OOSD program, SAIT, March-May 2022;
This app allows doing CRUD operations in select tables of the 'travelexperts' MySQL database
    using a friendly JavaFX GUI.
This is the controller class responsible for the 'Add/Edit/Delete Products' window.
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
import java.util.*;

public class ProductEditController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnDeleteProd"
    private Button btnDeleteProd; // Value injected by FXMLLoader

    @FXML // fx:id="btnSaveProdChanges"
    private Button btnSaveProdChanges; // Value injected by FXMLLoader

    @FXML // fx:id="lblAddDeleteProd"
    private Label lblAddDeleteProd; // Value injected by FXMLLoader

    @FXML // fx:id="lblProductD"
    private Label lblProductD; // Value injected by FXMLLoader

    @FXML // fx:id="lblProductName"
    private Label lblProductName; // Value injected by FXMLLoader

    @FXML // fx:id="txtProductID"
    private TextField txtProductID; // Value injected by FXMLLoader

    @FXML // fx:id="txtProductName"
    private TextField txtProductName; // Value injected by FXMLLoader

    private String mode;

    @FXML // fx:id="lblProdName"
    private Label lblProdName; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        //depending on the mode, this will be an edit or add
        btnSaveProdChanges.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                btnSaveClicked(mouseEvent);
            }
        });

        // when the "Delete Product" button is clicked:
        btnDeleteProd.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            // preparing connection parameters:
            public void handle(MouseEvent mouseEvent) {
                String user = "group6";
                String password = "group6";
                String url = "jdbc:mysql://localhost:3306/travelexperts";

                try {
                    Connection conn = DriverManager.getConnection(url, user, password);

                    String sql = "DELETE FROM `products` WHERE ProductId=?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, Integer.parseInt(txtProductID.getText()));

                    // before closing the app, a user is asked to confirm the closure
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("DELETE CONFIRMATION");
                    alert.setContentText("Press 'Ok' button if you want to delete the product, " +
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
                    alert.setContentText("A product cannot be deleted because of a DB's foreign key constraint");
                    alert.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        // txtProductName focus in and focus out event
        txtProductName.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (newPropertyValue) //focus in
                {
                    lblProdName.setVisible(false); //set the lblProdName as not visible
                }
                else //focus out
                {
                    if (txtProductName.getText().length() > 50) // check if the txtDescription length is greater than 50 character
                    {
                        lblProdName.setVisible(true); //set the lblProdName as visible
                    }
                    else
                    {
                        lblProdName.setVisible(false); //set the lblProdName as not visible
                    }
                }
            }
        });

        //alert messages sets as not visible
        lblProdName.setVisible(false);

    } // end of initialize();

    // method for passing mode to the dialog window
    public void passModeToDialog(String mode) {
        this.mode = mode;

        //display the mode on the dialog
        lblAddDeleteProd.setText("Current task: " + mode + " a product");

        //if this is add mode, hide the delete button, as there is nothing to delete
        if (mode.equals("add"))
        {
            btnDeleteProd.setVisible(false);
            // since ProductId is an auto-generated primary key, it's intentionally hidden:
            txtProductID.setVisible(false);
            lblProductD.setVisible(false);
        }
    }

    // method for populating text fields with variable values of a product to be edited or deleted:
    public void processProduct(Product p) throws ParseException {
        txtProductID.setText(p.getProductId() + "");
        txtProductName.setText(p.getProdName());

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
                sql = "UPDATE `products` SET `ProdName`=? WHERE ProductId=?";
            }
            else
            { // if not 'edit', it must be 'add' mode:
                sql = "INSERT INTO `products`(`ProductId`, `ProdName`) VALUES (null, ?)";
            }
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtProductName.getText());

            //if we are in "edit" mode there is the second parameter insert to do to set the ProductId:
            if (mode.equals("edit")) {
                stmt.setInt(2, Integer.parseInt(txtProductID.getText()));
            }

            //filter text fields if empty
            if (!txtProductName.getText().isEmpty()) // check if txtProductName if empty
            {
                if (txtProductName.getText().length() > 50) // check if the txtDescription length is greater than 50 character
                {
                    lblProdName.setVisible(true); //set the lblProdName as visible
                    // Alert Message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Save failed");
                    alert.setContentText("Please check the validated textfields.");
                    alert.showAndWait();
                }
                else
                {
                    lblProdName.setVisible(false); //set the lblProdName as not visible
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
                // Alert Message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Save failed");
                alert.setContentText("Please enter Product name first.");
                alert.showAndWait();
            }
        } //end of try
        catch (SQLException e) {
            e.printStackTrace();
        } // end of catch

    } // end of btnSaveClicked()
} // end of ProductEditController


