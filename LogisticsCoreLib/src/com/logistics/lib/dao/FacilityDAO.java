package com.logistics.lib.dao;

import com.logistics.lib.dto.Facility;
import com.logistics.lib.util.DataAccessObject;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import moh.logistics.lib.reports.UtilsClass;

public class FacilityDAO extends DataAccessObject<Facility> {
    static final String GET_LIST = " SELECT * FROM FACILITY F ";
    static final String GET_ONE = " SELECT * FROM FACILITY F WHERE FACILITY_ID = ? ";
    static final String GET_FACILITIES_BY_SUPPLIER = " SELECT * FROM FACILITY F WHERE SUP_CODE = ? ";
    public FacilityDAO(Connection connection) {
        super(connection);
    }  
    

    public List<Facility> getFacilityBySupplier(String supplierID, String [] centers){
        List<Facility> facilityList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            String facilities = null;
            String sql = GET_FACILITIES_BY_SUPPLIER;
            if (centers != null) {
                String typeLvl = UtilsClass.getTypeLvl(centers[0]);
                for (int index = 0; index < centers.length; index++) {
                    if (index == 0)
                        facilities = centers[index];
                    else
                        facilities += " , " + centers[index];
                }
                if (typeLvl.equals("3")) {
                    sql += " and ( ";
                    sql += " f.facility_id in (" + facilities + ") ";

                    sql += " ) ";
                }
            }
            pst = connection.prepareStatement(sql);
            pst.setString(1, supplierID);
            rs = pst.executeQuery();
            while(rs.next()){
                Facility facility = fillFacility(rs);
                facilityList.add(facility);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                UtilsClass.closeConnectionList( rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return facilityList;
    }
    
    Facility fillFacility(ResultSet rs) throws  SQLException {
        Facility facility = new Facility();
        facility.setId(rs.getLong("FACILITY_ID"));
        facility.setActive(rs.getString("ACTIVE"));
        facility.setDistributionRole(rs.getString("DISTRIBUTION_ROLE"));
        facility.setFacilityAddress1(rs.getString("FAC_ADDRESS1"));
        facility.setFacilityAddress2(rs.getString("FAC_ADDRESS2"));
        facility.setFacilityCity(rs.getString("FAC_CITY"));
        facility.setFacilityCode(rs.getString("FAC_CODE"));
        facility.setSupplierID(rs.getInt("SUP_CODE"));
        facility.setFacilityContact(rs.getString("FAC_CONTACT"));
        facility.setFacilityFax(rs.getString("FAC_FAX"));
        facility.setFacilityName(rs.getString("FAC_NAME"));
        facility.setFacilityPhone(rs.getString("FAC_PHONE"));
        facility.setFacilityResCode(rs.getString("FAC_RESCODE"));
        facility.setFacilityState(rs.getString("FAC_STATE"));
        facility.setFacilityTypeId(rs.getInt("FAC_TYPE_ID"));
        facility.setFieldDispensed(rs.getString("FIELD_DISPENSE"));
        facility.setHighestLvl(rs.getString("HIGHEST_LV"));
        return facility;
    }
    
    @Override
    public Facility findById(long id) {
        Facility facility = new Facility(); 
        PreparedStatement pst = null; 
        ResultSet rs = null;
        try {
            pst = this.connection.prepareStatement(GET_ONE);
            pst.setLong(1, id);
            pst.executeQuery();
            while(rs.next()){
                facility = fillFacility(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                UtilsClass.closeConnectionList(rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return facility;
    }

    @Override
    public List<Facility> findAll() {
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        List<Facility> facilityList = new ArrayList<>();
        try {
            pst = this.connection.prepareStatement(GET_ONE);
            rs = pst.executeQuery();
            while(rs.next()){
                Facility facility = new Facility();
                facility = fillFacility(rs);
                facilityList.add(facility);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                UtilsClass.closeConnectionList(rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return facilityList;
    }
}
