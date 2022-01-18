package moh.logistics.lib.reports.code;


public class FacilityDispensed {
    private String facilityId;
    private String supplierId;

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierId() {
        return supplierId;
    }
    private String productId;
    private String facilityDispensed;
    private String productName;

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
    private String productDose;

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setFacilityDispensed(String facilityDispensed) {
        this.facilityDispensed = facilityDispensed;
    }

    public String getFacilityDispensed() {
        return facilityDispensed;
    }

    public void setSupplierDispensed(String supplierDispensed) {
        this.supplierDispensed = supplierDispensed;
    }

    public String getSupplierDispensed() {
        return supplierDispensed;
    }

    public void setFacBySuppDispensed(String facBySuppDispensed) {
        this.facBySuppDispensed = facBySuppDispensed;
    }

    public String getFacBySuppDispensed() {
        return facBySuppDispensed;
    }
    private String supplierDispensed;
    private String facBySuppDispensed;
}
