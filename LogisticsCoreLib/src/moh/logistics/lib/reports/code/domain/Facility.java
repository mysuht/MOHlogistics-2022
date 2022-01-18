package moh.logistics.lib.reports.code.domain;

import java.io.Serializable;

public class Facility implements Serializable {
    private long facilityId;
    private String facilityName;
    private String facilityType;
    private String facilityTypeHierarchyId;
    private String facCode;
    private String supCode;
    private String contact;
    private String phone;
    public Facility() {
        super();
    }


    public void setFacilityId(long facilityId) {
        this.facilityId = facilityId;
    }

    public long getFacilityId() {
        return facilityId;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityTypeHierarchyId(String facilityTypeHierarchyId) {
        this.facilityTypeHierarchyId = facilityTypeHierarchyId;
    }

    public String getFacilityTypeHierarchyId() {
        return facilityTypeHierarchyId;
    }

    public void setFacCode(String facCode) {
        this.facCode = facCode;
    }

    public String getFacCode() {
        return facCode;
    }

    public void setSupCode(String supCode) {
        this.supCode = supCode;
    }

    public String getSupCode() {
        return supCode;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
}
