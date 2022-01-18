package moh.logistics.lib.reports;

import com.logistics.lib.util.DataAccessObject;
import com.logistics.lib.util.DatabaseConnectionManager;

import java.math.BigDecimal;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.Map;

import moh.logistics.lib.reports.code.FacilityDispensed;
import moh.logistics.lib.reports.code.FacilityDispensedToUserReport;
import moh.logistics.lib.reports.code.FacilityTemplate;

import moh.logistics.lib.reports.code.domain.Facility;

import oracle.jdbc.OracleDriver;

import oracle.sql.ARRAY;

import oracle.sql.ArrayDescriptor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UtilsClass {
    
    public static List<FacilityTemplate> facilityOfSupplierList(String prod, String mon, String year, String dat,
                                                                     String fY, String tY, String fM, String tM,
                                                                     String hq, String facilities[], String type) {
        List<FacilityTemplate> mainList = new ArrayList();
                            List<FacilityTemplate> supplyStatusFacilityList  
                                = facilitiesCTFMainList(mon, year, dat, fY, tY, fM, tM, hq, type);
                            for(int i = 0; i < supplyStatusFacilityList.size(); i++){                                
                                for(int j = 0; j < facilitiesCTFMainList(mon, year, dat, fY, tY, fM, tM, hq, type).size(); j++){
                                    if(supplyStatusFacilityList.get(i).getFacilityId() == facilitiesCTFMainList(mon, year, dat, fY, tY, fM, tM, hq, type).get(j).getFacilityId()){
                                        FacilityTemplate facility = new FacilityTemplate();
                                        facility = supplyStatusFacilityList.get(i);
                                        mainList.add(facility);
                                        break;
                                    }
                                } 
                              
                            }
                            System.out.println(" mainListSize : " + mainList.size());
        return mainList;
    }
    public static List<FacilityTemplate> facilitiesCTFMainList(String mon, String year, String dat, String fY, String tY,
                                          String fM, String tM, String hq, String SupplierID){
        List<FacilityTemplate> facilityList = new ArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;   
        String sql = "SELECT CM.FACILITY_ID FROM CTF_MAIN CM, Facility F ";
        sql += " WHERE 1 = 1 ";
        sql += " AND F.FACILITY_ID = CM.FACILITY_ID ";
        sql += " AND ( ";
        sql += " F.SUP_CODE = " +SupplierID;
        sql += " OR F.FACILITY_ID = " + getSupplier(SupplierID);
        sql += " ) ";
        String innerClause = innerClause(mon, year, dat, fY, tY, fM, tM, hq);
        sql += innerClause;
        System.out.println(sql);
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                FacilityTemplate FacilityTemplate = new FacilityTemplate();
                FacilityTemplate.setFacilityId(rs.getLong(1));
                facilityList.add(FacilityTemplate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
            return facilityList ;
    }
    
    public static boolean facilitiesCTFMain(String prod, String mon, String year, String dat, String fY, String tY,
                                          String fM, String tM, String hq, String facilityId){
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean value = false;
    
        String sql = "SELECT count(*) FROM CTF_MAIN CM, Facility F, CTF_ITEM CI ";
        sql += " WHERE 1 = 1 ";
        sql += " AND CI.PROD_ID = " + prod;
        sql += " AND CI.CTF_MAIN_ID = CM.CTF_MAIN_ID ";
        sql += " and CM.FACILITY_ID = F.FACILITY_ID ";
        sql += " AND CM.FACILITY_ID = " + facilityId ;
        
        String innerClause = innerClause(mon, year, dat, fY, tY, fM, tM, hq);
        sql += innerClause;
        //System.out.println(sql);
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //int i = 0;
            if(rs.next()){
                if(rs.getInt(1) > 0)
             value = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
            return value ;
    }



    /*
     * Connection Info
     */

    JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
    /*
     * Main Methods Classes
     */
    
    public static String getDate() {
        String sql = "select to_char(sysdate,'dd-MM-yyyy') from dual ";
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String result = null;
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                result = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String getTime() {
        String sql = "select to_char(sysdate,'hh24:mi:ss') from dual ";
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String result = null;
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                result = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    public static FacilityTemplate getFacilityInfo(String facilityId){
        System.out.println("************************ FACILITY :  "+ facilityId + " *************************");
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select f.facility_id as facilityId ";
        sql += " , f.fac_name as facilityName ";
        sql += " , FT.FAC_NAME AS facilityType \n";
        sql += " , nvl(f.sup_code,0) AS supCode \n";
        sql += " FROM FACILITY f, FAC_TYPE FT ";
        sql += " WHERE FT.FAC_TYPE_ID = f.FAC_TYPE_ID and f.facility_id = " + facilityId;
        FacilityTemplate facility = new FacilityTemplate();
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                facility.setFacilityId(Long.parseLong(facilityId));
                facility.setFacilityName(rs.getString("facilityName"));
                facility.setFacilityType(rs.getString("facilityType"));
                facility.setSupCode(rs.getString("supCode"));
                facility.setDispensed("0");
                facility.setProgram("0");                   
                facility.setAvg("0");
                    facility.setOpenningBal("0");
                    facility.setClosingBal("0");
                    facility.setIssues("0");
                    facility.setReceipts("0");
                    facility.setAdjustments("0");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return facility;
    }
    
    public static List<FacilityTemplate> supplyStatusFacilityList(String prod, String mon, String year, String dat,
                                                                     String fY, String tY, String fM, String tM,
                                                                     String hq, String facilities[], String type) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String faces = " ";
        if (facilities != null) {
            for (int i = 0; i < facilities.length; i++) {
                if (i == 0) {
                    faces = facilities[i];
                } else {
                    faces += ", " + facilities[i];
                }
            }
            int [] intFacilities = new int [facilities.length];
            for (int k=0; k < facilities.length; k++){
                intFacilities[k] = Integer.parseInt(facilities[k]);
            }
        }
        String sql = "";
        sql = "select f.facility_id as facilityId ";
        sql += " , f.fac_name as facilityName ";
        sql += " , FT.FAC_NAME AS facilityType \n";
        sql += " , nvl(f.sup_code,0) AS supCode \n";
        sql += " , dispensed(" + prod + ", f.facility_id, '" + mon + "', '" + year + "', '" +
            dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "', '"+type+"' ) as dispensed \n" +
            ", percentDispensed(" + prod + " , f.facility_id, '" + mon + "', '" + year + "', '" + dat + "', '" + fY +
            "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "', '"+type+"' ) as program\n " +
                "  , avgmonval(f.facility_id, '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY + "', '" + tY + "', '" +
                fM + "', '" + tM + "','0', '" + hq + "') as avg\n" +
                "  , openbalval(f.facility_id, '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY
                + "', '" + tY + "', '"
                +    fM + "', '" + tM + "','0', '" + hq + "') as openBal\n" +
                "  , closebalval(f.facility_id, '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY + "', '" + tY + "', '" +
                fM + "', '" + tM + "', '0', '" + hq + "' ) as closingBal\n"
                + " , issues( '" + prod + "', f.facility_id, '" + mon +
                "', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as issues\n" +
                "  , receipts( '" + prod + "', f.facility_id, '" + mon +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as receipts\n" +    
                "  , adjustments( '" + prod + "', f.facility_id, '" + mon  +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as adjustments\n" ;
        sql += "  , isFacilityDataEntered( '" + prod + "', f.facility_id, '" + mon  +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as isFacilityDataEntered \n" ;    
        sql += " from facility f, fac_type ft\n" + " where f.fac_type_id = ft.fac_type_id \n";
            if (facilities != null) {
                if (getTypeLvl(facilities[0]).equals("2")) {
                    sql += " and ( ";
                    sql += " ( f.sup_code in ( " + faces + " ) or f.facility_id in ( " + faces + " ) ) \n";
                    sql += " and ( f.sup_code = " + type + "  ) ";
                    sql += " ) ";
                    sql += " or ( f.facility_id = " + type + " or f.sup_code = " + type + " ) ";
                } else if (getTypeLvl(facilities[0]).equals("3")) {
                    if(type.toString().equals("498")){
                        sql += " and ( ";
                        sql += " ( f.facility_id = " + type + " ) or f.sup_code = " + type + " ) ";
                    }else{
                        sql += " and ( f.facility_id in ( " + faces + " ) and f.sup_code = " + type + " ) \n";
                    sql += " or ( f.facility_id = " + type + " ) ";
                    }
                }

            } else {
                sql += " or ( ";
                sql += " f.facility_id = " + type ;
                //sql += facilitiesCTFMain(prod, mon, year, dat, fY, tY, fM, tM, hq, type);
                sql += " or f.sup_code = " + type ;
                sql += " ) ";
            }
       


        sql +=
            "start with sup_code is null\n" + "connect by prior f.facility_id = f.sup_code \n" +
            "order SIBLINGS BY f.fac_name";
        System.out.println(sql);
        List<FacilityTemplate> facilityList = new ArrayList();
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {                
                if(rs.getInt("isFacilityDataEntered") > 0){
                    FacilityTemplate facility = new FacilityTemplate();
                    facility.setFacilityId(rs.getLong("facilityId"));
                    facility.setFacilityName(rs.getString("facilityName"));
                    facility.setFacilityType(rs.getString("facilityType"));
                    facility.setSupCode(rs.getString("supCode"));
                    facility.setDispensed(rs.getString("dispensed"));
                    facility.setProgram(rs.getString("program"));                   
                    facility.setAvg(
                    rs.getInt("avg") < 0 ? "0" : rs.getString("avg")
                    );
                        facility.setOpenningBal(rs.getString("openBal"));
                        facility.setClosingBal(rs.getString("closingBal"));
                        facility.setIssues(rs.getString("issues"));
                        facility.setReceipts(rs.getString("receipts"));
                        facility.setAdjustments(rs.getString("adjustments"));
                facilityList.add(facility);
                }
                
            }
            boolean SuppExist = false;
            for(FacilityTemplate ft: facilityList){
                if( (ft.getFacilityId()+"").equals(type)){
                    SuppExist = true;
                    System.out.println("^^^^^^^^^^^^^^^^^^^ The Supplier Found &^^^^^^^^^^^^^^^^^ :" + type);
                    break;
                }
            }
            
            if(!SuppExist){                
                facilityList.add(0, getFacilityInfo(type));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return facilityList;
    }

    public static String getTypeLvl(String facility) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "select typelvl( " + facility + " ) from dual ";
        //System.out.println(sql);
            
                String value = "";

        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            
            if(rs.next()){
                value = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return value;

        //return facilityLvl;
    }

    public static String getFacTypeLvl(String facility) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "select facTypelvl( " + facility + " ) from dual ";
        System.out.println(sql);
            
                String value = "";

        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            
            if(rs.next()){
                value = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return value;

        //return facilityLvl;
    }


    public static String getSupplier(String facility) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String value = "";
        String sql = "select sup_code from facility where facility_id = " + facility;
        //String facilityLvl = jdbcTemplate.queryForObject(sql, String.class);
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                value = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return value;

        //return facilityLvl;
    }
    
    public static String getSupplierCode(String facility) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String value = "";
        String sql = "select supplier.fac_code ";
            sql += " from facility f, facility supplier ";
            sql += " where supplier.facility_id = f.sup_code ";
            sql += " and f.facility_id = " + facility;
        //String facilityLvl = jdbcTemplate.queryForObject(sql, String.class);
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                value = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return value;

        //return facilityLvl;
    }
    
    public static String getSupplierName(String facility) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "select fac_name from facility where facility_id = " + getSupplier(facility);
        //String facilityLvl = jdbcTemplate.queryForObject(sql, String.class);
        String value = "";
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next())
            value = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return value;
        
        //return facilityLvl;
    }
    
    public static String getFacilityName(String facility) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "select fac_name from facility where facility_id = " + facility;
        //String facilityLvl = jdbcTemplate.queryForObject(sql, String.class);
        String value = "";
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next())
            value = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return value;
        
        //return facilityLvl;
    }
    
    public static String getFacilityCode(String facility) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "select fac_code from facility where facility_id = " + facility;
        //String facilityLvl = jdbcTemplate.queryForObject(sql, String.class);
        String value = "";
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next())
            value = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return value;
        
        //return facilityLvl;
    }
   
    public static int dispensedDirectorate(String prod, String mon, String year, String dat,
                                                                     String fY, String tY, String fM, String tM,
                                                                     String hq, String facility, String type) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = " select ";
        sql += "  dispensed(" + prod + ", '" + facility + "' , '" + mon + "', '" + year + "', '" +
            dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "', '"+type+"' ) as dispensed \n ";
        sql += " from dual ";
        int result = 0;
        try {
            System.out.println(sql);
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            result = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public static String programProductDisepensedQuery(String supplier, String product){
        String sql = "";
        sql += " select distinct( f.facility_id) fff ";
        sql += " from facility f, ctf_main cm, ctf_item ci ";
        sql += " where cm.ctf_main_id(+)=ci.ctf_main_id ";
        sql += " and cm.facility_id=f.facility_id  ";
        sql += " and f.sup_code = " + supplier;
        sql += " and ci.prod_id=" + product;
        sql += " and ci.issues > 0";
        return sql;
    }
    
    
    public static String facilityProductDisepensedQuery(String supplier, String product, String centers[]){
        String sql = "";
        sql += " select distinct( f.facility_id) facility_id, f.fac_code, f.fac_name ";
        sql += " from facility f, ctf_main cm, ctf_item ci ";
        sql += " where cm.ctf_main_id(+)=ci.ctf_main_id ";
        sql += " and cm.facility_id=f.facility_id  ";
        sql += " and ci.prod_id=" + product;
        sql += UtilsClass.facilitiesOfSupplier(supplier, centers);
        sql += " and ci.issues > 0";
        return sql;
    }
    
    
    public static List<FacilityTemplate> getFacilityDispensedRepAllIntersect(String quart, String year, String prod[], String dat, String fY,
                                                   String tY, String fM, String tM, String type, String hq, String facSup, String [] centers) {
            int result = 0;
            Connection conn = null;
            PreparedStatement pst = null;
            ResultSet rs = null;
            String sql = "";
            for (int kl = 0; kl < prod.length; kl++) {
                 String innerClause = UtilsClass.innerClause(quart, year, dat, fY, tY, fM, tM, hq);
                if (kl == 0) {
                    sql += UtilsClass.facilityProductDisepensedQuery(facSup, prod[kl], centers);
                    sql += innerClause;
                } else {
                    sql += " intersect " ;
                    sql += UtilsClass.facilityProductDisepensedQuery(facSup, prod[kl], centers);
                    sql += innerClause;
                }
             }
             List<FacilityTemplate> facilityList = new ArrayList<>(); 
             System.out.println(sql);
            try {
                conn = UtilsClass.jdbcConnection();
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while(rs.next()){
                    FacilityTemplate facility = new FacilityTemplate();
                    facility.setFacCode(rs.getString("FAC_CODE"));
                    facility.setFacilityId(rs.getLong("FACILITY_ID"));
                    facility.setFacilityName(rs.getString("FAC_NAME"));
                    facilityList.add(facility);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                try {
                    UtilsClass.closeConnectionList(conn, rs, pst);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return facilityList ;
         }
    
    public int getProgDispensedRepAllIntersect(String quart, String year, String prod[], String dat, String fY,
                                               String tY, String fM, String tM, String type, String hq, String facSup) {

        String sql = "";
        for (int kl = 0; kl < prod.length; kl++) {
             String innerClause = UtilsClass.innerClause(quart, year, dat, fY, tY, fM, tM, hq);
            if (kl == 0) {
                sql += UtilsClass.programProductDisepensedQuery(facSup, prod[kl]);
                sql += innerClause;
            } else {
                sql += " intersect " ;
                sql += UtilsClass.programProductDisepensedQuery(facSup, prod[kl]);
                sql += innerClause;
            }
         }
         sql = "select count(fff) from (" + sql + " )";
         System.out.println(sql);
        return (Integer) getOneValue(sql);
     }
    public static List<FacilityTemplate> getDirectoratesList(String mon, String year,
                                                                     String dat, String prod, String fY, String tY,
                                                                     String fM, String tM, String hq,
                                                                     String directorates[]) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        //JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
        String centersCollection = "";

        String sql = "select f.facility_id AS FACILITY_ID ";
        sql += " , f.fac_name AS FACILITY_NAME, ft.fac_type_id ";
        sql += " , ft.fac_name as factype ";
            sql += " from facility f, fac_type ft ";
            sql += " where f.fac_type_id = ft.fac_type_id \n" ;
            sql += "and type_hierarchy in (1, 2)  ";
        if (directorates != null) {
            String typeLvl = UtilsClass.getTypeLvl(directorates[0]);
            for (int index = 0; index < directorates.length; index++) {
                if (index == 0)
                    centersCollection = directorates[index];
                else
                    centersCollection += " , " + directorates[index];
            }
            if (typeLvl.equals("2") || typeLvl.equals("1")) {
                sql += " and ( ";
                sql += " f.facility_id in (" + centersCollection + ") ";
                sql += " or ft.type_hierarchy = 1  ) ";
            } else if (typeLvl.equals("3")) {
                sql += " and ( ";
                sql +=
                    " f.facility_id in ( select sup_code from facility where facility_id in (" + centersCollection +
                    ") or ft.type_hierarchy = 1  ";
                sql += " ) ";
                sql += " ) ";
            }
            sql += " ";
        }
        sql += " order by 2";
       System.out.println(sql);
        List<FacilityTemplate> results = new ArrayList();
        
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                FacilityTemplate template = new FacilityTemplate();
                template.setFacilityId(rs.getLong("FACILITY_ID"));
                template.setFacilityName(rs.getString("FACILITY_NAME"));
                template.setFacilityType(rs.getString("factype"));
                template.setSupCode( getSupplier(template.getFacilityId() + ""));
                template.setSupplierName(getSupplierName(template.getFacilityId() + ""));
                template.setDispensed(
                FacilityProductDispensedList(template.getFacilityId()+"", mon, year, dat, prod, fY, tY, fM, tM, hq, directorates).size()+""
                    );
                results.add(template);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //   System.out.println(sql);
        // List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        return results;
    }
    
    
    
    public static List<FacilityTemplate> getDirectoratesList(String[] directorates) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        //JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
        String centersCollection = "";

        String sql = "select f.facility_id AS FACILITY_ID ";
        sql += " , f.fac_name AS FACILITY_NAME, ft.fac_type_id ";
        sql += " , ft.fac_name as factype, f.fac_code as CODE, ft.type_hierarchy ";
            sql += " from facility f, fac_type ft ";
            sql += " where f.fac_type_id = ft.fac_type_id \n" ;
            sql += "and type_hierarchy in (1, 2)  ";
        if (directorates != null) {
            String typeLvl = UtilsClass.getTypeLvl(directorates[0]);
            for (int index = 0; index < directorates.length; index++) {
                if (index == 0)
                    centersCollection = directorates[index];
                else
                    centersCollection += " , " + directorates[index];
            }
            if (typeLvl.equals("2") || typeLvl.equals("1")) {
                sql += " and ( ";
                sql += " f.facility_id in (" + centersCollection + ") ";
                sql += " or ft.type_hierarchy = 1  ) ";
            } else if (typeLvl.equals("3")) {
                sql += " and ( ";
                sql +=
                    " f.facility_id in ( select sup_code from facility where facility_id in (" + centersCollection +
                    ") or ft.type_hierarchy = 1  ";
                sql += " ) ";
                sql += " ) ";
            }
            sql += " ";
        }
        sql += " order by 2";
       System.out.println(sql);
        List<FacilityTemplate> results = new ArrayList();
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                FacilityTemplate template = new FacilityTemplate();
                template.setFacilityId(rs.getLong("FACILITY_ID"));
                template.setFacilityName(rs.getString("FACILITY_NAME"));
                template.setFacilityType(rs.getString("factype"));
                template.setSupCode( getSupplier(template.getFacilityId() + ""));
                template.setFacCode(rs.getString("CODE"));
                template.setFacilityTypeHierarchyId("TYPE_HIERARCHY");
                template.setSupplierName(getSupplierName(template.getFacilityId() + ""));
                results.add(template);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //   System.out.println(sql);
        // List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        return results;
    }
    
    public static String GETFacilityTypeID(Connection connection, String facilityID){
        String typeHierarchy = null;
        String sql = " SELECT TYPELVL(?) FROM DUAL ";
        try {          
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, facilityID);
            ResultSet rs = pst.executeQuery();
            rs.next();
            typeHierarchy =  rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeHierarchy;
    }
    
    
    public static List<FacilityTemplate> getDirectoratesList2ndLVL(String[] directorates) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        //JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
        String centersCollection = "";

        String sql = "select f.facility_id AS FACILITY_ID ";
        sql += " , f.fac_name AS FACILITY_NAME, ft.fac_type_id ";
        sql += " , ft.fac_name as factype ";
            sql += " from facility f, fac_type ft ";
            sql += " where f.fac_type_id = ft.fac_type_id \n" ;
            sql += "and type_hierarchy in ( 2)  ";
        if (directorates != null) {
            String typeLvl = UtilsClass.getTypeLvl(directorates[0]);
            for (int index = 0; index < directorates.length; index++) {
                if (index == 0)
                    centersCollection = directorates[index];
                else
                    centersCollection += " , " + directorates[index];
            }
            if (typeLvl.equals("2") || typeLvl.equals("1")) {
                sql += " and ( ";
                sql += " f.facility_id in (" + centersCollection + ") ";
                //sql += " or ft.type_hierarchy = 1 ";
                sql += " ) ";
            } else if (typeLvl.equals("3")) {
                sql += " and ( ";
                sql += " f.facility_id in ( select sup_code from facility where facility_id in (" + centersCollection +" ) ";
                //sql += "  or ft.type_hierarchy = 1  ";
                sql += " ) ";
                sql += " ) ";
            }
            sql += " ";
        }
        sql += " order by 2";
       System.out.println(sql);
        List<FacilityTemplate> results = new ArrayList();
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                FacilityTemplate template = new FacilityTemplate();
                template.setFacilityId(rs.getLong("FACILITY_ID"));
                template.setFacilityName(rs.getString("FACILITY_NAME"));
                template.setFacilityType(rs.getString("factype"));
                template.setSupCode( getSupplier(template.getFacilityId() + ""));
                template.setFacCode("CODE");
                template.setSupplierName(getSupplierName(template.getFacilityId() + ""));
                results.add(template);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //   System.out.println(sql);
        // List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        return results;
    }
    
    
    public static List<FacilityTemplate> getFacilitiesList(String[] facilities) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String centersCollection = "";

        String sql = "select f.facility_id AS FACILITY_ID ";
        sql += " , f.fac_name AS FACILITY_NAME, ft.fac_type_id ";
        sql += " , ft.fac_name as factype ";
        sql += " from facility f, fac_type ft ";
        sql += " where f.fac_type_id = ft.fac_type_id \n" ;
        if(facilities == null){
        sql += "and type_hierarchy in (1, 2, 3)  ";
        }else{
            String typeLvl = UtilsClass.getTypeLvl(facilities[0]);
            String suppliers = null;
            for (int index = 0; index < facilities.length; index++) {
                if (index == 0)
                    centersCollection = facilities[index];
                else
                    centersCollection += " , " + facilities[index];
                
                
                if (index == 0)
                    suppliers = getSupplier(facilities[index]);
                else
                    suppliers += " , " + getSupplier(facilities[index]);
            }
            if (typeLvl.equals("2") || typeLvl.equals("1")) {
                sql += " and ( ";
                sql += " f.facility_id in (" + centersCollection + ") or f.sup_code  in (" + centersCollection + ") ";
                sql += " or ft.type_hierarchy = 1  ) ";
            } else if (typeLvl.equals("3")) {
                sql += " and ( ";
                sql += " f.facility_id in (" + centersCollection + ") or f.facility_id  in (" + suppliers + ") ";
                sql += " or ft.type_hierarchy = 1  ) ";
            }
            sql += " ";
        }
        sql += "start with f.sup_code is null\n" ;
        sql += "connect by prior f.facility_id = f.sup_code \n" ;
        sql += "order SIBLINGS BY f.fac_name";
        //sql += " order by 2";
      // System.out.println(sql);
        List<FacilityTemplate> results = new ArrayList();
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                FacilityTemplate template = new FacilityTemplate();
                template.setFacilityId(rs.getLong("FACILITY_ID"));
                template.setFacilityName(rs.getString("FACILITY_NAME"));
                template.setFacilityType(rs.getString("factype"));
                template.setSupCode( getSupplier(template.getFacilityId() + ""));
                template.setSupplierName(getSupplierName(template.getFacilityId() + ""));
                results.add(template);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public static List<Map<String, Object>> getDirectorates(String[] directorates) {
        JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
        String centersCollection = "";

        String sql =
            "select f.facility_id AS FACILITY_ID , f.fac_name AS FACILITY_NAME, ft.fac_type_id , ft.fac_name as factype from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id \n" +
            "and type_hierarchy in (1, 2)  ";
        if (directorates != null) {
            String typeLvl = UtilsClass.getTypeLvl(directorates[0]);
            for (int index = 0; index < directorates.length; index++) {
                if (index == 0)
                    centersCollection = directorates[index];
                else
                    centersCollection += " , " + directorates[index];
            }
            if (typeLvl.equals("2") || typeLvl.equals("1")) {
                sql += " and ( ";
                sql += " f.facility_id in (" + centersCollection + ") ";
                sql += " or ft.type_hierarchy = 1  ) ";
            } else if (typeLvl.equals("3")) {
                sql += " and ( ";
                sql +=
                    " f.facility_id in ( select sup_code from facility where facility_id in (" + centersCollection +
                    ") or ft.type_hierarchy = 1  ";
                sql += " ) ";
            }
            sql += " ";
        }
        sql += " order by 2";
        //   System.out.println(sql);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        return results;
    }

    public static String getDirName(String dir) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        int facilityLvl = FacilityLVL(dir);
        String sql = null;
        String value = null;
        if (facilityLvl != 3) {
            sql = "select fac_name from facility where facility_id=" + dir;
        } else {
            sql =
                "select t.fac_name from facility t where " +
                " t.facility_id in ( select sup_code from facility where facility_id =  " + dir + ")";
        }
        //return jdbcTemplate.queryForObject(sql, String.class);
        try {
            
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            value = rs.getString(1);
           
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    public static String getDirCode(String dir) {
        Connection  conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String value = null;
        int facilityLvl = FacilityLVL(dir);
        String sql = null;
        if (facilityLvl != 3) {
            sql = "select fac_code from facility where facility_id=" + dir;
        } else {
            sql =
                "select fac_code from facility where facility_id in ( select ff.sup_code from facility ff where ff.facility_id=" +
                dir + " ) ";
        }
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            value = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static int FacilityLVL(String dir) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql =
            "select type_hierarchy " + "from fac_type ft, facility f " + "where f.fac_type_id=ft.fac_type_id " +
            "and f.facility_id = " + dir;
        //return jdbcTemplate.queryForInt(sql);
        System.out.println(sql);
        int value = 0;
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                value = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //value = (Integer) getOneValue(sql);
        return value;
    }


    public static String getProdDose(String prod) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "select  pro_dose from product where prod_id=" + prod;
        String ProdDose = null;
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next())
            ProdDose = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ProdDose;
    }

    public static String getProdName(String prod) {        
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "select  pro_name from product where prod_id=" + prod;
        String ProdName = null;
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next())
            ProdName = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return ProdName;
    }

    public static Connection jdbcConnection() throws SQLException {
//        String username = MainInterface.username;
//        String password = MainInterface.password;
//        String thinConn = MainInterface.thinConn;
//        DriverManager.registerDriver(new OracleDriver());
//        Connection conn = DriverManager.getConnection(thinConn, username, password);
//        
//        
        DatabaseConnectionManager dcm =
            new DatabaseConnectionManager(DataAccessObject.getProperties().getProperty("jdbc.host"),
                                          DataAccessObject.getProperties().getProperty("jdbc.dbName"),
                                          DataAccessObject.getProperties().getProperty("jdbc.username"),
                                          DataAccessObject.getProperties().getProperty("jdbc.password"));
        //conn.setAutoCommit(false);
        return dcm.getConnection();
    }

    public static void avg(String fac, String mon, String year, String prod, String dat, String fY, String tY,
                           String fM, String tM, String type, String hq) {
        boolean individual = false;
        String lvl = type;
        if (type.equals("lvl")) {

        } else if (type.equals("type")) {

        } else {
            individual = true;
        }

    }

    public static String avgMnthlyCons(String fac, String mon, String year, String prod, String dat, String fY,
                                       String tY, String fM, String tM, String type, String hq) {
        String avg =
            " AVGMONVAL('" + fac + "', '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY + "', '" +
            tY + "', '" + fM + "', '" + tM + "', '" + type + "', '" + hq + "') ";

        String closingBal =
            " CLOSEBALVAL('" + fac + "', '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY +
            "', '" + tY + "', '" + fM + "', '" + tM + "', '" + type + "', '" + hq + "') ";

        String openingBal =
            " OPENBALVAL('" + fac + "', '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY + "', '" +
            tY + "', '" + fM + "', '" + tM + "', '" + type + "', '" + hq + "') ";

        String sql = "SELECT " + avg + " as AVG FROM DUAL ";
        String avgValue = "0";
        avgValue = getOneValue(sql).toString();
        return avgValue;
        //return jdbcTemplate.queryForObject(sql, String.class);
    }

    public static Object getOneValue(String sql) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection conn = null;
        Object value = null;
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            value = rs.getObject(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static String closeBal(String fac, String mon, String year, String prod, String dat, String fY, String tY,
                                  String fM, String tM, String type, String hq) {
        JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
        String avg =
            " AVGMONVAL('" + fac + "', '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY + "', '" +
            tY + "', '" + fM + "', '" + tM + "', '" + type + "', '" + hq + "') ";

        String closingBal =
            " CLOSEBALVAL('" + fac + "', '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY +
            "', '" + tY + "', '" + fM + "', '" + tM + "', '" + type + "', '" + hq + "') ";

        String openingBal =
            " OPENBALVAL('" + fac + "', '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY + "', '" +
            tY + "', '" + fM + "', '" + tM + "', '" + type + "', '" + hq + "') ";

        String sql = "SELECT " + closingBal + " as CLOSE_BAL FROM DUAL ";
        String value = "0";
        value = getOneValue(sql).toString();
        return value;
        //return jdbcTemplate.queryForObject(sql, String.class);
    }

    public static String openBal(String fac, String mon, String year, String prod, String dat, String fY, String tY,
                                 String fM, String tM, String type, String hq) {
        JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
        String avg =
            " AVGMONVAL('" + fac + "', '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY + "', '" +
            tY + "', '" + fM + "', '" + tM + "', '" + type + "', '" + hq + "') ";

        String closingBal =
            " CLOSEBALVAL('" + fac + "', '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY +
            "', '" + tY + "', '" + fM + "', '" + tM + "', '" + type + "', '" + hq + "') ";

        String openingBal =
            " OPENBALVAL('" + fac + "', '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY + "', '" +
            tY + "', '" + fM + "', '" + tM + "', '" + type + "', '" + hq + "') ";

        String sql = "SELECT " + openingBal + " as OPEN_BAL FROM DUAL ";
        String value = "0";
        value = getOneValue(sql).toString();
        return value;
        //return jdbcTemplate.queryForObject(sql, String.class);
    }
    
    public static String mosBelow(String fac, String mon, String year, String prod, String dat, String fY, String tY,
                             String fM, String tM, String type, String hq) {
        JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
        String avg =
            " AVGMONVAL('" + fac + "', '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY + "', '" +
            tY + "', '" + fM + "', '" + tM + "', '" + type + "', '" + hq + "') ";

        String closingBal =
            " CLOSEBALVAL('" + fac + "', '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY +
            "', '" + tY + "', '" + fM + "', '" + tM + "', '" + type + "', '" + hq + "') ";

        String openingBal =
            " OPENBALVAL('" + fac + "', '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY + "', '" +
            tY + "', '" + fM + "', '" + tM + "', '" + type + "', '" + hq + "') ";
        String sql =
            "SELECT ( CASE WHEN " + closingBal + " > 0 AND " + avg + " = 0 THEN 99.9 WHEN " + closingBal +
            " = 0 OR "+ avg + " = 0 THEN 99.9 ELSE ROUND( " + closingBal + " / " + avg + " , 2 ) END ) FROM DUAL ";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public static String mos(String fac, String mon, String year, String prod, String dat, String fY, String tY,
                             String fM, String tM, String type, String hq) {
        JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
        String avg =
            " AVGMONVAL('" + fac + "', '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY + "', '" +
            tY + "', '" + fM + "', '" + tM + "', '" + type + "', '" + hq + "') ";

        String closingBal =
            " CLOSEBALVAL('" + fac + "', '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY +
            "', '" + tY + "', '" + fM + "', '" + tM + "', '" + type + "', '" + hq + "') ";

        String openingBal =
            " OPENBALVAL('" + fac + "', '" + mon + "', '" + year + "', '" + prod + "', '" + dat + "', '" + fY + "', '" +
            tY + "', '" + fM + "', '" + tM + "', '" + type + "', '" + hq + "') ";
        String sql =
            "SELECT ( CASE WHEN " + closingBal + " > 0 AND " + avg + " = 0 THEN 99.9 WHEN " + closingBal +
            " = 0 THEN 0 ELSE ROUND( " + closingBal + " / " + avg + " , 2 ) END ) FROM DUAL ";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    


    public static List<FacilityTemplate> dispensedToUserFacilityList(String prod, String mon, String year, String dat,
                                                                     String fY, String tY, String fM, String tM,
                                                                     String hq, String facilities[], String type, String facType) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String faces = " ";
        if (facilities != null) {
           // facType = getFacTypeLvl(facilities[0]);
                
                for (int i = 0; i < facilities.length; i++) {
                    if (i == 0) {
                        faces = facilities[i];
                    } else {
                        faces += ", " + facilities[i];
                    }
                }
            
        }
        String sql = "";
        sql =
            "select f.facility_id as facilityId, f.fac_name as facilityName, FT.FAC_NAME AS facilityType \n" +
            ", f.sup_code AS supCode \n" 
            + ", dispensed(" + prod + ", f.facility_id, '" + mon + "', '" + year + "', '" +
            dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq+ "', '"   + facType + "') as dispensed \n" +
            ", percentDispensed(" + prod + " , f.facility_id, '" + mon + "', '" + year + "', '" + dat + "', '" + fY +
            "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "', '" + facType + "' ) as program\n" ;
             sql += "  , isFacilityDataEntered( '" + prod + "', f.facility_id, '" + mon  +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as isFacilityDataEntered \n" ;      
             sql += " , f.fac_type_id ";
             sql += " from facility f, fac_type ft\n" + "where f.fac_type_id = ft.fac_type_id\n";
            if (facilities != null) {
                
                for (int i = 0; i < facilities.length; i++) {
                    if (i == 0) {
                        faces = facilities[i];
                    } else {
                        faces += ", " + facilities[i];
                    }
                }
                if (getTypeLvl(facilities[0]).equals("2")) {
                    sql += " and ( ";
                    sql += " ( f.sup_code in ( " + faces + " ) or f.facility_id in ( " + faces + " ) ) \n";
                    sql += " and ( f.sup_code = " + type + "  ) ";
                    sql += " ) ";
                    sql += " or ( f.facility_id = " + type + " or f.sup_code = " + type + " ) ";
                } else if (getTypeLvl(facilities[0]).equals("3")) {
                    if(type.toString().equals("498")){
                        sql += " and ( ( f.facility_id = " + type + " ) or f.sup_code = " + type + " ) ";
                    }else{
                        sql += " and ( f.facility_id in ( " + faces + " ) and f.sup_code = " + type + " ) \n";
                    sql += " or ( f.facility_id = " + type + " ) ";
                    }
                } else if (getTypeLvl(facilities[0]).equals("1")) {
                    sql += " and ( f.facility_id in ( " + faces + " ) and f.sup_code = " + type + " ) \n";
                }

            } else {
                sql += " or ( f.facility_id = " + type + " or f.sup_code = " + type + " ) ";
            }
       


        sql += "start with sup_code is null\n" ;
        sql += "connect by prior f.facility_id = f.sup_code \n" ;
        sql += "order SIBLINGS BY f.fac_name";
        System.out.println(sql);
        List<FacilityTemplate> facilityList = new ArrayList();

        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                if(rs.getInt("isFacilityDataEntered") > 0 ){
                FacilityTemplate facility = new FacilityTemplate();
                facility.setFacilityId(rs.getLong("facilityId"));
                facility.setFacilityName(rs.getString("facilityName"));
                facility.setFacilityType(rs.getString("facilityType"));
                facility.setFacilityTypeHierarchyId(rs.getString("FAC_TYPE_ID"));
                facility.setSupCode(rs.getString("supCode"));
                facility.setDispensed(rs.getString("dispensed"));
                facility.setProgram(rs.getString("program"));
//                if(type.equals("498")){
//                    System.out.println("XXXXXXXXXXXXXXX ACTION XXXXXXXXXXX");
//                    if( getFacTypeLvl(rs.getString("facilityId")).equals("7")){
//                        facilityList.add(facility);
//                    }
//                }else{
                    facilityList.add(facility);
//                }
                
                
//                if(getTypeLvl(type).equals("1")){
//                    if(getFacTypeLvl(facType).equals("7")){
//                            facilityList.add(facility);
//                        }
//                }else{
//                    facilityList.add(facility);
//                }
//                    facilityList.add(facility);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return facilityList;
    }

    public static <T> Collection<T> filter(Collection<T> target, IPredicate<T> predicate) {
        Collection<T> result = new ArrayList<T>();
        for (T element : target) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }

    public static void closeConnectionList(Connection conn, ResultSet rs, PreparedStatement pst) throws SQLException {
        if (pst != null)
            pst.close();
        if (rs != null)
            rs.close();
        if (conn != null)
            conn.close();

    }
    
    public static void closeConnectionList(ResultSet rs, PreparedStatement pst) throws SQLException {
        if (pst != null)
            pst.close();
        if (rs != null)
            rs.close();
    }
    
    public static  List<FacilityTemplate> AllFacilityQueryXXX(String prod, String mon, String year, String dat, String fY, String tY,
                                          String fM, String tM, String type, String hq, String facilities[]) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String AllFacilities_1 =
            "SELECT " +
            " f.facility_id as FACILITY_ID " +
            " , ft.fac_type_id as facTypeId" +
            " , nvl(f.fac_name, ' ') as FacilityName " +
            " , nvl(ft.fac_name,' ') as FacilityType  \n" +
            " , nvl(fac_code, ' ') as Code " +
            " , nvl(fac_contact,' ') as Contact " +
            " , nvl(fac_phone, ' ') as Phone " +
            " , sup_code as SupplierCode " +
            " , type_hierarchy as lvl " ;
            AllFacilities_1 += 
             ", MonthOfSupplierAvg( '" + prod + "', f.facility_id, '" + mon +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as MonthOfSupplierAvg \n";
            AllFacilities_1 += 
             ", ClosingBalanceAvg( '" + prod + "', f.facility_id, '" + mon +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as closingBalance \n";
            AllFacilities_1 += 
             ", AvgAvg( '" + prod + "', f.facility_id, '" + mon +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as AVG \n";
            AllFacilities_1 += 
             ", facilityExist( '" + prod + "', f.facility_id, '" + mon +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as facilityExist \n";
            AllFacilities_1 += 
            " from  facility f, fac_type ft  \n" + " where ft.fac_type_id = f.fac_type_id \n" ;
            //" and f.facility_id in \n" + " (\n " + " select \n" + " f.facility_id \n" +
            //" from ctf_main cm, ctf_item ci, facility f, fac_type ft \n" + " where ft.fac_type_id = f.fac_type_id \n " +
            // " and   f.facility_id = cm.facility_id\n" + " and cm.ctf_main_id = ci.ctf_main_id \n" +
            //" and ci.prod_id = " + prod + "   \n" +  " ) \n" ;
        //" :innerClause " ;
        String AllFacilities_2 =
           " start with sup_code is null " + " connect by prior facility_id = sup_code ";

        String faces = " ";
        if (facilities != null) {
            for (int i = 0; i < facilities.length; i++) {
                if (i == 0) {
                    faces = facilities[i];
                } else {
                    faces += ", " + facilities[i];
                }
            }
        }
        String query = AllFacilities_1;
        if (facilities != null) {
            query +=
                " and  ( f.facility_id in ( " + faces + " )  or f.sup_code in ( " + faces + " ) " +
                " or f.facility_id in (select ff.sup_code from facility ff where ff.facility_id  in ( " + faces +
                " ) )" + " ) ";
        }
        ;
        //String innerClause = innerClause(mon, year, dat, fY, tY, fM, tM, hq);
        //query += innerClause;
        query += AllFacilities_2;
        
        System.out.println(query);
        List<FacilityTemplate> result = new ArrayList<>();
        try {
           
            conn = jdbcConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            while(rs.next()){
                if(rs.getInt("facilityExist") > 0){
                FacilityTemplate ft = new FacilityTemplate();
                Facility facility = new Facility();
                facility.setFacilityId(rs.getLong("FACILITY_ID"));
                facility.setContact(rs.getString("Contact"));
                facility.setFacCode(rs.getString("Code"));
                facility.setFacilityName(rs.getString("FacilityName"));
                facility.setFacilityType(rs.getString("FacilityType"));
                facility.setFacilityTypeHierarchyId(rs.getString("lvl"));
                facility.setPhone(rs.getString("Phone"));
                facility.setSupCode(rs.getString("SupplierCode"));               
                ft.setFacility(facility);
                ft.setMonthOfSupplier(rs.getDouble("MonthOfSupplierAvg"));
                ft.setClosingBal(rs.getString("closingBalance"));
                ft.setAvg(rs.getString("AVG"));           
                ft.setFacilityType(rs.getString("FacilityType"));
                result.add(ft);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static String AllFacilityQuery(String prod, String mon, String year, String dat, String fY, String tY,
                                          String fM, String tM, String type, String hq, String facilities[]) {
        String AllFacilities_1 =
            "SELECT " +
            " f.facility_id as FACILITY_ID , ft.fac_type_id, nvl(f.fac_name, ' ') as FacilityName, nvl(ft.fac_name,' ') as FacilityType  \n" +
            " , nvl(fac_code, ' ') as Code, nvl(fac_contact,' ') as Contact, nvl(fac_phone, ' ') as Phone, sup_code as Supplier, type_hierarchy as lvl " +
            " from  facility f, fac_type ft  \n" + " where ft.fac_type_id = f.fac_type_id\n" +
            " and f.facility_id in \n" + " (\n " + " select \n" + " f.facility_id \n" +
            " from ctf_main cm, ctf_item ci, facility f, fac_type ft \n" + " where ft.fac_type_id = f.fac_type_id \n " +
            " and   f.facility_id = cm.facility_id\n" + " and cm.ctf_main_id = ci.ctf_main_id \n" +
            " and ci.prod_id = " + prod + "   \n";
        //" :innerClause " ;
        String AllFacilities_2 =
            " ) \n" + " start with sup_code is null " + " connect by prior facility_id = sup_code ";

        String faces = " ";
        if (facilities != null) {
            for (int i = 0; i < facilities.length; i++) {
                if (i == 0) {
                    faces = facilities[i];
                } else {
                    faces += ", " + facilities[i];
                }
            }
        }
        String query = AllFacilities_1;
        if (facilities != null) {
            query +=
                " and  ( f.facility_id in ( " + faces + " )  or f.sup_code in ( " + faces + " ) " +
                " or f.facility_id in (select ff.sup_code from facility ff where ff.facility_id  in ( " + faces +
                " ) )" + " ) ";
        }
        ;
        String innerClause = innerClause(mon, year, dat, fY, tY, fM, tM, hq);
        query += innerClause;
        query += AllFacilities_2;

        return query;
    }


    public static String innerClause(String mon, String year, String dat, String fY, String tY, String fM, String tM,
                                     String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }


            if (dat.equals("q") && !mon.equals("0")) {
                if (mon.equals("1")) {
                    sql +=
                        "and cm.p_date between to_date('01/" + year + "','MM/yyyy') and to_date('03/" + year +
                        "','MM/yyyy')";
                }
                if (mon.equals("2")) {
                    sql +=
                        "and cm.p_date between to_date('04" + year + "','MM/yyyy') and to_date('06/" + year +
                        "','MM/yyyy')";
                }
                if (mon.equals("3")) {
                    sql +=
                        "and cm.p_date between to_date('07/" + year + "','MM/yyyy') and to_date('09/" + year +
                        "','MM/yyyy')";
                }
                if (mon.equals("4")) {
                    sql +=
                        "and cm.p_date between to_date('10/" + year + "','MM/yyyy') and to_date('12/" + year +
                        "','MM/yyyy')";
                }
            }


            if (mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'";
            }


        }
        if (dat.equals("u")) {
            sql += " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') ";
            sql += "and to_date('" + tM + "/" + tY + "','mm/yyyy')";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql += " and cm.p_date between to_date('01/" + year + "','mm/yyyy') ";
                sql += "and to_date('06/" + year + "','mm/yyyy')";
            } else {
                sql += " and cm.p_date between to_date('07/" + year + "','mm/yyyy') ";
                sql += "and to_date('12/" + year + "','mm/yyyy')";
            }
        }

        return sql;
    }


    public static String facilitiesOfSupplier(String SupplierId, String[] centers) {
        String facilities = "";
        String sql = " ";

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
        sql += " and F.SUP_CODE = " + SupplierId;

        return sql;
    }
    
    
    //List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
    //


    public static int issuesFacilitiesOfSupplier(String supplier, String mon, String year, String dat, String prod,
                                                 String fY, String tY, String fM, String tM, String hq,
                                                 String centers[]) {
        int result = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String supplierLvl = getTypeLvl(supplier);
        //JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
        String facilities = "";
        String sql = "select nvl(sum(ci.issues),0) ";
        sql += " from facility f, ctf_item ci , ctf_main cm, fac_type ft ";
        sql += " where  ci.ctf_main_id=cm.ctf_main_id(+) ";
        sql += " and cm.facility_id = f.facility_id ";
        sql += " and f.fac_type_id = ft.fac_type_id ";
        sql += " and ci.prod_id = " + prod;
        if (centers != null) {
            for (int index = 0; index < centers.length; index++) {
                if (index == 0)
                    facilities = centers[index];
                else
                    facilities += " , " + centers[index];
            }
        }

        if (supplierLvl.equals("1")) {
            sql += " and ft.type_hierarchy = 3 ";
        } else if (supplierLvl.equals("2")) {
            sql += " and f.sup_code = " + supplier;
        } else if (getTypeLvl(supplier).equals("3")) {
            sql += " and f.facility_id = " + supplier;
        }
        sql += innerClause(mon, year, dat, fY, tY, fM, tM, hq);
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static int dispensedCountry(String[] centers, String mon, String year, String dat, String prod, String fY,
                                    String tY, String fM, String tM, String hq, String type) {
                                        Connection conn = null;
                                        ResultSet rs = null;
                                        PreparedStatement pst = null;
                                        String facilities = null;
                                        int result = 0;
                                        
//                                        if(centers != null){
//                                            type = getFacTypeLvl(centers[0]);
//                                        }
//                                        
                                        String sql = "";
                                        sql = " select ";
                                        
                                           
                                        if (centers != null) {
                                            for (int index = 0; index < centers.length; index++) {
                                                if (index == 0)
                                                    facilities = centers[index];
                                                else
                                                    facilities += " , " + centers[index];
                                            }
                                            sql +=   " dispensed(" + prod + ", 498, '" + mon + "', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "', '"+type+"' ) as dispensed ";                  
                                        }else{
                                            sql +=   " dispensed(" + prod + ", 498, '" + mon + "', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "', '"+getFacTypeLvl(centers[0])+"' ) as dispensed ";
                                        }
                                        
                                        
                                        sql += " from dual" ;
                                        try {
                                            System.out.println(sql);
                                            conn = jdbcConnection();
                                            pst = conn.prepareStatement(sql);
                                            rs = pst.executeQuery();
                                            if(rs.next()){
                                                result = rs.getInt(1);
                                            }
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }finally {
                                            try {
                                                closeConnectionList(conn, rs, pst);
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        return result;
                                 
                                    }
    public static int issuesCountry(String[] centers, String mon, String year, String dat, String prod, String fY,
                                    String tY, String fM, String tM, String hq) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String facilities = null;
        int result = 0;
        
        
        String sql = "select nvl(sum(ci.issues),0) ";
        sql += " from facility f, ctf_item ci , ctf_main cm, fac_type ft ";
        sql += " where  ci.ctf_main_id=cm.ctf_main_id(+) ";
        sql += " and cm.facility_id = f.facility_id ";
        sql += " and f.fac_type_id = ft.fac_type_id ";
        sql += " and  ft.type_hierarchy = 3 ";
        sql += " and ci.prod_id = " + prod;
        sql += innerClause(mon, year, dat, fY, tY, fM, tM, hq);
        if (centers != null) {
            for (int index = 0; index < centers.length; index++) {
                if (index == 0)
                    facilities = centers[index];
                else
                    facilities += " , " + centers[index];
            }

        }
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;

    }
    
    
    
    public static int dispensed(String facilityId, String[] centers, String mon, String year, String dat, String prod, String fY,
                                    String tY, String fM, String tM, String hq) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String facilities = null;
        int result = 0;
        
        
        String sql = "select nvl(sum(ci.issues),0) ";
        sql += " from facility f, ctf_item ci , ctf_main cm, fac_type ft ";
        sql += " where  ci.ctf_main_id=cm.ctf_main_id(+) ";
        sql += " and cm.facility_id = f.facility_id ";
        sql += " and f.fac_type_id = ft.fac_type_id ";
        
        if(getTypeLvl(facilityId).equals("1")) {
        sql += " and  ft.type_hierarchy = 3 ";
        } else if(getTypeLvl(facilityId).equals("2")) {
        sql += " and f.sup_code = " + facilityId;
        }else if(getTypeLvl(facilityId).equals("3")) {
        sql += " and f.facility_id = " + facilityId;
        }
        if (centers != null) {
            for (int index = 0; index < centers.length; index++) {
                if (index == 0)
                    facilities = centers[index];
                else
                    facilities += " , " + centers[index];
            }
            if(getTypeLvl(centers[0]).equals("1")) {
            sql += " and  ft.type_hierarchy = 3 ";
            } else if(getTypeLvl(centers[0]).equals("2")) {
            sql += " and f.sup_code = " + facilityId;
            }else if(getTypeLvl(centers[0]).equals("3")) {
            sql += " and f.facility_id in (" + facilities + " ) ";
            }
        }else{
            
        }
        sql += " and ci.prod_id = " + prod;
        sql += innerClause(mon, year, dat, fY, tY, fM, tM, hq);
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                result = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;

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

    public static List<FacilityTemplate> getFacTypesMainName(String typeID) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        List<FacilityTemplate> facilityList = new ArrayList();
        String sql = "select fac_type_id, ";
            sql += " fac_name, type_hierarchy ";
            sql += " from fac_type where fac_type_id=" + typeID;
            if ( Integer.parseInt(typeID) > 100 ){
        sql =
            "select grp_id, grp_desc from\n" + 
            " fac_type ft, facility f, group_of_facilities gof, groups g \n" + 
            " \n" + 
            " where ft.fac_type_id = f.fac_type_id and ft.fac_type_id = gof_facility_id and \n" + 
            " \n" + 
            " gof.gof_grp_id = g.grp_id and gof.gof_grp_id= " +
            
            (Integer.parseInt(typeID) - 100) + " order by 2";
            }
            System.out.println(sql);
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(!rs.next()){
                String sqlGroup = "select ft.fac_type_id, ";
                    sqlGroup += " ft.fac_name, ft.type_hierarchy ";
                  sqlGroup+= " from fac_type ft " ;
                  sqlGroup+= " , facility f ";
                  sqlGroup+= " where ";
                  sqlGroup += "  ft.fac_type_id in ";
                  sqlGroup += " (select gof_facility_id from group_of_facilities ";
                  sqlGroup+= " where gof_grp_id="+(Integer.parseInt(typeID)-100)+" ) " ;
                pst = conn.prepareStatement(sqlGroup);
                rs = pst.executeQuery();
                while(rs.next()){
                    FacilityTemplate facility = new FacilityTemplate();
                    facility.setFacilityId(rs.getLong(1));
                    facility.setFacilityName(rs.getString(2));
                    facility.setFacilityTypeHierarchyId(rs.getString(3));
                    
                    facilityList.add(facility);
                }
            }else{
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while(rs.next()){
                FacilityTemplate facility = new FacilityTemplate();
                facility.setFacilityId(rs.getLong(1));
                facility.setFacilityName(rs.getString(2));
                //facility.setFacilityTypeHierarchyId(rs.getString(3));
                
                facilityList.add(facility);
                }
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return facilityList;

    }
    
    
    
    public static FacilityTemplate getFacTypesMainNameItem(String fac) {
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
        FacilityTemplate facility = new FacilityTemplate();
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(!rs.next()){
                String sqlGroup = "select ft.fac_type_id, ";
                    sqlGroup += " ft.fac_name, ft.type_hierarchy ";
                  sqlGroup+= " from fac_type ft " ;
                  sqlGroup+= " , facility f ";
                  sqlGroup+= " where ";
                  sqlGroup += "  ft.fac_type_id in ";
                  sqlGroup += " (select gof_facility_id from group_of_facilities ";
                  sqlGroup+= " where gof_grp_id="+(Integer.parseInt(fac)-100)+" ) " ;
                pst = conn.prepareStatement(sqlGroup);
                rs = pst.executeQuery();
                while(rs.next()){
                    
                    facility.setFacilityId(rs.getLong(1));
                    facility.setFacilityName(rs.getString(2));
                    facility.setFacilityTypeHierarchyId(rs.getString(3));
                    
                }
            }else{
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while(rs.next()){
                facility.setFacilityId(rs.getLong(1));
                facility.setFacilityName(rs.getString(2));
                //facility.setFacilityTypeHierarchyId(rs.getString(3));
                }
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }finally{
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return facility;

    }
        
    
    
    
    
    
    
    
    
    
    
    public static List<FacilityTemplate> belowEmergencyOrderPointList(String prod, String mon, String year, String dat,
                                                                     String fY, String tY, String fM, String tM,
                                                                     String hq, String facilities[], String type) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String faces = " ";
        if (facilities != null) {
            
            for (int i = 0; i < facilities.length; i++) {
                if (i == 0) {
                    faces = facilities[i];
                } else {
                    faces += ", " + facilities[i];
                }
            }
            int [] intFacilities = new int [facilities.length];
            for (int k=0; k<facilities.length; k++){
                intFacilities[k] = Integer.parseInt(facilities[k]);
            }
        }

        String sql = "SELECT " +
        " f.facility_id as FACILITY_ID " +
        " , ft.fac_type_id as facTypeId" +
        " , nvl(f.fac_name, ' ') as FacilityName " +
        " , nvl(ft.fac_name,' ') as FacilityType  \n" +
        " , nvl(fac_code, ' ') as Code " +
        " , nvl(fac_contact,' ') as Contact " +
        " , nvl(fac_phone, ' ') as Phone " +
        " , sup_code as SupplierCode " +
        " , type_hierarchy as lvl " ;
        sql  += 
         ", MonthOfSupplierAvg( '" + prod + "', f.facility_id, '" + mon +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as MonthOfSupplierAvg \n";
        sql += 
         ", ClosingBalanceAvg( '" + prod + "', f.facility_id, '" + mon +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as closingBalance \n";
        sql += 
         ", AvgAvg( '" + prod + "', f.facility_id, '" + mon +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as AVG \n";
        sql += 
         ", facilityExist( '" + prod + "', f.facility_id, '" + mon +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as facilityExist \n";             
        sql += " from facility f, fac_type ft \n" ;
        sql += " where f.fac_type_id = ft.fac_type_id \n";
            if (facilities != null) {
                if (getTypeLvl(facilities[0]).equals("2")) {
                    sql += " and ( ";
                    sql += " ( f.sup_code in ( " + faces + " ) or f.facility_id in ( " + faces + " ) ) \n";
                    sql += " and ( f.sup_code = " + type + "  ) ";
                    sql += " ) ";
                    sql += " or ( f.facility_id = " + type + " or f.sup_code = " + type + " ) ";
                } else if (getTypeLvl(facilities[0]).equals("3")) {
                    if(type.toString().equals("498")){
                        sql += " and ( ";
                        sql += " ( f.facility_id = " + type + " ) or f.sup_code = " + type + " ) ";
                    }else{
                        sql += " and ( f.facility_id in ( " + faces + " ) and f.sup_code = " + type + " ) \n";
                    sql += " or ( f.facility_id = " + type + " ) ";
                    }
                }

            } else {
                sql += " or ( ";
                sql += " f.facility_id = " + type ;
                //sql += facilitiesCTFMain(prod, mon, year, dat, fY, tY, fM, tM, hq, type);
                sql += " or f.sup_code = " + type ;
                sql += " ) ";
            }
       


        sql +=
            "start with sup_code is null\n" + "connect by prior f.facility_id = f.sup_code \n" +
            "order SIBLINGS BY f.fac_name";
        System.out.println(sql);
        List<FacilityTemplate> facilityList = new ArrayList();

        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
                while(rs.next()){
                    if(rs.getInt("facilityExist") > 0){
                    FacilityTemplate ft = new FacilityTemplate();
                    Facility facility = new Facility();
                    facility.setFacilityId(rs.getLong("FACILITY_ID"));
                    facility.setContact(rs.getString("Contact"));
                    facility.setFacCode(rs.getString("Code"));
                    facility.setFacilityName(rs.getString("FacilityName"));
                    facility.setFacilityType(rs.getString("FacilityType"));
                    facility.setFacilityTypeHierarchyId(rs.getString("lvl"));
                    facility.setPhone(rs.getString("Phone"));
                    facility.setSupCode(rs.getString("SupplierCode"));               
                    ft.setFacility(facility);
                    ft.setMonthOfSupplier(rs.getDouble("MonthOfSupplierAvg"));
                    ft.setClosingBal(rs.getString("closingBalance"));
                    ft.setAvg(rs.getString("AVG"));           
                    ft.setFacilityType(rs.getString("FacilityType"));
                        double mos = ft.getMonthOfSupplier();
                        String lvl = ft.getFacility().getFacilityTypeHierarchyId();
                        boolean condition = (lvl.equals("3") && mos < 0.55 ) || (lvl.equals("2") && mos <= 1.0);
                        if(condition)
                    facilityList.add(ft);
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return facilityList;
    }
    

    public static String getGroupName(String group) {
        String sql = "select grp_desc from groups where grp_id=" + group;
        String result = getOneValue(sql).toString();
        return result;
    }
    
    public static String supplierFunciton(String supplier){
        String Supp = (FacilityLVL(supplier)+"").equals("1") ? supplier : "supplier(" + supplier + " ) ";
        return Supp;
    }
    
    public static String supplierId(String supplier){
        String sql = " SELECT " + supplierFunciton(supplier) + " FROM dual ";
        return getOneValue(sql).toString();       
    }
    
    public static List<FacilityDispensed> facilityDispensedToUserReport( String mon, String year, String dat,
                                                                     String fY, String tY, String fM, String tM,
                                                                     String hq, String supplier, String type) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        
         String   sql = "select ";
         sql += " prod_id \n";
         sql += " , pro_name \n";
         sql += " , pro_dose \n";
         sql += " , dispensed( prod_id , '"+supplier+"', '" + mon + "', '" + year + "', '" +
                dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq+ "', '"   + type + "') as facilityDispensed \n";
            sql += ", dispensed(prod_id, " +  supplierFunciton(supplier) + ", '" + mon + "', '" + year + "', '" +
                   dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq+ "', '"   + type + "') as supplierDispensed \n";
         sql += " from PRODUCT ";
         List<FacilityDispensed> facilityDispensedList = new ArrayList<>();
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            DecimalFormat df = new DecimalFormat("###,###.##");
                while(rs.next()){
                    FacilityDispensed facility = new FacilityDispensed();
                    facility.setProductId(rs.getString("prod_id"));
                    facility.setProductName(rs.getString("pro_name"));
                    facility.setProductDose(rs.getString("pro_dose"));
                    facility.setFacilityId(supplier);
                    facility.setFacilityDispensed(rs.getString("facilityDispensed"));
                    facility.setSupplierDispensed(rs.getString("supplierDispensed"));
                    facility.setFacBySuppDispensed(
                    facility.getSupplierDispensed().equals("0")?"0":df.format(((Double.valueOf(facility.getFacilityDispensed()) / Double.valueOf(facility.getSupplierDispensed()) ) * 100 ))+" %"
                        );                
                    facilityDispensedList.add(facility);
                }            
            } catch (SQLException e) {
                e.printStackTrace();
        }finally{
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return facilityDispensedList;                                                                     
        }
    public static Double getFacDispensedToUserRepMainSum(String dir, String quart, String year, String dat, String fY,
                                                  String tY, String fM, String tM, String type, String hq) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "";
        sql = " select nvl(sum(ci.issues),0) ";
        sql += " from  ctf_item ci, product p, ctf_main cm ";
        sql += " where cm.ctf_main_id= ci.ctf_main_id ";
        sql += " and ci.prod_id=p.prod_id(+) ";
        sql += " and to_char(cm.p_date,'yyyy')= " + year;
        sql += " and f.sup_code =  " + dir;
        sql += innerClause(quart, year, dat, fY, tY, fM, tM, hq);
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            System.out.println("SUM " + sql);
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.010;
        }

    }
    
    public ResultSet getFacDispensedToUserRep(String dir, String quart, String year, String dat, String fY, String tY,
                                              String fM, String tM, String type, String hq) {
        ResultSet rs = null;
        String sql = "";
        sql = " select p.pro_name ";
        sql += " , nvl(sum(ci.issues),0) ";
        sql += " ,p.prod_id ";
        sql += " from  ctf_item ci, product p, ctf_main cm ";
        sql += " where cm.ctf_main_id = ci.ctf_main_id ";
        sql += " and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')= "+ year;
        sql += "  and cm.facility_id in (" + dir + ") ";
        sql += " group by p.pro_name, p.prod_id order by 1";
        if ((FacilityLVL(dir)+"").equals("3")) {

        ///////////////////////// for centers
        //////////////////////////////////////
        //////////////////////////////////////
        //////////// now for higher level /////////////////
        ///////////////////////////////////////////
        } else {
            System.out.println("******************* This is A Directorate **************************");
            if (dat.equals("m") || dat.equals("q")) {
                if (quart.equals("0")) {
                    //sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')="+year+"  and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                    sql =
                        " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
                        year + "  and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                        ")   group by p.pro_name, p.prod_id order by 1";
                }
                if (!quart.equals("0") &&
                    dat.equals("q")) {
                    //sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')="+year+" and to_char(ci.p_date,'Q')="+quart+" and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                    sql =
    " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
    year + " and to_char(cm.p_date,'Q')=" + quart +
    " and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
    ")   group by p.pro_name, p.prod_id order by 1";
                }
                if (!quart.equals("0") &&
                    dat.equals("m")) {
                    //  sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')="+year+" and to_char(ci.p_date,'mm')="+quart+" and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                    sql =
    " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
    year + " and to_char(cm.p_date,'mm')=" + quart +
    " and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
    ")   group by p.pro_name, p.prod_id order by 1";
                }


            }
            if (dat.equals("u")) {
                //sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'mm/yyyy') between '"+fM+"/"+fY+"' and '"+fM+"/"+tY+"'  and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                sql =
                    " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                    "','mm/yyyy')  and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                    ")   group by p.pro_name, p.prod_id order by 1";


            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year donnnnnnnnnnnnnne");
                if (hq.equals("1")) {
                    //sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')="+year+" and to_char(ci.p_date,'Q') in ('1','2') and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                    sql =
                        " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')='" +
                        year +
                        "' and to_char(cm.p_date,'Q') in ('1','2') and cm.facility_id in (select facility_id from facility where sup_code=" +
                        dir + ")   group by p.pro_name, p.prod_id order by 1";
                }
                if (hq.equals("2")) {
                    //sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')="+year+" and to_char(ci.p_date,'Q') in ('3','4') and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                    sql =
                        " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')='" +
                        year +
                        "' and to_char(cm.p_date,'Q') in ('3','4') and cm.facility_id in (select facility_id from facility where sup_code=" +
                        dir + ")   group by p.pro_name, p.prod_id order by 1";
                }
                ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
            }


        } //for higher level

        try {
//            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            System.out.println("3rd level " + sql);
//            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }


    public static List<FacilityTemplate> FacilityProductDispensedReport(String prod, String mon, String year, String dat,
                                                                     String fY, String tY, String fM, String tM,
                                                                     String hq, String facilities[], String type, String facType) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String faces = " ";
        if (facilities != null) {
           // facType = getFacTypeLvl(facilities[0]);
                
                for (int i = 0; i < facilities.length; i++) {
                    if (i == 0) {
                        faces = facilities[i];
                    } else {
                        faces += ", " + facilities[i];
                    }
                }
            
        }
        String sql = "";
        sql =
            "select f.facility_id as facilityId, f.fac_name as facilityName, f.FAC_CODE as facilityCode, FT.FAC_NAME AS facilityType \n" +
            ", f.sup_code AS supCode \n" 
            + ", dispensed(" + prod + ", f.facility_id, '" + mon + "', '" + year + "', '" +
            dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq+ "', '"   + facType + "') as dispensed \n" +
            ", percentDispensed(" + prod + " , f.facility_id, '" + mon + "', '" + year + "', '" + dat + "', '" + fY +
            "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "', '" + facType + "' ) as program\n" ;
             sql += "  , isFacilityDataEntered( '" + prod + "', f.facility_id, '" + mon  +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as isFacilityDataEntered \n" ;
             
             sql += " from facility f, fac_type ft\n" + "where f.fac_type_id = ft.fac_type_id\n";
            if (facilities != null) {
                
                for (int i = 0; i < facilities.length; i++) {
                    if (i == 0) {
                        faces = facilities[i];
                    } else {
                        faces += ", " + facilities[i];
                    }
                }
                if (getTypeLvl(facilities[0]).equals("2")) {
                    sql += " and ( ";
                    sql += " ( f.sup_code in ( " + faces + " ) or f.facility_id in ( " + faces + " ) ) \n";
                    sql += " and ( f.sup_code = " + type + "  ) ";
                    sql += " ) ";
                    sql += " or ( f.facility_id = " + type + " or f.sup_code = " + type + " ) ";
                } else if (getTypeLvl(facilities[0]).equals("3")) {
                    if(type.toString().equals("498")){
                        sql += " and ( ( f.facility_id = " + type + " ) or f.sup_code = " + type + " ) ";
                    }else{
                        sql += " and ( f.facility_id in ( " + faces + " ) and f.sup_code = " + type + " ) \n";
                    sql += " or ( f.facility_id = " + type + " ) ";
                    }
                }

            } else {
                sql += " or ( f.facility_id = " + type + " or f.sup_code = " + type + " ) ";
            }
       


        sql +=
            "start with sup_code is null\n" + "connect by prior f.facility_id = f.sup_code \n" +
            "order SIBLINGS BY f.fac_name";
        System.out.println(sql);
        List<FacilityTemplate> facilityList = new ArrayList();

        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                if(rs.getInt("isFacilityDataEntered") > 0 ){
                FacilityTemplate facility = new FacilityTemplate();
                facility.setFacilityId(rs.getLong("facilityId"));
                facility.setFacilityName(rs.getString("facilityName"));
                facility.setFacCode(rs.getString("facilityCode"));
                facility.setFacilityType(rs.getString("facilityType"));
                facility.setSupCode(rs.getString("supCode"));
                facility.setDispensed(rs.getString("dispensed"));
                facility.setProgram(rs.getString("program"));
                if(!facility.getDispensed().equals("0"))
                facilityList.add(facility);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return facilityList;
    }
    
    public static String queryFacilityProductDispensed(String supplier, String mon, String year,
                                                                     String dat, String product, String fY, String tY,
                                                                     String fM, String tM, String hq,
                                                                     String centers[]) {
    String sql =     " select f.fac_name as FACILITY_NAME";
        sql += " , UPPER(f.fac_code) as facCode";
        sql += " , ft.fac_name AS factype";
        sql += " , f.facility_id AS FACILITY_ID";
        sql += " , isFacilityDataEntered( '" + product + "', f.facility_id, '" + mon  +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as isFacilityDataEntered \n" ;
        sql += " from facility f,  fac_type ft ";
        sql += " where ";
        sql += " f.fac_type_id = ft.fac_type_id(+) ";
        sql += UtilsClass.facilitiesOfSupplier(supplier, centers);
    return sql;    
    }
    
    public static List<FacilityTemplate> FacilityProductDispensedList(String supplier, String mon, String year,
                                                                     String dat, String product, String fY, String tY,
                                                                     String fM, String tM, String hq,
                                                                     String centers[]) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = null;
        sql = " select f.fac_name as FACILITY_NAME";
        sql += " , UPPER(f.fac_code) as facCode";
        sql += " , ft.fac_name AS factype";
        sql += " , f.facility_id AS FACILITY_ID";
        sql += " , isFacilityDataEntered( '" + product + "', f.facility_id, '" + mon  +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as isFacilityDataEntered \n" ;
        sql += " from facility f,  fac_type ft ";
        sql += " where ";
        sql += " f.fac_type_id = ft.fac_type_id(+) ";
        sql += UtilsClass.facilitiesOfSupplier(supplier, centers);
        System.out.println(sql);
        List<FacilityTemplate> list = new ArrayList();
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                if(rs.getInt("isFacilityDataEntered") > 0 ){
                FacilityTemplate template = new FacilityTemplate();
                template.setFacilityName(rs.getString("FACILITY_NAME"));
                template.setFacilityType(rs.getString("factype"));
                template.setFacilityId(rs.getLong("FACILITY_ID"));
                template.setFacCode(rs.getString("facCode"));
                list.add(template);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                UtilsClass.closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }        
        return list;
    }
    
    
    
    
    
    public static FacilityTemplate getFacTypesMainNameItemXXX(String fac) {
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
        FacilityTemplate facility = new FacilityTemplate();
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(!rs.next()){
                String sqlGroup = "select ft.fac_type_id, ";
                    sqlGroup += " ft.fac_name, ft.type_hierarchy ";
                  sqlGroup+= " from fac_type ft " ;
                  sqlGroup+= " , facility f ";
                  sqlGroup+= " where ";
                  sqlGroup += "  ft.fac_type_id in ";
                  sqlGroup += " (select gof_facility_id from group_of_facilities ";
                  sqlGroup+= " where gof_grp_id="+(Integer.parseInt(fac)-100)+" ) " ;
                pst = conn.prepareStatement(sqlGroup);
                rs = pst.executeQuery();
                while(rs.next()){
                    
                    facility.setFacilityId(rs.getLong(1));
                    facility.setFacilityName(rs.getString(2));
                    facility.setFacilityTypeHierarchyId(rs.getString(3));
                    
                }
            }else{
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while(rs.next()){
                facility.setFacilityId(rs.getLong(1));
                facility.setFacilityName(rs.getString(2));
                //facility.setFacilityTypeHierarchyId(rs.getString(3));
                }
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }finally{
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return facility;

    }
    
    
    public static double Dispensed(String mon, String year,String dat, String product, 
                                String fY, String tY,String fM, 
                                String tM, String hq,
                                String centers[], String type) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sqlDispensed = "SELECT \n" + 
        "        nvl(SUM(issues), 0) \n" + 
        "    FROM\n" + 
        "        ctf_main   cm,\n" + 
        "        ctf_item   ci,\n" + 
        "        facility   f,\n" + 
        "        fac_type   ft\n" + 
        "    WHERE \n" + 
        "        cm.ctf_main_id = ci.ctf_main_id\n" + 
        "        AND ft.fac_type_id = f.fac_type_id\n" + 
        "        AND cm.facility_id = f.facility_id\n" + 
        "        AND ci.prod_id = " + product;
        if(type.equals("0")){
        sqlDispensed += " AND ft.type_hierarchy = 3 ";
        }else{
            if((FacilityLVL(centers[0])+"").equals("1")){
                sqlDispensed += " AND ft.type_hierarchy = 3 ";
            }else if((FacilityLVL(centers[0])+"").equals("2")){
                sqlDispensed += " AND f.sup_code = " + centers[0];
            }else if((FacilityLVL(centers[0])+"").equals("3")){
                String faces = "";
                for(int k=0; k<centers.length; k++){
                    if(k==0){
                        faces = centers[k];
                    }else{
                        faces += ", " + centers[k];
                    }
                }
                sqlDispensed += " AND f.facility_id in ( ";
                sqlDispensed += faces;
                sqlDispensed += " ) ";
            } 
        }
        sqlDispensed += innerClause(mon, year, dat, fY, tY, fM, tM, hq);
        try {
            System.out.println(sqlDispensed);
            conn = jdbcConnection();
            pst = conn.prepareStatement(sqlDispensed);
            rs = pst.executeQuery();
            rs.next();
            return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    
    

    public static List<FacilityTemplate> FacilityProductDispensedList(String supplier, String mon, String year,
                                                                     String dat, String products[], String fY, String tY,
                                                                     String fM, String tM, String hq,
                                                                     String centers[]) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sqlMain = "";
        for (int p = 0; p < products.length; p++) {
            if (p == 0) {
                sqlMain +=
                    queryFacilityProductDispensed(supplier, mon, year, dat, products[p], fY, tY, fM, tM, hq, centers);
            } else {
                sqlMain += " intersect ";
                sqlMain +=
                    queryFacilityProductDispensed(supplier, mon, year, dat, products[p], fY, tY, fM, tM, hq, centers);
            }
        }
        System.out.println(sqlMain);
        List<FacilityTemplate> list = new ArrayList();
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sqlMain);
            rs = pst.executeQuery();
            while(rs.next()){
                if(rs.getInt("isFacilityDataEntered") > 0 ){
                FacilityTemplate template = new FacilityTemplate();
                template.setFacilityName(rs.getString("FACILITY_NAME"));
                template.setFacilityType(rs.getString("factype"));
                template.setFacilityId(rs.getLong("FACILITY_ID"));
                template.setFacCode(rs.getString("facCode"));
                list.add(template);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                UtilsClass.closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }        
        return list;
    }
    
    
    public static String getFacProdDispensedRepMain(String quart, String year, String prod, String dir, String dat, String fY,
                                             String tY, String fM, String tM, String type, String hq, int len) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "";
        String mains = "";
        sql =
            "select  ";
        sql += " ff.facility_id, ";
        sql += " sum(ci.receipts), ";
        sql += " sum(ci.issues) ";
        sql += " from facility ff, ctf_main cm, ctf_item ci ";
        sql += " where  cm.facility_id= ff.facility_id ";
        sql += " and ci.ctf_main_id=cm.ctf_main_id ";
        sql += " and prod_id in  (" +prod + ")";
        sql += " and ff.sup_code=" + dir;
        sql += " and ci.issues > 0 group by ff.facility_id order by 1";
        sql += innerClause(quart, year, dat, fY, tY, fM, tM, hq);
        try {
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            System.out.println(sql);
            rs = pst.executeQuery(sql);
            int p = 0;
            while (rs.next()) {
                if (p == 0) {
                    mains = rs.getString(2);
                } else {
                    mains += ", " + rs.getString(2);
                }
                p++;
            }
            return mains;
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return null;
        }

    }

    
    public static List<FacilityTemplate> serviceStatisticsReportList(String prod, String mon, String year, String dat,
                                                                     String fY, String tY, String fM, String tM,
                                                                     String hq, String facilities[], String supplier, String type) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String faces = " ";
        if (facilities != null) {
            for (int i = 0; i < facilities.length; i++) {
                if (i == 0) {
                    faces = facilities[i];
                } else {
                    faces += ", " + facilities[i];
                }
            }
            int [] intFacilities = new int [facilities.length];
            for (int k=0; k<facilities.length; k++){
                intFacilities[k] = Integer.parseInt(facilities[k]);
            }
        }
        String sql = "";
        sql =   "   SELECT ";
        sql +=  "   f.FACILITY_ID as facilityId ";
        sql +=  " , f.FAC_NAME as facilityName ";
        sql +=  " , FT.FAC_NAME AS facilityType \n";
        sql +=  " , nvl(f.sup_code,0) AS supCode \n";
        sql +=  "  , servicestatistics ( '" + prod + "', f.facility_id, '" + mon  +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq +  "', '" + type +"', 'NEW_VISITS' ) as NEW_USERS \n" ;
        sql +=  "  , servicestatistics ( '" + prod + "', f.facility_id, '" + mon  +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq +  "', '" + type +"', 'CONT_VISITS' ) as CONT_USERS \n" ;
        sql +=  "  , servicestatistics_country ( '" + prod + "', f.facility_id, '" + mon  +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq +  "', '" + type + "', 'NEW_VISITS' ) as NEW_USERS_COUNTRY \n" ;
        sql +=  "  , servicestatistics_country ( '" + prod + "', f.facility_id, '" + mon  +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq +  "', '" + type +"', 'CONT_VISITS' ) as CONT_USERS_COUNTRY \n" ;
        sql += "  , isFacilityDataEntered( '" + prod + "', f.facility_id, '" + mon  +"', '" + year + "', '" + dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq + "' ) as isFacilityDataEntered \n" ;    
        sql += " from facility f, fac_type ft \n " ;
        sql += " where f.fac_type_id = ft.fac_type_id \n";
            if (facilities != null) {
                if (getTypeLvl(facilities[0]).equals("2")) {
                    sql += " and ( ";
                    sql += " ( f.sup_code in ( " + faces + " ) or f.facility_id in ( " + faces + " ) ) \n";
                    sql += " and ( f.sup_code = " + supplier + "  ) ";
                    sql += " ) ";
                    sql += " or ( f.facility_id = " + supplier + " or f.sup_code = " + supplier + " ) ";
                } else if (getTypeLvl(facilities[0]).equals("3")) {
                    if(type.toString().equals("498")){
                        sql += " and ( ";
                        sql += " ( f.facility_id = " + supplier + " ) or f.sup_code = " + supplier + " ) ";
                    }else{
                        sql += " and ( f.facility_id in ( " + faces + " ) and f.sup_code = " + supplier + " ) \n";
                    sql += " or ( f.facility_id = " + supplier + " ) ";
                    }
                }
            } else {
                sql += " or ( ";
                sql += " f.facility_id = " + supplier ;
                //sql += facilitiesCTFMain(prod, mon, year, dat, fY, tY, fM, tM, hq, type);
                sql += " or f.sup_code = " + supplier ;
                sql += " ) ";
            }
        sql +=  " start with sup_code is null\n" + "connect by prior f.facility_id = f.sup_code \n" +
                " order SIBLINGS BY f.fac_name";
        System.out.println(sql);
        List<FacilityTemplate> facilityList = new ArrayList();
        try {
            System.out.println(sql);
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {                
                if(rs.getInt("isFacilityDataEntered") > 0){
                    FacilityTemplate facility = new FacilityTemplate();
                    facility.setFacilityId(rs.getLong("facilityId"));
                    facility.setFacilityName(rs.getString("facilityName"));
                    facility.setFacilityType(rs.getString("facilityType"));
                    facility.setSupCode(rs.getString("supCode"));
                    //facility.setDispensed(rs.getString("dispensed"));
                    //facility.setProgram(rs.getString("program"));                   
                    facility.setContUsersCountry(rs.getString("CONT_USERS_COUNTRY"));
                    facility.setNewUsersCountry(rs.getString("NEW_USERS_COUNTRY"));
                    facility.setNewUsers(rs.getString("NEW_USERS"));
                    facility.setContUsers(rs.getString("CONT_USERS"));
//                    facility.setAvg(
//                    rs.getInt("avg") < 0 ? "0" : rs.getString("avg")
//                    );
//                        facility.setOpenningBal(rs.getString("openBal"));
//                        facility.setClosingBal(rs.getString("closingBal"));
//                        facility.setIssues(rs.getString("issues"));
//                        facility.setReceipts(rs.getString("receipts"));
//                        facility.setAdjustments(rs.getString("adjustments"));
                facilityList.add(facility);
                }
                
            }
            boolean SuppExist = false;
            for(FacilityTemplate ft: facilityList){
                if( (ft.getFacilityId()+"").equals(supplier)){
                    SuppExist = true;
                    System.out.println("^^^^^^^^^^^^^^^^^^^ The Supplier Found &^^^^^^^^^^^^^^^^^ :" + type);
                    break;
                }
            }
            
            if(!SuppExist){                
                facilityList.add(0, getFacilityInfo(supplier));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return facilityList;
    }


}


