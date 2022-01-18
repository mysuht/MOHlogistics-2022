package moh.logistics.lib.reports;

import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.naming.NamingException;

import javax.sql.DataSource;

import oracle.jdbc.OracleDriver;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;


public class JDBCConfig {
    public static JdbcTemplate DataSourceJdbcTempateInfo() {
        DataSource dataSource = null;
        try {
            dataSource = (DataSource) new InitialContext().lookup(MainInterface.ds);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }

    public static DataSource dataSource() {
//        ApplicationContext ac = new ClassPathXmlApplicationContext(xmlAppContext, Test.class);
//        return (DataSource) ac.getBean("dataSource");
        Context ctx;
        DataSource ds = null;
        try {
            ctx = new InitialContext();
            ds =(DataSource)ctx.lookup(MainInterface.ds);
            return ds;
        } catch (NamingException e) {
            e.printStackTrace();
        } 
        return ds;
        //return UtilsClass.jdbcConnection().
    }
    

}
