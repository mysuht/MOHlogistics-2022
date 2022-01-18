package com.logistics.lib.report;
import com.logistics.lib.dao.CTFItemDAO;
import com.logistics.lib.dao.CTFMainDAO;
import com.logistics.lib.dao.FacilityDAO;
import com.logistics.lib.dto.CTFItem;
import com.logistics.lib.dto.CTFMain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import moh.logistics.lib.reports.UtilsClass;
import moh.logistics.lib.reports.code.FacilityTemplate;
import moh.logistics.lib.reports.code.domain.Facility;

public class FacilityProductDispensedReport {
    public FacilityProductDispensedReport() {
        super();
    }

    public static List<FacilityTemplate> getFacilityDispensedRepAllIntersect(String mon, String year, String products[],
                                                                             String dat, String fY, String tY,
                                                                             String fM, String tM, String type,
                                                                             String hq, String supplierID,
                                                                             String[] centers) {
        //   return UtilsClass.getFacilityDispensedRepAllIntersect(quart, year, prod, dat, fY, tY, fM, tM, type, hq, facSup, centers);
        List<FacilityTemplate> returnFacilityList = new ArrayList<>();
        try {
            Connection connection = UtilsClass.jdbcConnection();
            FacilityDAO facilityDAO = new FacilityDAO(connection);
            CTFMainDAO ctfMainDAO = new CTFMainDAO(connection);
            CTFItemDAO ctfItemDAO = new CTFItemDAO(connection);
            List<com.logistics.lib.dto.Facility> facilityOfSupplierList =
                facilityDAO.getFacilityBySupplier(supplierID, centers);
            for (com.logistics.lib.dto.Facility facility : facilityOfSupplierList) {
                List<CTFMain> ctfMainList =
                    ctfMainDAO.getFacilityDates(mon, year, dat, fY, tY, fM, tM, hq, facility.getId());
                boolean condition = false;
                for (CTFMain ctfMain : ctfMainList) {
                    List<CTFItem> ctfItemList = ctfItemDAO.findFacilityItems(products, ctfMain.getId());
                    condition = CheckDispensedCTFItems2(ctfItemList);
                    if (condition)
                        break;
                    else
                        continue;
                }

                if (condition) {
                    FacilityTemplate facilityTemplate = new FacilityTemplate();
                    facilityTemplate.setFacCode(facility.getFacilityCode());
                    facilityTemplate.setFacilityId(facility.getId());
                    facilityTemplate.setFacilityName(facility.getFacilityName());
                    facilityTemplate.setFacilityTypeHierarchyId(UtilsClass.GETFacilityTypeID(connection,
                                                                                             facilityTemplate.getFacilityId() +
                                                                                             ""));
                    returnFacilityList.add(facilityTemplate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnFacilityList;

    }
    // CheckDispensedCTFItems2 : all products condition in one month
    // CheckDispensedCTFItems : at least one product condition in one month
    private static boolean CheckDispensedCTFItems2(List<CTFItem> ctfItemList) {
        boolean condition = false;
        for (CTFItem item : ctfItemList) {
            if (true) {
                if (item.getIssues() > 0) {
                    condition = true;
                } else {
                    condition = false;
                    break;
                }
            }
        }
        return condition;
    }

    private static boolean CheckDispensedCTFItems(List<CTFItem> ctfItemList) {
        boolean condition = false;
        for (CTFItem item : ctfItemList) {
            if (item.getIssues() > 0) {
                condition = true;
                break;

            } else {
                condition = false;
                continue;
            }

        }
        return condition;
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
                                                                     String hq, String facilities[], String type,
                                                                     String facType) {
        return UtilsClass.dispensedToUserFacilityList(prod, mon, year, dat, fY, tY, fM, tM, hq, facilities, type,
                                                      facType);
    }

    public static List<FacilityTemplate> FacilityProductDispensedList(String supplier, String mon, String year,
                                                                      String dat, String prod[], String fY, String tY,
                                                                      String fM, String tM, String hq,
                                                                      String centers[]) {
        return UtilsClass.FacilityProductDispensedList(supplier, mon, year, dat, prod, fY, tY, fM, tM, hq, centers);
    }
}
