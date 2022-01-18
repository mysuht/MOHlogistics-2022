package ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.context.FacesContext;

import moh.logistics.lib.common.UIDSConnection;

import oracle.adf.share.ADFContext;

import oracle.jdbc.OracleDriver;

public class getDataClass {
    public getDataClass() {
    }
  
    
    Connection conn  = UIDSConnection.getConnection();
    //    int xx = 0;
    String locCtfMainId = "";
    

    public String getData() {
        QueryClass qc = new QueryClass();
        FacesContext fc = FacesContext.getCurrentInstance();
              ELContext elc = fc.getELContext();
              ExpressionFactory ef = fc.getApplication().getExpressionFactory();

    if(locCtfMainId == null || locCtfMainId.equals("null") || locCtfMainId.equals("") ){
    //System.out.println("xxxxxxxxxxxxxxxxxx");
           // #{bindings.CtfMainId.inputValue}
           ValueExpression ve15  = ef.createValueExpression(elc,"#{bindings.CtfMainId.inputValue}", Object.class); 
           locCtfMainId = ""+ve15.getValue(elc);    
    }
              
        
    try {
          
   PreparedStatement ps2 =
       conn.prepareStatement("select ctf_main_id,FAC_CODE," +
       "f.FAC_NAME,ft.FAC_NAME,f.FAC_TYPE_ID,DE_STAFF," +
       "LC_STAFF,to_char(P_DATE,'month,yyyy')," +
       "ctf_comments " +
       "from ctf_main cm," +
       "facility f,FAC_TYPE ft " +
       "where f.FAC_TYPE_ID = ft.FAC_TYPE_ID " +
       "and cm.facility_id = f.facility_id " +
       "and cm.ctf_main_id = ?");
            
    
    //System.out.println("aaaaaaaaaaaaaaaaaaaa=========================="+locCtfMainId);
            locCtfMainId = ADFContext.getCurrent().getPageFlowScope().get("mainId")+"";      
            //System.out.println(" xxxxxxxxxxxxxxxx after new setter mainId is : "+locCtfMainId);
        ps2.setString(1, locCtfMainId);
         //ADFContext.getCurrent().getSessionScope().put("mainId",locCtfMainId);
           // ADFContext.getCurrent().getPageFlowScope().put("mainId",locCtfMainId);
        ResultSet rs2 = ps2.executeQuery();
        rs2.next();
        ValueExpression ve1  = ef.createValueExpression(elc,"#{pageFlowScope.sFacCode}", Object.class); 
         ve1.setValue(elc, rs2.getString(2));
        ADFContext.getCurrent().getSessionScope().put("sFacCode", rs2.getString(2) + "");
            
            
        ValueExpression ve6  = ef.createValueExpression(elc,"#{pageFlowScope.sFacName}", Object.class); 
         ve6.setValue(elc, rs2.getString(3));
            ADFContext.getCurrent().getSessionScope().put("sFacName", rs2.getString(3) + "");
            ADFContext.getCurrent().getSessionScope().put("ss", rs2.getString(3) + "");
            //System.out.println("The Facility Name is "+ ADFContext.getCurrent().getSessionScope().get("sFacName")+""  );
            
        ValueExpression ve7  = ef.createValueExpression(elc,"#{pageFlowScope.sTypeDesc}", Object.class); 
         ve7.setValue(elc, rs2.getString(4));
            ADFContext.getCurrent().getSessionScope().put("sTypeDesc", rs2.getString(4) + "");
            
        
        ValueExpression ve2  = ef.createValueExpression(elc,"#{pageFlowScope.sEnteredBy}", Object.class); 
         ve2.setValue(elc, rs2.getString(6));
            ADFContext.getCurrent().getSessionScope().put("sEnteredBy", rs2.getString(6) + "");
            
        
        ValueExpression ve3  = ef.createValueExpression(elc,"#{pageFlowScope.sChangeBy}", Object.class); 
         ve3.setValue(elc, rs2.getString(7));
            ADFContext.getCurrent().getSessionScope().put("sChangeBy", rs2.getString(7) + "");
        
        ValueExpression ve5  = ef.createValueExpression(elc,"#{pageFlowScope.sMonYear}", Object.class); 
         ve5.setValue(elc, rs2.getString(8));
            ADFContext.getCurrent().getSessionScope().put("sMonYear", rs2.getString(8) + "");
            

            ValueExpression ve55  = ef.createValueExpression(elc,"#{pageFlowScope.sComments}", Object.class); 
             ve55.setValue(elc, rs2.getString(9));
            ADFContext.getCurrent().getSessionScope().put("sComments", rs2.getString(9) + "");
        
        
        
        PreparedStatement ps3 = conn.prepareStatement("select sysdate from dual");
        ResultSet rs3 = ps3.executeQuery();
        
        rs3.next();
        ValueExpression ve8  = ef.createValueExpression(elc,"#{pageFlowScope.sSysDate}", Object.class); 
         ve8.setValue(elc, rs3.getString(1));
            ADFContext.getCurrent().getSessionScope().put("sSysDate", rs3.getString(1) + "");
        //System.out.println("The Date is "+ ADFContext.getCurrent().getSessionScope().get("sSysDate")+""  );
        
    
        } catch (SQLException sqle) {
        // TODO: Add catch code
        sqle.printStackTrace();
        }
        
        return "goItem";
    }



}
