package jo.gov.moh.contraceptives.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import jo.gov.moh.contraceptives.model.config.HibernateUtil;
import jo.gov.moh.contraceptives.model.entities.Facility;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

@WebServlet(name = "test", urlPatterns = { "/test" })
public class test extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
//        PrintWriter out = response.getWriter();
//        out.println("<html>");
//        out.println("<head><title>test</title></head>");
//        out.println("<body>");
//        out.println("<p>The servlet has received a GET. This is the reply.</p>");
//        out.println("</body></html>");
//        out.close();
        try {
//            // 1. configuring hibernate
//            Configuration configuration = new Configuration().configure();
// 
//            // 2. create sessionfactory
//            SessionFactory sessionFactory = configuration.buildSessionFactory();
// 
//            // 3. Get Session object
//            Session session = sessionFactory.openSession();
              
              
              Session session = HibernateUtil.getSessionFactory().openSession();      
            // 4. Starting Transaction
            Transaction transaction = session.beginTransaction();
            Facility facility = (Facility)session.get(Facility.class, 2);
            transaction.commit();
            System.out.println("\n\n Details Added \n");
//            HibernateUtil.shutdown();
            
        } catch (HibernateException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("error");
        }
    }
}
