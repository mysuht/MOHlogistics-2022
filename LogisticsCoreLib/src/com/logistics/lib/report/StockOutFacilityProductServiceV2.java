package com.logistics.lib.report;
import com.logistics.lib.dao.CTFItemDAO;
import com.logistics.lib.dao.CTFMainDAO;
import com.logistics.lib.dao.FacilityDAO;
import com.logistics.lib.dto.CTFItem;
import com.logistics.lib.dto.CTFMain;

import com.logistics.lib.dto.Facility;

import com.logistics.lib.util.DataAccessObject;

import com.logistics.lib.util.DataTransferObject;

import com.logistics.lib.util.DatabaseConnectionManager;

import java.sql.Connection;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import moh.logistics.lib.reports.UtilsClass;
import moh.logistics.lib.reports.code.FacilityTemplate;

public class StockOutFacilityProductServiceV2 {

    public static void main(String[] args) {
        String[] products = {"10","11","12", "3","4","9"};
        String [] centers = {"373"};
        String supplierID = "352";
        String month = "10";
        String year = "2018";
        int value = getStockOutFacilityProducts(month, year, "m", "", "", "", "", "", products, supplierID, centers).size();
        System.out.println(value);
    }
    
    public static String getProdName(String prod) {   
     return UtilsClass.getProdName(prod);
    }
    
    public static List<FacilityTemplate> getFacTypesMainName(String typeID) {
     return UtilsClass.getFacTypesMainName(typeID)   ;
    }
    
    public static List<FacilityTemplate> getDirectoratesList(String mon, String year,
                                                                     String dat, String prod, String fY, String tY,
                                                                     String fM, String tM, String hq,
                                                                     String directorates[]) {
        return UtilsClass.getDirectoratesList(mon, year, dat, prod, fY, tY, fM, tM, hq, directorates);
                                                                     }

    public static String getDate() {
     return UtilsClass.getDate();   
    }
    
    public static String getTime() {
     return UtilsClass.getTime();   
    }
    
    public static List<FacilityTemplate> getDirectoratesList2ndLVL(String[] directorates) {
     return UtilsClass.getDirectoratesList2ndLVL(directorates)   ;
    }
    public static List<FacilityTemplate> getStockOutFacilityProducts(String mon, String year, String dat, String fY, String tY,
                                                             String fM, String tM, String hq, String[] products,
                                                             String supplierID, String[] centers) {
        
        List<FacilityTemplate> returnFacilityList = new ArrayList<>();
        try {
            Connection connection = UtilsClass.jdbcConnection();
            FacilityDAO facilityDAO = new FacilityDAO(connection);
            CTFMainDAO ctfMainDAO = new CTFMainDAO(connection);
            CTFItemDAO ctfItemDAO = new CTFItemDAO(connection);
            List<Facility> facilityOfSupplierList = facilityDAO.getFacilityBySupplier(supplierID, centers);
            for (Facility facility : facilityOfSupplierList) {
                List<CTFMain> ctfMainList =
                    ctfMainDAO.getFacilityDates(mon, year, dat, fY, tY, fM, tM, hq, facility.getId());
                boolean condition = false;
                for (CTFMain ctfMain : ctfMainList) {
                    List<CTFItem> ctfItemList = ctfItemDAO.findFacilityItems(products, ctfMain.getId());
                    condition = CheckCTFItems2( ctfItemList);
                    if(condition)
                        break;
                    else
                        continue;
                }
                
                if (condition){
                    FacilityTemplate facilityTemplate = new FacilityTemplate();
                    facilityTemplate.setFacCode(facility.getFacilityCode());
                    facilityTemplate.setFacilityId(facility.getId());
                    facilityTemplate.setFacilityName(facility.getFacilityName());
                    facilityTemplate.setFacilityTypeHierarchyId(UtilsClass.GETFacilityTypeID(connection, facilityTemplate.getFacilityId()+""));
                    returnFacilityList.add(facilityTemplate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnFacilityList;
    }

    private static boolean CheckCTFItems1(List<CTFItem> ctfItemList) {
        boolean condition = false;
        int index = 0;
        for (CTFItem item : ctfItemList) {         
            String adjTypeID = (item.getAdjTypeId() + "");
            if (!adjTypeID.toString().equals("12") || !adjTypeID.toString().equals("17") ||
                !adjTypeID.toString().equals("19")) {
                if ((item.getAvgMnthlyCons() > 0 && (item.getClosingBalance()+"").equals("0"))) {
                    condition = true;
                        break;
              
                }else {
                        condition = false;
                        continue;
                    }
            } 
                else {
                    condition = false;
                    continue;
                }
            
        }
        return condition;
    }

    private static boolean CheckCTFItems2(List<CTFItem> ctfItemList) {
        boolean condition = false;
        for (CTFItem item : ctfItemList) {
            
            String adjTypeID = (item.getAdjTypeId() + "");
            if (!adjTypeID.toString().equals("12") || !adjTypeID.toString().equals("17") ||
                !adjTypeID.toString().equals("19")) {
                if ((item.getAvgMnthlyCons() > 0 && (item.getClosingBalance()+"").equals("0"))) {
                    condition = true;
                }else{
                    condition = false;
                    break;
                }
            } 
        }
        return condition;
    }
    
    public static List<FacilityTemplate> getDirectoratesList(String[] directorates) {
     return UtilsClass.getDirectoratesList(directorates)   ;
    }
}

