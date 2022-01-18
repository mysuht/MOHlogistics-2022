package moh.logistics.lib.reports.code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import moh.logistics.lib.reports.JDBCConfig;

import moh.logistics.lib.reports.UtilsClass;

import org.springframework.jdbc.core.JdbcTemplate;

public class SupplyStatusReport {
 JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();

    public SupplyStatusReport() {
        super();
    }
    public static List<FacilityTemplate> supplyStatusFacilityList(String prod, String mon, String year, String dat,
                                                                     String fY, String tY, String fM, String tM,
                                                                     String hq, String facilities[], String type) {
       return UtilsClass.supplyStatusFacilityList(prod, mon, year, dat, fY, tY, fM, tM, hq, facilities, type) ;
                                                                     }
    
    public static List<FacilityTemplate> getDirectoratesList(String[] directorates) {
        return UtilsClass.getDirectoratesList(directorates);
    }

    public List<Map<String, Object>> AllFacilities(String prod, String mon, String year, String dat, String fY,
                                                   String tY, String fM, String tM, String type, String hq,
                                                   String facilities[]) {
        String query = UtilsClass.AllFacilityQuery(prod, mon, year, dat, fY, tY, fM, tM, type, hq, facilities);

        List<Map<String, Object>> results = jdbcTemplate.queryForList(query);
        return results;
    }

    public List<Map<String, Object>> getDirectorates(String type, String[] directorates) {
        
        String centersCollection = "";
        String sql =
            "select f.facility_id AS FACILITY_ID , f.fac_name AS factype from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id(+)  ";

        if(directorates != null){
        String typeLvl =  UtilsClass.getTypeLvl(directorates[0]);
        if (!type.equals("0") && typeLvl.equals("2")) {
            for (int index = 0; index < directorates.length; index++) {
                if (index == 0)
                    centersCollection = directorates[index];
                else
                    centersCollection += " , " + directorates[index];
            }

            sql += " and ( ";
            sql += " f.facility_id in (" + centersCollection + ") ";

            sql += " ) ";
            sql += " union ";
            sql += " ( select f.facility_id AS FACILITY_ID , \n";
            sql += " f.fac_name AS factype \n";
            sql += " from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id(+)  ";
            sql += " and type_hierarchy = 1 ) ";
        }
        }else {
            sql += "and type_hierarchy in (1,2)  ";
    }
        sql += " order by 2";
        System.out.println(sql);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        return results;
    }

    public static String avgMnthlyCons(String fac, String mon, String year, String prod, String dat, String fY,
                                       String tY, String fM, String tM, String type, String hq) {
        return UtilsClass.avgMnthlyCons(fac, mon, year, prod, dat, fY, tY, fM, tM, type, hq);
    }

    public static String getProdDose(String prod) {
        return UtilsClass.getProdDose(prod);
    }

    public static String getProdName(String prod) {
        return UtilsClass.getProdName(prod);
    }
    public static String getDate() {
        return UtilsClass.getDate();
    }
    public static String getTime() {
        return UtilsClass.getTime();
    }
    public static List<FacilityTemplate> getFacTypesMainName(String fac) {
        return UtilsClass.getFacTypesMainName(fac);
    }

    public static String closeBal(String fac, String mon, String year, String prod, String dat, String fY, String tY,
                                  String fM, String tM, String type, String hq) {
        return UtilsClass.closeBal(fac, mon, year, prod, dat, fY, tY, fM, tM, type, hq);
    }

    public static String openBal(String fac, String mon, String year, String prod, String dat, String fY, String tY,
                                 String fM, String tM, String type, String hq) {
        return UtilsClass.openBal(fac, mon, year, prod, dat, fY, tY, fM, tM, type, hq);
    }

    public static String mos(String fac, String mon, String year, String prod, String dat, String fY, String tY,
                             String fM, String tM, String type, String hq) {
        return UtilsClass.mos(fac, mon, year, prod, dat, fY, tY, fM, tM, type, hq);
    }

    public List<Map<String, Object>> getSupplyStatusRepBySupplierRS(String fac, String mon, String year, String prod,
                                                                    String dat, String fY, String tY, String fM,
                                                                    String tM, String type, String hq, String cens[],
                                                                    String ty) {


        String sql =
            "select  f.fac_name as facname, " + "ft.fac_name as factype,  " + "nvl(sum(ci.receipts),0) as receipts, \n";
        sql +=
            " nvl(sum(ci.issues),0) as issues, f.facility_id as FACILITY_ID,  "; //       "nvl("+closingBal+",0) as closingBal, \n";
        sql += " sum(ci.adjustments) as adj,  ";
        sql += " f.facility_id as facid, ft.fac_type_id \n";
        sql += " from ctf_item ci, ctf_main cm, facility f, fac_type ft \n";

        sql += " where ci.ctf_main_id = cm.ctf_main_id(+) \n";
        sql += " and cm.facility_id = f.facility_id(+) \n";
        sql += " and f.fac_type_id= ft.fac_type_id(+) \n";
        sql += " and ci.prod_id=" + prod + " \n";
        if (type.equals("supp")) {
            if (!ty.equals("0")) {
                String centersCollection = "";

                for (int index = 0; index < cens.length; index++) {
                    if (index == 0)
                        centersCollection = cens[index];
                    else
                        centersCollection += " , " + cens[index];

                }

                
                String typeLvl = UtilsClass.getTypeLvl(cens[0]);    
                if (typeLvl.equals("2")) {
                    sql += " and f.sup_code  in (" + centersCollection + " )";
                } else {
                    sql += " and f.facility_id in (select ff.facility_id from facility ff ";
                    sql += " where sup_code=" + fac + " and ff.facility_id in (" + centersCollection + ")) \n";
                }
            } else {
                sql += " and f.facility_id in (select ff.facility_id from facility ff ";
                sql += " where sup_code=" + fac + ") \n";
            }
        } else if (type.equals("indv")) {
            sql += " and f.facility_id = " + fac;
        }
        String innerClause = UtilsClass.innerClause(mon, year, dat, fY, tY, fM, tM, hq);
        sql += innerClause;
        sql += " group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id  \n";
        sql += " order by 1 ";
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println(sql);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        return results;

    }


    public int getSupplyStatusRepBySupplierReceipts(String fac, String mon, String year, String prod, String dat,
                                                    String fY, String tY, String fM, String tM, String type, String hq,
                                                    String cens[], String ty) {
        int receipts = 0;


        String sql = "select ";
        //sql += " f.fac_name as facname, " ;
        //sql += "ft.fac_name as factype,  " ;
        sql += " nvl(sum(ci.receipts),0) as receipts \n";
        //sql += " , nvl(sum(ci.issues),0) as issues " ;

        // sql += "  ,  sum(ci.adjustments) as adj,  ";
        //sql += " f.facility_id as facid, ft.fac_type_id \n";
        sql += " from ctf_item ci, ctf_main cm, facility f, fac_type ft \n";

        sql += " where ci.ctf_main_id = cm.ctf_main_id(+) \n";
        sql += " and cm.facility_id = f.facility_id(+) \n";
        sql += " and f.fac_type_id= ft.fac_type_id(+) \n";
        sql += " and ci.prod_id=" + prod + " \n";
        sql += " and f.sup_code = " +fac;
        sql += UtilsClass.innerClause(mon, year, dat, fY, tY, fM, tM, hq);
        //sql += " group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id  \n";
        sql += " order by 1 ";
        System.out.println(sql);
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            receipts = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                if(pst != null){
                    pst.close();
                }
                if(rs!= null){
                    rs.close();
                }
                if(conn!= null){
                    conn.close();
                }
           } catch (Exception e) {
                // TODO: Add catch code
                e.printStackTrace();
            }
        }
        //receipts = jdbcTemplate.queryForInt(sql);
        return receipts;
    }

}



//List<Movie> movies = Arrays.asList(
//
//        new Movie("Lord of the rings", 8.8),
//
//        new Movie("Back to the future", 8.5),
//
//        new Movie("Carlito's way", 7.9),
//
//        new Movie("Pulp fiction", 8.9));
//
//movies.sort(Comparator.comparingDouble(Movie::getRating)
//
//                      .reversed());
//
//movies.forEach(System.out::println);
