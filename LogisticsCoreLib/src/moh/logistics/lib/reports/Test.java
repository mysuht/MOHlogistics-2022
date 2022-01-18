package moh.logistics.lib.reports;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;

import javax.sql.DataSource;

import moh.logistics.lib.reports.code.DispensedToUser;
import moh.logistics.lib.reports.code.FacilityTemplate;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class Test {
    
  

    public static void main(String[] args) {
      //  Test test = new Test();
        
//          ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml", Test.class);
//          //AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ReportConfig.class);
//          DataSource dataSource = (DataSource) ac.getBean("dataSource");


          //JdbcTemplate jdbcTemplate =  JDBCConfig.DataSourceJdbcTempateInfo(); //new JdbcTemplate(dataSource);
          //System.out.println(jdbcTemplate.queryForList("select * from product").size());
          //List<Map<String, Object>> results = jdbcTemplate.queryForList("select * from product");
         // LogisticsReportsClass c = new LogisticsReportsClass();
         // System.out.println(c.getFacilitiesByLevelSTMT(2,new String[]{"3"},"a"));
//            for(Map row : results){
//                System.out.println(row.get("prod_id"));
//                System.out.println(row.get("pro_name"));
//                System.out.println(row.get("prod_cyp"));
//                System.out.println(row.get("pro_dose"));
//                
//         //   }
            //String[]faces = {"3", "287", "102"};
            //String[]faces = { "287", "102"};
          // String[]faces = {"15", "48", "49", "61"};
            String[]faces = null;
            
           List<FacilityTemplate> dirList =  DispensedToUser.getDirectoratesList(null);
                       for(FacilityTemplate directorate: dirList){
                           List<FacilityTemplate> lftList = UtilsClass.supplyStatusFacilityList
                           // ("4", "01", "2016", "m", "", "", "", "", "", faces, "498");
                           ("4", "01", "2016", "m", "", "", "", "", "", faces, directorate.getFacilityId()+"");
                           // ("4", "01", "2016", "m", "", "", "", "", "", faces, "61");
                           for(FacilityTemplate ft: lftList){
                               System.out.println(ft.getFacilityId() + " xxxxx " + ft.getFacilityName() +" xxxxxxxxxxx " +
                                                  ft.getSupCode() + " xxxxxxxxxxxxx " + ft.getDispensed()+ " xxxx "+ ft.getProgram()
                                                  
                                                  + " xxxx "+ ft.getAvg()
                                                  + " xxxx "+ ft.getIssues()
                                                  + " xxxx "+ ft.getReceipts()
                                                  + " xxxx "+ ft.getAdjustments());
                           }
                           System.out.println(" ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ ");
                           
                       }
            
            
            

//            List<FacilityTemplate> dirList =  DispensedToUser.getDirectoratesList(null);
//            for(FacilityTemplate directorate: dirList){
//                System.out.println(directorate.getFacilityId() + " xxxxx " + directorate.getFacilityName() 
//                                   +" xxxxxxxxxxx " + directorate.getFacilityType() + 
//                                   " xxxxxxxxxxxxx " + directorate.getDispensed()+ " xxxx "+ directorate.getProgram());
//            }
                //(prod, mon, year, dat, fY, tY, fM, tM, hq, facilities, type);
//          jdbcTemplate.batchUpdate(new String[] { "update customer set first_name = 'FN#'",
//              "delete from customer where id > 2" });

       
    
    }
}
