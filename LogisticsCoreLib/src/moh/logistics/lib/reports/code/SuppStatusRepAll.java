package moh.logistics.lib.reports.code;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.List;
import java.util.Map;

import moh.logistics.lib.reports.JDBCConfig;

import java.sql.Connection;

import java.sql.Statement;

import moh.logistics.lib.reports.MainInterface;

import oracle.jdbc.pool.OracleDataSource;



import org.springframework.jdbc.core.JdbcTemplate;

public class SuppStatusRepAll {
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    public SuppStatusRepAll() {
    }
    String jdbcUrl = MainInterface.thinConn;
    String userid = MainInterface.username;
    String password = MainInterface.password; 
    Connection conn;
    Statement stmt = null;
    public void getDBConnection() throws SQLException{
        OracleDataSource ds;
        ds = new OracleDataSource();
        ds.setURL(jdbcUrl);
        //ds.setMaxStatements(40000);
        ds.setImplicitCachingEnabled(true);
        conn=ds.getConnection(userid,password);
        
    }
    
    public ResultSet getFacTypesMain1() {

        
        String sql =
            "(select fac_type_id, fac_name from fac_type ) union (select grp_id+100 , grp_desc from groups ) order by 2";
        try {
            getDBConnection();
            System.out.println(sql);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                           ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }
    
    
    
    
    public ResultSet getDirectorates(String type, String[]directorates) {
        String centersCollection = "";
        
        
        String sql =
            "select f.facility_id , f.fac_name from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id(+)  ";
        
        if(!type.equals("0") && getTypeLvl(directorates[0]).equals("2")){
            
            
            for (int index = 0; index <directorates.length; index++){
                if(index == 0)
                    centersCollection = directorates[index];
                else
                centersCollection += " , "+directorates[index] ;
                
                }
            
            sql +=  "and f.facility_id in ("+centersCollection+") ";
        }
            sql +=  "and type_hierarchy in (1,2)";

            
        
        sql += " order by 2";
        System.out.println(sql);
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                           ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }
    
    
    public String getTypeLvl(String facility) {
        String type  = null;
        String sql =
            "select typelvl("+facility+") from dual " ;
     
           
        
        
        
        System.out.println(sql);
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                           ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            type = rs.getString(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        System.out.println(" typelvl    "+type);
        return type;

    }
    
    
    public int getSupplyStatusRepBySupplierReceipts(String fac, String mon, String year, String prod,
                                                                  String dat, String fY, String tY, String fM,
                                                                  String tM, String type, String hq, String cens[], String ty) {
                    int receipts = 0;                                                        
        
        
                   String sql=  "select  f.fac_name as facname, " + 
                                "ft.fac_name as factype,  " +
                                "nvl(sum(ci.receipts),0) as receipts, \n";
                                sql += " nvl(sum(ci.issues),0) as issues " ;
                                    
                                sql += "  ,  sum(ci.adjustments) as adj,  ";
                                sql += " f.facility_id as facid, ft.fac_type_id \n";
                                sql += " from ctf_item ci, ctf_main cm, facility f, fac_type ft \n";
                                
                                sql += " where ci.ctf_main_id = cm.ctf_main_id(+) \n";
                                sql += " and cm.facility_id = f.facility_id(+) \n";
                                sql += " and f.fac_type_id= ft.fac_type_id(+) \n";
                                sql += " and ci.prod_id=" + prod + " \n";
    
        System.out.println(" ty ***  "+ty);
        if (type.equals("supp")) {
            System.out.println("  suht xxxxxxxxxxxxxxxxxxxxxx ");
            if(!ty.equals("0") ){
            String centersCollection = "";
            
            for (int index = 0; index <cens.length; index++){
                if(index == 0)
                    centersCollection = cens[index];
                else
                centersCollection += " , "+cens[index] ;
                
                }
               if ( getTypeLvl(cens[0]).equals("2")) {
                     sql += " and f.sup_code  in ("+centersCollection+" )";
                 }   else  {                                                
                    sql += " and f.facility_id in (select ff.facility_id from facility ff ";
                    sql += " where sup_code=" + fac + " and ff.facility_id in ("+centersCollection+")) \n";
                 }
            }else{
                
            sql += " and f.facility_id in (select ff.facility_id from facility ff ";
            sql += " where sup_code=" + fac + ") \n";
            }
        } else if (type.equals("indv")) {
            sql += " and f.facility_id = " + fac;
        }
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {

                sql += " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "' \n";

            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";
                sql += " and to_char(cm.p_date,'Q')='" + mon + "' \n";
            }
            if (mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";

            }
        }
        if (dat.equals("u")) {
            sql += " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') \n";
            sql += " and to_date('" + tM + "/" + tY + "','mm/yyyy') \n";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";
                sql += " and to_char(cm.p_date,'Q') in ('1','2')  \n";

            }
            if (hq.equals("2")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";
                sql += " and to_char(cm.p_date,'Q') in ('3','4') \n";
            }

        }
        sql += " group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id  \n";
        sql += " order by 1 ";
       
        try {
            System.out.println(sql);
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                           ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                receipts += rs.getInt(3);
            }

        } catch (SQLException e) {
           
            e.printStackTrace();
        } 
        return receipts;
    }
    
    
    
    
    JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
    
    public List<Map<String,Object>> getSupplyStatusRepBySupplierRSTemplate(String fac, String mon, String year, String prod,
                                                                  String dat, String fY, String tY, String fM,
                                                                  String tM, String type, String hq, String cens[], String ty) {
       String avg =
           " AVGMONVAL(f.facility_id, '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"') ";                                                            
       
       String closingBal =
           " CLOSEBALVAL(f.facility_id, '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"') ";                                                            
       
       String openingBal =
           " OPENBALVAL(f.facility_id, '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"') ";                                                            
       
       
                  String sql=  "select  f.fac_name as facname, " + 
                               "ft.fac_name as factype,  " +
                               "nvl(sum(ci.receipts),0) as receipts, \n";
                               sql += " nvl(sum(ci.issues),0) as issues, " + 
                                      "nvl("+closingBal+",0) as closingBal, \n";
                               sql += " decode(nvl("+avg+",0), 0,0,"+closingBal+"/nvl("+avg+",0)) as mos, \n";
                             
                               sql += " nvl("+avg+",0) as avg, ";
                               sql += " nvl("+openingBal+",0) as openingBal,  sum(ci.adjustments) as adj,  ";
                               sql += " f.facility_id as facid, ft.fac_type_id \n";
                               sql += " from ctf_item ci, ctf_main cm, facility f, fac_type ft \n";
                               
                               sql += " where ci.ctf_main_id = cm.ctf_main_id(+) \n";
                               sql += " and cm.facility_id = f.facility_id(+) \n";
                               sql += " and f.fac_type_id= ft.fac_type_id(+) \n";
                               sql += " and ci.prod_id=" + prod + " \n";
       
       System.out.println(" ty ***  "+ty);
       if (type.equals("supp")) {
           System.out.println("  suht xxxxxxxxxxxxxxxxxxxxxx ");
           if(!ty.equals("0") ){
           String centersCollection = "";
           
           for (int index = 0; index <cens.length; index++){
               if(index == 0)
                   centersCollection = cens[index];
               else
               centersCollection += " , "+cens[index] ;
               
               }
              if ( getTypeLvl(cens[0]).equals("2")) {
                    sql += " and f.sup_code  in ("+centersCollection+" )";
                }   else  {                                                
                   sql += " and f.facility_id in (select ff.facility_id from facility ff ";
                   sql += " where sup_code=" + fac + " and ff.facility_id in ("+centersCollection+")) \n";
                }
           }else{
               
           sql += " and f.facility_id in (select ff.facility_id from facility ff ";
           sql += " where sup_code=" + fac + ") \n";
           }
       } else if (type.equals("indv")) {
           sql += " and f.facility_id = " + fac;
       }
       if (dat.equals("m") || dat.equals("q")) {
           if (dat.equals("m") && !mon.equals("0")) {

               sql += " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "' \n";

           }
           if (dat.equals("q") && !mon.equals("0")) {
               sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";
               sql += " and to_char(cm.p_date,'Q')='" + mon + "' \n";
           }
           if (mon.equals("0")) {
               sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";

           }
       }
       if (dat.equals("u")) {
           sql += " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') \n";
           sql += " and to_date('" + tM + "/" + tY + "','mm/yyyy') \n";
       }
       if (dat.equals("hq")) {
           System.out.println("1st &&&&&&&&&&&&& 2nd half year");
           if (hq.equals("1")) {
               sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";
               sql += " and to_char(cm.p_date,'Q') in ('1','2')  \n";

           }
           if (hq.equals("2")) {
               sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";
               sql += " and to_char(cm.p_date,'Q') in ('3','4') \n";
           }

       }
       sql += " group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id  \n";
       sql += " order by 1 ";
       
       List<Map<String, Object>> results =
       jdbcTemplate.queryForList(sql);
       
       return results;
        
   }
    
    
    public ResultSet getSupplyStatusRepBySupplierRS(String fac, String mon, String year, String prod,
                                                                  String dat, String fY, String tY, String fM,
                                                                  String tM, String type, String hq, String cens[], String ty) {
        String avg =
            " AVGMONVAL(f.facility_id, '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"') ";                                                            
        
        String closingBal =
            " CLOSEBALVAL(f.facility_id, '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"') ";                                                            
        
        String openingBal =
            " OPENBALVAL(f.facility_id, '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"') ";                                                            
        
        
                   String sql=  "select  f.fac_name as facname, " + 
                                "ft.fac_name as factype,  " +
                                "nvl(sum(ci.receipts),0) as receipts, \n";
                                sql += " nvl(sum(ci.issues),0) as issues, " + 
                                       "nvl("+closingBal+",0) as closingBal, \n";
                                sql += " decode(nvl("+avg+",0), 0,0,"+closingBal+"/nvl("+avg+",0)) as mos, \n";
                              
                                sql += " nvl("+avg+",0) as avg, ";
                                sql += " nvl("+openingBal+",0) as openingBal,  sum(ci.adjustments) as adj,  ";
                                sql += " f.facility_id as facid, ft.fac_type_id \n";
                                sql += " from ctf_item ci, ctf_main cm, facility f, fac_type ft \n";
                                
                                sql += " where ci.ctf_main_id = cm.ctf_main_id(+) \n";
                                sql += " and cm.facility_id = f.facility_id(+) \n";
                                sql += " and f.fac_type_id= ft.fac_type_id(+) \n";
                                sql += " and ci.prod_id=" + prod + " \n";
  
        System.out.println(" ty ***  "+ty);
        if (type.equals("supp")) {
            System.out.println("  suht xxxxxxxxxxxxxxxxxxxxxx ");
            if(!ty.equals("0") ){
            String centersCollection = "";
            
            for (int index = 0; index <cens.length; index++){
                if(index == 0)
                    centersCollection = cens[index];
                else
                centersCollection += " , "+cens[index] ;
                
                }
               if ( getTypeLvl(cens[0]).equals("2")) {
                     sql += " and f.sup_code  in ("+centersCollection+" )";
                 }   else  {                                                
                    sql += " and f.facility_id in (select ff.facility_id from facility ff ";
                    sql += " where sup_code=" + fac + " and ff.facility_id in ("+centersCollection+")) \n";
                 }
            }else{
                
            sql += " and f.facility_id in (select ff.facility_id from facility ff ";
            sql += " where sup_code=" + fac + ") \n";
            }
        } else if (type.equals("indv")) {
            sql += " and f.facility_id = " + fac;
        }
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {

                sql += " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "' \n";

            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";
                sql += " and to_char(cm.p_date,'Q')='" + mon + "' \n";
            }
            if (mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";

            }
        }
        if (dat.equals("u")) {
            sql += " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') \n";
            sql += " and to_date('" + tM + "/" + tY + "','mm/yyyy') \n";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";
                sql += " and to_char(cm.p_date,'Q') in ('1','2')  \n";

            }
            if (hq.equals("2")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";
                sql += " and to_char(cm.p_date,'Q') in ('3','4') \n";
            }

        }
        sql += " group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id  \n";
        sql += " order by 1 ";
       
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                           ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);

        } catch (SQLException e) {
           
            e.printStackTrace();
         } 

        return rs;
    }
    
    
    

    public List<Map<String, Object>> getSupplyStatusRepBySupplier(String fac, String mon, String year, String prod,
                                                                  String dat, String fY, String tY, String fM,
                                                                  String tM, String type, String hq, String cens[], String ty) {
                                                                      
        String sql =
            " select  f.fac_name as facname, " + 
            "ft.fac_name as factype,  " +
            "nvl(sum(ci.receipts),0) as receipts, \n";
        sql += " nvl(sum(ci.issues),0) as issues, " +
               "nvl(sum(ci.closing_bal),0), \n";
        sql += " decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), \n";
      
        sql += " nvl(sum(AVG_MNTHLY_CONS),0), ";
        sql += " f.facility_id as facid, ft.fac_type_id \n";
        sql += " from ctf_item ci, ctf_main cm, facility f, fac_type ft \n";
        sql += " where ci.ctf_main_id = cm.ctf_main_id(+) \n";
        sql += " and cm.facility_id = f.facility_id(+) \n";
        sql += " and f.fac_type_id= ft.fac_type_id(+) \n";
        sql += " and ci.prod_id=" + prod + " \n";
        if (type.equals("supp")) {
            if(!ty.equals("0") ){
            String centersCollection = "";
            
            for (int index = 0; index <cens.length; index++){
                if(index == 0)
                    centersCollection = cens[index];
                else
                centersCollection += " , "+cens[index] ;
                //index++;
                }
                if ( getTypeLvl(cens[0]).equals("2")) {
                    sql += " and f.sup_code  in ("+centersCollection+" )";
                }else{
                    sql += " and f.facility_id in (select ff.facility_id from facility ff ";
                    sql += " where sup_code=" + fac + " and ff.facility_id in ("+centersCollection+")) \n";
                }
                    
                
               
              
            
            }else{
            sql += " and f.facility_id in (select ff.facility_id from facility ff ";
            sql += " where sup_code=" + fac + ") \n";
            }
        } else if (type.equals("indv")) {
            sql += " and f.facility_id = " + fac;
        }
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {

                sql += " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "' \n";

            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";
                sql += " and to_char(cm.p_date,'Q')='" + mon + "' \n";
            }
            if (mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";

            }
        }
        if (dat.equals("u")) {
            sql += " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') \n";
            sql += " and to_date('" + tM + "/" + tY + "','mm/yyyy') \n";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";
                sql += " and to_char(cm.p_date,'Q') in ('1','2')  \n";

            }
            if (hq.equals("2")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";
                sql += " and to_char(cm.p_date,'Q') in ('3','4') \n";
            }

        }
        sql += " group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id  \n";
        sql += " order by 1 ";
        List<Map<String, Object>> result = null;
        JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
         result = jdbcTemplate.queryForList(sql);
         return result;
        
    }


    public List<Map<String, Object>> getClosingBalAggStockMovementsRepInd(String fac, String mon, String year,
                                                                          String prod, String dat, String fY, String tY,
                                                                          String fM, String tM, String type,
                                                                          String hq) {
        String avg = " AVGMONVAL('"+fac+"', '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"') ";
        
        String sql =
            " select  distinct sum(ci.closing_bal) cbal, " +avg+" as avg, " +
            //" decode ( "+avg+", 0 , 0 , sum(ci.closing_bal)/ "+avg+" ) as MOS , " +
            "cm.p_date  \n" +
            //"-- ,ROW_NUMBER() OVER (ORDER BY last_updated ASC) row_id \n" +
            "from ctf_item ci, ctf_main cm , facility ff, fac_type fft \n" +
            "where ci.ctf_main_id = cm.ctf_main_id \n" + "and ff.facility_id = cm.facility_id \n" +
            "and fft.fac_type_id = ff.fac_type_id \n" + "and ff.facility_id = " + fac + " \n " + "and ci.prod_id = " +
            prod + " \n" + "-- and ff.active = 1 \n";


        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql += " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon + "'";
            }

            if (mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'";
            }


        }
        if (dat.equals("u")) {
            sql +=
                " and cm.p_date " + "between to_date('" + fM + "/" + fY + "','mm/yyyy') " + "and to_date('" + tM + "/" +
                tY + "','mm/yyyy') ";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' " + " and to_char(cm.p_date,'Q') in ('1','2')";
            } else {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' " + "and to_char(cm.p_date,'Q') in ('3','4')";
            }

        }


        sql += " group by cm.p_date \n" + "order by cm.p_date desc";
System.out.println(sql);

        List<Map<String, Object>> result = null;
        JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
         result = jdbcTemplate.queryForList(sql);
         return result;

    }
    
    
    
    
    // avg lvl 2 calculation
    
    


    public List<Map<String, Object>> getOpeningBalAggStockMovementsRepInd(String fac, String mon, String year,
                                                                          String prod, String dat, String fY, String tY,
                                                                          String fM, String tM, String type,
                                                                          String hq) {
        String sql =
            " select  distinct sum(ci.Open_bal) obal, " + "cm.p_date  \n" +
            //"-- ,ROW_NUMBER() OVER (ORDER BY last_updated ASC) row_id \n" +
            "from ctf_item ci, ctf_main cm , facility ff, fac_type fft \n" +
   "where ci.ctf_main_id = cm.ctf_main_id \n" + "and ff.facility_id = cm.facility_id \n" +
   "and fft.fac_type_id = ff.fac_type_id \n" + "and ff.facility_id = " + fac + " \n " + "and ci.prod_id = " + prod +
   " \n" + "-- and ff.active = 1 \n";


        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql += " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon + "'";
            }

            if (mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'";
            }


        }
        if (dat.equals("u")) {
            sql +=
                " and cm.p_date " + "between to_date('" + fM + "/" + fY + "','mm/yyyy') " + "and to_date('" + tM + "/" +
                tY + "','mm/yyyy') ";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' " + " and to_char(cm.p_date,'Q') in ('1','2')";
            } else {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' " + "and to_char(cm.p_date,'Q') in ('3','4')";
            }

        }
        sql += " group by cm.p_date \n" + "order by cm.p_date asc";
        List<Map<String, Object>> result = null;
        JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
         result = jdbcTemplate.queryForList(sql);
         return result;
    }
}
