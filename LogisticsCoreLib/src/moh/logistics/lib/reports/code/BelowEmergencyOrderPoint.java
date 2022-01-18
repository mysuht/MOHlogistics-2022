package moh.logistics.lib.reports.code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import moh.logistics.lib.reports.UtilsClass;
public class BelowEmergencyOrderPoint  {

    
    //JdbcTemplate jdbcTemplate = JDBCTemplateInfo.DataSourceJdbcTempateInfo();

    public static ResultSet getFacTypesMainName(String fac) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "select fac_type_id, ";
            sql += " fac_name, type_hierarchy ";
            sql += " from fac_type where fac_type_id=" + fac;
            if ( Integer.parseInt(fac) > 100 ){
        sql =
            "select grp_id, grp_desc from\n" + 
            " fac_type ft, facility f, group_of_facilities gof, groups g \n" + 
            " \n" + 
            " where ft.fac_type_id = f.fac_type_id and ft.fac_type_id = gof_facility_id and \n" + 
            " \n" + 
            " gof.gof_grp_id = g.grp_id and gof.gof_grp_id= " +
            
            (Integer.parseInt(fac) - 100) + " order by 2";
            }
            System.out.println(sql);
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery(sql); 
            if(!rs.next()){
                String sqlGroup = "select ft.fac_type_id, ";
                    sqlGroup += " ft.fac_name, ft.type_hierarchy ";
                  sqlGroup+= " from fac_type ft " ;
                  sqlGroup+= " , facility f ";
                  sqlGroup+= " where ";
                  //sqlGroup += "  ft.fac_type_id = f.fac_type_id ";
                  //sql+Group += " and ";
                  sqlGroup += "  ft.fac_type_id in ";
                  sqlGroup += " (select gof_facility_id from group_of_facilities ";
                  sqlGroup+= " where gof_grp_id="+(Integer.parseInt(fac)-100)+" ) " ;
                 // sqlGroup += " order by 2 ";
                  System.out.println(sqlGroup);
                pst = conn.prepareStatement(sqlGroup);
                rs = pst.executeQuery(); 
            }else{
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery(); 
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }
    
    public static String getDate() {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "select to_char(sysdate,'dd-MM-yyyy') from dual ";
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            e.printStackTrace();
            return "non";
        }finally{
            try {
                UtilsClass.closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
    }

    public static ResultSet getDirectorates() {
Connection conn = null;
ResultSet rs = null;
PreparedStatement pst = null;
        String sql =
            "select f.facility_id , f.fac_name from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id(+) and type_hierarchy in (1,2) order by 2";
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }
    
    public static ResultSet getProducts() {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            String sql = "select prod_id, pro_name, pro_dose from product order by 2";
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }

        return rs;
    }

    public static String getTime() {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "select to_char(sysdate,'hh24:mi:ss') from dual ";
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return "non";
        }finally{
            try {
                UtilsClass.closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    
//    public List<Map<String, Object>> AllFacilities(String prod, String mon, String year, String dat, String fY,
//                                                           String tY, String fM, String tM, String type, String hq,
//                                                           String facilities[]) {
//        String query = UtilsClass.AllFacilityQuery( prod,  mon,  year,  dat,  fY,
//                                                            tY,  fM,  tM,  type,  hq,
//                                                            facilities);
//        
//        List<Map<String, Object>> results =
//        jdbcTemplate.queryForList(query);
//        return results;
//    }
    
    public List<FacilityTemplate> AllFacilitiesXXX(String prod, String mon, String year, String dat, String fY,
                                                           String tY, String fM, String tM, String type, String hq,
                                                           String facilities[]) {
        return UtilsClass.AllFacilityQueryXXX( prod,  mon,  year,  dat,  fY,
                                                            tY,  fM,  tM,  type,  hq,
                                                            facilities);
    }

//    public List<Map<String, Object>> AllFacilities(String prod, String mon, String year, String dat, String fY,
//                                                           String tY, String fM, String tM, String type, String hq,
//                                                           String facilities[]) {
//        String query = UtilsClass.AllFacilityQuery( prod,  mon,  year,  dat,  fY,
//                                                            tY,  fM,  tM,  type,  hq,
//                                                            facilities);
//        
//        List<Map<String, Object>> results =
//        jdbcTemplate.queryForList(query);
//        return results;
//    }
    
    public static String mos(String fac, String mon, String year, String prod, String dat, String fY, String tY,
                             String fM, String tM, String type, String hq) {
        return UtilsClass.mosBelow(fac, mon, year, prod, dat, fY, tY, fM, tM, type, hq);
    }
    
    public static String avgMnthlyCons(String fac, String mon, String year, String prod, String dat, String fY,
                                       String tY, String fM, String tM, String type, String hq) {
        return UtilsClass.avgMnthlyCons(fac, mon, year, prod, dat, fY, tY, fM, tM, type, hq);
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
    
    
    public static String getDirName(String dir) {
       return UtilsClass.getDirName(dir);
    }
    
    public static String getDirCode(String dir) {
       return UtilsClass.getDirCode(dir);
    }
    
    public static int FacilityLVL(String dir) {
        return UtilsClass.FacilityLVL(dir);
    }
}
