package org.suht.moh.model;

import java.io.Serializable;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CTF_MAIN", schema = "LOG5")
public class CTFMain implements Serializable {
    @Id
    @Column(name = "CTF_MAIN_ID")
    //@GeneratedValue
    private Long id;
    @Column(name = "FACILITY_ID")
    private Long facilityId;
    @Column(name = "P_DATE")
    private Timestamp pDate;
    @Column(name = "CTF_COMMENTS")
    private String ctfComments;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    public Long getFacilityId() {
        return facilityId;
    }

    public void setPDate(Timestamp pDate) {
        this.pDate = pDate;
    }

    public Timestamp getPDate() {
        return pDate;
    }

    public void setCtfComments(String ctfComments) {
        this.ctfComments = ctfComments;
    }

    public String getCtfComments() {
        return ctfComments;
    }
}
