package com.logistics.lib.dto;

import com.logistics.lib.util.DataTransferObject;

public class CTFItem implements DataTransferObject {
    private long id;
    private Long ctfMainId;
    private int productID;
    private int openBalance;
    private int receipts;
    private int issues;
    private int adjustments;
    private Long adjTypeId;
    private int avgMnthlyCons;
    private int closingBalance;


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


    public void setAvgMnthlyCons(int avgMnthlyCons) {
        this.avgMnthlyCons = avgMnthlyCons;
    }

    public int getAvgMnthlyCons() {
        return avgMnthlyCons;
    }

    public void setCtfMainId(Long ctfMainId) {
        this.ctfMainId = ctfMainId;
    }

    public Long getCtfMainId() {
        return ctfMainId;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getProductID() {
        return productID;
    }

    public void setOpenBalance(int openBalance) {
        this.openBalance = openBalance;
    }

    public int getOpenBalance() {
        return openBalance;
    }

    public void setReceipts(int receipts) {
        this.receipts = receipts;
    }

    public int getReceipts() {
        return receipts;
    }

    public void setIssues(int issues) {
        this.issues = issues;
    }

    public int getIssues() {
        return issues;
    }

    public void setAdjustments(int adjustments) {
        this.adjustments = adjustments;
    }

    public int getAdjustments() {
        return adjustments;
    }

    public void setAdjTypeId(Long adjTypeId) {
        this.adjTypeId = adjTypeId;
    }

    public Long getAdjTypeId() {
        return adjTypeId;
    }

    public void setClosingBalance(int closingBalance) {
        this.closingBalance = closingBalance;
    }

    public int getClosingBalance() {
        return closingBalance;
    }

}
