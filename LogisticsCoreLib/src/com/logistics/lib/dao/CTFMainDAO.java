package com.logistics.lib.dao;

import com.logistics.lib.dto.CTFMain;
import com.logistics.lib.util.DataAccessObject;

import com.logistics.lib.util.DataTransferObject;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CTFMainDAO extends DataAccessObject <CTFMain> {

    static final String GET_ONE 
        = " SELECT * FROM CTF_MAIN CM WHERE CM.CTF_MAIN_ID = ? ";
    static final String FACILITY_DATES = "SELECT * FROM CTF_MAIN CM WHERE CM.FACILITY_ID = ? ";
    public CTFMainDAO(Connection connection){
        super(connection);
    }
    @Override
    protected int getLastVal(String sequence) {
        // TODO Implement this method
        return super.getLastVal(sequence);
    }
    @Override
    public CTFMain findById(long id) {
        // TODO Implement this method
        return null;
    }

    @Override
    public List findAll() {
        // TODO Implement this method
        return Collections.emptyList();
    }
    
    public static CTFMain fillCTFMain(ResultSet rs) throws SQLException{
        CTFMain ctfMain = new CTFMain();
        ctfMain.setId(rs.getLong("CTF_MAIN_ID"));
        ctfMain.setFacilityId(rs.getLong("FACILITY_ID"));
        ctfMain.setCtfComments(rs.getString("CTF_COMMENTS"));
        return ctfMain;
    }
    
    
    
    public List<CTFMain> getFacilityDates(String mon, String year, String dat, String fY, String tY, String fM, String tM,
                                     String hq, Long facility) {
       String sql = FACILITY_DATES;
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }


            if (dat.equals("q") && !mon.equals("0")) {
                if (mon.equals("1")) {
                    sql +=
                        "and cm.p_date between to_date('01/" + year + "','MM/yyyy') and to_date('03/" + year +
                        "','MM/yyyy')";
                }
                if (mon.equals("2")) {
                    sql +=
                        "and cm.p_date between to_date('04" + year + "','MM/yyyy') and to_date('06/" + year +
                        "','MM/yyyy')";
                }
                if (mon.equals("3")) {
                    sql +=
                        "and cm.p_date between to_date('07/" + year + "','MM/yyyy') and to_date('09/" + year +
                        "','MM/yyyy')";
                }
                if (mon.equals("4")) {
                    sql +=
                        "and cm.p_date between to_date('10/" + year + "','MM/yyyy') and to_date('12/" + year +
                        "','MM/yyyy')";
                }
            }


            if (mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'";
            }


        }
        if (dat.equals("u")) {
            sql += " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') ";
            sql += "and to_date('" + tM + "/" + tY + "','mm/yyyy')";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql += " and cm.p_date between to_date('01/" + year + "','mm/yyyy') ";
                sql += "and to_date('06/" + year + "','mm/yyyy')";
            } else {
                sql += " and cm.p_date between to_date('07/" + year + "','mm/yyyy') ";
                sql += "and to_date('12/" + year + "','mm/yyyy')";
            }
        }
        
        List<CTFMain> ctfMainList = new ArrayList<>();
        try {
            PreparedStatement pst = null;
            ResultSet rs = null;
            try {
                pst = this.connection.prepareStatement(sql);
                pst.setLong(1, facility);
                rs = pst.executeQuery();              
                while (rs.next()) {
                    CTFMain ctfMain = fillCTFMain(rs);
                    ctfMainList.add(ctfMain);
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }finally{
                if(rs != null)
                    closeResultSet(rs);
                if(pst != null)
                    closePreparedStatement(pst);
            }
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ctfMainList;
    }
    
    
    
    public List<CTFMain> getFaciltiesInDate(String mon, String year, String dat, String fY, String tY, String fM, String tM,
                                     String hq) {
        //String sql = "SELECT CM.FACILITY_ID, TO_CHAR(CM.P_DATE) FROM CTF_MAIN CM WHERE 1 = 1 ";
        String sql = "SELECT distinct CM.FACILITY_ID as FACILITY_ID FROM CTF_MAIN CM WHERE 1 = 1 ";
        if (dat.equals("m") || dat.equals("q")) {

            if (dat.equals("m") && !mon.equals("0")) {
                sql += " and to_char(cm.p_date,'mm/yyyy')='" + mon + "/" + year + "'";
            }


            if (dat.equals("q") && !mon.equals("0")) {
                if (mon.equals("1")) {
                    sql +=
                        "and cm.p_date between to_date('01/" + year + "','MM/yyyy') and to_date('03/" + year +
                        "','MM/yyyy')";
                }
                if (mon.equals("2")) {
                    sql +=
                        "and cm.p_date between to_date('04" + year + "','MM/yyyy') and to_date('06/" + year +
                        "','MM/yyyy')";
                }
                if (mon.equals("3")) {
                    sql +=
                        "and cm.p_date between to_date('07/" + year + "','MM/yyyy') and to_date('09/" + year +
                        "','MM/yyyy')";
                }
                if (mon.equals("4")) {
                    sql +=
                        "and cm.p_date between to_date('10/" + year + "','MM/yyyy') and to_date('12/" + year +
                        "','MM/yyyy')";
                }
            }


            if (mon.equals("0")) {
                sql += " and to_char(cm.p_date,'yyyy')='" + year + "'";
            }


        }
        if (dat.equals("u")) {
            sql += " and cm.p_date between to_date('" + fM + "/" + fY + "','mm/yyyy') ";
            sql += "and to_date('" + tM + "/" + tY + "','mm/yyyy')";

        }
        if (dat.equals("hq")) {
            if (hq.equals("1")) {
                sql += " and cm.p_date between to_date('01/" + year + "','mm/yyyy') ";
                sql += "and to_date('06/" + year + "','mm/yyyy')";
            } else {
                sql += " and cm.p_date between to_date('07/" + year + "','mm/yyyy') ";
                sql += "and to_date('12/" + year + "','mm/yyyy')";
            }
        }
        
        List<CTFMain> ctfMainList = new ArrayList<>();
        try {
            PreparedStatement pst = this.connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                CTFMain ctfMain = fillCTFMain(rs);
                ctfMainList.add(ctfMain);
            }
            closeResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ctfMainList;
    }
    
    
}
