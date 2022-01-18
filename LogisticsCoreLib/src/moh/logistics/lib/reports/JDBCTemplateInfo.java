package moh.logistics.lib.reports;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;


public class JDBCTemplateInfo {
    public static final String xmlAppContext = "beans.xml";
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
    
    public static DataSource dataSource(){
        return JDBCConfig.dataSource();
    }
}
