package com.logistics.lib.dao;

import com.logistics.lib.dto.FacilityType;
import com.logistics.lib.util.DataAccessObject;
import com.logistics.lib.util.DataTransferObject;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FacilityTypeDAO extends DataAccessObject<FacilityType> {
    private static String GET_ITEM 
        = " SELECT * FROM FAC_TYPE WHERE FAC_TYPE_ID = ? ";
    private static String GET_LIST
        = " SELECT * FROM FAC_TYPE WHERE 1 = 1 ";

    public FacilityTypeDAO(Connection connection) {
        super(connection);
    }

    private FacilityType fillItem(ResultSet rs) throws SQLException {
        FacilityType facilityType = new FacilityType();
        facilityType.setId(rs.getLong("FAC_TYPE_ID"));
        facilityType.setFacilityTypeName(rs.getString("FAC_NAME"));
        facilityType.setTypeHierarchy(rs.getInt("TYPE_HIERARCHY"));
        return facilityType;
    }
    public FacilityType findById(long id) {
        FacilityType facilityType = new FacilityType();
        try {
            PreparedStatement pst = this.connection.prepareStatement(GET_ITEM);
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                facilityType = fillItem(rs);
            }
            closeResultSet(rs);
            closePreparedStatement(pst);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facilityType;
    }

    public List<FacilityType> findAll() {
        List<FacilityType> facilityTypeList = new ArrayList<>();
        try {
            PreparedStatement pst = this.connection.prepareStatement(GET_LIST);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                FacilityType facilityType = new FacilityType();
                facilityType = fillItem(rs);
                facilityTypeList.add(facilityType);
            }
            closeResultSet(rs);
            closePreparedStatement(pst);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facilityTypeList;
    }
}
