package ui;


import java.sql.*;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Map;

import javax.el.ELContext;

import javax.el.ExpressionFactory;

import javax.el.MethodExpression;

import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.share.ADFContextManager;
import oracle.adf.view.rich.component.rich.RichPanelWindow;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichColumn;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputText;


import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;

import oracle.adfinternal.view.faces.model.binding.FacesCtrlHierBinding;

import oracle.binding.BindingContainer;

import oracle.jbo.*;
import oracle.jbo.ApplicationModule;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.Number;
import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.client.Configuration;
import oracle.jbo.uicli.binding.JUCtrlActionBinding;
import oracle.jbo.uicli.binding.JUCtrlHierBinding;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import oracle.jdbc.OracleDriver;

import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.CollectionModel;
import javax.faces.event.ValueChangeEvent;

import moh.logistics.lib.common.UIDSConnection;
import moh.logistics.lib.reports.MainInterface;

import oracle.adf.share.ADFContext;

import oracle.adf.view.rich.component.rich.data.RichTable;

import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;


public class MainItemsClass implements MainInterface {
    
        Connection conn = UIDSConnection.getConnection();
        String locCtfMainId = "";
        String facilityId = "";
        String oldBal = "";
        String newBal = "";
        String oldReceipt = "";
        String newReceipt = "";
        String oldClosingBal = "";
        String newClosingBal = "";
        String oldAvgMonCons = "";
        String newAvgMonCons = "";
        String oldQtyReq = "";
        String newQtyReq = "";
        String oldAdjustments = "";
        String newAdjustments = "";
        
        int currow;
       
        private RichInputDate pdate;
        private RichPopup popup1;
        private RichPopup openBalPopup;
        private RichPopup receiptPopup;
        private RichPopup closingBalPopup;
        private RichPanelWindow cvPop;
        private RichPopup cvPopup;
        private RichPopup savePopup;
        private RichPopup lbPopup;
        private RichTable itemsTable;
    private RichPopup searchPopup;
    private RichPopup deleteDataPopup;
    private RichInputText qtyReqId;

    public void setClosingBalPopup(RichPopup closingBalPopup) {
            this.closingBalPopup = closingBalPopup;
        }

        public RichPopup getClosingBalPopup() {
            return closingBalPopup;
        }
        private RichPopup avgMonConsPopup;
        private RichPopup qtyReqPopup;
        private RichPopup adjustmentPopup;
        private RichPopup p7;
        private RichInputText ctfMainIdTxt;
        private RichInputText ctfCommentsTxt;
        private RichInputText ctfCommentsTxtNonDB;
        private RichTable filterDateMainBrowse;
        private RichInputText getDateCtfMain;
        private RichInputText commentView111;
        
        
        public  int getProductsCount() {
            int count = 0;
            PreparedStatement ps = null;
            ResultSet rs = null;
            
            String sql = "select count(*) from product";
           
            try{
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                rs.next();
                count = rs.getInt(1); 
               
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                closePS(ps);
            }
            
            return count;
        }

        public String createRecords() {
            PreparedStatement ps2 = null;
            ResultSet rs2 = null;
            PreparedStatement ps = null;
            FacesContext fc = FacesContext.getCurrentInstance();
                  ELContext elc = fc.getELContext();
                  ExpressionFactory ef = fc.getApplication().getExpressionFactory();
                    MethodExpression me = ef.createMethodExpression(elc, "#{bindings.Commit.execute}", Object.class, new Class[0]);
                    me.invoke(elc, new Object[0]);
                    
                     String ctfm = ""+ctfMainIdTxt.getValue();
                     
     
      
            try {

                conn.setAutoCommit(true);

                ps2 = conn.prepareStatement("select * from CTF_ITEM where ctf_main_id ="+ctfm);
                rs2 =   ps2.executeQuery();
                if(!rs2.next()){
               
                for (int i = 1; i < getProductsCount()+1 ; i++) {
                        ps = conn.prepareStatement("insert into CTF_ITEM (CTF_MAIN_ID,PROD_ID,OPEN_BAL,RECEIPTS,ISSUES,ADJUSTMENTS,CLOSING_BAL,AVG_MNTHLY_CONS,QTY_REQUIRED,QTY_RECEIVED,NEW_VISITS,CONT_VISITS) values ("+ctfm+","+i+",0,0,0,0,0,0,0,0,0,0)");
                     
                        ps.executeUpdate();
                       
                        conn.commit();
                    }
                }
                
               


                conn.setAutoCommit(true);
                conn.commit();
             
            } catch (SQLException sqle) {
              
                sqle.printStackTrace();
            }finally{
                closeRsAndPs(rs2, ps2);
                closePS(ps);
               
            }
            
          
            
            return "back";
        }




        private String getFacilityAndDateByMainId(String mainId, int Column){
            String value = null;
         
            ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
            ViewObject vo = am.createViewObjectFromQueryStmt("voFacByMainId", "select cmain.facility_id,to_char(cmain.p_date,'dd/mm/yyyy'), cmain.ctf_main_id from ctf_main cmain");
            vo.setWhereClause("ctf_main_id = "+mainId);
            vo.executeQuery();
            if(vo.hasNext()){
                Row row = vo.next();
                value = row.getAttribute(Column).toString();
            }
            
            
            vo.remove();
            Configuration.releaseRootApplicationModule(am, true);  
            
            return value;
        }
        
        
        private String getFacilityAndDateByMainId11111(String mainId, int Column){
            String value = null;
         
            ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
            ViewObject vo = am.createViewObjectFromQueryStmt("voFacByMainId", "select cmain.facility_id,to_char(cmain.p_date,'dd/mm/yyyy'), cmain.ctf_main_id from ctf_main cmain");
            vo.setWhereClause("ctf_main_id = "+mainId);
            vo.executeQuery();
            if(vo.hasNext()){
                Row row = vo.next();
                value = row.getAttribute(Column).toString();
            }
            
            
            vo.remove();
            Configuration.releaseRootApplicationModule(am, true);  
            
            return value;
        }
        
        
        public String SearchCtfMain(){
            String date = ADFContext.getCurrent().getViewScope().get("pD")+"";
            String code = ADFContext.getCurrent().getViewScope().get("pCode")+"";
            String chooseDate = ADFContext.getCurrent().getViewScope().get("chooseDate")+"";
            String startDate = ADFContext.getCurrent().getViewScope().get("chooseDate")+"";
            String endDate = ADFContext.getCurrent().getViewScope().get("chooseDate")+"";
            //System.out.println(" XXXXXXXXX DATE IS "+date+" XXXXXXXXX CODE IS "+code);
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            DCBindingContainer bc = (DCBindingContainer)bindings;
            JUCtrlActionBinding searchMainItems = 
            (JUCtrlActionBinding) bc.findCtrlBinding("SearchMainItems");
              searchMainItems.getParamsMap().put("pD",date);
              Object result = searchMainItems.execute();
            AdfFacesContext.getCurrentInstance().addPartialTarget(filterDateMainBrowse);
            searchPopup.hide();
            return null;
        }
        
        
       private void SearchItemsByNextPreviousBTNS(){
//            String date = ADFContext.getCurrent().getViewScope().get("pD")+"";
//            String code = ADFContext.getCurrent().getViewScope().get("pCode")+"";
//            String chooseDate = ADFContext.getCurrent().getViewScope().get("chooseDate")+"";
//            String startDate = ADFContext.getCurrent().getViewScope().get("chooseDate")+"";
//            String endDate = ADFContext.getCurrent().getViewScope().get("chooseDate")+"";
//            //System.out.println(" XXXXXXXXX DATE IS "+date+" XXXXXXXXX CODE IS "+code);
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            DCBindingContainer bc = (DCBindingContainer)bindings;
            JUCtrlActionBinding searchMainItems = 
            (JUCtrlActionBinding) bc.findCtrlBinding("SearchMainItems");
             // searchMainItems.getParamsMap().put("pD",date);
              Object result = searchMainItems.execute();
            
        }
        
    public String ClearSearchCtfMain(){
        BindingContext bctx = BindingContext.getCurrent();
        BindingContainer bindings = bctx.getCurrentBindingsEntry();
        DCBindingContainer bc = (DCBindingContainer)bindings;
        JUCtrlActionBinding searchMainItems =
        (JUCtrlActionBinding) bc.findCtrlBinding("ClearSearchMainItems");
         Object result = searchMainItems.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(filterDateMainBrowse);
        searchPopup.hide();
        return null;
    }


        public String testdc(){
            
           ResultSet rs = null;
            
            int openBal = 0;
            int receipts = 0 ;
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            DCBindingContainer bc = (DCBindingContainer)bindings;
            DCIteratorBinding dc = bc.findIteratorBinding("CtfItemCtfMainVOIterator");
      
           JUCtrlActionBinding getDate = (JUCtrlActionBinding) bc.findCtrlBinding("getItemsByMainId");
          //  getDate.getParamsMap().put("dsfs",value);
           Object result = getDate.execute();
           if(result instanceof ResultSet){
               //System.out.println("  Excellent  ");
               rs = (ResultSet) result;
                try {
                    
                    for(Row row : dc.getAllRowsInRange()){
                        if( rs.next()){
                         openBal = rs.getInt(2)   ;
                         receipts = rs.getInt(3);
                        }
                    row.setAttribute("OpenBal", openBal);
                    row.setAttribute("Receipts",receipts);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            ADFContext.getCurrent().getViewScope().put("lb", true);
           AdfFacesContext.getCurrentInstance().addPartialTarget(itemsTable);
            return null;
        }
        
        
        public String test_calculate(){
            
           ResultSet rs = null;
            
            
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            DCBindingContainer bc = (DCBindingContainer)bindings;
            DCIteratorBinding dc = bc.findIteratorBinding("CtfItemCtfMainVOIterator");
        
           JUCtrlActionBinding getDate = (JUCtrlActionBinding) bc.findCtrlBinding("CalculateValues");
          //  getDate.getParamsMap().put("dsfs",value);
           Object result = getDate.execute();
           if(result instanceof ResultSet){
               //System.out.println("  Excellent  ");
               rs = (ResultSet) result;
                try {
                    
                    for(Row row : dc.getAllRowsInRange()){
                        rs.next();
                    row.setAttribute("OpenBal", rs.getString(2));
                    row.setAttribute("Receipts", rs.getString(3));
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
           AdfFacesContext.getCurrentInstance().addPartialTarget(itemsTable);
            return null;
        }
        
        public String getLastBalanceNew(){
            PreparedStatement ps1 = null;
            PreparedStatement ps2 = null;
            ResultSet rs1 = null;
            ResultSet rs = null;
            ResultSet rs2 = null;
            PreparedStatement ps = null;
                 BindingContext bctx = BindingContext.getCurrent();
                 BindingContainer bindings = bctx.getCurrentBindingsEntry();
                DCBindingContainer bc = (DCBindingContainer)bindings;
                DCIteratorBinding dc = bc.findIteratorBinding("dsfdsfds");
               
                
                 
                 FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
                 
                        for (int i = 1; i < 13; i++) {
                       
                    String ctfMId = locCtfMainId;
                    String prodId = ""+i;
                    String maxCtfId = "";
                     
                    
                            //System.out.println("prodid ===="+prodId);
                      try {
                        
                      
                        
                        
                        
                        ps1 = conn.prepareStatement("select facility_id,to_char(p_date,'dd/mm/yyyy') from ctf_main where ctf_main_id = ?");
                        ps1.setString(1,ctfMId);
                        rs1 = ps1.executeQuery();
                        if (rs1.next()){
                      facilityId = rs1.getString(1);
                       //System.out.println("facility id ==== "+facilityId); 
                        //System.out.println("ctf Main id ==== "+ctfMId);
                        //System.out.println(" p_date ==== "+rs1.getString(2));
            
                        ps2 = conn.prepareStatement("select CTF_MAIN_ID from ctf_main where facility_id = ? and to_char(add_months(p_date,1),'dd/mm/yyyy') = ?");
                        ps2.setString(1, facilityId);
                        ps2.setString(2, rs1.getString(2));
                            
                        rs2 = ps2.executeQuery();
            
                        rs2.next();
                        maxCtfId = rs2.getString(1);
                        //System.out.println("maxctfmainid ==== "+maxCtfId);
                        ps = conn.prepareStatement("select CLOSING_BAL,qty_received from ctf_item where CTF_main_ID = ? and prod_id = ?");
                        ps.setString(1, maxCtfId);
                        ps.setString(2, prodId);
                        rs = ps.executeQuery();
                        rs.next();
                        String closeLastBal = rs.getString(1);
                        //System.out.println("last balance ===== "+closeLastBal);
                        tm.setAttributeInRow(i-1, 4, closeLastBal,true);
                        
                        String qtyReceiv = rs.getString(2);
                        //System.out.println("qtyReceiv ===== "+qtyReceiv);
                        tm.setAttributeInRow(i-1, 5, qtyReceiv,true);
                        }else{
                            tm.setAttributeInRow(i-1, 4, 0,true);
                            tm.setAttributeInRow(i-1, 5, 0,true);
                        }
                    } catch (SQLException sqle) {
                        // TODO: Add catch code
                        sqle.printStackTrace();
                    }finally{
                        closeRsAndPs(rs, ps);
                        closeRsAndPs(rs1, ps1);
                            closeRsAndPs(rs2, ps2);
                            
                        }
                    
                 
                    
                        }
           
            return null;
        }







        public static int screenViewIteratorFacesCtrlHierBindingRowCount(FacesCtrlHierBinding tm){
            return tm.getRowIterator().getRowCount();
        }




    public String getLastBalance() {
       
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs1 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        PreparedStatement ps = null;
             BindingContext bctx = BindingContext.getCurrent();
             BindingContainer bindings = bctx.getCurrentBindingsEntry();
        //        FacesCtrlAttrsBinding x = (FacesCtrlAttrsBinding)bindings.get("Empno");
        //        System.out.println("xxxxxxxxxx="+x.getAttributeValue());
             
             FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
        int IteratorRowCount = screenViewIteratorFacesCtrlHierBindingRowCount(tm);
            System.out.println("xxxxxxxxx====="+ IteratorRowCount);
       // tm.getCollectionModel().getRowIndex();
    //             tm.setCurrentRowAtIndex(3);
    //          System.out.println("yyyyyyyyyyy===="+ tm.getAttributeValue(2));
             
            System.out.println("zzzzzzzzz===="+ tm.getAttributeFromRow(3, 2));
           // tm.setAttributeInRow(4, 5, 9999,true);
             System.out.println("zzzzzzzzz===="+ tm.getAttributeFromRow(4, 5));
           
            for (int i = 1; i < IteratorRowCount + 1; i++) {
            
        

        int mon =0;
        int year = 0;
        String ctfMId = locCtfMainId;
        String prodId = ""+i;
        String maxCtfId = "";
        String prodIdL = "";  
        
                System.out.println("prodid ===="+prodId);
        try {
            
              
            ps1 = conn.prepareStatement("select facility_id,to_char(p_date,'dd/mm/yyyy') from ctf_main where ctf_main_id = ?");
            ps1.setString(1,ctfMId);
            //ps1.setString(2, prodId);
            rs1 = ps1.executeQuery();
            if (rs1.next()){
          facilityId = rs1.getString(1);
           System.out.println("facility id ==== "+facilityId); 
            System.out.println("ctf Main id ==== "+ctfMId);
            System.out.println(" p_date ==== "+rs1.getString(2));
                            
            
    //            PreparedStatement ps2 = conn.prepareStatement("select CTF_MAIN_ID from ctf_main where facility_id = ? and CTF_MAIN_ID = (select max(CTF_MAIN_ID) from CTF_MAIN where facility_id = ? and CTF_MAIN_ID < ?)");
    //
    //            ps2.setString(1, facilityId);
    //            ps2.setString(2, facilityId);
    //            ps2.setString(3, ctfMId);
            ps2 = conn.prepareStatement("select CTF_MAIN_ID from ctf_main where facility_id = ? and to_char(add_months(p_date,1),'dd/mm/yyyy') = ?");
            ps2.setString(1, facilityId);
            ps2.setString(2, rs1.getString(2));
                
            rs2 = ps2.executeQuery();

            rs2.next();
            maxCtfId = rs2.getString(1);
            System.out.println("maxctfmainid ==== "+maxCtfId);
            ps = conn.prepareStatement("select CLOSING_BAL,qty_received from ctf_item where CTF_main_ID = ? and prod_id = ?");
            ps.setString(1, maxCtfId);
            ps.setString(2, prodId);
            rs = ps.executeQuery();
            rs.next();
            String closeLastBal = rs.getString(1);
            System.out.println("last balance ===== "+closeLastBal);
            tm.setAttributeInRow(i-1, 4, closeLastBal,true);
            
            String qtyReceiv = rs.getString(2);
            System.out.println("qtyReceiv ===== "+qtyReceiv);
            tm.setAttributeInRow(i-1, 5, qtyReceiv,true);
            }else{
                tm.setAttributeInRow(i-1, 4, 0,true);
                tm.setAttributeInRow(i-1, 5, 0,true);
            }
        } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
        }finally{
            closeRsAndPs(rs, ps);
            closeRsAndPs(rs1, ps1);
                closeRsAndPs(rs2, ps2);
                
            }
        
        ADFContext.getCurrent().getViewScope().put("lb", true);
        
            }
         
        
        return null;
    }
    /*
     *  30_AUG_2016 MODIFICATIONS
     */
        public String getLastBalanceOLD() {
          //  Connection conn = null;
            PreparedStatement ps1 = null;
            PreparedStatement ps2 = null;
            ResultSet rs1 = null;
            ResultSet rs = null;
            ResultSet rs2 = null;
            PreparedStatement ps = null;
                 BindingContext bctx = BindingContext.getCurrent();
                 BindingContainer bindings = bctx.getCurrentBindingsEntry();
          
                 
                 FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
            
              //  //System.out.println("xxxxxxxxx====="+ tm.getCollectionModel().getRowCount());
              //  //System.out.println("zzzzzzzzz===="+ tm.getAttributeFromRow(3, 2));
              //  //System.out.println("zzzzzzzzz===="+ tm.getAttributeFromRow(4, 5));
                
                
                
                
                 /*
                  * This looping throw the iterator.
                  */
                 String prodIdL = null; 
                 String maxCtfId = null;
                 String ctfMId  = null;
                 String date = null;
                 ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
                DCBindingContainer dc = (DCBindingContainer) bindings;
            DCIteratorBinding iterator =  dc.findIteratorBinding("CtfItemCtfMainVOIterator");
            
    //        ViewObject vo = am.createViewObjectFromQueryStmt("FMAINANDDATECTFMAINVO",
    //        " select facility_id,to_char(p_date,'dd/mm/yyyy') from ctf_main where ctf_main_id =  "+ctfMId);
                            ViewObject vo = am.createViewObjectFromQueryStmt("FMAINANDDATECTFMAINVO",
                           " select cmain.facility_id,to_char(cmain.p_date,'dd/mm/yyyy'), ,cmain.ctf_main_id from ctf_main cmain  ");
               
               
            String sqlvoBalanceQtyRcvVO =
                "select citem.CLOSING_BAL, citem.qty_received from ctf_item citem ";
           
            ViewObject voBalanceQtyRcvVO = am.createViewObjectFromQueryStmt("SLDKFDS",sqlvoBalanceQtyRcvVO);
            
            
            String sqlvoCtfMainIdAddedMonths =
            "select cmain.facility_id, cmain.to_char(p_date,'dd/mm/yyyy') from ctf_main cmain"; // where ctf_main_id = ?
           // sqlvoCtfMainIdAddedMonths += " where  facility_id = "+Integer.parseInt(facilityId)+" and to_char(add_months(p_date,1),'dd/mm/yyyy') = '"+date+"'";
            ViewObject voCtfMainIdAddedMonths = am.createViewObjectFromQueryStmt("FMafdsfaMAINVO",sqlvoCtfMainIdAddedMonths );
            
                 for( Row row : iterator.getAllRowsInRange()){
                    
                    ctfMId = locCtfMainId;
                     
                     prodIdL = row.getAttribute("ProdId").toString();
                    
                     
                             //System.out.println("prodid ===="+prodIdL);
                    
                             
                                            
                         
                         /*
                          * NOW WORKING ON CTF_MAIN TABLE BY CTF_MAIN_ID
                          */
                           
                           // was error
                             vo.setWhereClause("ctf_main_id = ?");
                             vo.setWhereClauseParam(1, ctfMId);
                             vo.executeQuery();
                         /*
                          * NOW WORKING ON THE BALANCE AND QUENTITY RECEIVED
                          */
                         
                        
                         
                         if(vo.hasNext()){
                             
                             Row mainRow = vo.next();
                             
                             //System.out.println("xxxxxxxxxxxxx pppppppppppppppppppppppp "+ mainRow.getAttribute(0)+"");
                             facilityId = 
                                  "" + mainRow.getAttribute(0);
                             date =
                              "" + mainRow.getAttribute(1);
                             //System.out.println("facility id ==== "+facilityId); 
                              //System.out.println("ctf Main id ==== "+ctfMId);
                              //System.out.println(" p_date ==== "+date);
                              
                              /*
                               * NOW WORKING ON CTF_MAIN TABLE WITH DIFIRENT CRITERIA
                               * WHICH IS P_DATE AND FACILITY_ID to get data of next month
                               */
                            
                            
                            //  String sqlvoCtfMainIdAddedMonths = MainInterface.CtfMainByFacilityIdAndMainId;
                             // sqlvoCtfMainIdAddedMonths += " where  facility_id = "+Integer.parseInt(facilityId)+" and to_char(add_months(p_date,1),'dd/mm/yyyy') = '"+date+"'";
                             // ViewObject voCtfMainIdAddedMonths = am.createViewObjectFromQueryStmt("FMafdsfaMAINVO",sqlvoCtfMainIdAddedMonths );
                             // voCtfMainIdAddedMonths.setWhereClause( );   
                              
                             voCtfMainIdAddedMonths.setWhereClause(" cmain.facility_id = ? and cmain.to_char(add_months(p_date,1),'dd/mm/yyyy') = ? ");
                             voCtfMainIdAddedMonths.setWhereClauseParam(1, Integer.parseInt(facilityId));
                             voCtfMainIdAddedMonths.setWhereClauseParam(2, date);
                             voCtfMainIdAddedMonths.executeQuery();
                             if(voCtfMainIdAddedMonths.hasNext()){
                                 Row row1 = voCtfMainIdAddedMonths.next();
                                 maxCtfId =  "" + row1.getAttribute(0);
                                 //System.out.println("maxctfmainid ==== "+maxCtfId);
                                 
                                 
                                 // heer
    //                             String sqlvoBalanceQtyRcvVO =  MainInterface.MainCtfBalanceByProductItemsByProduct;
    //                             sqlvoBalanceQtyRcvVO  +=  "where CTF_main_ID = "+Integer.parseInt(maxCtfId)+" and prod_id = "+(i);
    //                             ViewObject voBalanceQtyRcvVO = am.createViewObjectFromQueryStmt("SLDKFDS",sqlvoBalanceQtyRcvVO);
                                
                                
                                
                                
                                 voBalanceQtyRcvVO.setWhereClause(" citem.CTF_main_ID = ? and citem.prod_id = ? ");
                                 voBalanceQtyRcvVO.setWhereClauseParam(1, Integer.parseInt(maxCtfId));
                                 voBalanceQtyRcvVO.setWhereClauseParam(2, prodIdL);
                               
                               
                               
                               
                                 voBalanceQtyRcvVO.executeQuery();
                                 
                                 if(voBalanceQtyRcvVO.hasNext()){
                                     Row balanceRow = voBalanceQtyRcvVO.next();
                                     
                                     String closeLastBal =  "" + balanceRow.getAttribute(0);
                                     //System.out.println("last balance ===== "+closeLastBal);
                                    
                                    // tm.setAttributeInRow(i-1, 4, closeLastBal,true);
                                     row.setAttribute("ClosingBal", closeLastBal);
                                     
                                     String qtyReceiv = "" + balanceRow.getAttribute(1);
                                     //System.out.println("qtyReceiv ===== "+qtyReceiv);
                                    // tm.setAttributeInRow(i-1, 5, qtyReceiv,true);
                                    row.setAttribute("QtyReceived", qtyReceiv);
                                 }
                                 
                             }
                                 
                             voCtfMainIdAddedMonths.remove();
                              
                              
                              
                               
                         }else{
                            // tm.setAttributeInRow(i-1, 4, 0,true);
                            // tm.setAttributeInRow(i-1, 5, 0,true);
                            row.setAttribute("ClosingBal", 0);
                            row.setAttribute("QtyReceived", 0);
                         }
                             vo.remove();
                           
                             
                             
                             
                 }
               
            Configuration.releaseRootApplicationModule(am, true);
            ADFContext.getCurrent().getViewScope().put("lb", true);
             
            
            return null;
        }

        public void getCtfMainId(SelectionEvent selectionEvent) {
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            DCBindingContainer dc = (DCBindingContainer) bindings;
            DCIteratorBinding iterator =  dc.findIteratorBinding("CtfMainVOIterator");
            
            //////////////////////////////////////////////////
            /////////////////selection /////////////////////
            PreparedStatement ps2 = null;
            ResultSet rs2 = null;
            
            RichTable a_table = (RichTable) selectionEvent.getSource();
                CollectionModel   a_tableModel = (CollectionModel) a_table.getValue();
                JUCtrlHierBinding a_adfTableBinding = (JUCtrlHierBinding) a_tableModel.getWrappedData();
                DCIteratorBinding a_tableIteratorBinding = a_adfTableBinding.getDCIteratorBinding();
                Object a_selectedRowData = a_table.getSelectedRowData(); 
                JUCtrlHierNodeBinding a_nodeBinding = (JUCtrlHierNodeBinding) a_selectedRowData;
                Key a_rwKey = a_nodeBinding.getRowKey();
                a_tableIteratorBinding.setCurrentRowWithKey(a_rwKey.toStringFormat(true));  
            
            
            ////////////////////end selection/////////////////
            ///////////////////////////////////////////////
            FacesContext fc = FacesContext.getCurrentInstance();
                   ELContext elc = fc.getELContext();
                   ExpressionFactory ef = fc.getApplication().getExpressionFactory();
                   
                   RichTable _table = (RichTable) selectionEvent.getSource();
                  
                   CollectionModel   _tableModel = (CollectionModel) _table.getValue();
                   
                   JUCtrlHierBinding _adfTableBinding = (JUCtrlHierBinding) _tableModel.getWrappedData();
                  
                   DCIteratorBinding _tableIteratorBinding = _adfTableBinding.getDCIteratorBinding();
                   
                   
                   Object _selectedRowData = _table.getSelectedRowData();
                 
                   JUCtrlHierNodeBinding _nodeBinding = (JUCtrlHierNodeBinding) _selectedRowData;
                   
                   Key _rwKey = _nodeBinding.getRowKey();
                   _tableIteratorBinding.setCurrentRowWithKey(_rwKey.toStringFormat(true));        
                   ValueExpression ve2 = ef.createValueExpression(elc,"#{bindings.CtfMainVOIterator.currentRow}", Object.class);
                   Row r = (Row)ve2.getValue(elc);
                   Key k = r.getKey();
                   
                   
                  
                   
                   locCtfMainId = ""+k.getAttribute(0);
                   
                   if(locCtfMainId == null || locCtfMainId.equals("null") ){
                      // #{bindings.CtfMainId.inputValue}
                      ValueExpression ve15  = ef.createValueExpression(elc,"#{bindings.CtfMainId.inputValue}", Object.class); 
                      locCtfMainId = ""+ve15.getValue(elc);    
                   }
                  
                   
                   
                  
                    ValueExpression ve5  = ef.createValueExpression(elc,"#{pageFlowScope.sComplCode}", Object.class); 
                     ve5.setValue(elc, locCtfMainId);
                     //System.out.println("ctfmainid =======  "+locCtfMainId);
                     
            ADFContext.getCurrent().getSessionScope().put("mainId", locCtfMainId);
            ADFContext.getCurrent().getPageFlowScope().put("mainId",locCtfMainId);
            
            ValueExpression ve6  = ef.createValueExpression(elc,"#{pageFlowScope.isNotDir}", Object.class); 
            ve6.setValue(elc, true);
            
                     String wplace =  ""+r.getAttribute(11);
                     //System.out.println("wplace ============== "+wplace);
                     if( (""+(wplace.charAt(2))).equals("0") && (""+(wplace.charAt(3))).equals("0") && (""+(wplace.charAt(4))).equals("0") ){
                         //System.out.println("is directorate");
                         ve6.setValue(elc, false);
                     }



    ApplicationModule am =
    Configuration.createRootApplicationModule(amDef,config);
    String sql = 
        " select type_hierarchy from fac_type ft,facility f " +
        "where ft.fac_type_id = f.fac_type_id AND UPPER(f.FAC_CODE) = UPPER('"+wplace+"')";
    //ViewObject vo = am.createViewObjectFromQueryStmt("sdfdsf", MainInterface.TypeHierarchyByfacilityCode);
    ViewObject vo = am.createViewObjectFromQueryStmt("sdfdsf",sql);
    //vo.setWhereClause("FAC_TYPE.fac_type_id = FACILITY.fac_type_id AND UPPER(FACILITY.FAC_CODE) = UPPER('"+wplace+"')");
    if(vo.hasNext()){
        Row row = vo.next();
        int type = Integer.parseInt( "" + row.getAttribute(0) );
        
        if(type == 1 || type == 2){
            //System.out.println("is directorate ttttttttttttttttttttttttttt");
            ve6.setValue(elc, false);
        }
        
    }

    vo.remove();
    Configuration.releaseRootApplicationModule(am,true);

        ADFContext.getCurrent().getSessionScope().put("mainId",iterator.getCurrentRow().getAttribute("CtfMainId")+"");
            ADFContext.getCurrent().getPageFlowScope().put("mainId",iterator.getCurrentRow().getAttribute("CtfMainId")+"");
            
            ADFContext.getCurrent().getPageFlowScope().put("sFacName",iterator.getCurrentRow().getAttribute("FacName")+"");
            ADFContext.getCurrent().getPageFlowScope().put("sMonYear",iterator.getCurrentRow().getAttribute("PDate")+"");
//System.out.println(" xxxxxxxxxxx selected MainId is   :    "+ADFContext.getCurrent().getSessionScope().get("mainId")+"");
            
                     
                     //getData();
                     
                     
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
                    //System.out.println("The result set cannot be closed.");
                }
            }
            if (ps != null)
            {
                try
                {
                    ps.close();
                } catch (SQLException e)
                {
                    //System.out.println("The PreparedStatement cannot be closed.");
                }
            }
           

        }
        
        
        
        public static void closePS( Statement ps)
        {
            
            
            if (ps != null)
            {
                try
                {
                    ps.close();
                } catch (SQLException e)
                {
                    //System.out.println("The PreparedStatement cannot be closed.");
                }
            }
           

        }



    public String calculateValues() {
        PreparedStatement ps8 = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        ResultSet rs8 = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps4 = null;
        ResultSet rs3 = null;
        ResultSet rs5 = null;
        ResultSet rs4 = null;
        PreparedStatement ps5 = null;
        if(   ADFContext.getCurrent().getViewScope().get("lb")  == true ){
          
        
        BindingContext bctx = BindingContext.getCurrent();
        BindingContainer bindings = bctx.getCurrentBindingsEntry();
        //        FacesCtrlAttrsBinding x = (FacesCtrlAttrsBinding)bindings.get("Empno");
        //        System.out.println("xxxxxxxxxx="+x.getAttributeValue());
        
        FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
            int IteratorRowCount = screenViewIteratorFacesCtrlHierBindingRowCount(tm);
         System.out.println("ROW COUNT IS : "+ tm.getRowIterator().getRowCount());   
        
         //             tm.setCurrentRowAtIndex(3);
          //System.out.println("zzzzzzzzz===="+ tm.getAttributeFromRow(3, 2));
        for (int i = 0; i < IteratorRowCount; i++) {            
        


        int openBal =Integer.parseInt(""+tm.getAttributeFromRow(i, 4));
        int receipts = Integer.parseInt(""+tm.getAttributeFromRow(i, 5));
        int issue = Integer.parseInt(""+tm.getAttributeFromRow(i, 6));
        int adj = Integer.parseInt(""+tm.getAttributeFromRow(i, 8));
            int closeBal = 0;   
            String adjType = "";
            System.out.println("sssssssssssssssssssss "+openBal+"  "+receipts+"  "+issue+"  "+adj);
                try {
                
                  
               adjType = ""+tm.getAttributeFromRow(i, 7);
                    if(adjType.equals("null")||adjType==null){
                        adjType = "0";
                    }
                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa =====  "+adjType);
                ps8 = conn.prepareStatement("select ALWAYS_NEGATIVE from ADJ_TYPE where ADJ_TYPE_ID = ?");
                    ps8.setString(1, adjType);
                    rs8 = ps8.executeQuery();
                    
                    if(rs8.next()){
                        System.out.println("select ALWAYS_NEGATIVE from ADJ_TYPE where ADJ_TYPE_ID = ?"+adjType);
                    if("1".equals(rs8.getString(1))){
                         closeBal = openBal+receipts-issue-adj;
                            System.out.println("closebalance ===== "+closeBal);
                            tm.setAttributeInRow(i, 12, closeBal,true);
                    }else{
                    
                    
                    
         closeBal = openBal+receipts-issue+adj;
            System.out.println("closebalance ===== "+closeBal);
            tm.setAttributeInRow(i, 12, closeBal,true);
                    }
                    }else{
                        closeBal = openBal+receipts-issue;
                        tm.setAttributeInRow(i, 12, closeBal,true);
                        System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii======="+closeBal);
                    }
                    
               
                 System.out.println("facility id ==== "+facilityId); 
    System.out.println("facility_Id ======================================================"+facilityId);
              ps1 = conn.prepareStatement("select nvl(count(ctf_main_id),0) from ctf_main where facility_Id = ?");
                ps1.setString(1,facilityId);
                //ps1.setString(2, prodId);
                rs1 = ps1.executeQuery();
                rs1.next(); 
                int countCtfId = rs1.getInt(1);
                int sum = 0;
                int avg = 0;
                if (countCtfId == 0 ) {
                
                    tm.setAttributeInRow(i, 9, issue,true);
                    int qtyReq = (2*issue)-closeBal;
                    tm.setAttributeInRow(i, 10, qtyReq,true);
                    System.out.println("sum of issue countCtfId == 0 === "+sum);
                    
                } else if(countCtfId < 3){
                    ps4 = conn.prepareStatement("select CTF_MAIN_ID from ctf_main where facility_id = ? ");
                    ps4.setString(1, facilityId);
                    rs4 = ps4.executeQuery();
                    while(rs4.next()){
                ps3 = conn.prepareStatement("select ISSUES from  ctf_item where CTF_MAIN_ID     = ? and prod_id = ?");
                ps3.setString(1,rs4.getString(1));
                ps3.setInt(2, i+1);
                rs3 = ps3.executeQuery(); 
                   
                    while (rs3.next()){
                      sum += rs3.getInt(1);
                        
                       
                    }
                    }
                    sum += issue;
                System.out.println("sum of issue countCtfId <<<<<<< 3 === "+sum);
                    avg = (int)Math.ceil((double)sum / (countCtfId));
                    tm.setAttributeInRow(i, 9, avg,true);
                    int qtyReq = (2*avg)-closeBal;
                    tm.setAttributeInRow(i, 10, qtyReq,true);
                } else{
                   ps3 = conn.prepareStatement("select ctf_main_id from (select ctf_main_id from ctf_main where facility_id = ? and p_date < (select p_date from ctf_main where ctf_main_id = ?) order by p_date desc) where rownum <3");
                  ps3.setString(1,facilityId);
                  ps3.setString(2, locCtfMainId);
    System.out.println("ctf_main_id=============================================================="+locCtfMainId);
                 
                   rs3 = ps3.executeQuery(); 
                     
                      while (rs3.next()){
                          ps5 = conn.prepareStatement("select ISSUES from ctf_item where ctf_main_id = ? and prod_id = ?");
                          ps5.setString(1, rs3.getString(1));
                          ps5.setInt(2, i+1);
                          rs5 = ps5.executeQuery();
                          while(rs5.next()){
                          
                        sum += rs5.getInt(1);
                          
                          }
                      }
                      sum += issue;
                  System.out.println("sum of issue countCtfId >>>>>> 3 === "+sum);
                      avg = (int)Math.ceil((double)sum / 3 );
                    
                    tm.setAttributeInRow(i, 9, avg,true);
                    int qtyReq = (2*avg)-closeBal;
                    tm.setAttributeInRow(i, 10, qtyReq,true);
                }
                
               
            } catch (SQLException sqle) {
                // TODO: Add catch code
                sqle.printStackTrace();
                }finally{
                    closeRsAndPs(rs1, ps1);
                    closeRsAndPs(rs3, ps3);
                    closeRsAndPs(rs4, ps4);
                    closeRsAndPs(rs5, ps5);
                    closeRsAndPs(rs8, ps8);
                    
                }
        }   
        
        ADFContext.getCurrent().getViewScope().put("cv", true);
        
        }
        else{
            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            lbPopup.show(hints);
            
        }
        
        return null;
    }



        public String calculateValuesOLD() {
            PreparedStatement ps8 = null;
            PreparedStatement ps1 = null;
            ResultSet rs1 = null;
            ResultSet rs8 = null;
            PreparedStatement ps3 = null;
            PreparedStatement ps4 = null;
            ResultSet rs3 = null;
            ResultSet rs5 = null;
            ResultSet rs4 = null;
            PreparedStatement ps5 = null;
            if(   ADFContext.getCurrent().getViewScope().get("lb")  == true ){
              
            
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            //        FacesCtrlAttrsBinding x = (FacesCtrlAttrsBinding)bindings.get("Empno");
            //        //System.out.println("xxxxxxxxxx="+x.getAttributeValue());
            
            FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
                int IteratorRowCount = screenViewIteratorFacesCtrlHierBindingRowCount(tm);
             //             tm.setCurrentRowAtIndex(3);
              ////System.out.println("zzzzzzzzz===="+ tm.getAttributeFromRow(3, 2));
            for (int i = 0; i < IteratorRowCount ; i++) {            
            


            int openBal =Integer.parseInt(""+tm.getAttributeFromRow(i, 4));
            int receipts = Integer.parseInt(""+tm.getAttributeFromRow(i, 5));
            int issue = Integer.parseInt(""+tm.getAttributeFromRow(i, 6));
            int adj = Integer.parseInt(""+tm.getAttributeFromRow(i, 8));
                int closeBal = 0;   
                String adjType = "";
                //System.out.println("sssssssssssssssssssss "+openBal+"  "+receipts+"  "+issue+"  "+adj);
                    try {
                    
                      
                   adjType = ""+tm.getAttributeFromRow(i, 7);
                        if(adjType.equals("null")||adjType==null){
                            adjType = "0";
                        }
                        //System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa =====  "+adjType);
                    ps8 = conn.prepareStatement("select ALWAYS_NEGATIVE from ADJ_TYPE where ADJ_TYPE_ID = ?");
                        ps8.setString(1, adjType);
                        rs8 = ps8.executeQuery();
                        
                        if(rs8.next()){
                            //System.out.println("select ALWAYS_NEGATIVE from ADJ_TYPE where ADJ_TYPE_ID = ?"+adjType);
                        if("1".equals(rs8.getString(1))){
                             closeBal = openBal+receipts-issue-adj;
                                //System.out.println("closebalance ===== "+closeBal);
                                tm.setAttributeInRow(i, 12, closeBal,true);
                        }else{
                        
                        
                        
             closeBal = openBal+receipts-issue+adj;
                //System.out.println("closebalance ===== "+closeBal);
                tm.setAttributeInRow(i, 12, closeBal,true);
                        }
                        }else{
                            closeBal = openBal+receipts-issue;
                            tm.setAttributeInRow(i, 12, closeBal,true);
                            //System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii======="+closeBal);
                        }
                        
                   
                     //System.out.println("facility id ==== "+facilityId); 
        //System.out.println("facility_Id ======================================================"+facilityId);
                  ps1 = conn.prepareStatement("select nvl(count(ctf_main_id),0) from ctf_main where facility_Id = ?");
                    ps1.setString(1,facilityId);
                    //ps1.setString(2, prodId);
                    rs1 = ps1.executeQuery();
                    rs1.next(); 
                    int countCtfId = rs1.getInt(1);
                    int sum = 0;
                    int avg = 0;
                    if (countCtfId == 0 ) {
                    
                        tm.setAttributeInRow(i, 9, issue,true);
                        int qtyReq = (2*issue)-closeBal;
                        tm.setAttributeInRow(i, 10, qtyReq,true);
                        //System.out.println("sum of issue countCtfId == 0 === "+sum);
                        
                    } else if(countCtfId < 3){
                        ps4 = conn.prepareStatement("select CTF_MAIN_ID from ctf_main where facility_id = ? ");
                        ps4.setString(1, facilityId);
                        rs4 = ps4.executeQuery();
                        while(rs4.next()){
                    ps3 = conn.prepareStatement("select ISSUES from  ctf_item where CTF_MAIN_ID     = ? and prod_id = ?");
                    ps3.setString(1,rs4.getString(1));
                    ps3.setInt(2, i+1);
                    rs3 = ps3.executeQuery(); 
                       
                        while (rs3.next()){
                          sum += rs3.getInt(1);
                            
                           
                        }
                        }
                        sum += issue;
                    //System.out.println("sum of issue countCtfId <<<<<<< 3 === "+sum);
                        avg = (int)Math.ceil((double)sum / (countCtfId));
                        tm.setAttributeInRow(i, 9, avg,true);
                        int qtyReq = (2*avg)-closeBal;
                        tm.setAttributeInRow(i, 10, qtyReq,true);
                    } else{
                       ps3 = conn.prepareStatement("select ctf_main_id from (select ctf_main_id from ctf_main where facility_id = ? and p_date < (select p_date from ctf_main where ctf_main_id = ?) order by p_date desc) where rownum <3");
                      ps3.setString(1,facilityId);
                      ps3.setString(2, locCtfMainId);
        //System.out.println("ctf_main_id=============================================================="+locCtfMainId);
                     
                       rs3 = ps3.executeQuery(); 
                         
                          while (rs3.next()){
                              ps5 = conn.prepareStatement("select ISSUES from ctf_item where ctf_main_id = ? and prod_id = ?");
                              ps5.setString(1, rs3.getString(1));
                              ps5.setInt(2, i+1);
                              rs5 = ps5.executeQuery();
                              while(rs5.next()){
                              
                            sum += rs5.getInt(1);
                              
                              }
                          }
                          sum += issue;
                      //System.out.println("sum of issue countCtfId >>>>>> 3 === "+sum);
                          avg = (int)Math.ceil((double)sum / 3 );
                        
                        tm.setAttributeInRow(i, 9, avg,true);
                        int qtyReq = (2*avg)-closeBal;
                        tm.setAttributeInRow(i, 10, qtyReq,true);
                    }
                    
                   
                } catch (SQLException sqle) {
                    // TODO: Add catch code
                    sqle.printStackTrace();
                    }finally{
                        closeRsAndPs(rs1, ps1);
                        closeRsAndPs(rs3, ps3);
                        closeRsAndPs(rs4, ps4);
                        closeRsAndPs(rs5, ps5);
                        closeRsAndPs(rs8, ps8);
                        
                    }
            }   
            
            ADFContext.getCurrent().getViewScope().put("cv", true);
            
            }
            else{
                RichPopup.PopupHints hints = new RichPopup.PopupHints();
                lbPopup.show(hints);
                
            }
            
            return null;
        }

        public String forImad() {
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            //        FacesCtrlAttrsBinding x = (FacesCtrlAttrsBinding)bindings.get("Empno");
            //        //System.out.println("xxxxxxxxxx="+x.getAttributeValue());
            
            FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
            
             //             tm.setCurrentRowAtIndex(3);
              ////System.out.println("zzzzzzzzz===="+ tm.getAttributeFromRow(3, 2));
         


            //System.out.println(tm.getAttributeFromRow(0, 0));
            //System.out.println(tm.getAttributeFromRow(0, 1));
            //System.out.println(tm.getAttributeFromRow(0, 2));
            //System.out.println(tm.getAttributeFromRow(0, 3));
            //System.out.println(tm.getAttributeFromRow(0, 4));
            //System.out.println(tm.getAttributeFromRow(0, 5));
            //System.out.println(tm.getAttributeFromRow(0, 6));
            //System.out.println(tm.getAttributeFromRow(0, 7));
            //System.out.println(tm.getAttributeFromRow(0, 8));
            //System.out.println(tm.getAttributeFromRow(0, 9));
            //System.out.println(tm.getAttributeFromRow(0, 10));
            //System.out.println(tm.getAttributeFromRow(0, 11));
            //System.out.println(tm.getAttributeFromRow(0, 12));
            return null;
        }
        
        public void getData(ActionEvent actionEvent) {
            PreparedStatement ps2 = null;
            ResultSet rs2 = null;
            PreparedStatement ps3 = null;
            ResultSet rs3 = null;
            FacesContext fc = FacesContext.getCurrentInstance();
                  ELContext elc = fc.getELContext();
                  ExpressionFactory ef = fc.getApplication().getExpressionFactory();

            if(locCtfMainId == null || locCtfMainId.equals("null") || locCtfMainId.equals("") ){
             //System.out.println("xxxxxxxxxxxxxxxxxx");
             
               ValueExpression ve15  = ef.createValueExpression(elc,"#{bindings.CtfMainId.inputValue}", Object.class); 
               locCtfMainId = ""+ve15.getValue(elc);    
            }
            
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            DCBindingContainer bc = (DCBindingContainer)bindings;
            DCIteratorBinding dc = bc.findIteratorBinding("CtfMainVOIterator");
            locCtfMainId = dc.getCurrentRow().getAttribute("CtfMainId")+"";
            System.out.println(" mainId newwwwwwwwwwwwwwwwwwwwwwwwwww "+locCtfMainId);
                  
            
            try {
                

                 ps2 = conn.prepareStatement("select ctf_main_id,FAC_CODE,f.FAC_NAME,ft.FAC_NAME,f.FAC_TYPE_ID,DE_STAFF,LC_STAFF,to_char(P_DATE,'mm ,yyyy'),ctf_comments from ctf_main cm,facility f,FAC_TYPE ft where f.FAC_TYPE_ID = ft.FAC_TYPE_ID and cm.facility_id = f.facility_id and cm.ctf_main_id = ?");
                
        
    //System.out.println("aaaaaaaaaaaaaaaaaaaa=========================="+locCtfMainId);
            ps2.setString(1, locCtfMainId);
            
            rs2 = ps2.executeQuery();
            rs2.next();
            ValueExpression ve1  = ef.createValueExpression(elc,"#{pageFlowScope.sFacCode}", Object.class); 
             ve1.setValue(elc, rs2.getString(2));
            //ADFContext.getCurrent().getSessionScope().put("sFacCode", rs2.getString(2) + "");
                
                
            ValueExpression ve6  = ef.createValueExpression(elc,"#{pageFlowScope.sFacName}", Object.class); 
             ve6.setValue(elc, rs2.getString(3));
                //ADFContext.getCurrent().getSessionScope().put("sFacName", rs2.getString(3) + "");
                //ADFContext.getCurrent().getSessionScope().put("ss", "hiiiiiiiiii" + "");
                ////System.out.println("The Facility Name is "+ ADFContext.getCurrent().getSessionScope().get("sFacName")+""  );
                
            ValueExpression ve7  = ef.createValueExpression(elc,"#{pageFlowScope.sTypeDesc}", Object.class); 
             ve7.setValue(elc, rs2.getString(4));
                ADFContext.getCurrent().getSessionScope().put("sTypeDesc", rs2.getString(4) + "");
                
            
            ValueExpression ve2  = ef.createValueExpression(elc,"#{pageFlowScope.sEnteredBy}", Object.class); 
             ve2.setValue(elc, rs2.getString(6));
               // ADFContext.getCurrent().getSessionScope().put("sEnteredBy", rs2.getString(6) + "");
                
            
            ValueExpression ve3  = ef.createValueExpression(elc,"#{pageFlowScope.sChangeBy}", Object.class); 
             ve3.setValue(elc, rs2.getString(7));
               // ADFContext.getCurrent().getSessionScope().put("sChangeBy", rs2.getString(7) + "");
            
            ValueExpression ve5  = ef.createValueExpression(elc,"#{pageFlowScope.sMonYear}", Object.class); 
             ve5.setValue(elc, rs2.getString(8));
               // ADFContext.getCurrent().getSessionScope().put("sMonYear", rs2.getString(8) + "");
                

                ValueExpression ve55  = ef.createValueExpression(elc,"#{pageFlowScope.sComments}", Object.class); 
                 ve55.setValue(elc, rs2.getString(9));
               // ADFContext.getCurrent().getSessionScope().put("sComments", rs2.getString(9) + "");
            
            
            
            ps3 = conn.prepareStatement("select sysdate from dual");
            rs3 = ps3.executeQuery();
            
            rs3.next();
            ValueExpression ve8  = ef.createValueExpression(elc,"#{pageFlowScope.sSysDate}", Object.class); 
             ve8.setValue(elc, rs3.getString(1));
               // ADFContext.getCurrent().getSessionScope().put("sSysDate", rs3.getString(1) + "");
            //System.out.println("The Date is "+ ADFContext.getCurrent().getSessionScope().get("sSysDate")+""  );
            
       
            } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
            }finally{
                closeRsAndPs(rs2, ps2);
                closeRsAndPs(rs3, ps3);
               
               
            }
        }





        public String getData() {
            PreparedStatement ps2 = null;
            ResultSet rs2 = null;
            PreparedStatement ps3 = null;
            ResultSet rs3 = null;
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
    //            UIDSConnection connClass = new UIDSConnection();
    //              conn = connClass.conn;
                
                
                
                
            ps2 = conn.prepareStatement("select ctf_main_id,FAC_CODE,f.FAC_NAME,ft.FAC_NAME,f.FAC_TYPE_ID,DE_STAFF,LC_STAFF,to_char(P_DATE,'month,yyyy'),ctf_comments from ctf_main cm,facility f,FAC_TYPE ft where f.FAC_TYPE_ID = ft.FAC_TYPE_ID and cm.facility_id = f.facility_id and cm.ctf_main_id = ?");
                
        
        //System.out.println("aaaaaaaaaaaaaaaaaaaa=========================="+locCtfMainId);
            ps2.setString(1, locCtfMainId);
            
            rs2 = ps2.executeQuery();
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
            
            
            
            ps3 = conn.prepareStatement("select sysdate from dual");
            rs3 = ps3.executeQuery();
            
            rs3.next();
            ValueExpression ve8  = ef.createValueExpression(elc,"#{pageFlowScope.sSysDate}", Object.class); 
             ve8.setValue(elc, rs3.getString(1));
                ADFContext.getCurrent().getSessionScope().put("sSysDate", rs3.getString(1) + "");
            //System.out.println("The Date is "+ ADFContext.getCurrent().getSessionScope().get("sSysDate")+""  );
            
        
            } catch (SQLException sqle) {
            // TODO: Add catch code
            sqle.printStackTrace();
            }finally{
                closeRsAndPs(rs2, ps2);
                closeRsAndPs(rs3, ps3);
                
                
            }
            
            //return "goItem";
            return null;
        }











    public String nextMonth() {
        String code = ADFContext.getCurrent().getPageFlowScope().get("sFacCode")+"";
        String date = ADFContext.getCurrent().getPageFlowScope().get("sMonYear")+"";
        String nextMonthsSQLSTMT = 
            "SELECT ctf_main_id, \n" + 
            "  FAC_CODE, \n" + 
            "  f.FAC_NAME, \n" + 
            "  ft.FAC_NAME, \n" + 
            "  f.FAC_TYPE_ID, \n" + 
            "  DE_STAFF, \n" + 
            "  LC_STAFF, \n" + 
            "  TO_CHAR(P_DATE,'mm ,yyyy'), \n" + 
            "  ctf_comments, sysdate \n" + 
            "FROM ctf_main cm, \n" + 
            "  facility f, \n" + 
            "  FAC_TYPE ft \n" + 
            "WHERE f.FAC_TYPE_ID= ft.FAC_TYPE_ID \n" + 
            "AND cm.facility_id = f.facility_id \n" + 
            "AND p_date        IN \n" + 
            "  (SELECT c.p_date \n" + 
            "  FROM ctf_main c \n" + 
            "  WHERE c.p_date  = add_months((to_date(?,'mm/yyyy')), 1) \n" + 
            "  AND cm.facility_id                       IN \n" + 
            "    (SELECT ff.facility_id \n" + 
            "    FROM facility ff \n" + 
            "    WHERE ff.facility_id = cm.facility_id \n" + 
            "    AND ff.fac_code      =  ? \n" + 
            "    ) \n" + 
            "  )";
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        //PreparedStatement ps4 = null;
        //ResultSet rs4 = null;
        FacesContext fc = FacesContext.getCurrentInstance();
              ELContext elc = fc.getELContext();
              ExpressionFactory ef = fc.getApplication().getExpressionFactory();
              
        
        try {
            
           
            ps2 = conn.prepareStatement(nextMonthsSQLSTMT);
            ps2.setString(1, date);
            ps2.setString(2,code);
            rs2 = ps2.executeQuery();
            if(rs2.next()){
                
                locCtfMainId = rs2.getString(1);
                System.out.println(" XXXXXXXXXXXXXXX NEXT MAIN_ID IS : "+locCtfMainId);
                
                                ValueExpression ve1  = ef.createValueExpression(elc,"#{pageFlowScope.sFacCode}", Object.class); 
                                ve1.setValue(elc, rs2.getString(2));
                                
                                ValueExpression ve9  = ef.createValueExpression(elc,"#{pageFlowScope.sComments}", Object.class); 
                                 ve9.setValue(elc, rs2.getString(9));
                                ctfCommentsTxtNonDB.setValue(rs2.getString(9)+"");
                                AdfFacesContext.getCurrentInstance().addPartialTarget(this.getCtfCommentsTxtNonDB());
                            ValueExpression ve6  = ef.createValueExpression(elc,"#{pageFlowScope.sFacName}", Object.class); 
                             ve6.setValue(elc, rs2.getString(3));
                            
                            ValueExpression ve7  = ef.createValueExpression(elc,"#{pageFlowScope.sTypeDesc}", Object.class); 
                             ve7.setValue(elc, rs2.getString(4));
                            
                            ValueExpression ve2  = ef.createValueExpression(elc,"#{pageFlowScope.sEnteredBy}", Object.class); 
                             ve2.setValue(elc, rs2.getString(6));
                            
                            ValueExpression ve3  = ef.createValueExpression(elc,"#{pageFlowScope.sChangeBy}", Object.class); 
                             ve3.setValue(elc, rs2.getString(7));
                            
                            ValueExpression ve5  = ef.createValueExpression(elc,"#{pageFlowScope.sMonYear}", Object.class); 
                             ve5.setValue(elc, rs2.getString(8));
                            ValueExpression ve8  = ef.createValueExpression(elc,"#{pageFlowScope.sSysDate}", Object.class); 
                             ve8.setValue(elc, rs2.getString(10));
                            
                            ///////////////////////
                            //System.out.println("Previous ctf main id for ctf item is =============="+rs4.getString(1));
                            ValueExpression v1 = ef.createValueExpression
                                (elc, "#{pageFlowScope.ctfMainId}", Object.class);  
                            v1.setValue(elc,rs2.getString(1));
                  
                ADFContext.getCurrent().getPageFlowScope().put("mainId", rs2.getString(1));
                MethodExpression me = ef.createMethodExpression(elc, "#{bindings.setCurrentRowWithKeyValue.execute}", Object.class, new Class[0]);
                me.invoke(elc, new Object[0]);
                
            }
//            locCtfMainId = ADFContext.getCurrent().getPageFlowScope().get("mainId")+"";
//              
//        ps2 = conn.prepareStatement("select ctf_main_id,FAC_CODE,f.FAC_NAME,ft.FAC_NAME,f.FAC_TYPE_ID,DE_STAFF,LC_STAFF,to_char(add_months(to_date(to_char(P_DATE,'mm/yyyy'),'mm/yyyy'),1),'mm/yyyy') from ctf_main cm,facility f,FAC_TYPE ft where f.FAC_TYPE_ID = ft.FAC_TYPE_ID and cm.facility_id = f.facility_id and cm.ctf_main_id = ?");
//            
//        System.out.println("aaaaaaaaaaaaaaaaaaaa=========================="+locCtfMainId);
//        ps2.setString(1, locCtfMainId);
//          
//        rs2 = ps2.executeQuery();
//        rs2.next();
//            
//            System.out.println("aaaaaaaaaaaaaaaaaaaa=========================="+rs2.getString(2));
//            System.out.println("aaaaaaaaaaaaaaaaaaaa=========================="+rs2.getString(8));
//            System.out.println("******* changing month from month to mm *****|");
//         //   PreparedStatement ps4 = conn.prepareStatement("select ctf_main_id,FAC_CODE,f.FAC_NAME,ft.FAC_NAME,f.FAC_TYPE_ID,DE_STAFF,LC_STAFF,to_char(P_DATE,'month ,yyyy'),ctf_comments from ctf_main cm,facility f,FAC_TYPE ft where f.FAC_TYPE_ID = ft.FAC_TYPE_ID and cm.facility_id = f.facility_id and fac_code = ? and to_char(p_date,'mm/yyyy') = ? ");
//            ps4 = conn.prepareStatement("select ctf_main_id,FAC_CODE," +
//                "f.FAC_NAME,ft.FAC_NAME,f.FAC_TYPE_ID,DE_STAFF," +
//                "LC_STAFF,to_char(P_DATE,'mm ,yyyy'),ctf_comments " +
//                "from ctf_main cm,facility f,FAC_TYPE ft " +
//                "where f.FAC_TYPE_ID = ft.FAC_TYPE_ID " +
//                "and cm.facility_id = f.facility_id " +
//                "and fac_code = ? and to_char(p_date,'mm/yyyy') = ? ");
//            ps4.setString(1, rs2.getString(2));
//            ps4.setString(2, rs2.getString(8));
//            
//            rs4 = ps4.executeQuery();
//            rs4.next();
//            
//            locCtfMainId = rs4.getString(1);
//            
//            
//        ValueExpression ve1  = ef.createValueExpression(elc,"#{pageFlowScope.sFacCode}", Object.class); 
//         ve1.setValue(elc, rs4.getString(2));
//            ValueExpression ve9  = ef.createValueExpression(elc,"#{pageFlowScope.sComments}", Object.class); 
//             ve9.setValue(elc, rs4.getString(9));
//            ctfCommentsTxtNonDB.setValue(rs4.getString(9)+"");
//         AdfFacesContext.getCurrentInstance().addPartialTarget(this.getCtfCommentsTxtNonDB());
//            //getDBTransaction().getSession().getUserData().get("UserName"); 
//         //   ADFContext.getCurrent().getSessionScope().put("sComments", rs4.getString(9)+"");
//        
//        ValueExpression ve6  = ef.createValueExpression(elc,"#{pageFlowScope.sFacName}", Object.class); 
//         ve6.setValue(elc, rs4.getString(3));
//        
//        ValueExpression ve7  = ef.createValueExpression(elc,"#{pageFlowScope.sTypeDesc}", Object.class); 
//         ve7.setValue(elc, rs4.getString(4));
//        
//        ValueExpression ve2  = ef.createValueExpression(elc,"#{pageFlowScope.sEnteredBy}", Object.class); 
//         ve2.setValue(elc, rs4.getString(6));
//        
//        ValueExpression ve3  = ef.createValueExpression(elc,"#{pageFlowScope.sChangeBy}", Object.class); 
//         ve3.setValue(elc, rs4.getString(7));
//        
//        ValueExpression ve5  = ef.createValueExpression(elc,"#{pageFlowScope.sMonYear}", Object.class); 
//         ve5.setValue(elc, rs4.getString(8));
//        
//        PreparedStatement ps3 = conn.prepareStatement("select sysdate from dual");
//        ResultSet rs3 = ps3.executeQuery();
//        
//        rs3.next();
//        ValueExpression ve8  = ef.createValueExpression(elc,"#{pageFlowScope.sSysDate}", Object.class); 
//         ve8.setValue(elc, rs3.getString(1));
//        
//        
//        ///////////// #{sessionScope.ctfMainId}
//            System.out.println("Next ctf main id for ctf item is =============="+rs4.getString(1));
//            ValueExpression v1 = ef.createValueExpression(elc, "#{pageFlowScope.ctfMainId}", Object.class);  
//            v1.setValue(elc,rs4.getString(1));
    
            
           // ADFContext.getCurrent().getSessionScope().put("mainId", locCtfMainId);
//            ADFContext.getCurrent().getPageFlowScope().put("mainId", locCtfMainId);
//        
//        
//            MethodExpression me = ef.createMethodExpression(elc, "#{bindings.setCurrentRowWithKeyValue.execute}", Object.class, new Class[0]);
//            me.invoke(elc, new Object[0]);
//            MethodExpression meExeItems = ef.createMethodExpression(elc, "#{bindings.ExecuteItemsReport.execute}", Object.class, new Class[0]);
//            meExeItems.invoke(elc, new Object[0]);
        } catch (SQLException sqle) {
        // TODO: Add catch code
        sqle.printStackTrace();
        }finally{
            closeRsAndPs(rs2, ps2);
            //closeRsAndPs(rs4, ps4);
            
        }


    //commentView111.resetValue();

        return null;
    }

        public String previousMonth() {
            String code = ADFContext.getCurrent().getPageFlowScope().get("sFacCode")+"";
                   String date = ADFContext.getCurrent().getPageFlowScope().get("sMonYear")+"";
                   String previousMonthsSQLSTMT = 
           
                "SELECT ctf_main_id, \n" + 
                "  FAC_CODE, \n" + 
                "  f.FAC_NAME, \n" + 
                "  ft.FAC_NAME, \n" + 
                "  f.FAC_TYPE_ID, \n" + 
                "  DE_STAFF, \n" + 
                "  LC_STAFF, \n" + 
                "  TO_CHAR(P_DATE,'mm ,yyyy'), \n" + 
                "  ctf_comments, sysdate \n" + 
                "FROM ctf_main cm, \n" + 
                "  facility f, \n" + 
                "  FAC_TYPE ft \n" + 
                "WHERE f.FAC_TYPE_ID= ft.FAC_TYPE_ID \n" + 
                "AND cm.facility_id = f.facility_id \n" + 
                "AND p_date        IN \n" + 
                "  (SELECT c.p_date \n" + 
                "  FROM ctf_main c \n" + 
                "  WHERE c.p_date  = add_months((to_date(?,'mm/yyyy')), -1) \n" + 
                "  AND cm.facility_id                       IN \n" + 
                "    (SELECT ff.facility_id \n" + 
                "    FROM facility ff \n" + 
                "    WHERE ff.facility_id = cm.facility_id \n" + 
                "    AND ff.fac_code      =  ? \n" + 
                "    ) \n" + 
                "  )";
                   
            PreparedStatement ps2 = null;
            ResultSet rs2  = null;
           // PreparedStatement ps3 = null;
           //ResultSet rs3  = null;
            //PreparedStatement ps4 = null;
            //ResultSet rs4  = null;

            FacesContext fc = FacesContext.getCurrentInstance();
                  ELContext elc = fc.getELContext();
                  ExpressionFactory ef = fc.getApplication().getExpressionFactory();
                  
            
            try {
               ps2 = conn.prepareStatement(previousMonthsSQLSTMT);
                          ps2.setString(1, date);
                          ps2.setString(2,code);
                          rs2 = ps2.executeQuery();
                          if(rs2.next()){
                              
                              locCtfMainId = rs2.getString(1);
                              System.out.println(" XXXXXXXXXXXXXXX Previous MAIN_ID IS : "+locCtfMainId);
                              
                              ValueExpression ve1  = ef.createValueExpression(elc,"#{pageFlowScope.sFacCode}", Object.class); 
                              ve1.setValue(elc, rs2.getString(2));
                              
                              ValueExpression ve9  = ef.createValueExpression(elc,"#{pageFlowScope.sComments}", Object.class); 
                               ve9.setValue(elc, rs2.getString(9));
                              ctfCommentsTxtNonDB.setValue(rs2.getString(9)+"");
                              AdfFacesContext.getCurrentInstance().addPartialTarget(this.getCtfCommentsTxtNonDB());
                              ValueExpression ve6  = ef.createValueExpression(elc,"#{pageFlowScope.sFacName}", Object.class);
                              ve6.setValue(elc, rs2.getString(3));
                              
                              ValueExpression ve7  = ef.createValueExpression(elc,"#{pageFlowScope.sTypeDesc}", Object.class);
                              ve7.setValue(elc, rs2.getString(4));
                              
                              ValueExpression ve2  = ef.createValueExpression(elc,"#{pageFlowScope.sEnteredBy}", Object.class);
                              ve2.setValue(elc, rs2.getString(6));
                              
                              ValueExpression ve3  = ef.createValueExpression(elc,"#{pageFlowScope.sChangeBy}", Object.class);
                              ve3.setValue(elc, rs2.getString(7));
                              
                              ValueExpression ve5  = ef.createValueExpression(elc,"#{pageFlowScope.sMonYear}", Object.class);
                              ve5.setValue(elc, rs2.getString(8));
                              ValueExpression ve8  = ef.createValueExpression(elc,"#{pageFlowScope.sSysDate}", Object.class);
                              ve8.setValue(elc, rs2.getString(10));
                              
                              ///////////////////////
                              //System.out.println("Previous ctf main id for ctf item is =============="+rs4.getString(1));
                              ValueExpression v1 = ef.createValueExpression
                              (elc, "#{pageFlowScope.ctfMainId}", Object.class);  
                              v1.setValue(elc,rs2.getString(1));
                              
                              ADFContext.getCurrent().getPageFlowScope().put("mainId", rs2.getString(1));
                              MethodExpression me = ef.createMethodExpression(elc, "#{bindings.setCurrentRowWithKeyValue.execute}", Object.class, new Class[0]);
                              me.invoke(elc, new Object[0]);
                              
                              
                              AdfFacesContext.getCurrentInstance().addPartialTarget(this.getItemsTable());
                          }else{
                              ADFContext.getCurrent().getPageFlowScope().put("mainId", locCtfMainId);
                              MethodExpression me = ef.createMethodExpression(elc, "#{bindings.setCurrentRowWithKeyValue.execute}", Object.class, new Class[0]);
                              me.invoke(elc, new Object[0]);
                              
                              
                              AdfFacesContext.getCurrentInstance().addPartialTarget(this.getItemsTable());   
                          }
                
//           String code = ADFContext.getCurrent().getPageFlowScope().get("sFacCode")+""    ;
//            ps2 = conn.prepareStatement("select ctf_main_id," +
//                "FAC_CODE,f.FAC_NAME,ft.FAC_NAME,f.FAC_TYPE_ID," +
//                "DE_STAFF,LC_STAFF," +
//                "to_char(add_months(to_date(to_char(P_DATE,'mm/yyyy')" +
//                ",'mm/yyyy'),-1),'mm/yyyy') " +
//                "from ctf_main cm,facility f,FAC_TYPE ft " +
//                "where f.FAC_TYPE_ID = ft.FAC_TYPE_ID " +
//                "and cm.facility_id = f.facility_id " +
//                "and cm.ctf_main_id = ? " +
//                "and f.fac_code = ?");
//                locCtfMainId = 
//                ADFContext.getCurrent().getPageFlowScope().get("mainId").toString();
//            //System.out.println("aaaaaaaaaaaaaaaaaaaa=========================="+locCtfMainId);
//               // ADFContext.getCurrent().getSessionScope().put("mainId", locCtfMainId);
//               
//            ps2.setString(1, locCtfMainId);
//            ps2.setString(2,code);
//            rs2 = ps2.executeQuery();
//                if(rs2.next()){
//               
//                
//                //System.out.println("aaaaaaaaaaaaaaaaaaaa=========================="+rs2.getString(2));
//                //System.out.println("aaaaaaaaaaaaaaaaaaaa=========================="+rs2.getString(8));
//               
//               
//               //changing date to mm/yyyy
//              //  PreparedStatement ps4 = conn.prepareStatement("select ctf_main_id,FAC_CODE,f.FAC_NAME,ft.FAC_NAME,f.FAC_TYPE_ID,DE_STAFF,LC_STAFF,to_char(P_DATE,'month ,yyyy'),ctf_comments from ctf_main cm,facility f,FAC_TYPE ft where f.FAC_TYPE_ID = ft.FAC_TYPE_ID and cm.facility_id = f.facility_id and fac_code = ? and to_char(p_date,'mm/yyyy') = ? ");
//                ps4 = conn.prepareStatement("select ctf_main_id,FAC_CODE,f.FAC_NAME,ft.FAC_NAME,f.FAC_TYPE_ID,DE_STAFF,LC_STAFF,to_char(P_DATE,'mm ,yyyy'),ctf_comments from ctf_main cm,facility f,FAC_TYPE ft where f.FAC_TYPE_ID = ft.FAC_TYPE_ID and cm.facility_id = f.facility_id and fac_code = ? and to_char(p_date,'mm/yyyy') = ? ");
//                ps4.setString(1, rs2.getString(2));
//                ps4.setString(2, rs2.getString(8));
//                
//                rs4 = ps4.executeQuery();
//                
//                
//                
//                rs4.next();
//                
//                locCtfMainId = rs4.getString(1);
//                //System.out.println("************  after change month to  mm/yyy from month/yyyy ******");
//                //ADFContext.getCurrent().getSessionScope().put("mainId", locCtfMainId);
//                //ADFContext.getCurrent().getPageFlowScope().put("mainId", locCtfMainId);
//                ValueExpression ve1  = ef.createValueExpression(elc,"#{pageFlowScope.sFacCode}", Object.class); 
//                ve1.setValue(elc, rs4.getString(2));
//                
//                ValueExpression ve9  = ef.createValueExpression(elc,"#{pageFlowScope.sComments}", Object.class); 
//                 ve9.setValue(elc, rs4.getString(9));
//                ctfCommentsTxtNonDB.setValue(rs4.getString(9)+"");
//                AdfFacesContext.getCurrentInstance().addPartialTarget(this.getCtfCommentsTxtNonDB());
//            ValueExpression ve6  = ef.createValueExpression(elc,"#{pageFlowScope.sFacName}", Object.class); 
//             ve6.setValue(elc, rs4.getString(3));
//            
//            ValueExpression ve7  = ef.createValueExpression(elc,"#{pageFlowScope.sTypeDesc}", Object.class); 
//             ve7.setValue(elc, rs4.getString(4));
//            
//            ValueExpression ve2  = ef.createValueExpression(elc,"#{pageFlowScope.sEnteredBy}", Object.class); 
//             ve2.setValue(elc, rs4.getString(6));
//            
//            ValueExpression ve3  = ef.createValueExpression(elc,"#{pageFlowScope.sChangeBy}", Object.class); 
//             ve3.setValue(elc, rs4.getString(7));
//            
//            ValueExpression ve5  = ef.createValueExpression(elc,"#{pageFlowScope.sMonYear}", Object.class); 
//             ve5.setValue(elc, rs4.getString(8));
//            
//            ps3 = conn.prepareStatement("select sysdate from dual");
//            rs3 = ps3.executeQuery();
//            
//            rs3.next();
//            ValueExpression ve8  = ef.createValueExpression(elc,"#{pageFlowScope.sSysDate}", Object.class); 
//             ve8.setValue(elc, rs3.getString(1));
//            
//            ///////////////////////
//            //System.out.println("Previous ctf main id for ctf item is =============="+rs4.getString(1));
//            ValueExpression v1 = ef.createValueExpression
//                (elc, "#{pageFlowScope.ctfMainId}", Object.class);  
//            v1.setValue(elc,rs4.getString(1));
//            
//               ADFContext.getCurrent().getPageFlowScope().put("mainId",locCtfMainId);
//             //   SearchItemsByNextPreviousBTNS();
//            MethodExpression me = ef.createMethodExpression(elc, "#{bindings.setCurrentRowWithKeyValue.execute}", Object.class, new Class[0]);
//            me.invoke(elc, new Object[0]);
//         }
            
            
            } catch (SQLException sqle) {
            
            sqle.printStackTrace();
            }finally{
                closeRsAndPs(rs2, ps2);
              //  closeRsAndPs(rs3, ps3);
              //  closeRsAndPs(rs4, ps4);
               
               
            }
            ADFContext.getCurrent().getViewScope().put("lb", false);
            ADFContext.getCurrent().getViewScope().put("cv", false);
           //commentView111.resetValue();
            return null;
        }

        public void setPdate(RichInputDate pdate) {
            this.pdate = pdate;
        }

        public RichInputDate getPdate() {
            return pdate;
        }

        public void pDateVCL(ValueChangeEvent valueChangeEvent) {
           PreparedStatement ps1 = null;
           ResultSet rs = null;
            
          //System.out.println(  pdate.getValue().toString());
          
          String m = pdate.getValue().toString().substring(0, 2);
          //System.out.println(m);
          
            String y = pdate.getValue().toString().substring(3);
            //System.out.println(y);
            
            try {
    //             UIDSConnection connClass = new UIDSConnection();
    //               conn = connClass.conn;
                
                
                ps1 = conn.prepareStatement("select * from active_months where AM_MONTH = ? and AM_YEAR = ?");
                ps1.setString(1, m);
                ps1.setString(2,y);
                rs = ps1.executeQuery();
                if(!rs.next()){
                    RichPopup.PopupHints hints = new RichPopup.PopupHints();
                    popup1.show(hints);
                    pdate.setValue("");
                }
     
             } catch (SQLException sqle) {
                // TODO: Add catch code
                sqle.printStackTrace();
            }finally{
                closeRsAndPs(rs, ps1);
            }
            
            
        }

        public void setPopup1(RichPopup popup1) {
            this.popup1 = popup1;
        }

        public RichPopup getPopup1() {
            return popup1;
        }

        public String hidePopup1() {
            // Add event code here...
            popup1.hide();
            return null;
        }

        public void openBalVCL(ValueChangeEvent valueChangeEvent) {
            // Add event code here...
    //        //System.out.println("valueChangeEvent ----------"+valueChangeEvent.getOldValue()+" "+valueChangeEvent.getNewValue());
    //        
    //        oldBal = ""+valueChangeEvent.getOldValue();
    //        newBal = ""+valueChangeEvent.getNewValue();

    //        BindingContext bctx = BindingContext.getCurrent();
    //        BindingContainer bindings = bctx.getCurrentBindingsEntry();
    //        FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
    //        currow = tm.getCurrentRowIndex();

            RichPopup.PopupHints hints = new RichPopup.PopupHints();

           
            openBalPopup.show(hints);
           // p7.show(hints);
           ADFContext.getCurrent().getViewScope().put("cv",false);

        }

        public void setOpenBalPopup(RichPopup openBalPopup) {
            this.openBalPopup = openBalPopup;
        }

        public RichPopup getOpenBalPopup() {
            return openBalPopup;
        }
        public String openBalPopupYes() {
            // Add event code here...
            openBalPopup.hide();
            return null;
        }

        public String openBalPopupNo() {
            // Add event code here...        
    //        BindingContext bctx = BindingContext.getCurrent();
    //        BindingContainer bindings = bctx.getCurrentBindingsEntry();
    //        FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
    ////System.out.println("old=========================================="+currow+" "+oldBal);
    //        tm.setAttributeInRow(currow,4,oldBal,true);

            openBalPopup.hide();
            return null;
        }

        public String openBalPopupCancel() {
            // Add event code here...
            openBalPopup.hide();
            return null;
        }

        public void setReceiptPopup(RichPopup receiptPopup) {
            this.receiptPopup = receiptPopup;
        }

        public RichPopup getReceiptPopup() {
            return receiptPopup;
        }

        public String receiptPopupYes() {
            // Add event code here...
            receiptPopup.hide();
            return null;
        }

        public String receiptPopupNo() {
            // Add event code here...
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
            //System.out.println("old=========================================="+currow+" "+oldReceipt);
            tm.setAttributeInRow(currow,5,oldReceipt,true);
            receiptPopup.hide();

            return null;
        }

        public String receiptPopupCancel() {
            // Add event code here...
            receiptPopup.hide();
            return null;
        }

        public void receiptVCL(ValueChangeEvent valueChangeEvent) {
            
            if(facilityId.equals("") || facilityId.equals(null)){
                facilityId="0";
            }
           // ResultSet rs22 = null;
           // PreparedStatement pst22 = null;
           // String sql = "select type_hierarchy from fac_type ft, facility f where f.fac_type_id=ft.fac_type_id and f.facility_id="+facilityId;
            //System.out.println("valueChangeEvent ----------"+valueChangeEvent.getOldValue()+" "+valueChangeEvent.getNewValue());
            
            oldReceipt = ""+valueChangeEvent.getOldValue();
            newReceipt = ""+valueChangeEvent.getNewValue();
            
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
            currow = tm.getCurrentRowIndex();
            
            ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
            ViewObject voQuery = am.createViewObjectFromQueryStmt("voq1", "select type_hierarchy from fac_type ft, facility f where f.fac_type_id=ft.fac_type_id and f.facility_id="+facilityId);
            if(voQuery.hasNext()){
                Row r = voQuery.next();
                if(r.getAttribute(0).equals("3")){
                    RichPopup.PopupHints hints = new RichPopup.PopupHints();
                    receiptPopup.show(hints);
                }
            }
            Configuration.releaseRootApplicationModule(am, true);
            
            ADFContext.getCurrent().getViewScope().put("cv",false);
            
       
           

            
        }
    //////////////////////////////////////////////////////////////////////////////////

        public void closingBalVCL(ValueChangeEvent valueChangeEvent) {
           
            if(facilityId.equals("") || facilityId.equals(null)){
                facilityId="0";
            }
           
            ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
            
            
            
            
            // Add event code here...
            // //System.out.println("valueChangeEvent ----------"+valueChangeEvent.getOldValue()+" "+valueChangeEvent.getNewValue());
            
            oldClosingBal = ""+valueChangeEvent.getOldValue();
            newClosingBal = ""+valueChangeEvent.getNewValue();
            
            FacesContext fc = FacesContext.getCurrentInstance();
            ELContext elc = fc.getELContext();
            ExpressionFactory ef = fc.getApplication().getExpressionFactory();

            //ValueExpression ve1  = ef.createValueExpression(elc,"#{pageFlowScope.oldClosingBal}", Object.class); 
            // ve1.setValue(elc, oldClosingBal);
            ADFContext.getCurrent().getViewScope().put("oldClosingBal", oldClosingBal);
            
            ADFContext.getCurrent().getPageFlowScope().put("oldClosingBal", oldClosingBal);
           //        ValueExpression ve2  = ef.createValueExpression(elc,"#{pageFlowScope.newClosingBal}", Object.class); 
           //         ve2.setValue(elc, newClosingBal);
            ADFContext.getCurrent().getViewScope().put("newClosingBal", newClosingBal);
            ADFContext.getCurrent().getPageFlowScope().put("newClosingBal", newClosingBal);
            
            
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
            currow = tm.getCurrentRowIndex();
            
            
            ViewObject voQuery = am.createViewObjectFromQueryStmt("TestView","select type_hierarchy from fac_type ft, facility f where f.fac_type_id=ft.fac_type_id and f.facility_id="+facilityId);
            if(voQuery.hasNext()){
                ////System.out.println("facility id ==== "+facilityId);
                RichPopup.PopupHints hints = new RichPopup.PopupHints();
                closingBalPopup.show(hints);
            }
                

            Configuration.releaseRootApplicationModule(am, true);
      
            
            ADFContext.getCurrent().getViewScope().put("cv",false);

        }

        public String closingBalPopupNo() {
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
            //System.out.println("old=========================================="+currow+" "+oldClosingBal);
            tm.setAttributeInRow(currow,12,oldClosingBal,true);
            closingBalPopup.hide();
            return null;
        }
        
        public String closingBalPopupYes() {
            // Add event code here...
     closingBalPopup.hide();
            
            FacesContext fc = FacesContext.getCurrentInstance();
            ELContext elc = fc.getELContext();
            ExpressionFactory ef = fc.getApplication().getExpressionFactory();


            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
            
            ValueExpression ve1  = ef.createValueExpression(elc,"#{pageFlowScope.oldClosingBal}", Object.class); 
            int  oldClosingBal = Integer.parseInt(""+ve1.getValue(elc));

            ValueExpression ve2  = ef.createValueExpression(elc,"#{pageFlowScope.newClosingBal}", Object.class); 
            int newClosingBal = Integer.parseInt(""+ve2.getValue(elc));
            
    //System.out.println("oldClosingBal==================="+oldClosingBal);
    //System.out.println("newClosingBal==================="+newClosingBal);
    //System.out.println("currow==================="+currow);

            String cm = ""+ctfCommentsTxtNonDB.getValue();
            if(newClosingBal > oldClosingBal){ //Credit
                tm.setAttributeInRow(currow,7,2,true);
                tm.setAttributeInRow(currow,8,newClosingBal - oldClosingBal,true);
                ctfCommentsTxtNonDB.setValue(cm+" Computer Generated Adjustment of "+(newClosingBal - oldClosingBal)+" for "+tm.getAttributeFromRow(currow,3));
            }else{ // Debit
                tm.setAttributeInRow(currow,7,1,true);
                tm.setAttributeInRow(currow,8,oldClosingBal - newClosingBal,true);
                ctfCommentsTxtNonDB.setValue(cm+" Computer Generated Adjustment of "+(newClosingBal - oldClosingBal)+" for "+tm.getAttributeFromRow(currow,3));
            }
                

            ADFContext.getCurrent().getViewScope().put("cv",false);
            return null;
        }

        public String closingBalPopupCancel() {
            // Add event code here...
            closingBalPopup.hide();
            return null;
        }
    ///////////////////////////////////////////////////////////////////////////////////
        public String avgMonConsYes() {
            ADFContext.getCurrent().getViewScope().put("cv",true);
            ADFContext.getCurrent().getViewScope().put("avgc",true);
            // Add event code here...
            avgMonConsPopup.hide();
            setFocus(qtyReqId.getClientId());
            return null;
        }

        public String avgMonConsNo() {
            // Add event code here...
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
            //System.out.println("old=========================================="+currow+" "+oldAvgMonCons);
            tm.setAttributeInRow(currow,9,oldAvgMonCons,true);
            avgMonConsPopup.hide();
            setFocus( qtyReqId.getClientId());
            return null;
        }

        public String avgMonConsCancel() {
            // Add event code here...
            avgMonConsPopup.hide();
            setFocus("it9");
            return null;
        }

        public void setAvgMonConsPopup(RichPopup avgMonConsPopup) {
            this.avgMonConsPopup = avgMonConsPopup;
        }

        public RichPopup getAvgMonConsPopup() {
            return avgMonConsPopup;
        }

        public void avgMonConsVCL(ValueChangeEvent valueChangeEvent) {
            // Add event code here...
            //System.out.println("valueChangeEvent ----------"+valueChangeEvent.getOldValue()+" "+valueChangeEvent.getNewValue());
            
            oldAvgMonCons = ""+valueChangeEvent.getOldValue();
            newAvgMonCons = ""+valueChangeEvent.getNewValue();
            
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
            currow = tm.getCurrentRowIndex();
            
            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            avgMonConsPopup.show(hints);
            ADFContext.getCurrent().getViewScope().put("cv",false);
            //ADFContext.getCurrent().getViewScope().put("cv",true);
        }
    ///////////////////////////////////////////////////////////////////////////////////////////////////
        public void setQtyReqPopup(RichPopup qtyReqPopup) {
            this.qtyReqPopup = qtyReqPopup;
        }

        public RichPopup getQtyReqPopup() {
            return qtyReqPopup;
        }

        public String qtyReqYes() {
            // Add event code here...
            qtyReqPopup.hide();
            setFocus("it10");
            return null;
        }

        public String qtyReqNo() {
            // Add event code here...
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
            //System.out.println("old=========================================="+currow+" "+oldQtyReq);
            tm.setAttributeInRow(currow,10,oldQtyReq,true);
            qtyReqPopup.hide();
            setFocus("it10");
            return null;
        }

        public String qtyReqCancel() {
            // Add event code here...
            qtyReqPopup.hide();
            setFocus("it10");
            return null;
        }

        public void qtyReqVCL(ValueChangeEvent valueChangeEvent) {
            // Add event code here...
            //System.out.println("valueChangeEvent ----------"+valueChangeEvent.getOldValue()+" "+valueChangeEvent.getNewValue());
            
            oldQtyReq = ""+valueChangeEvent.getOldValue();
            newQtyReq = ""+valueChangeEvent.getNewValue();
            
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
            currow = tm.getCurrentRowIndex();
            
            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            qtyReqPopup.show(hints);
            if(ADFContext.getCurrent().getViewScope().get("avgc") == true ){
                ADFContext.getCurrent().getViewScope().put("cv",true);
            }else{
                ADFContext.getCurrent().getViewScope().put("cv",false);
            }
            

        }

        public void adjustmentsVCL(ValueChangeEvent valueChangeEvent) {
            // Add event code here...
            //System.out.println("valueChangeEvent ----------"+valueChangeEvent.getOldValue()+" "+valueChangeEvent.getNewValue());
            
            oldAdjustments = ""+valueChangeEvent.getOldValue();
            newAdjustments = ""+valueChangeEvent.getNewValue();
            
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
            currow = tm.getCurrentRowIndex();
            
        //System.out.println( "7777===="+ tm.getAttributeFromRow(currow, 7) );
        //System.out.println( "88888====="+ tm.getAttributeFromRow(currow, 8) );
          
            String adjType = ""+tm.getAttributeFromRow(currow, 7);
        //System.out.println("adjtype==="+adjType);
            
            if ((adjType == null || adjType.equals("null") || adjType.equals("") )&& Integer.parseInt(newAdjustments) > 0) {
                tm.setAttributeInRow(currow,7,1,true);
                RichPopup.PopupHints hints = new RichPopup.PopupHints();
                adjustmentPopup.show(hints);
            } 
            

            ADFContext.getCurrent().getViewScope().put("cv",false);
            

        }

        public String adjustmentOK() {
            // Add event code here...
            adjustmentPopup.hide();
            return null;
        }

        public void setAdjustmentPopup(RichPopup adjustmentPopup) {
            this.adjustmentPopup = adjustmentPopup;
        }

        public RichPopup getAdjustmentPopup() {
            return adjustmentPopup;
        }

        public String backMainMethod() {
            SearchCtfMain();
         
       FacesContext fc = FacesContext.getCurrentInstance();
             ELContext elc = fc.getELContext();
             ExpressionFactory ef = fc.getApplication().getExpressionFactory();

       MethodExpression me = ef.createMethodExpression(elc, "#{bindings.Commit.execute}", Object.class, new Class[0]);
       me.invoke(elc, new Object[0]);

            
            return "backMain";
        }

        public void setP7(RichPopup p7) {
            this.p7 = p7;
        }

        public RichPopup getP7() {
            return p7;
        }

        public String saveButton() {
             PreparedStatement ps = null;
            PreparedStatement ps1 = null;
            if(   ADFContext.getCurrent().getViewScope().get("cv") == true ){
            BindingContext bctx = BindingContext.getCurrent();
            BindingContainer bindings = bctx.getCurrentBindingsEntry();
            FacesCtrlHierBinding  tm = (FacesCtrlHierBinding )bindings.get("CtfItemCtfMainVO");
                int IteratorRowCount = screenViewIteratorFacesCtrlHierBindingRowCount(tm);
            try {
    //            UIDSConnection connClass = new UIDSConnection();
    //              conn = connClass.conn;
                
                
                
                conn.setAutoCommit(false);
                
            int ctf =  Integer.parseInt(""+tm.getAttributeFromRow(0, 1));
                
           
           
           ////////////////////////////WORK
          ps = conn.prepareStatement("update CTF_ITEM set OPEN_BAL = ? ,RECEIPTS =  ? ,ISSUES = ? ,ADJUSTMENTS= ? ,ADJ_TYPE_ID= ? ,CLOSING_BAL= ? ,AVG_MNTHLY_CONS= ? ,QTY_REQUIRED= ? ,QTY_RECEIVED= ? ,NEW_VISITS = ? ,CONT_VISITS= ?  where CTF_MAIN_ID= ? and PROD_ID = ? ");
          
            for (int i = 0; i < IteratorRowCount ; i++) {

                
            int pid =  Integer.parseInt(""+tm.getAttributeFromRow(i, 2)); 
            int a1 =  Integer.parseInt(""+tm.getAttributeFromRow(i, 4));
            int a2 =  Integer.parseInt(""+tm.getAttributeFromRow(i, 5));    
            int a3 =  Integer.parseInt(""+tm.getAttributeFromRow(i, 6));
            int a4 =  Integer.parseInt(""+tm.getAttributeFromRow(i, 8));    
                
           int a5 = 0; 
            Object o5 = tm.getAttributeFromRow(i, 7);
                if (o5 == null || o5.equals("") || o5.equals("null") ){
                    a5 = 0;
                }else{
                    a5 =  Integer.parseInt(""+o5);
                    
                }
                //System.out.println("The Value of A5 is "+a5);
                if( a5  == 2 || a5 == 7 || a5 == 8 ){
                    //System.out.println("The value of Positive a4 is ****************** "+a4);
                    a4 = Math.abs(Integer.parseInt(""+tm.getAttributeFromRow(i, 8))); 
                    
                }else{
                    
                    a4 = -1 * Math.abs(Integer.parseInt(""+tm.getAttributeFromRow(i, 8))); 
                    //System.out.println("The value of a4 is ****************** "+a4);
                }
            int a6 =  Integer.parseInt(""+tm.getAttributeFromRow(i, 12));    
            int a7 =  Integer.parseInt(""+tm.getAttributeFromRow(i, 9));
            int a8 =  Integer.parseInt(""+tm.getAttributeFromRow(i, 10));    
            int a9 =  Integer.parseInt(""+tm.getAttributeFromRow(i, 11));
            int a10;
                Object o10 = tm.getAttributeFromRow(i, 13);
                    if (o10 == null || o10.equals("") || o10.equals("null") ){
                        a10 = 0;
                    }else{
                        a10 =  Integer.parseInt(""+o10);
                    }
            int a11;
                Object o11 = tm.getAttributeFromRow(i, 14);
                    if (o11 == null || o11.equals("") || o11.equals("null") ){
                        a11 = 0;
                    }else{
                        a11 =  Integer.parseInt(""+o11);
                    }

                
    //System.out.println("update CTF_ITEM set OPEN_BAL = "+a1+",RECEIPTS = "+a2+",ISSUES ="+a3+",ADJUSTMENTS="+a4+",ADJ_TYPE_ID="+a5+",CLOSING_BAL="+a6+",AVG_MNTHLY_CONS="+a7+",QTY_REQUIRED="+a8+",QTY_RECEIVED="+a9+",NEW_VISITS="+a10+",CONT_VISITS="+a11+" where CTF_MAIN_ID="+ctf+" and PROD_ID="+pid);
                
                ps.setInt(1,a1);
                ps.setInt(2,a2);
                ps.setInt(3,a3);
                ps.setInt(4,a4);
                ps.setInt(5,a5);
                ps.setInt(6,a6);
                ps.setInt(7,a7);
                ps.setInt(8,a8);
                ps.setInt(9,a9);
                ps.setInt(10,a10);
                ps.setInt(11,a11);
                ps.setInt(12,ctf);
                ps.setInt(13,pid);
                ps.addBatch();
                ///////////////////////////////////////////////////////////////
                //////////////////////////////////////////////////////////////
              //  ps.executeUpdate();
    ////System.out.println("before commit");
           //     conn.commit();
    ////System.out.println("after commit");
                
                
                
            }        
           
           
           ps.executeBatch();
          conn.commit();
           
           ///////////////////////////////////////
                //////////////////////////////////
           
           
                String comm = ""+ctfCommentsTxtNonDB.getValue();
                ps1 = conn.prepareStatement("update CTF_main set CTF_COMMENTS = '"+comm+"' where CTF_MAIN_ID="+ctf   );
                //System.out.println("update CTF_main set CTF_COMMENTS = '"+comm+"' where CTF_MAIN_ID="+ctf);
                ps1.executeUpdate();

              
                conn.setAutoCommit(true);
                conn.commit();
               
            } catch (SQLException sqle) {
                // TODO: Add catch code
                sqle.printStackTrace();
            }finally{
                    
                     
                        
                      if (ps != null)
                          {
                              try
                              {
                                  ps.close();
                              } catch (SQLException e)
                              {
                                 //System.out.println ("The statement cannot be closed.");
                              }
                          }
                        if (ps1 != null)
                            {
                                try
                                {
                                    ps1.close();
                                } catch (SQLException e)
                                {
                                   //System.out.println ("The statement1111111 cannot be closed.");
                                }
                            }
                          
                    
                    
                   
                    
                }
                    
                    
                    
                    
                    
                    
                    
                    
                    
            ADFContext.getCurrent().getViewScope().put("saved", true);
            }else{
                
                RichPopup.PopupHints hints = new RichPopup.PopupHints();
                cvPopup.show(hints);
            }
                    
                    
            return null;
        }

        public void setCtfMainIdTxt(RichInputText ctfMainIdTxt) {
            this.ctfMainIdTxt = ctfMainIdTxt;
        }

        public RichInputText getCtfMainIdTxt() {
            return ctfMainIdTxt;
        }

        public void setCtfCommentsTxt(RichInputText ctfCommentsTxt) {
            this.ctfCommentsTxt = ctfCommentsTxt;
        }

        public RichInputText getCtfCommentsTxt() {
            return ctfCommentsTxt;
        }

        public void setCtfCommentsTxtNonDB(RichInputText ctfCommentsTxtNonDB) {
            this.ctfCommentsTxtNonDB = ctfCommentsTxtNonDB;
        }

        public RichInputText getCtfCommentsTxtNonDB() {
            return ctfCommentsTxtNonDB;
        }

        public void searchDate(ActionEvent actionEvent) {
            String val =  this.getGetDateCtfMain().getValue()+"";
            if(val !=null){

              try {
                     RichTable t1 = this.getFilterDateMainBrowse();
                       FilterableQueryDescriptor filterQD =  (FilterableQueryDescriptor) t1.getFilterModel();  
                         Map filterCriteria = filterQD.getFilterCriteria();
                     
                         
                       DateFormat formatter;
                                 java.util.Date date;

                formatter = new SimpleDateFormat("MM/yyyy");
                date = formatter.parse(val);
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                oracle.jbo.domain.Date jboDate = new oracle.jbo.domain.Date(sqlDate);

               
                 filterCriteria.put("PDate", jboDate);      
                 getFilterDateMainBrowse().queueEvent(new QueryEvent(getFilterDateMainBrowse(), filterQD));
                 AdfFacesContext.getCurrentInstance().addPartialTarget(this.getFilterDateMainBrowse());
                 } catch (ParseException e) {

                          e.printStackTrace();
                  }

            }// -
        }

        public void setFilterDateMainBrowse(RichTable filterDateMainBrowse) {
            this.filterDateMainBrowse = filterDateMainBrowse;
        }

        public RichTable getFilterDateMainBrowse() {
            return filterDateMainBrowse;
        }

        public void setGetDateCtfMain(RichInputText getDateCtfMain) {
            this.getDateCtfMain = getDateCtfMain;
        }

        public RichInputText getGetDateCtfMain() {
            return getDateCtfMain;
        }

        public void resetInputText(ActionEvent actionEvent) {
            //private void resetInputText(String id) {
    //        RichInputText input = (RichInputText) JsfUtils.findComponentInRoot("it1");
    //        input.setSubmittedValue(null);
    //        input.resetValue();
    //        DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
    // DCIteratorBinding iter = bindings.findIteratorBinding("CtfMainVOIterator");
    // iter.executeQuery();
            this.getGetDateCtfMain().resetValue() ;
    //adfFacesContext.addPartialTarget(this.getGetDateCtfMain());
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getGetDateCtfMain());
           // }
        }

        public void setCommentView111(RichInputText commentView111) {
            this.commentView111 = commentView111;
        }

        public RichInputText getCommentView111() {
            return commentView111;
        }

        public void deleteCtfMainData(ActionEvent actionEvent) {
            PreparedStatement pst = null;
           String sql = "delete from ctf_item ci where ci.ctf_main_id="+locCtfMainId;
            try {


                pst = conn.prepareStatement(sql);
                //System.out.println(sql);
                int i = pst.executeUpdate();
              
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                //System.out.println("Errrrrrrrrrror");
            }finally{
                try {
                    if (pst != null)
                    pst.close();
                } catch (SQLException e) {
                    //System.out.println(" ************ Connection connot be closed ************* ");
                }
            }
            
           
        }

        public void setCvPop(RichPanelWindow cvPop) {
            this.cvPop = cvPop;
        }

        public RichPanelWindow getCvPop() {
            return cvPop;
        }

        public void setCvPopup(RichPopup cvPopup) {
            this.cvPopup = cvPopup;
        }

        public RichPopup getCvPopup() {
            return cvPopup;
        }

        public String cvPopOk() {
            cvPopup.hide();
            return null;
        }

        public String backMethod() {
            savePopup.hide();
            return "backMain";
        }

        public String saveOk() {
            savePopup.hide();
            return null;
        }

        public void setSavePopup(RichPopup savePopup) {
            this.savePopup = savePopup;
        }

        public RichPopup getSavePopup() {
            return savePopup;
        }

        public void setLbPopup(RichPopup lbPopup) {
            this.lbPopup = lbPopup;
        }

        public RichPopup getLbPopup() {
            return lbPopup;
        }

        public String lbPopupOk() {
            lbPopup.hide();
            return null;
        }

        public String callBackPop() {
            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            savePopup.show(hints);
            return null;
        }

        public void qtyrecVLC(ValueChangeEvent valueChangeEvent) {
            ADFContext.getCurrent().getViewScope().put("cv",true);
        }

        public void nusersVLC(ValueChangeEvent valueChangeEvent) {
            ADFContext.getCurrent().getViewScope().put("cv",true);
        }

        public void cusersVLC(ValueChangeEvent valueChangeEvent) {
           ADFContext.getCurrent().getViewScope().put("cv",true);
        }

        public void issuesVLC(ValueChangeEvent valueChangeEvent) {
            ADFContext.getCurrent().getViewScope().put("cv",false);
        }

        public void adjIdvlc(ValueChangeEvent valueChangeEvent) {
            ADFContext.getCurrent().getViewScope().put("cv",false);
        }

        public void setItemsTable(RichTable itemsTable) {
            this.itemsTable = itemsTable;
        }

        public RichTable getItemsTable() {
            return itemsTable;
        }

    public String SearchFacilities() {
        // Add event code here...
        return null;
    }

    public String callSearchMainItemsPopup() {
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        searchPopup.show(hints);
        
        return null;
    }

    public void setSearchPopup(RichPopup searchPopup) {
        this.searchPopup = searchPopup;
    }

    public RichPopup getSearchPopup() {
        return searchPopup;
    }

    public void setDeleteDataPopup(RichPopup deleteDataPopup) {
        this.deleteDataPopup = deleteDataPopup;
    }

    public RichPopup getDeleteDataPopup() {
        return deleteDataPopup;
    }

    public String hideDeleteDataPopup() {
        deleteDataPopup.hide();
        return null;
    }


    public String callDeletetemsPopup() {
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        deleteDataPopup.show(hints);
        
        return null;
    }

    public String deleteDataPopup() {
        PreparedStatement pst = null;
        String sql = "delete from ctf_item ci where ci.ctf_main_id="+locCtfMainId;
        try {


            pst = conn.prepareStatement(sql);
            //System.out.println(sql);
            int i = pst.executeUpdate();
          
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("Errrrrrrrrrror");
        }finally{
            try {
                if (pst != null)
                pst.close();
            } catch (SQLException e) {
                //System.out.println(" ************ Connection connot be closed ************* ");
            }
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(filterDateMainBrowse);
        
        return null;
    }
    
    
    public void setFocus(String inputTextIdToFocus) {
    FacesContext facesCtx = FacesContext.getCurrentInstance();
    RowKeySet rks = itemsTable.getSelectedRowKeys();
    String inputId ="";
    if(rks != null && rks.size() > 0) {
    Object rowKey = rks.iterator().next();
    String rowId = itemsTable.getClientRowKeyManager().getClientRowKey(facesCtx, itemsTable, rowKey);
    inputId = itemsTable.getClientId(facesCtx) + ":" + rowId + ":" + inputTextIdToFocus; //here it3 is id for inputtext in1st column.
    System.out.println("inputid "+inputTextIdToFocus);
    } else {
    // handle error
    }
    ExtendedRenderKitService service = Service.getRenderKitService(facesCtx, ExtendedRenderKitService.class);
    service.addScript(facesCtx, "comp = AdfPage.PAGE.findComponent(‘"+inputTextIdToFocus+"‘);\n" +
    "comp.focus()");      // javascript method is used
    
    }

    public void setQtyReqId(RichInputText qtyReqId) {
        this.qtyReqId = qtyReqId;
    }

    public RichInputText getQtyReqId() {
        return qtyReqId;
    }
}


