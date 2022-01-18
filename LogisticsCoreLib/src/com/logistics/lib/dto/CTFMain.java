package com.logistics.lib.dto;

import com.logistics.lib.util.DataTransferObject;

import java.util.Date;

public class CTFMain implements DataTransferObject {
    private long id;
    private Long facilityId;
    private String pDate;
    private String ctfComments;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setPDate(String pDate) {
        this.pDate = pDate;
    }

    public String getPDate() {
        return pDate;
    }

    public void setCtfComments(String ctfComments) {
        this.ctfComments = ctfComments;
    }

    public String getCtfComments() {
        return ctfComments;
    }

}
