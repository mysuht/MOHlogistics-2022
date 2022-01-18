package moh.logistics.lib.reports.code;

import java.util.List;

import moh.logistics.lib.reports.UtilsClass;

public class ServiceStatisticsReport {
    public static List<FacilityTemplate> serviceStatisticsReportList(String prod, String mon, String year, String dat,
                                                                     String fY, String tY, String fM, String tM,
                                                                     String hq, String facilities[],String supplier, String type) {
        return UtilsClass.serviceStatisticsReportList(prod, mon, year, dat, fY, tY, fM, tM, hq, facilities,supplier, type);
                                                                     }
}
