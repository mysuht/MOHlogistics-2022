package com.logistics.lib.util;

import java.sql.Connection;
import java.sql.SQLException;

import moh.logistics.lib.reports.MainInterface;

import oracle.jdbc.pool.OracleDataSource;


public class DataHandler {
    public DataHandler() {
    }
    String jdbcUrl = MainInterface.thinConn;
    String userid = MainInterface.username;
    String password = MainInterface.password; 
    Connection conn;
    public void getDBConnection() throws SQLException{
        OracleDataSource ds;
        ds = new OracleDataSource();
        ds.setURL(jdbcUrl);
        conn=ds.getConnection(userid,password);
        
    }
}