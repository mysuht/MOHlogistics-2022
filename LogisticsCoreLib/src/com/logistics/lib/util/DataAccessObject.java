package com.logistics.lib.util;

import com.logistics.lib.dao.CTFMainDAO;
import com.logistics.lib.dto.CTFMain;

import java.io.IOException;
import java.io.InputStream;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import moh.logistics.lib.reports.MainInterface.URLInfo;

public abstract class DataAccessObject <T extends DataTransferObject> {
    protected final Connection connection;
    protected final static String LAST_VAL = 
        " select last_number from user_sequences where  sequence_name = ";
    protected final static String Customer_Sequence
        = "'hp_customer_sequence'";
    public DataAccessObject(Connection connection) {
        super();
        this.connection = connection;
    }
    
    public static Properties getProperties(){
        Properties prop = new Properties();
        InputStream input = null;
        input =  URLInfo.class.getClassLoader().getResourceAsStream("database.properties");
            try {
                prop.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return prop;
    }
    
    
    public abstract T findById(long id);
    public abstract List<T> findAll();
//    public abstract T update(T dto);
//    public abstract T create(T dto);
//    public abstract void delete(long id);
    
    protected int getLastVal(String sequence){
        int key = 0;
        String sql = LAST_VAL + sequence;
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                key = rs.getInt(0);
            }
            return key;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public static void closeResultSet(ResultSet rs) throws SQLException {
        rs.close();
    }
    public static void closePreparedStatement(PreparedStatement pst) throws SQLException {
        pst.close();
    }
    
    
}
