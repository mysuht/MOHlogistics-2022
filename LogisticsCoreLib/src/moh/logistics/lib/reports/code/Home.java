package moh.logistics.lib.reports.code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;

import moh.logistics.lib.common.DBUtil;
import moh.logistics.lib.reports.UtilsClass;

public class Home extends UtilsClass {
    public Home() {
        super();
    }

    public static void main(String[] args) {
        //System.out.println(" MAX REPORTED YEAR IS : " + maxReportedYear());
    }

    public static ResultSet getProducts() {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "select prod_id, pro_name, pro_dose from product order by 2";
        try {
            conn = DBUtil.getDataSource().getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery(sql);

        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        } finally {
//            DBUtil.cleanDS(pst, conn);
        }
        return rs;
    }
    
    public static List<Map<String, Object>> getProductList() {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "select prod_id, pro_name, pro_dose from product order by 2";
        try {
            conn = DBUtil.getDataSource().getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            rows = DBUtil.resultSetToList(rs);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        } finally {
            DBUtil.cleanDS(pst, rs, conn);
        }
        return rows;
    }

    public static List<FacilityTemplate> getFacilityByTypeId(String sup) {
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
            conn = DBUtil.getDataSource().getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery(sqlCount);
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println(" More Than 0");
                sql =
                    "select facility_id , nvl(fac_name,' '), fac_code " + "from facility where fac_type_id in (" + sup +
                    ") order by 2 ";

                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery(sql);
                while (rs.next()) {
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
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery(sql);
                while (rs.next()) {
                    FacilityTemplate template = new FacilityTemplate();
                    template.setFacilityId(rs.getLong(1));
                    template.setFacilityName(rs.getString(2));
                    template.setFacCode(rs.getString(3));
                    result.add(template);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally { 
                DBUtil.cleanDS(pst, rs, conn);
        
        }
        System.out.println(sql);
        return result;

    }

    public static int maxReportedYear() {
        int result = 0;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "SELECT MAX(AM_YEAR) MAX_YEAR FROM ACTIVE_MONTHS";
//            "select to_char(cmm.p_date,'yyyy')\n" + "from\n" + "(select max(cm.ctf_main_id) as max_ctf_main\n" +
//            "from ctf_main cm, ctf_item ci\n" + "where\n" + "cm.ctf_main_id = ci.ctf_main_id)  ctf_main_x\n" +
//            ", ctf_main cmm \n" + "where cmm.ctf_main_id = ctf_main_x.max_ctf_main";
        try {
            conn = DBUtil.getDataSource().getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        } finally {
            DBUtil.cleanDS(pst, rs, conn);
        }
        return result;
    }

    public static List<Report> getReportsNames() {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "select rep_id, rep_name from reports";
        List<Report> list = new ArrayList();
        try {
            conn = DBUtil.getDataSource().getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while (rs.next()) {
                Report report = new Report();
                report.setReportID(rs.getInt(1));
                report.setReportName(rs.getString(2));
                list.add(report);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.cleanDS(pst, rs, conn);
        }
        return list;

    }

    public static List<FacilityTemplate> getFacTypesMain1() {
        List<FacilityTemplate> result = new ArrayList();
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql =
            "(select fac_type_id as facTypeId, fac_name as facilityName from fac_type ) union (select grp_id+100 , grp_desc from groups ) order by 2";
        try {
            conn = DBUtil.getDataSource().getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            while (rs.next()) {
                FacilityTemplate template = new FacilityTemplate();
                template.setFacilityId(rs.getLong("facTypeId"));
                template.setFacilityName(rs.getString("facilityName"));
                result.add(template);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.cleanDS(pst, rs, conn);
        }
        return result;

    }
}
