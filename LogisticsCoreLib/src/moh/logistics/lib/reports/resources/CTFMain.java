package moh.logistics.lib.reports.resources;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class CTFMain {
    private Integer ctfMainId;
    private Integer facilityId;
    private Date pdate;
    private Date deDate;
    private String deStaff;
    private Date lcDate;
    private String lcStaff;
    private String ctfStatus;
    private String ctfComments;
    private String xfacCode;
    private String ctfStage;
    private Set<CTFItems> ctfItems = new HashSet<CTFItems>(0);

    public CTFMain() {

    }


    public void setCtfMainId(Integer ctfMainId) {
        this.ctfMainId = ctfMainId;
    }

    public Integer getCtfMainId() {
        return ctfMainId;
    }

    public void setFacilityId(Integer facilityId) {
        this.facilityId = facilityId;
    }

    public Integer getFacilityId() {
        return facilityId;
    }

    public void setPdate(Date pdate) {
        this.pdate = pdate;
    }

    public Date getPdate() {
        return pdate;
    }


    public void setDeDate(Date deDate) {
        this.deDate = deDate;
    }

    public Date getDeDate() {
        return deDate;
    }

    public void setDeStaff(String deStaff) {
        this.deStaff = deStaff;
    }

    public String getDeStaff() {
        return deStaff;
    }

    public void setLcDate(Date lcDate) {
        this.lcDate = lcDate;
    }

    public Date getLcDate() {
        return lcDate;
    }

    public void setLcStaff(String lcStaff) {
        this.lcStaff = lcStaff;
    }

    public String getLcStaff() {
        return lcStaff;
    }

    public void setCtfStatus(String ctfStatus) {
        this.ctfStatus = ctfStatus;
    }

    public String getCtfStatus() {
        return ctfStatus;
    }

    public void setCtfComments(String ctfComments) {
        this.ctfComments = ctfComments;
    }

    public String getCtfComments() {
        return ctfComments;
    }

    public void setXfacCode(String xfacCode) {
        this.xfacCode = xfacCode;
    }

    public String getXfacCode() {
        return xfacCode;
    }

    public void setCtfStage(String ctfStage) {
        this.ctfStage = ctfStage;
    }

    public String getCtfStage() {
        return ctfStage;
    }

    public void setCtfItems(Set<CTFItems> ctfItems) {
        this.ctfItems = ctfItems;
    }

    public Set<CTFItems> getCtfItems() {
        return ctfItems;
    }


}
