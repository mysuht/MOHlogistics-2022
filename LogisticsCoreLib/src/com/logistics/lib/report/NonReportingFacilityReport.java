package com.logistics.lib.report;

import com.logistics.lib.dao.FacilityDAO;
import com.logistics.lib.dao.FacilityTypeDAO;
import com.logistics.lib.dto.Facility;

import com.logistics.lib.dto.FacilityType;

import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import moh.logistics.lib.reports.UtilsClass;
import moh.logistics.lib.reports.code.FacilityTemplate;

public class NonReportingFacilityReport extends UtilsClass {
    public NonReportingFacilityReport(){
        super();
    }
    public List<FacilityTemplate> facilityList(String mon, String year, String dat, String fY, String tY, String fM,
                                               String tM, String hq, String supplierID, String [] centers) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<FacilityTemplate> facilityList
            = new ArrayList<>();
        String sql = " select distinct cm.facility_id, f.fac_city, f.fac_type_id ";
        sql += " from cm, facility f ";
        sql += " where f.facility_id=cm.facility_id ";
        sql += " and cm.ctf_main_id not in ";
        sql += " (select nvl(ci.ctf_main_id,0) from ci) ";
        sql += innerClause(mon, year, dat, fY, tY, fM, tM, hq);
        sql += facilitiesOfSupplier(supplierID, centers);
        sql += " and cm.facility_id  is not null and  f.active=1 and f.isLocked <> 'Y' ";
        System.out.println(sql);
        try {
            pst = jdbcConnection().prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                FacilityTemplate facilityTemplate
                    = new FacilityTemplate();
                facilityTemplate.setFacilityId(rs.getLong("FACILITY_ID"));
                Facility facility = new Facility();
                FacilityDAO facilityDAO 
                    = new FacilityDAO(jdbcConnection());
                facility = facilityDAO.findById(facilityTemplate.getFacilityId());
                facilityTemplate.setFacilityDTO(facility);
                facilityList.add(facilityTemplate);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
            closeConnectionList(rs, pst);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return facilityList;
    }
    
    public static List<FacilityTemplate> getDirectoratesList(String [] directorates){
        return UtilsClass.getDirectoratesList(directorates);
    }
    
    public  FacilityType facilityType(long facilityTypeID) throws SQLException {
        FacilityTypeDAO facilityTypeDAO
            = new FacilityTypeDAO(jdbcConnection());
        return facilityTypeDAO.findById(facilityTypeID);
    }
    
    
    public List<FacilityTemplate> facilityListOuterJoin(String mon, String year, String dat, String fY, String tY, String fM,
                                               String tM, String hq, String supplierID, String [] centers) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<FacilityTemplate> facilityList
            = new ArrayList<>();
        String sql = "select  f.facility_id, f.fac_city, f.fac_type_id, f.active, f.isLocked  \n" + 
        "from facility f inner join \n" + 
        "ctf_main cm\n" + 
        "on f.facility_id = cm.facility_id\n" + 
        "left outer  join ctf_item ci \n" + 
        "on cm.ctf_main_id = ci.ctf_main_id\n" + 
        "where\n" + 
        "nvl(ci.ctf_main_id,0) = 0\n" + 
        "and  f.active=1 \n" + 
        "and f.isLocked <> 'Y' ";
        sql += innerClause(mon, year, dat, fY, tY, fM, tM, hq);
        sql += facilitiesOfSupplier(supplierID, centers);
        System.out.println(sql);
        try {
            pst = jdbcConnection().prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                FacilityTemplate facilityTemplate
                    = new FacilityTemplate();
                facilityTemplate.setFacilityId(rs.getLong("FACILITY_ID"));
                Facility facility = new Facility();
                FacilityDAO facilityDAO 
                    = new FacilityDAO(jdbcConnection());
                facility = facilityDAO.findById(facilityTemplate.getFacilityId());
                facilityTemplate.setFacilityDTO(facility);
                facilityList.add(facilityTemplate);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
            closeConnectionList(rs, pst);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return facilityList;
    }
    
}
