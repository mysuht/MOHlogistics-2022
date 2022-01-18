package moh.logistics.lib.reports;

import java.util.List;
import java.util.Map;

public abstract class LogisticsMainQueries {
    public String mainSqlQueryProccessorTxt(String sql, String mon, String year, String dat, String fY, String tY,
                                            String fM, String tM, String hq) {
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") &&
                !mon.equals("0")) {
                   sql += " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
                }
            if (dat.equals("q") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" +  year + "'";
                sql += " and to_char(cm.p_date,'Q')= " +mon;
                
               
            }
            if (mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" +  year + "'";
            }

        }
        if (dat.equals("u")) {
            sql += "  and to_date('" + tM + "/" + tY + "','mm/yyyy')  ";
            sql +=" between to_date('" + fM + "/" + fY + "','mm/yyyy') ";
            sql += " and to_date('" + tM + "/" + tY + "','mm/yyyy') ";
               
        }


        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'";
                sql += " and to_char(cm.p_date,'Q') in ('1','2') ";
                
            }
            if (hq.equals("2")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'";
                sql += " and to_char(cm.p_date,'Q') in ('3','4') ";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        
        return sql;
    }

    public abstract List<Map<String, Object>> getAdjSummaryList(String query);

   

    public abstract String 
    adjSummaryListQueryParameters(String prod, String dir, String adj);
}
