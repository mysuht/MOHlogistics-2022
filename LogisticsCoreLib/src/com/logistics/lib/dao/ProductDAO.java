package com.logistics.lib.dao;

import com.logistics.lib.dto.Facility;
import com.logistics.lib.dto.Product;
import com.logistics.lib.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductDAO extends DataAccessObject<Product> {   
    public ProductDAO(Connection connection){
        super( connection);
    }
    private static final String GET_ONE
        = " SELECT * FROM PRODUCT WHERE PROD_ID = ? ";
    private static final String GET_LIST
        = " SELECT * FROM PRODUCT WHERE 1 = 1 ";
    public Product findById(long id) {
        Product Product = new Product();
        try {
            PreparedStatement pst = this.connection.prepareStatement(GET_ONE);
            ResultSet rs = pst.executeQuery();
            Product = fillItem(rs);
            closeResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Product;
    }
    
    Product fillItem(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("PROD_ID"));
        product.setIsProductActive(rs.getString("PROD_IS_ACTIVE"));
        product.setProductName(rs.getString("PRO_NAME"));
        product.setProductDose(rs.getString("PRO_DOSE"));
        product.setProductCYP(rs.getString("PROD_CYP"));
        product.setPrCtrindex(rs.getString("PR_CTRINDEX"));
        product.setProductShipQTYPIN(rs.getString("PROD_SHIP_QTY_IN"));
        product.setProductShipQTYWHSE(rs.getString("PROD_SHIP_QTY_WHSE"));
        product.setProductShipQTYSDP(rs.getString("PROD_SHIP_QTY_SDP"));
        return product;
    }

    public List<Product> findAll() {
        List<Product> productList = new ArrayList<>();
        try {
            PreparedStatement pst = this.connection.prepareStatement(GET_LIST);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                Product product = fillItem(rs);
                productList.add(product);
            }
            closeResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }
}
