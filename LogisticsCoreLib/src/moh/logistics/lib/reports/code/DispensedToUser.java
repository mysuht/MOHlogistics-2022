package moh.logistics.lib.reports.code;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import moh.logistics.lib.reports.JDBCConfig;
import moh.logistics.lib.reports.UtilsClass;

import org.springframework.jdbc.core.JdbcTemplate;

public class DispensedToUser {
    public DispensedToUser() {
        super();
    }

    public static String getSupplierId(String facility) {
        return UtilsClass.getSupplier(facility);
    }
    
    public static String getSupplierName(String facility) {
        return UtilsClass.getSupplierName(facility);
    }
    public static int issuesCountry(String[] centers, String mon, String year, String dat, String prod, String fY,
                                    String tY, String fM, String tM, String hq) {
        return UtilsClass.issuesCountry(centers, mon, year, dat, prod, fY, tY, fM, tM, hq);
    }


    public static int dispensedCountry(String[] centers, String mon, String year, String dat, String prod, String fY,
                                    String tY, String fM, String tM, String hq, String type) {
        return UtilsClass.dispensedCountry(centers, mon, year, dat, prod, fY, tY, fM, tM, hq, type);
    }

    public static List<Map<String, Object>> getDirectorates(String[] directorates) {
        return UtilsClass.getDirectorates(directorates);
    }

    public static int issuesFacilitiesOfSupplier(String supplier, String mon, String year, String dat, String prod,
                                                 String fY, String tY, String fM, String tM, String hq,
                                                 String centers[]) {
        return UtilsClass.issuesFacilitiesOfSupplier(supplier, mon, year, dat, prod, fY, tY, fM, tM, hq, centers);
    }

    public static String avgMnthlyCons(String fac, String mon, String year, String prod, String dat, String fY,
                                       String tY, String fM, String tM, String type, String hq) {
        return UtilsClass.avgMnthlyCons(fac, mon, year, prod, dat, fY, tY, fM, tM, type, hq);
    }
    public static List<FacilityTemplate> getDirectoratesList(String[] directorates) {
        return UtilsClass.getDirectoratesList(directorates);
    }
    public static String getProdDose(String prod) {
        return UtilsClass.getProdDose(prod);
    }

    public static String getProdName(String prod) {
        return UtilsClass.getProdName(prod);
    }

    public static String closeBal(String fac, String mon, String year, String prod, String dat, String fY, String tY,
                                  String fM, String tM, String type, String hq) {
        return UtilsClass.closeBal(fac, mon, year, prod, dat, fY, tY, fM, tM, type, hq);
    }

    public static String openBal(String fac, String mon, String year, String prod, String dat, String fY, String tY,
                                 String fM, String tM, String type, String hq) {
        return UtilsClass.openBal(fac, mon, year, prod, dat, fY, tY, fM, tM, type, hq);
    }

    public static String mos(String fac, String mon, String year, String prod, String dat, String fY, String tY,
                             String fM, String tM, String type, String hq) {
        return UtilsClass.mos(fac, mon, year, prod, dat, fY, tY, fM, tM, type, hq);
    }
    
    public static List<FacilityTemplate> dispensedToUserFacilityList(String prod, String mon, String year, String dat,
                                                                     String fY, String tY, String fM, String tM,
                                                                     String hq, String facilities[], String type, String facType) {
       return UtilsClass.dispensedToUserFacilityList(prod, mon, year, dat, fY, tY, fM, tM, hq, facilities, type, facType) ;
                                                                     }

    public static List<FacilityTemplate> facilitiesOfSupplierList(String supplier, String mon, String year,
                                                                     String dat, String prod, String fY, String tY,
                                                                     String fM, String tM, String hq,
                                                                     String centers[]) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = null;
        sql = "select f.fac_name as FACILITY_NAME, ft.fac_name AS factype, f.facility_id AS FACILITY_ID";
        sql += " from facility f,  fac_type ft ";
        sql += " where ";
        //sql += " ci.ctf_main_id=cm.ctf_main_id(+) ";
        //sql += " and cm.facility_id=f.facility_id(+) ";
        sql += "  f.fac_type_id=ft.fac_type_id(+) ";
        //sql += " and ci.prod_id = " + prod;
        //sql += innerClause(mon, year, dat, fY, tY, fM, tM, hq);
        sql += UtilsClass.facilitiesOfSupplier(supplier, centers);
        System.out.println(sql);
        //     System.out.println(sql);
        List<FacilityTemplate> list = new ArrayList();
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                FacilityTemplate template = new FacilityTemplate();
                template.setFacilityName(rs.getString("FACILITY_NAME"));
                template.setFacilityType(rs.getString("factype"));
                template.setFacilityId(rs.getLong("FACILITY_ID"));
                list.add(template);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        
        //     System.out.println(sql);
        return list;
    }
}
