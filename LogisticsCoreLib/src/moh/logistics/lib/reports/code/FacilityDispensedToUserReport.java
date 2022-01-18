package moh.logistics.lib.reports.code;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import moh.logistics.lib.reports.UtilsClass;

public class FacilityDispensedToUserReport {
    public FacilityDispensedToUserReport() {
    }
    
    
    public static FacilityTemplate getFacTypesMainNameItem(String fac) {
       return UtilsClass.getFacTypesMainNameItem(fac);
    }      
    public static String getDate() {
        return UtilsClass.getDate();
    }
    public static String getTime() {
        return UtilsClass.getTime();
    }
    
    public static Double getFacDispensedToUserRepMainSum(String dir, String quart, String year, String dat, String fY,
                                                  String tY, String fM, String tM, String type, String hq) {
    return UtilsClass.getFacDispensedToUserRepMainSum(dir, quart, year, dat, fY, tY, fM, tM, type, hq)    ;
                                                  }
    
    public static String supplierId(String supplier){
     return UtilsClass.supplierId(supplier)   ;
    }
    public static String getGroupName(String group) {
        return UtilsClass.getGroupName(group);
    }
    
    public static List<FacilityTemplate> getDirectoratesList(String[] directorates) {
        return UtilsClass.getFacilitiesList(directorates);
    }
    
    public static List<FacilityDispensed> facilityDispensedToUserReport(String mon, String year, String dat,
                                                                     String fY, String tY, String fM, String tM,
                                                                     String hq, String supplier, String type) {
        return UtilsClass.facilityDispensedToUserReport(mon, year, dat, fY, tY, fM, tM, hq, supplier, type);
    }
}
