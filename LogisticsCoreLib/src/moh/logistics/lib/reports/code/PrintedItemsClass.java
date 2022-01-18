package moh.logistics.lib.reports.code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import moh.logistics.lib.reports.UtilsClass;

public class PrintedItemsClass extends UtilsClass {
    public PrintedItemsClass() {
        super();
    }
    
    

    public static ResultSet getPrintedRepItems(String mainId) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
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
            
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }

    
    public static String getDate() {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "select to_char(sysdate,'dd-MM-yyyy') from dual ";
        try {
            conn = jdbcConnection();
            pst  = conn.prepareStatement(sql);
            rs = pst.executeQuery(sql);
            rs.next();
            return rs.getString(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
            return "non";
        }finally{
            try {
                closeConnectionList(conn, rs, pst);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static ResultSet getPrintedRepMain(String mainId) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pst = null;
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
            conn = jdbcConnection();
            pst = conn.prepareStatement(sql);
            //  System.out.println(sql);
            rs = pst.executeQuery(sql);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        return rs;

    }

}
