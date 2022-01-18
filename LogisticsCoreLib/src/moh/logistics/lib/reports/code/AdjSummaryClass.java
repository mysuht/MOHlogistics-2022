package moh.logistics.lib.reports.code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import moh.logistics.lib.reports.JDBCConfig;
import org.springframework.jdbc.core.JdbcTemplate;

public class AdjSummaryClass extends LogisticsReportsClass {
    public AdjSummaryClass(){
        
    }
    String mainQuery = null, 
          mainQueryParams = null,
         query = null;
    /*
     * Connection Info
     */
    
        JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
    /*
     * Main Methods Classes
     */
        
    public static ResultSet dirsLVL2(String dirs[], String type) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            String dir = null;
            for(int index = 0; index < dirs.length; index++){
                if(index == 0)
                dir = dirs[index];
                else
                dir += ", "+dirs[index];
            }
           
            String sql =
            "select f.facility_id , f.fac_name from facility f, fac_type ft ";
            sql += " where f.fac_type_id = ft.fac_type_id(+) ";
            if (type.equals("0"))
                sql += " and type_hierarchy in (1,2) ";
            else
                sql += " and facility_id in ("+dir+",498) ";
                
            sql += " order by 2";
            conn = JDBCConfig.dataSource().getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }

        return rs;
    }
    public List<Map<String,Object>> getAdjSummaryMainList(String prod, String mon, String year, String dir, String dat, String fY,
                                       String tY, String fM, String tM, String type, String hq, String facilities[]) {
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
        mainQuery = AdjSummaryQueriesInterface.findAdjustments;
        mainQueryParams = 
        FindAdjustments(mainQuery,  prod,  dir,type,  facilities);
        query = LogisticsReportsClass.QueryProcessor(mainQueryParams, mon, year, dat, fY, tY, fM, tM, hq);
        System.out.println(query);
        List <Map<String, Object>> results = jdbcTemplate.queryForList(query);
        return results;
    }
    
    
    public List<Map<String,Object>> getAdjSummaryList(String prod, String mon, String year, String dir, String adj, String dat, String fY,
                                   String tY, String fM, String tM, String type, String hq, String facilities[]) {
        
         mainQuery = AdjSummaryQueriesInterface.adjSummaryMainQuery;
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
            }else {
             if ( FacilityLVL(facilities[0]) == 2 ){
                if ( FacilityLVL(dir) == 1 ){
       
                sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir + " and ff.facility_id in ("+faces+" ) )" ;
                }else  if ( FacilityLVL(dir) == 2 ){
               
                sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir +" )" ;
                }
            }  if ( FacilityLVL(facilities[0]) == 3 ){
                if ( FacilityLVL(dir) == 1 ){
                
                sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir + " and ff.facility_id in ("+faces+" ) )" ;
                }else  if ( FacilityLVL(dir) == 2 ){
                
                sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir +" and ff.facility_id in ("+faces+" ) )" ;
                } 
            }
            
            }
      //  }else{
      //      sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir + " ) ";
       //     sql += " and cm.facility_id in ("+faces+")";
       // }
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
        }else if ( type.equals("0")){
            
            sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir + " ) ";
            }else{
             if ( FacilityLVL(facilities[0]) == 2 ){
                if ( FacilityLVL(dir) == 1 ){
       
                sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir + " and ff.facility_id in ("+faces+" ) )" ;
                }else  if ( FacilityLVL(dir) == 2 ){
               
                sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir +" )" ;
                }
            }  if ( FacilityLVL(facilities[0]) == 3 ){
                if ( FacilityLVL(dir) == 1 ){
                
                sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir + " and ff.facility_id in ("+faces+" ) )" ;
                }else  if ( FacilityLVL(dir) == 2 ){
                
                sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir +" and ff.facility_id in ("+faces+" ) )" ;
                } 
            }
            
            
      //  }else{
      //      sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir + " ) ";
       //     sql += " and cm.facility_id in ("+faces+")";
       // }
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
