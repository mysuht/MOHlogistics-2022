package moh.logistics.lib.common;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import moh.logistics.lib.reports.MainInterface;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

public class UIDSConnection implements MainInterface {
    
    
    public static Connection getConnection() {
        int intArray[] = { 1,2,3,4,5,6 };   
//        try {
//            ArrayDescriptor desc= ArrayDescriptor.createDescriptor ("NUM_ARRAY", con);
//            ARRAY array = new ARRAY  (desc, con, intArray);
//        } catch (SQLException e) {
//        }
        Connection con = null ;
        try {
            javax.naming.Context initialContext = new javax.naming.InitialContext();
              javax.sql.DataSource dataSource = 
                        (javax.sql.DataSource)initialContext.lookup(datasourceLookup);
              con = dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("xxxxxxxxxxxxxxxxxxxxxx UIDSConnection Connection has been Failed xxxxxxxxxxxxxxxxxxxxxxxxx");
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return con;
    }

    
    public Connection getJNDIConnection() {
        // TODO Implement this method
        return null;
    }
}
