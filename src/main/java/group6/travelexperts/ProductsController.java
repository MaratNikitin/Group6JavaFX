/*
Author: Rebecca Allan;
Co-Author: , contribution: ;
Workshop #6 (JavaFX),
PROJ-207 Threaded Project, Stage 3, Workshop #6 (JavaFX),
OOSD program, SAIT, March-May 2022;
This app allows doing CRUD operations in select tables of the 'travelexperts' MySQL database
    using a friendly JavaFX GUI.
This is the controller class responsible for the 'Products' window.
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductsController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAddProduct"
    private Button btnAddProduct; // Value injected by FXMLLoader

    @FXML // fx:id="colProdName"
    private TableColumn<Product, String> colProdName; // Value injected by FXMLLoader

    @FXML // fx:id="colProductId"
    private TableColumn<Product, Integer> colProductId; // Value injected by FXMLLoader

    @FXML // fx:id="lblProductsHeader"
    private Label lblProductsHeader; // Value injected by FXMLLoader

    @FXML // fx:id="tvProducts"
    private TableView<Product> tvProducts; // Value injected by FXMLLoader

    // this observable list, used for populating TableView, is created at class level to make it accessible everywhere:
    private ObservableList<Product> data = FXCollections.observableArrayList();

    // this mode variable is either "edit" or "add":
    private String mode = "edit";

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        colProductId.setCellValueFactory(new PropertyValueFactory<Product, Integer>("productId"));
        colProdName.setCellValueFactory(new PropertyValueFactory<Product, String>("prodName"));

        // Populating the TableView with data from the DB using data binding:
        tvProducts.setItems(data);

        getProducts(); // getting products from DB and loading them to TableView

        // when a product is selected in the TableView:
        tvProducts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue<? extends Product> observableValue, Product aProduct, Product t1) {
                if (tvProducts.getSelectionModel().isSelected(tvProducts.getSelectionModel().getSelectedIndex())) {
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

        // when "Add a New Product" button is clicked:
        btnAddProduct.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //loading the dialog in "add" mode. No Product is needed.
                mode = "add";
                openDialog(null, mode);
            }
        });


    } // end of initialize() method

    // method for opening a new dialog window:
    private void openDialog(Product t1, String mode) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit_products.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProductEditController dialogController = fxmlLoader.<ProductEditController>getController();
        dialogController.passModeToDialog(mode);
        if (mode.equals("edit")) {
            // if a product is edited, pass it to the dialog:
            try {
                dialogController.processProduct(t1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // opening the "Add/Modify/Delete a Product" modal window:
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Add/Modify/Delete a Product");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

        // running this method again is needed for refreshing the TableView:
        getProducts();
    }

    // method getting products from the DB:
    private void getProducts() {
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
            ResultSet rs = stmt.executeQuery("select * from products");
            while (rs.next())
            {
                data.add(new Product(rs.getInt(1), rs.getString(2)));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // end of getProducts() method:


} // end of ProductController class
