package com.logistics.lib.dao;

import com.logistics.lib.dto.CTFItem;
import com.logistics.lib.util.DataAccessObject;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CTFItemDAO extends DataAccessObject<CTFItem> {
    static final String GET_ONE 
        = " SELECT * FROM CTF_ITEM CI WHERE CTF_ITEM_ID = ? ";
    static final String GET_LIST
        = " SELECT * FROM CTF_ITEM CI WHERE CTF_MAIN_ID = ? ";
    
    public CTFItemDAO(Connection connection) {
        super(connection);
    }

    @Override
    public CTFItem findById(long id) {
        CTFItem ctfItem = new CTFItem();
        try {
            PreparedStatement pst = this.connection.prepareStatement(GET_ONE);
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                ctfItem.setId(id);
            }
            closeResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    


    public List<CTFItem> findCTFItems(Long ctfMainId) {
        List<CTFItem> ctfItemList = new ArrayList<>();
        try {
            PreparedStatement pst = this.connection.prepareStatement(GET_LIST);
            pst.setLong(1, ctfMainId);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                CTFItem ctfItem = fillCTFItem(rs);
                ctfItemList.add(ctfItem);
            }
            closeResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ctfItemList;
    }
    
    CTFItem fillCTFItem(ResultSet rs) throws SQLException{
    CTFItem ctfItem = new CTFItem();
    ctfItem.setId(rs.getLong("CTF_ITEM_ID"));
    ctfItem.setProductID(rs.getInt("PROD_ID"));
    ctfItem.setCtfMainId(rs.getLong("CTF_MAIN_ID"));
    ctfItem.setAdjTypeId(rs.getLong("ADJ_TYPE_ID"));
    ctfItem.setAdjustments(rs.getInt("ADJUSTMENTS"));
    ctfItem.setAvgMnthlyCons(rs.getInt("AVG_MNTHLY_CONS"));
    ctfItem.setClosingBalance(rs.getInt("CLOSING_BAL"));
    ctfItem.setIssues(rs.getInt("ISSUES"));
    ctfItem.setOpenBalance(rs.getInt("OPEN_BAL"));
    ctfItem.setReceipts(rs.getInt("RECEIPTS"));
    return ctfItem;
    }
    
    public List<CTFItem> findFacilityItems(String [] products, Long ctfMainId) {
        String strProducts = null;
        for (int i = 0; i < products.length; i++) {
            if (i == 0) {
                strProducts = products[i];
            } else {
                strProducts += ", " + products[i];
            }
        }
        List<CTFItem> ctfItemList = new ArrayList<>();
        String sql = GET_LIST + " and CI.PROD_ID IN ( " + strProducts + " ) ";
        //System.out.println(sql);
        try {
            
            PreparedStatement pst = this.connection.prepareStatement(sql);
            pst.setLong(1, ctfMainId);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                CTFItem ctfItem = fillCTFItem(rs);           
                ctfItemList.add(ctfItem);
            }
            closeResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ctfItemList;
    }

    public List<CTFItem> findAll() {
        // TODO Implement this method
        return Collections.emptyList();
    }
}
