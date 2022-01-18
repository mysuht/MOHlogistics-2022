package ui;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.faces.event.ActionEvent;

import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.jbo.*;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.*;
import oracle.jbo.domain.Number;

import oracle.jdbc.OracleDriver;

public class CTFItemClass {
  String username = "log5";
  String password = "log5";
//  String thinConn = "jdbc:oracle:thin:@localhost:1521:orcl";
    String thinConn = "jdbc:oracle:thin:@10.160.12.5:1521:ora10g";

    private RichInputText openBal;
    private RichInputText closebal;
    private RichInputText receipt;
    private RichInputText issue;
    private RichInputListOfValues adjId;
    private RichInputText adjAmount;
    private RichInputDate operDate;
    private RichInputText ctfItemId;
    private RichInputListOfValues prodId;
    private RichInputText ctfMainId;
    private RichInputText avgMonCon;
    private RichInputText qtyReq;

    public void setOpenBal(RichInputText openBal) {
        this.openBal = openBal;
    }

    public RichInputText getOpenBal() {
        return openBal;
    }

    public void setClosebal(RichInputText closebal) {
        this.closebal = closebal;
    }

    public RichInputText getClosebal() {
        return closebal;
    }

    public void setReceipt(RichInputText receipt) {
        this.receipt = receipt;
    }

    public RichInputText getReceipt() {
        return receipt;
    }

    public void setIssue(RichInputText issue) {
        this.issue = issue;
    }

    public RichInputText getIssue() {
        return issue;
    }

    public void setAdjId(RichInputListOfValues adjId) {
        this.adjId = adjId;
    }

    public RichInputListOfValues getAdjId() {
        return adjId;
    }

    public void setAdjAmount(RichInputText adjAmount) {
        this.adjAmount = adjAmount;
    }

    public RichInputText getAdjAmount() {
        return adjAmount;
    }

    public void calculateValues(ActionEvent actionEvent) {
          int mon =0;
          int year = 0;
          String ctfMId = ""+ctfMainId.getValue();
          String proId = ""+prodId.getValue();
          String maxCtfId = "";
          String prodIdL = "";
          int countCtfId = 0;
          int sum = 0;
          int avg = 0;
          
          try {
              DriverManager.registerDriver(new OracleDriver());
              Connection conn =
                  DriverManager.getConnection(thinConn, username, password);
            PreparedStatement ps2 = conn.prepareStatement("select nvl(count(ctf_item_id),0) from ctf_item where ctf_main_id = ? and prod_id = ?");
              ps2.setString(1,ctfMId);
              ps2.setString(2, proId);
              ResultSet rs2 = ps2.executeQuery();
              rs2.next();
            countCtfId = rs2.getInt(1);
            String issuex = ""+issue.getValue();
              if (countCtfId == 0 ) {
             
              avgMonCon.setValue(issuex);
            } else if(countCtfId < 3){
              PreparedStatement ps3 = conn.prepareStatement("select ISSUES from  ctf_item where CTF_MAIN_ID     = ? and PROD_ID = ?");
              ps3.setString(1,ctfMId);
              ps3.setString(2, proId);
              ResultSet rs3 = ps3.executeQuery(); 
                 
                  while (rs3.next()){
                    sum += rs3.getInt(1);
                      
                     
                  }
                  sum += Integer.parseInt(issuex);
              System.out.println("sum of issue === "+sum);
                  avg = sum / (countCtfId+1);
              avgMonCon.setValue(avg);
              } else{
                PreparedStatement ps3 = conn.prepareStatement("select issues from (select p_date,issues from ctf_item where ctf_main_id = ? and prod_id = ? order by p_date desc) where rownum <3");
                ps3.setString(1,ctfMId);
                ps3.setString(2, proId);
                ResultSet rs3 = ps3.executeQuery(); 
                   
                    while (rs3.next()){
                      sum += rs3.getInt(1);
                        
                       
                    }
                    sum += Integer.parseInt(issuex);
                System.out.println("sum of issue === "+sum);
                    avg = sum / 3;
                avgMonCon.setValue(avg);
              }
              

          } catch (SQLException sqle) {
              // TODO: Add catch code
              sqle.printStackTrace();
          }
          
          //////////////////////////////////////////////////////////////////
          
     
     
     
     /////////////////////////////////////////////////////////////////////////////
      
     int x = 0;
          String adjIdV = "";
          String amDef = "model.AM.LOGAM";
            String config = "LOGAMLocal";
            ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
          
     
        ViewObject vo = am.findViewObject("AdjTypeVO");
        
        vo.setWhereClause("ADJ_TYPE_ID = "+adjId.getValue());
        
        
        
        if(vo.hasNext()){
          Row r = vo.next();
          adjIdV = ""+r.getAttribute(2);
          
        }
        if (adjIdV.equals("1")){
        // Work with your appmodule and view object here
        //Configuration.releaseRootApplicationModule(am, true);
      int  ob =   Integer.parseInt(""+openBal.getValue());
      int  r =   Integer.parseInt(""+receipt.getValue());
      int  iss =   Integer.parseInt(""+issue.getValue());
      int  ajm =   Integer.parseInt(""+adjAmount.getValue());
     x = ob+r-iss+ajm;
     System.out.println("x   =======  "+x);
      closebal.setValue(x);
        } else {
          int  ob =   Integer.parseInt(""+openBal.getValue());
          int  r =   Integer.parseInt(""+receipt.getValue());
          int  iss =   Integer.parseInt(""+issue.getValue());
          int  ajm =   Integer.parseInt(""+adjAmount.getValue());
          x = (ob+r-iss)-ajm;
          System.out.println("sum   =======  "+x);
          closebal.setValue(x);
        }
        
         int reqQty =  2*avg-x;
          qtyReq.setValue(reqQty);
        }


    public void setOperDate(RichInputDate operDate) {
        this.operDate = operDate;
    }

    public RichInputDate getOperDate() {
        return operDate;
    }

    public void setCtfItemId(RichInputText ctfItemId) {
        this.ctfItemId = ctfItemId;
    }

    public RichInputText getCtfItemId() {
        return ctfItemId;
    }

    public void setopenBal(ActionEvent actionEvent) {
       
        int mon =0;
        int year = 0;
        String ctfMId = ""+ctfMainId.getValue();
        String proId = ""+prodId.getValue();
        String maxCtfId = "";
        String prodIdL = "";
          try {
              DriverManager.registerDriver(new OracleDriver());
              Connection conn =
                  DriverManager.getConnection(thinConn, username, password);
            PreparedStatement ps1 = conn.prepareStatement("select nvl(max(ctf_item_id),0) from ctf_item where ctf_main_id = ? and prod_id = ?");
              ps1.setString(1,ctfMId);
              ps1.setString(2, proId);
              ResultSet rs1 = ps1.executeQuery();
              rs1.next();
            maxCtfId = rs1.getString(1);
             System.out.println(maxCtfId); 
              PreparedStatement ps = conn.prepareStatement("select to_char(P_DATE,'mm'),to_char(P_DATE,'yyyy'),prod_id from ctf_item where CTF_ITEM_ID = ?");
              ps.setString(1, maxCtfId);
              ResultSet rs = ps.executeQuery();
              rs.next();
              mon = rs.getInt(1);
             year = rs.getInt(2);
            prodIdL = rs.getString(3);
              System.out.println(mon+"  "+year);
          } catch (SQLException sqle) {
              // TODO: Add catch code
              sqle.printStackTrace();
          }
        
      
        
        String amDef = "model.AM.LOGAM";
          String config = "LOGAMLocal";
          ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
        
          if(proId.equals(prodIdL)){
           
             ViewObject ctfvo = am.findViewObject("CtfItemVO");
             ctfvo.setWhereClause("ctf_item_id = "+maxCtfId+" and CtfItemEO.PROD_ID = "+prodId.getValue()+" and CTF_MAIN_ID = "+ctfMainId.getValue()+" and to_char(P_DATE,'mm') = "+mon+" and to_char(P_DATE,'yyyy') = "+year);
             System.out.println("ctf_item_id = "+maxCtfId+" and CtfItemEO.PROD_ID = "+prodId.getValue()+" and CTF_MAIN_ID = "+ctfMainId.getValue()+" and to_char(P_DATE,'mm') = "+mon+" and to_char(P_DATE,'yyyy') = "+year);
              if(ctfvo.hasNext()){
              Row rr = ctfvo.next();
              String closeLastBal = ""+rr.getAttribute(9);
              System.out.println("closebalance===========  "+closeLastBal);
            openBal.setValue(closeLastBal);
              }
          }
    }

    public void setProdId(RichInputListOfValues prodId) {
        this.prodId = prodId;
    }

    public RichInputListOfValues getProdId() {
        return prodId;
    }

    public void setCtfMainId(RichInputText ctfMainId) {
        this.ctfMainId = ctfMainId;
    }

    public RichInputText getCtfMainId() {
        return ctfMainId;
    }

    public void setAvgMonCon(RichInputText avgMonCon) {
        this.avgMonCon = avgMonCon;
    }

    public RichInputText getAvgMonCon() {
        return avgMonCon;
    }

    public void setQtyReq(RichInputText qtyReq) {
        this.qtyReq = qtyReq;
    }

    public RichInputText getQtyReq() {
        return qtyReq;
    }
}
