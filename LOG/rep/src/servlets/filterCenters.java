package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.ResultSet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

import moh.logistics.lib.reports.code.LogisticsReportsClass;

public class filterCenters extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1256";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        String dirParam = request.getParameter("cenDir");
//        LogisticsReportsClass db = new LogisticsReportsClass();
//        ResultSet rs = db.getFacilityByTypeId(dirParam);
//        try {
//            StringBuilder sb = new StringBuilder();
//            while (rs.next()) {
//
//            }
//            out.write("pxxxxxxxxxxxxxxxxxx");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        out.close();
    }
}
