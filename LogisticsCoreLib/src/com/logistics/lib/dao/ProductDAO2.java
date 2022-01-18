package com.logistics.lib.dao;

import com.logistics.lib.dto.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class ProductDAO2 {
    final DataSource dataSource;
    public ProductDAO2(DataSource dataSource) {
        this.dataSource = dataSource;
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
    public List<Product> list() throws SQLException {
            List<Product> products = new ArrayList<Product>();
            try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM product");
                ResultSet resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    Product product = fillItem(resultSet);
                    products.add(product);
                    System.out.println(product.getProductName());
                }
            }
            return products;
        }
    
    }

