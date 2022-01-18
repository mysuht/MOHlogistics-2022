package moh.logistics.lib.reports.code;

import java.sql.ResultSet;

import java.util.List;
    import java.util.Map;

    import moh.logistics.lib.reports.MainInterface;

import moh.logistics.lib.reports.JDBCTemplateInfo;

import moh.logistics.lib.reports.UtilsClass;

import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
public class StockOutFacilityByProduct {


    public String getDirCode(String dir) {
        String sql = "select fac_code from facility where facility_id=" + dir;
        String dirCode = jdbcTemplate.queryForObject(sql, String.class) ;
        
        return dirCode;
    }


       
        String StockOutFacilityProductsQuery(String prod[], String mon, String year, String dat, String fY,
                                                                String tY, String fM, String tM, String type, String hq,
                                                                String FacilityId) {
                 String prodList = " ";
                 if (prod != null) {
                     for (int i = 0; i < prod.length; i++) {
                         if (i == 0) {
                             prodList = prod[i];
                         } else {
                             prodList += ", " + prod[i];
                         }
                     }
                 }
                 String StockOutFacilityQuery_1 = " select \n" +
                          " cm.p_Date, f.facility_id, "+
                          " ci.prod_id, nvl(ci.adjustments,0) as ADJUSTMENTS, nvl(ci.adj_type_id,0) AS ADJ_TYPE_ID , adj.type_name   \n" +
                          " , nvl(closing_bal,0) AS CLOSE_BAL, nvl(avg_mnthly_cons,0) AS AVG, " + 
                          " decode(nvl(avg_mnthly_cons,0), 0, 0, Round(closing_bal / avg_mnthly_cons, 2) ) as MOS "+
                          " from ctf_main cm, ctf_item ci, facility f, fac_type ft, adj_type adj \n" +
                          " where ft.fac_type_id = f.fac_type_id\n" +
                          " and   f.facility_id = cm.facility_id\n" +
                          " and cm.ctf_main_id = ci.ctf_main_id\n" +
                          " and nvl(ci.adj_type_id,0) = adj.adj_type_id(+) \n"  +
                          " and ci.prod_id in ( " + prodList + " )  \n" ;
                 
                 String StockOutFacilityQuery_2 = " and f.facility_id = "+FacilityId;
              
                 
                 String query = StockOutFacilityQuery_1;
                 String innerClause = UtilsClass.innerClause(mon, year, dat, fY, tY, fM, tM, hq);
                 query += innerClause;
                 query += StockOutFacilityQuery_2;
                 System.out.println(query);
                    
                    return query;
             }
        
        
        
        
        JdbcTemplate jdbcTemplate = JDBCTemplateInfo.DataSourceJdbcTempateInfo();


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


        public List<Map<String, Object>> GetStockOutFacilityByProduct(String prod[], String mon, String year, String dat, String fY,
                                                               String tY, String fM, String tM, String type, String hq,
                                                               String FacilityId) {
            String query  = StockOutFacilityProductsQuery(prod, mon, year, dat, fY, tY, fM, tM, type, hq, FacilityId);
            System.out.println(query);
            List<Map<String, Object>> results =
                jdbcTemplate.queryForList(query);
            return results;
        }
        
//    public List<FacilityTemplate> GetStockOutFacilityProduct(String prod[], String mon, String year, String dat, String fY,
//                                                           String tY, String fM, String tM, String type, String hq,
//                                                           String SupplierId, String centers[]) {
//        String sql = " select  cm.p_Date";
//               sql += " , f.facility_id ";
//               sql += " , ci.prod_id";
//               sql += " , nvl(ci.adjustments,0) as ADJUSTMENTS ";
//               sql += " , nvl(ci.adj_type_id,0) AS ADJ_TYPE_ID ";
//               sql += " , adj.type_name   \n" ;
//               sql += " , nvl(closing_bal,0) AS CLOSE_BAL";
//               sql += " , nvl(avg_mnthly_cons,0) AS AVG " ;
//               sql += " decode(nvl(avg_mnthly_cons,0), 0, 0, Round(closing_bal / avg_mnthly_cons, 2) ) as MOS ";
//               sql += " from ctf_main cm, ctf_item ci, facility f, fac_type ft, adj_type adj \n "; 
//               sql += " where ft.fac_type_id = f.fac_type_id\n" ;
//               sql += " and   f.facility_id = cm.facility_id\n" ;
//               sql += " and cm.ctf_main_id = ci.ctf_main_id\n" ;
//               sql += " and nvl(ci.adj_type_id,0) = adj.adj_type_id(+) \n"  ;
//               sql += " and ci.prod_id in ( " + prodList + " )  \n" ;
//        sql += UtilsClass.innerClause(mon, year, dat, fY, tY, fM, tM, hq);
//        sql += UtilsClass.facilitiesOfSupplier(SupplierId, centers);
//        String query  = StockOutFacilityProductsQuery(prod, mon, year, dat, fY, tY, fM, tM, type, hq, FacilityId);
//        System.out.println(query);
//        List<Map<String, Object>> results =
//            jdbcTemplate.queryForList(query);
//        return results;
//    }

}
