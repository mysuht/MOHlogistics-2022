package moh.logistics.lib.reports.resources;

import java.sql.Date;

public class CTFItems {
    private CTFMain ctfMainId;
    private Integer ctfItemId;
    private Date pdate;
    private Integer prodId;
    private Integer openBal;
    private Integer closingBal;

    public CTFItems() {

    }


    public void setCtfMainId(CTFMain ctfMainId) {
        this.ctfMainId = ctfMainId;
    }

    public CTFMain getCtfMainId() {
        return ctfMainId;
    }

    public void setCtfItemId(Integer ctfItemId) {
        this.ctfItemId = ctfItemId;
    }

    public Integer getCtfItemId() {
        return ctfItemId;
    }


    public void setPdate(Date pdate) {
        this.pdate = pdate;
    }

    public Date getPdate() {
        return pdate;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public Integer getProdId() {
        return prodId;
    }

    public void setOpenBal(Integer openBal) {
        this.openBal = openBal;
    }

    public Integer getOpenBal() {
        return openBal;
    }

    public void setClosingBal(Integer closingBal) {
        this.closingBal = closingBal;
    }

    public Integer getClosingBal() {
        return closingBal;
    }
}
