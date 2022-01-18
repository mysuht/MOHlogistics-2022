package com.logistics.lib.dto;

import com.logistics.lib.util.DataTransferObject;

public class Product implements DataTransferObject {
    private long id;
    private String productName;
    private String productDose;
    private String productCYP;
    private String isProductActive;
    private String productShipQTYPIN;
    private String productShipQTYWHSE;
    private String productShipQTYSDP;
    private String PrCtrindex;


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductDose(String productDose) {
        this.productDose = productDose;
    }

    public String getProductDose() {
        return productDose;
    }

    public void setProductCYP(String productCYP) {
        this.productCYP = productCYP;
    }

    public String getProductCYP() {
        return productCYP;
    }

    public void setIsProductActive(String isProductActive) {
        this.isProductActive = isProductActive;
    }

    public String getIsProductActive() {
        return isProductActive;
    }

    public void setProductShipQTYPIN(String productShipQTYPIN) {
        this.productShipQTYPIN = productShipQTYPIN;
    }

    public String getProductShipQTYPIN() {
        return productShipQTYPIN;
    }

    public void setProductShipQTYWHSE(String productShipQTYWHSE) {
        this.productShipQTYWHSE = productShipQTYWHSE;
    }

    public String getProductShipQTYWHSE() {
        return productShipQTYWHSE;
    }

    public void setProductShipQTYSDP(String productShipQTYSDP) {
        this.productShipQTYSDP = productShipQTYSDP;
    }

    public String getProductShipQTYSDP() {
        return productShipQTYSDP;
    }

    public void setPrCtrindex(String PrCtrindex) {
        this.PrCtrindex = PrCtrindex;
    }

    public String getPrCtrindex() {
        return PrCtrindex;
    }

}
