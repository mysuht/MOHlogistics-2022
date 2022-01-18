package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.*;
import javax.servlet.http.*;

import moh.logistics.lib.reports.code.LogisticsReportsClass;

public class getData extends HttpServlet {
    PreparedStatement pst;
    ResultSet rs;
    Connection conn;
    private static final String CONTENT_TYPE = "text/html; charset=windows-1256";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        String dirParam = request.getParameter("cenDir");
        LogisticsReportsClass db = new LogisticsReportsClass();
//        ResultSet rs = db.getFacilityByTypeId(dirParam);
//        StringBuilder sb = new StringBuilder();
//        try {
//           
//            if(!rs.next()){
//          out.write("not executed gooooooooooood");
//            }else{
////                rs.beforeFirst();
//            while (rs.next()) {
//                System.out.println("goooooooooooooooooooooooooood");
//sb.append(rs.getInt(1)+"*"+rs.getString(2)+":");
//            }
//            }
//           
//           // out.write("pxxxxxxxxxxxxxxxxxx");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        out.write(sb.toString());
        //out.close();
    }
    public ResultSet getFacilityByTypeId(String sup){
        
        
        
        
      //  String sql1 = "select * from groups where grp_id" 
            
            
            
            
        if(sup.equals("") || sup.equals(null) || sup.equals(" ")){
            sup = "0";
        }
        String sqlCount = "select count(*) from facility where fac_type_id in ("+sup+") ";
        String sql = "select facility_id , nvl(fac_name,' '), fac_code from facility where fac_type_id in ("+sup+") order by 2 ";
        try {
            pst = conn.prepareStatement(sqlCount);
            rs = pst.executeQuery();
            rs.next();
            if(rs.getInt(1) > 0 ){
                System.out.println(" More Than 0");
               sql = "select facility_id , nvl(fac_name,' '), fac_code from facility where fac_type_id in ("+sup+") order by 2 ";  
             //   sql="select facility_id , nvl(fac_name,' '), fac_code from facility where fac_type_id in (select gof_facility_id from group_of_facilities where gof_grp_id="+(Integer.parseInt(sup)-100)+") order by 2";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
           }else{
                    System.out.println("*********** group Types &&&&&&&&&&&");
                    //sql = "select facility_id , nvl(fac_name,' '), fac_code from facility where fac_type_id in ("+sup+") order by 2 ";  
                     sql="select facility_id , nvl(fac_name,' '), fac_code from facility where fac_type_id in (select gof_facility_id from group_of_facilities where gof_grp_id="+(Integer.parseInt(sup)-100)+") order by 2";
                     pst = conn.prepareStatement(sql);
                     rs = pst.executeQuery();
                }
            } catch (SQLException e) {
        //}
    } catch (Exception e) {
            // TODO: Add catch code
          
            e.printStackTrace();
        }
        System.out.println(sql);
        return rs;
            
    }
}
