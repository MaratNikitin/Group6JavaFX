/*
Author: Marat Nikitin;
Co-Author: , contribution: ;
Workshop #6 (JavaFX),
PROJ-207 Threaded Project, Stage 3, Workshop #6 (JavaFX),
OOSD program, SAIT, March-May 2022;
This app allows doing CRUD operations in select tables of the 'travelexperts' MySQL database
    using a friendly JavaFX GUI.
This is the 'Package' class representing 'packages' database entity.
*/

package group6.travelexperts;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;

public class Package {
    // defining class variables:
    private SimpleIntegerProperty packageId;
    private SimpleStringProperty pkgName;
    private SimpleStringProperty pkgStartDate;
    private SimpleStringProperty pkgEndDate;
    private SimpleStringProperty pkgDesc;
    private BigDecimal pkgBasePrice;
    private BigDecimal pkgAgencyCommission;

    // complete constructor with conversion to 'Simple*Properties':
    public Package(int packageId, String pkgName,
                   String pkgStartDate, String pkgEndDate,
                   String pkgDesc, String pkgBasePrice,
                   String pkgAgencyCommission) {
        this.packageId = new SimpleIntegerProperty(packageId);
        this.pkgName = new SimpleStringProperty(pkgName);
        this.pkgStartDate = new SimpleStringProperty(pkgStartDate);
        this.pkgEndDate = new SimpleStringProperty(pkgEndDate);
        this.pkgDesc = new SimpleStringProperty(pkgDesc);
        this.pkgBasePrice = new BigDecimal(pkgBasePrice);
        this.pkgAgencyCommission = new BigDecimal(pkgAgencyCommission);
    }

    // getters and setters for all class variables:

    public int getPackageId() {
        return packageId.get();
    }

    public SimpleIntegerProperty packageIdProperty() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId.set(packageId);
    }

    public String getPkgName() {
        return pkgName.get();
    }

    public SimpleStringProperty pkgNameProperty() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName.set(pkgName);
    }

    public String getPkgStartDate() {
        return pkgStartDate.get();
    }

    public SimpleStringProperty pkgStartDateProperty() {
        return pkgStartDate;
    }

    public void setPkgStartDate(String pkgStartDate) {
        this.pkgStartDate.set(pkgStartDate);
    }

    public String getPkgEndDate() {
        return pkgEndDate.get();
    }

    public SimpleStringProperty pkgEndDateProperty() {
        return pkgEndDate;
    }

    public void setPkgEndDate(String pkgEndDate) {
        this.pkgEndDate.set(pkgEndDate);
    }

    public String getPkgDesc() {
        return pkgDesc.get();
    }

    public SimpleStringProperty pkgDescProperty() {
        return pkgDesc;
    }

    public void setPkgDesc(String pkgDesc) {
        this.pkgDesc.set(pkgDesc);
    }

    public BigDecimal getPkgBasePrice() {
        return pkgBasePrice;
    }

    public void setPkgBasePrice(BigDecimal pkgBasePrice) {
        this.pkgBasePrice = pkgBasePrice;
    }

    public BigDecimal getPkgAgencyCommission() {
        return pkgAgencyCommission;
    }

    public void setPkgAgencyCommission(BigDecimal pkgAgencyCommission) {
        this.pkgAgencyCommission = pkgAgencyCommission;
    }
}
