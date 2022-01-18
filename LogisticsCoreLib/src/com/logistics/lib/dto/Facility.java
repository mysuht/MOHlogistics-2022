package com.logistics.lib.dto;

import com.logistics.lib.util.DataTransferObject;

public class Facility implements DataTransferObject {
    private long id;
    private String facilityName;
    private int facilityTypeId;
    private String facilityCode;
    private int supplierID;
    private String facilityContact;
    private String facilityAddress1;
    private String facilityAddress2;
    private String facilityCity;
    private String facilityState;
    private String facilityResCode;
    private String facilityPhone;
    private String facilityFax;
    private String fieldDispensed;
    private String maxMOS;
    private String minMOS ;
    private String active;
    private String warehouse;
    private String highestLvl;
    private String distributionRole;
    private String isLocked;
    private String lockStartDate;
    private String lockEndtDate;
    
    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
    }

    public String getFacilityCode() {
        return facilityCode;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setFacilityContact(String facilityContact) {
        this.facilityContact = facilityContact;
    }

    public String getFacilityContact() {
        return facilityContact;
    }

    public void setFacilityAddress1(String facilityAddress1) {
        this.facilityAddress1 = facilityAddress1;
    }

    public String getFacilityAddress1() {
        return facilityAddress1;
    }

    public void setFacilityAddress2(String facilityAddress2) {
        this.facilityAddress2 = facilityAddress2;
    }

    public String getFacilityAddress2() {
        return facilityAddress2;
    }

    public void setFacilityCity(String facilityCity) {
        this.facilityCity = facilityCity;
    }

    public String getFacilityCity() {
        return facilityCity;
    }

    public void setFacilityState(String facilityState) {
        this.facilityState = facilityState;
    }

    public String getFacilityState() {
        return facilityState;
    }

    public void setFacilityResCode(String facilityResCode) {
        this.facilityResCode = facilityResCode;
    }

    public String getFacilityResCode() {
        return facilityResCode;
    }

    public void setFacilityPhone(String facilityPhone) {
        this.facilityPhone = facilityPhone;
    }

    public String getFacilityPhone() {
        return facilityPhone;
    }

    public void setFacilityFax(String facilityFax) {
        this.facilityFax = facilityFax;
    }

    public String getFacilityFax() {
        return facilityFax;
    }

    public void setFieldDispensed(String fieldDispensed) {
        this.fieldDispensed = fieldDispensed;
    }

    public String getFieldDispensed() {
        return fieldDispensed;
    }

    public void setMaxMOS(String maxMOS) {
        this.maxMOS = maxMOS;
    }

    public String getMaxMOS() {
        return maxMOS;
    }

    public void setMinMOS(String minMOS) {
        this.minMOS = minMOS;
    }

    public String getMinMOS() {
        return minMOS;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getActive() {
        return active;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setHighestLvl(String highestLvl) {
        this.highestLvl = highestLvl;
    }

    public String getHighestLvl() {
        return highestLvl;
    }

    public void setDistributionRole(String distributionRole) {
        this.distributionRole = distributionRole;
    }

    public String getDistributionRole() {
        return distributionRole;
    }

    public void setIsLocked(String isLocked) {
        this.isLocked = isLocked;
    }

    public String getIsLocked() {
        return isLocked;
    }

    public void setLockStartDate(String lockStartDate) {
        this.lockStartDate = lockStartDate;
    }

    public String getLockStartDate() {
        return lockStartDate;
    }

    public void setLockEndtDate(String lockEndtDate) {
        this.lockEndtDate = lockEndtDate;
    }

    public String getLockEndtDate() {
        return lockEndtDate;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityTypeId(int facilityTypeId) {
        this.facilityTypeId = facilityTypeId;
    }

    public int getFacilityTypeId() {
        return facilityTypeId;
    }
}
