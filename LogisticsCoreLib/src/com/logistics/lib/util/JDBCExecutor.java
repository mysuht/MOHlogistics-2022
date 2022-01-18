package com.logistics.lib.util;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCExecutor {
    public static void main(String[] args) {
        DatabaseConnectionManager dcm 
            = new DatabaseConnectionManager("10.160.12.5"
                                            ,"ora10g"
                                            ,"log5"
                                            ,"log5");
        try {
            Connection conn = dcm.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
