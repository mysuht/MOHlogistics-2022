package ui;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import moh.logistics.lib.common.UIDSConnection;

import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.jdbc.OracleDriver;


public class QueryClass {

//    String username = "log5";
//    String password = "log5";
////    String thinConn = "jdbc:oracle:thin:@localhost:1521:orcl";
//    String thinConn = "jdbc:oracle:thin:@10.160.12.5:1521:ora10g";
    Connection conn = UIDSConnection.getConnection();

    private RichSelectOneChoice year;
    private RichSelectOneChoice month;
    private RichSelectOneChoice fac;
    private RichOutputText facid;
    private RichOutputText statsmsg;
    private RichOutputText statsmsg1;

    public String getStats() {
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        PreparedStatement ps3 = null;
        ResultSet rs3 = null;
        PreparedStatement ps4 = null;
        ResultSet rs4 = null;
        System.out.println(""+year.getValue()+month.getValue()+fac.getValue()+ fac.getSubmittedValue()+facid.getValue());
        try {
           
            ps1 = conn.prepareStatement("select count(*) from ctf_main where to_char(p_date,'yyyy') = ? and to_char(p_date,'mm')=? and ctf_main.facility_id in (select facility.facility_id from facility where sup_code =?  )");
            ps1.setString(1, ""+year.getValue());
            ps1.setString(2, ""+month.getValue());
            ps1.setString(3, ""+facid.getValue());
            rs = ps1.executeQuery();
            int entered=0;
            if(rs.next()){
                System.out.println(rs.getInt(1));
                entered = rs.getInt(1);
            }


            ps2 = conn.prepareStatement("select count(*) from facility where sup_code =?");
            ps2.setString(1, ""+facid.getValue());
            rs2 = ps2.executeQuery();
            int total=0;
            if(rs2.next()){
                System.out.println(rs2.getInt(1));
                total = rs2.getInt(1);
            }
            

/////////////////////////////////////////////////////////////////////////
                       
            ps3 = conn.prepareStatement("select count(*) from ctf_main where to_char(p_date,'yyyy') = ? and to_char(p_date,'mm')=? and ctf_stage='A' and ctf_main.facility_id in (select facility.facility_id from facility where sup_code =?  )");
            ps3.setString(1, ""+year.getValue());
            ps3.setString(2, ""+month.getValue());
            ps3.setString(3, ""+facid.getValue());
            rs3 = ps3.executeQuery();
            int approved=0;
            if(rs3.next()){
                System.out.println(rs3.getInt(1));
                approved = rs3.getInt(1);
            }
            
            
            ps4 = conn.prepareStatement("select count(*) from ctf_main where to_char(p_date,'yyyy') = ? and to_char(p_date,'mm')=? and ctf_stage='O' and ctf_main.facility_id in (select facility.facility_id from facility where sup_code =? )");
            ps4.setString(1, ""+year.getValue());
            ps4.setString(2, ""+month.getValue());
            ps4.setString(3, ""+facid.getValue());
            rs4 = ps4.executeQuery();
            int open=0;
            if(rs4.next()){
                System.out.println(rs4.getInt(1));
                open = rs4.getInt(1);
            }
                     
            statsmsg.setValue("Total Number of Centers = "+total+" ,Number of Centers Initialized = "+entered+" ,Number of Centers NOT Initialized = "+(total-entered));
            statsmsg1.setValue("Total Number of Centers = "+total+" ,Number of Centers Approved ="+approved+" ,Number of Centers NOT Approved ="+open);
            
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }finally{
            closeRsAndPs(rs, ps1);
            closeRsAndPs(rs2, ps2);
            closeRsAndPs(rs3, ps3);
            closeRsAndPs(rs4, ps4);
            
            
        }
        
        return null;
    }
    
    
    
    public static void closeRsAndPs(ResultSet rs, Statement ps)
    {
        if (rs!=null)
        {
            try
            {
                rs.close();

            }
            catch(SQLException e)
            {
                System.out.println("The result set cannot be closed.");
            }
        }
        if (ps != null)
        {
            try
            {
                ps.close();
            } catch (SQLException e)
            {
                System.out.println("The PreparedStatement cannot be closed.");
            }
        }
       

    }

    public void setYear(RichSelectOneChoice year) {
        this.year = year;
    }

    public RichSelectOneChoice getYear() {
        return year;
    }

    public void setMonth(RichSelectOneChoice month) {
        this.month = month;
    }

    public RichSelectOneChoice getMonth() {
        return month;
    }

    public void setFac(RichSelectOneChoice fac) {
        this.fac = fac;
    }

    public RichSelectOneChoice getFac() {
        return fac;
    }

    public void setFacid(RichOutputText facid) {
        this.facid = facid;
    }

    public RichOutputText getFacid() {
        return facid;
    }

    public void setStatsmsg(RichOutputText statsmsg) {
        this.statsmsg = statsmsg;
    }

    public RichOutputText getStatsmsg() {
        return statsmsg;
    }

    public void setStatsmsg1(RichOutputText statsmsg1) {
        this.statsmsg1 = statsmsg1;
    }

    public RichOutputText getStatsmsg1() {
        return statsmsg1;
    }
}
