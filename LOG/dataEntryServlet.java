package log;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.*;
import javax.servlet.http.*;

import oracle.jdbc.OracleDriver;

public class dataEntryServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "application/json";
    PreparedStatement pst;
    Connection conn;
    ResultSet rs;
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>dataEntryServlet</title></head>");
        out.println("<body>");
        out.println("<p>The servlet has received a GET. This is the reply.</p>");
        out.println("</body></html>");
        out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
       
        JSONObject json =new JSONObject();
        DBClass db = new DBClass();
        conn = db.conn;
        String sup = request.getParameter("dir");
        String sql = "select facility_id, fac_name from facility where sup_code="+sup+ " order by 2";
        List v1 = new ArrayList();
        List v2 = new ArrayList();
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                v1.add(rs.getString(1));
                v2.add(rs.getString(2));
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
            }
            
            json.put("dir1", v1);
            json.put("name", v2);
        } catch (Exception e) {
            // TODO: Add catch code
            e.printStackTrace();
        }
        out.print(json);
        System.out.println(json);
        JSONArray array;
        try {
            array = json.getJSONArray("name");
            System.out.println(" NNNNNNNNNNNNNNNNNNNN " +array.getString(1));
        } catch (JSONException e) {
        }
        out.close();
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                          IOException {
    }
}

