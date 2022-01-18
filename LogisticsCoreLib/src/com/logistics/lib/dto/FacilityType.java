package com.logistics.lib.dto;

import com.logistics.lib.util.DataTransferObject;

public class FacilityType implements DataTransferObject {
    private long id;
    private String facilityTypeName;
    private int typeHierarchy;


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setFacilityTypeName(String facilityTypeName) {
        this.facilityTypeName = facilityTypeName;
    }

    public String getFacilityTypeName() {
        return facilityTypeName;
    }

    public void setTypeHierarchy(int typeHierarchy) {
        this.typeHierarchy = typeHierarchy;
    }

    public int getTypeHierarchy() {
        return typeHierarchy;
    }
}
