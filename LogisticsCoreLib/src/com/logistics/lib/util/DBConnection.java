package com.logistics.lib.util;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

import moh.logistics.lib.reports.MainInterface;

import oracle.jdbc.OracleDriver;

public class DBConnection {
    Connection conn = null;

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public DBConnection(){
        this.conn = jndiConnection();
    }
    
    /** Uses JNDI and Datasource (preferred style).   */
    public  Connection   jndiConnection() {

        Connection conn = null;
        String username = MainInterface.username;
        String password = MainInterface.password;
        String thinConn = MainInterface.thinConn;
        
        try {
            DriverManager.registerDriver(new OracleDriver());
            conn = DriverManager.getConnection(thinConn, username, password);
        } catch (SQLException e) {
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%% Connectio failed %%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        }

        return conn;
    }
    
    
    
    public  static Connection   JDBCConnection() {

        Connection conn = null;
        String username = MainInterface.username;
        String password = MainInterface.password;
        String thinConn = MainInterface.thinConn;
        
        try {
            DriverManager.registerDriver(new OracleDriver());
            conn = DriverManager.getConnection(thinConn, username, password);
        } catch (SQLException e) {
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%% Connectio failed %%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        }

        return conn;
    }
    
    
    
    public static String getTypeLvl(String facility) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        String type  = null;
        String sql =
            "select typelvl("+facility+") from dual " ;
     
           
        
        
        
        System.out.println(sql);
        try {
            DBConnection DBClass = new DBConnection();
            pst = DBClass.jndiConnection().prepareStatement(sql);
            rs = pst.executeQuery();
            rs.next();
            type = rs.getString(1);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        System.out.println(" typelvl    "+type);
        return type;

    }
    
    
    public static ResultSet dirsLVL2(String dirs[], String type) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            String dir = null;
            for(int index = 0; index < dirs.length; index++){
                if(index == 0)
                dir = dirs[index];
                else
                dir += ", "+dirs[index];
            }
           
            String sql =
            "select f.facility_id , f.fac_name from facility f, fac_type ft ";
            sql += " where f.fac_type_id = ft.fac_type_id(+) ";
            if (type.equals("0"))
                sql += " and type_hierarchy in (1,2) ";
            else
                sql += " and facility_id in ("+dir+",498) ";
                
            sql += " order by 2";
            DBConnection DBClass = new DBConnection();
            pst = DBClass.jndiConnection().prepareStatement(sql);
            rs = pst.executeQuery();

        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }

        return rs;
    }
    
    
    public ResultSet selectDB(String sql) {
            Connection con = null;
         
             
            ResultSet rs = null;
            Statement stmt;
            try {
                InitialContext ctx= new InitialContext();
                 DataSource ds = (DataSource) ctx.lookup(MainInterface.ds);
                con = ds.getConnection();;
                stmt = con.createStatement();
                rs = stmt.executeQuery(sql);        
              //con.close();[/I]
            } catch(SQLException exc) {
                exc.printStackTrace();
            }catch(NamingException exc) {
                exc.printStackTrace();
            }
            return rs;        
        }
}
