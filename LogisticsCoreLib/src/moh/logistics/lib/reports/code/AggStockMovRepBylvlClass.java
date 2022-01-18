package moh.logistics.lib.reports.code;

import java.sql.ResultSet;

import java.util.List;
import java.util.Map;

import moh.logistics.lib.reports.JDBCTemplateInfo;

import moh.logistics.lib.reports.MainQueriesInterface;

import org.springframework.jdbc.core.JdbcTemplate;

public class AggStockMovRepBylvlClass extends LogisticsReportsClass {
    
    public AggStockMovRepBylvlClass(){
        
    }
    
    String mainQuery = null, 
          mainQueryParams = null,
         query = null;
    /*
     * Connection Info
     */
    
        JdbcTemplate jdbcTemplate = JDBCTemplateInfo.DataSourceJdbcTempateInfo();
    /*
     * Main Methods Classes
     */
        
    public List<Map<String,Object>> getAggStockMovByLvlList(String prod, String mon, String year, String dir, String dat, String fY,
                                       String tY, String fM, String tM, String type, String hq, String facilities[]) {
        String closingBalQuery = 
        " select  distinct sum(cci.closing_bal) cbal, " +
        "ccm.p_date  \n" + 
        //"-- ,ROW_NUMBER() OVER (ORDER BY last_updated ASC) row_id \n" + 
        "from ctf_item cci, ctf_main ccm , facility ff, fac_type fft \n" + 
        "where cci.ctf_main_id = ccm.ctf_main_id \n" + 
        "and ff.facility_id = ccm.facility_id \n" + 
        "and fft.fac_type_id = ff.fac_type_id \n" + 
        "and fft.type_hierarchy = ? \n " +
        "and cci.prod_id = ? \n" +
        "-- and ff.active = 1 \n" + 
        "group by ccm.p_date \n" +
        "order by ccm.p_date desc"  ;
        
        
        
        String mainQuery = "select ft.type_hierarchy as type_hierarchy, \n" +
        "count((select  mf.facility_id from " + 
        "facility mf  where  mf.facility_id = f.facility_id)) as ourCount,\n" + 
        
       
        
        "sum(receipts), sum(issues),sum(adjustments), cm.p_date \n" +
        "from ctf_main cm, ctf_item ci, \n" + 
        "facility f, fac_type ft\n" + 
        "-- ,group_of_facilities gof, groups g\n" + 
        "where f.facility_id = cm.facility_id\n" + 
        "and cm.ctf_main_id = ci.ctf_main_id\n" + 
        "and f.fac_type_id = ft.fac_type_id\n" + 
        "--and ft.fac_type_id = gof.gof_facility_id\n" + 
        "--and gof.gof_grp_id = g.grp_id\n" + 
        "-- and f.active = 1 \n" + 
        "and ci.prod_id = ? \n" + 
        "and to_char(cm.p_date,'mm/yyyy') between '01/2016' and '05/2016'\n" + 
        "group by ft.type_hierarchy , cm.p_date\n" + 
        "order by 3";
        
        
        
        String faces = " ";
        if ( facilities != null){
            for(int i=0; i < facilities.length; i++){
                if(i == 0){
                    faces =  facilities[i];
                }else{
                faces += ", "+ facilities[i];
                }
            }
        }
        System.out.println(" size  of array llllllllllllll "+faces);
      
        mainQueryParams = 
        FindAdjustments(mainQuery,  prod,  dir,type,  facilities);
        query = LogisticsReportsClass.QueryProcessor(mainQueryParams, mon, year, dat, fY, tY, fM, tM, hq);
        System.out.println(query);
        List <Map<String, Object>> results = jdbcTemplate.queryForList(query);
        return results;
    }
    
    
    public List<Map<String,Object>> getAdjSummaryList(String prod, String mon, String year, String dir, String adj, String dat, String fY,
                                   String tY, String fM, String tM, String type, String hq, String facilities[]) {
        
         mainQuery = MainQueriesInterface.adjSummaryMainQuery;
         mainQueryParams = 
         adjSummaryListQuery(mainQuery,  prod,  dir,  adj,type, facilities);
         query = LogisticsReportsClass.QueryProcessor(mainQueryParams, mon, year, dat, fY, tY, fM, tM, hq);
         query +=
        " group by f.fac_name, adj.type_name, adj.adj_type_id,adj.always_negative";
        List <Map<String, Object>> results = jdbcTemplate.queryForList(query);
         return results;
    }
    
    
    
    public String FindAdjustments(String sql, String prod, String dir, String type, String facilities[]){
        String faces = " ";
        if ( facilities != null){
        for(int i=0; i < facilities.length; i++){
            if(i == 0){
                faces =  facilities[i];
            }else{
            faces += ", "+ facilities[i];
            }
        }
        }
        if(type.equals("mch")){
            sql += " and cm.facility_id in ("+dir+")"; 
        }else{
            if (type.equals("0")){
            sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir + " ) ";
            }else if ( FacilityLVL(facilities[0]) != 3 ){
       
        sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir + " ) ";
        }else{
            sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir + " ) ";
            sql += " and cm.facility_id in ("+faces+")"; 
        }
        }
        sql += " and ci.prod_id=" + prod ;
        return sql;
    }
    
    
    public String  adjSummaryListQuery(String sql, String prod, String dir, String adj, String type, String facilities[]){
        String faces = " ";
        if ( facilities != null){
        for(int i=0; i < facilities.length; i++){
            if(i == 0){
                faces =  facilities[i];
            }else{
            faces += ", "+ facilities[i];
            }
        }
        }
        if(type.equals("mch")){
          //  System.out.println("dddddddddddddddddddddddddddddddddddddd");
            sql += " and cm.facility_id in ("+dir+")";
        }else{
            if ( type.equals("0")){
            sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir + " ) ";
            }else  if ( FacilityLVL(facilities[0]) != 3 ){
       
        sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir + " ) ";
        }else{
            sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir + " ) ";
            sql += " and cm.facility_id in ("+faces+")";
        }
        }
        sql += " and adj.adj_type_id=" + adj ;
        sql += " and ci.prod_id=" + prod ;
        System.out.println("kkkkkkkkkkk "+sql);
        return sql;
    }
    
    
    /*
     * OTHER METHODS
     */
    
    public List<Map<String, Object>> getFacTypesMainNameListD(String fac) {
    System.out.println("place ");
        String sql = "select fac_type_id, ";
            sql += " fac_name, type_hierarchy ";
            sql += " from fac_type where fac_type_id=" + fac;
        System.out.println(sql);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
       
        return results;

    }
    
    /*
     * Helper Methods
     */
    
    public  int FacilityLVL(String facility){
        String sql = "select type_hierarchy " +
            "from fac_type ft, facility f " +
            "where f.fac_type_id=ft.fac_type_id " + 
            "and f.facility_id = "+facility;
    
        return jdbcTemplate.queryForInt(sql);
    }

    @Override
    public String getDate() {
        // TODO Implement this method
        return super.getDate();
    }


    @Override
    public String getTime() {
        // TODO Implement this method
        return super.getTime();
    }

    @Override
    public String getProdName(String prod) {
        // TODO Implement this method
        return super.getProdName(prod);
    }

    @Override
    public ResultSet getProducts() {
        // TODO Implement this method
        return super.getProducts();
    }

    @Override
    public ResultSet getProduct() {
        // TODO Implement this method
        return super.getProduct();
    }

    @Override
    public double getProdQty(String prod) {
        // TODO Implement this method
        return super.getProdQty(prod);
    }

    @Override
    public String getprodId(String pro) {
        // TODO Implement this method
        return super.getprodId(pro);
    }

    @Override
    public String getProdDose(String prod) {
        // TODO Implement this method
        return super.getProdDose(prod);
    }

    @Override
    public String getDirName(String dir) {
        // TODO Implement this method
        return super.getDirName(dir);
    }

    @Override
    public ResultSet getDirectorates() {
        // TODO Implement this method
        return super.getDirectorates();
    }


    @Override
    public int getFacilityMaster() {
        // TODO Implement this method
        return super.getFacilityMaster();
    }
}
