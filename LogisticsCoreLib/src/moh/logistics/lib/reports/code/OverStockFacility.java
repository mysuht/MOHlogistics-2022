package moh.logistics.lib.reports.code;

import java.util.List;
import java.util.Map;

import moh.logistics.lib.reports.JDBCConfig;
import moh.logistics.lib.reports.JDBCTemplateInfo;

import moh.logistics.lib.reports.UtilsClass;

import org.springframework.jdbc.core.JdbcTemplate;

public class OverStockFacility {
    public OverStockFacility() {
        super();
    }
    JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();


    public List<Map<String, Object>> AllFacilities(String prod, String mon, String year, String dat, String fY,
                                                           String tY, String fM, String tM, String type, String hq,
                                                           String facilities[]) {
        String query = UtilsClass.AllFacilityQuery( prod,  mon,  year,  dat,  fY,
                                                            tY,  fM,  tM,  type,  hq,
                                                            facilities);
        
        List<Map<String, Object>> results =
        jdbcTemplate.queryForList(query);
        return results;
    }
    
    public static String mos(String fac, String mon, String year, String prod, String dat, String fY, String tY,
                             String fM, String tM, String type, String hq) {
        return UtilsClass.mos(fac, mon, year, prod, dat, fY, tY, fM, tM, type, hq);
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
