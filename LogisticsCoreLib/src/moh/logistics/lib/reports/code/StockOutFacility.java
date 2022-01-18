package moh.logistics.lib.reports.code;

import java.util.List;
import java.util.Map;

import moh.logistics.lib.reports.MainInterface;

import moh.logistics.lib.reports.JDBCTemplateInfo;

import moh.logistics.lib.reports.UtilsClass;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public class StockOutFacility {
    
 
//    String GetTypeHierarchy(String FacilityId) {
//        String query = "select "
//    }
   
    String StockOutFacilityQuery(String prod, String mon, String year, String dat, String fY,
                                                            String tY, String fM, String tM, String type, String hq,
                                                            String FacilityId) {
             String StockOutFacilityQuery_1 = " select \n" +
                      " cm.p_Date, f.facility_id, "+
                      " ci.prod_id, nvl(ci.adjustments,0) as ADJUSTMENTS, nvl(ci.adj_type_id,0) AS ADJ_TYPE_ID , adj.type_name   \n" +
                      " , nvl(closing_bal,0) AS CLOSE_BAL, nvl(avg_mnthly_cons,0) AS AVG, decode(nvl(avg_mnthly_cons,0), 0, 0, Round(closing_bal / avg_mnthly_cons, 2) ) as MOS "+
                      " from ctf_main cm, ctf_item ci, facility f, fac_type ft, adj_type adj \n" +
                      " where ft.fac_type_id = f.fac_type_id\n" +
                      " and   f.facility_id = cm.facility_id\n" +
                      " and cm.ctf_main_id = ci.ctf_main_id\n" +
                      " and nvl(ci.adj_type_id,0) = adj.adj_type_id(+) \n"  +
                      " and ci.prod_id = " + prod + "  \n" ;
             
             String StockOutFacilityQuery_2 = " and f.facility_id = "+FacilityId;
          
             
             String query = StockOutFacilityQuery_1;
             String innerClause = UtilsClass.innerClause(mon, year, dat, fY, tY, fM, tM, hq);
             query += innerClause;
             query += StockOutFacilityQuery_2;
             System.out.println(query);
                
                return query;
         }
    
    
    
    
    JdbcTemplate jdbcTemplate = JDBCTemplateInfo.DataSourceJdbcTempateInfo();

    public StockOutFacility() {

    }

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


    /**
     * @param prod
     * @param mon
     * @param year
     * @param dat
     * @param fY
     * @param tY
     * @param fM
     * @param tM
     * @param type
     * @param hq
     * @param facilities
     * @return
     */
    public List<Map<String, Object>> GetStockOutFacility(String prod, String mon, String year, String dat, String fY,
                                                           String tY, String fM, String tM, String type, String hq,
                                                           String FacilityId) {
        String query  = StockOutFacilityQuery(prod, mon, year, dat, fY, tY, fM, tM, type, hq, FacilityId);
        System.out.println(query);
        List<Map<String, Object>> results =
            jdbcTemplate.queryForList(query);
        return results;
    }

   
    
   

}
