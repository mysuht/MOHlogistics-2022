package rep;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class TestServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1256";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("context-path : " + request.getContextPath());
        response.sendRedirect("abc" + "/a.jsp");
//        response.setContentType(CONTENT_TYPE);
//        PrintWriter out = response.getWriter();
//        out.println("<html>");
//        out.println("<head><title>TestServlet</title></head>");
//        out.println("<body>");
//        out.println("<p>The servlet has received a GET. This is the reply.</p>");
//        out.println("</body></html>");
//        out.close();
    }
}
