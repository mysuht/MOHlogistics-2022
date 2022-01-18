package moh.logistics.lib.reports.resources;

//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


public class HibernateUtil {
    private static final SessionFactory sessionFactory;
 
      static {
         try {
             // Create the SessionFactory from hibernate.cfg.xml
              sessionFactory = new Configuration().configure().buildSessionFactory();
         } catch (Throwable ex) {
               // Make sure you log the exception, as it might be swallowed
             System.err.println("Initial SessionFactory creation failed." + ex);
             throw new ExceptionInInitializerError(ex);
          }
       }
          public static SessionFactory getSessionFactory() {
                  return sessionFactory;
              }
}
