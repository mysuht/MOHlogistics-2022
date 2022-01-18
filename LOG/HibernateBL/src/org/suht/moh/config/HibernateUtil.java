package org.suht.moh.config;

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.suht.moh.model.CTFMain;


/**
 * Java based configuration
 * @author ramesh Fadatare
 *
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;
    //private static Properties hibernateProperties;
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Properties settings = new Properties();
                settings = getHibernateProperties();
                settings.put(Environment.DRIVER, settings.getProperty("hibernate.connection.driver_class"));
                settings.put(Environment.URL, settings.getProperty("hibernate.connection.url"));
                settings.put(Environment.USER, settings.getProperty("hibernate.connection.username"));
                settings.put(Environment.PASS, settings.getProperty("hibernate.connection.password"));
                settings.put(Environment.DIALECT, settings.getProperty("hibernate.dialect"));
                settings.put(Environment.SHOW_SQL, settings.getProperty("hibernate.show_sql"));
                
                /*
                 * BY SUHT - THIS TO NOT GENERATE SCHEMA AND RELATIONS
                 */
                //settings.put(Environment.HBM2DDL_AUTO, settings.getProperty("hibernate.hbm2ddl.auto"));
                System.out.println("Environment.HBM2DDL_AUTO" + " -> " + configuration.getProperty( Environment.HBM2DDL_AUTO) );
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, settings.getProperty("hibernate.current_session_context_class"));

                configuration.setProperties(settings);
                configuration.addAnnotatedClass(CTFMain.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
                System.out.println("Hibernate Java Config serviceRegistry created");
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                return sessionFactory;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }


    @SuppressWarnings("oracle.jdeveloper.java.null-map-return")
    private static Properties getHibernateProperties() {
         Properties prop = new Properties();
            try  {
                   InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream("hibernate.properties");                   
            if (input == null) {
                       System.out.println("Sorry, unable to find config.properties");
                       return null;
                   }

                   //load a properties file from class path, inside static method
                   prop.load(input);

                   //get the property value and print it out
                   System.out.println(prop.getProperty("hibernate.connection.driver_class"));
                   System.out.println(prop.getProperty("hibernate.connection.url"));
                   System.out.println(prop.getProperty("hibernate.connection.username"));

               } catch (IOException ex) {
                   ex.printStackTrace();
               } 
            return prop;
    }
    
}