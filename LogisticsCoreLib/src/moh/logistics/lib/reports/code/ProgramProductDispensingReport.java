package moh.logistics.lib.reports.code;

import java.util.List;

import moh.logistics.lib.reports.UtilsClass;

public class ProgramProductDispensingReport {
    public ProgramProductDispensingReport() {
        super();
    }
    
    public static List<FacilityTemplate> getDirectoratesList(String mon, String year,
                                                                     String dat, String prod, String fY, String tY,
                                                                     String fM, String tM, String hq,
                                                                     String directorates[]) {
        return UtilsClass.getDirectoratesList( mon, year, dat, prod, fY, tY, fM, tM, hq, directorates);
    }
}
