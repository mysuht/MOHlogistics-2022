package moh.logistics.lib.reports.code;


import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.naming.NamingException;
import javax.sql.DataSource;

import moh.logistics.lib.common.DBUtil;
import moh.logistics.lib.reports.JDBCConfig;
import moh.logistics.lib.reports.MainInterface;
import moh.logistics.lib.reports.ReportConfig;
import moh.logistics.lib.reports.UtilsClass;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class LogisticsReportsClass implements MainInterface { 
    
    JdbcTemplate jdbcTemplate = JDBCConfig.DataSourceJdbcTempateInfo();
    
    public LogisticsReportsClass() {
    }
    String jdbcUrl = MainInterface.thinConn;
    String userid = MainInterface.username;
    String password = MainInterface.password; 
    Connection conn;
    Statement stmt = null;
    public void getDBConnection() throws SQLException{
        //DataSource ds = JDBCConfig.dataSource();
        conn= UtilsClass.jdbcConnection();  //ds.getConnection(userid,password);
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // DataSource mysqlDataSource = (DataSource) ac.getBean("mysqlDataSource");
    public  Connection getConnection() throws SQLException {
        conn = JDBCConfig.dataSource().getConnection();
        return conn;
    }
    /** Uses JNDI and Datasource (preferred style).   */
    public   Connection   getJNDIConnection() throws SQLException {
         conn = JDBCConfig.dataSource().getConnection();
         return conn;
    }
    
    
    
    public static String QueryProcessor(String sql, String mon, String year, String dat, String fY, String tY,
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
           
            sql += "  and cm.p_date  ";
            sql +=" between to_date('" + fM + "/" + fY + "','mm/yyyy') ";
            sql += " and to_date('" + tM + "/" + tY + "','mm/yyyy') ";
            System.out.println("hello "+dat + " "+sql);
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

   

    ResultSet rs;



    public ResultSet getDirectorates() {

        String sql =
            "select f.facility_id , f.fac_name from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id(+) and type_hierarchy in (1,2) order by 2";
        try {
            getDBConnection(); 
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }


    public ResultSet getPrintedRepItems(String mainId) {

        String sql =
            "SELECT CtfItemEO.PROD_ID,    \n" + //1
            "      nvl( CtfItemEO.OPEN_BAL,0),    \n" + //2
            "      nvl( CtfItemEO.RECEIPTS, 0),   \n" + //3
            "      nvl( CtfItemEO.ISSUES, 0),   \n" + //4
            "     nvl(  ABS(CtfItemEO.ADJUSTMENTS),  0),   \n" + //5
            "       CtfItemEO.ADJ_TYPE_ID,    \n" + //6
            "     nvl(  CtfItemEO.CLOSING_BAL,   0), \n" + //7
            "       CtfItemEO.ERR_OB,    \n" + //8
            "       CtfItemEO.ERR_CB,    \n" + //9
            "       CtfItemEO.DE_STAT,    \n" + //10
            "       CtfItemEO.NEW_VISITS,    \n" + //11
            "       CtfItemEO.CONT_VISITS,    \n" + //12
            "       CtfItemEO.ERR_QTY_RQRD,    \n" + //13
            "       CtfItemEO.ERR_QTY_RCVD,    \n" + //14
            "       CtfItemEO.ERR_AMC,    \n" + //15
            "       CtfItemEO.AVG_MNTHLY_CONS,    \n" + //16
            "       CtfItemEO.QTY_REQUIRED,    \n" + //17
            "       CtfItemEO.QTY_RECEIVED,    \n" + //18
            "       nvl(AdjTypeEO.TYPE_NAME,' '),     \n" + //19
            "       AdjTypeEO.ADJ_TYPE_ID AS ADJ_TYPE_ID1,    \n" + //20
            "       ProductEO.PRO_NAME,    \n" + //21
            "       ProductEO.PROD_ID AS PROD_ID1,    \n" + //22
            "       ProductEO.PRO_DOSE ,\n" + //23
            "CtfItemEO.CTF_ITEM_ID,    \n" + //24
            "       CtfItemEO.CTF_MAIN_ID,    \n" + //25
            "       CtfItemEO.P_DATE  \n" + //26
            "FROM  CTF_ITEM CtfItemEO, ADJ_TYPE AdjTypeEO, PRODUCT ProductEO   \n" +
   "WHERE (CtfItemEO.ADJ_TYPE_ID = AdjTypeEO.ADJ_TYPE_ID(+)) AND (CtfItemEO.PROD_ID = ProductEO.PROD_ID(+))\n" +
   "and CtfItemEO.ctf_main_id=" + mainId + " order by ProductEO.PROD_ID ";
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }


    public ResultSet getPrintedRepMain(String mainId) {

        String sql =
            "SELECT CtfMainEO.CTF_MAIN_ID, \n" + //1
            "       CtfMainEO.FACILITY_ID, \n" + //2
            "       to_char(CtfMainEO.P_DATE,'month, yyyy'), \n" + // 3 month/Year
            "       to_char(CtfMainEO.DE_DATE,'dd-mon-yy'), \n" + //4
            "       CtfMainEO.DE_STAFF, \n" + //5 Entered By
            "       to_char(CtfMainEO.LC_DATE,'dd-mon-yy')  , \n" + //6
            "       CtfMainEO.LC_STAFF, \n" + //7 last changed By
            "       CtfMainEO.CTF_STATUS, \n" + //8
            "       nvl(CtfMainEO.CTF_COMMENTS,' '),  \n" + //9
            "       FacilityEO.FAC_NAME, \n" + //10
            "       FacilityEO.FACILITY_ID AS FACILITY_ID1, \n" + //11
            "       FacilityEO.FAC_CODE, \n" + //12 Facility Code
            "       FacilityEO.FAC_TYPE_ID, \n" + //13
            "       FacilityEO.SUP_CODE, \n" + //14
            "       FacilityEO.FAC_NAME AS FAC_NAME1, \n" + // 15 Facility Name
            "       FacilityEO.FACILITY_ID AS FACILITY_ID2,   \n" + //16
            "       ft.fac_name " + //17 Facility Type
            "FROM  CTF_MAIN CtfMainEO, FACILITY FacilityEO, fac_type ft    \n" + "where CtfMainEO.CTF_MAIN_ID = " +
   mainId + " \n " +
   " and ft.fac_type_id(+)  = FacilityEO.fac_type_id and CtfMainEO.facility_id = FacilityEO.facility_id   ";
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            //  System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }

    public ResultSet getAllFac() {

        String sql =
            "select f.facility_id , f.fac_name from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id(+) and type_hierarchy in (1,2,3) order by 2";
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }


    public ResultSet getFacesSups(String code) {

        String sql =
            "select f.facility_id , f.fac_name from facility f where f.sup_code=" + code +
            " order by sup_code,fac_name";
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }


    public String getFacHier(String fac) {

        String sql =
            "select ft.type_hierarchy from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id=" +
            fac;
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return null;
        }


    }


    public String getfacTypeHier(String fac) {

        String sql =
            "select ft.type_hierarchy from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id=" +
            fac;
        try {
            getDBConnection(); 
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("Hierarchy is ");
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return null;
        }


    }

    public ResultSet getDirectorates2() {

        String sql =
            "select f.facility_id , f.fac_name from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id(+) and type_hierarchy in (2) order by 2";
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }

    // select gof_id, gof_facility_id as type, gof_grp_id as group_type from group_of_facilities where gof_grp_id=
    public ResultSet getTypesOfGroup(int type) {

        String sql =
            "   select gof_id, gof_facility_id as type, gof_grp_id as group_type from group_of_facilities where gof_grp_id=" +
            type;
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }


    public String getTypeName(String type) {

        String sql = "select fac_name from fac_type where fac_type_id=" + type;
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return null;
        }


    }

    public String getTypeNameD(String type) {

        String sql =
            "select ft.fac_name from fac_type ft, facility f  where f.fac_type_id=ft.fac_type_id and  facility_id=" +
            type;
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return null;
        }


    }


    public ResultSet getDirectoratesD() {

        String sql =
            "select f.facility_id , f.fac_name from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id(+) and type_hierarchy in (2) order by 2";
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }

    public List<Report> getReportsNames() {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "select rep_id, rep_name from reports";
        List<Report> list = new ArrayList();
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while(rs.next()){
                Report report = new Report();
                report.setReportID(rs.getInt(1));
                report.setReportName(rs.getString(2));
                list.add(report);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                UtilsClass.closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;

    }

    public ResultSet getFacTypesMain() {

        String sql = "select fac_type_id, fac_name, type_hierarchy from fac_type order by 2";
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }

    public List<FacilityTemplate> getFacTypesMain1() {
        List<FacilityTemplate> result = new ArrayList();
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql =
            "(select fac_type_id as facTypeId, fac_name as facilityName from fac_type ) union (select grp_id+100 , grp_desc from groups ) order by 2";
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while(rs.next()){
            FacilityTemplate template = new FacilityTemplate();
                template.setFacilityId(rs.getLong("facTypeId"));
                template.setFacilityName(rs.getString("facilityName"));
                result.add(template);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                UtilsClass.closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    public ResultSet getFacTypesMainName(String fac) {
        String sql = "select fac_type_id, ";
            sql += " fac_name, type_hierarchy ";
            sql += " from fac_type where fac_type_id=" + fac;
            if ( Integer.parseInt(fac) > 100 ){
        sql =
            "select grp_id, grp_desc from\n" + 
            " fac_type ft, facility f, group_of_facilities gof, groups g \n" + 
            " \n" + 
            " where ft.fac_type_id = f.fac_type_id and ft.fac_type_id = gof_facility_id and \n" + 
            " \n" + 
            " gof.gof_grp_id = g.grp_id and gof.gof_grp_id= " +
            
            (Integer.parseInt(fac) - 100) + " order by 2";
            }
            System.out.println(sql);
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
            ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql); 
            if(!rs.next()){
                String sqlGroup = "select ft.fac_type_id, ";
                    sqlGroup += " ft.fac_name, ft.type_hierarchy ";
                  sqlGroup+= " from fac_type ft " ;
                  sqlGroup+= " , facility f ";
                  sqlGroup+= " where ";
                  //sqlGroup += "  ft.fac_type_id = f.fac_type_id ";
                  //sql+Group += " and ";
                  sqlGroup += "  ft.fac_type_id in ";
                  sqlGroup += " (select gof_facility_id from group_of_facilities ";
                  sqlGroup+= " where gof_grp_id="+(Integer.parseInt(fac)-100)+" ) " ;
                 // sqlGroup += " order by 2 ";
                  System.out.println(sqlGroup);
                getDBConnection();
                stmt = conn.prepareStatement(sqlGroup);
                rs = stmt.executeQuery(sql);
            }else{
                getDBConnection();
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = stmt.executeQuery(sql);
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }
    
    public List<Map<String, Object>> getFacTypesMainNameList(String fac) {

        String sql = "select fac_type_id, ";
            sql += " fac_name, type_hierarchy ";
            sql += " from fac_type where fac_type_id=" + fac;
        System.out.println(sql);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
       
        return results;

    }


    public ResultSet getDireDispensed(String sup) {
        String sql = "select facility_id , fac_name,fac_code from facility where sup_code=" + sup + " order by 2";
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }

    public List<FacilityTemplate> getFacilityByTypeId(String sup) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        List<FacilityTemplate> result = new ArrayList();
        if (sup.equals("") || sup.equals(null) || sup.equals(" ")) {
            sup = "0";
        }
        String sqlCount = "select count(*) from facility where fac_type_id in (" + sup + ") ";
        String sql =
            "select facility_id , nvl(fac_name,' '), fac_code from facility where fac_type_id in (" + sup +
            ") order by 2 ";
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery(sqlCount);
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println(" More Than 0");
                sql =
                    "select facility_id , nvl(fac_name,' '), fac_code " +
                    "from facility where fac_type_id in (" + sup +
                    ") order by 2 ";
               
                conn = UtilsClass.jdbcConnection();
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery(sql);
                while(rs.next()){
                    FacilityTemplate template = new FacilityTemplate();
                    template.setFacilityId(rs.getLong(1));
                    template.setFacilityName(rs.getString(2));
                    template.setFacCode(rs.getString(3));
                    result.add(template);
                }
                } else {
                System.out.println("*********** group Types &&&&&&&&&&&");
                //sql = "select facility_id , nvl(fac_name,' '), fac_code from facility where fac_type_id in ("+sup+") order by 2 ";
                sql =
                    "select facility_id , nvl(fac_name,' '), fac_code from facility where fac_type_id in (select gof_facility_id from group_of_facilities where gof_grp_id=" +
                    (Integer.parseInt(sup) - 100) + ") order by 2";
                conn = UtilsClass.jdbcConnection();
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery(sql);
                while(rs.next()){
                    FacilityTemplate template = new FacilityTemplate();
                    template.setFacilityId(rs.getLong(1));
                    template.setFacilityName(rs.getString(2));
                    template.setFacCode(rs.getString(3));
                    result.add(template);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sql);
        return result;

    }
    // select sum(new_visits) from ctf_item where prod_id=1
    public Double getNewVisitsByDateProduct(String prod, String quart, String year, String dat, String fY, String tY,
                                            String fM, String tM, String type, String hq) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = " SELECT nvl(sum(new_visits),0) ";
        sql += " from ctf_item ci, ctf_main cm ";
        sql += " where cm.ctf_main_id=ci.ctf_main_id ";
        sql += " and prod_id=" +prod;
        sql += UtilsClass.innerClause(quart, year, dat, fY, tY, fM, tM, hq);
        try {
            System.out.println(sql);
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e){
            e.printStackTrace();
            return 0.010;
        }
    }


    public Double getNewVisitsByDateProductGT(String prod, String quart, String year, String dat, String fY, String tY,
                                              String fM, String tM, String type, String hq, String[] faces) {
        String fac = "";
        for (int i = 0; i < faces.length; i++) {
            if (i == 0) {
                fac = faces[i];
            } else {
                fac += ", " + faces[i];
            }
        }
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("q") && !quart.equals("0")) {
                sql =
                    "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and prod_id=" +
                    prod + " and to_char(cm.p_date,'Q')=" + quart + " and cm.facility_id in (" + fac +
                    ") and to_char(cm.p_date,'yyyy')=" + year;
            }
            if (dat.equals("m") && !quart.equals("0")) {
                sql =
                    "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and prod_id=" +
                    prod + " and to_char(cm.p_date,'mm')=" + quart + " and cm.facility_id in (" + fac +
                    ") and to_char(cm.p_date,'yyyy')=" + year;
            }
            if (quart.equals("0")) {
                sql =
                    "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and prod_id=" +
                    prod + " and cm.facility_id in (" + fac + ")  and to_char(cm.p_date,'yyyy')=" + year;
            }
        }


        if (dat.equals("u")) {
            sql =
                "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and prod_id=" +
                prod + " and cm.facility_id in (" + fac + ")  and cm.p_date between to_date('" + fM + "/" + fY +
                "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') ";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and prod_id=" +
                    prod + " and cm.facility_id in (" + fac +
                    ") and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')=" + year;
            }
            if (hq.equals("2")) {
                sql =
                    "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and prod_id=" +
                    prod + " and cm.facility_id in (" + fac +
                    ") and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')=" + year;
            }
        }

        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }
        //return rs;

    }


    public Double getNewVisitsByDateProductGTd(String prod, String quart, String year, String dat, String fY, String tY,
                                               String fM, String tM, String type, String hq, String[] faces) {
        String sql = "";
        String fac = "";

        for (int i = 0; i < faces.length; i++) {
            if (i == 0) {
                fac = faces[i];
            } else {
                fac += ", " + faces[i];
            }
        }
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("q") && !quart.equals("0")) {
                sql =
                    "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and prod_id=" +
                    prod + " and to_char(cm.p_date,'Q')=" + quart + " and cm.facility_id in (" + fac +
                    ") and to_char(cm.p_date,'yyyy')=" + year;
            }
            if (dat.equals("m") && !quart.equals("0")) {
                sql =
                    "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and prod_id=" +
                    prod + " and to_char(cm.p_date,'mm')=" + quart + " and cm.facility_id in (" + fac +
                    ") and to_char(cm.p_date,'yyyy')=" + year;
            }
            if (quart.equals("0")) {
                sql =
                    "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and prod_id=" +
                    prod + " and cm.facility_id in (" + fac + ")  and to_char(cm.p_date,'yyyy')=" + year;
            }
        }


        if (dat.equals("u")) {
            sql =
                "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and prod_id=" +
                prod + " and cm.facility_id in (" + fac + ")  and cm.p_date between to_date('" + fM + "/" + fY +
                "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') ";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and prod_id=" +
                    prod + " and cm.facility_id in (" + fac +
                    ") and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')=" + year;
            }
            if (hq.equals("2")) {
                sql =
                    "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and prod_id=" +
                    prod + " and cm.facility_id in (" + fac +
                    ") and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')=" + year;
            }
        }

        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }
        //return rs;

    }


    public Double getNewVisitsByDateProductD(String prod, String quart, String year, String dat, String dir, String fY,
                                             String tY, String fM, String tM, String type, String hq) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String      sql = "SELECT nvl(sum(new_visits),0) ";
        sql += " from ctf_item ci, ctf_main cm, facility f ";
        sql += " where ci.ctf_main_id=cm.ctf_main_id(+) ";
        sql += " and f.facility_id = cm.facility_id ";
        sql += " and prod_id = " +prod;
        sql += " and f.sup_code = " + dir;
        sql += UtilsClass.innerClause(quart, year, dat, fY, tY, fM, tM, hq);
        try {
            System.out.println(sql);
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.010;
        }
    }


    public Double getNewVisitsByDateProductDGT(String prod, String quart, String year, String dat, String dir,
                                               String fY, String tY, String fM, String tM, String type, String hq,
                                               String[] faces) {
        String fac = "";
        for (int i = 0; i < faces.length; i++) {
            if (i == 0) {
                fac = faces[i];
            } else {
                fac += ", " + faces[i];
            }
        }
        String sql = "";

        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("q") && !quart.equals("0")) {
                sql =
                    "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id=cm.ctf_main_id(+) and prod_id=" +
                    prod + " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + ") and facility_id in (" + fac + ") and to_char(cm.p_date,'Q')=" + quart +
                    " and to_char(cm.p_date,'yyyy')=" + year;
            }
            if (dat.equals("m") && !quart.equals("0")) {
                sql =
                    "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id=cm.ctf_main_id(+) and prod_id=" +
                    prod + " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + ") and facility_id in (" + fac + ") and to_char(cm.p_date,'mm')=" + quart +
                    " and to_char(cm.p_date,'yyyy')=" + year;
            }
            if (quart.equals("0")) {
                sql =
                    "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id=cm.ctf_main_id(+) and prod_id=" +
                    prod + " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + ") and facility_id in (" + fac + ") and to_char(cm.p_date,'yyyy')=" + year;
            }


        }
        if (dat.equals("u")) {
            sql =
                "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id=cm.ctf_main_id(+) and prod_id=" +
                prod + " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                ") and facility_id in (" + fac + ")  and cm.p_date between to_date('" + fM + "/" + fY +
                "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')    ";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id=cm.ctf_main_id(+) and prod_id=" +
                    prod + " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + ") and facility_id in (" + fac +
                    ") and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')=" + year;
            }
            if (hq.equals("2")) {
                sql =
                    "select nvl(sum(new_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id=cm.ctf_main_id(+) and prod_id=" +
                    prod + " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + ") and facility_id in (" + fac +
                    ") and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')=" + year;
            }
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }
        //return rs;
        //return rs;

    }


    public Double getContVisitsByDateProductD(String prod, String quart, String year, String dat, String dir, String fY,
                                              String tY, String fM, String tM, String type, String hq) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String      sql = "SELECT nvl(sum(cont_visits),0) ";
        sql += " from ctf_item ci, ctf_main cm, facility f ";
        sql += " where ci.ctf_main_id=cm.ctf_main_id(+) ";
        sql += " and f.facility_id = cm.facility_id ";
        sql += " and prod_id = " +prod;
        sql += " and f.sup_code = " + dir;
        sql += UtilsClass.innerClause(quart, year, dat, fY, tY, fM, tM, hq);
        try {
            System.out.println(sql);
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.010;
        }
    }


    public Double getContVisitsByDateProductDGT(String prod, String quart, String year, String dat, String dir,
                                                String fY, String tY, String fM, String tM, String type, String hq,
                                                String[] faces) {
        String fac = "";
        for (int i = 0; i < faces.length; i++) {
            if (i == 0) {
                fac = faces[i];
            } else {
                fac += ", " + faces[i];
            }
        }
        String sql = "";

        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("q") && !quart.equals("0")) {
                sql =
                    "select nvl(sum(cont_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id=cm.ctf_main_id(+) and prod_id=" +
                    prod + "  and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + ") and cm.facility_id in (" + fac + ") and to_char(cm.p_date,'Q')=" + quart +
                    " and to_char(cm.p_date,'yyyy')=" + year;
            }
            if (dat.equals("m") && !quart.equals("0")) {
                sql =
                    "select nvl(sum(cont_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id=cm.ctf_main_id(+) and prod_id=" +
                    prod + " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + ") and cm.facility_id in (" + fac + ") and to_char(cm.p_date,'mm')=" + quart +
                    " and to_char(cm.p_date,'yyyy')=" + year;
            }
            if (quart.equals("0")) {
                sql =
                    "select nvl(sum(cont_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id=cm.ctf_main_id(+) and prod_id=" +
                    prod + " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + ") and cm.facility_id in (" + fac + ")  and to_char(cm.p_date,'yyyy')=" + year;
            }


        }
        if (dat.equals("u")) {
            sql =
                "select nvl(sum(cont_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id=cm.ctf_main_id(+) and prod_id=" +
                prod + " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                ") and cm.facility_id in (" + fac + ")  and cm.p_date between to_date('" + fM + "/" + fY +
                "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select nvl(sum(cont_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id=cm.ctf_main_id(+) and prod_id=" +
                    prod + " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + ")  and cm.facility_id in (" + fac +
                    ") and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')=" + year;
            }
            if (hq.equals("2")) {
                sql =
                    "select nvl(sum(cont_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id=cm.ctf_main_id(+) and prod_id=" +
                    prod + " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + ") and cm.facility_id in (" + fac +
                    ") and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')=" + year;
            }
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }
        //return rs;

    }


    public Double getContVisitsByDateProduct(String prod, String quart, String year, String dat, String fY, String tY,
                                             String fM, String tM, String type, String hq) {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "SELECT nvl(sum(cont_visits),0) ";
        sql += " from ctf_item ci, ctf_main cm ";
        sql += " where ci.ctf_main_id = cm.ctf_main_id ";
        sql += " and prod_id=" + prod;
        sql += UtilsClass.innerClause(quart, year, dat, fY, tY, fM, tM, hq);
        try {
            System.out.println(sql);
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e){
            e.printStackTrace();
            return 0.010;
        }
        //return rs;

    }


    public Double getContVisitsByDateProductGT(String prod, String quart, String year, String dat, String fY, String tY,
                                               String fM, String tM, String type, String hq, String[] faces) {
        String sql = "";
        String fac = "";

        for (int i = 0; i < faces.length; i++) {
            if (i == 0) {
                fac = faces[i];
            } else {
                fac += ", " + faces[i];
            }
        }
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("q") && !quart.equals("0")) {
                sql =
                    "select nvl(sum(cont_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and prod_id=" +
                    prod + " and cm.facility_id in (" + fac + ") and to_char(cm.p_date,'Q')=" + quart +
                    " and to_char(cm.p_date,'yyyy')=" + year;
            }
            if (dat.equals("m") && !quart.equals("0")) {
                sql =
                    "select nvl(sum(cont_visits),0) from ctf_item ci , ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and prod_id=" +
                    prod + " and cm.facility_id in (" + fac + ") and to_char(cm.p_date,'mm')=" + quart +
                    " and to_char(cm.p_date,'yyyy')=" + year;
            }
            if (quart.equals("0")) {
                sql =
                    "select nvl(sum(cont_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and prod_id=" +
                    prod + " and cm.facility_id in (" + fac + ")  and to_char(cm.p_date,'yyyy')=" + year;
            }
        }


        if (dat.equals("u")) {
            sql =
                "select nvl(sum(cont_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and prod_id=" +
                prod + " and cm.facility_id in (" + fac + ")  and cm.p_date between to_date('" + fM + "/" + fY +
                "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') ";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select nvl(sum(cont_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and prod_id=" +
                    prod + " and cm.facility_id in (" + fac +
                    ") and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')=" + year;
            }
            if (hq.equals("2")) {
                sql =
                    "select nvl(sum(cont_visits),0) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and prod_id=" +
                    prod + " and cm.facility_id in (" + fac +
                    ") and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')=" + year;
            }
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }
        //return rs;

    }

    //   select nvl(sum(new_visits),0), facility_id from ctf_item,ctf_main where ctf_item.ctf_main_id=ctf_main.ctf_main_id and  prod_id=4 and to_char(ctf_item.p_date,'Q')=1 and to_char(ctf_item.p_date,'yyyy')='2012'

    public Double getContVisitsByDateProductFac(String prod, String quart, String year, String fac, String dat,
                                                String fY, String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("q") && !quart.equals("0")) {
                sql =
                    " select nvl(sum(cont_visits),0) from ctf_item,ctf_main where ctf_item.ctf_main_id=ctf_main.ctf_main_id(+) and  prod_id=" +
                    prod + " and to_char(ctf_main.p_date,'Q')='" + quart + "' and to_char(ctf_main.p_date,'yyyy')='" +
                    year + "' and ctf_main.facility_id=" + fac;
            }
            if (dat.equals("m") && !quart.equals("0")) {
                sql =
                    " select nvl(sum(cont_visits),0) from ctf_item,ctf_main where ctf_item.ctf_main_id=ctf_main.ctf_main_id and  prod_id=" +
                    prod + " and to_char(ctf_main.p_date,'mm')='" + quart + "' and to_char(ctf_main.p_date,'yyyy')='" +
                    year + "' and ctf_main.facility_id=" + fac;
            }
            if (quart.equals("0")) {
                sql =
                    " select nvl(sum(cont_visits),0) from ctf_item,ctf_main where ctf_item.ctf_main_id=ctf_main.ctf_main_id and  prod_id=" +
                    prod + " and  to_char(ctf_main.p_date,'yyyy')='" + year + "' and ctf_main.facility_id=" + fac;
            }


        }
        if (dat.equals("u")) {
            sql =
                " select nvl(sum(cont_visits),0) from ctf_item ci,ctf_main cm where ci.ctf_main_id=cm.ctf_main_id and  ci.prod_id=" +
                prod + "  and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') and cm.facility_id=" + fac;


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select nvl(sum(cont_visits),0) from ctf_item,ctf_main where ctf_item.ctf_main_id=ctf_main.ctf_main_id(+) and  prod_id=" +
                    prod + " and to_char(ctf_main.p_date,'Q') in ('1','2') and to_char(ctf_main.p_date,'yyyy')='" +
                    year + "' and ctf_main.facility_id=" + fac;
            }
            if (hq.equals("2")) {
                sql =
                    " select nvl(sum(cont_visits),0) from ctf_item,ctf_main where ctf_item.ctf_main_id=ctf_main.ctf_main_id(+) and  prod_id=" +
                    prod + " and to_char(ctf_main.p_date,'Q') in ('3','4') and to_char(ctf_main.p_date,'yyyy')='" +
                    year + "' and ctf_main.facility_id=" + fac;
            }
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }
        //return rs;

    }

    public double getNewVisitsByDateProductFac(String prod, String quart, String year, String fac, String dat,
                                               String fY, String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        sql = " select nvl(sum(new_visits),0) ";
        sql += " from ctf_item ci,ctf_main cm ";
        sql += " where ci.ctf_main_id=cm.ctf_main_id(+) ";
        sql += " and  prod_id = " + prod;
        sql += " and cm.facility_id=" + fac;
        sql += UtilsClass.innerClause(quart, year, dat, fY, tY, fM, tM, hq);
        return Double.parseDouble((String)UtilsClass.getOneValue(sql));
    }
    
    public  int FacilityLVL(String facility){
        String sql = "select type_hierarchy " +
            "from fac_type ft, facility f " +
            "where f.fac_type_id=ft.fac_type_id " + 
            "and f.facility_id = "+facility;
        return jdbcTemplate.queryForInt(sql);
    }
    
    public String getDirNameX(String dir) {
        String sql = null;

         sql = "select fac_name from facility where facility_id=" + dir;

        return (String)UtilsClass.getOneValue(sql);
    }
    

    public String getDirName(String dir) {
        String sql = null;
        if ( FacilityLVL(dir) == 3 ){
         sql = "select fac_name from facility where facility_id=" + dir;
        }else{
            sql = "select t.fac_name from facility t where " +
                " t.facility_id in ( select sup_code from facility where facility_id =  "+dir +")";
        }
        return (String)UtilsClass.getOneValue(sql);
    }
    
    public static String getSupplierName(String facilityId){
        return UtilsClass.getSupplierName(facilityId);
    }
    
    public static String getSupplierCode(String facilityId){
        return UtilsClass.getSupplierCode(facilityId);
    }
    
    public static String getFacilityName(String facilityId){
        return UtilsClass.getFacilityName(facilityId);
    }
    
    public static String getFacilityCode(String facilityId){
        return UtilsClass.getFacilityCode(facilityId);
    }

    public ResultSet getDirNameDispensed(String fac, String year, String quart, String prod, String dat, String fY,
                                         String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("q") && !quart.equals("0")) {
                sql =
                    "select f.facility_id, f.fac_name,sum(ci.new_visits), nvl(sum(ci.cont_visits),0) from facility f, ctf_item ci,ctf_main cm  where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select fac.facility_id  from facility fac where fac.sup_code=" + fac +
                    ") group by f.facility_id, f.fac_name order by 2 ";
            }
            if (dat.equals("m") && !quart.equals("0")) {
                sql =
                    "select f.facility_id, f.fac_name,nvl(sum(ci.new_visits),0),nvl(sum(ci.cont_visits),0) from facility f, ctf_item ci,ctf_main cm  where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select fac.facility_id  from facility fac where fac.sup_code=" + fac +
                    ") group by f.facility_id, f.fac_name order by 2";
            }
            if (quart.equals("0")) {
                sql =
                    "select f.facility_id, f.fac_name,nvl(sum(ci.new_visits),0), nvl(sum(ci.cont_visits),0) from facility f, ctf_item ci,ctf_main cm  where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and ci.prod_id=" + prod +
                    " and f.facility_id in (select fac.facility_id  from facility fac where fac.sup_code=" + fac +
                    ") group by f.facility_id, f.fac_name order by 2";
            }
        }


        if (dat.equals("u")) {
            sql =
                "select f.facility_id, f.fac_name,nvl(sum(ci.new_visits),0), nvl(sum(ci.cont_visits),0) from facility f, ctf_item ci,ctf_main cm  where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id=" + prod +
                " and f.facility_id in (select fac.facility_id  from facility fac where fac.sup_code=" + fac +
                ") group by f.facility_id, f.fac_name order by 2";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select f.facility_id, f.fac_name,sum(ci.new_visits), nvl(sum(ci.cont_visits),0) from facility f, ctf_item ci,ctf_main cm  where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod +
                    " and f.facility_id in (select fac.facility_id  from facility fac where fac.sup_code=" + fac +
                    ") group by f.facility_id, f.fac_name order by 2 ";
            }
            if (hq.equals("2")) {
                sql =
                    "select f.facility_id, f.fac_name,sum(ci.new_visits), nvl(sum(ci.cont_visits),0) from facility f, ctf_item ci,ctf_main cm  where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod +
                    " and f.facility_id in (select fac.facility_id  from facility fac where fac.sup_code=" + fac +
                    ") group by f.facility_id, f.fac_name order by 2 ";
            }
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("zzzzzzzzzzzzzzz " + sql);
            rs = stmt.executeQuery(sql);


        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();

        }
        return rs;
    }


    public ResultSet getDirNameDispensedGT(String fac, String year, String quart, String prod, String dat, String fY,
                                           String tY, String fM, String tM, String type, String hq, String faces[]) {
        String dir = "";
        for (int i = 0; i < faces.length; i++) {
            if (i == 0) {
                dir = faces[i];
            } else {
                dir += ", " + faces[i];
            }
        }

        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("q") && !quart.equals("0")) {
                sql =
                    "select f.facility_id, f.fac_name,sum(ci.new_visits), nvl(sum(ci.cont_visits),0) from facility f, ctf_item ci,ctf_main cm  where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select fac.facility_id  from facility fac where fac.sup_code=" + fac +
                    ") and f.facility_id in (" + dir + ") group by f.facility_id, f.fac_name order by 2 ";
            }
            if (dat.equals("m") && !quart.equals("0")) {
                sql =
                    "select f.facility_id, f.fac_name,nvl(sum(ci.new_visits),0),nvl(sum(ci.cont_visits),0) from facility f, ctf_item ci,ctf_main cm  where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select fac.facility_id  from facility fac where fac.sup_code=" + fac +
                    ") and f.facility_id in (" + dir + ") group by f.facility_id, f.fac_name order by 2";
            }
            if (quart.equals("0")) {
                sql =
                    "select f.facility_id, f.fac_name,nvl(sum(ci.new_visits),0), nvl(sum(ci.cont_visits),0) from facility f, ctf_item ci,ctf_main cm  where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and ci.prod_id=" + prod +
                    " and f.facility_id in (select fac.facility_id  from facility fac where fac.sup_code=" + fac +
                    ") and f.facility_id in (" + dir + ") group by f.facility_id, f.fac_name order by 2";
            }
        }


        if (dat.equals("u")) {
            sql =
                "select f.facility_id, f.fac_name,nvl(sum(ci.new_visits),0), nvl(sum(ci.cont_visits),0) from facility f, ctf_item ci,ctf_main cm  where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id=" + prod +
                " and f.facility_id in (select fac.facility_id  from facility fac where fac.sup_code=" + fac +
                ") and f.facility_id in (" + dir + ") group by f.facility_id, f.fac_name order by 2";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select f.facility_id, f.fac_name,sum(ci.new_visits), nvl(sum(ci.cont_visits),0) from facility f, ctf_item ci,ctf_main cm  where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod +
                    " and f.facility_id in (select fac.facility_id  from facility fac where fac.sup_code=" + fac +
                    ") and f.facility_id in (" + dir + ") group by f.facility_id, f.fac_name order by 2 ";
            }
            if (hq.equals("2")) {
                sql =
                    "select f.facility_id, f.fac_name,sum(ci.new_visits), nvl(sum(ci.cont_visits),0) from facility f, ctf_item ci,ctf_main cm  where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod +
                    " and f.facility_id in (select fac.facility_id  from facility fac where fac.sup_code=" + fac +
                    ") and f.facility_id in (" + dir + ") group by f.facility_id, f.fac_name order by 2 ";
            }
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);


        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();

        }
        return rs;
    }


    public String getDirCode(String dir) {
        String sql = "select fac_code from facility where facility_id=" + dir;
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString(1);

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return "non";
        }
    }

    public String getDirSupInfo(String dir, int index) {
        String sql = "select facility_id,fac_name, fac_code,sup_code from facility where facility_id=" + dir;
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString(index);

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return "non";
        }
    }


    public String getDirInfo(String dir, int index) {
        String sql = "select facility_id,fac_name, fac_code from facility where facility_id=" + dir;
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString(index);

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return "non";
        }
    }

    public String getDirType(String dir) {
        String sql =
            "select ft.fac_name ";
        sql += " from facility f, fac_type ft ";
        sql += " where f.fac_type_id=ft.fac_type_id ";
        sql += " and f.facility_id= " + dir;
        System.out.println(sql)  ;
        return (String)UtilsClass.getOneValue(sql);
    }

    public String getProdDose(String prod) {
        String sql = "select  pro_dose from product where prod_id=" + prod;
        return (String)UtilsClass.getOneValue(sql);
    }


    public String getProdName(String prod) {
        String sql = "select  pro_name from product where prod_id=" + prod;
        return (String)UtilsClass.getOneValue(sql);
    }

    public double getProdQty(String prod) {
        String sql = "select  prod_ship_qty_whse from product where prod_id=" + prod;
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.0;
        }
    }

    public String getDate() {
        String sql = "select to_char(sysdate,'dd-MM-yyyy') from dual ";
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return "non";
        }
    }

    public String getTime() {
        String sql = "select to_char(sysdate,'hh24:mi:ss') from dual ";
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return "non";
        }
    }

    public String getGroupName(String group) {
        String sql = "select grp_desc from groups where grp_id=" + group;
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return "non";
        }
    }


    public ResultSet getProducts() {
        String sql = "select prod_id, pro_name, pro_dose from product order by 2";
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }

    
    public List<Product> getProductsList() {
        List<Product> productList = new ArrayList();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "select prod_id, pro_name, pro_dose from product order by 2";
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                Product product = new Product();
                product.setProductID(rs.getInt(1));
                product.setProductName(rs.getString(2));
                productList.add(product);
            }

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return productList;
    }


    // select  p.pro_name, p.pro_dose,  open_bal,receipts,issues, adjustments, closing_bal, avg_mnthly_cons, max_mos, decode(avg_mnthly_cons, 0, 0, (closing_bal / avg_mnthly_cons)*min_mos) mos from ctf_item ci, ctf_main cm, product p,facility f   where cm.facility_id = f.facility_id(+) and ci.prod_id = p.prod_id(+) and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=67  and to_char(ci.p_date,'MM/yyyy')='08/2012'

    public ResultSet getMovStockRep(String dir, String mon, String year, String dat, String fY, String tY, String fM,
                                    String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (mon.equals("0")) {
                sql =
                    "select  p.pro_name, p.pro_dose,  sum(open_bal),sum(receipts),sum(issues), sum(adjustments),nvl(ad.type_name,' '), sum(closing_bal), sum(avg_mnthly_cons), (sum(avg_mnthly_cons) * 6) , ((sum(avg_mnthly_cons) * 6) - sum(closing_bal)), p.prod_id from ctf_item ci, ctf_main cm, product p,facility f , adj_type ad where ci.adj_type_id=ad.adj_type_id(+) and  cm.facility_id = f.facility_id(+) and ci.prod_id = p.prod_id(+) and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=" +
                    dir + "  and to_char(cm.p_date,'yyyy')='" + year +
                    "' group by  p.pro_name, p.pro_dose, p.prod_id,ad.type_name order by 1";
            }
            if (!mon.equals("0") && dat.equals("m")) {
                sql =
                    "select  p.pro_name, p.pro_dose,  sum(open_bal),sum(receipts),sum(issues), sum(adjustments), nvl(ad.type_name,' '), sum(closing_bal), sum(avg_mnthly_cons), (sum(avg_mnthly_cons) * 6) , ((sum(avg_mnthly_cons) * 6) - sum(closing_bal)), p.prod_id from ctf_item ci, ctf_main cm, product p,facility f , adj_type ad where ci.adj_type_id=ad.adj_type_id(+) and cm.facility_id = f.facility_id(+) and ci.prod_id = p.prod_id(+) and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=" +
                    dir + "  and to_char(cm.p_date,'MM/yyyy')='" + mon + "/" + year +
                    "' group by  p.pro_name, p.pro_dose, p.prod_id, ad.type_name order by 1";
            }
            if (!mon.equals("0") && dat.equals("q")) {
                sql =
                    "select  p.pro_name, p.pro_dose,  sum(open_bal),sum(receipts),sum(issues), sum(adjustments),nvl(ad.type_name,' '), sum(closing_bal), sum(avg_mnthly_cons), (sum(avg_mnthly_cons) * 6) , ((sum(avg_mnthly_cons) * 6) - sum(closing_bal)), p.prod_id from ctf_item ci, ctf_main cm, product p,facility f  , adj_type ad where ci.adj_type_id=ad.adj_type_id(+) and cm.facility_id = f.facility_id(+) and ci.prod_id = p.prod_id(+) and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=" +
                    dir + "  and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' group by  p.pro_name, p.pro_dose, p.prod_id, ad.type_name order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select  p.pro_name, p.pro_dose,  sum(open_bal),sum(receipts),sum(issues), sum(adjustments),decode(sum(open_bal) + sum(issues) - sum(receipts), sum(closing_bal), 'D', 'C') typ, sum(closing_bal), sum(avg_mnthly_cons), (sum(avg_mnthly_cons) * 6) , ((sum(avg_mnthly_cons) * 6) - sum(closing_bal)), p.prod_id from ctf_item ci, ctf_main cm, product p,facility f   where cm.facility_id = f.facility_id(+) and ci.prod_id = p.prod_id(+) and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=" +
                dir + "  and cm.p_date between to_date('" + fM + "/" + fY + "','MM/yyyy') and to_date('" + tM + "/" +
                tY + "','MM/yyyy') group by  p.pro_name, p.pro_dose, p.prod_id order by 1 ";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  p.pro_name, p.pro_dose,  sum(open_bal),sum(receipts),sum(issues), sum(adjustments),decode(sum(open_bal) + sum(issues) - sum(receipts), sum(closing_bal), 'D', 'C') typ, sum(closing_bal), sum(avg_mnthly_cons), (sum(avg_mnthly_cons) * 6) , ((sum(avg_mnthly_cons) * 6) - sum(closing_bal)), p.prod_id from ctf_item ci, ctf_main cm, product p,facility f   where cm.facility_id = f.facility_id(+) and ci.prod_id = p.prod_id(+) and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=" +
                    dir + "  and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('1','2') group by  p.pro_name, p.pro_dose, p.prod_id";
            }
            if (hq.equals("2")) {
                sql =
                    "select  p.pro_name, p.pro_dose,  sum(open_bal),sum(receipts),sum(issues), sum(adjustments),decode(sum(open_bal) + sum(issues) - sum(receipts), sum(closing_bal), 'D', 'C') typ, sum(closing_bal), sum(avg_mnthly_cons), (sum(avg_mnthly_cons) * 6) , ((sum(avg_mnthly_cons) * 6) - sum(closing_bal)), p.prod_id from ctf_item ci, ctf_main cm, product p,facility f   where cm.facility_id = f.facility_id(+) and ci.prod_id = p.prod_id(+) and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=" +
                    dir + "  and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('3','4') group by  p.pro_name, p.pro_dose, p.prod_id";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    // select  ft.fac_name, sum(ci.issues), sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id group by ft.fac_name

    public ResultSet getAggStockMovementsOLD(String prod, String mon, String year, String dat, String fY, String tY,
                                          String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues),sum(ci.adjustments), sum(ci.closing_bal),   decode(           (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='" +
                    year + "' and to_char(cmm.p_date,'mm') = '" + mon + "' and ciy.prod_id=" + prod +
                    " and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))              ,0,0,         sum(closing_bal)/ (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='" +
                    year + "' and to_char(cmm.p_date,'mm') = '" + mon + "' and ciy.prod_id=" + prod +
                    " and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))   )                   ,ft.type_hierarchy  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                    "'  group by ft.fac_name, ft.fac_type_id,ft.type_hierarchy order by ft.type_hierarchy,ft.fac_name";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' and f.active=1 group by ft.fac_name, ft.fac_type_id order by 1";
            }
            if (mon.equals("0")) {
                sql =
                    "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.active=1  group by ft.fac_name, ft.fac_type_id order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') group by ft.fac_name, ft.fac_type_id order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(ci.p_date,'Q') in ('1','2') group by ft.fac_name, ft.fac_type_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(ci.p_date,'Q') in ('3','4') group by ft.fac_name, ft.fac_type_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            System.out.println(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public double getAggStockMovementsSum(String prod, String mon, String year, String dat, String fY, String tY,
                                          String fM, String tM, String type, String hq) {
        String sql = "";
        double cb = 0;
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal),   decode(           (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='" +
                    year + "' and to_char(cmm.p_date,'mm') = '" + mon + "' and ciy.prod_id=" + prod +
                    " and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))              ,0,0,         sum(closing_bal)/ (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='" +
                    year + "' and to_char(cmm.p_date,'mm') = '" + mon + "' and ciy.prod_id=" + prod +
                    " and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))   )                   ,ft.type_hierarchy  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                    "'  group by ft.fac_name, ft.fac_type_id,ft.type_hierarchy order by ft.type_hierarchy,ft.fac_name";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' and f.active=1 group by ft.fac_name, ft.fac_type_id order by 1";
            }
            if (mon.equals("0")) {
                sql =
                    "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.active=1  group by ft.fac_name, ft.fac_type_id order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') group by ft.fac_name, ft.fac_type_id order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(ci.p_date,'Q') in ('1','2') group by ft.fac_name, ft.fac_type_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(ci.p_date,'Q') in ('3','4') group by ft.fac_name, ft.fac_type_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                cb += rs.getDouble(7);
            }
            System.out.println(sql);
            return cb;

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }

    }


    // override
    public ResultSet getAggStockMovements(String prod, String mon, String year, String dat, String fY, String tY,
                                          String fM, String tM, String type, String hq, String cen[]) {
        String faces = "";
        for (int p = 0; p < cen.length; p++) {
            if (p == 0) {
                faces = cen[p];
            } else {
                faces += ", " + cen[p];
            }

        }
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS)),ft.type_hierarchy  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and f.facility_id in (" +
                    faces +
                    ") and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                    "' group by ft.fac_name, ft.fac_type_id,ft.type_hierarchy order by ft.type_hierarchy,ft.fac_name";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and f.facility_id in (" +
                    faces +
                    ") and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' group by ft.fac_name, ft.fac_type_id order by 1";
            }
            if (mon.equals("0")) {
                sql =
                    "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and f.facility_id in (" +
                    faces +
                    ") and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "'  group by ft.fac_name, ft.fac_type_id order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and f.facility_id in (" +
                faces + ") and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') group by ft.fac_name, ft.fac_type_id order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and f.facility_id in (" +
                    faces +
                    ") and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(ci.p_date,'Q') in ('1','2') group by ft.fac_name, ft.fac_type_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  ft.fac_type_id, ft.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and f.facility_id in (" +
                    faces +
                    ") and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(ci.p_date,'Q') in ('3','4') group by ft.fac_name, ft.fac_type_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            System.out.println(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public int getFacTypesCount(String facType) {
        String sql = "select count(*) from facility where fac_type_id=" + facType;
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return -1;

        }
    }


    public String getFacTypesCount1(String facType) {
        String sql = "select FACILITY_ID from facility where sup_code=" + facType;
        try {
            String fac = "";
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            int i = 0;
            while (rs.next()) {

                if (i == 0) {
                    fac = rs.getString(1);
                } else {
                    fac += ", " + rs.getString(1);
                }
                i++;
            }
            System.out.println("hiiiiiiiiiiiiiiiii");
            System.out.println(" The facilities are " + fac);
            return fac;
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return "00";

        }
    }


    public Double getClosingBalAggStockMovements(String prod, String mon, String year, String dat, String fY, String tY,
                                                 String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select nvl(sum(ci.closing_bal),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "' and active=0  order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select nvl(sum(ci.closing_bal),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' order by 1";
            } else {
                sql =
                    "select nvl(sum(ci.closing_bal),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year + "' order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select nvl(sum(ci.closing_bal),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy')  order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select nvl(sum(ci.closing_bal),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(ci.p_date,'Q') in ('1','2') order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select nvl(sum(ci.closing_bal),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(ci.p_date,'Q') in ('1','2') order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }


    }

    // override
    public Double getClosingBalAggStockMovements(String prod, String mon, String year, String dat, String fY, String tY,
                                                 String fM, String tM, String type, String hq, String[] cen) {
        String faces = "";
        for (int p = 0; p < cen.length; p++) {
            if (p == 0) {
                faces = cen[p];
            } else {
                faces += ", " + cen[p];
            }

        }
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select nvl(sum(ci.closing_bal),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "' and f.facility_id in (" +
                    faces + ")  order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select nvl(sum(ci.closing_bal),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' and f.facility_id in (" + faces + ") order by 1";
            } else {
                sql =
                    "select nvl(sum(ci.closing_bal),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and f.facility_id in (" + faces +
                    ") order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select nvl(sum(ci.closing_bal),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') and f.facility_id in (" + faces + ")  order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select nvl(sum(ci.closing_bal),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(ci.p_date,'Q') in ('1','2') and f.facility_id in (" + faces + ") order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select nvl(sum(ci.closing_bal),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(ci.p_date,'Q') in ('1','2') and f.facility_id in (" + faces + ") order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }


    }

    public ResultSet getAggStockMovByLevelOLD(String prod, String mon, String year, String dat, String fY, String tY,
                                           String fM, String tM, String type, String hq, String cen[]) {

        // select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=4 and to_char(ci.p_date,'mm/yyyy')='08/2012' group by ft.type_hierarchy
        String sql = "";
        String mon1 = "";
        //        for(int i=0; i<mon.length; i++){
        //            if(i == 0){
        //                mon1 = "'"+mon[i]+"'";
        //            }else{
        //                mon1 += ", '"+mon[i]+"'";
        //            }
        //        }
        if (type.equals("0")) {
            if (dat.equals("m") || dat.equals("q")) {
                if (dat.equals("m") && !mon.equals("0")) {
                    sql =
                        "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'mm') = '" + mon +
                        "' and f.active=1  group by ft.type_hierarchy order by ft.type_hierarchy";
                }
                if (dat.equals("q") && !mon.equals("0")) {
                    sql =
                        "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q') = '" + mon1 +
                        "' and f.active=1  group by ft.type_hierarchy order by ft.type_hierarchy";
                }
                if (mon.equals("0")) {
                    sql =
                        "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')='" + year +
                        "'  and f.active=1  group by ft.type_hierarchy order by ft.type_hierarchy";
                }
            }
            if (dat.equals("u")) {
                sql =
                    "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM +
                    "/" + tY + "','mm/yyyy')  and f.active=1  group by ft.type_hierarchy order by ft.type_hierarchy";
            }
            if (dat.equals("hq")) {
                if (hq.equals("1")) {
                    sql =
                        "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')='" + year +
                        "' and to_char(cm.p_date,'Q') in ('1','2') and f.active=1  group by ft.type_hierarchy order by ft.type_hierarchy";
                } else {
                    sql =
                        "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')='" + year +
                        "' and to_char(cm.p_date,'Q') in ('3','4') and f.active=1  group by ft.type_hierarchy order by ft.type_hierarchy";
                }
            }


        } else {
            String faces = "";
            for (int s = 0; s < cen.length; s++) {
                if (s == 0) {
                    faces = cen[s];
                } else {
                    faces += ", " + cen[s];
                }
            }

            if (dat.equals("m") || dat.equals("q")) {
                if (dat.equals("m") && !mon.equals("0")) {
                    sql =
                        "select  f.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(ci.p_date,'yyyy')='" + year + "' and to_char(ci.p_date,'mm') = '" + mon +
                        "' and f.active=1 and f.fac_type_id=" + type + " and f.facility_id in (" + faces +
                        ") group by f.fac_name order by 1";
                }
                if (dat.equals("q") && !mon.equals("0")) {
                    sql =
                        "select   f.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(ci.p_date,'yyyy')='" + year + "' and to_char(ci.p_date,'Q') = '" + mon1 +
                        "' and f.active=1 and f.fac_type_id=" + type + " and f.facility_id in (" + faces +
                        ")  group by f.fac_name order by 1";
                }
                if (mon.equals("0")) {
                    sql =
                        "select   f.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(ci.p_date,'yyyy')='" + year + "'  and f.active=1 and f.fac_type_id=" +
                        type + " and f.facility_id in (" + faces + ")  group by f.fac_name order by 1";
                }
            }
            if (dat.equals("u")) {
                sql =
                    "select  f.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM +
                    "/" + tY + "','mm/yyyy')  and f.active=1 and f.fac_type_id=" + type + " and f.facility_id in (" +
                    faces + ")  group by f.fac_name order by 1";
            }
            if (dat.equals("hq")) {
                if (hq.equals("1")) {
                    sql =
                        "select  f.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(ci.p_date,'yyyy')='" + year +
                        "' and to_char(ci.p_date,'Q') in ('1','2') and f.active=1 and f.fac_type_id=" + type +
                        " and f.facility_id in (" + faces + ")  group by f.fac_name order by 1";
                } else {
                    sql =
                        "select  f.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(ci.p_date,'yyyy')='" + year +
                        "' and to_char(ci.p_date,'Q') in ('3','4') and f.active=1 and f.fac_type_id=" + type +
                        " and f.facility_id in (" + faces + ")  group by f.fac_name order by 1";
                }
            }

        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getAggStockMovByLevel(String prod, String mon, String year, String dat, String fY, String tY,
                                           String fM, String tM, String type, String hq) {
       
        String sql =
            "select ft.type_hierarchy as type_hierarchy, \n" +
            "(select count(*)  from " +
            "facility mf, fac_type mft  where " +
            " mf.fac_type_id = mft.fac_type_id " +
            " and mft.type_hierarchy =ft.type_hierarchy  ) as ourCount,\n" +



            "sum(receipts), sum(issues),sum(adjustments), sum(avg_mnthly_cons) as avg  \n" +
            "from ctf_main cm, ctf_item ci, \n" +
            "facility f, fac_type ft\n" +
            //"-- ,group_of_facilities gof, groups g\n" +
            "where f.facility_id = cm.facility_id\n" +
             "and cm.ctf_main_id = ci.ctf_main_id\n" +
            "and f.fac_type_id = ft.fac_type_id\n" +
           // "--and ft.fac_type_id = gof.gof_facility_id\n" +
           // "--and gof.gof_grp_id = g.grp_id\n" + "and f.active = 1 \n" +
            "and ci.prod_id = " + prod + " \n";


        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/"+ year+"'" ;
                       


            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'" +
                       "and to_char(cm.p_date,'Q') = '" + mon + "'";

            }
            if (mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'";

            }
        }
        if (dat.equals("u")) {
            sql +=
                " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') " + 
                "and to_date('" + tM + "/" + tY +
                "','mm/yyyy')  ";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + 
                       "' and to_char(cm.p_date,'Q') in ('1','2') ";


            } else if (hq.equals("2")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + 
                       "' and to_char(cm.p_date,'Q') in ('3','4') ";
            }
        }
        try {

            sql += " group by ft.type_hierarchy \n" + "order by 1";
            
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public Double getOpenClosingBalAggStockMovementsByLevel(String prod, String mon, String year, String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq) {
        String sql =     
            "select nvl((ci.closing_bal),0), nvl((ci.open_bal),0), cm.p_date from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) " +
            " and f.fac_type_id= ft.fac_type_id(+) " +
            " and ci.prod_id=" + prod ;
                   
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql += " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'" +
                    " and to_char(cm.p_date,'Q')='" + mon + "'";
            }

            if (mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'";
            }

        }
        if (dat.equals("u")) {
            sql += " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') " +
                " and to_date('" + tM + "/" +tY + "','mm/yyyy') ";
                

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'" +
                    " and to_char(ci.p_date,'Q') in ('1','2')";
            } else {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'" +
                    " and to_char(ci.p_date,'Q') in ('3','4')";
            }
            if(type.equals("cb")){
            sql += "order by 3 desc ";
            }else{
                sql += "order by 3  ";
            }
            //   sql = "select nvl(sum(ci.closing_bal),0) from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(ci.p_date,'mm/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"'";

        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            if(type.equals("cb")){
                return rs.getDouble(1);
            }else{
                return rs.getDouble(2);
            }
           
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }
          
        }


    // ------------------overRide
    public Double getClosingBalAggStockMovementsByLevel(String prod, String mon, String year, String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq,
                                                        String cen[]) {
        String sql = "";
        
        String faces = "";
        for (int s = 0; s < cen.length; s++) {
            if (s == 0) {
                faces = cen[s];

            } else {
                faces += ", " + cen[s];
            }
        }
        
        
        sql =
            "select nvl(sum(ci.closing_bal),0) from ctf_item ci, " +
            "ctf_main cm, facility f, fac_type ft " +
            "where ci.ctf_main_id = cm.ctf_main_id(+) " +
            "and cm.facility_id = f.facility_id(+) " +
            "and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +prod +
            " and ft.fac_type_id=" +type + " and f.facility_id in (" + faces + ")";
                    
            
        
        
        if (!type.equals("0")) {
            if (dat.equals("m") || dat.equals("q")) {

                if (dat.equals("m") && !mon.equals("0")) {
                    sql += " and to_char(ci.p_date,'mm/yyyy')='" + mon + "/" + year + "'"; 
                }
                if (dat.equals("q") && !mon.equals("0")) {
                    if (mon.equals("1")) {
                        sql += " and to_char(ci.p_date,'mm/yyyy')='03/" + year + "'";
                    }else   if (mon.equals("2")) {
                        sql += " and to_char(ci.p_date,'mm/yyyy')='06/" + year + "'";
                    }else   if (mon.equals("3")) {
                        sql += " and to_char(ci.p_date,'mm/yyyy')='09/" + year + "'";
                    }else   if (mon.equals("4")) {
                        sql += " and to_char(ci.p_date,'mm/yyyy')='12/" + year + "'";
                    }
                   
                } else {
                    sql += " and to_char(ci.p_date,'mm/yyyy')='12/" + year + "'";
                }

            }
            if (dat.equals("u")) {
                sql = " and cm.p_date = to_date('" + tM + "/" + tY + "','mm/yyyy') ";

            }
            if (dat.equals("hq")) {
                if (hq.equals("1")) {
                    sql += " and to_char(ci.p_date,'mm/yyyy')='06/" + year + "'";
                } else {
                    sql += " and to_char(ci.p_date,'mm/yyyy')='12/" + year + "'";
                }
                //   sql = "select nvl(sum(ci.closing_bal),0) from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(ci.p_date,'mm/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"'";

            }

        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }

    }
    // end OverRide

    //select count(*) from facility where fac_type_id in (select  fac_type_id from fac_type where type_hierarchy=1)

    public int getFacTypesCountByLevel(String type, String mon, String year, String dat, String fY, String tY,
                                       String fM, String tM, String typeF, String hq) {
        // String sql = "select count(*) from facility f where f.fac_type_id in (select  fac_type_id from fac_type where type_hierarchy="+type+") and f.facility_id in (select cm.facility_id from ctf_main cm where to_char(cm.p_date,'mm/yyyy')='"+mon+"/"+year+"') and f.active=1";
        String sql = "";
        System.out.println(" The type is " + type);
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select count(*) from facility f where f.fac_type_id in (select  fac_type_id from fac_type where type_hierarchy=" +
                    type +
                    ") and f.facility_id in (select cm.facility_id from ctf_main cm where to_char(cm.p_date,'mm/yyyy')='" +
                    mon + "/" + year + "') and f.active=1";
                // sql = "select f.facility_id from  facility f where facility_id not in (select cm.facility_id from ctf_main cm where to_char(cm.p_date,'mm/yyyy')='"+mon+"/"+year+"')" ;
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select count(*) from facility f where f.fac_type_id in (select  fac_type_id from fac_type where type_hierarchy=" +
                    type +
                    ") and f.facility_id in (select cm.facility_id from ctf_main cm where to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')='" + mon + "' ) and f.active=1";
                //sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q')='"+mon+"')" ;
            }
            if (mon.equals("0")) {
                sql =
                    "select count(*) from facility f where f.fac_type_id in (select  fac_type_id from fac_type where type_hierarchy=" +
                    type +
                    ") and f.facility_id in (select cm.facility_id from ctf_main cm where to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.active=1)";
                //sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'yyyy')='"+year+"')" ;
            }
        }
        if (dat.equals("u")) {
            sql =
                "select count(*) from facility f where f.fac_type_id in (select  fac_type_id from fac_type where type_hierarchy=" +
                type +
                ") and f.facility_id in (select cm.facility_id from ctf_main cm where  cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') ) and f.active=1";
            //sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select count(*) from facility f where f.fac_type_id in (select  fac_type_id from fac_type where type_hierarchy=" +
                    type +
                    ") and f.facility_id in (select cm.facility_id from ctf_main cm where to_char(cm.p_date,'Q') in ('1','2') ) and f.active=1";
                //sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')='"+year+"')" ;
            }
            if (hq.equals("2")) {
                sql =
                    "select count(*) from facility f where f.fac_type_id in (select  fac_type_id from fac_type where type_hierarchy=" +
                    type +
                    ") and f.facility_id in (select cm.facility_id from ctf_main cm where to_char(cm.p_date,'Q') in ('3','4') ) and f.active=1";
                //sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')='"+year+"')" ;
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }


        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            } else
                return 0;
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return -1;
        }
    }

    //

    public ResultSet getCypFactorRep(String prod, String quart, String year, String dat, String fY, String tY,
                                     String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp ,ft.type_hierarchy  from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year +
                    " and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
            }
            if (dat.equals("q") && !quart.equals("0")) {
                sql =
                    "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'Q')=" + quart +
                    " and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
            }
            if (dat.equals("m") && !quart.equals("0")) {
                sql =
                    "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'mm')=" + quart +
                    " and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by  ft.fac_name ,ft.type_hierarchy ";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY +
                "','mm/yyyy') and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('1','2') and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
            }
            if (hq.equals("2")) {
                sql =
                    "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('3','4') and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    //select gof_id, gof_facility_id as type, gof_grp_id as group_type from group_of_facilities where gof_grp_id=

    public ResultSet getCypFactorRep(String prod, String quart, String year, String dat, String fY, String tY,
                                     String fM, String tM, String type, String hq, String[] cen) {
        String sql = "";
        String faces = "";
        for (int p = 0; p < cen.length; p++) {
            if (p == 0) {
                faces = cen[p];

            } else {
                faces += ", " + cen[p];
            }
        }
        System.out.println("The Type is " + type);
        System.out.println("The type is now " + getfacTypeHier(type));
        if (!type.equals("7")) {


            if (dat.equals("m") || dat.equals("q")) {
                if (quart.equals("0")) {
                    sql =
                        "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp ,ft.type_hierarchy  from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.facility_id in (" +
                        faces +
                        ") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')=" + year +
                        " and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
                }
                if (dat.equals("q") && !quart.equals("0")) {
                    sql =
                        "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.facility_id in (" +
                        faces +
                        ") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'Q')=" + quart +
                        " and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
                }
                if (dat.equals("m") && !quart.equals("0")) {
                    sql =
                        "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.facility_id in (" +
                        faces +
                        ") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'mm')=" + quart +
                        " and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by  ft.fac_name ,ft.type_hierarchy ";
                }
            }
            if (dat.equals("u")) {
                sql =
                    "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and f.facility_id in (" +
                    faces +
                    ") and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" + prod +
                    " and to_date(to_char(cm.p_date,'mm/yyyy'),'mm/yyyy') between to_date('" + fM + "/" + fY +
                    "','mm/yyyy') and to_date('" + tM + "/" + tY +
                    "','mm/yyyy') and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    sql =
                        "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and f.facility_id in (" +
                        faces +
                        ") and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')=" + year +
                        " and to_char(cm.p_date,'Q') in ('1','2') and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
                }
                if (hq.equals("2")) {
                    sql =
                        "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.facility_id in (" +
                        faces +
                        ") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')=" + year +
                        " and to_char(cm.p_date,'Q') in ('3','4') and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
                }
                ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
            }

        } else {


            if (dat.equals("m") || dat.equals("q")) {
                if (quart.equals("0")) {
                    sql =
                        "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp ,ft.type_hierarchy  from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.sup_code in (" +
                        faces +
                        ") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')=" + year +
                        "  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
                }
                if (dat.equals("q") && !quart.equals("0")) {
                    sql =
                        "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.sup_code in (" +
                        faces +
                        ") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'Q')=" + quart +
                        " and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
                }
                if (dat.equals("m") && !quart.equals("0")) {
                    sql =
                        "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.sup_code in (" +
                        faces +
                        ") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'mm')=" + quart +
                        " and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by  ft.fac_name ,ft.type_hierarchy ";
                }
            }
            if (dat.equals("u")) {
                sql =
                    "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and f.sup_code in (" +
                    faces +
                    ") and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" + prod +
                    " and to_date(to_char(cm.p_date,'mm/yyyy'),'mm/yyyy') between to_date('" + fM + "/" + fY +
                    "','mm/yyyy') and to_date('" + tM + "/" + tY +
                    "','mm/yyyy') and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    sql =
                        "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and f.sup_code in (" +
                        faces +
                        ") and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')=" + year +
                        " and to_char(cm.p_date,'Q') in ('1','2') and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
                }
                if (hq.equals("2")) {
                    sql =
                        "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.sup_code in (" +
                        faces +
                        ") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')=" + year +
                        " and to_char(cm.p_date,'Q') in ('3','4') and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
                }
                ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
            }


        }


        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getCypFactorRepByDirs(String prod, String quart, String year, String dat, String fY, String tY,
                                           String fM, String tM, String type, String hq, String cen) {
        String sql = "";
        //        String faces = "";
        //        for(int p=0; p<cen.length; p++){
        //            if(p == 0){
        //                faces = cen[p];
        //
        //            }else{
        //                faces += ", "+cen[p];
        //            }
        //        }
        System.out.println("The Type is " + type);
        System.out.println("The type is now " + getfacTypeHier(type));
        //   if(!type.equals("7")){

        //
        //        if(dat.equals("m") || dat.equals("q")){
        //        if(quart.equals("0")){
        //         sql = "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp ,ft.type_hierarchy  from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.facility_id in ("+faces+") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')="+year+" and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
        //        }if(dat.equals("q") && !quart.equals("0")){
        //            sql = "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.facility_id in ("+faces+") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q')="+quart+" and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
        //        }
        //        if(dat.equals("m") && !quart.equals("0")){
        //                    sql = "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.facility_id in ("+faces+") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'mm')="+quart+" and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by  ft.fac_name ,ft.type_hierarchy ";
        //                }
        //        }if(dat.equals("u")){
        //            sql = "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and f.facility_id in ("+faces+") and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_date(to_char(cm.p_date,'mm/yyyy'),'mm/yyyy') between to_date('"+fM+"/"+fY+"','mm/yyyy') and to_date('"+tM+"/"+tY+"','mm/yyyy') and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
        //        } if(dat.equals("hq")){
        //              System.out.println("1st &&&&&&&&&&&&& 2nd half year");
        //              if(hq.equals("1")){
        //                      sql = "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and f.facility_id in ("+faces+") and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('1','2') and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
        //                  }if(hq.equals("2")){
        //                      sql = "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.facility_id in ("+faces+") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('3','4') and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
        //                  }
        //            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        //        }
        //   null;

        //   }else
        {


            if (dat.equals("m") || dat.equals("q")) {
                if (quart.equals("0")) {
                    sql =
                        "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp ,ft.type_hierarchy  from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.sup_code in (" +
                        cen +
                        ") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')=" + year +
                        "  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
                }
                if (dat.equals("q") && !quart.equals("0")) {
                    sql =
                        "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.sup_code in (" +
                        cen +
                        ") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'Q')=" + quart +
                        " and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
                }
                if (dat.equals("m") && !quart.equals("0")) {
                    sql =
                        "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.sup_code in (" +
                        cen +
                        ") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'mm')=" + quart +
                        " and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by  ft.fac_name ,ft.type_hierarchy ";
                }
            }
            if (dat.equals("u")) {
                sql =
                    "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and f.sup_code in (" +
                    cen + ") and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_date(to_char(cm.p_date,'mm/yyyy'),'mm/yyyy') between to_date('" + fM + "/" + fY +
                    "','mm/yyyy') and to_date('" + tM + "/" + tY +
                    "','mm/yyyy') and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    sql =
                        "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and f.sup_code in (" +
                        cen +
                        ") and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')=" + year +
                        " and to_char(cm.p_date,'Q') in ('1','2') and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
                }
                if (hq.equals("2")) {
                    sql =
                        "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.sup_code in (" +
                        cen +
                        ") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')=" + year +
                        " and to_char(cm.p_date,'Q') in ('3','4') and ft.type_hierarchy=3  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
                }
                ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
            }


        }


        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public double getCypFactorRepIs(String prod, String quart, String year, String dat, String fY, String tY, String fM,
                                    String tM, String type, String hq, String[] cen, int index) {
        String sql = "";
        String faces = "";
        for (int p = 0; p < cen.length; p++) {
            if (p == 0) {
                faces = cen[p];

            } else {
                faces += ", " + cen[p];
            }
        }
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp ,ft.type_hierarchy  from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.facility_id in (" +
                    faces +
                    ") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year + " and ft.type_hierarchy=3 and ft.fac_type_id=" +
                    type +
                    "  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
            }
            if (dat.equals("q") && !quart.equals("0")) {
                sql =
                    "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.facility_id in (" +
                    faces +
                    ") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'Q')=" + quart +
                    " and ft.type_hierarchy=3 and ft.fac_type_id=" + type +
                    " group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
            }
            if (dat.equals("m") && !quart.equals("0")) {
                sql =
                    "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id  and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'mm')=" + quart +
                    " and ft.type_hierarchy=3 and ft.fac_type_id=" + type +
                    "  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by  ft.fac_name ,ft.type_hierarchy ";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and f.facility_id in (" +
                faces + ") and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') and ft.type_hierarchy=3 and ft.fac_type_id=" + type +
                "  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id and f.facility_id in (" +
                    faces +
                    ") and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" + prod +
                    " and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('1','2') and ft.type_hierarchy=3 and ft.fac_type_id=" + type +
                    "  group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
            }
            if (hq.equals("2")) {
                sql =
                    "select  ft.fac_name, ft.fac_type_id ,(select count(*) from facility fac where fac.fac_type_id=ft.fac_type_id) , sum(ci.issues), p.prod_cyp,ft.type_hierarchy   from ctf_item ci, ctf_main cm, facility f, fac_type ft,product p where ci.prod_id = p.prod_id and f.facility_id in (" +
                    faces +
                    ") and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id = f.facility_id and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('3','4') and ft.type_hierarchy=3  and ft.fac_type_id=" + type +
                    " group by ft.fac_name, ft.fac_type_id, p.prod_cyp,ft.type_hierarchy order by ft.fac_name ,ft.type_hierarchy ";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(index);
            } else {
                return 0.011111;
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }

    }


    

    public ResultSet getSupplyStatusRep(String fac, String mon, String year, String prod, String dat, String fY,
                                        String tY, String fM, String tM, String type, String hq) {
        String   sql = " select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), \n";
                 sql +=" nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), \n";
                 sql +=" decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), \n";
                 sql +=" nvl(sum(AVG_MNTHLY_CONS),0),f.facility_id , ft.fac_type_id \n";
                 sql += " from ctf_item ci, ctf_main cm, facility f, fac_type ft \n";
                 sql += " where ci.ctf_main_id = cm.ctf_main_id(+) \n";
                 sql += " and cm.facility_id = f.facility_id(+) \n";
                 sql += " and f.fac_type_id= ft.fac_type_id(+) \n";
                 sql += " and ci.prod_id=" + prod +" \n";
                 sql += " and f.facility_id in (select ff.facility_id from facility ff ";
                 sql += " where sup_code=" + fac +  ") \n";
                 
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                
                sql +=" and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +"' \n";
               
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "' \n";
                sql += " and to_char(cm.p_date,'Q')='" + mon +"' \n";
            }
            if (mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year +"' \n";
               
            }
        }
        if (dat.equals("u")) {
            sql +=" and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') \n";
            sql +=" and to_date('" + tM + "/" + tY + "','mm/yyyy') \n";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year +"' \n";
                sql += " and to_char(cm.p_date,'Q') in ('1','2')  \n";
                
            }
            if (hq.equals("2")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year +"' \n";
                sql += " and to_char(cm.p_date,'Q') in ('3','4') \n";
            }
           
        }
        sql += " group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id  \n";
        sql += " order by 1 ";
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
        // closePst(pst);
        }
                     
        
    }


    public double getSupplyStatusIndAvgRep(String fac, String mon, String year, String prod, String dat, String fY,
                                           String tY, String fM, String tM, String type, String hq) {
        double avg = 0;
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0),f.facility_id , ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id  order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0),f.facility_id  , ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name, f.facility_id, ft.fac_type_id order by 1";
            }
            if (mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) ,f.facility_id , ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) ,f.facility_id , ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                ") group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0),f.facility_id , ft.fac_type_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('1','2') and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) ,f.facility_id , ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('3','4') and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                avg += rs.getDouble(7);
            }

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return avg;
    }


    public ResultSet getSupplyStatusRepGT(String fac, String mon, String year, String prod, String dat, String fY,
                                          String tY, String fM, String tM, String type, String hq, String cen[]) {
        String face = "";
        for (int i = 0; i < cen.length; i++) {
            if (i == 0) {
                face = cen[i];
            } else {
                face += ", " + cen[i];
            }
        }
        System.out.println(" faces are " + face);
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0),f.facility_id , ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") and f.facility_id in (" + face +
                    ") group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id  order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0),f.facility_id  , ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") and f.facility_id in (" + face +
                    ") group by f.fac_name, ft.fac_name, f.facility_id, ft.fac_type_id order by 1";
            }
            if (mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) ,f.facility_id , ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") and f.facility_id in (" + face +
                    ") group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) ,f.facility_id , ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                ") and f.facility_id in (" + face +
                ") group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0),f.facility_id , ft.fac_type_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('1','2') and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") and f.facility_id in (" + face +
                    ") group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) ,f.facility_id , ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('3','4') and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") and f.facility_id in (" + face +
                    ") group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public double getSupplyStatusRepCB(String fac, String mon, String year, String prod, String dat, String fY,
                                       String tY, String fM, String tM, String type, String hq, String specDir) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0),f.facility_id , ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") and f.facility_id=" + specDir +
                    " group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id  order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0),f.facility_id  , ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name, f.facility_id, ft.fac_type_id order by 1";
            }
            if (mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) ,f.facility_id , ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) ,f.facility_id , ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                ") group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0),f.facility_id , ft.fac_type_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('1','2') and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) ,f.facility_id , ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('3','4') and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name,f.facility_id, ft.fac_type_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getDouble(5);

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.0;
        }
    }


    public double getSupplyStatusRepSum(String fac, String mon, String year, String prod, String dat, String fY,
                                        String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        double avg = 0;
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name  order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {

                String q = "";
                if (mon.equals("1")) {
                    q = "to_char(cm.p_date,'mm')='03'";
                }
                if (mon.equals("2")) {
                    q = "to_char(cm.p_date,'mm')='06'";
                }
                if (mon.equals("3")) {
                    q = "to_char(cm.p_date,'mm')='09'";
                }
                if (mon.equals("4")) {
                    q = "to_char(cm.p_date,'mm')='12'";
                }


                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and " + q +
                    " and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }

            if (mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='12' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and to_char(cm.p_date,'mm/yyyy') = '" + tM + "/" + tY +
                "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                ") group by f.fac_name, ft.fac_name  order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='06' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='12' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("Sum " + sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                avg += rs.getDouble(7);
            }
            System.out.println(" avarage is **************" + avg);
            return avg;

        } catch (Exception e) {
            // TODO: Add catch code


            e.printStackTrace();
            return 0.010;
        }

    }


    public double getSupplyStatusRepSum(String fac, String mon, String year, String prod[], String dat, String fY,
                                        String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        double avg = 0;
        String products = "";
        for (int i = 0; i < prod.length; i++) {
            if (i == 0) {
                products = prod[i];
            } else {
                products += ", " + prod[i];
            }
        }


        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                    products + ") and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name  order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {

                String q = "";
                if (mon.equals("1")) {
                    q = "to_char(cm.p_date,'mm')='03'";
                }
                if (mon.equals("2")) {
                    q = "to_char(cm.p_date,'mm')='06'";
                }
                if (mon.equals("3")) {
                    q = "to_char(cm.p_date,'mm')='09'";
                }
                if (mon.equals("4")) {
                    q = "to_char(cm.p_date,'mm')='12'";
                }


                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                    products + ") and to_char(cm.p_date,'yyyy')='" + year + "' and " + q +
                    " and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }

            if (mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                    products + ") and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='12' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                products + ") and to_char(cm.p_date,'mm/yyyy') = '" + tM + "/" + tY +
                "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                ") group by f.fac_name, ft.fac_name  order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                    products + ") and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='06' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                    products + ") and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='12' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("Sum " + sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                avg += rs.getDouble(7);
            }
            System.out.println(" avarage is **************" + avg);
            return avg;

        } catch (Exception e) {
            // TODO: Add catch code


            e.printStackTrace();
            return 0.010;
        }

    }


    public double getSupplyStatusRepSumGT(String fac, String mon, String year, String prod, String dat, String fY,
                                          String tY, String fM, String tM, String type, String hq, String[] cen) {
        String face = "";
        for (int i = 0; i < cen.length; i++) {
            if (i == 0) {
                face = cen[i];
            } else {
                face += cen[i];
            }
        }

        String sql = "";
        double avg = 0;
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") and f.facility_id in (" + face + ") group by f.fac_name, ft.fac_name  order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {

                String q = "";
                if (mon.equals("1")) {
                    q = "to_char(cm.p_date,'mm')='03'";
                }
                if (mon.equals("2")) {
                    q = "to_char(cm.p_date,'mm')='06'";
                }
                if (mon.equals("3")) {
                    q = "to_char(cm.p_date,'mm')='09'";
                }
                if (mon.equals("4")) {
                    q = "to_char(cm.p_date,'mm')='12'";
                }


                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and " + q +
                    " and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") and f.facility_id in (" + face + ") group by f.fac_name, ft.fac_name order by 1";
            }

            if (mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='12' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") and f.facility_id in (" + face + ") group by f.fac_name, ft.fac_name order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and to_char(cm.p_date,'mm/yyyy') = '" + tM + "/" + tY +
                "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                ") and f.facility_id in (" + face + ") group by f.fac_name, ft.fac_name  order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='06' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") and f.facility_id in (" + face + ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='12' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") and f.facility_id in (" + face + ") group by f.fac_name, ft.fac_name order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("Sum " + sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                avg += rs.getDouble(7);
            }
            System.out.println(" avarage is **************" + avg);
            return avg;

        } catch (Exception e) {
            // TODO: Add catch code


            e.printStackTrace();
            return 0.010;
        }

    }


    public double getSupplyStatusRepMchSum(String fac, String mon, String year, String prod, String dat, String fY,
                                           String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        double avg = 0;
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name  order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                ") group by f.fac_name, ft.fac_name  order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('1','2') and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('3','4') and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                avg += rs.getInt(7);
            }
            System.out.println(" avarage is **************" + avg);
            return avg;

        } catch (Exception e) {
            // TODO: Add catch code


            e.printStackTrace();
            return 0.010;
        }

    }


    public ResultSet getSupplyStatusRepCondition(String fac, String mon, String year, String prod, String dat,
                                                 String fY, String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ")   order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") order by 1";
            }
            if (mon.equals("0")) {
                sql =
                    "select    nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select    nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                ")  order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select    nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('1','2') and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ")  order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('3','4') and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getSupplyStatusByFacRep(String fac, String mon, String year, String prod, String dat, String fY,
                                             String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id ,ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "' and f.facility_id=" + fac +
                    " group by f.fac_name, ft.fac_name , f.facility_id,ft.fac_type_id order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id ,ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'Q')='" + mon + "' and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id=" + fac +
                    " group by f.fac_name, ft.fac_name, f.facility_id ,ft.fac_type_id order by 1";
            }
            if (mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id ,ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and  to_char(cm.p_date,'yyyy')='" + year + "' and f.facility_id=" + fac +
                    " group by f.fac_name, ft.fac_name , f.facility_id,ft.fac_type_id order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) , f.facility_id ,ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') and f.facility_id=" + fac +
                " group by f.fac_name, ft.fac_name, f.facility_id ,ft.fac_type_id order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) , f.facility_id ,ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id=" + fac +
                    " group by f.fac_name, ft.fac_name, f.facility_id,ft.fac_type_id  order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id ,ft.fac_type_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id=" + fac +
                    " group by f.fac_name, ft.fac_name , f.facility_id,ft.fac_type_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getSupplyStatusByFacRepGT(String fac, String mon, String year, String prod, String dat, String fY,
                                               String tY, String fM, String tM, String type, String hq, String cen[]) {
        String face = "";
        String sqType = "";
        ResultSet rsType;
        
        if (Integer.parseInt(type) > 100) {

            sqType =
                "select gof_facility_id from group_of_facilities where gof_grp_id=" + (Integer.parseInt(type) - 100);
            type = "";
            try {
                getDBConnection();
                stmt = conn.prepareStatement(sqType);

                rsType = stmt.executeQuery(sqType);
                System.out.println(sqType);
                int s = 0;
                while (rsType.next()) {
                    if (s == 0) {
                        type = rsType.getString(1);
                    } else {
                        type += "                 , " + rsType.getString(1);
                    }
                    s++;
                }

            } catch (SQLException e) {
            }
        }
        for (int i = 0; i < cen.length; i++) {
            if (i == 0) {
                face = cen[i];

            } else {
                face += ", " + cen[i];
            }
        }
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id ,ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "' and f.facility_id=" + fac +
                    " and ft.fac_type_id in (" + type +
                    ")  group by f.fac_name, ft.fac_name , f.facility_id,ft.fac_type_id order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id ,ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'Q')='" + mon + "' and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id=" + fac + " and ft.fac_type_id in (" + type +
                    ") group by f.fac_name, ft.fac_name, f.facility_id ,ft.fac_type_id order by 1";
            }
            if (mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id ,ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and  to_char(cm.p_date,'yyyy')='" + year + "' and f.facility_id=" + fac +
                    " and ft.fac_type_id in (" + type +
                    ") group by f.fac_name, ft.fac_name , f.facility_id,ft.fac_type_id order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) , f.facility_id ,ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') and f.facility_id=" + fac + " and ft.fac_type_id in (" + type +
                ") group by f.fac_name, ft.fac_name, f.facility_id ,ft.fac_type_id order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) , f.facility_id ,ft.fac_type_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id=" + fac + " and ft.fac_type_id in (" + type +
                    ") group by f.fac_name, ft.fac_name, f.facility_id,ft.fac_type_id  order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id ,ft.fac_type_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id=" + fac + " and ft.fac_type_id in (" + type +
                    ") group by f.fac_name, ft.fac_name , f.facility_id,ft.fac_type_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("         ***    " + sql);
            rs = stmt.executeQuery(sql);

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public double getSupplyStatusByFacRepClosedBal(String fac, String mon, String year, String prod, String dat,
                                                   String fY, String tY, String fM, String tM, String type, String hq,
                                                   String t) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and ft.fac_type_id=" + t + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                    "' and f.facility_id=" + fac + " group by f.fac_name, ft.fac_name , f.facility_id order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "  and ft.fac_type_id=" + t + " and to_char(cm.p_date,'Q')='" + mon +
                    "' and to_char(cm.p_date,'yyyy')='" + year + "' and f.facility_id=" + fac +
                    " group by f.fac_name, ft.fac_name, f.facility_id  order by 1";
            }
            if (mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "  and ft.fac_type_id=" + t + " and  to_char(cm.p_date,'yyyy')='" + year +
                    "' and  to_char(cm.p_date,'mm')='12' and f.facility_id=" + fac +
                    " group by f.fac_name, ft.fac_name , f.facility_id order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) , f.facility_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + "  and ft.fac_type_id=" + t + " and to_char(cm.p_date,'mm/yyyy')= '" + tM + "/" + tY +
                "' and f.facility_id=" + fac + " group by f.fac_name, ft.fac_name, f.facility_id  order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) , f.facility_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "  and ft.fac_type_id=" + t +
                    " and  to_char(cm.p_date,'mm')='03' and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id=" + fac + " group by f.fac_name, ft.fac_name, f.facility_id  order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "  and ft.fac_type_id=" + t +
                    " and to_char(cm.p_date,'mm')='12' and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id=" + fac + " group by f.fac_name, ft.fac_name , f.facility_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(5);
            } else {
                return 0.0;
            }

        } catch (Exception e) {
            // TODO: Add catch code

            e.printStackTrace();
            return 0.00000;
        }

    }


    public double getSupplyStatusByFacRepClosedBalAll(String fac, String mon, String year, String prod, String dat,
                                                      String fY, String tY, String fM, String tM, String type,
                                                      String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "  and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "' and f.facility_id=" + fac +
                    " group by f.fac_name, ft.fac_name , f.facility_id order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                String q = "";
                if (mon.equals("1")) {
                    q = "to_char(cm.p_date,'mm')='03'";
                }
                if (mon.equals("2")) {
                    q = "to_char(cm.p_date,'mm')='06'";
                }
                if (mon.equals("3")) {
                    q = "to_char(cm.p_date,'mm')='09'";
                }
                if (mon.equals("4")) {
                    q = "to_char(cm.p_date,'mm')='12'";
                }
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "  and " + q + " and to_char(cm.p_date,'yyyy')='" + year + "' and f.facility_id=" + fac +
                    " group by f.fac_name, ft.fac_name, f.facility_id  order by 1";
            }
            if (mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "  and  to_char(cm.p_date,'yyyy')='" + year +
                    "' and  to_char(cm.p_date,'mm')='12' and f.facility_id=" + fac +
                    " group by f.fac_name, ft.fac_name , f.facility_id order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) , f.facility_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + "   and to_char(cm.p_date,'mm/yyyy')= '" + tM + "/" + tY + "' and f.facility_id=" + fac +
                " group by f.fac_name, ft.fac_name, f.facility_id  order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) , f.facility_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "   and  to_char(cm.p_date,'mm')='06' and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id=" + fac + " group by f.fac_name, ft.fac_name, f.facility_id  order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "   and to_char(cm.p_date,'mm')='12' and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id=" + fac + " group by f.fac_name, ft.fac_name , f.facility_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(5);
            } else {
                return 0.0;
            }

        } catch (Exception e) {
            // TODO: Add catch code

            e.printStackTrace();
            return 0.00000;
        }

    }


    public double getSupplyStatusByFacRepClosedBalAllGT(String fac, String mon, String year, String prod, String dat,
                                                        String fY, String tY, String fM, String tM, String type,
                                                        String hq) {
        String sql = "";


        String sqType = "";
        ResultSet rsType;
        
        if (Integer.parseInt(type) > 100) {

            sqType =
                "select gof_facility_id from group_of_facilities where gof_grp_id=" + (Integer.parseInt(type) - 100);
            type = "";
            try {
                getDBConnection();
                stmt = conn.prepareStatement(sqType);
                rsType = stmt.executeQuery(sqType);
                int s = 0;
                while (rsType.next()) {
                    if (s == 0) {
                        type = rsType.getString(1);
                    } else {
                        type = " " + rsType.getString(1);
                    }
                    s++;
                }

            } catch (SQLException e) {
            }
        }


        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") &&
                !mon.equals("0")) {
                //   sql = "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+"  and to_char(cm.p_date,'mm/yyyy')='"+mon+"/"+year+"' and f.facility_id="+fac+" and ft.fac_type_id in ("+type+") group by f.fac_name, ft.fac_name , f.facility_id order by 1";
                sql =
 "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
 prod + "  and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "' and f.facility_id=" + fac +
 "  group by f.fac_name, ft.fac_name , f.facility_id order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                String q = "";
                if (mon.equals("1")) {
                    q = "to_char(cm.p_date,'mm')='03'";
                }
                if (mon.equals("2")) {
                    q = "to_char(cm.p_date,'mm')='06'";
                }
                if (mon.equals("3")) {
                    q = "to_char(cm.p_date,'mm')='09'";
                }
                if (mon.equals("4")) {
                    q = "to_char(cm.p_date,'mm')='12'";
                }
                // sql = "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+"  and "+q+" and to_char(cm.p_date,'yyyy')='"+year+"' and f.facility_id="+fac+" group by f.fac_name, ft.fac_name, f.facility_id  order by 1";
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "  and " + q + " and to_char(cm.p_date,'yyyy')='" + year + "' and f.facility_id=" + fac +
                    " group by f.fac_name, ft.fac_name, f.facility_id  order by 1";
            }
            if (mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "  and  to_char(cm.p_date,'yyyy')='" + year +
                    "' and  to_char(cm.p_date,'mm')='12' and f.facility_id=" + fac +
                    " group by f.fac_name, ft.fac_name , f.facility_id order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) , f.facility_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + "   and to_char(cm.p_date,'mm/yyyy')= '" + tM + "/" + tY + "' and f.facility_id=" + fac +
                " group by f.fac_name, ft.fac_name, f.facility_id  order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) , f.facility_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "   and  to_char(cm.p_date,'mm')='06' and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id=" + fac + " group by f.fac_name, ft.fac_name, f.facility_id  order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "   and to_char(cm.p_date,'mm')='12' and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id=" + fac + " group by f.fac_name, ft.fac_name , f.facility_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("now  " + sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(5);
            } else {
                return 0.0;
            }

        } catch (Exception e) {
            // TODO: Add catch code

            e.printStackTrace();
            return 0.00000;
        }

    }


    public double getSupplyStatusByFacRepAvgAll(String fac, String mon, String year, String prod, String dat, String fY,
                                                String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "  and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "' and f.facility_id=" + fac +
                    " group by f.fac_name, ft.fac_name , f.facility_id order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "  and to_char(cm.p_date,'Q')='" + mon + "' and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id=" + fac + " group by f.fac_name, ft.fac_name, f.facility_id  order by 1";
            }
            if (mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "  and  to_char(cm.p_date,'yyyy')='" + year +
                    "' and  to_char(cm.p_date,'mm')='12' and f.facility_id=" + fac +
                    " group by f.fac_name, ft.fac_name , f.facility_id order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) , f.facility_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + "   and to_char(cm.p_date,'mm/yyyy')= '" + tM + "/" + tY + "' and f.facility_id=" + fac +
                " group by f.fac_name, ft.fac_name, f.facility_id  order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0) , f.facility_id from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "   and  to_char(cm.p_date,'mm')='03' and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id=" + fac + " group by f.fac_name, ft.fac_name, f.facility_id  order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  f.fac_name, ft.fac_name,   nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,nvl(sum(closing_bal),0)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0), f.facility_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + "   and to_char(cm.p_date,'mm')='12' and to_char(cm.p_date,'yyyy')='" + year +
                    "' and f.facility_id=" + fac + " group by f.fac_name, ft.fac_name , f.facility_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(7);
            } else {
                return 0.0;
            }

        } catch (Exception e) {
            // TODO: Add catch code

            e.printStackTrace();
            return 0.00000;
        }

    }


    // select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p where ci.prod_id = p.prod_id and to_char(p_date,'Q')=1 and to_char(p_date,'yyyy')='2012' group by pro_name, pro_dose order by 1

    public ResultSet getProgramServicesStatisticsRep(String quart, String year, String dat, String fY, String tY,
                                                     String fM, String tM, String hq) {

        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id  and to_char(cm.p_date,'yyyy')=" +
                    year + " group by pro_name, pro_dose order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'Q')=" +
                    quart + " and to_char(cm.p_date,'yyyy')=" + year + " group by pro_name, pro_dose order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'mm')=" +
                    quart + " and to_char(cm.p_date,'yyyy')=" + year + " group by pro_name, pro_dose order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id  and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                "','mm/yyyy') group by pro_name, pro_dose order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')=" +
                    year + " group by pro_name, pro_dose order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')=" +
                    year + " group by pro_name, pro_dose order by 1";
            }
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getProgramServicesStatisticsRep(String quart, String year, String dat, String fY, String tY,
                                                     String fM, String tM, String type, String hq, String cen[]) {
        String faces = "";
        for (int i = 0; i < cen.length; i++) {
            if (i == 0) {
                faces = cen[i];

            } else {
                faces += ", " + cen[i];
            }
        }
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p, ctf_main cm, facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id  and to_char(cm.p_date,'yyyy')=" +
                    year + "  and f.facility_id in (" + faces + ") group by pro_name, pro_dose order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'Q')=" +
                    quart + " and to_char(cm.p_date,'yyyy')=" + year + "  and f.facility_id in (" + faces +
                    ") group by pro_name, pro_dose order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'mm')=" +
                    quart + " and to_char(cm.p_date,'yyyy')=" + year + "  and f.facility_id in (" + faces +
                    ") group by pro_name, pro_dose order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p, ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id  and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and f.facility_id in (" +
                faces + ") group by pro_name, pro_dose order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')=" +
                    year + "  and f.facility_id in (" + faces + ") group by pro_name, pro_dose order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')=" +
                    year + "  and f.facility_id in (" + faces + ")  group by pro_name, pro_dose order by 1";
            }
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getProgramServicesStatisticsRepG(String quart, String year, String dat, String fY, String tY,
                                                      String fM, String tM, String type, String hq, String cen[],
                                                      String fac) {
        String faces = "";
        for (int i = 0; i < cen.length; i++) {
            if (i == 0) {
                faces = cen[i];

            } else {
                faces += ", " + cen[i];
            }
        }
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p, ctf_main cm, facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id  and to_char(cm.p_date,'yyyy')=" +
                    year + "  and f.facility_id in (" + faces + ") and f.sup_code=" + fac +
                    " group by pro_name, pro_dose order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'Q')=" +
                    quart + " and to_char(cm.p_date,'yyyy')=" + year + " and f.facility_id in (" + faces +
                    ") and f.sup_code=" + fac + " group by pro_name, pro_dose order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'mm')=" +
                    quart + " and to_char(cm.p_date,'yyyy')=" + year + "  and f.facility_id in (" + faces +
                    ") and f.sup_code=" + fac + " group by pro_name, pro_dose order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p, ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id  and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and f.facility_id in (" +
                faces + ") and f.sup_code=" + fac + " group by pro_name, pro_dose order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')=" +
                    year + "  and f.facility_id in (" + faces + ") and f.sup_code=" + fac +
                    " group by pro_name, pro_dose order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')=" +
                    year + "  and f.facility_id in (" + faces + ") and f.sup_code=" + fac +
                    "  group by pro_name, pro_dose order by 1";
            }
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getProgramServicesStatisticsRepGall(String quart, String year, String dat, String fY, String tY,
                                                         String fM, String tM, String type, String hq, String fac) {
        //   String faces = "";
        //        for(int i=0; i<cen.length; i++){
        //            if(i==0){
        //                faces = cen[i];
        //
        //            }else{
        //                faces += ", "+cen[i];
        //            }
        //        }
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p, ctf_main cm, facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id  and to_char(cm.p_date,'yyyy')=" +
                    year + "  and f.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                    fac + ") group by pro_name, pro_dose order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'Q')=" +
                    quart + " and to_char(cm.p_date,'yyyy')=" + year +
                    " and f.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" + fac +
                    ")  group by pro_name, pro_dose order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'mm')=" +
                    quart + " and to_char(cm.p_date,'yyyy')=" + year +
                    "  and f.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" + fac +
                    ")  group by pro_name, pro_dose order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p, ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id  and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                "','mm/yyyy')  and f.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                fac + ")  and f.sup_code=" + fac + " group by pro_name, pro_dose order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')=" +
                    year + "  and f.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                    fac + ")  group by pro_name, pro_dose order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')=" +
                    year + "  and f.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                    fac + ")  group by pro_name, pro_dose order by 1";
            }
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getProgramServicesStatisticsRepGT(String quart, String year, String dat, String fY, String tY,
                                                       String fM, String tM, String type, String hq, String cen[]) {
        String faces = "";
        for (int i = 0; i < cen.length; i++) {
            if (i == 0) {
                faces = cen[i];

            } else {
                faces += ", " + cen[i];
            }
        }
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p, ctf_main cm, facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id  and to_char(cm.p_date,'yyyy')=" +
                    year + " and  f.facility_id in (select ff.facility_id from facility ff where ff.sup_code in (" +
                    faces + ")) group by pro_name, pro_dose order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'Q')=" +
                    quart + " and to_char(cm.p_date,'yyyy')=" + year +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code in (" + faces +
                    ") ) group by pro_name, pro_dose order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'mm')=" +
                    quart + " and to_char(cm.p_date,'yyyy')=" + year +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code in (" + faces +
                    ") ) group by pro_name, pro_dose order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p, ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id  and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                "','mm/yyyy') and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code in (" +
                faces + ")) group by pro_name, pro_dose order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')=" +
                    year + " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code in (" +
                    faces + ")) group by pro_name, pro_dose order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    " select pro_name,pro_dose, nvl(sum(new_visits),0), nvl(sum(cont_visits),0)  from ctf_item ci, product p , ctf_main cm , facility f, fac_type ft where ft.fac_type_id=f.fac_type_id and cm.facility_id=f.facility_id and cm.ctf_main_id=ci.ctf_main_id and ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')=" +
                    year + " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code in (" +
                    faces + "))  group by pro_name, pro_dose order by 1";
            }
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }
    
    public ResultSet getProgDisToUserRep(String quart, String year, String dat, String fY, String tY, String fM,
                                         String tM, String hq, String type, String facilities[]) {
        
        String facilityId = "";
           if(type.equals("0")){
               facilityId = "498";
           }else{
//               for(int k=0; k< facilities.length; k++){
//                   if(k==0)
//                   facililyId 
//               }
           }
        String sql = "";
        sql =" select p.prod_id, p.pro_name ";
        sql += " , p.pro_dose";
        sql += ", dispensed(p.prod_id, 498, '" + quart + "', '" + year + "', '";
        sql += dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq+ "', '"   + type + "') as dispensed \n";
        sql += " FROM PRODUCT p ";
       System.out.println(sql);
       try {
           System.out.println("***************** TYPE IS : " + type);
           getDBConnection();
           stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
           rs = stmt.executeQuery(sql);
       } catch (Exception e) {
           // TODO: Add catch code
           e.printStackTrace();
       }
       return rs;
       }


    public static double Dispensed(String mon, String year,String dat, String product, 
                                String fY, String tY,String fM, 
                                String tM, String hq,
                                String centers[], String type) {
        return UtilsClass.Dispensed(mon, year, dat, product, fY, tY, fM, tM, hq, centers, type);
                                }
    public ResultSet getProgDisToUserRep(String quart, String year, String dat, String fY, String tY, String fM,
                                         String tM, String hq) {
        String sql = "";
        sql =" select p.prod_id, p.pro_name ";
        sql += " , p.pro_dose";
        //sql += ", dispensed(p.prod_id, 498, '" + quart + "', '" + year + "', '";
        //sql += dat + "', '" + fY + "', '" + tY + "', '" + fM + "', '" + tM + "', '" + hq+ "', '0') as dispensed \n";
        //sql += " , nvl(sum(ci.issues),0) ";
        sql += " from Product p";
        //sql += UtilsClass.innerClause(quart, year, dat, fY, tY, fM, tM, hq);
        //sql += " group by pro_name, pro_dose order by 1";
        
        System.out.println(sql);
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getProgDisToUserRepByProd(String quart, String year, String dat, String fY, String tY, String fM,
                                               String tM, String hq, String prod, String type) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id  and to_char(cm.p_date,'yyyy')=" +
                    year + " and p.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3) group by pro_name, pro_dose order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q')=" +
                    quart + " and p.prod_id=" + prod + " and to_char(cm.p_date,'yyyy')=" + year +
                    " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3) group by pro_name, pro_dose order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'mm')=" +
                    quart + " and to_char(cm.p_date,'yyyy')=" + year + " and p.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3) group by pro_name, pro_dose order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and  cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and p.prod_id=" + prod +
                " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3) group by pro_name, pro_dose order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('1','2') and to_char(ci.p_date,'yyyy')=" +
                    year + " and p.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3) group by pro_name, pro_dose order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('3','4') and  to_char(ci.p_date,'yyyy')=" +
                    year + " and p.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3) group by pro_name, pro_dose order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }

//    public ResultSet getProgDisToUserRep(String quart, String year, String dat, String fY, String tY, String fM,
//                                         String tM, String type, String hq) throws SQLException {
//        String sql = "";
//        String sqlOrg = "select type_hierarchy from fac_type where fac_type_id=" + type;
//        getDBConnection();
//        PreparedStatement pstOrg = conn.prepareStatement(sqlOrg);
//        ResultSet rsOrg = pstOrg.executeQuery();
//        if (rsOrg.next()) {
//            if (rsOrg.getString(1).equals("3")) {
//
//                if (dat.equals("m") || dat.equals("q")) {
//                    if (quart.equals("0")) {
//                        sql =
//                            " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id  and to_char(cm.p_date,'yyyy')=" +
//                            year +
//                            " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
//                            type + ") group by pro_name, pro_dose order by 1";
//                    }
//                    if (!quart.equals("0") && dat.equals("q")) {
//                        sql =
//                            " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q')=" +
//                            quart + " and to_char(cm.p_date,'yyyy')=" + year +
//                            " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
//                            type + ") group by pro_name, pro_dose order by 1";
//                    }
//                    if (!quart.equals("0") && dat.equals("m")) {
//                        sql =
//                            " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'mm')=" +
//                            quart + " and to_char(cm.p_date,'yyyy')=" + year +
//                            " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
//                            type + ") group by pro_name, pro_dose order by 1";
//                    }
//                }
//                if (dat.equals("u")) {
//                    sql =
//                        " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and  cm.p_date between to_date('" +
//                        fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
//                        "','mm/yyyy') and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
//                        type + ") group by pro_name, pro_dose order by 1";
//                }
//                if (dat.equals("hq")) {
//                    System.out.println("1st &&&&&&&&&&&&& 2nd half year");
//                    if (hq.equals("1")) {
//                        sql =
//                            " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('1','2') and to_char(ci.p_date,'yyyy')=" +
//                            year +
//                            " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
//                            type + ") group by pro_name, pro_dose order by 1";
//                    }
//                    if (hq.equals("2")) {
//                        sql =
//                            " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('3','4') and to_char(ci.p_date,'yyyy')=" +
//                            year +
//                            " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
//                            type + ") group by pro_name, pro_dose order by 1";
//                    }
//                    ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
//                }
//
//
//            } // type hierarchy is 3
//        } else {
//
//
//            if (dat.equals("m") || dat.equals("q")) {
//                if (quart.equals("0")) {
//                    sql =
//                        " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id  and to_char(cm.p_date,'yyyy')=" +
//                        year +
//                        " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
//                        type + ") group by pro_name, pro_dose order by 1";
//                }
//                if (!quart.equals("0") && dat.equals("q")) {
//                    sql =
//                        " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q')=" +
//                        quart + " and to_char(cm.p_date,'yyyy')=" + year +
//                        " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
//                        type + ") group by pro_name, pro_dose order by 1";
//                }
//                if (!quart.equals("0") && dat.equals("m")) {
//                    sql =
//                        " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'mm')=" +
//                        quart + " and to_char(cm.p_date,'yyyy')=" + year +
//                        " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 ) and f.sup_code in (select fff.sup_code from facility fff where fac_type_id=" +
//                        type + "  ) group by pro_name, pro_dose order by 1";
//                }
//            }
//            if (dat.equals("u")) {
//                sql =
//                    " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and  cm.p_date between to_date('" +
//                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
//                    "','mm/yyyy') and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
//                    type + ") group by pro_name, pro_dose order by 1";
//            }
//            if (dat.equals("hq")) {
//                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
//                if (hq.equals("1")) {
//                    sql =
//                        " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('1','2') and to_char(ci.p_date,'yyyy')=" +
//                        year +
//                        " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
//                        type + ") group by pro_name, pro_dose order by 1";
//                }
//                if (hq.equals("2")) {
//                    sql =
//                        " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('3','4') and to_char(ci.p_date,'yyyy')=" +
//                        year +
//                        " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
//                        type + ") group by pro_name, pro_dose order by 1";
//                }
//                ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
//            }
//
//
//        }
//
//
//        try {
//            getDBConnection();
//            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            System.out.println(sql);
//            rs = stmt.executeQuery(sql);
//        } catch (Exception e) {
//            // TODO: Add catch code
//            e.printStackTrace();
//        }
//        return rs;
//    }


    public String getprodId(String pro) {
        String sql = "select prod_id from product where pro_name like '" + pro + "'";
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString(1);
        } catch (SQLException e) {
            System.out.println("failded");
            return "fail";
        }

    }


    public ResultSet getProgDisToUserSpecRep(String quart, String year, String dat, String fY, String tY, String fM,
                                             String tM, String type, String hq, String faces[]) throws SQLException {
        String face = "";
        String sql = "";
        for (int i = 0; i < faces.length; i++) {
            if (i == 0) {
                face = faces[i];
            } else {
                face += ", " + faces[i];
            }
        }
        String sqlOrg = "select type_hierarchy from fac_type where fac_type_id=" + type;
        getDBConnection();
        PreparedStatement pstOrg = conn.prepareStatement(sqlOrg);
        ResultSet rsOrg = pstOrg.executeQuery();
        if (rsOrg.next()) {
            if (rsOrg.getString(1).equals("3")) {

                if (dat.equals("m") || dat.equals("q")) {
                    if (quart.equals("0")) {
                        sql =
                            " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id  and to_char(cm.p_date,'yyyy')=" +
                            year +
                            " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
                            type + " and ff.facility_id in (" + face + ")) group by pro_name, pro_dose order by 1";
                    }
                    if (!quart.equals("0") && dat.equals("q")) {
                        sql =
                            " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q')=" +
                            quart + " and to_char(cm.p_date,'yyyy')=" + year +
                            " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
                            type + " and ff.facility_id in (" + face + ")) group by pro_name, pro_dose order by 1";
                    }
                    if (!quart.equals("0") && dat.equals("m")) {
                        sql =
                            " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'mm')=" +
                            quart + " and to_char(cm.p_date,'yyyy')=" + year +
                            " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
                            type + " and ff.facility_id in (" + face + ")) group by pro_name, pro_dose order by 1";
                    }


                }
                if (dat.equals("u")) {
                    sql =
                        " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and  cm.p_date between to_date('" +
                        fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                        "','mm/yyyy') and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
                        type + "  and ff.facility_id in (" + face + ")  ) group by pro_name, pro_dose order by 1";
                }


                if (dat.equals("hq")) {
                    System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                    if (hq.equals("1")) {
                        sql =
                            " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('1','2') and to_char(ci.p_date,'yyyy')=" +
                            year +
                            " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
                            type + "  and ff.facility_id in (" + face + ") ) group by pro_name, pro_dose order by 1";
                    }
                    if (hq.equals("2")) {
                        sql =
                            " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('3','4') and to_char(ci.p_date,'yyyy')=" +
                            year +
                            " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 and ff.fac_type_id=" +
                            type + "  and ff.facility_id in (" + face + ") ) group by pro_name, pro_dose order by 1";
                    }
                    ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
                }


            } // type hierarchy is 3
            else {


                for (int i = 0; i < faces.length; i++) {
                    if (i == 0) {
                        face = faces[i];
                    } else {
                        face += ", " + faces[i];
                    }
                }


                if (dat.equals("m") || dat.equals("q")) {
                    if (quart.equals("0")) {
                        sql =
                            " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id  and to_char(cm.p_date,'yyyy')=" +
                            year +
                            " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 ) and f.facility_id in (select fff.facility_id from facility fff where fff.sup_code in (" +
                            face + ")  ) group by pro_name, pro_dose order by 1";
                    }
                    if (!quart.equals("0") && dat.equals("q")) {
                        sql =
                            " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q')=" +
                            quart + " and to_char(cm.p_date,'yyyy')=" + year +
                            " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 ) and f.facility_id in (select fff.facility_id from facility fff where fff.sup_code in (" +
                            face + ")  ) group by pro_name, pro_dose order by 1";
                    }
                    if (!quart.equals("0") && dat.equals("m")) {
                        sql =
                            " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'mm')=" +
                            quart + " and to_char(cm.p_date,'yyyy')=" + year +
                            " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 ) and f.facility_id in (select fff.facility_id from facility fff where fff.sup_code in (" +
                            face + ")  ) group by pro_name, pro_dose order by 1";
                    }


                }
                if (dat.equals("u")) {
                    sql =
                        " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and  cm.p_date between to_date('" +
                        fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                        "','mm/yyyy') and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3) and f.facility_id in (select fff.facility_id from facility fff where fff.sup_code in (" +
                        face + ")  ) group by pro_name, pro_dose order by 1";


                }
                if (dat.equals("hq")) {
                    System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                    if (hq.equals("1")) {
                        sql =
                            " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('1','2') and to_char(ci.p_date,'yyyy')=" +
                            year +
                            " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 ) and f.facility_id in (select fff.facility_id from facility fff where fff.sup_code in (" +
                            face + ")  ) group by pro_name, pro_dose order by 1";
                    }
                    if (hq.equals("2")) {
                        sql =
                            " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('3','4') and to_char(ci.p_date,'yyyy')=" +
                            year +
                            " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 ) and f.facility_id in (select fff.facility_id from facility fff where fff.sup_code in (" +
                            face + ")  ) group by pro_name, pro_dose order by 1";
                    }
                    ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
                }


            }


        } else { // group types


            for (int i = 0; i < faces.length; i++) {
                if (i == 0) {
                    face = faces[i];
                } else {
                    face += ", " + faces[i];
                }
            }


            if (dat.equals("m") || dat.equals("q")) {
                if (quart.equals("0")) {
                    sql =
                        " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id  and to_char(cm.p_date,'yyyy')=" +
                        year +
                        " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 ) and f.facility_id in  (" +
                        face + ")   group by pro_name, pro_dose order by 1";
                }
                if (!quart.equals("0") && dat.equals("q")) {
                    sql =
                        " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q')=" +
                        quart + " and to_char(cm.p_date,'yyyy')=" + year +
                        " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 ) and f.facility_id in (" +
                        face + ")   group by pro_name, pro_dose order by 1";
                }
                if (!quart.equals("0") && dat.equals("m")) {
                    sql =
                        " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'mm')=" +
                        quart + " and to_char(cm.p_date,'yyyy')=" + year +
                        " and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3 ) and f.facility_id in   (" +
                        face + ")   group by pro_name, pro_dose order by 1";
                }
            }
            if (dat.equals("u")) {
                sql =
                    " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and  cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                    "','mm/yyyy') and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3) and f.facility_id in  (" +
                    face + ")   group by pro_name, pro_dose order by 1";

            }


            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year think");
                if (hq.equals("1")) {
                    sql =
                        " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('1','2') and to_char(ci.p_date,'yyyy')='" +
                        year +
                        "' and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3) and f.facility_id in  (" +
                        face + ")   group by pro_name, pro_dose order by 1";
                }
                if (hq.equals("2")) {
                    sql =
                        " select p.pro_name, p.pro_dose, nvl(sum(ci.issues),0) from ctf_item ci, product p, facility f, ctf_main cm  where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and  ci.prod_id = p.prod_id and to_char(cm.p_date,'Q') in ('3','4') and to_char(ci.p_date,'yyyy')='" +
                        year +
                        "' and f.facility_id in (select ff.facility_id from facility ff, fac_type fty where ff.fac_type_id=fty.fac_type_id(+) and fty.type_hierarchy=3) and f.facility_id in  (" +
                        face + ")   group by pro_name, pro_dose order by 1";
                }
                ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
            }


        }


        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    // select cm.facility_id, ffy.fac_name from ctf_item ci, ctf_main cm, facility ffy where ffy.facility_id=cm.facility_id and cm.ctf_main_id  = ci.ctf_main_id and ci.prod_id=3 and cm.facility_id in (select fy.facility_id  from facility fy, fac_type ftt  where fy.fac_type_id=ftt.fac_type_id and ftt.fac_type_id=6 and fy.sup_code=128) and to_char(ci.p_date,'mm/yyyy')='08/2012'

    public ResultSet getStockedOutFac(String mon, String year, String prod, String fac) {
        String sql =
            "select cm.facility_id, ffy.fac_name from ctf_item ci, ctf_main cm, facility ffy where ffy.facility_id=cm.facility_id and cm.ctf_main_id  = ci.ctf_main_id and ci.prod_id=" +
            prod +
            " and cm.facility_id in (select fy.facility_id  from facility fy, fac_type ftt  where fy.fac_type_id=ftt.fac_type_id and ftt.fac_type_id=6 and fy.sup_code=" +
            fac + ") and to_char(ci.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }

    // select  p.pro_name,  ci.open_bal,ci.receipts, ci.issues, ci.adjustments, ci.closing_bal, decode(avg_mnthly_cons, 0,0,closing_bal/AVG_MNTHLY_CONS)  from ctf_item ci, ctf_main cm, facility f, product p where ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.facility_id=134  and to_char(ci.p_date,'mm/yyyy')='08/2012';
    public ResultSet getDataEntryError(String mon, String year, String fac, String dat) {
        String sql = "";
        if (dat.equals("m") && !mon.equals("0")) {
            sql =
                "select  p.pro_name,  sum(ci.open_bal),sum(ci.receipts), sum(ci.issues), sum(ci.adjustments) , sum(ci.closing_bal) , sum(avg_mnthly_cons), sum(qty_required), sum(qty_received),sum(new_visits),sum(cont_visits), to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---'), adj.adj_type_id  from ctf_item ci, ctf_main cm, facility f, product p, adj_type adj where ci.adj_type_id=adj.adj_type_id(+) and ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.facility_id=" +
                fac + "  and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                "' group by to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---'),p.pro_name,  adj.adj_type_id";
        }
        if (dat.equals("q") && !mon.equals("0")) {
            sql =
                "select  p.pro_name,  sum(ci.open_bal),sum(ci.receipts), sum(ci.issues), decode( sum(adj.always_negative) ,1, sum(ci.adjustments * -1) , sum(ci.adjustments) ), sum(ci.closing_bal), sum(avg_mnthly_cons), sum(qty_required), sum(qty_received),sum(new_visits),sum(cont_visits), to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---')  from ctf_item ci, ctf_main cm, facility f, product p, adj_type adj where ci.adj_type_id=adj.adj_type_id(+) and ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.facility_id=" +
                fac + "  and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                "' group by to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---'),p.pro_name";
        }
        if (mon.equals("0")) {
            sql =
                "select  p.pro_name,  sum(ci.open_bal),sum(ci.receipts), sum(ci.issues), decode(sum(adj.always_negative),1,sum(ci.adjustments * -1),sum(ci.adjustments) ),  sum(ci.closing_bal), sum(avg_mnthly_cons), sum(qty_required), sum(qty_received),sum(new_visits),sum(cont_visits), to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---')  from ctf_item ci, ctf_main cm, facility f, product p, adj_type adj where ci.adj_type_id=adj.adj_type_id(+) and ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.facility_id=" +
                fac + "  and to_char(cm.p_date,'yyyy')='" + year +
                "' group by to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---'),p.pro_name";
        }

        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public boolean getDataEntryErrorCheck(String mon, String year, String fac, String dat) {
        String sql = "";
        boolean x = false;
        if (dat.equals("m") && !mon.equals("0")) {
            sql =
                "select  p.pro_name,  sum(ci.open_bal),sum(ci.receipts), sum(ci.issues),sum(ci.adjustments) , sum(ci.closing_bal) , sum(avg_mnthly_cons), sum(qty_required), sum(qty_received),sum(new_visits),sum(cont_visits), to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---'), adj.adj_type_id  from adj_type adj, ctf_item ci, ctf_main cm, facility f, product p where ci.adj_type_id=adj.adj_type_id(+) and ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.facility_id=" +
                fac + "  and cm.p_date = to_date('" + mon + "/" + year +
                "','mm/yyyy') group by to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---'),p.pro_name,  adj.adj_type_id";
        }
        if (dat.equals("q") && !mon.equals("0")) {
            sql =
                "select  p.pro_name,  sum(ci.open_bal),sum(ci.receipts), sum(ci.issues) , sum(ci.adjustments), sum(ci.closing_bal), sum(avg_mnthly_cons), sum(qty_required), sum(qty_received),sum(new_visits),sum(cont_visits), to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---')  from adj_type adj, ctf_item ci, ctf_main cm, facility f, product p where ci.adj_type_id=adj.adj_type_id(+) and ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.facility_id=" +
                fac + "  and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                "' group by to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---'),p.pro_name";
        }
        if (mon.equals("0")) {
            sql =
                "select  p.pro_name,  sum(ci.open_bal),sum(ci.receipts), sum(ci.issues), sum(ci.adjustments) ,  sum(ci.closing_bal), sum(avg_mnthly_cons), sum(qty_required), sum(qty_received),sum(new_visits),sum(cont_visits), to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---')  from adj_type adj, ctf_item ci, ctf_main cm, facility f, product p where ci.adj_type_id=adj.adj_type_id(+) and ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.facility_id=" +
                fac + "  and to_char(cm.p_date,'yyyy')='" + year +
                "' group by to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---'),p.pro_name";
        }

        try {
            int adjType = 0;
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            int lop = 0;
            while (rs.next()) {
                adjType = rs.getInt(17);
                if (adjType == 1 || adjType == 2) {
                    System.out.println("XXXXXXXXXX     ***********    X   X  X   **************     XXXXXXXXXXXXXXXXXXXXXX");
                    x = true;
                }


            }
            return x;

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return false;
        }

    }


    public ResultSet getDataEntryErrorAll(String mon, String year, String fac, String dat) {
        String sql = "";
        if (dat.equals("m") && !mon.equals("0")) {
            sql =
                "select  p.pro_name,  sum(ci.open_bal),sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), sum(avg_mnthly_cons), sum(qty_required), sum(qty_received),sum(new_visits),sum(cont_visits), to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---')  from ctf_item ci, ctf_main cm, facility f, product p, adj_type adj where ci.adj_type_id=adj.adj_type_id(+) and ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.facility_id=" +
                fac + "  and to_char(ci.p_date,'mm/yyyy')='" + mon + "/" + year +
                "' group by to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---'),p.pro_name";
        }
        if (dat.equals("q") && !mon.equals("0")) {
            sql =
                "select  p.pro_name,  sum(ci.open_bal),sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), sum(avg_mnthly_cons), sum(qty_required), sum(qty_received),sum(new_visits),sum(cont_visits), to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---')  from ctf_item ci, ctf_main cm, facility f, product p, adj_type adj where ci.adj_type_id=adj.adj_type_id(+) and ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.facility_id=" +
                fac + "  and to_char(ci.p_date,'yyyy')='" + year + "' and to_char(ci.p_date,'Q')='" + mon +
                "' group by to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---'),p.pro_name";
        }
        if (mon.equals("0")) {
            sql =
                "select  p.pro_name,  sum(ci.open_bal),sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), sum(avg_mnthly_cons), sum(qty_required), sum(qty_received),sum(new_visits),sum(cont_visits), to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---')  from ctf_item ci, ctf_main cm, facility f, product p, adj_type adj where ci.adj_type_id=adj.adj_type_id(+) and ci.prod_id = p.prod_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.facility_id=" +
                fac + "  and to_char(ci.p_date,'yyyy')='" + year +
                "' group by to_char(cm.de_date,'dd-mon-yyyy'), de_staff, to_char(lc_date,'dd-mon-yyyy'), lc_staff, nvl(adj.type_name,'---'),p.pro_name";
        }

        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getDirsLevel2() {
        String sql =
            "select f.facility_id, f.fac_name from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id and ft.type_hierarchy=2";
        ResultSet rs = null;
        PreparedStatement pst = null;
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    // select ft.fac_name, ft.fac_type_id ,(select count(*) from facility ff, fac_type fty, ctf_item ci,ctf_main cm where ff.fac_type_id = fty.fac_type_id(+) and ff.fac_type_id=ft.fac_type_id and  ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id = f.facility_id and ci.prod_id=2 ) from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id(+) group by ft.fac_name, ft.fac_type_id

    public ResultSet getProgDispensedRep(String quart, String year, String prod, String dat, String fY, String tY,
                                         String fM, String tM, String hq) {

        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                    prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')=" + year +
                    "  and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy,fac_type fff where fff.fac_type_id = fy.fac_type_id and fff.type_hierarchy=2) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                    prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'Q')=" +
                    quart +
                    " and ff.sup_code=f.facility_id) as bb, ft.type_hierarchy from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy,fac_type fff where fff.fac_type_id = fy.fac_type_id and fff.type_hierarchy =2 ) group by ft.fac_type_id, f.facility_id, f.fac_name, ft.type_hierarchy order by  ft.type_hierarchy";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                    prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'mm')=" + quart +
                    " and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy,fac_type fff where fff.fac_type_id = fy.fac_type_id and fff.type_hierarchy=2) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                prod + " and ci.issues > 0 and cm.p_date between to_date('" + fM + "/" + fY +
                "','mm/yyyy') and to_date('" + tM + "/" + tY +
                "','mm/yyyy')  and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy,fac_type fff where fff.fac_type_id = fy.fac_type_id and fff.type_hierarchy=2) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                    prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('1','2') and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy,fac_type fff where fff.fac_type_id = fy.fac_type_id and fff.type_hierarchy=2) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
            }
            if (hq.equals("2")) {
                sql =
                    "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                    prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('3','4') and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy,fac_type fff where fff.fac_type_id = fy.fac_type_id and fff.type_hierarchy=2) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getProgDispensedRepIntersect(String quart, String year, String prod[], String dat, String fY,
                                                  String tY, String fM, String tM, String hq) {

        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                    prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')=" + year +
                    "  and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy,fac_type fff where fff.fac_type_id = fy.fac_type_id and fff.type_hierarchy=2) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                    prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'Q')=" +
                    quart +
                    " and ff.sup_code=f.facility_id) as bb, ft.type_hierarchy from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy,fac_type fff where fff.fac_type_id = fy.fac_type_id and fff.type_hierarchy =2 ) group by ft.fac_type_id, f.facility_id, f.fac_name, ft.type_hierarchy order by  ft.type_hierarchy";
            }
            if (!quart.equals("0") && dat.equals("m")) {

                //sql = "select distinct( f.facility_id) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'mm')="+quart+" and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy,fac_type fff where fff.fac_type_id = fy.fac_type_id and fff.type_hierarchy=2)";
                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {
                        sql =
                            "select distinct( f.facility_id) ffff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                            prod[k] + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')=" + year +
                            " and to_char(cm.p_date,'mm')=" + quart +
                            " and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy,fac_type fff where fff.fac_type_id = fy.fac_type_id and fff.type_hierarchy=2)";
                    } else {
                        sql +=
                            " intersect " +
                            "select distinct( f.facility_id) ffff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                            prod[k] + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')=" + year +
                            " and to_char(cm.p_date,'mm')=" + quart +
                            " and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy,fac_type fff where fff.fac_type_id = fy.fac_type_id and fff.type_hierarchy=2)";
                    }
                }
                sql = "select count(ffff) from (" + sql + ")";


            }


        }
        if (dat.equals("u")) {
            sql =
                "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                prod + " and ci.issues > 0 and cm.p_date between to_date('" + fM + "/" + fY +
                "','mm/yyyy') and to_date('" + tM + "/" + tY +
                "','mm/yyyy')  and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy,fac_type fff where fff.fac_type_id = fy.fac_type_id and fff.type_hierarchy=2) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                    prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('1','2') and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy,fac_type fff where fff.fac_type_id = fy.fac_type_id and fff.type_hierarchy=2) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
            }
            if (hq.equals("2")) {
                sql =
                    "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                    prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('3','4') and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy,fac_type fff where fff.fac_type_id = fy.fac_type_id and fff.type_hierarchy=2) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getProgDispensedRep(String quart, String year, String prod, String dat, String fY, String tY,
                                         String fM, String tM, String type, String hq, String cen[]) {
        String faces = "";
        for (int f = 0; f < cen.length; f++) {
            if (f == 0) {
                faces = cen[f];
            } else {
                faces += ", " + cen[f];
            }
        }
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                    prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                    "'  and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id=" +
                    type + " and fy.facility_id in (" + faces +
                    ") ) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                    prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q')='" + quart +
                    "' and ff.sup_code=f.facility_id) as bb, ft.type_hierarchy from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id=" +
                    type + " and fy.facility_id in (" + faces +
                    ") ) group by ft.fac_type_id, f.facility_id, f.fac_name, ft.type_hierarchy order by  ft.type_hierarchy";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                    prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='" + quart +
                    "' and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id=" +
                    type + " and fy.facility_id in (" + faces +
                    ")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                //sql = "select count(distinct f.facility_id) from facility f, ctf_main cm where cm.facility_id=f.facility_id and f.sup_code=167 and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'mm')='"+quart+"'";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                prod + " and ci.issues > 0 and to_char(cm.p_date,'mm/yyyy') between '" + fM + "/" + fY + "' and '" +
                tM + "/" + tY +
                "'  and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id=" +
                type + " and fy.facility_id in (" + faces +
                ")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
        }


        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                    prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('1','2') and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id=" +
                    type + " and fy.facility_id in (" + faces +
                    ")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
            }
            if (hq.equals("2")) {
                sql =
                    "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id=" +
                    prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('3','4') and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id=" +
                    type + " and fy.facility_id in (" + faces +
                    ")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    ///////////-----------------------------------------------------------

    public int getProgDispensedRepGT(String quart, String year, String prod, String dat, String fY, String tY,
                                     String fM, String tM, String type, String hq, String cen[], String facSup) {
        String faces = "";
        for (int f = 0; f < cen.length; f++) {
            if (f == 0) {
                faces = cen[f];
            } else {
                faces += ", " + cen[f];
            }
        }
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"'  and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+") ) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                sql =
                    "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                    faces + ")  and ci.prod_id=" + prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                    "'  and f.sup_code=" + facSup;
            }
            if (!quart.equals("0") &&
                dat.equals("q")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q')='"+quart+"' and ff.sup_code=f.facility_id) as bb, ft.type_hierarchy from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+") ) group by ft.fac_type_id, f.facility_id, f.fac_name, ft.type_hierarchy order by  ft.type_hierarchy";
                sql =
                                      "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                                      faces + ")  and ci.prod_id=" + prod +
                                      " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                                      "' and to_char(cm.p_date,'Q')='" + quart + "' and f.sup_code=" + facSup;
            }
            if (!quart.equals("0") &&
                dat.equals("m")) {
                //    sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'mm')='"+quart+"' and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                sql =
                                      "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                                      faces + ")  and ci.prod_id=" + prod +
                                      " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                                      "' and to_char(cm.p_date,'mm')='" + quart + "' and f.sup_code=" + facSup;

            }
        }
        if (dat.equals("u")) {
            //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'mm/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"'  and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
            sql =
                "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                faces + ")  and ci.prod_id=" + prod + " and ci.issues > 0 and cm.p_date between to_date('" + fM + "/" +
                fY + "','MM/yyyy') and to_date('" + tM + "/" + tY + "','MM/yyyy')  and f.sup_code=" + facSup;
        }


        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('1','2') and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                sql =
                    "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                    faces + ")  and ci.prod_id=" + prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('1','2') and f.sup_code=" + facSup;
            }
            if (hq.equals("2")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('3','4') and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                sql =
                    "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                    faces + ")  and ci.prod_id=" + prod + " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('3','4') and f.sup_code=" + facSup;
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        //return rs;
    }


    //////////-------------------------------------------------------

    public int getProgDispensedRepGTIntersect(String quart, String year, String prod[], String dat, String fY,
                                              String tY, String fM, String tM, String type, String hq, String cen[],
                                              String facSup) {


        String faces = "";
        for (int f = 0; f < cen.length; f++) {
            if (f == 0) {
                faces = cen[f];
            } else {
                faces += ", " + cen[f];
            }
        }


        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"'  and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+") ) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                //  sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"'  and f.sup_code="+facSup;
                for (int kl = 0; kl < prod.length; kl++) {
                    if (kl == 0) {
                        sql =
                            "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[kl] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year + "'  and f.sup_code=" + facSup;
                    } else {
                        sql +=
                            " intersect " +
                            "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[kl] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year + "'  and f.sup_code=" + facSup;
                    }
                }
                sql = "select count(fff) from (" + sql + " )";

            }
            if (!quart.equals("0") && dat.equals("q")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q')='"+quart+"' and ff.sup_code=f.facility_id) as bb, ft.type_hierarchy from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+") ) group by ft.fac_type_id, f.facility_id, f.fac_name, ft.type_hierarchy order by  ft.type_hierarchy";
                //    sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q')='"+quart+"' and f.sup_code="+facSup;
                for (int kl = 0; kl < prod.length; kl++) {
                    if (kl == 0) {
                        sql =
                            "select distinct ( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[kl] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q')='" + quart + "' and f.sup_code=" + facSup;
                    } else {
                        sql +=
                            " intersect " +
                            "select distinct (f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[kl] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q')='" + quart + "' and f.sup_code=" + facSup;
                    }
                }
                sql = "select count(fff) from (" + sql + " )";


            }
            if (!quart.equals("0") && dat.equals("m")) {
                //    sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'mm')='"+quart+"' and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                //  sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'mm')='"+quart+"' and f.sup_code="+facSup;

                for (int kl = 0; kl < prod.length; kl++) {
                    System.out.println(" facility id is " + facSup + " and products length is " + prod.length +
                                       " products names are " + prod[kl]);
                    if (kl == 0) {
                        sql =
                            "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + (prod[kl]) +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'mm')='" + quart + "' and f.sup_code=" + facSup;
                        //  k++;
                    } else {
                        sql +=
                            " intersect " +
                            "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + (prod[kl]) +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'mm')='" + quart + "' and f.sup_code=" + facSup;
                        //k++;
                    }
                    //k--;
                }
                sql = "select count(fff) from (" + sql + " )";


            }
        }
        if (dat.equals("u")) {
            //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'mm/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"'  and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
            //sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and cm.p_date between to_date('"+fM+"/"+fY+"','MM/yyyy') and to_date('"+tM+"/"+tY+"','MM/yyyy')  and f.sup_code="+facSup;
            for (int k = 0; k < prod.length; k++) {
                if (k == 0) {
                    sql =
                        "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                        faces + ")  and ci.prod_id=" + prod[k] + " and ci.issues > 0 and cm.p_date between to_date('" +
                        fM + "/" + fY + "','MM/yyyy') and to_date('" + tM + "/" + tY + "','MM/yyyy')  and f.sup_code=" +
                        facSup;
                } else {
                    sql +=
                        " intersect " +
                        "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                        faces + ")  and ci.prod_id=" + prod[k] + " and ci.issues > 0 and cm.p_date between to_date('" +
                        fM + "/" + fY + "','MM/yyyy') and to_date('" + tM + "/" + tY + "','MM/yyyy')  and f.sup_code=" +
                        facSup;
                }
            }
            sql = "select count(fff) from (" + sql + " )";

        }


        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('1','2') and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                //sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q') in ('1','2') and f.sup_code="+facSup;
                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {
                        sql =
                            "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[k] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q') in ('1','2') and f.sup_code=" + facSup;
                    } else {
                        sql +=
                            "intersect " +
                            "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[k] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q') in ('1','2') and f.sup_code=" + facSup;
                    }
                }
                sql = "select count(fff) from (" + sql + " )";

            }
            if (hq.equals("2")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('3','4') and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                //sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q') in ('3','4') and f.sup_code="+facSup;

                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {
                        sql =
                            "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[k] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q') in ('3','4') and f.sup_code=" + facSup;
                    } else {
                        sql +=
                            "intersect " +
                            "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[k] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q') in ('3','4') and f.sup_code=" + facSup;
                    }
                }

                sql = "select count(fff) from (" + sql + " )";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        //return rs;
    }


    public ResultSet getFacProgDispensedRepGTIntersect(String quart, String year, String prod[], String dat, String fY,
                                                       String tY, String fM, String tM, String type, String hq,
                                                       String cen[], String facSup) {


        String faces = "";
        for (int f = 0; f < cen.length; f++) {
            if (f == 0) {
                faces = cen[f];
            } else {
                faces += ", " + cen[f];
            }
        }


        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"'  and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+") ) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                //  sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"'  and f.sup_code="+facSup;
                for (int kl = 0; kl < prod.length; kl++) {
                    if (kl == 0) {
                        sql =
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[kl] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year + "'  and f.sup_code=" + facSup;
                    } else {
                        sql +=
                            " intersect " +
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[kl] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year + "'  and f.sup_code=" + facSup;
                    }
                }
                // sql = "select count(fff) from ("+sql+" )";

            }
            if (!quart.equals("0") && dat.equals("q")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q')='"+quart+"' and ff.sup_code=f.facility_id) as bb, ft.type_hierarchy from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+") ) group by ft.fac_type_id, f.facility_id, f.fac_name, ft.type_hierarchy order by  ft.type_hierarchy";
                //    sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q')='"+quart+"' and f.sup_code="+facSup;
                for (int kl = 0; kl < prod.length; kl++) {
                    if (kl == 0) {
                        sql =
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[kl] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q')='" + quart + "' and f.sup_code=" + facSup;
                    } else {
                        sql +=
                            " intersect " +
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[kl] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q')='" + quart + "' and f.sup_code=" + facSup;
                    }
                }
                //sql = "select count(fff) from ("+sql+" )";


            }
            if (!quart.equals("0") && dat.equals("m")) {
                //    sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'mm')='"+quart+"' and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                //  sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'mm')='"+quart+"' and f.sup_code="+facSup;

                for (int kl = 0; kl < prod.length; kl++) {
                    System.out.println(" facility id is " + facSup + " and products length is " + prod.length +
                                       " products names are " + prod[kl]);
                    if (kl == 0) {
                        sql =
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + (prod[kl]) +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'mm')='" + quart + "' and f.sup_code=" + facSup;
                        //  k++;
                    } else {
                        sql +=
                            " intersect " +
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + (prod[kl]) +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'mm')='" + quart + "' and f.sup_code=" + facSup;
                        //k++;
                    }
                    //k--;
                }
                // sql = "select count(fff) from ("+sql+" )";


            }
        }
        if (dat.equals("u")) {
            //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'mm/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"'  and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
            //sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and cm.p_date between to_date('"+fM+"/"+fY+"','MM/yyyy') and to_date('"+tM+"/"+tY+"','MM/yyyy')  and f.sup_code="+facSup;
            for (int k = 0; k < prod.length; k++) {
                if (k == 0) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                        faces + ")  and ci.prod_id=" + prod[k] + " and ci.issues > 0 and cm.p_date between to_date('" +
                        fM + "/" + fY + "','MM/yyyy') and to_date('" + tM + "/" + tY + "','MM/yyyy')  and f.sup_code=" +
                        facSup;
                } else {
                    sql +=
                        " intersect " +
                        "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                        faces + ")  and ci.prod_id=" + prod[k] + " and ci.issues > 0 and cm.p_date between to_date('" +
                        fM + "/" + fY + "','MM/yyyy') and to_date('" + tM + "/" + tY + "','MM/yyyy')  and f.sup_code=" +
                        facSup;
                }
            }
            // sql = "select count(fff) from ("+sql+" )";

        }


        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('1','2') and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                //sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q') in ('1','2') and f.sup_code="+facSup;
                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {
                        sql =
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[k] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q') in ('1','2') and f.sup_code=" + facSup;
                    } else {
                        sql +=
                            "intersect " +
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[k] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q') in ('1','2') and f.sup_code=" + facSup;
                    }
                }
                // sql = "select count(fff) from ("+sql+" )";

            }
            if (hq.equals("2")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('3','4') and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                //sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q') in ('3','4') and f.sup_code="+facSup;

                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {
                        sql =
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[k] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q') in ('3','4') and f.sup_code=" + facSup;
                    } else {
                        sql +=
                            "intersect " +
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (" +
                            faces + ")  and ci.prod_id=" + prod[k] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q') in ('3','4') and f.sup_code=" + facSup;
                    }
                }

                //  sql = "select count(fff) from ("+sql+" )";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            // rs.next();
            //  return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            //  return 0;
        }
        return rs;
    }


//    public int getProgDispensedRepAllIntersect(String quart, String year, String prod[], String dat, String fY,
//                                               String tY, String fM, String tM, String type, String hq, String facSup) {
//
//        String sql = "";
//        for (int kl = 0; kl < prod.length; kl++) {
//            if (kl == 0) {
//                sql += UtilsClass.programProductDisepensedQuery(facSup, prod[kl]);
//            } else {
//                sql += " intersect " + UtilsClass.programProductDisepensedQuery(facSup, prod[kl]);
//            }
//        }
//        
//        if (dat.equals("m") || dat.equals("q")) {
//            if (quart.equals("0")) {
//                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'";
//            }
//            if (!quart.equals("0") && dat.equals("q")) {
//                sql += " and to_char(cm.p_date,'yyyy')='" + year +"'";
//                sql += " and to_char(cm.p_date,'Q')='" + quart + "' ";
//            }
//            if (!quart.equals("0") && dat.equals("m")) {
//                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'";
//                sql += " and to_char(cm.p_date,'mm')='" + quart + "'";
//            }
//        }
//        if (dat.equals("u")) {
//            for (int k = 0; k < prod.length; k++) {
//                if (k == 0) {
//                    sql =
//                        "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
//                        facSup + ")  and ci.prod_id=" + prod[k] + " and ci.issues > 0 and cm.p_date between to_date('" +
//                        fM + "/" + fY + "','MM/yyyy') and to_date('" + tM + "/" + tY + "','MM/yyyy') ";
//                } else {
//                    sql +=
//                        " intersect " +
//                        "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
//                        facSup + ")  and ci.prod_id=" + prod[k] + " and ci.issues > 0 and cm.p_date between to_date('" +
//                        fM + "/" + fY + "','MM/yyyy') and to_date('" + tM + "/" + tY + "','MM/yyyy')";
//                }
//            }
//            sql = "select count(fff) from (" + sql + " )";
//        }
//        if (dat.equals("hq")) {
//            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
//            if (hq.equals("1")) {
//                for (int k = 0; k < prod.length; k++) {
//                    if (k == 0) {
//                        sql =
//                            "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
//                            facSup + ")  and ci.prod_id=" + prod[k] +
//                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
//                            "' and to_char(cm.p_date,'Q') in ('1','2')";
//                    } else {
//                        sql +=
//                            "intersect " +
//                            "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
//                            facSup + ")  and ci.prod_id=" + prod[k] +
//                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
//                            "' and to_char(cm.p_date,'Q') in ('1','2')";
//                    }
//                }
//                sql = "select count(fff) from (" + sql + " )";
//
//            }
//            if (hq.equals("2")) {
//                for (int k = 0; k < prod.length; k++) {
//                    if (k == 0) {
//                        sql =
//                            "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
//                            facSup + ")  and ci.prod_id=" + prod[k] +
//                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
//                            "' and to_char(cm.p_date,'Q') in ('3','4')";
//                    } else {
//                        sql +=
//                            "intersect " +
//                            "select distinct( f.facility_id) fff from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
//                            facSup + ")  and ci.prod_id=" + prod[k] +
//                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
//                            "' and to_char(cm.p_date,'Q') in ('3','4')";
//                    }
//                }
//
//                sql = "select count(fff) from (" + sql + " )";
//            }
//        }
//        sql = "select count(fff) from (" + sql + " )";
//        try {
//            getDBConnection();
//            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            System.out.println(sql);
//            rs = stmt.executeQuery(sql);
//            rs.next();
//            return rs.getInt(1);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//        //return rs;
//    }

    public static List<FacilityTemplate> getDirectoratesList(String[] directorates) {
        return UtilsClass.getDirectoratesList(directorates);
    }
    
    public static List<FacilityTemplate> serviceStatisticsReportList(String prod, String mon, String year, String dat,
                                                                     String fY, String tY, String fM, String tM,
                                                                     String hq, String facilities[],String supplier, String type) {
        return UtilsClass.serviceStatisticsReportList(prod, mon, year, dat, fY, tY, fM, tM, hq, facilities, supplier, type);
                                                                     }
    public int getProgDispensedRepAllIntersect(String quart, String year, String prod[], String dat, String fY,
                                               String tY, String fM, String tM, String type, String hq, String facSup) {
        int result = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "";
        for (int kl = 0; kl < prod.length; kl++) {
             String innerClause = UtilsClass.innerClause(quart, year, dat, fY, tY, fM, tM, hq);
            if (kl == 0) {
                sql += UtilsClass.programProductDisepensedQuery(facSup, prod[kl]);
                sql += innerClause;
            } else {
                sql += " intersect " ;
                sql += UtilsClass.programProductDisepensedQuery(facSup, prod[kl]);
                sql += innerClause;
            }
         }
         sql = "select count(fff) from (" + sql + " )";
         System.out.println(sql);
        try {
            conn = UtilsClass.jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next())
            result = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                UtilsClass.closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result ;
     }
    
    public ResultSet getFacProgDispensedRepAllIntersect(String quart, String year, String prod[], String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq,
                                                        String facSup) {

        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"'  and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+") ) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                //  sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"'  and f.sup_code="+facSup;
                for (int kl = 0; kl < prod.length; kl++) {
                    if (kl == 0) {
                        sql =
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                            facSup + ")  and ci.prod_id=" + prod[kl] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year + "'";
                    } else {
                        sql +=
                            " intersect " +
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                            facSup + ")  and ci.prod_id=" + prod[kl] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year + "'  ";
                    }
                }
                // sql = "select count(fff) from ("+sql+" )";

            }
            if (!quart.equals("0") && dat.equals("q")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q')='"+quart+"' and ff.sup_code=f.facility_id) as bb, ft.type_hierarchy from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+") ) group by ft.fac_type_id, f.facility_id, f.fac_name, ft.type_hierarchy order by  ft.type_hierarchy";
                //    sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q')='"+quart+"' and f.sup_code="+facSup;
                for (int kl = 0; kl < prod.length; kl++) {
                    if (kl == 0) {
                        sql =
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                            facSup + ")  and ci.prod_id=" + prod[kl] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q')='" + quart + "' ";
                    } else {
                        sql +=
                            " intersect " +
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                            facSup + ")  and ci.prod_id=" + prod[kl] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q')='" + quart + "'";
                    }
                }
                //sql = "select count(fff) from ("+sql+" )";


            }
            if (!quart.equals("0") && dat.equals("m")) {
                //    sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'mm')='"+quart+"' and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                //  sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'mm')='"+quart+"' and f.sup_code="+facSup;

                for (int kl = 0; kl < prod.length; kl++) {
                    System.out.println(" facility id is " + facSup + " and products length is " + prod.length +
                                       " products names are " + prod[kl]);
                    if (kl == 0) {
                        sql =
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                            facSup + ")  and ci.prod_id=" + (prod[kl]) +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'mm')='" + quart + "'";
                        //  k++;
                    } else {
                        sql +=
                            " intersect " +
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                            facSup + ")  and ci.prod_id=" + (prod[kl]) +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'mm')='" + quart + "'";
                        //k++;
                    }
                    //k--;
                }
                // sql = "select count(fff) from ("+sql+" )";


            }
        }
        if (dat.equals("u")) {
            //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'mm/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"'  and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
            //sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and cm.p_date between to_date('"+fM+"/"+fY+"','MM/yyyy') and to_date('"+tM+"/"+tY+"','MM/yyyy')  and f.sup_code="+facSup;
            for (int k = 0; k < prod.length; k++) {
                if (k == 0) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                        facSup + ")  and ci.prod_id=" + prod[k] + " and ci.issues > 0 and cm.p_date between to_date('" +
                        fM + "/" + fY + "','MM/yyyy') and to_date('" + tM + "/" + tY + "','MM/yyyy') ";
                } else {
                    sql +=
                        " intersect " +
                        "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                        facSup + ")  and ci.prod_id=" + prod[k] + " and ci.issues > 0 and cm.p_date between to_date('" +
                        fM + "/" + fY + "','MM/yyyy') and to_date('" + tM + "/" + tY + "','MM/yyyy')";
                }
            }
            // sql = "select count(fff) from ("+sql+" )";

        }


        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('1','2') and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                //sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q') in ('1','2') and f.sup_code="+facSup;
                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {
                        sql =
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                            facSup + ")  and ci.prod_id=" + prod[k] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q') in ('1','2')";
                    } else {
                        sql +=
                            "intersect " +
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                            facSup + ")  and ci.prod_id=" + prod[k] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q') in ('1','2')";
                    }
                }
                // sql = "select count(fff) from ("+sql+" )";

            }
            if (hq.equals("2")) {
                //sql = "select  ft.fac_type_id,f.facility_id,f.fac_name, (select count(distinct ff.fac_name) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id(+) and ci.ctf_main_id=cm.ctf_main_id(+)  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('3','4') and ff.sup_code=f.facility_id) as bb from facility f , fac_type ft where f.fac_type_id = ft.fac_type_id(+) and f.facility_id in (select fy.facility_id from facility fy where fy.fac_type_id="+type+" and fy.facility_id in ("+faces+")) group by ft.fac_type_id, f.facility_id, f.fac_name order by fac_name";
                //sql = "select count(distinct f.facility_id) from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in  ("+faces+")  and ci.prod_id="+prod+" and ci.issues > 0 and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q') in ('3','4') and f.sup_code="+facSup;

                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {
                        sql =
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                            facSup + ")  and ci.prod_id=" + prod[k] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q') in ('3','4')";
                    } else {
                        sql +=
                            "intersect " +
                            "select distinct f.facility_id, f.fac_name from facility f, ctf_main cm,ctf_item ci where cm.ctf_main_id(+)=ci.ctf_main_id and cm.facility_id=f.facility_id  and cm.facility_id in (select ftf.facility_id from facility ftf where ftf.sup_code=" +
                            facSup + ")  and ci.prod_id=" + prod[k] +
                            " and ci.issues > 0 and to_char(cm.p_date,'yyyy')='" + year +
                            "' and to_char(cm.p_date,'Q') in ('3','4')";
                    }
                }

                // sql = "select count(fff) from ("+sql+" )";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            //rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            //  return 0;
        }
        return rs;

    }


    //
    public ResultSet getFacProdDispensedRep(String quart, String year, String prod, String dir, String dat, String fY,
                                            String tY, String fM, String tM, String type, String hq, String mains) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {


            if (quart.equals("0")) {
                sql =
                    "select  ff.facility_id, sum(ci.receipts), sum(ci.issues) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and prod_id in  (" +
                    prod + ") and to_char(cm.p_date,'yyyy')=" + year + " and ff.sup_code=" + dir +
                    " and ci.issues > 0 group by ff.facility_id order by 1";


            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select  ff.facility_id, sum(ci.receipts), sum(ci.issues) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and prod_id in (" +
                    prod + ") and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'Q')=" + quart +
                    " and ff.sup_code=" + dir + " and ci.issues > 0 and cm.ctf_main_id in (" + mains +
                    " )                        group by ff.facility_id order by 1";


            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select  ff.facility_id, sum(ci.receipts), sum(ci.issues) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'mm')=" + quart +
                    " and ff.sup_code=" + dir + " and ci.issues > 0 group by ff.facility_id order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select  ff.facility_id, sum(ci.receipts), sum(ci.issues) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy')  and ff.sup_code=" + dir + " and ci.issues > 0 group by ff.facility_id order by 1";


        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql =
                    "select  ff.facility_id, sum(ci.receipts), sum(ci.issues) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('1','2') and ff.sup_code=" + dir +
                    " and ci.issues > 0 group by ff.facility_id order by 1";
            } else {
                sql =
                    "select  ff.facility_id, sum(ci.receipts), sum(ci.issues) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('3','4') and ff.sup_code=" + dir +
                    " and ci.issues > 0 group by ff.facility_id order by 1";
            }

        }
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public String getFacProdDispensedRepMain(String quart, String year, String prod, String dir, String dat, String fY,
                                             String tY, String fM, String tM, String type, String hq, int len) {
        return UtilsClass.getFacProdDispensedRepMain(quart, year, prod, dir, dat, fY, tY, fM, tM, type, hq, len);
    }


    public int getFacProdDispensedRepCount(String quart, String year, String prod, String dir, String dat, String fY,
                                           String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        int sum = 0;
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select  ff.facility_id, sum(ci.receipts), sum(ci.issues),count(*) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year + " and ff.sup_code=" + dir +
                    " and ci.issues > 0 group by ff.facility_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select  ff.facility_id, sum(ci.receipts), sum(ci.issues),count(*) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'Q')=" + quart +
                    " and ff.sup_code=" + dir + " and ci.issues > 0 group by ff.facility_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select  ff.facility_id, sum(ci.receipts), sum(ci.issues),count(*) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'mm')=" + quart +
                    " and ff.sup_code=" + dir + " and ci.issues > 0 group by ff.facility_id order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select  ff.facility_id, sum(ci.receipts), sum(ci.issues),count(*) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy')  and ff.sup_code=" + dir + " and ci.issues > 0 group by ff.facility_id order by 1";


        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql =
                    "select  ff.facility_id, sum(ci.receipts), sum(ci.issues),count(*) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('1','2') and ff.sup_code=" + dir +
                    " and ci.issues > 0 group by ff.facility_id order by 1";
            } else {
                sql =
                    "select  ff.facility_id, sum(ci.receipts), sum(ci.issues),count(*) from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                    prod + " and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('3','4') and ff.sup_code=" + dir +
                    " and ci.issues > 0 group by ff.facility_id order by 1";
            }

        }
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                sum++;
            }
            return sum;
        } catch (Exception e) {
            // TODO: Add catch code

            e.printStackTrace();
            return 0;
        }

    }


    public int getFacProdDispensedRepCountIntersect(String quart, String year, String prod[], String dir, String dat,
                                                    String fY, String tY, String fM, String tM, String type,
                                                    String hq) {
        String sql = "";
        int sum = 0;
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                //sql = "select  distinct (ff.facility_id) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')="+year+" and ff.sup_code="+dir+" and ci.issues > 0 ";
                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {
                        sql =
                            "select  distinct (ff.facility_id) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                            prod[k] + " and to_char(cm.p_date,'yyyy')=" + year + " and ff.sup_code=" + dir +
                            " and ci.issues > 0 ";
                    } else {
                        sql +=
                            "intersect " +
                            "select  distinct (ff.facility_id) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                            prod[k] + " and to_char(cm.p_date,'yyyy')=" + year + " and ff.sup_code=" + dir +
                            " and ci.issues > 0 ";
                    }
                }
                sql = "select count(fff) from (" + sql + ")";


            }
            if (!quart.equals("0") && dat.equals("q")) {
                //sql = "select  distinct ( ff.facility_id) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q')="+quart+" and ff.sup_code="+dir+" and ci.issues > 0 ";
                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {
                        sql =
                            "select  distinct ( ff.facility_id) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                            prod[k] + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'Q')=" +
                            quart + " and ff.sup_code=" + dir + " and ci.issues > 0 ";
                    } else {
                        sql +=
                            "intersect " +
                            "select  distinct ( ff.facility_id) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                            prod[k] + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'Q')=" +
                            quart + " and ff.sup_code=" + dir + " and ci.issues > 0 ";
                    }
                }

                sql = "select count(fff) from (" + sql + ")";


            }
            if (!quart.equals("0") && dat.equals("m")) {
                //sql = "select distinct ( ff.facility_id ) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'mm')="+quart+" and ff.sup_code="+dir+" and ci.issues > 0 ";


                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {
                        sql =
                            "select distinct ( ff.facility_id ) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                            prod[k] + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'mm')=" +
                            quart + " and ff.sup_code=" + dir + " and ci.issues > 0 ";
                    } else {
                        sql +=
                            " intersect " +
                            "select distinct ( ff.facility_id ) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                            prod[k] + " and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'mm')=" +
                            quart + " and ff.sup_code=" + dir + " and ci.issues > 0 ";
                    }
                }

                sql = "select count(fff) from (" + sql + ")";
            }


        }
        if (dat.equals("u")) {
            // sql = "select distinct ( ff.facility_id ) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id="+prod+" and cm.p_date between to_date('"+fM+"/"+fY+"','mm/yyyy') and to_date('"+tM+"/"+tY+"','mm/yyyy')  and ff.sup_code="+dir+" and ci.issues > 0";

            for (int k = 0; k < prod.length; k++) {
                if (k == 0) {
                    sql =
                        "select distinct ( ff.facility_id ) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                        prod[k] + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" +
                        tM + "/" + tY + "','mm/yyyy')  and ff.sup_code=" + dir + " and ci.issues > 0";
                } else {
                    sql +=
                        "intersect " +
                        "select distinct ( ff.facility_id ) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                        prod[k] + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" +
                        tM + "/" + tY + "','mm/yyyy')  and ff.sup_code=" + dir + " and ci.issues > 0";
                }
            }


            sql = "select count(fff) from (" + sql + ")";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                // sql = "select distinct ( ff.facility_id ) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('1','2') and ff.sup_code="+dir+" and ci.issues > 0 ";
                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {
                        sql =
                            "select distinct ( ff.facility_id ) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                            prod[k] + " and to_char(cm.p_date,'yyyy')=" + year +
                            " and to_char(cm.p_date,'Q') in ('1','2') and ff.facility id in ( select ftf.facility_id from facility ftf where ftf.sup_code=" +
                            dir + ") and ci.issues > 0 ";
                    } else {
                        sql +=
                            " intersect " +
                            "select distinct ( ff.facility_id ) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                            prod[k] + " and to_char(cm.p_date,'yyyy')=" + year +
                            " and to_char(cm.p_date,'Q') in ('1','2') and ff.facility_id in ( select ftf.facility_id from facility ftf where ftf.sup_code=" +
                            dir + " and ci.issues > 0 ";
                    }
                }

                sql = "select count(fff) from (" + sql + ")";
            } else {
                //sql = "select distinct (  ff.facility_id ) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('3','4') and ff.sup_code="+dir+" and ci.issues > 0";
                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {
                        sql =
                            "select distinct (  ff.facility_id ) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                            prod[k] + " and to_char(cm.p_date,'yyyy')=" + year +
                            " and to_char(cm.p_date,'Q') in ('3','4') and ff.sup_code=" + dir + " and ci.issues > 0";
                    } else {
                        sql +=
                            "intersect " +
                            "select distinct (  ff.facility_id ) fff from facility ff, ctf_main cm, ctf_item ci where  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id=" +
                            prod[k] + " and to_char(cm.p_date,'yyyy')=" + year +
                            " and to_char(cm.p_date,'Q') in ('3','4') and ff.sup_code=" + dir + " and ci.issues > 0";
                    }
                }
                sql = "select count(fff) from (" + sql + ")";
            }

        }
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                sum++;
            }
            return sum;
        } catch (Exception e) {
            // TODO: Add catch code

            e.printStackTrace();
            return 0;
        }

    }


    public ResultSet getFacProdDispensedRephType(String quart, String year, String prod, String dir, String dat,
                                                 String fY, String tY, String fM, String tM, String type, String hq,
                                                 String faces[]) {
        System.out.println("sooooooooooooop");
        String sql = "";
        String fac = "";
        for (int i = 0; i < faces.length; i++) {
            if (i == 0) {
                fac = faces[i];
            } else {
                fac += ", " + faces[i];
            }
        }
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select  ff.facility_id, sum(ci.receipts), sum(ci.issues) from facility ff, ctf_main cm, ctf_item ci where ff.facility_id in (" +
                    fac +
                    ") and  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id in (" +
                    prod + ") and to_char(cm.p_date,'yyyy')=" + year + " and ff.sup_code=" + dir +
                    " and ci.issues > 0 group by ff.facility_id order by 1";


            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select  ff.facility_id, sum(ci.receipts), sum(ci.issues) from facility ff, ctf_main cm, ctf_item ci where ff.facility_id in (" +
                    fac +
                    ") and  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id in (" +
                    prod + ") and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'Q')=" + quart +
                    " and ff.sup_code=" + dir + " and ci.issues > 0 group by ff.facility_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select  ff.facility_id, sum(ci.receipts), sum(ci.issues) from facility ff, ctf_main cm, ctf_item ci where ff.facility_id in (" +
                    fac +
                    ") and  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id in (" +
                    prod + ") and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'mm')=" + quart +
                    " and ff.sup_code=" + dir + " and ci.issues > 0 group by ff.facility_id order by 1";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select  ff.facility_id, sum(ci.receipts), sum(ci.issues) from facility ff, ctf_main cm, ctf_item ci where ff.facility_id in (" +
                fac + ") and  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id in (" +
                prod + ") and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy')  and ff.sup_code=" + dir + " and ci.issues > 0 group by ff.facility_id order by 1";
        }


        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql =
                    "select  ff.facility_id, sum(ci.receipts), sum(ci.issues) from facility ff, ctf_main cm, ctf_item ci where ff.facility_id in (" +
                    fac +
                    ") and  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id in (" +
                    prod + ") and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('1','2') and ff.sup_code=" + dir +
                    " and ci.issues > 0 group by ff.facility_id order by 1";
            } else {
                sql =
                    "select  ff.facility_id, sum(ci.receipts), sum(ci.issues) from facility ff, ctf_main cm, ctf_item ci where ff.facility_id in (" +
                    fac +
                    ") and  cm.facility_id=ff.facility_id and ci.ctf_main_id=cm.ctf_main_id   and ci.prod_id in (" +
                    prod + ") and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('3','4') and ff.sup_code=" + dir +
                    " and ci.issues > 0 group by ff.facility_id order by 1";
            }

        }
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }

    //
    public int getFacilityTypeHierarchyLVL(String[]fac){
        String sql = "select ft.type_hierarchy  ";
            sql+= " from fac_type ft, facility f ";
            sql += " where ft.fac_type_id = f.fac_type_id ";
            sql += " and f.facility_id=" + fac[0];
        String sqlGroup = "select ft.type_hierarchy  ";
          sqlGroup+= " from fac_type ft, facility f ";
          sqlGroup += " where ft.fac_type_id = f.fac_type_id ";
          sqlGroup += " and fac_type_id in ";
          sqlGroup += " (select gof_facility_id from group_of_facilities ";
          sqlGroup+= " where gof_grp_id="+(Integer.parseInt(fac[0])-100)+")" ;
          sqlGroup += " order by 2 ";
          try {
           getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
              rs = stmt.executeQuery(sql);
              if(rs.next()){
                 return rs.getInt(1);
              }else{
                  System.out.println(sqlGroup);
                  getDBConnection();
                  stmt = conn.prepareStatement(sqlGroup);
                  rs = stmt.executeQuery(sqlGroup); 
                  return rs.getInt(1);
              }
          
       } catch (Exception e) {
           
            e.printStackTrace();
           return 0;
        }
//         System.out.println(sqlGroup);
//            if( jdbcTemplate.queryForInt(sql) > 0){
//                System.out.println(sql);
//            return jdbcTemplate.queryForInt(sql);
//            }else{
//                System.out.println(sqlGroup);
//                return jdbcTemplate.queryForInt(sqlGroup);
//            }

    }
    
    public ResultSet getProgAdjSummary(String prod, String mon, String year, String dat, String fY, String tY,
         String fM, String tM, String type, String hq, String cen[]) {
        
        String faces = "";
        for (int f = 0; f < cen.length; f++) {
            if (f == 0) {
                faces = cen[f];
            } else {
                faces += ", " + cen[f];
            }
        }
        
        String sql = "select  adj.type_name ,";
        sql+= " sum(ci.adjustments) as adjustments ";
        sql+= " from ctf_item ci, adj_type adj, ctf_main cm ";
        sql += " where cm.ctf_main_id=ci.ctf_main_id  ";
        sql += " and adj.adj_type_id = ci.adj_type_id ";
       // if(DBConnection.getTypeLvl(cen[0]).equals("2"))
        sql += " and prod_id=" + prod ;
        sql += getFacilitiesByLevelSTMT(getFacilityTypeHierarchyLVL(cen), cen, null);
        //sql+= " and cm.facility_id in (" + faces + ")";
        sql+= " and ci.adj_type_id is not null ";
        
        
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'mm/yyyy')='" +mon + "/" + year + "'";
                    
            }
            if (dat.equals("q") && !mon.equals("0")) {
                 sql +=" and to_char(cm.p_date,'yyyy')='" + year + "'";
                 sql += " and to_char(cm.p_date,'Q')='" + mon + "'";
                
            }
            if (mon.equals("0")) {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "'";
            }
        }

        if (dat.equals("u")) {
                sql += " and cm.p_date between ";
                sql+= " to_date('" +fM + "/" + fY + "','mm/yyyy') ";
                sql+= " and to_date('" + tM + "/" + tY + "','mm/yyyy') ";
            

        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
              sql += " and to_char(cm.p_date,'yyyy')='" +year + "'";
              sql+=  " and to_char(cm.p_date,'Q')in ('1','2') ";
   

            }
            if (hq.equals("2")) {
                sql =
                sql += " and to_char(cm.p_date,'yyyy')='" +year + "'";
                sql+=  " and to_char(cm.p_date,'Q')in ('3','4') ";

            }


        }
        sql += " group by adj.type_name";
      
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    //--------------------------------------------

    public ResultSet getProgAdjSummary(String prod, String mon, String year, String dat, String fY, String tY,
                                       String fM, String tM, String type, String hq) {
        String sql =                
                "select  adj.type_name , sum(ci.adjustments )  " +
                " from ctf_item ci, adj_type adj, ctf_main cm " +
                " where cm.ctf_main_id(+) = ci.ctf_main_id " +
                " and adj.adj_type_id = ci.adj_type_id " ;
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") &&
                !mon.equals("0")) {
                //sql = "select  adj.type_name ,case when sum(ci.adjustments) > 0 then decode(sum(adj.always_negative),1,sum(ci.adjustments * -1),sum(ci.adjustments) ) else sum(ci.adjustments) end   from ctf_item ci, adj_type adj, ctf_main cm where cm.ctf_main_id(+) = ci.ctf_main_id and adj.adj_type_id = ci.adj_type_id and to_char(cm.p_date,'mm/yyyy')='"+mon+"/"+year+"' and prod_id="+prod+" and ci.adj_type_id is not null group by adj.type_name" ;
                
                sql += " and to_char(cm.p_date,'mm/yyyy')= '" +mon + "/" + year + "' " ;
                
                
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql +=
                    " and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')='" + mon + "'";
                    
            }
            if (mon.equals("0")) {
                sql +=
                    "  and to_char(cm.p_date,'yyyy')='" +year + "'" ;
                    
            }
        }

        if (dat.equals("u")) {
            sql +=
                "  and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') " +
                " and to_date('" + tM + "/" + tY + "','mm/yyyy') ";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql +=
                    " and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')in ('1','2') ";

            }
            if (hq.equals("2")) {
                sql +=
                    " and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')in ('3','4') ";

            }


        }
        sql += " and prod_id=" + prod + " " +
        " and ci.adj_type_id is not null group by adj.type_name";
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    //    public int getProgAdjSummarySum(String prod, String mon, String year, String dat,String fY, String tY, String fM, String tM, String type, String hq,String adj){
    //        String sql = "";
    //        if(dat.equals("m") || dat.equals("q")){
    //        if(dat.equals("m") && !mon.equals("0")){
    //        //sql = "select  adj.type_name ,case when sum(ci.adjustments) > 0 then decode(sum(adj.always_negative),1,sum(ci.adjustments * -1),sum(ci.adjustments) ) else sum(ci.adjustments) end   from ctf_item ci, adj_type adj, ctf_main cm where cm.ctf_main_id(+) = ci.ctf_main_id and adj.adj_type_id = ci.adj_type_id and to_char(cm.p_date,'mm/yyyy')='"+mon+"/"+year+"' and prod_id="+prod+" and ci.adj_type_id is not null group by adj.type_name" ;
    //        sql = "select  adj.type_name ,case when ci.adjustments > 0 then  decode(adj.always_negative,1, sum(ci.adjustments )* -1 ,sum(ci.adjustments) )   else sum(ci.adjustments) end   from ctf_item ci, adj_type adj, ctf_main cm where cm.ctf_main_id(+) = ci.ctf_main_id and adj.adj_type_id = ci.adj_type_id and to_char(cm.p_date,'mm/yyyy')='"+mon+"/"+year+"' and prod_id="+prod+" and adj.adj_type_id="+adj+" and ci.adj_type_id is not null group by adj.type_name,ci.adjustments,adj.always_negative" ;
    //        }        if(dat.equals("q") && !mon.equals("0")){
    //        sql = "select  adj.type_name , case when ci.adjustments > 0 then  decode(adj.always_negative,1, sum(ci.adjustments )* -1 ,sum(ci.adjustments) )   else sum(ci.adjustments) end from ctf_item ci, adj_type adj, ctf_main cm where cm.ctf_main_id(+) = ci.ctf_main_id and adj.adj_type_id = ci.adj_type_id and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q')='"+mon+"' and prod_id="+prod+" and ci.adj_type_id is not null group by adj.type_name, adj.always_negative,ci.adjustments,adj.always_negative" ;
    //        }if(mon.equals("0")){
    //            sql = "select  adj.type_name ,case when ci.adjustments > 0 then  decode(adj.always_negative,1, sum(ci.adjustments )* -1 ,sum(ci.adjustments) )   else sum(ci.adjustments) end from ctf_item ci, adj_type adj , ctf_main cm where cm.ctf_main_id(+) = ci.ctf_main_id and adj.adj_type_id = ci.adj_type_id and to_char(cm.p_date,'yyyy')='"+year+"' and prod_id="+prod+" and ci.adj_type_id is not null group by adj.type_name, adj.always_negative,ci.adjustments,adj.always_negative" ;
    //        }
    //        }
    //
    //        if(dat.equals("u")){
    //                    sql = "select  adj.type_name , case when ci.adjustments > 0 then  decode(adj.always_negative,1, sum(ci.adjustments )* -1 ,sum(ci.adjustments) )   else sum(ci.adjustments) end from ctf_item ci, adj_type adj , ctf_main cm where cm.ctf_main_id(+) = ci.ctf_main_id and adj.adj_type_id = ci.adj_type_id and to_char(cm.p_date,'mm/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' and prod_id="+prod+" and ci.adj_type_id is not null group by adj.type_name, adj.always_negative,ci.adjustments,adj.always_negative" ;
    //
    //                } if(dat.equals("hq")){
    //                      System.out.println("1st &&&&&&&&&&&&& 2nd half year");
    //                      if(hq.equals("1")){
    //                              sql = "select  adj.type_name , case when ci.adjustments > 0 then  decode(adj.always_negative,1, sum(ci.adjustments )* -1 ,sum(ci.adjustments) )   else sum(ci.adjustments) end from ctf_item ci, adj_type adj , ctf_main cm where cm.ctf_main_id(+) = ci.ctf_main_id and adj.adj_type_id = ci.adj_type_id and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q')in ('1','2') and prod_id="+prod+" and ci.adj_type_id is not null group by adj.type_name, adj.always_negative,ci.adjustments,adj.always_negative" ;
    //
    //                          }if(hq.equals("2")){
    //                              sql = "select  adj.type_name , case when ci.adjustments > 0 then  decode(adj.always_negative,1, sum(ci.adjustments )* -1 ,sum(ci.adjustments) )   else sum(ci.adjustments) end from ctf_item ci, adj_type adj , ctf_main cm where cm.ctf_main_id(+) = ci.ctf_main_id and adj.adj_type_id = ci.adj_type_id and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q')in ('3','4') and prod_id="+prod+" and ci.adj_type_id is not null group by adj.type_name, adj.always_negative,ci.adjustments,adj.always_negative" ;
    //
    //                          }
    //
    //
    //                }
    //
    //       try {
    //            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    //           System.out.println(sql);
    //           rs = stmt.executeQuery(sql);
    //        } catch (Exception e) {
    //            // TODO: Add catch code
    //            e.printStackTrace();
    //        }
    //       return rs;
    //    }


    //

    public ResultSet getAdjSummaryMain(String prod, String mon, String year, String dir, String dat, String fY,
                                       String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select distinct adj.adj_type_id from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                    dir + " ) and ci.prod_id=" + prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select distinct adj.adj_type_id from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                    dir + " ) and ci.prod_id=" + prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q')='" + mon + "'";
            }
            if (mon.equals("0")) {
                sql =
                    "select distinct adj.adj_type_id from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                    dir + " ) and ci.prod_id=" + prod + " and to_char(cm.p_date,'yyyy')='" + year + "' ";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select distinct adj.adj_type_id from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                dir + " ) and ci.prod_id=" + prod + " and cm.p_date between to_date('" + fM + "/" + fY +
                "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')   ";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select distinct adj.adj_type_id from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                    dir + " ) and ci.prod_id=" + prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('1','2')";
            }
            if (hq.equals("2")) {
                sql =
                    "select distinct adj.adj_type_id from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                    dir + " ) and ci.prod_id=" + prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('3','4')";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }
    
    public List<Map<String,Object>> getAdjSummaryMainList(String prod, String mon, String year, String dir, String dat, String fY,
                                       String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select distinct adj.adj_type_id from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                    dir + " ) and ci.prod_id=" + prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select distinct adj.adj_type_id from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                    dir + " ) and ci.prod_id=" + prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q')='" + mon + "'";
            }
            if (mon.equals("0")) {
                sql =
                    "select distinct adj.adj_type_id from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                    dir + " ) and ci.prod_id=" + prod + " and to_char(cm.p_date,'yyyy')='" + year + "' ";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select distinct adj.adj_type_id from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                dir + " ) and ci.prod_id=" + prod + " and cm.p_date between to_date('" + fM + "/" + fY +
                "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')   ";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select distinct adj.adj_type_id from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                    dir + " ) and ci.prod_id=" + prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('1','2')";
            }
            if (hq.equals("2")) {
                sql =
                    "select distinct adj.adj_type_id from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                    dir + " ) and ci.prod_id=" + prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('3','4')";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

       List<Map<String,Object>> results = jdbcTemplate.queryForList(sql);
        return results;
    }
    

    public void closeRs(ResultSet inrs) {
        try {
            if (inrs != null)
                inrs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closePst(PreparedStatement inpst) {
        try {
            if (inpst != null)
                inpst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getFacilityMaster() {
       
        String sql = "select facility_id ";
            sql += " from facility f, fac_type ft ";
            sql += " where f.fac_type_id = ft.fac_type_id ";
        sql += " and ft.type_hierarchy = 1 ";
        return jdbcTemplate.queryForInt(sql);
    }

    public String getFacilitiesByLevelSTMT(int facilityLVL, String[] facilities, String aliasSQL) {
        aliasSQL = aliasSQL == null ? "" : aliasSQL + ".";
        String faciitySTMT = null;
        String mainFacilitySTMT = null;
        for (int f = 0; f < facilities.length; f++) {
            if (f == 0) {
                faciitySTMT = facilities[f];
            } else {
                faciitySTMT += ", " + facilities[f];
            }
        }
       

        String sql = "select facility_id from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id ";
        sql += " and ft.type_hierarchy = "+facilityLVL + " and facility_id in (" + faciitySTMT + ")";
        try {
            getDBConnection(); 
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            // int count = 0;
           
            
            if ( facilityLVL == 2) {
                System.out.println("jakob");
                mainFacilitySTMT = " and " + aliasSQL + "  facility_id in ";
                mainFacilitySTMT += " (select fff.facility_id from facility fff where fff.sup_code in (" + faciitySTMT + ") or facility_id  in (" + faciitySTMT + " ) )  ";
                //mainFacilitySTMT += "  ";
                 
               //` mainFacilitySTMT = " and " + aliasSQL + "sup_code in (" + faciitySTMT + ")  ";
            } else if ( facilityLVL == 3) {
                mainFacilitySTMT = " and " + aliasSQL + "facility_id in (" + faciitySTMT + ")  ";
            }

        } catch (Exception e) {
            System.out.println(mainFacilitySTMT);
            e.printStackTrace();
        } finally {

        }

        return mainFacilitySTMT;
    }

    //
    public List<Map<String,Object>> getAdjSummaryList(String prod, String mon, String year, String dir, String adj, String dat, String fY,
                                   String tY, String fM, String tM, String type, String hq) {
        String sql = "select f.fac_name, adj.type_name, ";
        sql += " case when ci.adjustments > 0 then  decode(adj.always_negative,1, sum(ci.adjustments )* -1 ,sum(ci.adjustments) )   else sum(ci.adjustments) end as adjustments ";
        sql += " , adj.adj_type_id ";
        sql += " from ctf_item ci, ctf_main cm, facility f, adj_type adj ";
        sql += " where adj.adj_type_id = ci.adj_type_id ";
        sql += " and f.facility_id=cm.facility_id ";
        sql += " and ci.ctf_main_id = cm.ctf_main_id ";
        sql += " and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +dir + " ) ";
        sql += " and adj.adj_type_id=" + adj ;
        sql += " and ci.prod_id=" + prod ;
        
        
          
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
        sql += " group by f.fac_name, adj.type_name, adj.adj_type_id,adj.always_negative,ci.adjustments";
       List <Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        return results;
    }


    public String getAdjSummaryCount(String prod, String mon, String year, String dir, String adj, int index,
                                     String dat, String fY, String tY, String fM, String tM, String type, String hq) {
        System.out.println("----------------" + dat);
        System.out.println("----------------" + mon);
        System.out.println("----------------" + year);
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("heloooooooooooo");
                sql =
                    "select adj.type_name, nvl(count(*),0),  case when ci.adjustments > 0 then  decode(adj.always_negative,1, sum(ci.adjustments )* -1 ,sum(ci.adjustments) )   else sum(ci.adjustments) end from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                    dir + " ) and ci.prod_id=" + prod + " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                    "' and adj.adj_type_id=" + adj + " group by adj.type_name,adj.always_negative,ci.adjustments";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select adj.type_name, nvl(count(*),0),  case when ci.adjustments > 0 then  decode(adj.always_negative,1, sum(ci.adjustments )* -1 ,sum(ci.adjustments) )   else sum(ci.adjustments) end from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                    dir + " ) and ci.prod_id=" + prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q')=" + mon + " and adj.adj_type_id=" + adj +
                    " group by adj.type_name,adj.always_negative,ci.adjustments";
            }
            if (mon.equals("0")) {
                sql =
                    "select adj.type_name, nvl(count(*),0),  case when ci.adjustments > 0 then  decode(adj.always_negative,1, sum(ci.adjustments )* -1 ,sum(ci.adjustments) )   else sum(ci.adjustments) end from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                    dir + " ) and ci.prod_id=" + prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and adj.adj_type_id=" + adj + " group by adj.type_name,adj.always_negative,ci.adjustments";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select adj.type_name, nvl(count(*),0),  case when ci.adjustments > 0 then  decode(adj.always_negative,1, sum(ci.adjustments )* -1 ,sum(ci.adjustments) )   else sum(ci.adjustments) end from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                dir + " ) and ci.prod_id=" + prod + " and cm.p_date between to_date('" + fM + "/" + fY +
                "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and adj.adj_type_id=" + adj +
                " group by adj.type_name,adj.always_negative,ci.adjustments";
        }


        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select adj.type_name,  case when ci.adjustments > 0 then  decode(adj.always_negative,1, sum(ci.adjustments )* -1 ,sum(ci.adjustments) )   else sum(ci.adjustments) end from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                    dir + " ) and ci.prod_id=" + prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('1','2') and adj.adj_type_id=" + adj +
                    " group by adj.type_name,adj.always_negative,ci.adjustments";
            }
            if (hq.equals("2")) {
                sql =
                    "select adj.type_name,  case when ci.adjustments > 0 then  decode(adj.always_negative,1, sum(ci.adjustments )* -1 ,sum(ci.adjustments) )   else sum(ci.adjustments) end from ctf_item ci, ctf_main cm, facility f, adj_type adj  where adj.adj_type_id = ci.adj_type_id and f.facility_id=cm.facility_id and ci.ctf_main_id = cm.ctf_main_id and cm.facility_id in ( select ff.facility_id from facility ff where ff.sup_code=" +
                    dir + " ) and ci.prod_id=" + prod + " and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('3','4') and adj.adj_type_id=" + adj +
                    " group by adj.type_name,adj.always_negative,ci.adjustments";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {

            System.out.println(sql + " xxxxxxxxxxxxxxx,,,,,,,,,,,,,,xxxxxxxxxxxxxx");
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            System.out.println(sql);
            rs.next();

            return rs.getString(index);


        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return "err";
        }

    }


    //
    
    public ResultSet getNonRepFacilityList(String mon, String year, String dat, String fY, String tY, String fM, String tM,
                                  String type, String hq) {
        String dateFromStr = null;
        String dateToStr = null;
        switch(dat){
        case "m":
            if(mon.equals("0")){
                dateFromStr = "01/"+year;
                dateToStr = "12/"+year;
            }else{
                dateFromStr = mon+"/"+year;
                dateToStr = mon+"/"+year;
            }
            break;
        case "q" :
            if(mon.equals("0")){
                dateFromStr = "01/"+year;
                dateToStr = "12/"+year;
            }else{
                switch(Integer.valueOf(mon)) {
                case 1 :
                dateFromStr = "01/"+year;
                dateToStr = "03/"+year;
                 break;   
                    case 2 :
                    dateFromStr = "04/"+year;
                    dateToStr = "06/"+year;
                     break;  
                    case 3 :
                    dateFromStr = "07/"+year;
                    dateToStr = "09/"+year;
                     break;  
                    case 4 :
                    dateFromStr = "10/"+year;
                    dateToStr = "12/"+year;
                     break;  
                }
            }
            break;
        case "u" :
            dateFromStr = fM+"/"+fY;
            dateToStr = tM+"/"+tY;
            break;
        }
            return null;
     }
    
    
    public ResultSet getNonRepFac(String mon, String year, String dat, String fY, String tY, String fM, String tM,
                                  String type, String hq) {
        System.out.println("hq value is     " + hq);
        String sql = "";
        String lockCondition = " and f.isLocked='Y' and cm.p_date between f.lock_start_date and f.lock_end_date ";
        String order = "  order by 2 ";
        System.out.println(" month is " + mon + "  year is        " + year + "  flag is    " + dat + " period is " +
                           hq);
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0") && type.equals("0")) {
                System.out.println("$$$$$$$$$ ok %%%%%%%%%%%%%%%");
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id ";
                sql += " from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'mm/yyyy')='" +
                    mon + "/" + year + "' and cm.facility_id  is not null and  f.active=1 ";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'mm/yyyy')='" +
                    mon + "/" + year + "' and cm.facility_id  is not null and  f.active=1 " + lockCondition;


            }
            if (dat.equals("q") && !mon.equals("0") && type.equals("0")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')='" + mon + "') and cm.facility_id and f.active=1  is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "') and cm.facility_id and f.active=1  is not null " + lockCondition;
            }
            if (mon.equals("0") && type.equals("0")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and to_char(cm.p_date,'yyyy')='" +
                    year + "' and cm.facility_id  is not null  ";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and to_char(cm.p_date,'yyyy')='" +
                    year + "' and cm.facility_id  is not null  " + lockCondition;
            }

            if (dat.equals("m") && !mon.equals("0") && !type.equals("0")) {
                System.out.println("$$$$$$$$$ ok here is work %%%%%%%%%%%%%%%");
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'mm/yyyy')='" +
                    mon + "/" + year + "' and cm.facility_id  is not null and f.fac_type_id=" + type +
                    " and  f.active=1 ";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'mm/yyyy')='" +
                    mon + "/" + year + "' and cm.facility_id  is not null and f.fac_type_id=" + type +
                    " and  f.active=1 " + lockCondition;


            }
            if (dat.equals("q") && !mon.equals("0") && !type.equals("0")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "') and cm.facility_id  is not null and f.fac_type_id=" + type + " and f.active=1  is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "') and cm.facility_id  is not null and f.fac_type_id=" + type + " and f.active=1  is not null" +
                    lockCondition;
            }
            if (mon.equals("0") && !type.equals("0")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.fac_type_id=" + type +
                    " and cm.facility_id is not null  and f.active=1  is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.fac_type_id=" + type +
                    " and cm.facility_id is not null  and f.active=1  is not null" + lockCondition;
            }


            //////// --------------------------
        }
        if (dat.equals("u") && type.equals("0")) {
            sql =
                "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci)  and cm.p_date between to_date('" +
                fM + "/" + fY + "','MM/yyyy') and to_date('" + tM + "/" + tY +
                "','MM/yyyy')  and f.active=1 and cm.facility_id  is not null ";
            sql +=
                " minus " +
                "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci)  and cm.p_date between to_date('" +
                fM + "/" + fY + "','MM/yyyy') and to_date('" + tM + "/" + tY +
                "','MM/yyyy')  and f.active=1 and cm.facility_id  is not null " + lockCondition;
        }
        if (dat.equals("hq") && type.equals("0")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')='" +
                    year + "'  and f.active=1 and cm.facility_id  is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')='" +
                    year + "'  and f.active=1 and cm.facility_id  is not null" + lockCondition;
            }
            if (hq.equals("2")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')='" +
                    year + "'  and f.active=1 and cm.facility_id  is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')='" +
                    year + "'  and f.active=1 and cm.facility_id  is not null" + lockCondition;
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        //-------------------
        if (dat.equals("u") && !type.equals("0")) {
            sql =
                "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and f.fac_type_id=" +
                type + " and f.active=1 and cm.facility_id  is not null";
            sql +=
                " minus " +
                "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and f.fac_type_id=" +
                type + " and f.active=1 and cm.facility_id  is not null" + lockCondition;
        }
        if (dat.equals("hq") && !type.equals("0")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.fac_type_id=" + type + " and f.active=1 and cm.facility_id  is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.fac_type_id=" + type + " and f.active=1 and cm.facility_id  is not null" +
                    lockCondition;
            }
            if (hq.equals("2")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and f.fac_type_id=" +
                    type + " and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')='" + year +
                    "'  and cm.facility_id  is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and f.fac_type_id=" +
                    type + " and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')='" + year +
                    "'  and cm.facility_id  is not null" + lockCondition;
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }


        sql += order;
        //---------------

        try {
            connection = DBUtil.getDataSource().getConnection();
            pst = connection.prepareStatement(sql);
            rs = pst.executeQuery();
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getNonRepFacType(String mon, String year, String dat, String fY, String tY, String fM, String tM,
                                      String type, String hq, String fac) {
        System.out.println("hq value is     " + hq);
        String sql = "";
        String lockCondition = " and cm.p_date between f.lock_start_date and f.lock_end_date ";
        String order = " order by 3,2";
        System.out.println(" month is " + mon + "  year is        " + year + "  flag is    " + dat + " period is " +
                           hq);
        if (dat.equals("m") || dat.equals("q")) {


            if (dat.equals("m") && !mon.equals("0") && !type.equals("0")) {
                System.out.println("$$$$$$$$$ ok ddddddddddddddd %%%%%%%%%%%%%%%");
                sql =
                    "select distinct f.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'mm/yyyy')='" +
                    mon + "/" + year + "' and cm.facility_id  is not null and f.facility_id in (" + fac +
                    ") and  f.active=1 ";
                sql +=
                    " minus " +
                    "select distinct f.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'mm/yyyy')='" +
                    mon + "/" + year + "' and cm.facility_id  is not null and f.facility_id in (" + fac +
                    ") and  f.active=1 and cm.p_date between f.lock_start_date and f.lock_end_date ";


            }
            if (dat.equals("q") && !mon.equals("0") && !type.equals("0")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' and cm.facility_id is not null and f.facility_id in (" + fac + ")  and f.active=1  ";

            }
            if (mon.equals("0") && !type.equals("0")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.facility_id in (" + fac + ")  and cm.facility_id and f.active=1  is not null";
            }


            //////// --------------------------
        }
        //-------------------
        if (dat.equals("u") && !type.equals("0")) {
            sql =
                "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and f.facility_id in (" +
                fac + ")  and f.active=1 and cm.facility_id  is not null";
        }


        if (dat.equals("hq") && !type.equals("0")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.facility_id in (" + fac + ")  and f.active=1 and cm.facility_id  is not null";
            }
            if (hq.equals("2")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and f.fac_type_id=" +
                    type + " and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')='" + year +
                    "'  and cm.facility_id  is not null";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }


        //---------------

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getNonRepFacTypeChilds(String mon, String year, String dat, String fY, String tY, String fM,
                                            String tM, String type, String hq, String fac) {
        System.out.println("hq value is     " + hq);
        String sql = "";
        String lockCondition = " and f.isLocked='Y' and cm.p_date between f.lock_start_date and f.lock_end_date ";
        String order = " order by 3,2";
        System.out.println(" month is " + mon + "  year is        " + year + "  flag is    " + dat + " period is " +
                           hq);
        if (dat.equals("m") || dat.equals("q")) {


            if (dat.equals("m") && !mon.equals("0") && !type.equals("0")) {
                System.out.println("$$$$$$$$$ ok %%%%%%%%%%%%%%%");
                sql =
                    "select distinct f.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'mm/yyyy')='" +
                    mon + "/" + year + "' and cm.facility_id  is not null and f.sup_code=" + fac + " and  f.active=1";
                sql +=
                    " minus " +
                    "select distinct f.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'mm/yyyy')='" +
                    mon + "/" + year + "' and cm.facility_id  is not null and f.sup_code=" + fac + " and  f.active=1 " +
                    lockCondition;


            }
            if (dat.equals("q") && !mon.equals("0") && !type.equals("0")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')='" + mon + "' and cm.facility_id is not null and f.sup_code=" +
                    fac + "  and f.active=1  ";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')='" + mon + "' and cm.facility_id is not null and f.sup_code=" +
                    fac + "  and f.active=1  " + lockCondition;
            }
            if (mon.equals("0") && !type.equals("0")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.sup_code=" + fac + "  and cm.facility_id and f.active=1  is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.sup_code=" + fac + "  and cm.facility_id and f.active=1  is not null" +
                    lockCondition;
            }


            //////// --------------------------
        }
        //-------------------
        if (dat.equals("u") && !type.equals("0")) {
            sql =
                "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and f.sup_code=" + fac +
                "  and f.active=1 and cm.facility_id  is not null";
            sql +=
                " minus " +
                "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and f.sup_code=" + fac +
                "  and f.active=1 and cm.facility_id  is not null" + lockCondition;
        }


        if (dat.equals("hq") && !type.equals("0")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.sup_code=" + fac + "  and f.active=1 and cm.facility_id  is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.sup_code=" + fac + "  and f.active=1 and cm.facility_id  is not null" +
                    lockCondition;
            }
            if (hq.equals("2")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and f.fac_type_id=" +
                    type + " and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')='" + year +
                    "'  and cm.facility_id  is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and f.fac_type_id=" +
                    type + " and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')='" + year +
                    "'  and cm.facility_id  is not null" + lockCondition;
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }


        sql += order;


        //---------------

        try {
            System.out.println("");
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public int getNonRepFacTypeChildsCount(String mon, String year, String dat, String fY, String tY, String fM,
                                           String tM, String type, String hq, String fac) {
        if (Integer.parseInt(mon) < 10) {
            mon = "0" + Integer.parseInt(mon);
        }
        System.out.println("hq value is     " + hq);
        String sql = "";
        String lockCondition = " and f.isLocked='Y' and cm.p_date between f.lock_start_date and f.lock_end_date ";
        String order = " order by 3,2";
        int count = 0;
        System.out.println(" month is " + mon + "  year is        " + year + "  flag is    " + dat + " period is " +
                           hq);
        if (dat.equals("m") || dat.equals("q")) {


            if (dat.equals("m") && !mon.equals("0") && !type.equals("0")) {
                System.out.println("$$$$$$$$$ ok %%%%%%%%%%%%%%%");
                sql =
                    "select distinct f.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'mm/yyyy')='" +
                    mon + "/" + year + "' and cm.facility_id  is not null and f.sup_code=" + fac + " and  f.active=1 ";
                sql +=
                    " minus " +
                    " select distinct f.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'mm/yyyy')='" +
                    mon + "/" + year + "' and cm.facility_id  is not null and f.sup_code=" + fac + " and  f.active=1 " +
                    lockCondition;


            }
            if (dat.equals("q") && !mon.equals("0") && !type.equals("0")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')='" + mon + "' and cm.facility_id is not null and f.sup_code=" +
                    fac + "  and f.active=1  ";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')='" + mon + "' and cm.facility_id is not null and f.sup_code=" +
                    fac + "  and f.active=1  " + lockCondition;
            }
            if (mon.equals("0") && !type.equals("0")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.sup_code=" + fac + "  and cm.facility_id and f.active=1  is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.sup_code=" + fac + "  and cm.facility_id and f.active=1  is not null" +
                    lockCondition;
            }


            //////// --------------------------
        }
        //-------------------
        if (dat.equals("u") && !type.equals("0")) {
            sql =
                "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and f.sup_code=" + fac +
                "  and f.active=1 and cm.facility_id  is not null";
            sql +=
                " minus " +
                "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and f.sup_code=" + fac +
                "  and f.active=1 and cm.facility_id  is not null" + lockCondition;
        }


        if (dat.equals("hq") && !type.equals("0")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.sup_code=" + fac + "  and f.active=1 and cm.facility_id  is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.sup_code=" + fac + "  and f.active=1 and cm.facility_id  is not null" +
                    lockCondition;
            }
            if (hq.equals("2")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and f.fac_type_id=" +
                    type + " and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')='" + year +
                    "'  and cm.facility_id  is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and f.fac_type_id=" +
                    type + " and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')='" + year +
                    "'  and cm.facility_id  is not null" + lockCondition;
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }


        sql += order;
        //---------------

        try {
            System.out.println("");
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(" Month is " + mon + " and hiiiiiiiiiiiiiiii and Suplier is " + fac);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                count++;
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return count;
    }


    public ResultSet getNonRepFacType(String mon, String year, String dat, String fY, String tY, String fM, String tM,
                                      String type, String hq, String[] faces) {
        System.out.println("hq value is     " + hq);
        String sql = "";
        String fac = "";
        String lockCondition = " and f.isLocked='Y' and cm.p_date between f.lock_start_date and f.lock_end_date ";
        String order = "  order by 3,2 ";
        for (int i = 0; i < faces.length; i++) {
            if (i == 0) {
                fac = faces[i];
            } else {
                fac += ", " + faces[i];
            }
        }
        System.out.println(" month is " + mon + "  year is        " + year + "  flag is    " + dat + " period is " +
                           hq);
        if (dat.equals("m") || dat.equals("q")) {


            if (dat.equals("m") && !mon.equals("0") && !type.equals("0")) {
                System.out.println("$$$$$$$$$ ok %%%%%%%%%%%%%%%");
                sql =
                    "select distinct f.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'mm/yyyy')='" +
                    mon + "/" + year + "' and cm.facility_id  is not null and f.facility_id in (" + fac +
                    ") and  f.active=1 ";
                sql +=
                    " minus " +
                    "select distinct f.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'mm/yyyy')='" +
                    mon + "/" + year + "' and cm.facility_id  is not null and f.facility_id in (" + fac +
                    ") and  f.active=1 " + lockCondition;


            }
            if (dat.equals("q") && !mon.equals("0") && !type.equals("0")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' and cm.facility_id is not null and f.facility_id in (" + fac + ")  and f.active=1  ";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' and cm.facility_id is not null and f.facility_id in (" + fac + ")  and f.active=1  " +
                    lockCondition;
            }
            if (mon.equals("0") && !type.equals("0")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.facility_id in (" + fac + ")  and f.active=1  and cm.facility_id is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.facility_id in (" + fac + ")   and f.active=1 and cm.facility_id  is not null " +
                    lockCondition;
            }


            //////// --------------------------
        }
        //-------------------
        if (dat.equals("u") && !type.equals("0")) {
            sql =
                "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and f.facility_id in (" +
                fac + ")  and f.active=1 and cm.facility_id  is not null";
            sql +=
                " minus " +
                "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and f.facility_id in (" +
                fac + ")  and f.active=1 and cm.facility_id  is not null" + lockCondition;
        }


        if (dat.equals("hq") && !type.equals("0")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.facility_id in (" + fac + ")  and f.active=1 and cm.facility_id  is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and to_char(cm.p_date,'Q') in ('1','2') and to_char(cm.p_date,'yyyy')='" +
                    year + "' and f.facility_id in (" + fac + ")  and f.active=1 and cm.facility_id  is not null" +
                    lockCondition;
            }
            if (hq.equals("2")) {
                sql =
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and f.fac_type_id=" +
                    type + " and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')='" + year +
                    "'  and cm.facility_id  is not null";
                sql +=
                    " minus " +
                    "select cm.facility_id, f.fac_city, f.fac_type_id from cm, facility f where   f.facility_id=cm.facility_id and cm.ctf_main_id not in (select nvl(ci.ctf_main_id,0) from ci) and f.active=1 and f.fac_type_id=" +
                    type + " and to_char(cm.p_date,'Q') in ('3','4') and to_char(cm.p_date,'yyyy')='" + year +
                    "'  and cm.facility_id  is not null" + lockCondition;
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }


        sql += order;

        //---------------

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    //

    double avg = 0.0;

    public double getAVGnonRepFac(int mon, String year, String dir, String prod, String dat, String fY, String tY,
                                  String fM, String tM, String type, String hq, String pr) {

        String product = prod;
        String sql = "";

        String month = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (mon == 0) {
                sql =
                    " select nvl(sum(avg_mnthly_cons),0) ";
                sql += " from ctf_item ci, ctf_main cm, facility ff ";
                sql += " where ff.facility_id(+) = cm.facility_id ";
                sql += " and cm.ctf_main_id = ci.ctf_main_id(+) ";
                sql += " and ff.facility_id in ( ";
                sql += " select f.facility_id ";
                sql += " from facility f, fac_type ft ";
                sql += " where ft.fac_type_id = f.fac_type_id ";
                sql += " and ft.type_hierarchy=3 and f.sup_code=" +
                    dir + ") and to_char(cm.p_date,'yyyy')='" + year + "'";
                sql += " and ci.prod_id=" + prod;
            }

            if (mon != 0 && dat.equals("m")) {
                System.out.println("The month is " + mon);
                if (mon < 10) {
                    //if(month.length() == 1){
                    month = "0" + mon;
                    sql =
                        " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                        dir + ") and to_char(cm.p_date,'mm/yyyy')='" + month + "/" + year + "' and ci.prod_id=" + prod;
                } else {
                    month = "" + mon;
                    sql =
                        " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                        dir + ") and to_char(cm.p_date,'mm/yyyy')='" + month + "/" + year + "' and ci.prod_id=" + prod;
                }
            }
            if (mon != 0 && dat.equals("q")) {
                sql =
                    " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                    dir + ") and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' and ci.prod_id=" + prod;
            }
        }
        if (dat.equals("u")) {
            sql =
                " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                dir + ") and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') and ci.prod_id=" + prod;
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                    dir + ") and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod;
            }
            if (hq.equals("2")) {
                sql =
                    " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                    dir + ") and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod;
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }


        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("The count of facilities are " + sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                if (dir.equals("525")) {
                    avg = rs.getDouble(1);
                    if (rs.getDouble(1) == 0 || rs.getDouble(1) == 0.0) {
                        System.out.println("**** The AVG is zero");
                        if (mon == 1) {
                            mon = 12;
                            year = (Integer.parseInt(year) - 1) + "";
                        } else {
                            mon = mon - 1;
                        }
                        getAVGnonRepFac(mon, year, dir, 4 + "", dat, fY, tY, fM, tM, type, hq, pr);
                    } else {
                        return getAVGnonRepFacOrg(mon, year, dir, pr, dat, fY, tY, fM, tM, type, hq);
                    }
                } else {


                    if (rs.getDouble(1) == 0 || rs.getDouble(1) == 0.0) {
                        System.out.println("**** The AVG is zero");
                        if (mon == 1) {
                            mon = 12;
                            year = (Integer.parseInt(year) - 1) + "";
                        } else {
                            mon = mon - 1;
                        }
                        getAVGnonRepFac(mon, year, dir, 4 + "", dat, fY, tY, fM, tM, type, hq, pr);
                    } else {
                        return getAVGnonRepFacOrg(mon, year, dir, pr, dat, fY, tY, fM, tM, type, hq);
                    }
                }

                // return rs.getDouble(1);

            } else {
                return 0.0;
            }
            return rs.getDouble(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.113;
        }


    }


    public double getAVGnonRepFacOrg(int mon, String year, String dir, String prod, String dat, String fY, String tY,
                                     String fM, String tM, String type, String hq) {
        String sql = "";

        String month = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (mon == 0) {
                sql =
                    " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                    dir + ") and to_char(cm.p_date,'yyyy')='" + year + "' and ci.prod_id=" + prod;
            }
            if (mon != 0 && dat.equals("m")) {
                if (mon < 10) {
                    month = "0" + mon;
                    sql =
                        " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                        dir + ") and to_char(cm.p_date,'mm/yyyy')='0" + mon + "/" + year + "' and ci.prod_id=" + prod;

                } else {
                    month = "" + mon;
                    sql =
                        " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                        dir + ") and to_char(cm.p_date,'mm/yyyy')='" + month + "/" + year + "' and ci.prod_id=" + prod;
                }
            }
            if (mon != 0 && dat.equals("q")) {
                sql =
                    " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                    dir + ") and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' and ci.prod_id=" + prod;
            }
        }
        if (dat.equals("u")) {
            sql =
                " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                dir + ") and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') and ci.prod_id=" + prod;
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                    dir + ") and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod;
            }
            if (hq.equals("2")) {
                sql =
                    " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                    dir + ") and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod;
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }


        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                return 0.0;
            }


            //return avg;


        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.113;
        }


    }


    public double getAVGnonRepFac1(String mon, String year, String dir, String prod, String dat, String fY, String tY,
                                   String fM, String tM, String type, String hq) {

        if (Integer.parseInt(mon) < 10)
            mon = "0" + Integer.parseInt(mon);


        String sql = "";
        String sqlCountFac = "select count(*) from facility f where f.active=1 and f.sup_code=" + dir;
        String sqlMainCount =
            "select count(*) from facility f where f.active=1 and f.sup_code=" + dir + " and f.facility_id in ()";
        String k = "";
        // System.out.println("Facilityis at month "+mon +" and year "+year+" and counts "+getNonRepFacTypeChildsCount( mon,  year,  dat,  fY,  tY,  fM,  tM,  type,  hq,  dir)  );
        mon = Integer.parseInt(mon) + "";

        if (Integer.parseInt(mon) < 10)
            mon = "0" + Integer.parseInt(mon);


        while (getNonRepFacTypeChildsCount(mon, year, dat, fY, tY, fM, tM, type, hq, dir) 
               != 0) {
            if (Integer.parseInt(mon) < 10)
                mon = "0" + Integer.parseInt(mon);
            System.out.println(" xxxxx 2 - MAR - 2017 XXXXX");
            if (Integer.parseInt(mon) < 10 && Integer.parseInt(mon) != 1) {
                mon = "0" + (Integer.parseInt(mon) - 1);
            } else if (Integer.parseInt(mon) == 1) {
                mon = "12";
                year = (Integer.parseInt(year) - 1) + "";
            } else {
                mon = (Integer.parseInt(mon) - 1) + "";
            }
            k = (Integer.parseInt(mon) - 1) + "";
        }
        if (Integer.parseInt(mon) < 10) {
            mon = "0" + Integer.parseInt(mon);
        }
        try {
            if (dat.equals("m") || dat.equals("q")) {
                if (Integer.parseInt(mon) < 10) {
                    mon = "0" + Integer.parseInt(mon);
                }
                if (mon.equals("0")) {
                    sql =" select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                        dir + ") and to_char(cm.p_date,'yyyy')='" + year + "' and ci.prod_id=" + prod;
                }
                if (!mon.equals("0") && dat.equals("m")) {
                    System.out.println("xxxxxxxxxxxx abc xxxxxxxxxxxxxxxxxxxxxxxxxx");
                    // the modified at 14/11/2015
                    // sql = " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code="+dir+") and to_char(cm.p_date,'mm/yyyy')='"+(Integer.parseInt(mon) >= 10 ? Integer.parseInt(mon) : "0"+Integer.parseInt(mon))+"/"+year+"' and ci.prod_id="+prod ;
                    sql =
                        " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                        dir + ") and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "' and ci.prod_id=" + prod;
                    System.out.println("Thishere  " + sql);


                }
                if (!mon.equals("0") && dat.equals("q")) {
                    sql =
                        " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                        dir + ") and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                        "' and ci.prod_id=" + prod;
                }
            }
            if (dat.equals("u")) {
                sql =
                    " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                    dir + ") and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM +
                    "/" + tY + "','mm/yyyy') and ci.prod_id=" + prod;
            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    sql =
                        " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                        dir + ") and to_char(cm.p_date,'yyyy')='" + year +
                        "' and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod;
                }
                if (hq.equals("2")) {
                    sql =
                        " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                        dir + ") and to_char(cm.p_date,'yyyy')='" + year +
                        "' and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod;
                }
                ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
            }


            try {
                getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                System.out.println(sql);
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    return rs.getDouble(1);
                } else {
                    return 0.0;
                }
            } catch (Exception e) {
                // TODO: Add catch code
                e.printStackTrace();
                return 0.113;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return 0.0;

    }


    public double getAVGnonRepFacDivBy3(String mon, String year, String dir, String prod, String dat, String fY,
                                        String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        String month = "";
        String yr = "";
        String m = "";
        if (mon.equals("1")) {
            for (int k = 0; k < 3; k++) {

                if (Integer.parseInt(mon) < 10) {
                    m = "0" + (Integer.parseInt(mon) - 1);
                }
                if (k == 0) {
                    month = m;
                    yr = year;
                } else {
                    month += ", " + m;
                }

            }
            year = (Integer.parseInt(year) - 1) + "";
        }

        if (mon.equals("2")) {
            for (int k = 0; k < 3; k++) {

                if (Integer.parseInt(mon) < 10) {
                    m = "0" + (Integer.parseInt(mon) - 1);
                }
                if (k == 0) {
                    month = m;
                    yr = year;
                } else {
                    month += ", " + m;
                }

            }
            year = (Integer.parseInt(year) - 1) + "";
        }


        if (dat.equals("m") || dat.equals("q")) {
            if (mon.equals("0")) {
                sql =
                    " select nvl(sum(issues),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                    dir + ") and to_char(cm.p_date,'yyyy')='" + year + "' and ci.prod_id=" + prod;
            }
            if (!mon.equals("0") && dat.equals("m")) {
                sql =
                    " select nvl(sum(issues)/3,0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                    dir + ") and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'MM')=('" + year +
                    "','" + year + "','" + year + "') and ci.prod_id=" + prod;
            }
            if (!mon.equals("0") && dat.equals("q")) {
                sql =
                    " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                    dir + ") and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon +
                    "' and ci.prod_id=" + prod;
            }
        }
        if (dat.equals("u")) {
            sql =
                " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                dir + ") and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') and ci.prod_id=" + prod;
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                    dir + ") and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod;
            }
            if (hq.equals("2")) {
                sql =
                    " select nvl(sum(avg_mnthly_cons),0) from ctf_item ci, ctf_main cm, facility ff where ff.facility_id(+) = cm.facility_id and cm.ctf_main_id = ci.ctf_main_id(+) and ff.facility_id in (select f.facility_id from facility f, fac_type ft where ft.fac_type_id = f.fac_type_id and ft.type_hierarchy=3 and f.sup_code=" +
                    dir + ") and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod;
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }


        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.113;
        }


    }


    public ResultSet getBelowEmergencyOrderPoint(String mon, String year, String dir, String prod, String dat,
                                                 String fY, String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and ci.closing_bal <>0 and to_char(cm.p_date,'mm/yyyy')='" +
                    mon + "/" + year + "' and ci.prod_id=" + prod +
                    " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                    "  and (   decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) <= fac.min_mos) and ci.avg_mnthly_cons <> 0)";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and ci.closing_bal <>0 and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')='" + mon + "' and ci.prod_id=" + prod +
                    " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                    "  and (   decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) <= fac.min_mos) and ci.avg_mnthly_cons <> 0)";
            }
            if (mon.equals("0")) {
                sql =
                    "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id  and ci.closing_bal <>0 and to_char(cm.p_date,'yyyy')='" +
                    year + "' and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir +
                    "  and (   decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) <= fac.min_mos) and ci.avg_mnthly_cons <> 0)";
            }
        }


        if (dat.equals("u")) {
            sql =
                "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and ci.closing_bal <>0 and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and ci.prod_id=" + prod +
                " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                "  and (   decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) <= fac.min_mos) and ci.avg_mnthly_cons <> 0)";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and ci.closing_bal <>0 and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod +
                    " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                    "  and (   decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) <= fac.min_mos) and ci.avg_mnthly_cons <> 0)";
            }
            if (hq.equals("2")) {
                sql =
                    "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and ci.closing_bal <>0 and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod +
                    " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                    "  and (   decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) <= fac.min_mos) and ci.avg_mnthly_cons <> 0)";
            }
        }
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }

        return rs;
    }


    public ResultSet getOverStockedFacilities(String mon, String year, String dir, String prod, String dat, String fY,
                                              String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'mm/yyyy')='" +
                    mon + "/" + year + "' and ci.prod_id=" + prod +
                    " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                    "  and (   decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) > fac.max_mos) and ci.avg_mnthly_cons <> 0)";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql =
                    "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q')='" + mon + "' and ci.prod_id=" + prod +
                    " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                    "  and (   decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) > fac.max_mos) and ci.avg_mnthly_cons <> 0)";
            }
            if (mon.equals("0")) {
                sql =
                    "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'yyyy')='" +
                    year + "' and ci.prod_id=" + prod +
                    " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                    "  and (   decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) > fac.max_mos) and ci.avg_mnthly_cons <> 0)";
            }
        }
        if (dat.equals("u")) {
            sql =
                "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and ci.prod_id=" + prod +
                " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                "  and (   decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) > fac.max_mos) and ci.avg_mnthly_cons <> 0)";
        }


        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod +
                    " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                    "  and (   decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) > fac.max_mos) and ci.avg_mnthly_cons <> 0)";
            }
            if (hq.equals("2")) {
                sql =
                    "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'yyyy')='" +
                    year + "' and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod +
                    " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                    "  and (   decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) > fac.max_mos) and ci.avg_mnthly_cons <> 0)";
            }
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }

    public static String getTypeLvl(String facility) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection conn = null;
        String type  = null;
        String sql =
            "select typelvl("+facility+") from dual " ;
        try {
            conn = JDBCConfig.dataSource().getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            type = rs.getString(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        System.out.println(" typelvl    "+type);
        return type;

    }

    public ResultSet getStockedOutFacilities(String mon, String year, String dir, String prod, String dat, String fY,
                                             String tY, String fM, String tM, String type, String hq) throws SQLException {
        conn = getJNDIConnection();
        String sql = "";
        if (getFacHier(dir).equals("2")) {
            if (dat.equals("m") || dat.equals("q")) {
                if (dat.equals("m") && !mon.equals("0")) {
                    sql =
                        "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'mm/yyyy')='" +
                        mon + "/" + year + "' and ci.prod_id=" + prod +
                        " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0)";
                }
                if (dat.equals("q") && !mon.equals("0")) {
                    sql =
                        "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'yyyy')='" +
                        year + "' and to_char(cm.p_date,'Q')='" + mon + "' and ci.prod_id=" + prod +
                        " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0)";
                }
                if (mon.equals("0")) {
                    sql =
                        "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'yyyy')='" +
                        year + "' and ci.prod_id=" + prod +
                        " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0)";
                }
            }
            if (dat.equals("u")) {
                sql =
                    "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and ci.prod_id=" +
                    prod + " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0)";
            }


            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    sql =
                        "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'yyyy')='" +
                        year + "' and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod +
                        " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0)";
                }
                if (hq.equals("2")) {
                    sql =
                        "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'yyyy')='" +
                        year + "' and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod +
                        " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0)";
                }
            }
        }


        if (getFacHier(dir).equals("1")) {
            System.out.println("***************************   Mch   **************************");
            if (dat.equals("m") || dat.equals("q")) {
                if (dat.equals("m") && !mon.equals("0")) {
                    sql =
                        "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'mm/yyyy')='" +
                        mon + "/" + year + "' and ci.prod_id=" + prod +
                        " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 )";
                }
                if (dat.equals("q") && !mon.equals("0")) {
                    sql =
                        "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'yyyy')='" +
                        year + "' and to_char(cm.p_date,'Q')='" + mon + "' and ci.prod_id=" + prod +
                        " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 )";
                }
                if (mon.equals("0")) {
                    sql =
                        "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'yyyy')='" +
                        year + "' and ci.prod_id=" + prod +
                        " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.issues > 0)";
                }
            }
            if (dat.equals("u")) {
                sql =
                    "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and ci.prod_id=" +
                    prod + " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + "  and ci.closing_bal = 0)";
            }


            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    sql =
                        "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'yyyy')='" +
                        year + "' and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod +
                        " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0)";
                }
                if (hq.equals("2")) {
                    sql =
                        "select cm.facility_id, nvl(ci.avg_mnthly_cons,0),nvl(ci.closing_bal,0), decode(ci.avg_mnthly_cons , 0, 0,ci.closing_bal/ci.avg_mnthly_cons) from ctf_item ci, ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'yyyy')='" +
                        year + "' and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod +
                        " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 )";
                }
            }
        }
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }
    // select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(ci.p_date,'yyyy')='2012' and to_char(ci.p_date,'Q')='1' and ci.prod_id in (11,3,4,9,10) and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=128  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0) order by 2

    public ResultSet getStockedOutFacilitiesProducts(String quart, String year, String dir, String prod[], String dat,
                                                     String fY, String tY, String fM, String tM, String type,
                                                     String hq) {
        String sql = "";

        // double avg1 = db.getSupplyStatusRepSum(quart,year,dir,prod,dat,fY,tY,fM,type,hq);
        String products = "";
        for (int i = 0; i < prod.length; i++) {
            if (i == 0) {
                products = prod[i];
            } else {
                products += ", " + prod[i];
            }
        }


        if (getfacTypeHier(dir).equals("2")) {
            if (dat.equals("q") || dat.equals("m")) {
                if (dat.equals("q") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0) order by 2";
                }
                if (dat.equals("m") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0) order by 2";
                }
                if (quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + "  and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0) order by 2";
                }
            }
            if (dat.equals("u")) {
                sql =
                    "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id in (" +
                    products + ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0) order by 2";


            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0) order by 2";
                }
                if (hq.equals("2")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0) order by 2";
                }
            }
        }


        if (getfacTypeHier(dir).equals("1")) {


            if (dat.equals("q") || dat.equals("m")) {
                if (dat.equals("q") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 ) order by 2";
                }
                if (dat.equals("m") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 ) order by 2";
                }
                if (quart.equals("0")) {
                    sql =
                        "select  f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + "  and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 ) group by f.facility_id, f.fac_name  order by 2";
                }
            }
            if (dat.equals("u")) {
                sql =
                    "select  f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id in (" +
                    products + ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + "  and ci.closing_bal = 0) group by f.facility_id, f.fac_name order by 2";


            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    sql =
                        "select f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 ) group by f.facility_id, f.fac_name order by 2";
                }
                if (hq.equals("2")) {
                    sql =
                        "select f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 ) group by f.facility_id, f.fac_name  order by 2";
                }
            }
        }


        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            rs = stmt.executeQuery(sql);
            System.out.println("MONTHLYTTTTTTTTT--------------" + sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getStockedOutFacilitiesProductsIntersect(String quart, String year, String dir, String prod[],
                                                              String dat, String fY, String tY, String fM, String tM,
                                                              String type, String hq) {
        String sql = "";

        // double avg1 = db.getSupplyStatusRepSum(quart,year,dir,prod,dat,fY,tY,fM,type,hq);
        String products = "";
        for (int i = 0; i < prod.length; i++) {
            if (i == 0) {
                products = prod[i];
            } else {
                products += ", " + prod[i];
            }
        }


        if (getfacTypeHier(dir).equals("2")) {
            if (dat.equals("q") || dat.equals("m")) {
                if (dat.equals("q") && !quart.equals("0")) {


                    //sql = "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q')="+quart+" and ci.prod_id in ("+products+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+"  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0) order by 2" ;

                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {
                            sql =
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir +
                                "  and ci.closing_bal = 0  and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0)";
                        } else {
                            sql +=
                                " intersect " +
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir +
                                "  and ci.closing_bal = 0  and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0)";
                        }
                    }

                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0  and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0)";
                }
                if (dat.equals("m") && !quart.equals("0")) {
                    // sql = "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'mm')="+quart+" and ci.prod_id in ("+products+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+"  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0 and ci.adj_type_id <> 12) order by 2" ;
                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {
                            sql =
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir +
                                "  and ci.closing_bal = 0  and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0)";
                        } else {
                            sql +=
                                " intersect " +
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir +
                                "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0)";
                        }
                    }


                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0  and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0)";
                }
                if (quart.equals("0")) {
                    // sql = "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')="+year+"  and ci.prod_id in ("+products+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+"  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0  and ci.adj_type_id <> 12) order by 2" ;

                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {
                            sql =
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + "  and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir +
                                "  and ci.closing_bal = 0   and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0) ";
                        } else {
                            sql +=
                                " intersect " +
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + "  and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir +
                                "  and ci.closing_bal = 0  and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0)";
                        }
                    }


                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + "  and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0   and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0) ";

                }


            }
            if (dat.equals("u")) {
                //  sql = "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('"+fM+"/"+fY+"','mm/yyyy') and to_date('"+tM+"/"+tY+"','mm/yyyy')  and ci.prod_id in ("+products+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+"  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0 and ci.adj_type_id <> 12) order by 2" ;
                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {
                        sql =
                            "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('" +
                            fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                            "','mm/yyyy')  and ci.prod_id in (" + prod[k] +
                            ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                            dir +
                            "  and ci.closing_bal = 0  and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0)  ";
                    } else {
                        sql +=
                            " intersect " +
                            "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('" +
                            fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                            "','mm/yyyy')  and ci.prod_id in (" + prod[k] +
                            ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                            dir +
                            "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0)";
                    }
                }

                sql =
                    "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id in (" +
                    products + ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir +
                    "  and ci.closing_bal = 0  and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0)  ";


            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    //   sql = "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in ("+products+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+"  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0 and ci.adj_type_id <> 12) order by 2" ;
                    //                          for(int k=0; k<prod.length; k++){
                    //                              if(k==0){
                    //                                  sql = "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in ("+prod[k]+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+"  and ci.closing_bal = 0  and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0) " ;
                    //                              }else{
                    //                                  sql += " intersect " + "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in ("+prod[k]+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+"  and ci.closing_bal = 0  and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0) " ;
                    //                              }
                    //                          }
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0  and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0) ";
                }
                if (hq.equals("2")) {
                    // sql = "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in ("+products+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+"  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0 and ci.adj_type_id <> 12) order by 2" ;
                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {
                            sql =
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir +
                                "  and ci.avg_mnthly_cons <> 0 and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0)  ";
                        } else {
                            sql +=
                                " intersect " +
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir +
                                "   and ci.avg_mnthly_cons <> 0 and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0) ";
                        }
                    }

                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.avg_mnthly_cons <> 0 and nvl(ci.adj_type_id,0) <> 12 and nvl(ci.avg_mnthly_cons,'0') <> 0)  ";
                }
            }
        }


        if (getfacTypeHier(dir).equals("1")) {


            if (dat.equals("q") || dat.equals("m")) {
                if (dat.equals("q") && !quart.equals("0")) {
                    //sql = "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q')="+quart+" and ci.prod_id in ("+products+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+"  and ci.closing_bal = 0 and ci.adj_type_id <> 12 ) order by 2" ;
                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {
                            sql =
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir + "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12  )  ";
                        } else {
                            sql +=
                                " intersect " +
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir + "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12  ) ";
                        }
                    }
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12  )  ";
                }
                if (dat.equals("m") && !quart.equals("0")) {
                    // sql = "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'mm')="+quart+" and ci.prod_id in ("+products+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+"  and ci.closing_bal = 0 and ci.adj_type_id <> 12 ) order by 2" ;
                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {
                            sql =
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir + "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12  )  ";
                        } else {
                            sql +=
                                " intersect " +
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir + "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12  ) ";
                        }
                    }
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12  )  ";
                }
                if (quart.equals("0")) {
                    // sql = "select  f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')="+year+"  and ci.prod_id in ("+products+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+"  and ci.closing_bal = 0 and ci.adj_type_id <> 12 ) group by f.facility_id, f.fac_name  order by 2" ;
                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {
                            sql =
                                "select  f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + "  and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir +
                                "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12  ) group by f.facility_id, f.fac_name ";
                        } else {
                            sql +=
                                " intersect " +
                                "select  f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + "  and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir +
                                "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12  ) group by f.facility_id, f.fac_name ";
                        }


                        sql =
                            "select  f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                            year + "  and ci.prod_id in (" + products +
                            ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                            dir +
                            "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12  ) group by f.facility_id, f.fac_name ";
                    }
                }
            }
            if (dat.equals("u")) {
                //  sql = "select  f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('"+fM+"/"+fY+"','mm/yyyy') and to_date('"+tM+"/"+tY+"','mm/yyyy')  and ci.prod_id in ("+products+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+"  and ci.closing_bal = 0 and ci.adj_type_id <> 12) group by f.facility_id, f.fac_name order by 2" ;
                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {
                        sql =
                            "select  f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('" +
                            fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                            "','mm/yyyy')  and ci.prod_id in (" + prod[k] +
                            ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                            dir +
                            "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12  ) group by f.facility_id, f.fac_name ";
                    } else {
                        sql +=
                            " intersect " +
                            "select  f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('" +
                            fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                            "','mm/yyyy')  and ci.prod_id in (" + prod[k] +
                            ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                            dir +
                            "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0  ) <> 12) group by f.facility_id, f.fac_name ";
                    }
                }

                sql =
                    "select  f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id in (" +
                    products + ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir +
                    "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12  ) group by f.facility_id, f.fac_name ";


            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    //   sql = "select f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in ("+products+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+"  and ci.closing_bal = 0 and ci.adj_type_id <> 12 ) group by f.facility_id, f.fac_name order by 2" ;
                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {
                            sql =
                                "select f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir +
                                "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12 ) group by f.facility_id, f.fac_name ";
                        } else {
                            sql +=
                                " intersect " +
                                "select f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir +
                                "  and ci.closing_bal = 0 and ci.nvl(ci.adj_type_id,0) <> 12  ) group by f.facility_id, f.fac_name";
                        }
                    }
                    sql =
                        "select f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12 ) group by f.facility_id, f.fac_name ";
                }
                if (hq.equals("2")) {
                    //sql = "select f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in ("+products+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+"  and ci.closing_bal = 0 and ci.adj_type_id <> 12 ) group by f.facility_id, f.fac_name  order by 2" ;
                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {
                            sql =
                                "select f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir +
                                "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12  ) group by f.facility_id, f.fac_name  ";
                        } else {
                            sql +=
                                " intersect " +
                                "select f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir +
                                "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12 ) group by f.facility_id, f.fac_name  ";
                        }
                    }

                    sql =
                        "select f.facility_id, f.fac_name,count(*) from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and nvl(ci.adj_type_id,0) <> 12  ) group by f.facility_id, f.fac_name  ";
                }
            }
            System.out.println("sql directorate " + sql);
        }


        //if(prod.length==1){
        sql += " order by 2";
        //}


        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            System.out.println("MONTHLYTTTTTTTTT--------------" + sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getStockedOutFacilitiesProducts11(String quart, String year, String dir, String prod[], String dat,
                                                       String fY, String tY, String fM, String tM, String type,
                                                       String hq) {
        String sql = "";

        // double avg1 = db.getSupplyStatusRepSum(quart,year,dir,prod,dat,fY,tY,fM,type,hq);
        String products = "";
        for (int i = 0; i < prod.length; i++) {
            if (i == 0) {
                products = prod[i];
            } else {
                products += ", " + prod[i];
            }
        }


        if (getfacTypeHier(dir).equals("2")) {
            if (dat.equals("q") || dat.equals("m")) {
                if (dat.equals("q") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0) order by 2";
                }
                if (dat.equals("m") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0) order by 2";
                }
                if (quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + "  and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0) order by 2";
                }
            }
            if (dat.equals("u")) {
                sql =
                    "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id in (" +
                    products + ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0) order by 2";


            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0) order by 2";
                }
                if (hq.equals("2")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0) order by 2";
                }
            }
        }


        if (getfacTypeHier(dir).equals("1")) {


            if (dat.equals("q") || dat.equals("m")) {
                if (dat.equals("q") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 ) order by 2";
                }
                if (dat.equals("m") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 ) order by 2";
                }


                if (quart.equals("0")) {
                    sql =
                        "select  f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + "  and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 ) order by 2";

                    for (int pro = 0; pro < prod.length; pro++) {
                        for (int mon = 1; mon <= 12; mon++) {
                            sql =
                                "select  f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'mm/yyyy')='" +
                                mon + "/" + year + "'  and ci.prod_id =" + prod[pro] +
                                " and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir + "  and ci.closing_bal = 0 ) order by 2";
                            try {
                                getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                                System.out.println("        mmm         " + sql);
                                rs = stmt.executeQuery(sql);
                                if (!rs.next()) {
                                    return rs;
                                }
                            } catch (SQLException e) {
                            }
                        }
                    }


                }
            }
            if (dat.equals("u")) {
                sql =
                    "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id in (" +
                    products + ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + "  and ci.closing_bal = 0) order by 2";


            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 ) order by 2";
                }
                if (hq.equals("2")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        "  and ci.closing_bal = 0 ) order by 2";
                }
            }
        }


        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            rs = stmt.executeQuery(sql);
            System.out.println("MONTHLYTTTTTTTTT--------------" + sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public double getSupplyStatusRepSumP(String fac, String mon, String year, String prod, String dat, String fY,
                                         String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        double avg = 0;
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                    prod + ") and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name  order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {

                String q = "";
                if (mon.equals("1")) {
                    q = "to_char(cm.p_date,'mm')='03'";
                }
                if (mon.equals("2")) {
                    q = "to_char(cm.p_date,'mm')='06'";
                }
                if (mon.equals("3")) {
                    q = "to_char(cm.p_date,'mm')='09'";
                }
                if (mon.equals("4")) {
                    q = "to_char(cm.p_date,'mm')='12'";
                }


                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                    prod + ") and to_char(cm.p_date,'yyyy')='" + year + "' and " + q +
                    " and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }

            if (mon.equals("0")) {
                //sql = "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in ("+prod+") and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'mm')='12' and f.facility_id in (select ff.facility_id from facility ff where sup_code="+fac+") group by f.fac_name, ft.fac_name order by 1";


                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0),COUNT(*)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                    prod + ") and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='12' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name HAVING COUNT(*) <12 order by 1";


            }


        }
        if (dat.equals("u")) {
            sql =
                "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                prod + ") and to_char(cm.p_date,'mm/yyyy') = '" + tM + "/" + tY +
                "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                ") group by f.fac_name, ft.fac_name  order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                    prod + ") and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='06' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  f.fac_name, ft.fac_name,  nvl(sum(ci.receipts),0), nvl(sum(ci.issues),0), nvl(sum(ci.closing_bal),0), decode(nvl(sum(avg_mnthly_cons),0), 0,0,sum(closing_bal)/nvl(sum(AVG_MNTHLY_CONS),0)), nvl(sum(AVG_MNTHLY_CONS),0)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                    prod + ") and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='12' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("Sum " + sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                avg += rs.getDouble(7);
            }
            System.out.println(" avarage is **************" + avg);
            return avg;

        } catch (Exception e) {
            // TODO: Add catch code


            e.printStackTrace();
            return 0.010;
        }

    }


    public double getSupplyStatusRepSumByPoducts(String fac, String mon, String year, String prod, String dat,
                                                 String fY, String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        double avg = 0;
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql =
                    "select  sum(avg_mnthly_cons),f.fac_name, ft.fac_name  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                    prod + ") and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year +
                    "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name  order by 1";
            }
            if (dat.equals("q") && !mon.equals("0")) {

                String q = "";
                if (mon.equals("1")) {
                    q = "to_char(cm.p_date,'mm')='03'";
                }
                if (mon.equals("2")) {
                    q = "to_char(cm.p_date,'mm')='06'";
                }
                if (mon.equals("3")) {
                    q = "to_char(cm.p_date,'mm')='09'";
                }
                if (mon.equals("4")) {
                    q = "to_char(cm.p_date,'mm')='12'";
                }


                sql =
                    "select  sum(avg_mnthly_cons),f.fac_name, ft.fac_name from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                    prod + ") and to_char(cm.p_date,'yyyy')='" + year + "' and " + q +
                    " and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }

            if (mon.equals("0")) {
                ///sql = "select  sum(avg_mnthly_cons),f.fac_name, ft.fac_name,ci.prod_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in ("+prod+") and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'mm')='12' and f.facility_id in (select ff.facility_id from facility ff where sup_code="+fac+") group by f.fac_name, ft.fac_name,ci.prod_id order by 1";
                sql =
                    "select  sum(avg_mnthly_cons),f.fac_name, ft.fac_name,ci.prod_id,COUNT(*)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                    prod + ") and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='12' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") GROUP BY f.fac_name, ft.fac_name,ci.prod_id HAVING COUNT(*) < 12 order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select  sum(avg_mnthly_cons),f.fac_name, ft.fac_name  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                prod + ") and to_char(cm.p_date,'mm/yyyy') = '" + tM + "/" + tY +
                "' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" + fac +
                ") group by f.fac_name, ft.fac_name  order by 1";
        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select  sum(avg_mnthly_cons),f.fac_name, ft.fac_name  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                    prod + ") and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='06' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select  sum(avg_mnthly_cons),f.fac_name, ft.fac_name from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                    prod + ") and to_char(cm.p_date,'yyyy')='" + year +
                    "' and to_char(cm.p_date,'mm')='12' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                    fac + ") group by f.fac_name, ft.fac_name order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("Sum AVG " + sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                avg += rs.getDouble(1);
            }
            System.out.println(" avarage is **************" + avg);
            return avg;

        } catch (Exception e) {
            // TODO: Add catch code


            e.printStackTrace();
            return 0.010;
        }

    }


    public ResultSet getStockedOutFacilitiesProductsGT(String quart, String year, String dir, String prod[], String dat,
                                                       String fY, String tY, String fM, String tM, String type,
                                                       String hq, String fac[]) {
        String sql = "";
        String products = "";
        String faces = "";
        for (int i = 0; i < prod.length; i++) {
            if (i == 0) {
                products = prod[i];
            } else {
                products += ", " + prod[i];
            }
        }

        for (int i = 0; i < fac.length; i++) {
            if (i == 0) {
                faces = fac[i];
            } else {
                faces += ", " + fac[i];
            }
        }
        if (getFacHier(dir).equals("2")) {
            if (dat.equals("q") || dat.equals("m")) {
                if (dat.equals("q") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ")  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0 and  nvl(ci.adj_type_id,0) <> 12) order by 2";
                }
                if (dat.equals("m") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ")  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0 and  nvl(ci.adj_type_id,0) <> 12) order by 2";
                }
                if (quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + "  and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ") and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0) order by 2";
                }
            }
            if (dat.equals("u")) {
                sql =
                    "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id in (" +
                    products + ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + " and fac.facility_id in (" + faces +
                    ")  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0 and  nvl(ci.adj_type_id,0) <> 12) order by 2";


            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ")  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0 and  nvl(ci.adj_type_id,0) <> 12) order by 2";
                }
                if (hq.equals("2")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ")  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0 and  nvl(ci.adj_type_id,0) <> 12) order by 2";
                }
            }
        }


        if (getFacHier(dir).equals("1")) {
            if (dat.equals("q") || dat.equals("m")) {
                if (dat.equals("q") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ")  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0 and  nvl(ci.adj_type_id,0) <> 12 ) order by 2";
                }
                if (dat.equals("m") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ")  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0 and  nvl(ci.adj_type_id,0) <> 12 ) order by 2";
                }
                if (quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + "  and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ") and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0 and  nvl(ci.adj_type_id,0) <> 12 ) order by 2";
                }
            }
            if (dat.equals("u")) {
                sql =
                    "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id in (" +
                    products + ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + " and fac.facility_id in (" + faces +
                    ")  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0 and  nvl(ci.adj_type_id,0) <> 12 ) order by 2";


            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ")  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0 and  nvl(ci.adj_type_id,0) <> 12 ) order by 2";
                }
                if (hq.equals("2")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ")  and ci.closing_bal = 0 and ci.avg_mnthly_cons <> 0 and  nvl(ci.adj_type_id,0) <> 12 ) order by 2";
                }
            }
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            rs = stmt.executeQuery(sql);
            System.out.println(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getStockedOutFacilitiesProductsGTIntersect(String quart, String year, String dir, String prod[],
                                                                String dat, String fY, String tY, String fM, String tM,
                                                                String type, String hq, String fac[]) {
        String sql = "";
        String products = "";
        String faces = "";
        for (int i = 0; i < prod.length; i++) {
            if (i == 0) {
                products = prod[i];
            } else {
                products += ", " + prod[i];
            }
        }

        for (int i = 0; i < fac.length; i++) {
            if (i == 0) {
                faces = fac[i];
            } else {
                faces += ", " + fac[i];
            }
        }
        if (getFacHier(dir).equals("2")) {
            if (dat.equals("q") || dat.equals("m")) {
                if (dat.equals("q") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ")  and ci.closing_bal = 0 and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 ) order by 2";
                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {

                        } else {

                        }
                    }

                }
                if (dat.equals("m") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ")  and ci.closing_bal = 0  and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 ) order by 2";

                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {

                        } else {

                        }
                    }

                }
                if (quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + "  and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ") and ci.closing_bal = 0  and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 ) order by 2";


                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {

                        } else {

                        }
                    }

                }
            }
            if (dat.equals("u")) {
                sql =
                    "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id in (" +
                    products + ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + " and fac.facility_id in (" + faces +
                    ")  and ci.closing_bal = 0  and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 ) order by 2";

                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {

                    } else {

                    }
                }


            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ")  and ci.closing_bal = 0  and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 ) order by 2";

                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {

                        } else {

                        }
                    }

                }
                if (hq.equals("2")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ")  and ci.closing_bal = 0  and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 ) order by 2";

                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {

                        } else {

                        }
                    }

                }
            }
        }


        if (getFacHier(dir).equals("1")) {
            if (dat.equals("q") || dat.equals("m")) {
                if (dat.equals("q") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ")  and ci.closing_bal = 0  and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 ) order by 2";

                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {

                        } else {

                        }
                    }

                }
                if (dat.equals("m") && !quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ")  and ci.closing_bal = 0  and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 ) order by 2";

                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {

                        } else {

                        }
                    }

                }
                if (quart.equals("0")) {
                    sql =
                        "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                        year + "  and ci.prod_id in (" + products +
                        ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" + dir +
                        " and fac.facility_id in (" + faces +
                        ") and ci.closing_bal = 0  and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 ) order by 2";

                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {

                        } else {

                        }
                    }

                }
            }
            if (dat.equals("u")) {
                sql =
                    "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id in (" +
                    products + ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                    dir + " and fac.facility_id in (" + faces +
                    ")  and ci.closing_bal = 0  and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 ) order by 2";


                for (int k = 0; k < prod.length; k++) {
                    if (k == 0) {

                    } else {

                    }
                }


            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                if (hq.equals("1")) {
                    //sql = "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in ("+products+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+" and fac.facility_id in ("+faces+")  and ci.closing_bal = 0  and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 ) " ;

                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {
                            sql =
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir + " and fac.facility_id in (" + faces +
                                ")  and ci.closing_bal = 0  and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 ) ";
                        } else {
                            sql +=
                                " intersect " +
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir + " and fac.facility_id in (" + faces +
                                ")  and ci.closing_bal = 0  and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 ) ";
                        }
                    }

                }
                if (hq.equals("2")) {
                    // sql = "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in ("+products+") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code="+dir+" and fac.facility_id in ("+faces+")  and ci.closing_bal = 0  and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 )" ;

                    for (int k = 0; k < prod.length; k++) {
                        if (k == 0) {
                            sql =
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir + " and fac.facility_id in (" + faces +
                                ")  and ci.closing_bal = 0  and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 )";
                        } else {
                            sql +=
                                " intersect " +
                                "select distinct f.facility_id, f.fac_name from ctf_item ci, ctf_main cm, facility f where cm.facility_id = f.facility_id(+) and ci.ctf_main_id = cm.ctf_main_id(+) and to_char(cm.p_date,'yyyy')=" +
                                year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id in (" + prod[k] +
                                ") and cm.facility_id in (select fac.facility_id from facility fac where fac.sup_code=" +
                                dir + " and fac.facility_id in (" + faces +
                                ")  and ci.closing_bal = 0  and nvl(ci.avg_mnthly_cons,0) <> 0 and nvl(ci.adj_type_id,0) <> 12 )";
                        }
                    }

                }
            }
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            rs = stmt.executeQuery(sql);
            System.out.println(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public String getNonRepFacInfo(String fac, int index) {
        String sql =
            "select nvl(f.fac_name,' '), " +
            "nvl(fac_code,' '), " +
            "nvl(ft.fac_name,' '), " +
            "nvl(fac_contact,' '), " +
            "nvl(fac_phone,' '), " +
            "nvl(fac_fax,' '), " +
            "nvl(fac_city,' '), " +
            "nvl(fac_state,' ') " +
            "from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id and f.facility_id=" +
            fac;

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString(index);
            //  }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return " non ";
        }

    }
    //
    public String getBelowEmergencyOrderPointInfo(String fac, int index) {
        String sql =
            "select nvl(f.fac_name,' '), nvl(fac_code,' '), nvl(ft.fac_name,' '), nvl(fac_contact,' '), nvl(fac_phone,' '), nvl(fac_fax,' '), nvl(fac_city,' '), nvl(fac_state,' ') from facility f, fac_type ft where f.fac_type_id = ft.fac_type_id and f.facility_id=" +
            fac;

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString(index);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return " non ";
        }

    }

    public String type_hierarchyDir(String fac) {
        String sql =
            "select distinct(type_hierarchy) from facility f, fac_type ft where f.fac_type_id=ft.fac_type_id and facility_id=" +
            fac;

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return " non ";
        }

    }


    //


    public ResultSet getDispensedToUserRep(String quart, String year, String dir, String prod, String dat, String fY,
                                           String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues), f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name, f.facility_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues), f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name, f.facility_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues), f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name, f.facility_id order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select f.fac_name, ft.fac_name, sum(ci.issues), f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id=" + prod +
                " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                ") group by f.fac_name, ft.fac_name, f.facility_id order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) , f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name, f.facility_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) , f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name, f.facility_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getDispensedToUserRepGT(String quart, String year, String dir, String prod, String dat, String fY,
                                             String tY, String fM, String tM, String type, String hq, String faces[]) {
        String fac = "";
        for (int i = 0; i < faces.length; i++) {
            if (i == 0) {
                fac = faces[i];

            } else {
                fac += ", " + faces[i];
            }
        }
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues), f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and f.facility_id in (" + fac + ")  and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name, f.facility_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues), f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+)  and f.facility_id in (" +
                    fac + ") and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'Q')=" + quart +
                    " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name, f.facility_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues), f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+)  and f.facility_id in (" +
                    fac + ") and to_char(cm.p_date,'yyyy')=" + year + " and to_char(cm.p_date,'mm')=" + quart +
                    " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name, f.facility_id order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select f.fac_name, ft.fac_name, sum(ci.issues), f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+)  and f.facility_id in (" +
                fac + ") and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy')  and ci.prod_id=" + prod +
                " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                ") group by f.fac_name, ft.fac_name, f.facility_id order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) , f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+)  and f.facility_id in (" +
                    fac + ") and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name, f.facility_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) , f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+)  and f.facility_id in (" +
                    fac + ") and to_char(cm.p_date,'yyyy')=" + year +
                    " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name, f.facility_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public int getDispensedSum(String quart, String year, String dir, String prod, String dat, String fY, String tY,
                               String fM, String tM, String type, String hq) {
        int dispesed = 0;
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and  ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id=" + prod +
                " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                ") group by f.fac_name, ft.fac_name order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                dispesed += rs.getInt(3);
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return dispesed;
    }


    public int getDispensedSumGT(String quart, String year, String dir, String prod, String dat, String fY, String tY,
                                 String fM, String tM, String type, String hq, String[] faces) {
        String fac = "";
        for (int i = 0; i < faces.length; i++) {
            if (i == 0) {
                fac = faces[i];
            } else {
                fac += ", " + faces[i];
            }
        }
        int dispesed = 0;
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and  ci.ctf_main_id=cm.ctf_main_id(+) and f.facility_id in (" +
                    fac +
                    ") and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and f.facility_id in (" +
                    fac +
                    ") and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+)  and f.facility_id in (" +
                    fac +
                    ") and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+)  and f.facility_id in (" +
                fac +
                ") and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id=" + prod +
                " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                ") group by f.fac_name, ft.fac_name order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+)  and f.facility_id in (" +
                    fac +
                    ") and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+)  and f.facility_id in (" +
                    fac +
                    ") and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                dispesed += rs.getInt(3);
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return dispesed;
    }


    public double getDispensedSumD(String quart, String year, String dir, String prod, String dat, String fY, String tY,
                                   String fM, String tM, String type, String hq) {
        double dispesed = 0;
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and  ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id=" + prod +
                " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                ") group by f.fac_name, ft.fac_name order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") group by f.fac_name, ft.fac_name order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                dispesed += rs.getInt(3);
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return dispesed;
    }


    public double getDispensedToUserRepCount(String quart, String year, String dir, String prod, String dat, String fY,
                                             String tY, String fM, String tM, String type, String hq) {
        String sql = "";

        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") order by 1";
            }

            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id=" + prod +
                " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                ") order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }


        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.0;
        }

    }


    public double getDispensedToUserRepCountGP(String quart, String year, String dir, String prod, String dat,
                                               String fY, String tY, String fM, String tM, String type, String hq,
                                               String cen[]) {
        String sql = "";
        String faces = "";
        for (int i = 0; i < cen.length; i++) {
            if (i == 0) {
                faces = cen[i];
            } else {
                faces += ", " + cen[i];
            }

        }

        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where f.facility_id in (" +
                    faces +
                    ") and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where f.facility_id in (" +
                    faces +
                    ") and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") order by 1";
            }

            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where f.facility_id in (" +
                    faces +
                    ") and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where f.facility_id in (" +
                faces +
                ") and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id=" + prod +
                " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                ") order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where f.facility_id in (" +
                    faces +
                    ") and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where f.facility_id in (" +
                    faces +
                    ") and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.sup_code=" + dir +
                    ") order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }


        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("eww   " + sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.0;
        }

    }

    public double getDispensedToUserRepCountDir(String quart, String year, String prod, String dat, String fY,
                                                String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id=" + type +
                    ") order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id=" + type +
                    ") order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id=" + type +
                    ") order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and ci.prod_id=" + prod +
                " and f.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id=" + type +
                ") order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id=" + type +
                    ") order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id=" + type +
                    ") order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.0;
        }

    }


    public double getDispensedToUserRepCountDirGP(String quart, String year, String prod, String dat, String fY,
                                                  String tY, String fM, String tM, String type, String hq,
                                                  String[] cen) {
        String sql = "";
        String faces = "";
        for (int i = 0; i < cen.length; i++) {
            if (i == 0) {
                faces = cen[i];
            } else {
                faces += ", " + cen[i];
            }
        }
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where f.facility_id in (" +
                    faces +
                    ") and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and cm.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id=" +
                    (Integer.parseInt(type) - 100) + ") order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where f.facility_id in (" +
                    faces +
                    ") and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id=" +
                    (Integer.parseInt(type) - 100) + ") order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where f.facility_id in (" +
                    faces +
                    ") and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id=" +
                    (Integer.parseInt(type) - 100) + ") order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where f.facility_id in (" +
                faces +
                ") and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and ci.prod_id=" + prod +
                " and f.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id=" +
                (Integer.parseInt(type) - 100) + ") order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where f.facility_id in (" +
                    faces +
                    ") and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id=" +
                    (Integer.parseInt(type) - 100) + ") order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where f.facility_id in (" +
                    faces +
                    ") and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod +
                    " and f.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id=" +
                    (Integer.parseInt(type) - 100) + ") order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("toooot " + sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.0;
        }

    }


    public Double getDispensedToUserRepCountWholeCountry(String quart, String year, String prod, String dat, String fY,
                                                         String tY, String fM, String tM, String type, String hq) {

        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and ci.prod_id=" + prod + " order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id=" + prod + " order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id=" + prod + " order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id=" + prod +
                " order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod + " order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod + " order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }

    }

    public Double getDispensedToUserRepCountWholeCountryGP(String quart, String year, String prod, String dat,
                                                           String fY, String tY, String fM, String tM, String type,
                                                           String hq, String[] cen) {
        String face = "";
        for (int i = 0; i < cen.length; i++) {
            if (i == 0) {
                face = cen[i];
            } else {
                face += ", " + cen[i];
            }
        }

        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id in (" +
                    face +
                    ") and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and ci.prod_id=" + prod + " order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id in (" +
                    face +
                    ") and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id=" + prod + " order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id in (" +
                    face +
                    ") and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id=" + prod + " order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id in (" +
                face +
                ") and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id=" + prod +
                " order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id in (" +
                    face +
                    ") and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod + " order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id in (" +
                    face +
                    ") and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod + " order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }

    }


    public Double getDispensedToUserRepCountWholeCountryGP(String quart, String year, String prod, String dat,
                                                           String fY, String tY, String fM, String tM, String type,
                                                           String hq) {

        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=6 and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and ci.prod_id=" + prod + " order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=6 and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id=" + prod + " order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=6 and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id=" + prod + " order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=6 and f.fac_type_id=ft.fac_type_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and ci.prod_id=" + prod +
                " order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=6 and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('1','2') and ci.prod_id=" + prod + " order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select nvl(sum(ci.issues),0) from facility f, ctf_item ci , ctf_main cm, fac_type ft where ft.type_hierarchy=3 and ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=6 and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('3','4') and ci.prod_id=" + prod + " order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }

    }


    public ResultSet getDispensedToUserRepDirGT(String quart, String year, String dir, String prod, String dat,
                                                String fY, String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues), f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and ci.prod_id=" + prod + " and f.facility_id=" + dir +
                    " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues),f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id=" + prod + " and f.facility_id=" +
                    dir + " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues), f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id=" + prod + " and f.facility_id=" +
                    dir + " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select f.fac_name, ft.fac_name, sum(ci.issues),f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and ci.prod_id=" + prod +
                " and f.facility_id=" + dir + " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues),f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(ci.p_date,'yyyy')=" +
                    year + " and to_char(ci.p_date,'Q') in ('1','2') and ci.prod_id=" + prod + " and f.facility_id=" +
                    dir + " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues),f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(ci.p_date,'yyyy')=" +
                    year + " and to_char(ci.p_date,'Q') in ('3','4') and ci.prod_id=" + prod + " and f.facility_id=" +
                    dir + " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getDispensedToUserRepDir(String quart, String year, String dir, String prod, String dat, String fY,
                                              String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues), f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and ci.prod_id=" + prod + " and f.facility_id=" + dir +
                    " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues),f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart + " and ci.prod_id=" + prod + " and f.facility_id=" +
                    dir + " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues), f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'mm')=" + quart + " and ci.prod_id=" + prod + " and f.facility_id=" +
                    dir + " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select f.fac_name, ft.fac_name, sum(ci.issues),f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and ci.prod_id=" + prod +
                " and f.facility_id=" + dir + " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues),f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(ci.p_date,'yyyy')=" +
                    year + " and to_char(ci.p_date,'Q') in ('1','2') and ci.prod_id=" + prod + " and f.facility_id=" +
                    dir + " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues),f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(ci.p_date,'yyyy')=" +
                    year + " and to_char(ci.p_date,'Q') in ('3','4') and ci.prod_id=" + prod + " and f.facility_id=" +
                    dir + " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getDispensedToUserRepDirGT(String quart, String year, String dir, String prod, String dat,
                                                String fY, String tY, String fM, String tM, String type, String hq,
                                                String[] faces) {
        String fac = "";
        for (int i = 0; i < faces.length; i++) {
            if (i == 0) {
                fac = faces[i];
            } else {
                fac += ", " + faces[i];
            }
        }
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues), f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and f.facility_id in (" + fac + ") and ci.prod_id=" + prod + " and f.facility_id=" + dir +
                    " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues),f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and f.facility_id in (" + fac + ") and to_char(cm.p_date,'Q')=" + quart +
                    " and ci.prod_id=" + prod + " and f.facility_id=" + dir +
                    " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues), f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and f.facility_id in (" + fac + ") and to_char(cm.p_date,'mm')=" + quart +
                    " and ci.prod_id=" + prod + " and f.facility_id=" + dir +
                    " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                "select f.fac_name, ft.fac_name, sum(ci.issues),f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and f.facility_id in (" +
                fac + ") and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" +
                tY + "','mm/yyyy') and to_char(ci.p_date,'mm')=" + quart + " and ci.prod_id=" + prod +
                " and f.facility_id=" + dir + " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues),f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and f.facility_id in (" +
                    fac + ") and to_char(ci.p_date,'yyyy')=" + year +
                    " and to_char(ci.p_date,'Q') in ('1','2') and ci.prod_id=" + prod + " and f.facility_id=" + dir +
                    " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    "select f.fac_name, ft.fac_name, sum(ci.issues),f.facility_id from facility f, ctf_item ci , ctf_main cm, fac_type ft where ci.ctf_main_id=cm.ctf_main_id(+) and cm.facility_id=f.facility_id(+) and f.fac_type_id=ft.fac_type_id(+) and f.facility_id in (" +
                    fac + ") and to_char(ci.p_date,'yyyy')=" + year +
                    " and to_char(ci.p_date,'Q') in ('3','4') and ci.prod_id=" + prod + " and f.facility_id=" + dir +
                    " group by f.fac_name, ft.fac_name ,f.facility_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet getFacTypes() {
        String sql =
            "select f.facility_id, f.fac_name , f.fac_type_id from facility f where f.fac_type_id in (select ft.fac_type_id from fac_type ft where ft.type_hierarchy in (1,2)) order by 2";
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getFacTypesDetails(String dir) {
        String sql =
            "select nvl(facility_id,0), nvl(fac_name,' ') from facility where sup_code =" + dir + " order by 2";
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }


    //

    public ResultSet getFacDispensedToUserRep(String dir, String quart, String year, String dat, String fY, String tY,
                                              String fM, String tM, String type, String hq) {
        String sql = "";
        if (type_hierarchyDir(dir).equals("3")) {


            if (dat.equals("m") || dat.equals("q")) {
                if (quart.equals("0")) {
                    //sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')="+year+"  and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                    sql =
                        " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
                        year + "  and cm.facility_id in (" + dir + ")   group by p.pro_name, p.prod_id order by 1";
                }
                if (!quart.equals("0") &&
                    dat.equals("q")) {
                    //sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')="+year+" and to_char(ci.p_date,'Q')="+quart+" and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                    sql =
  " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
  year + " and to_char(cm.p_date,'Q')=" + quart + " and cm.facility_id in (" + dir +
  ")   group by p.pro_name, p.prod_id order by 1";
                }
                if (!quart.equals("0") &&
                    dat.equals("m")) {
                    //  sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')="+year+" and to_char(ci.p_date,'mm')="+quart+" and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                    sql =
  " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
  year + " and to_char(cm.p_date,'mm')=" + quart + " and cm.facility_id in (" + dir +
  ")   group by p.pro_name, p.prod_id order by 1";
                }


            }
            if (dat.equals("u")) {
                //sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'mm/yyyy') between '"+fM+"/"+fY+"' and '"+fM+"/"+tY+"'  and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                sql =
                    " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                    "','mm/yyyy')  and cm.facility_id in (" + dir + ")   group by p.pro_name, p.prod_id order by 1";


            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year doooooooooooo");
                if (hq.equals("1")) {
                    System.out.println("1st &&&&&&&&&&&&& 2nd half year doooooooooooooooo");
                    //sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')="+year+" and to_char(ci.p_date,'Q') in ('1','2') and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                    sql =
                        " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')='" +
                        year + "' and to_char(cm.p_date,'Q') in ('1','2') and cm.facility_id in (" + dir +
                        ")   group by p.pro_name, p.prod_id order by 1";
                }
                if (hq.equals("2")) {
                    //sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')="+year+" and to_char(ci.p_date,'Q') in ('3','4') and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                    sql =
                        " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')='" +
                        year + "' and to_char(cm.p_date,'Q') in ('3','4') and cm.facility_id in (" + dir +
                        ")   group by p.pro_name, p.prod_id order by 1";
                }
                ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
            }
        } ///////////////////////// for centers
        //////////////////////////////////////
        //////////////////////////////////////
        //////////// now for higher level /////////////////
        ///////////////////////////////////////////
        else {
            System.out.println("******************* This is A Directorate **************************");
            if (dat.equals("m") || dat.equals("q")) {
                if (quart.equals("0")) {
                    //sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')="+year+"  and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                    sql =
                        " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
                        year + "  and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                        ")   group by p.pro_name, p.prod_id order by 1";
                }
                if (!quart.equals("0") &&
                    dat.equals("q")) {
                    //sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')="+year+" and to_char(ci.p_date,'Q')="+quart+" and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                    sql =
  " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
  year + " and to_char(cm.p_date,'Q')=" + quart +
  " and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
  ")   group by p.pro_name, p.prod_id order by 1";
                }
                if (!quart.equals("0") &&
                    dat.equals("m")) {
                    //  sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')="+year+" and to_char(ci.p_date,'mm')="+quart+" and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                    sql =
  " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
  year + " and to_char(cm.p_date,'mm')=" + quart +
  " and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
  ")   group by p.pro_name, p.prod_id order by 1";
                }


            }
            if (dat.equals("u")) {
                //sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'mm/yyyy') between '"+fM+"/"+fY+"' and '"+fM+"/"+tY+"'  and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                sql =
                    " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                    "','mm/yyyy')  and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                    ")   group by p.pro_name, p.prod_id order by 1";


            }
            if (dat.equals("hq")) {
                System.out.println("1st &&&&&&&&&&&&& 2nd half year donnnnnnnnnnnnnne");
                if (hq.equals("1")) {
                    //sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')="+year+" and to_char(ci.p_date,'Q') in ('1','2') and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                    sql =
                        " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')='" +
                        year +
                        "' and to_char(cm.p_date,'Q') in ('1','2') and cm.facility_id in (select facility_id from facility where sup_code=" +
                        dir + ")   group by p.pro_name, p.prod_id order by 1";
                }
                if (hq.equals("2")) {
                    //sql = " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')="+year+" and to_char(ci.p_date,'Q') in ('3','4') and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   group by p.pro_name, p.prod_id order by 1" ;
                    sql =
                        " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')='" +
                        year +
                        "' and to_char(cm.p_date,'Q') in ('3','4') and cm.facility_id in (select facility_id from facility where sup_code=" +
                        dir + ")   group by p.pro_name, p.prod_id order by 1";
                }
                ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
            }


        } //for higher level

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("3rd level " + sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getFacServiceStatisticsRep(String dir, String quart, String year, String dat, String fY, String tY,
                                                String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.new_visits),0), p.prod_id, nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id(+)=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                    ")   group by p.pro_name, p.prod_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.new_visits),0),p.prod_id, nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart +
                    " and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                    ")   group by p.pro_name, p.prod_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.new_visits),0),p.prod_id, nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'mm')=" + quart +
                    " and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                    ")   group by p.pro_name, p.prod_id order by 1";
            }
        }


        if (dat.equals("u")) {
            sql =
                " select p.pro_name, nvl(sum(ci.new_visits),0), p.prod_id, nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id(+)=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                "','mm/yyyy')  and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                ")   group by p.pro_name, p.prod_id order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.new_visits),0),p.prod_id, nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year +
                    " and to_char(cm.p_date,'Q') in ('1','2') and cm.facility_id in (select facility_id from facility where sup_code=" +
                    dir + ")   group by p.pro_name, p.prod_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.new_visits),0),p.prod_id, nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year +
                    " and to_char(cm.p_date,'Q') in ('3','4') and cm.facility_id in (select facility_id from facility where sup_code=" +
                    dir + ")   group by p.pro_name, p.prod_id order by 1";
            }
        }
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("Main RS " + sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }


    public ResultSet getFacServiceStatisticsRep3(String dir, String quart, String year, String dat, String fY,
                                                 String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.new_visits),0), p.prod_id, nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id(+)=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and cm.facility_id in (" + dir + ")   group by p.pro_name, p.prod_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.new_visits),0),p.prod_id, nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart + " and cm.facility_id in (" + dir +
                    ")   group by p.pro_name, p.prod_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.new_visits),0),p.prod_id, nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'mm')=" + quart + " and cm.facility_id in (" + dir +
                    ")   group by p.pro_name, p.prod_id order by 1";
            }
        }


        if (dat.equals("u")) {
            sql =
                " select p.pro_name, nvl(sum(ci.new_visits),0), p.prod_id, nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id(+)=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and cm.facility_id in (" +
                dir + ")   group by p.pro_name, p.prod_id order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.new_visits),0),p.prod_id, nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('1','2') and cm.facility_id in (" + dir +
                    ")   group by p.pro_name, p.prod_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.new_visits),0),p.prod_id, nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('3','4') and cm.facility_id in (" + dir +
                    ")   group by p.pro_name, p.prod_id order by 1";
            }
        }
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("Main RS " + sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }


    public String getCommintCtfMain(String dir, String quart, String year, String dat, String fY, String tY, String fM,
                                    String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    " select decode(ctf_comments,'null',' ',ctf_comments) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id(+)=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and cm.facility_id in (" + dir + ")   ";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    " select  decode(ctf_comments,'null',' ',ctf_comments) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q')=" + quart + " and cm.facility_id in (" + dir + ")   ";
            }
            if (!quart.equals("0") &&
                dat.equals("m")) {
                //       sql = " select  decode(ctf_comments,'null',' ',ctf_comments) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')="+year+" and to_char(cm.p_date,'mm')="+quart+" and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   " ;
                sql =
                                      " select  decode(ctf_comments,'null',' ',ctf_comments) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                                      year + " and to_char(cm.p_date,'mm')=" + quart + " and cm.facility_id in (" +
                                      dir + ")   ";
            }
        }


        if (dat.equals("u")) {
            sql =
                " select  decode(ctf_comments,'null',' ',ctf_comments) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id(+)=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy')  and cm.facility_id in (" +
                dir + ")   ";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select  decode(ctf_comments,'null',' ',ctf_comments) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('1','2') and cm.facility_id in (" + dir + ")   ";
            }
            if (hq.equals("2")) {
                sql =
                    " select  decode(ctf_comments,'null',' ',ctf_comments) from  ctf_item ci, product p, ctf_main cm, facility f  where f.facility_id=cm.facility_id and cm.ctf_main_id(+)=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + " and to_char(cm.p_date,'Q') in ('3','4') and cm.facility_id in (" + dir + ")   ";
            }
        }
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("Main RS " + sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getString(1);
            } else {
                return " ";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Double getFacDispensedToUserRepMainSum(String dir, String quart, String year, String dat, String fY,
                                                  String tY, String fM, String tM, String type, String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    " select nvl(sum(ci.issues),0) from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and cm.facility_id in (select facility_id from facility where sup_code=" + dir + ")   ";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    " select nvl(sum(ci.issues),0) from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and to_char(cm.p_date,'Q')=" + quart +
                    " and cm.facility_id in (select facility_id from facility where sup_code=" + dir + ")   ";
            }
            if (!quart.equals("0") &&
                dat.equals("m")) {
                //sql = " select nvl(sum(ci.issues),0) from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(ci.p_date,'yyyy')="+year+"  and to_char(ci.p_date,'mm')="+quart+" and cm.facility_id in (select facility_id from facility where sup_code="+dir+")   " ;
                sql =
                                      " select nvl(sum(ci.issues),0) from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                                      year + "  and to_char(cm.p_date,'mm')=" + quart + " and cm.facility_id in (" +
                                      dir + ")   ";
            }


        }
        if (dat.equals("u")) {
            sql =
                " select nvl(sum(ci.issues),0) from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                "','mm/yyyy')   and cm.facility_id in (select facility_id from facility where sup_code=" + dir + ")   ";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select nvl(sum(ci.issues),0) from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year +
                    "  and to_char(cm.p_date,'Q') in ('1','2') and cm.facility_id in (select facility_id from facility where sup_code=" +
                    dir + ")   ";
            }
            if (hq.equals("2")) {
                sql =
                    " select nvl(sum(ci.issues),0) from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year +
                    "  and to_char(cm.p_date,'Q') in ('3','4') and cm.facility_id in (select facility_id from facility where sup_code=" +
                    dir + ")   ";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            System.out.println("SUM " + sql);
            rs.next();

            return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.010;
        }

    }

    public Double getFacilityServicesStatisticsRepMainSum(String dir, String quart, String year, int index, String dat,
                                                          String fY, String tY, String fM, String tM, String type,
                                                          String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    " select nvl(sum(ci.new_visits),0), nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and cm.facility_id in (select facility_id from facility where sup_code=" + dir + ")   ";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    " select nvl(sum(ci.new_visits),0), nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and to_char(cm.p_date,'Q')=" + quart +
                    " and cm.facility_id in (select facility_id from facility where sup_code=" + dir + ")   ";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    " select nvl(sum(ci.new_visits),0), nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year + "  and to_char(cm.p_date,'mm')=" + quart +
                    " and cm.facility_id in (select facility_id from facility where sup_code=" + dir + ")   ";
            }
        }


        if (dat.equals("u")) {
            sql =
                " select nvl(sum(ci.new_visits),0), nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY +
                "','mm/yyyy')  and to_char(cm.p_date,'mm')=" + quart +
                " and cm.facility_id in (select facility_id from facility where sup_code=" + dir + ")   ";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select nvl(sum(ci.new_visits),0), nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year +
                    "  and to_char(cm.p_date,'Q') in ('1','2') and cm.facility_id in (select facility_id from facility where sup_code=" +
                    dir + ")   ";
            }
            if (hq.equals("2")) {
                sql =
                    " select nvl(sum(ci.new_visits),0), nvl(sum(ci.cont_visits),0) from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id(+) and to_char(cm.p_date,'yyyy')=" +
                    year +
                    "  and to_char(cm.p_date,'Q') in ('3','4') and cm.facility_id in (select facility_id from facility where sup_code=" +
                    dir + ")   ";
            }
        }
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            System.out.println("SUM " + sql);
            rs.next();

            return rs.getDouble(index);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.010;
        }

    }


    public Double getFacDispensedToUserRepMain(String dir, String quart, String year, String prod, String dat,
                                               String fY, String tY, String fM, String tM, String type, String hq) {

        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod +
                    "  and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                    ")   group by p.pro_name, p.prod_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod + " and to_char(cm.p_date,'Q')=" + quart +
                    " and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                    ")   group by p.pro_name, p.prod_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod + " and to_char(cm.p_date,'mm')=" + quart +
                    " and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                    ")   group by p.pro_name, p.prod_id order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and ci.prod_id=" + prod +
                "  and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                ")   group by p.pro_name, p.prod_id order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod +
                    " and to_char(cm.p_date,'Q') in ('1','2') and cm.facility_id in (select facility_id from facility where sup_code=" +
                    dir + ")   group by p.pro_name, p.prod_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod +
                    " and to_char(cm.p_date,'Q') in ('3','4') and cm.facility_id in (select facility_id from facility where sup_code=" +
                    dir + ")   group by p.pro_name, p.prod_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("hiegher level " + sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(2);
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.010;
        }

    }


    public Double getFacDispensedToUserRepProg(String dir, String quart, String year, String prod, String dat,
                                               String fY, String tY, String fM, String tM, String type, String hq) {

        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and cm.facility_id in (select ff.facility_id from facility ff,fac_type fty where ff.fac_type_id=fty.fac_type_id and type_hierarchy=3) and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod + "     group by p.pro_name, p.prod_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and cm.facility_id in (select ff.facility_id from facility ff,fac_type fty where ff.fac_type_id=fty.fac_type_id and type_hierarchy=3) and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod + " and to_char(ci.p_date,'Q')=" + quart +
                    "    group by p.pro_name, p.prod_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and cm.facility_id in (select ff.facility_id from facility ff,fac_type fty where ff.fac_type_id=fty.fac_type_id and type_hierarchy=3) and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod + " and to_char(ci.p_date,'mm')=" + quart +
                    "    group by p.pro_name, p.prod_id order by 1";
            }


        }
        if (dat.equals("u")) {
            sql =
                " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and cm.facility_id in (select ff.facility_id from facility ff,fac_type fty where ff.fac_type_id=fty.fac_type_id and type_hierarchy=3) and ci.prod_id=p.prod_id and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and ci.prod_id=" + prod +
                "     group by p.pro_name, p.prod_id order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and cm.facility_id in (select ff.facility_id from facility ff,fac_type fty where ff.fac_type_id=fty.fac_type_id and type_hierarchy=3) and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod +
                    " and to_char(ci.p_date,'Q') in ('1','2')    group by p.pro_name, p.prod_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.issues),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and cm.facility_id in (select ff.facility_id from facility ff,fac_type fty where ff.fac_type_id=fty.fac_type_id and type_hierarchy=3) and ci.prod_id=p.prod_id and to_char(ci.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod +
                    " and to_char(ci.p_date,'Q') in ('3','4') and cm.facility_id in (select facility_id from facility where sup_code=" +
                    dir + ")   group by p.pro_name, p.prod_id order by 1";
            }
            ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble(2);
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.010;
        }

    }


    public Double getFacServiceStatisticsRepMain(String dir, String quart, String year, String prod, int index,
                                                 String dat, String fY, String tY, String fM, String tM, String type,
                                                 String hq) {
        String sql = "";
        if (dat.equals("m") || dat.equals("q")) {
            if (quart.equals("0")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.new_visits),0),nvl(sum(ci.cont_visits),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod +
                    "  and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                    ")   group by p.pro_name, p.prod_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("q")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.new_visits),0),nvl(sum(ci.cont_visits),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod + " and to_char(cm.p_date,'Q')=" + quart +
                    " and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                    ")   group by p.pro_name, p.prod_id order by 1";
            }
            if (!quart.equals("0") && dat.equals("m")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.new_visits),0),nvl(sum(ci.cont_visits),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod + " and to_char(cm.p_date,'mm')=" + quart +
                    " and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                    ")   group by p.pro_name, p.prod_id order by 1";
            }
        }


        if (dat.equals("u")) {
            sql =
                " select p.pro_name, nvl(sum(ci.new_visits),0),nvl(sum(ci.cont_visits),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and cm.p_date between to_date('" +
                fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and ci.prod_id=" + prod +
                "  and cm.facility_id in (select facility_id from facility where sup_code=" + dir +
                ")   group by p.pro_name, p.prod_id order by 1";


        }
        if (dat.equals("hq")) {
            System.out.println("1st &&&&&&&&&&&&& 2nd half year");
            if (hq.equals("1")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.new_visits),0),nvl(sum(ci.cont_visits),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod +
                    " and to_char(cm.p_date,'Q') in ('1','2') and cm.facility_id in (select facility_id from facility where sup_code=" +
                    dir + ")   group by p.pro_name, p.prod_id order by 1";
            }
            if (hq.equals("2")) {
                sql =
                    " select p.pro_name, nvl(sum(ci.new_visits),0),nvl(sum(ci.cont_visits),0),p.prod_id from  ctf_item ci, product p, ctf_main cm where cm.ctf_main_id=ci.ctf_main_id and ci.prod_id=p.prod_id and to_char(cm.p_date,'yyyy')=" +
                    year + " and ci.prod_id=" + prod +
                    " and to_char(cm.p_date,'Q') in ('3','4') and cm.facility_id in (select facility_id from facility where sup_code=" +
                    dir + ")   group by p.pro_name, p.prod_id order by 1";
            }
        }

        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            System.out.println(sql);
            rs.next();
            return rs.getDouble(index);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.010;
        }

    }


    public void s() {

        try {
            rs.close();
        } catch (Exception e) { /* ignored */
        }
        try {
            stmt.close();
        } catch (Exception e) { /* ignored */
        }
        try {
            conn.close();
        } catch (Exception e) { /* ignored */
        }


    }
    ////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////


    public ResultSet getFacilityDir() {
        try {
            getDBConnection();
            String sql = ("select * from facility where fac_type_id=7");
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);

        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }

        return rs;
    }
    
    public ResultSet directorates() {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select facility_id, fac_name from facility where fac_type_id=7 " +
                "union select facility_id, fac_name from facility where facility_id=498");
            rs = stmt.executeQuery(sql);

        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }

        return rs;
    }
    
    
    


    public ResultSet getFacilityCenter(String dir) {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select * from facility where fac_type_id=6 and sup_code= " + dir);
            rs = stmt.executeQuery(sql);

        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }

        return rs;
    }


    public ResultSet getFacilityCenters() {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select * from facility where fac_type_id=6 order by 3");
            rs = stmt.executeQuery(sql);

        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }

        return rs;
    }

    public ResultSet getFacilities() {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select * from facility order by 3");
            rs = stmt.executeQuery(sql);

        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }

        return rs;
    }

    public ResultSet getProduct() {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select * from product");
            rs = stmt.executeQuery(sql);

        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }

        return rs;
    }


    public ResultSet getFacType() {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select * from fac_type");
            rs = stmt.executeQuery(sql);

        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }

        return rs;
    }


    public ResultSet getAdjType() {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select * from adj_type");
            rs = stmt.executeQuery(sql);

        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }

        return rs;
    }


    public ResultSet getCTFMain(String fname, String y, String m) {
        System.out.println(fname + y + m);
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select m.ctf_main_id,m.facility_id,f.fac_name,m.lc_date from ctf_main m,facility f where  m.facility_id = f.facility_id and to_char(m.lc_date,'yyyy/mm') = '" +
                                      y + "/" + m + "' and m.facility_id =  " + fname);
            rs = stmt.executeQuery(sql);

        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }

        return rs;
    }

    public ResultSet getCTFItem(String ctf) {

        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select p.pro_name,p.pro_dose,i.open_bal, " +
                "i.receipts,i.issues,i.adjustments,i.closing_bal, " +
                "i.avg_mnthly_cons,i.qty_required,i.qty_received, " +
                "i.new_visits,i.cont_visits from product p, " +
                "ctf_item i where p.prod_id = i.prod_id  and i.ctf_main_id= "+ ctf);
            rs = stmt.executeQuery(sql);

        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }

        return rs;
    }


    ///select p.pro_name,p.pro_dose,i.open_bal,i.receipts,i.issues,i.adjustments,i.closing_bal,i.avg_mnthly_cons,i.qty_required,i.qty_received,i.new_visits,i.cont_visits from product p,ctf_item i where p.prod_id = i.prod_id  and i.ctf_main_id=63542


    //select m.ctf_main_id,m.facility_id,f.fac_name from ctf_main m,facility f where  m.facility_id = f.facility_id
    /////////////////////////////////////////////
    public ResultSet getSiteInfo() {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql  = ("select SITE_NAME,SITE_NAME_E from SEC_SITE_INFORMATIONS");
            rs = stmt.executeQuery(sql);

        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }

        return rs;
    }

    public ResultSet getJobGroups() {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
           String sql = ("select JOB_GROUP_ID,JOB_GROUP_DESC from SEC_JOB_GROUPS");
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }
        return rs;
    }

    public ResultSet getJobs(String jobGroup_id) {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select JOB_ID,JOB_DESC from SEC_JOBS where JOB_GROUP_ID =   "+  jobGroup_id);
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }
        ;
        return rs;
    }

    public ResultSet getUsers(String job_id) {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select USER_ID,FIRST_NAME,FIRST_NAME_E, " +
                "MIDDLE_NAME,MIDDLE_NAME_E,FAMILY_NAME,FAMILY_NAME_E " +
                "from SEC_USERS where JOB_ID = " + job_id);
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }
        return rs;
    }

    public ResultSet getTimeDate() {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select to_char(sysdate,'hh24:mi:ss'),to_char(sysdate,'dd/MM/yyyy') from dual");
            rs = stmt.executeQuery(sql);
            rs.next();
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }


        return rs;
    }

    public ResultSet getJobGroup(String jobGroupId) {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select JOB_GROUP_DESC from SEC_JOB_GROUPS where JOB_GROUP_ID = ?");
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }

        return rs;
    }

    public ResultSet getFaildLogons(String s, String e) {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select USER_NAME,USER_PASS,LOGON_TIME,LOGON_SEQ," +
                "MACHINE_NAME,NETWORK_PORT from SEC_USER_FAILD_LOGONS where " +
                "LOGON_TIME between to_date('"+s+"','dd/MM/yyyy') " +
                "and to_date('"+e+"','dd/MM/yyyy') ");
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }
        return rs;
    }

    public ResultSet getModules(String mod_id) {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select MODULE_ID,MODULE_DESC from SEC_GEN_MODULES where MODULE_ID = "+ mod_id);
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }


        return rs;
    }

    public ResultSet getModUsers(String job_id) {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select MODULE_ID from SEC_MODULE_USERS where JOB_ID = "+ job_id);
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }
        return rs;
    }

    public ResultSet getFunctions(String fun_id) {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select FUNCTION_DESC,FUNCTION_DESC_E from SEC_FUNCTIONS where FUNCTION_ID = "+fun_id);
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }
        return rs;
    }

    public ResultSet getFunAccessLvls(String job_id) {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select FUNCTION_ID from SEC_FUNCTION_ACCESS_LEVELS where JOB_ID = "+job_id);
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }
        return rs;
    }

    public ResultSet getMonthlyFiles(String month, String year) {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select mof_msf_id,to_char(mof_depr_date,'dd/MM/yyyy'), " +
                "mof_depr_pct,mof_monthly_depr_val,mof_pre_net_book_val,mof_pre_accum_depr_val, " +
                "mof_net_book_val,mof_accum_depr_val from fix_monthly_file where mof_month = " + month + 
                " and mof_year = " + year);
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }
        return rs;
    }

    public ResultSet getMsfCode(String msf_id) {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select msf_code,msf_desc from fix_master_file where msf_id = " + msf_id);
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }
        return rs;
    }

    public ResultSet getYears() {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select distinct(mof_year) from fix_monthly_file");
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }
        return rs;
    }

    public ResultSet getSellTransActions(String s_date, String e_date) {
        try {
            getDBConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = ("select hdf_code,to_char(hdf_trans_date,'dd/MM/yyyy'), " +
                "msf_code,lif_desc,to_char(lif_start_depr_date,'dd/MM/yyyy'), " +
                "lif_book_val,lif_resale_val,lif_depr_pct,lif_accum_depr_val, " +
                "decode(lif_resale_result_flag,'G','???','L','?????') " +
                "from fix_header_file,fix_lines_file,fix_master_file " +
                "where hdf_id = lif_hdf_id and hdf_trans_type = 'S' " +
                "and msf_id = lif_msf_id and hdf_post_flag = 'A' " +
                "and hdf_trans_date between to_date('"+s_date+"','dd/MM/yyyy') "+ 
                " and to_date('"+e_date+"','dd/MM/yyyy')");
            rs = stmt.executeQuery(sql);
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }
        return rs;
    }

   
    public boolean getSupplyStatusRepSumByPoductsboolean(String fac, String mon, String year, String prod[], String dat,
                                                         String fY, String tY, String fM, String tM, String type,
                                                         String hq) {
        String sql = "";
        double avg = 0;
        String sqlClose = "";
        boolean flag = false;
        for (int mo = 1; mo <= 12; mo++) {
            for (int p = 0; p < prod.length; p++) {
                if (dat.equals("m") || dat.equals("q")) {
                    if (dat.equals("m") &&
                        !mon.equals("0")) {
                        //sql = "select  sum(avg_mnthly_cons),f.fac_name, ft.fac_name  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in ("+prod+") and to_char(cm.p_date,'mm/yyyy')='"+mon+"/"+year+"' and f.facility_id in (select ff.facility_id from facility ff where sup_code="+fac+") group by f.fac_name, ft.fac_name  order by 1";
                        sql =
         "select sum(avg_mnthly_cons)     ,ci.prod_id,to_char(cm.p_date,'mm')  from ctf_item ci,ctf_main cm,facility f, facility fd where f.facility_id=fd.facility_id and ci.ctf_main_id=cm.ctf_main_id and cm.facility_id=f.facility_id and to_char(cm.p_date,'mm')=" +
         mon + " and to_char(cm.p_date,'yyyy')='" + year + "' and f.sup_code=" + fac + "  and ci.prod_id in (" +
         prod[p] + ") group by ci.prod_id,to_char(cm.p_date,'mm')  ";
                        sqlClose =
                            "select closing_bal  from ctf_item ci,ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'mm')=" +
                            mon + " and to_char(cm.p_date,'yyyy')='" + year + "' and ci.prod_id=" + prod[p] +
                            "  and cm.facility_id=" + fac;
                    }
                    if (dat.equals("q") && !mon.equals("0")) {
                        System.out.println("mon11 is " + mon);
                        System.out.println("herreeee");
                        //   String q = "";
                        if (mon.equals("01")) {
                            System.out.println("sssssssssssss");
                            sql =
                                "select sum(avg_mnthly_cons)     ,ci.prod_id,to_char(cm.p_date,'mm')  from ctf_item ci,ctf_main cm,facility f, facility fd where f.facility_id=fd.facility_id and ci.ctf_main_id=cm.ctf_main_id and cm.facility_id=f.facility_id and to_char(cm.p_date,'mm')=" +
                                mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and f.sup_code=" + fac +
                                "  and ci.prod_id in (" + prod[p] + ") group by ci.prod_id,to_char(cm.p_date,'mm')  ";
                            sqlClose =
                                "select closing_bal  from ctf_item ci,ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'mm')=" +
                                mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and ci.prod_id=" + prod[p] +
                                "  and cm.facility_id=" + fac;
                            if (mo > 3) {
                                continue;
                            }
                        }
                        if (mon.equals("02")) {
                            //q = "to_char(cm.p_date,'mm')='06'";
                            if (mo < 3 || mo > 6) {
                                continue;
                            }
                            sql =
                                "select sum(avg_mnthly_cons)     ,ci.prod_id,to_char(cm.p_date,'mm')  from ctf_item ci,ctf_main cm,facility f, facility fd where f.facility_id=fd.facility_id and ci.ctf_main_id=cm.ctf_main_id and cm.facility_id=f.facility_id and to_char(cm.p_date,'mm')=" +
                                mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and f.sup_code=" + fac +
                                "  and ci.prod_id in (" + prod[p] + ") group by ci.prod_id,to_char(cm.p_date,'mm')  ";
                            sqlClose =
                                "select closing_bal  from ctf_item ci,ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'mm')=" +
                                mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and ci.prod_id=" + prod[p] +
                                "  and cm.facility_id=" + fac;

                        }
                        if (mon.equals("03")) {
                            //q = "to_char(cm.p_date,'mm')='09'";
                            if (mo < 6 || mo > 9) {
                                continue;
                            }
                            sql =
                                "select sum(avg_mnthly_cons)     ,ci.prod_id,to_char(cm.p_date,'mm')  from ctf_item ci,ctf_main cm,facility f, facility fd where f.facility_id=fd.facility_id and ci.ctf_main_id=cm.ctf_main_id and cm.facility_id=f.facility_id and to_char(cm.p_date,'mm')=" +
                                mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and f.sup_code=" + fac +
                                "  and ci.prod_id in (" + prod[p] + ") group by ci.prod_id,to_char(cm.p_date,'mm')  ";
                            sqlClose =
                                "select closing_bal  from ctf_item ci,ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'mm')=" +
                                mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and ci.prod_id=" + prod[p] +
                                "  and cm.facility_id=" + fac;
                        }
                        if (mon.equals("04")) {
                            //            q = "to_char(cm.p_date,'mm')='12'";
                            if (mo < 10) {
                                continue;
                            }
                            sql =
                                "select sum(avg_mnthly_cons)     ,ci.prod_id,to_char(cm.p_date,'mm')  from ctf_item ci,ctf_main cm,facility f, facility fd where f.facility_id=fd.facility_id and ci.ctf_main_id=cm.ctf_main_id and cm.facility_id=f.facility_id and to_char(cm.p_date,'mm')=" +
                                mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and f.sup_code=" + fac +
                                "  and ci.prod_id in (" + prod[p] + ") group by ci.prod_id,to_char(cm.p_date,'mm')  ";
                            sqlClose =
                                "select closing_bal  from ctf_item ci,ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'mm')=" +
                                mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and ci.prod_id=" + prod[p] +
                                "  and cm.facility_id=" + fac;
                        }

                    }


                    //sql = "select  sum(avg_mnthly_cons),f.fac_name, ft.fac_name from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in ("+prod+") and to_char(cm.p_date,'yyyy')='"+year+"' and "+q+" and f.facility_id in (select ff.facility_id from facility ff where sup_code="+fac+") group by f.fac_name, ft.fac_name order by 1";


                }

                if (mon.equals("0")) {
                    //sql = "select  sum(avg_mnthly_cons),f.fac_name, ft.fac_name,ci.prod_id  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in ("+prod+") and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'mm')='12' and f.facility_id in (select ff.facility_id from facility ff where sup_code="+fac+") group by f.fac_name, ft.fac_name,ci.prod_id order by 1";
                    sql =
                        "select  sum(avg_mnthly_cons),f.fac_name, ft.fac_name,ci.prod_id,COUNT(*)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in (" +
                        prod[p] + ") and to_char(cm.p_date,'yyyy')='" + year +
                        "' and to_char(cm.p_date,'mm')='12' and f.facility_id in (select ff.facility_id from facility ff where sup_code=" +
                        fac + ") GROUP BY f.fac_name, ft.fac_name,ci.prod_id HAVING COUNT(*) < 12 order by 1";
                    sqlClose =
                        "select closing_bal  from ctf_item ci,ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'mm')=" +
                        mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and ci.prod_id=" + prod[p] +
                        "  and cm.facility_id=" + fac;
                    System.out.println("Annual");


                    for (int mmm = 0; mmm < 12; mmm++) {

                        sqlClose =
                            "select closing_bal  from ctf_item ci,ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'mm')=" +
                            mmm + " and to_char(cm.p_date,'yyyy')='" + year + "' and ci.prod_id=" + prod[p] +
                            "  and cm.facility_id=" + fac;
                        sql =
                            "select sum(avg_mnthly_cons)     ,ci.prod_id,to_char(cm.p_date,'mm')  from ctf_item ci,ctf_main cm,facility f, facility fd where f.facility_id=fd.facility_id and ci.ctf_main_id=cm.ctf_main_id and cm.facility_id=f.facility_id and to_char(cm.p_date,'mm')=" +
                            mmm + " and to_char(cm.p_date,'yyyy')='" + year + "' and f.sup_code=" + fac +
                            "  and ci.prod_id in (" + prod[p] + ") group by ci.prod_id,to_char(cm.p_date,'mm')  ";
                        try {
                            getDBConnection();
                            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            ResultSet rsss = stmt.executeQuery(sqlClose);
                            if (rsss.getInt(1) != 0) {
                                continue;
                            } else {
                                sqlClose =
                                    "select closing_bal  from ctf_item ci,ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'mm')=" +
                                    mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and ci.prod_id=" + prod[p] +
                                    "  and cm.facility_id=" + fac;
                            }
                        } catch (SQLException e) {
                        }
                    }

                    sqlClose =
                        "select closing_bal  from ctf_item ci,ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'mm')=" +
                        mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and ci.prod_id=" + prod[p] +
                        "  and cm.facility_id=" + fac;
                    sql =
                        "select sum(avg_mnthly_cons)     ,ci.prod_id,to_char(cm.p_date,'mm')  from ctf_item ci,ctf_main cm,facility f, facility fd where f.facility_id=fd.facility_id and ci.ctf_main_id=cm.ctf_main_id and cm.facility_id=f.facility_id and to_char(cm.p_date,'mm')=" +
                        mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and f.sup_code=" + fac +
                        "  and ci.prod_id in (" + prod[p] + ") group by ci.prod_id,to_char(cm.p_date,'mm')  ";
                }


                if (dat.equals("u")) {
                    //sql = "select  sum(avg_mnthly_cons),f.fac_name, ft.fac_name  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in ("+prod+") and to_char(cm.p_date,'mm/yyyy') = '"+tM+"/"+tY+"' and f.facility_id in (select ff.facility_id from facility ff where sup_code="+fac+") group by f.fac_name, ft.fac_name  order by 1";

                    sql =
                        "select sum(avg_mnthly_cons)     ,ci.prod_id,to_char(cm.p_date,'mm')  from ctf_item ci,ctf_main cm,facility f, facility fd where f.facility_id=fd.facility_id and ci.ctf_main_id=cm.ctf_main_id and cm.facility_id=f.facility_id and to_char(cm.p_date,'mm')=" +
                        mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and f.sup_code=" + fac +
                        "  and ci.prod_id in (" + prod[p] + ") group by ci.prod_id,to_char(cm.p_date,'mm')  ";
                    sqlClose =
                        "select closing_bal  from ctf_item ci,ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'mm')=" +
                        mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and ci.prod_id=" + prod[p] +
                        "  and cm.facility_id=" + fac;
                    System.out.println("mo is " + mo + " tM is " + tM);

                    if (mo > Integer.parseInt(tM))
                        continue;

                    //sqlClose  = "select sum(closing_bal)  from ctf_item ci,ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and  to_char(cm.p_date,'mm/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+fY+"'  and ci.prod_id="+prod[p]+"  and cm.facility_id="+fac;
                }
                if (dat.equals("hq")) {
                    System.out.println("1st &&&&&&&&&&&&& 2nd half year");
                    if (hq.equals("1")) {
                        //      sql = "select  sum(avg_mnthly_cons),f.fac_name, ft.fac_name  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in ("+prod+") and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'mm')='06' and f.facility_id in (select ff.facility_id from facility ff where sup_code="+fac+") group by f.fac_name, ft.fac_name order by 1";
                        sql =
                            "select sum(avg_mnthly_cons)     ,ci.prod_id,to_char(cm.p_date,'mm')  from ctf_item ci,ctf_main cm,facility f, facility fd where f.facility_id=fd.facility_id and ci.ctf_main_id=cm.ctf_main_id and cm.facility_id=f.facility_id and to_char(cm.p_date,'mm')=" +
                            mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and f.sup_code=" + fac +
                            "  and ci.prod_id in (" + prod[p] + ") group by ci.prod_id,to_char(cm.p_date,'mm')  ";
                        sqlClose =
                            "select closing_bal  from ctf_item ci,ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'mm')=" +
                            mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and ci.prod_id=" + prod[p] +
                            "  and cm.facility_id=" + fac;
                        if (mo > 6) {
                            break;
                        }

                    }
                    if (hq.equals("2")) {

                        if (mo <= 6) {
                            continue;
                        }
                        sql =
                            "select sum(avg_mnthly_cons)     ,ci.prod_id,to_char(cm.p_date,'mm')  from ctf_item ci,ctf_main cm,facility f, facility fd where f.facility_id=fd.facility_id and ci.ctf_main_id=cm.ctf_main_id and cm.facility_id=f.facility_id and to_char(cm.p_date,'mm')=" +
                            mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and f.sup_code=" + fac +
                            "  and ci.prod_id in (" + prod[p] + ") group by ci.prod_id,to_char(cm.p_date,'mm')  ";
                        sqlClose =
                            "select closing_bal  from ctf_item ci,ctf_main cm where ci.ctf_main_id = cm.ctf_main_id and to_char(cm.p_date,'mm')=" +
                            mo + " and to_char(cm.p_date,'yyyy')='" + year + "' and ci.prod_id=" + prod[p] +
                            "  and cm.facility_id=" + fac;

                        //                      sql = "select  sum(avg_mnthly_cons),f.fac_name, ft.fac_name from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id in ("+prod+") and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'mm')='12' and f.facility_id in (select ff.facility_id from facility ff where sup_code="+fac+") group by f.fac_name, ft.fac_name order by 1";
                    }
                    ///sql = "select f.facility_id from  facility f where facility_id not in(select cm.facility_id from ctf_main cm where to_char(cm.p_date,'MM/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' )" ;
                }
                System.out.println("Sum AVG Dir " + sql);
                System.out.println("Sum Close Dir " + sqlClose);
                try {
                    getDBConnection(); 
                    stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    System.out.println("Sum AVG " + sql);
                    rs = stmt.executeQuery(sql);
                    if(rs.next()){
                    getDBConnection();
                    PreparedStatement pstClosed = conn.prepareStatement(sqlClose);
                    ResultSet rsClosed = pstClosed.executeQuery();
                    if(rsClosed.next()){
                    System.out.println("pxpx" + sql);
                    System.out.println("px1px1" + sqlClose);
                    System.out.println("px1px1value " + rsClosed.getString(1));
                    //if(!rs.getString(1).equals("0") && rsClosed.getString(1).equals("0")){
                    if (!dat.equals("u")) {
                        if (rs.getInt(1) > 0 && rsClosed.getString(1).equals("0")) {
                            System.out.println("vag is" + rsClosed.getString(1));
                            flag = true;
                            //                    if(rsClosed.getString(1).equals("0"))
                            //                        flag = false;
                            break;
                        }
                    } else {
                        System.out.println("user defined");
                        ////////////////// user defined
                        //                if(rsClosed.getString(1).equals("0"))
                        //                    continue;
                        if (rs.getInt(1) > 0 && rsClosed.getString(1).equals("0")) {
                            System.out.println("vag is" + rsClosed.getString(1));

                            System.out.println("fac is" + fac + " rsClosed " + rsClosed.getString(1));


                            flag = true;

                            break;
                        }
                    }
                    }
                    }

                    System.out.println(" avarage is **************" + avg);
                    //return flag;

                } catch (Exception e) {
                    // TODO: Add catch code


                    e.printStackTrace();
                    return false;
                }
            }


        }
        return flag;
    }

    public ResultSet getAggStockMovByLevelOBCB(String prod, String mon, String year, String dat, String fY, String tY,
                                           String fM, String tM, String type, String hq) {


        String sql = "";
        String mon1 = "";

        if (type.equals("0")) {
            
            sql = "select  ft.type_hierarchy,  sum(ci.open_bal), " +
                "sum(ci.receipts), sum(ci.issues), " +
                " sum(ci.adjustments)  , sum(ci.closing_bal), " +
                " decode (           (select sum(ciy.avg_mnthly_cons) " +
                "from ctf_item ciy, ctf_main cmm where " +
                " cmm.ctf_main_id = ciy.ctf_main_id " +
                " and   to_char(cmm.p_date,'yyyy')='" +
                year + "' and to_char(cmm.p_date,'mm') = '" + mon + "'" +
                " and ciy.prod_id=" + prod +
                " and cmm.facility_id in (select ff.facility_id from facility ff " +
                " where ff.fac_type_id in (select fty.fac_type_id " +
                " from fac_type fty where fty.type_hierarchy=3))) ,0,0,                    sum(closing_bal)/ (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='" +
            year + "' and to_char(cmm.p_date,'Q') = '" + mon + "' and ciy.prod_id=" + prod +
            " and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))    )                                             from ctf_item ci, ctf_main cm, facility f, fac_type ft where  ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
            prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q') = '" + mon +
            "'    group by ft.type_hierarchy order by ft.type_hierarchy";
            
            sql =
                "select  ft.type_hierarchy,  ci.open_bal, ci.closing_bal, sum(ci.issues)," +
                " from ctf_item ci, ctf_main cm, facility f, fac_type ft " +
                " where  ci.ctf_main_id = cm.ctf_main_id(+) " +
                " and cm.facility_id = f.facility_id(+) " +
                " and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                prod + " and to_char(cm.p_date,'yyyy')='" + year + "'" +
                " and to_char(cm.p_date,'mm') = '" + mon +
                "'    group by ft.type_hierarchy,  ci.closing_bal, ci.open_bal " +
                " order by ft.type_hierarchy ";
               
            if (dat.equals("m") || dat.equals("q")) {
                if (dat.equals("m") &&
                    !mon.equals("0")) {
                    //sql = "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues),                           case when NVL(sum(ci.adjustments),0) > 0 then decode(sum(adj.always_negative),1,sum(ci.adjustments * -1),sum(ci.adjustments) ) else nvl(sum(ci.adjustments),0) end  , sum(ci.closing_bal),                                     decode (           (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='"+year+"' and to_char(cmm.p_date,'mm') = '"+mon+"' and ciy.prod_id="+prod+" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3))) ,0,0,                    sum(closing_bal)/ (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='"+year+"' and to_char(cmm.p_date,'mm') = '"+mon+"' and ciy.prod_id="+prod+" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))    )                                             from ctf_item ci, ctf_main cm, facility f, fac_type ft , adj_type adj where adj.adj_type_id(+)=ci.adj_type_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'mm') = '"+mon+"' and f.active=1  group by ft.type_hierarchy order by ft.type_hierarchy";
                    sql =
     "select  ft.type_hierarchy,  ci.open_bal, ci.closing_bal, sum(ci.issues)," +

                        " from ctf_item ci, ctf_main cm, facility f, fac_type ft where  ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
     prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'mm') = '" + mon +
     "'    group by ft.type_hierarchy order by ft.type_hierarchy";
                    // sql = "select sum(ci.receipts) from ctf_item ci where ci.prod_id=4 and to_char(p_date,'mm/yyyy')='11/2013'";
                    // sql = "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), decode(sum(adj.always_negative) ,1,sum(ci.adjustments * -1),sum(ci.adjustments)), sum(ci.closing_bal), sum(closing_bal)/(select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='"+year+"'  and ciy.prod_id="+prod+" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))   from ctf_item ci, ctf_main cm, facility f, fac_type ft , adj_type adj where adj.adj_type_id=ci.adj_type_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'yyyy')='"+mon+"'   and f.active=1  group by ft.type_hierarchy order by ft.type_hierarchy";
                }
                if (dat.equals("q") &&
                    !mon.equals("0")) {
                    //  sql = "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues),                     decode(sum(adj.always_negative) ,1,sum(ci.adjustments * -1),sum(ci.adjustments)), sum(ci.closing_bal), sum(closing_bal)/(select sum(ciy.avg_mnthly_cons) case when sum(ci.adjustments) > 0 then decode(sum(adj.always_negative),1,sum(ci.adjustments * -1),sum(ci.adjustments) ) else sum(ci.adjustments) end  , sum(ci.closing_bal),                                     decode (           (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='"+year+"' and to_char(cmm.p_date,'mm') = '"+mon+"' and ciy.prod_id=\"+prod+\" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3))) ,0,0,                    sum(closing_bal)/ (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='"+year+"' and to_char(cmm.p_date,'mm') = '"+mon+"' and ciy.prod_id="+prod+" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))    )                                    from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='"+year+"' and to_char(cmm.p_date,'Q') = '"+mon+"' and ciy.prod_id="+prod+" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))  from ctf_item ci, ctf_main cm, facility f, fac_type ft , adj_type adj where adj.adj_type_id=ci.adj_type_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(cm.p_date,'Q') = '"+mon1+"' and f.active=1  group by ft.type_hierarchy order by ft.type_hierarchy";
                    sql =
     "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues),                          sum(ci.adjustments)  , sum(ci.closing_bal),                                     decode (           (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='" +
     year + "' and to_char(cmm.p_date,'mm') = '" + mon + "' and ciy.prod_id=" + prod +
     " and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3))) ,0,0,                    sum(closing_bal)/ (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='" +
     year + "' and to_char(cmm.p_date,'Q') = '" + mon + "' and ciy.prod_id=" + prod +
     " and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))    )                                             from ctf_item ci, ctf_main cm, facility f, fac_type ft where  ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
     prod + " and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q') = '" + mon +
     "'    group by ft.type_hierarchy order by ft.type_hierarchy";
                }
                if (mon.equals("0")) {
                    // sql = "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues),               case when sum(ci.adjustments) > 0 then decode(sum(adj.always_negative),1,sum(ci.adjustments * -1),sum(ci.adjustments) ) else sum(ci.adjustments) end  , sum(ci.closing_bal),                                     decode (           (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='\"+year+\"' and to_char(cmm.p_date,'mm') = '"+mon+"' and ciy.prod_id="+prod+" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3))) ,0,0,                    sum(closing_bal)/ (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='"+year+"' and to_char(cmm.p_date,'mm') = '"+mon+"' and ciy.prod_id=\"+prod+\" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))    )      from ctf_item ci, ctf_main cm, facility f, fac_type ft , adj_type adj where adj.adj_type_id=ci.adj_type_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')='"+year+"'  and f.active=1  group by ft.type_hierarchy order by ft.type_hierarchy";
                    sql =
                        "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues),                          sum(ci.adjustments)  , sum(ci.closing_bal),                                     decode (           (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='" +
                        year + "' and to_char(cmm.p_date,'mm') = '" + mon + "' and ciy.prod_id=" + prod +
                        " and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3))) ,0,0,                    sum(closing_bal)/ (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='" +
                        year + "' and ciy.prod_id=" + prod +
                        " and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))    )                                             from ctf_item ci, ctf_main cm, facility f, fac_type ft where  ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')='" + year +
                        "'    group by ft.type_hierarchy order by ft.type_hierarchy";


                    //sql = "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues),                          sum(ci.adjustments)  , sum(ci.closing_bal),                                     decode (           (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='"+year+"' and to_char(cmm.p_date,'mm') = '"+mon+"' and ciy.prod_id="+prod+" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3))) ,0,0,                    sum(closing_bal)/ (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='"+year+"' and ciy.prod_id="+prod+" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))    )                                             from ctf_item ci, ctf_main cm, facility f, fac_type ft where  ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')='"+year+"'    group by ft.type_hierarchy order by ft.type_hierarchy";
                }
            }
            if (dat.equals("u")) {
                //sql = "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues),decode(sum(adj.always_negative) ,1,sum(ci.adjustments * -1),sum(ci.adjustments)), sum(ci.closing_bal), sum(closing_bal)/(select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and to_char(cm.p_date,'mm/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"' and ciy.prod_id="+prod+" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))   from ctf_item ci, ctf_main cm, facility f, fac_type ft , adj_type adj where adj.adj_type_id=ci.adj_type_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(cm.p_date,'mm/yyyy') between '"+fM+"/"+fY+"' and '"+tM+"/"+tY+"'  and f.active=1  group by ft.type_hierarchy order by ft.type_hierarchy";
                //  sql = "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues),                          sum(ci.adjustments)  , sum(ci.closing_bal),                                     decode (           (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and  cm.p_date between to_date('"+fM+"/"+fY+"','mm/yyyy') and to_date('"+tM+"/"+tY+"','mm/yyyy') and ciy.prod_id="+prod+" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3))) ,0,0,                    sum(closing_bal)/ (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and  cm.p_date between to_date('"+fM+"/"+fY+"','mm/yyyy') and to_date('"+tM+"/"+tY+"','mm/yyyy') and ciy.prod_id="+prod+" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))    )                                             from ctf_item ci, ctf_main cm, facility f, fac_type ft where  ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+"  and  cm.p_date between to_date('"+fM+"/"+fY+"','mm/yyyy') and to_date('"+tM+"/"+tY+"','mm/yyyy')    group by ft.type_hierarchy order by ft.type_hierarchy";
                sql =
                    "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues),                          sum(ci.adjustments)  , sum(ci.closing_bal),                                     decode (           (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and  cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and ciy.prod_id=" +
                    prod +
                    " and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3))) ,0,0,                    sum(closing_bal)/ (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   cm.p_date between to_date('" +
                    fM + "/" + fY + "','mm/yyyy') and to_date('" + tM + "/" + tY + "','mm/yyyy') and ciy.prod_id=" +
                    prod +
                    " and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3))))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where  ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM +
                    "/" + tY + "','mm/yyyy')    group by ft.type_hierarchy order by ft.type_hierarchy";
            }
            if (dat.equals("hq")) {
                if (hq.equals("1")) {
                    // sql = "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), decode(sum(adj.always_negative) ,1,sum(ci.adjustments * -1),sum(ci.adjustments)), sum(ci.closing_bal),      sum(closing_bal)/(select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(ci.p_date,'Q') in ('1','2') and ciy.prod_id="+prod+" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))                            from ctf_item ci, ctf_main cm, facility f, fac_type ft , adj_type adj where adj.adj_type_id=ci.adj_type_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(ci.p_date,'Q') in ('1','2') and f.active=1  group by ft.type_hierarchy order by ft.type_hierarchy";
                    sql =
                        "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues),                          sum(ci.adjustments)  , sum(ci.closing_bal),                                     decode (           (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='" +
                        year + "' and to_char(cmm.p_date,'mm') = '" + mon + "' and ciy.prod_id=" + prod +
                        " and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3))) ,0,0,                    sum(closing_bal)/ (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and  to_char(cm.p_date,'yyyy')='" +
                        year + "' and to_char(ci.p_date,'Q') in ('1','2') and ciy.prod_id=" + prod +
                        " and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))    )                                             from ctf_item ci, ctf_main cm, facility f, fac_type ft where  ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')='" + year +
                        "' and to_char(ci.p_date,'Q') in ('1','2') and    group by ft.type_hierarchy order by ft.type_hierarchy";
                } else {
                    //  sql = "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), decode(sum(adj.always_negative) ,1,sum(ci.adjustments * -1),sum(ci.adjustments)), sum(ci.closing_bal), sum(closing_bal)/(select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(ci.p_date,'Q') in ('3','4') and ciy.prod_id=\"+prod+\" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))  from ctf_item ci, ctf_main cm, facility f, fac_type ft , adj_type adj where adj.adj_type_id=ci.adj_type_id and ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')='"+year+"' and to_char(ci.p_date,'Q') in ('3','4') and f.active=1  group by ft.type_hierarchy order by ft.type_hierarchy";
                    sql =
                        "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues),                          sum(ci.adjustments)  , sum(ci.closing_bal),                                     decode (           (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='" +
                        year + "' and to_char(cmm.p_date,'mm') = '" + mon + "' and ciy.prod_id=" + prod +
                        " and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3))) ,0,0,                    sum(closing_bal)/ (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and  to_char(cm.p_date,'yyyy')='" +
                        year + "' and to_char(ci.p_date,'Q') in ('3','4') and ciy.prod_id=" + prod +
                        " and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))    )                                             from ctf_item ci, ctf_main cm, facility f, fac_type ft where  ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(cm.p_date,'yyyy')='" + year +
                        "' and to_char(ci.p_date,'Q') in ('3','4') and    group by ft.type_hierarchy order by ft.type_hierarchy";
                }
            }

            System.out.println(sql);
        } else {
            String faces = "";
            //            for(int s=0; s<cen.length;s++){
            //                if(s==0){
            //                    faces=cen[s];
            //                }else{
            //                    faces+=", "+cen[s];
            //                }
            //            }
            //
            if (dat.equals("m") || dat.equals("q")) {
                if (dat.equals("m") && !mon.equals("0")) {
                    sql =
                        "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal)  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(ci.p_date,'yyyy')='" + year + "' and to_char(ci.p_date,'mm') = '" + mon +
                        "' and f.active=1 and f.fac_type_id=" + type + " and f.facility_id in (" + faces +
                        ") group by f.fac_name order by ft.type_hierarchy";
                }
                if (dat.equals("q") && !mon.equals("0")) {
                    sql =
                        "select   f.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(ci.p_date,'yyyy')='" + year + "' and to_char(ci.p_date,'Q') = '" + mon1 +
                        "'  and f.fac_type_id=" + type + " and f.facility_id in (" + faces +
                        ")  group by f.fac_name order by ft.type_hierarchy";
                }
                if (mon.equals("0")) {
                    sql =
                        "select   f.fac_name,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(ci.p_date,'yyyy')='" + year + "'  and f.active=1 and f.fac_type_id=" +
                        type + " and f.facility_id in (" + faces + ")  group by f.fac_name order by ft.type_hierarchy";
                }
            }
            if (dat.equals("u")) {
                sql =
                    "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                    prod + " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') and to_date('" + tM +
                    "/" + tY + "','mm/yyyy')   and f.fac_type_id=" + type + " and f.facility_id in (" + faces +
                    ")  group by f.fac_name order by ft.type_hierarchy";
                //sql = "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues),                          sum(ci.adjustments)  , sum(ci.closing_bal),                                     decode (           (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='"+year+"' and to_char(cmm.p_date,'mm') = '"+mon+"' and ciy.prod_id="+prod+" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3))) ,0,0,                    sum(closing_bal)/ (select sum(ciy.avg_mnthly_cons) from ctf_item ciy, ctf_main cmm where cmm.ctf_main_id = ciy.ctf_main_id and   to_char(cmm.p_date,'yyyy')='"+year+"' and ciy.prod_id="+prod+" and cmm.facility_id in (select ff.facility_id from facility ff where ff.fac_type_id in (select fty.fac_type_id from fac_type fty where fty.type_hierarchy=3)))    )                                             from ctf_item ci, ctf_main cm, facility f, fac_type ft where  ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id="+prod+" and to_char(cm.p_date,'yyyy')='"+year+"'    group by ft.type_hierarchy order by ft.type_hierarchy";
            }
            if (dat.equals("hq")) {
                if (hq.equals("1")) {
                    sql =
                        "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(ci.p_date,'yyyy')='" + year +
                        "' and to_char(ci.p_date,'Q') in ('1','2') and f.active=1 and f.fac_type_id=" + type +
                        " and f.facility_id in (" + faces + ")  group by f.fac_name order by ft.type_hierarchy";
                } else {
                    sql =
                        "select  ft.type_hierarchy,  sum(ci.open_bal), sum(ci.receipts), sum(ci.issues), sum(ci.adjustments), sum(ci.closing_bal), decode(sum(avg_mnthly_cons), 0,0,sum(closing_bal)/sum(AVG_MNTHLY_CONS))  from ctf_item ci, ctf_main cm, facility f, fac_type ft where ci.ctf_main_id = cm.ctf_main_id(+) and cm.facility_id = f.facility_id(+) and f.fac_type_id= ft.fac_type_id(+) and ci.prod_id=" +
                        prod + " and to_char(ci.p_date,'yyyy')='" + year +
                        "' and to_char(ci.p_date,'Q') in ('3','4') and f.active=1 and f.fac_type_id=" + type +
                        " and f.facility_id in (" + faces + ")  group by f.fac_name order by ft.type_hierarchy";
                }
            }

        }
        try {
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }
    
    
    public String AvgMonConsAggStockMovRepByLvl (String prod, String mon, String year, String dat, String fY, String tY,
                                           String fM, String tM, String type, String hq){
        String sql = " select sum(ciy.avg_mnthly_cons) " +
                "from ctf_item ciy, ctf_main cmm where " +
                " cmm.ctf_main_id = ciy.ctf_main_id " +
               
                " and ciy.prod_id=" + prod +
                " and cmm.facility_id in (select ff.facility_id from facility ff " +
                " where ff.fac_type_id in (select fty.fac_type_id " +
                " from fac_type fty where fty.type_hierarchy=3)) " ;
        
        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") &&
                !mon.equals("0")) {
                   sql += 
                " and   to_char(cmm.p_date,'yyyy')='" +    year +
             "' and to_char(cmm.p_date,'mm') = '" + mon + "'" ;
        
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
        return "(" +sql+")";
    }
    
    
    public Double getClosingBalAggStockMovementsByLevelOLD(String prod, String mon, String year, String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq) {
        String sql = "select type_hierarchy,to_char(cm.p_date,'MM/yyyy') as pdate, " +
            " sum(ci.closing_bal) closingbal " +
           " from ctf_item ci, ctf_main cm, facility f, fac_type ft where \n" + 
            "ci.ctf_main_id = cm.ctf_main_id " +
            "and cm.facility_id = f.facility_id " +
            "and ft.fac_type_id = f.fac_type_id\n" + 
            
            "and ci.prod_id = " +prod+ " \n" ;
       
       
        
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql+= " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon + "'";
            }

            if (mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "'";
            }

          

        }
        if (dat.equals("u")) {
            sql +=" and cm.p_date " +
                "between to_date('" + fM + "/" + fY + "','mm/yyyy') " +
                "and to_date('" + tM + "/" +
                tY + "','mm/yyyy') ";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
                    " and to_char(ci.p_date,'Q') in ('1','2')";
            } else {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
                    "and to_char(ci.p_date,'Q') in ('3','4')";
            }
            
        }
        try {
            sql += "and type_hierarchy = " + type+ " group by type_hierarchy,cm.p_date order by cm.p_date desc";
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getDouble(3);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }

    }
    
    
    
    public Double getOpeningBalAggStockMovementsByLevelOLD(String prod, String mon, String year, String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq) {
        String sql = "select type_hierarchy,to_char(cm.p_date,'MM/yyyy') as pdate, " +
            " sum(ci.closing_bal) closingbal " +
           " from ctf_item ci, ctf_main cm, facility f, fac_type ft where \n" + 
            "ci.ctf_main_id = cm.ctf_main_id " +
            "and cm.facility_id = f.facility_id " +
            "and ft.fac_type_id = f.fac_type_id\n" + 
            
            "and ci.prod_id = " +prod+ " \n" ;
       
       
        
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql+= " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon + "'";
            }

            if (mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "'";
            }

          

        }
        if (dat.equals("u")) {
            sql +=" and cm.p_date " +
                "between to_date('" + fM + "/" + fY + "','mm/yyyy') " +
                "and to_date('" + tM + "/" +
                tY + "','mm/yyyy') ";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
                    " and to_char(ci.p_date,'Q') in ('1','2')";
            } else {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
                    "and to_char(ci.p_date,'Q') in ('3','4')";
            }
            
        }
        try {
            sql += "and type_hierarchy = " + type+ " group by type_hierarchy,cm.p_date order by cm.p_date asc";
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getDouble(3);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }

    }


    public ResultSet getAggStockMovByLevel(String prod, String mon, String year, String dat, String fY, String tY,
                                           String fM, String tM, String type, String hq, String cen[]) {


        String sql =
            "select ft.type_hierarchy as type_hierarchy, \n" +
            "count((select  mf.facility_id from " +
            "facility mf  where  mf.facility_id = f.facility_id)) as ourCount,\n" +



            "sum(receipts), sum(issues),sum(adjustments) \n" +
            "from ctf_main cm, ctf_item ci, \n" +
            "facility f, fac_type ft\n" +
            //"-- ,group_of_facilities gof, groups g\n" +
            "where f.facility_id = cm.facility_id\n" +
             "and cm.ctf_main_id = ci.ctf_main_id\n" +
            "and f.fac_type_id = ft.fac_type_id\n" +
           // "--and ft.fac_type_id = gof.gof_facility_id\n" +
           // "--and gof.gof_grp_id = g.grp_id\n" + "and f.active = 1 \n" +
            "and ci.prod_id = " + prod + " \n";


        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'" + 
                       " and to_char(cm.p_date,'mm') = '" + mon + "'";


            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'" +
                       "and to_char(cm.p_date,'Q') = '" + mon + "'";

            }
            if (mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'";

            }
        }
        if (dat.equals("u")) {
            sql +=
                " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') " + 
                "and to_date('" + tM + "/" + tY +
                "','mm/yyyy')  ";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + 
                       "' and to_char(cm.p_date,'Q') in ('1','2') ";


            } else if (hq.equals("2")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + 
                       "' and to_char(cm.p_date,'Q') in ('3','4') ";


            }
        }
        try {

            sql += "group by ft.type_hierarchy \n" + "order by 3";
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }
    
    
    public ResultSet AllFacility(String prod, String mon, String year, String dat, String fY,
                                 String tY, String fM, String tM, String type, String hq){
        String sql = "select f.facility_id , ft.fac_type_id, f.fac_name, ft.fac_name   \n" ; 
        sql += " , fac_code, fac_contact, fac_phone ";
        sql += "from  facility f, fac_type ft  \n" ;
        sql += "where ft.fac_type_id = f.fac_type_id\n" ;
        sql += "and f.facility_id in \n" ;
        sql += "(\n" ;
        sql += "select \n" ;
        sql += "f.facility_id \n" ; 
        sql += "from ctf_main cm, ctf_item ci, facility f, fac_type ft \n" ;
        sql += "where ft.fac_type_id = f.fac_type_id\n" ;
        sql += "and   f.facility_id = cm.facility_id\n" ;
        sql += "and cm.ctf_main_id = ci.ctf_main_id\n" ;
        sql += "and ci.prod_id = " + prod + "   \n" ;
                     
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql+= " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }

                
                if (dat.equals("q") && !mon.equals("0")) {
                                if (mon.equals("1")) {
                                   // sql+=  "and cm.p_date between to_date('"+mon+"/'"+year+"','MM/yyyy')='03'"  ;
                                   sql+=  "and cm.p_date between to_date('01/"+year+"','MM/yyyy') and to_date('03/"+year+"','MM/yyyy')"  ;
                                }
                                if (mon.equals("2")) {
                                    sql+=  "and cm.p_date between to_date('04"+year+"','MM/yyyy') and to_date('06/"+year+"','MM/yyyy')"  ;
                                }
                                if (mon.equals("3")) {
                                    sql+=  "and cm.p_date between to_date('07/"+year+"','MM/yyyy') and to_date('09/"+year+"','MM/yyyy')"  ;
                                }
                                if (mon.equals("4")) {
                                    sql+=  "and cm.p_date between to_date('10/"+year+"','MM/yyyy') and to_date('12/"+year+"','MM/yyyy')"  ;
                                }
                }
                


            if (mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "'";
            }

          

        }
        if (dat.equals("u")) {
            sql += " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') ";
            sql += "and to_date('" + tM + "/" + tY + "','mm/yyyy')";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql += " and cm.p_date between to_date('01/" + fY + "','mm/yyyy') ";
                sql += "and to_date('06/" + tY + "','mm/yyyy')";
            } else {
                sql += " and cm.p_date between to_date('07/" + fY + "','mm/yyyy') ";
                sql += "and to_date('12/" + tY + "','mm/yyyy')";
            } 
        }             
        sql += ") \n" ;
        sql += "-- start with sup_code is null ";
        sql += "-- connect by prior facility_id = sup_code ";
      try {
          System.out.println(sql);
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
    
    public static Connection getLogisticsConnection() throws SQLException, NamingException {
        String username = MainInterface.username;
        String password = MainInterface.password;
        String thinConn = MainInterface.thinConn;
       // DriverManager.registerDriver(new OracleDriver());
        //Connection conn = DriverManager.getConnection(thinConn, username, password);
        ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml", LogisticsReportsClass.class);
        DataSource dataSource = JDBCConfig.dataSource();
//        Context ctx = new InitialContext();
//        DataSource ds = (DataSource) ctx.lookup("jdbc/SequeLink");
       Connection conn = dataSource.getConnection();

        return conn;
    }
    
    
    public boolean StockOutFacilityOneProduct(String facility, String prod, String mon, String year, String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        boolean condition = false;
        

        double avg= 0.0;
        double closeBal = 0.0;
        double  openBal = 0.0;
        double mos = 0.0;



        
       
        String sql = "select \n" ;
                sql += "cm.p_Date, f.facility_id, ";
                sql += " ci.prod_id, ci.adjustments , ci.adj_type_id , adj.type_name   \n" ;
                sql += ", nvl(closing_bal,0), nvl(avg_mnthly_cons,0), decode(nvl(avg_mnthly_cons,0), 0, 0, Round(closing_bal / avg_mnthly_cons, 2) ) as mos ";
                sql += "from ctf_main cm, ctf_item ci, facility f, fac_type ft, adj_type adj \n" ;
                sql += "where ft.fac_type_id = f.fac_type_id\n" ;
                sql += "and   f.facility_id = cm.facility_id\n" ;
                sql += "and cm.ctf_main_id = ci.ctf_main_id\n" ;
                sql += "and nvl(ci.adj_type_id,0) = adj.adj_type_id(+) \n"  ;
                sql += "and f.facility_id = " + facility;
                sql += " and ci.prod_id = " + prod + "  \n" ;
       
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql+= " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }

                
                if (dat.equals("q") && !mon.equals("0")) {
                                if (mon.equals("1")) {
                                   // sql+=  "and cm.p_date between to_date('"+mon+"/'"+year+"','MM/yyyy')='03'"  ;
                                   sql+=  "and cm.p_date between to_date('01/"+year+"','MM/yyyy') and to_date('03/"+year+"','MM/yyyy')"  ;
                                }
                                if (mon.equals("2")) {
                                    sql+=  "and cm.p_date between to_date('04/"+year+"','MM/yyyy') and to_date('06/"+year+"','MM/yyyy')"  ;
                                }
                                if (mon.equals("3")) {
                                    sql+=  "and cm.p_date between to_date('07/"+year+"','MM/yyyy') and to_date('09/"+year+"','MM/yyyy')"  ;
                                }
                                if (mon.equals("4")) {
                                    sql+=  "and cm.p_date between to_date('10/"+year+"','MM/yyyy') and to_date('12/"+year+"','MM/yyyy')"  ;
                                }
                }
                


            if (mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "'";
            }

          

        }
        if (dat.equals("u")) {
            sql += " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') ";
            sql += "and to_date('" + tM + "/" + tY + "','mm/yyyy')";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql += " and cm.p_date between to_date('01/" + fY + "','mm/yyyy') ";
                sql += "and to_date('06/" + tY + "','mm/yyyy')";
            } else {
                sql += " and cm.p_date between to_date('07/" + fY + "','mm/yyyy') ";
                sql += "and to_date('12/" + tY + "','mm/yyyy')";
            } 
        }  

        
       try {
           System.out.println(">>>XXXXXXXXXXXXXXXXXXXXXXX StockOutFacilityOneProduct XXXXXXXXX**************************************");
           System.out.println(sql);
           getDBConnection();
           stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                          ResultSet.CONCUR_READ_ONLY);
           rs = stmt.executeQuery(sql); 
           
           if(rs.next()){
                 avg = Double.valueOf(rs.getString(8));
                 closeBal = Double.valueOf(rs.getString(7));
                 mos = Double.valueOf(rs.getString(9));
                 int adjustments = rs.getInt(4);
               int adjType = rs.getInt(5);
               
               if(  Double.valueOf(closeBal).intValue() == 0 
                                  && avg >= 0
                                  && adjustments > 0 &&
                                  adjType != 12 ){
                                  condition = true;
                                   
                             }
               System.out.println("######################## STOCK OUT FACILITY ##############################");
           }

          
       } catch (Exception e) {
           // TODO: Add catch code
           e.printStackTrace();
          
       }finally{
           closeRs(rs);
           closePst(pst);
          // s();
       }
       
       return condition;
    }
    
    
    public double StockOutFacilityOneProductAVG(String facility, String prod, String mon, String year, String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq) {
        ResultSet rs = null;
        PreparedStatement pst = null;
        boolean condition = false;
        

        double avg= 0.0;
        double closeBal = 0.0;
        double  openBal = 0.0;
        double mos = 0.0;



        
       
        String sql = "select \n" ;
                sql += "cm.p_Date, f.facility_id, ";
                sql += " ci.prod_id, ci.adjustments , nvl(ci.adj_type_id,0), nvl(adj.type_name, '')   \n" ;
                sql += ", nvl(closing_bal,0), nvl(avg_mnthly_cons,0), decode(nvl(avg_mnthly_cons,0), 0, 0, Round(closing_bal/ avg_mnthly_cons, 2) ) as mos ";
                sql += "from ctf_main cm, ctf_item ci, facility f, fac_type ft, adj_type adj \n" ;
                sql += "where ft.fac_type_id = f.fac_type_id\n" ;
                sql += "and   f.facility_id = cm.facility_id\n" ;
                sql += "and cm.ctf_main_id = ci.ctf_main_id\n" ;
                sql += "and nvl(ci.adj_type_id,0) = adj.adj_type_id(+) \n"  ;
                sql += "and f.facility_id = " + facility;
                sql += " and ci.prod_id = " + prod + "  \n" ;
       
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql+= " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }

                
                if (dat.equals("q") && !mon.equals("0")) {
                                if (mon.equals("1")) {
                                   // sql+=  "and cm.p_date between to_date('"+mon+"/'"+year+"','MM/yyyy')='03'"  ;
                                   sql+=  "and cm.p_date between to_date('01/"+year+"','MM/yyyy') and to_date('03/"+year+"','MM/yyyy')"  ;
                                }
                                if (mon.equals("2")) {
                                    sql+=  "and cm.p_date between to_date('04/"+year+"','MM/yyyy') and to_date('06/"+year+"','MM/yyyy')"  ;
                                }
                                if (mon.equals("3")) {
                                    sql+=  "and cm.p_date between to_date('07/"+year+"','MM/yyyy') and to_date('09/"+year+"','MM/yyyy')"  ;
                                }
                                if (mon.equals("4")) {
                                    sql+=  "and cm.p_date between to_date('10/"+year+"','MM/yyyy') and to_date('12/"+year+"','MM/yyyy')"  ;
                                }
                }
                


            if (mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "'";
            }

          

        }
        if (dat.equals("u")) {
            sql += " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') ";
            sql += "and to_date('" + tM + "/" + tY + "','mm/yyyy')";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql += " and cm.p_date between to_date('01/" + fY + "','mm/yyyy') ";
                sql += "and to_date('06/" + tY + "','mm/yyyy')";
            } else {
                sql += " and cm.p_date between to_date('07/" + fY + "','mm/yyyy') ";
                sql += "and to_date('12/" + tY + "','mm/yyyy')";
            } 
        }  

        
       try {
          
           System.out.println(">>> StockOutFacilityOneProductAVG **************************************");
           System.out.println(sql);
           getDBConnection();
           stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                          ResultSet.CONCUR_READ_ONLY);
           rs = stmt.executeQuery(sql); 
           if(rs.next()){
                 closeBal = rs.getDouble(7);
                 mos = rs.getDouble(9);
                 int adjustments = rs.getInt(4);
               int adjType = rs.getInt(5);
               
               if(  Double.valueOf(closeBal).intValue() == 0 
                                  && avg >= 0
                                  && adjustments > 0 &&
                                  adjType != 12 ){
                                  condition = true;
                                   
                             }
               System.out.println("######################## STOCK OUT FACILITY ##############################");
           }
            
          
       } catch (Exception e) {
           // TODO: Add catch code
           e.printStackTrace();
          
       }finally{
           closeRs(rs);
           closePst(pst);
       }
       
       return avg;
    }
    
    
    public ResultSet OpenCloseAvgMosData(String facility, String prod, String mon, String year, String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq) {


       String avg = " AVGMONVAL('"+ facility+"', '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"') " ;
       String closeBal = " CLOSEBALVAL('"+ facility+"', '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"')  ";
       String openBal = " OPENBALVAL('"+ facility+"', '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"')  ";
        String sql = "Select ";
        sql +=  avg + ", \n";
        sql += openBal + ", \n";
        sql += closeBal + ", \n";
        sql +=  " decode ( "+avg+", 0 , 0 , " + closeBal + " / "+avg+" ) as mos \n" ;
        sql += " FROM DUAL ";
        
       try {
           System.out.println(sql);
           getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//
           rs = stmt.executeQuery(sql);
//           rs = dbConn.selectDB(sql);
      
         
          
       } catch (Exception e) {
           // TODO: Add catch code
           e.printStackTrace();
          
       }
        return rs;
   }
    
                                           
    public Double getClosingBalAggStockMovementsByLevel(String prod, String mon, String year, String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq, int colInd) {
        
        String avg = " AVGMONVAL('lvl', '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"') ";
        String closeBal = " CLOSEBALVAL('lvl', '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"') ";
        String sql =     
        " select  distinct " + closeBal + " cbal,"+avg+" as avg," +
        " decode ( "+avg+", 0 , 0 , " + closeBal + " / "+avg+" ) as mos, " +
        "cm.p_date  \n" + 
        //"-- ,ROW_NUMBER() OVER (ORDER BY last_updated ASC) row_id \n" + 
        "from ctf_item ci, ctf_main cm , facility ff, fac_type fft \n" + 
        "where ci.ctf_main_id = cm.ctf_main_id \n" + 
        "and ff.facility_id = cm.facility_id \n" + 
        "and fft.fac_type_id = ff.fac_type_id \n" + 
        "and fft.type_hierarchy = "+type+" \n " +
        "and ci.prod_id = "+prod+" \n" +
        "-- and ff.active = 1 \n" ;
   
       
        
            if (dat.equals("m") || dat.equals("q")) {

                if (dat.equals("m") && !mon.equals("0")) {
                    System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    sql+= " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
                }

                    
                    if (dat.equals("q") && !mon.equals("0")) {
                                    
                                    if (mon.equals("1")) {
                                        sql+=  "and to_char(cm.p_date,'mm')='03'"  ;
                                    }
                                    if (mon.equals("2")) {
                                        sql+=  "and to_char(cm.p_date,'mm')='06'";
                                    }
                                    if (mon.equals("3")) {
                                       sql+= " and to_char(cm.p_date,'mm')='09'";
                                    }
                                    if (mon.equals("4")) {
                                        sql+=  "and to_char(cm.p_date,'mm')='12'";
                                    }
                                    sql +=  " and to_char(cm.p_date,'yyyy')='" + year + "'";
                    }
                    


                if (mon.equals("0")) {
                    System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    sql+=  "and to_char(cm.p_date,'mm')='12'"  ;
                    sql +=" and to_char(cm.p_date,'yyyy')='" + year + "'";
                }

              

            }
            if (dat.equals("u")) {
            //            sql +=" and cm.p_date " +
            //                "between to_date('" + fM + "/" + fY + "','mm/yyyy') " +
            //                "and to_date('" + tM + "/" +
            //                tY + "','mm/yyyy') ";
                sql += " and cm.p_date " + " = to_date('" + tM + "/" + tY + "','mm/yyyy') ";

            }
            if (dat.equals("hq")) {
                if (hq.equals("1")) {
            //                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
            //                    " and to_char(cm.p_date,'Q') in ('1','2')";
                     sql+=  "and to_char(cm.p_date,'mm')='06'"  ;
                } else {
            //                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
            //                    "and to_char(cm.p_date,'Q') in ('3','4')";
                    sql+=  "and to_char(cm.p_date,'mm')='12'"  ;
                }
                
                sql +=  " and to_char(cm.p_date,'yyyy')='" + year + "'";
                
            }
        try {
            sql +=     " group by cm.p_date \n" +
                    "order by cm.p_date desc"  ;
       
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if(rs.next()){
            if(colInd == 1){
                return rs.getDouble(1);
            }else             if(colInd == 2){
                return rs.getDouble(2);
            }else{
                return rs.getDouble(3);
            }
            }else{
                return 0d;
            }
            
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0d;
        }

    }
    
    
    public Double getSumClosingBalAggStockMovementsByLevel(String prod, String mon, String year, String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq) {
        String sql =     
            " select   sum(ci.closing_bal) cbal " +
   // "cm.p_date  \n" + 
    //"-- ,ROW_NUMBER() OVER (ORDER BY last_updated ASC) row_id \n" + 
    "from ctf_item ci, ctf_main cm , facility ff, fac_type fft \n" + 
    "where ci.ctf_main_id = cm.ctf_main_id \n" + 
    "and ff.facility_id = cm.facility_id \n" + 
    "and fft.fac_type_id = ff.fac_type_id \n" + 
    //"and fft.type_hierarchy = "+type+" \n " +
    "and ci.prod_id = "+prod+" \n" +
    "--and ff.active = 1 \n" ;
    
       
        
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql+= " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }

                
                if (dat.equals("q") && !mon.equals("0")) {
                                
                                if (mon.equals("1")) {
                                    sql+=  "and to_char(cm.p_date,'mm')='03'"  ;
                                }
                                if (mon.equals("2")) {
                                    sql+=  "and to_char(cm.p_date,'mm')='06'";
                                }
                                if (mon.equals("3")) {
                                   sql+= " and to_char(cm.p_date,'mm')='09'";
                                }
                                if (mon.equals("4")) {
                                    sql+=  "and to_char(cm.p_date,'mm')='12'";
                                }
                                sql +=  " and to_char(cm.p_date,'yyyy')='" + year + "'";
                }
                


            if (mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql+=  "and to_char(cm.p_date,'mm')='12'"  ;
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "'";
            }

          

        }
        if (dat.equals("u")) {
//            sql +=" and cm.p_date " +
//                "between to_date('" + fM + "/" + fY + "','mm/yyyy') " +
//                "and to_date('" + tM + "/" +
//                tY + "','mm/yyyy') ";
            sql += " and cm.p_date " + " = to_date('" + tM + "/" + tY + "','mm/yyyy') ";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
//                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
//                    " and to_char(cm.p_date,'Q') in ('1','2')";
                 sql+=  "and to_char(cm.p_date,'mm')='06'"  ;
            } else {
//                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
//                    "and to_char(cm.p_date,'Q') in ('3','4')";
                sql+=  "and to_char(cm.p_date,'mm')='12'"  ;
            }
            
            sql +=  " and to_char(cm.p_date,'yyyy')='" + year + "'";
            
        }
        try {
//            sql +=     " group by cm.p_date \n" +
//                    "order by cm.p_date desc"  ;
       
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }

    }
    
    
    
    public Double getOpeningBalAggStockMovementsByLevel(String prod, String mon, String year, String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq) {
       
        String sql =     " select  distinct sum(ci.Open_bal) obal, " +
    "cm.p_date  \n" + 
    //"-- ,ROW_NUMBER() OVER (ORDER BY last_updated ASC) row_id \n" + 
    "from ctf_item ci, ctf_main cm , facility ff, fac_type fft \n" + 
    "where ci.ctf_main_id = cm.ctf_main_id \n" + 
    "and ff.facility_id = cm.facility_id \n" + 
    "and fft.fac_type_id = ff.fac_type_id \n" + 
    "and fft.type_hierarchy = "+type+" \n " +
    "and ci.prod_id = "+prod+" \n" +
    "--and ff.active = 1 \n" ;
    
       
        
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql+= " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon + "'";
            }

            if (mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "'";
            }

          

        }
        if (dat.equals("u")) {
            sql +=" and cm.p_date " +
                "between to_date('" + fM + "/" + fY + "','mm/yyyy') " +
                "and to_date('" + tM + "/" +
                tY + "','mm/yyyy') ";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
                    " and to_char(cm.p_date,'Q') in ('1','2')";
            } else {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
                    "and to_char(cm.p_date,'Q') in ('3','4')";
            }
            
        }
        try {
            sql +=     " group by cm.p_date \n" +
                    "order by cm.p_date asc"  ;
            sql = "select  OPENBALVAL('lvl', '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"') from dual ";
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if(rs.next()){
            return rs.getDouble(1);
            }else{
                return 0d;
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0d;
        }

    }
    
    
    public Double getSumOpeningBalAggStockMovementsByLevel(String prod, String mon, String year, String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq) {
        String sql =     " select   sum(ci.Open_bal) obal " +
   
    //"-- ,ROW_NUMBER() OVER (ORDER BY last_updated ASC) row_id \n" + 
    "from ctf_item ci, ctf_main cm , facility ff, fac_type fft \n" + 
    "where ci.ctf_main_id = cm.ctf_main_id \n" + 
    "and ff.facility_id = cm.facility_id \n" + 
    "and fft.fac_type_id = ff.fac_type_id \n" + 
    "and fft.type_hierarchy = "+type+" \n " +
    "and ci.prod_id = "+prod+" \n" +
    "and ff.active = 1 \n" ;
    
       
        
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql+= " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon + "'";
            }

            if (mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "'";
            }

          

        }
        if (dat.equals("u")) {
            sql +=" and cm.p_date " +
                "between to_date('" + fM + "/" + fY + "','mm/yyyy') " +
                "and to_date('" + tM + "/" +
                tY + "','mm/yyyy') ";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
                    " and to_char(cm.p_date,'Q') in ('1','2')";
            } else {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
                    "and to_char(cm.p_date,'Q') in ('3','4')";
            }
            
        }
        try {
//            sql +=     " group by cm.p_date \n" +
//                    "order by cm.p_date asc"  ;
       
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getDouble(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0.010;
        }

    }
    
    public ResultSet getAggStockMovements(String prod, String mon, String year, String dat, String fY, String tY,
                                          String fM, String tM, String type, String hq) {
       
        String sql =
            "select nvl(ft.fac_name, ' '), nvl(ft.fac_type_id, 0 ) as type, \n" +
            "(select count(*)  from " +
            "facility mf, fac_type mft  where " +
            " mf.fac_type_id = mft.fac_type_id (+) \n" +
            " and mft.fac_type_id =ft.fac_type_id  \n" +
            "  ) as ourCount,\n" +



            "sum(receipts), sum(issues),sum(adjustments) , ft.type_hierarchy \n" +
            "from ctf_main cm, ctf_item ci, \n" +
            "facility f, fac_type ft\n" +
            //"-- ,group_of_facilities gof, groups g\n" +
            "where f.facility_id (+) = cm.facility_id\n" +
             "and cm.ctf_main_id (+) = ci.ctf_main_id\n" +
            "and f.fac_type_id = ft.fac_type_id (+) \n" +
           // "--and ft.fac_type_id = gof.gof_facility_id\n" +
           // "--and gof.gof_grp_id = g.grp_id\n" + "and f.active = 1 \n" +
            "and ci.prod_id = " + prod + " \n";


        if (dat.equals("m") || dat.equals("q")) {
            if (dat.equals("m") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/"  + year + "'" ;
                       //" and to_char(cm.p_date,'mm') = ;


            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'" +
                       "and to_char(cm.p_date,'Q') = '" + mon + "'";

            }
            if (mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'";

            }
        }
        if (dat.equals("u")) {
            sql +=
                " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') " + 
                "and to_date('" + tM + "/" + tY +
                "','mm/yyyy')  ";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + 
                       "' and to_char(cm.p_date,'Q') in ('1','2') ";


            } else if (hq.equals("2")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + 
                       "' and to_char(cm.p_date,'Q') in ('3','4') ";


            }
        }
        try {

            sql += " group by ft.fac_name, ft.fac_type_id, ft.type_hierarchy \n" + "order by 1";
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;
    }



// agg stock movement rep querys
    
    public Double getClosingBalAggStockMovementsRep(String prod, String mon, String year, String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq, int colInd) {
        String avg = " AVGMONVAL('type', '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"') ";
        String closeBal = " CLOSEBALVAL('type', '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"') ";
        String sql =     " select  " + closeBal + " cbal, " + avg + " as avg," +
            " decode ( " + avg + ", 0 , 0 , " + closeBal + " / "+avg+" ) as mos, " +
    "cm.p_date  \n" + 
    //"-- ,ROW_NUMBER() OVER (ORDER BY last_updated ASC) row_id \n" + 
    "from ctf_item ci, ctf_main cm , facility ff, fac_type fft \n" + 
    "where ci.ctf_main_id = cm.ctf_main_id \n" + 
    "and ff.facility_id = cm.facility_id \n" + 
    "and fft.fac_type_id = ff.fac_type_id \n" + 
    "and fft.fac_type_id = "+type+" \n " +
    "and ci.prod_id = "+prod+" \n" +
    "-- and ff.active = 1 \n" ;
   
       
        
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql+= " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }

                
                if (dat.equals("q") && !mon.equals("0")) {
                                
                                if (mon.equals("1")) {
                                    sql+=  "and to_char(cm.p_date,'mm')='03'"  ;
                                }
                                if (mon.equals("2")) {
                                    sql+=  "and to_char(cm.p_date,'mm')='06'";
                                }
                                if (mon.equals("3")) {
                                   sql+= " and to_char(cm.p_date,'mm')='09'";
                                }
                                if (mon.equals("4")) {
                                    sql+=  "and to_char(cm.p_date,'mm')='12'";
                                }
                                sql +=  " and to_char(cm.p_date,'yyyy')='" + year + "'";
                }
                


            if (mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql+=  "and to_char(cm.p_date,'mm')='12'"  ;
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "'";
            }

          

        }
        if (dat.equals("u")) {
            sql += " and cm.p_date " + " = to_date('" + tM + "/" + tY + "','mm/yyyy') ";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                 sql+=  "and to_char(cm.p_date,'mm')='06'"  ;
            } else {
                      sql+=  "and to_char(cm.p_date,'mm')='12'"  ;
            }
            
            sql +=  " and to_char(cm.p_date,'yyyy')='" + year + "'";
            
        }
        try {
            sql +=     " group by cm.p_date \n" +
                    "order by cm.p_date desc"  ;
            getDBConnection(); 
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if(rs.next()){
            if(colInd == 1){
                return rs.getDouble(1);
            }else if(colInd == 2){
                    return rs.getDouble(2);
            }else{
                return rs.getDouble(3);
            }
            }else{
                return 0d;
            }
            
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0d;
        }

    }
    
    
   
    
    
    
    public Double getOpeningBalAggStockMovementsRep(String prod, String mon, String year, String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq) {
        String sql =     " select  distinct sum(ci.Open_bal) obal, " +
    "cm.p_date  \n" + 
    //"-- ,ROW_NUMBER() OVER (ORDER BY last_updated ASC) row_id \n" + 
    "from ctf_item ci, ctf_main cm , facility ff, fac_type fft \n" + 
    "where ci.ctf_main_id = cm.ctf_main_id \n" + 
    "and ff.facility_id = cm.facility_id \n" + 
    "and fft.fac_type_id = ff.fac_type_id \n" + 
    "and fft.fac_type_id = "+type+" \n " +
    "and ci.prod_id = "+prod+" \n" +
    "-- and ff.active = 1 \n" ;
    
       
        
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql+= " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon + "'";
            }

            if (mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "'";
            }

          

        }
        if (dat.equals("u")) {
            sql +=" and cm.p_date " +
                "between to_date('" + fM + "/" + fY + "','mm/yyyy') " +
                "and to_date('" + tM + "/" +
                tY + "','mm/yyyy') ";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
                    " and to_char(cm.p_date,'Q') in ('1','2')";
            } else {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
                    "and to_char(cm.p_date,'Q') in ('3','4')";
            }
            
        }
        try {
            sql +=     " group by cm.p_date \n" +
                    "order by cm.p_date asc"  ;
            sql = "select  OPENBALVAL('type', '"+ mon+"', '"+year+"', '"+prod+"', '"+dat+"', '"+fY+"', '"+tY+"', '"+fM+"', '"+tM+"', '"+type+"', '"+hq+"') from dual ";
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if(rs.next()){
            return rs.getDouble(1);
            }else{
                return 0d;
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0d;
        }

    }
    
    
    
    
    
    public Double getClosingBalAggStockMovementsRepInd(String prod, String mon, String year, String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq, int colInd) {
        String sql =     " select  distinct sum(ci.closing_bal) cbal,sum(avg_mnthly_cons) as avg," +
            " decode ( sum(avg_mnthly_cons), 0 , 0 , sum(ci.closing_bal)/ sum(avg_mnthly_cons) ) as mos, " +
    "cm.p_date  \n" + 
    //"-- ,ROW_NUMBER() OVER (ORDER BY last_updated ASC) row_id \n" + 
    "from ctf_item ci, ctf_main cm , facility ff, fac_type fft \n" + 
    "where ci.ctf_main_id = cm.ctf_main_id \n" + 
    "and ff.facility_id = cm.facility_id \n" + 
    "and fft.fac_type_id = ff.fac_type_id \n" + 
    "and ff.facility_id = "+type+" \n " +
    "and ci.prod_id = "+prod+" \n" +
    "-- and ff.active = 1 \n" ;
    
       
        
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql+= " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon + "'";
            }

            if (mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "'";
            }

          

        }
        if (dat.equals("u")) {
            sql +=" and cm.p_date " +
                "between to_date('" + fM + "/" + fY + "','mm/yyyy') " +
                "and to_date('" + tM + "/" +
                tY + "','mm/yyyy') ";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
                    " and to_char(cm.p_date,'Q') in ('1','2')";
            } else {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
                    "and to_char(cm.p_date,'Q') in ('3','4')";
            }
            
        }
        try {
            sql +=     " group by cm.p_date \n" +
                    "order by cm.p_date desc"  ;
       
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if(rs.next()){
            if(colInd == 1){
                return rs.getDouble(1);
            }else{
                return rs.getDouble(3);
            }
            }else{
                return 0d;
            }
            
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0d;
        }

    }
    
    
    
    
    
    
    public Double getOpeningBalAggStockMovementsRepInd(String prod, String mon, String year, String dat, String fY,
                                                        String tY, String fM, String tM, String type, String hq) {
        String sql =     " select  distinct sum(ci.Open_bal) obal, " +
    "cm.p_date  \n" + 
    //"-- ,ROW_NUMBER() OVER (ORDER BY last_updated ASC) row_id \n" + 
    "from ctf_item ci, ctf_main cm , facility ff, fac_type fft \n" + 
    "where ci.ctf_main_id = cm.ctf_main_id \n" + 
    "and ff.facility_id = cm.facility_id \n" + 
    "and fft.fac_type_id = ff.fac_type_id \n" + 
    "and ff.facility_id = "+type+" \n " +
    "and ci.prod_id = "+prod+" \n" +
    "-- and ff.active = 1 \n" ;
    
       
        
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql+= " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }
            if (dat.equals("q") && !mon.equals("0")) {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' and to_char(cm.p_date,'Q')='" + mon + "'";
            }

            if (mon.equals("0")) {
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "'";
            }

          

        }
        if (dat.equals("u")) {
            sql +=" and cm.p_date " +
                "between to_date('" + fM + "/" + fY + "','mm/yyyy') " +
                "and to_date('" + tM + "/" +
                tY + "','mm/yyyy') ";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
                    " and to_char(cm.p_date,'Q') in ('1','2')";
            } else {
                sql +=" and to_char(cm.p_date,'yyyy')='" + year + "' " +
                    "and to_char(cm.p_date,'Q') in ('3','4')";
            }
            
        }
        try {
            sql +=     " group by cm.p_date \n" +
                    "order by cm.p_date asc"  ;
       
            getDBConnection(); stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if(rs.next()){
            return rs.getDouble(1);
            }else{
                return 0d;
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return 0d;
        }

    }
    
    
    
    public static void main(String[] args) {
      //  Test test = new Test();
        
          //ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml", Test.class);

          DataSource dataSource = JDBCConfig.dataSource();
          // DataSource mysqlDataSource = (DataSource) ac.getBean("mysqlDataSource");

          JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
          System.out.println(jdbcTemplate.queryForList("select * from product").size());
          List<Map<String, Object>> results = jdbcTemplate.queryForList("select * from product");
          LogisticsReportsClass c = new LogisticsReportsClass();
          System.out.println(c.getFacilitiesByLevelSTMT(2,new String[]{"3"},"a"));
            for(Map row : results){
                System.out.println(row.get("prod_id"));
                System.out.println(row.get("pro_name"));
                System.out.println(row.get("prod_cyp"));
                System.out.println(row.get("pro_dose"));
                
            }

       
    
    }
    
}

