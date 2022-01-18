package moh.logistics.lib.reports.code;

import java.io.Serializable;

import java.math.BigDecimal;

import moh.logistics.lib.reports.code.domain.Facility;

public class FacilityTemplate implements Serializable {
    @SuppressWarnings("compatibility:5517652435247901073")
    private static final long serialVersionUID = 1L;
    private long facilityId;
    private String facilityName;
    private String facilityType;
    private String newUsers;
    private String contUsers;
    private String newUsersCountry;
    private String contUsersCountry;
    private com.logistics.lib.dto.Facility facilityDTO;

    public void setNewUsersCountry(String newUsersCountry) {
        this.newUsersCountry = newUsersCountry;
    }

    public String getNewUsersCountry() {
        return newUsersCountry;
    }

    public void setContUsersCountry(String contUsersCountry) {
        this.contUsersCountry = contUsersCountry;
    }

    public String getContUsersCountry() {
        return contUsersCountry;
    }

    public void setNewUsers(String newUsers) {
        this.newUsers = newUsers;
    }

    public String getNewUsers() {
        return newUsers;
    }

    public void setContUsers(String contUsers) {
        this.contUsers = contUsers;
    }

    public String getContUsers() {
        return contUsers;
    }

    public void setFacilityCode(String facilityCode) {
        this.facilityCode = facilityCode;
    }

    public String getFacilityCode() {
        return facilityCode;
    }
    private String facilityCode;
    private String facilityTypeHierarchyId;
    private String supCode;
    private String supplierName;
    private String dispensed;
    private String dispensedSupplier;

    public void setDispensedSupplier(String dispensedSupplier) {
        this.dispensedSupplier = dispensedSupplier;
    }

    public String getDispensedSupplier() {
        return dispensedSupplier;
    }
    private String program;
    private String avg;
    private String openningBal;
    private String closingBal;
    private String issues;
    private String receipts;
    private String adjustments;
    private String facCode;
    private double monthOfSupplier;
    private Facility facility;

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setMonthOfSupplier(double monthOfSupplier) {
        this.monthOfSupplier = monthOfSupplier;
    }

    public double getMonthOfSupplier() {
        return monthOfSupplier;
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

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierName() {
        return supplierName;
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

    public void setSupCode(String supCode) {
        this.supCode = supCode;
    }

    public String getSupCode() {
        return supCode;
    }

    public void setDispensed(String dispensed) {
        this.dispensed = dispensed;
    }

    public String getDispensed() {
        return dispensed;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getProgram() {
        return program;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getAvg() {
        return avg;
    }

    public void setOpenningBal(String openningBal) {
        this.openningBal = openningBal;
    }

    public String getOpenningBal() {
        return openningBal;
    }

    public void setClosingBal(String closingBal) {
        this.closingBal = closingBal;
    }

    public String getClosingBal() {
        return closingBal;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    public String getIssues() {
        return issues;
    }

    public void setReceipts(String receipts) {
        this.receipts = receipts;
    }

    public String getReceipts() {
        return receipts;
    }

    public void setAdjustments(String adjustments) {
        this.adjustments = adjustments;
    }

    public String getAdjustments() {
        return adjustments;
    }

    public FacilityTemplate() {
        super();
    }

    public void setFacilityDTO(com.logistics.lib.dto.Facility facilityDTO) {
        this.facilityDTO = facilityDTO;
    }

    public com.logistics.lib.dto.Facility getFacilityDTO() {
        return facilityDTO;
    }
    
    @Override()
    public boolean equals(Object obj) {
        // This is unavoidable, since equals() must accept an Object and not something more derived
        if (obj instanceof FacilityTemplate) {
            // Note that I use equals() here too, otherwise, again, we will check for referential equality.
            // Using equals() here allows the Model class to implement it's own version of equality, rather than
            // us always checking for referential equality.
            FacilityTemplate ft = (FacilityTemplate) obj;
            return ft.getFacilityId() == this.getFacilityId();
        }

        return false;
    } 

}
