package moh.logistics.lib.reports.code;

import java.io.Serializable;

@SuppressWarnings("oracle.jdeveloper.java.serialversionuid-field-missing")
public class Report implements Serializable {
    private int reportID;
    private String reportName;

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportName() {
        return reportName;
    }
}
