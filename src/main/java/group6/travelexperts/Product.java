/*
Author: Rebecca Allan;
Co-Author: , contribution: ;
Workshop #6 (JavaFX),
PROJ-207 Threaded Project, Stage 3, Workshop #6 (JavaFX),
OOSD program, SAIT, March-May 2022;
This app allows doing CRUD operations in select tables of the 'travelexperts' MySQL database
    using a friendly JavaFX GUI.
This is the 'Product' class representing 'products' database entity.
*/

package group6.travelexperts;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Product {
    // defining class variables:
    private SimpleIntegerProperty productId;
    private SimpleStringProperty prodName;

    // complete constructor with conversion to 'Simple*Properties':
    public Product(int productId, String prodName) {
        this.productId = new SimpleIntegerProperty(productId);
        this.prodName = new SimpleStringProperty(prodName);
    }

    // getters and setters for all class variables:

    public int getProductId() {
        return productId.get();
    }

    public SimpleIntegerProperty productIdProperty() {return productId;}

    public void setProductId(int productId) {this.productId.set(productId);}

    public String getProdName() {return prodName.get();}

    public SimpleStringProperty prodNameProperty() {return prodName;}

    public void setProdName(String prodName) {this.prodName.set(prodName);}
}
