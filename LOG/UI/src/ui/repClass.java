package ui;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.el.ELContext;

import javax.el.ExpressionFactory;

import javax.el.MethodExpression;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import moh.logistics.lib.common.UIDSConnection;

import moh.logistics.lib.reports.MainInterface;

import oracle.adf.model.BindingContext;
import oracle.adf.share.ADFContext;
import oracle.adf.view.faces.bi.component.graph.UIGraph;
import oracle.adf.view.rich.component.rich.input.RichSelectManyChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;

import oracle.jbo.*;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.Number;
import oracle.jbo.uicli.binding.JUCtrlListBinding;

import oracle.jdbc.OracleDriver;

public class repClass implements MainInterface  {
    private RichSelectOneChoice repType;
    private RichSelectOneChoice facType;
    private RichSelectManyChoice facilitiesOfType;
    private RichSelectOneChoice cm;
    private RichSelectOneChoice cq;
    private RichSelectOneChoice ch;
    private RichSelectOneChoice cy;
    private RichSelectOneChoice cfm;
    private RichSelectOneChoice cfy;
    private RichSelectOneChoice ctm;
    private RichSelectOneChoice cty;
    private RichSelectOneChoice cprod;
    private RichSelectOneRadio periodType;
    private RichSelectOneChoice graphName;
    
    private UIGraph dispensedGraph;
    private UIGraph servicesStatisticsGraph;
    private UIGraph cypGraph;
    Connection conn = UIDSConnection.getConnection();
    public repClass() {
    }

    public void typeVLC(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        ADFContext.getCurrent().getSessionScope().put("repFacType", Integer.parseInt(facType.getValue() + "") );
        
        FacesContext fc = FacesContext.getCurrentInstance();
        ExpressionFactory ef = fc.getApplication().getExpressionFactory();
        ELContext el = fc.getELContext();
        MethodExpression me = ef.createMethodExpression(el, "#{bindings.ExecuteWithParams.execute}", Object.class, new Class[0]);
        me.invoke(el, new Object[0]);
        System.out.println(" Value of Type is "+ ADFContext.getCurrent().getSessionScope().get("repFacType"));
       ;
       
        JUCtrlListBinding listBindings = (JUCtrlListBinding)getBindings().get("repFacilityTypeParam1");
                        Object str[] = listBindings.getSelectedValues();
                        listBindings.clearSelectedIndices();
                       // listBindings.clear();
        
    }

    public void setRepType(RichSelectOneChoice repType) {
        this.repType = repType;
    }

    public RichSelectOneChoice getRepType() {
        return repType;
    }

    public void setFacType(RichSelectOneChoice facType) {
        this.facType = facType;
    }

    public RichSelectOneChoice getFacType() {
        return facType;
    }

    public void facilitiesOfTypeVLC(ValueChangeEvent valueChangeEvent) {
       
                       // listBindings.clear();
    }

    public void setFacilitiesOfType(RichSelectManyChoice facilitiesOfType) {
        this.facilitiesOfType = facilitiesOfType;
    }

    public RichSelectManyChoice getFacilitiesOfType() {
        return facilitiesOfType;
    }
    
    public  BindingContainer getBindings(){
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public String callReport() {
        JUCtrlListBinding listBindings = (JUCtrlListBinding)getBindings().get("repFacilityTypeParam1");
                Object str[] = listBindings.getSelectedValues();
              //  listBindings.
              String selectedFaces = "";
              String firstFacility = "";
        int strIndexes[] = listBindings.getSelectedIndices();
                for (int i = 0; i < str.length; i++) {
                    System.out.println(str[i]);
                    if(i == 0){
                    selectedFaces = ""+str[i];
                    firstFacility = str[i]+"";
                    }else{
                    selectedFaces += ", "+str[i];
                    }
                }
                String period = "";
              ADFContext.getCurrent().getSessionScope().put("firstFacility",firstFacility);
        ADFContext.getCurrent().getSessionScope().put("selectedFaces",selectedFaces);
        ADFContext.getCurrent().getSessionScope().put("ct",facType.getValue()+"");
        ADFContext.getCurrent().getSessionScope().put("cm",cm.getValue()+"");
        ADFContext.getCurrent().getSessionScope().put("cy",cy.getValue()+"");
        ADFContext.getCurrent().getSessionScope().put("cprod",cprod.getValue()+"");
        ADFContext.getCurrent().getSessionScope().put("cfm",cfm.getValue()+"");
        ADFContext.getCurrent().getSessionScope().put("cfy",cfy.getValue()+"");
        ADFContext.getCurrent().getSessionScope().put("ctm",ctm.getValue()+"");
        ADFContext.getCurrent().getSessionScope().put("cty",cty.getValue()+"");
        ADFContext.getCurrent().getSessionScope().put("ch",ch.getValue()+"");
        ADFContext.getCurrent().getSessionScope().put("cq",cq.getValue()+"");
        ADFContext.getCurrent().getSessionScope().put("periodType",periodType.getValue()+"");
        System.out.println("type is "+ADFContext.getCurrent().getSessionScope().get("ct"));
        String month = ADFContext.getCurrent().getSessionScope().get("cm") +"";
        if(month.equals(null) || month.equals("") ||  month.equals("null")){
            ADFContext.getCurrent().getSessionScope().put("cm","01");
        }
        String year = ADFContext.getCurrent().getSessionScope().get("cy")+"";
        if(year.equals(null) || year.equals("") ||  year.equals("null")){
            ADFContext.getCurrent().getSessionScope().put("cy","2000");
        }
        String quarter = ADFContext.getCurrent().getSessionScope().get("cq")+"";
        if(quarter.equals(null) || quarter.equals("") ||  quarter.equals("null")){
            ADFContext.getCurrent().getSessionScope().put("cq","1");
        }
        String half = ADFContext.getCurrent().getSessionScope().get("ch") +"";
        if(half.equals(null) || half.equals("") ||  half.equals("null")){
            ADFContext.getCurrent().getSessionScope().put("ch","0");
        }
        String cprod = ADFContext.getCurrent().getSessionScope().get("cprod")+"";
        if(cprod.equals(null) || cprod.equals("") ||  cprod.equals("null")){
            ADFContext.getCurrent().getSessionScope().put("cprod","0");
        }
        String fm = ADFContext.getCurrent().getSessionScope().get("cfm")+"";
        if(fm.equals(null) || fm.equals("") ||  fm.equals("null")){
            ADFContext.getCurrent().getSessionScope().put("cfm","01");
        }
        String fy = ADFContext.getCurrent().getSessionScope().get("cfy")+"";
        if(fy.equals(null) || fy.equals("") ||  fy.equals("null")){
            ADFContext.getCurrent().getSessionScope().put("cfy","2000");
        }
        String tm = ADFContext.getCurrent().getSessionScope().get("ctm")+"";
        if(tm.equals(null) || tm.equals("") ||  tm.equals("null")){
            ADFContext.getCurrent().getSessionScope().put("ctm","01");
        }
        String ty = ADFContext.getCurrent().getSessionScope().get("cty")+"";
        if(ty.equals(null) || ty.equals("") ||  ty.equals("null")){
            ADFContext.getCurrent().getSessionScope().put("cty","2000");
        }
        
        String per = "";
        String quote="";
        if( (periodType.getValue()+"").equals("qua")){
            if(cq.getValue().equals("1") || cq.getValue().equals("01")){
                quote = "st";
            }else             if(cq.getValue().equals("2") || cq.getValue().equals("02")){
                quote = "nd";
            }else             if(cq.getValue().equals("3") || cq.getValue().equals("03")){
                quote = "rd";
            }else              if(cq.getValue().equals("4") || cq.getValue().equals("04")){
                quote = "th";
            }
            
            period = " PERIOD: "+cq.getValue()+quote+" Quarter "+cy.getValue()+" - ";
            per = cq.getValue()+quote+" QUARTER "+cy.getValue()+"";
            
        } else         if( (periodType.getValue()+"").equals("ann")){
            
            period = " PERIOD: ANNUAL "+ cy.getValue()+" - " ;
            per = "Annual "+ cy.getValue()+"";
            
        } else         if( (periodType.getValue()+"").equals("hy")){
            if(ch.getValue().equals("1") || ch.getValue().equals("01")){
                quote = "st";
            }else             if(ch.getValue().equals("2") || ch.getValue().equals("02")){
                quote = "nd";
            }
            
            period = " PERIOD: "+ ch.getValue()+quote+" Half "+cy.getValue()+" - ";
            per = ch.getValue()+""+" HALF "+cy.getValue()+"";
            
        } else         if( (periodType.getValue()+"").equals("mon")){
            
            period = " PERIOD: "+ cm.getValue()+ "/"+cy.getValue()+" -";
            per = ""+ cm.getValue()+ "/"+cy.getValue()+"";
            
        }else         if( (periodType.getValue()+"").equals("u")){
            
            period = " PERIOD: "+ cfm.getValue()+ " / "+cfy.getValue()+" - "+ ctm.getValue()+ " / "+cty.getValue()+" - ";
            per =  cfm.getValue()+ "/"+cfy.getValue()+" - "+ ctm.getValue()+ "/"+cty.getValue()+"";
            
        }
        
        System.out.println("month  is "+ADFContext.getCurrent().getSessionScope().get("cm"));
        System.out.println("year is "+ADFContext.getCurrent().getSessionScope().get("cy"));
        System.out.println("product is "+ADFContext.getCurrent().getSessionScope().get("cprod"));
        System.out.println("from month is "+ADFContext.getCurrent().getSessionScope().get("cfm"));
        System.out.println("from year is "+ADFContext.getCurrent().getSessionScope().get("cfy"));
        System.out.println("to month is "+ADFContext.getCurrent().getSessionScope().get("ctm"));
        System.out.println("to year is "+ADFContext.getCurrent().getSessionScope().get("cty"));
        System.out.println("half is "+ADFContext.getCurrent().getSessionScope().get("ch"));
        System.out.println("quarter is "+ADFContext.getCurrent().getSessionScope().get("cq"));
              
        FacesContext fc = FacesContext.getCurrentInstance();
        ExpressionFactory ef = fc.getApplication().getExpressionFactory();
        ELContext el = fc.getELContext();
        if(graphName.getValue().equals("1")){
            ///////////// execute graph cyp
            System.out.println("******************** now working with CYP graph **********************");
        MethodExpression me = ef.createMethodExpression(el, "#{bindings.ExecuteWithParams1.execute}", Object.class, new Class[0]);
        me.invoke(el, new Object[0]);
        }else         if(graphName.getValue().equals("2")){
            ///////////// execute dispensed To User Graph
            System.out.println("******************** now working with Dispensed To User Graph  **********************");
        MethodExpression me = ef.createMethodExpression(el, "#{bindings.ExecuteWithParams2.execute}", Object.class, new Class[0]);
        me.invoke(el, new Object[0]);
        }else{
            System.out.println("******************** now working with Service Statistics graph **********************");
            MethodExpression me = ef.createMethodExpression(el, "#{bindings.ExecuteWithParams3.execute}", Object.class, new Class[0]);
            me.invoke(el, new Object[0]);
            
        }
        ADFContext.getCurrent().getSessionScope().put("pressed",true);
        //////////////////////////// Title of the Graph ////////////////////////
        ////////////////////////////////////////////////////////////////////////
        
        String allornot = "";
        String gName = "";
       
        String pro = "";
        String gType = "";
       
        if(facType.getValue().equals("0")){
            allornot = "ALL FACILITIES - ";
            gType = "All Facilities ";
        }else{
//            String username = "log5";
//            String password = "log5";
//            String thinConn = "jdbc:oracle:thin:@10.160.12.5:1521:ora10g";
            try {
                
                DriverManager.registerDriver(new OracleDriver());
            } catch (SQLException e) {
                System.out.println(" Connection Failed");
            }
          
        
            String sqlCheck = "select fac_type_id, upper(fac_name) from fac_type where fac_type_id="+facType.getValue();
            try {
                PreparedStatement pst = conn.prepareStatement(sqlCheck);
                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    allornot = " "+rs.getString(2)+" -";
                    gType = rs.getString(2);
                }else{
                    String sqlGroup = "select (grp_id) as id , upper(grp_desc) as name from groups where grp_id="+(Integer.parseInt(facType.getValue()+"")-100);
                    PreparedStatement pst1 = conn.prepareStatement(sqlGroup);
                    ResultSet rs1 = pst1.executeQuery();
                    rs1.next();
                    allornot = " "+rs1.getString(2)+" - ";
                    gType = rs1.getString(2);
                }
            } catch (SQLException e) {
                System.out.println("faaaaaaaaaaaaaaailed");
            }


           
        }
        
        
       
     
        JUCtrlListBinding listBindings2 = (JUCtrlListBinding)getBindings().get("repFacilityTypeParam1");
                Object str2[] = listBindings2.getSelectedValues();
              //  listBindings.
              String selectedFaces2 = "";
              String firstFacility2 = "";
        int strIndexes2[] = listBindings2.getSelectedIndices();
                for (int i = 0; i < str2.length; i++) {
           
            System.out.println(str2[i]);
            try {
                String sqlFaces = "select facility_id, upper(fac_name) from facility where facility_id="+ str2[i];
                PreparedStatement pst3 = conn.prepareStatement(sqlFaces);
                System.out.println(sqlFaces);
                ResultSet rs3 = pst3.executeQuery();
                if(rs3.next()){
                    
                    if(i == 0){
                            selectedFaces2 = ""+rs3.getString(2);
                            firstFacility2 = rs3.getString(2)+"";
                            }else{
                            selectedFaces2 += ", "+ rs3.getString(2);
                            }
                    
                }
                
            } catch (SQLException e) {
                System.out.println("sql failed");
            }
         
                    if(i==3)
                    break;
                }
        
        
        
         
                    ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
                    ViewObject vo = am.createViewObjectFromQueryStmt("vo1", "select upper(pro_name) from product where prod_id="+this.getCprod().getValue());
                    vo.executeQuery();
                    if(vo.hasNext()){
                    Row r = vo.next();
                    //allornot+=" "+r.getAttribute(0)+" ";
                    pro = r.getAttribute(0)+" - ";
                    //pro = r.getAttribute(0)+"";
                    }
                    // Work with your appmodule and view object here
                    Configuration.releaseRootApplicationModule(am, true);
        
        
        ////////////////////////////////////////////////////////////////////////
        String title1 = "";
        String title = "";
          if(getGraphName().getValue().equals("1")){
                    title1 = " CYP GRAPH - ";
                    gName = "GYP Graph";
        }else  if(getGraphName().getValue().equals("2")){
title1 = " DISPENSED TO USER GRAPH - ";
gName = " Dispensed To User Graph ";

        }else  if(getGraphName().getValue().equals("3")){
title1 = " SERVICE STATISTICS GRAPH - ";
gName = " Service Statistics Graph  ";

        }
       
                 //period+= allornot;
                 title = title1 +pro+ period+allornot+selectedFaces2; 
                 System.out.println(title);
                    
        ADFContext.getCurrent().getSessionScope().put("gtitle",title);   
        
     
       
        if(getGraphName().getValue().equals("1")){
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getCypGraph());
        }else  if(getGraphName().getValue().equals("2")){
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getDispensedGraph());

        }else  if(getGraphName().getValue().equals("3")){
            
            AdfFacesContext.getCurrentInstance().addPartialTarget(this.getServicesStatisticsGraph());

        }         
                    
                    
                  
        return null;
    }

    public void setCm(RichSelectOneChoice cm) {
        this.cm = cm;
    }

    public RichSelectOneChoice getCm() {
        return cm;
    }

    public void setCq(RichSelectOneChoice cq) {
        this.cq = cq;
    }

    public RichSelectOneChoice getCq() {
        return cq;
    }

    public void setCh(RichSelectOneChoice ch) {
        this.ch = ch;
    }

    public RichSelectOneChoice getCh() {
        return ch;
    }

    public void setCy(RichSelectOneChoice cy) {
        this.cy = cy;
    }

    public RichSelectOneChoice getCy() {
        return cy;
    }

    public void setCfm(RichSelectOneChoice cfm) {
        this.cfm = cfm;
    }

    public RichSelectOneChoice getCfm() {
        return cfm;
    }

    public void setCfy(RichSelectOneChoice cfy) {
        this.cfy = cfy;
    }

    public RichSelectOneChoice getCfy() {
        return cfy;
    }

    public void setCtm(RichSelectOneChoice ctm) {
        this.ctm = ctm;
    }

    public RichSelectOneChoice getCtm() {
        return ctm;
    }

    public void setCty(RichSelectOneChoice cty) {
        this.cty = cty;
    }

    public RichSelectOneChoice getCty() {
        return cty;
    }

    public void setCprod(RichSelectOneChoice cprod) {
        this.cprod = cprod;
    }

    public RichSelectOneChoice getCprod() {
        return cprod;
    }

    public void setPeriodType(RichSelectOneRadio periodType) {
        this.periodType = periodType;
    }

    public RichSelectOneRadio getPeriodType() {
        return periodType;
    }

    public void graphNameVLC(ValueChangeEvent valueChangeEvent) {
     
        ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
        ViewObject voCyp = am.findViewObject("cypVO1");
       voCyp.clearCache();
        ViewObject voDispensed = am.findViewObject("dispensedToUserLOV1");
        voDispensed.clearCache();
        ViewObject voNewContUsers = am.findViewObject("serviceStatisticsLOV1");
        voNewContUsers.clearCache();
        Configuration.releaseRootApplicationModule(am, true);
    }

    public void setGraphName(RichSelectOneChoice graphName) {
        this.graphName = graphName;
    }

    public RichSelectOneChoice getGraphName() {
        return graphName;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public String printCYPAction() {
        //the String passed to the method is the graph component Id,
        //including the naming container Id if the graph is contained //in a naming container like dialog, table etc �
        printDVTComponent(this.getCypGraph().getClientId()); 
        return null; 
        }
    
    public String printDispensedGraph() {
       
        printDVTComponent(this.getDispensedGraph().getClientId()); 
        return null; 
        }
    public String printServiceStatisticsGraphGraph() {
       
        printDVTComponent(this.getServicesStatisticsGraph().getClientId()); 
        return null; 
        }
    
    
    public String exportGraph(){
        String GraphName = "";
        String allornot = "";
        Connection conn = null;
        if(facType.getValue().equals("0")){
            allornot = "_All_Facilities_";
        }else{
            String username = "log5";
            String password = "log5";
            String thinConn = "jdbc:oracle:thin:@10.160.12.5:1521:ora10g";
            try {
                conn = DriverManager.getConnection(thinConn, username, password);
                DriverManager.registerDriver(new OracleDriver());
            } catch (SQLException e) {
                System.out.println(" Connection Failed");
            }
          
        
            String sqlCheck = "select fac_type_id, fac_name from fac_type where fac_type_id="+facType.getValue();
            try {
                PreparedStatement pst = conn.prepareStatement(sqlCheck);
                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    allornot = "_"+rs.getString(2)+"_";
                }else{
                    String sqlGroup = "select (grp_id) as id , grp_desc as name from groups where grp_id="+(Integer.parseInt(facType.getValue()+"")-100);
                    PreparedStatement pst1 = conn.prepareStatement(sqlGroup);
                    ResultSet rs1 = pst1.executeQuery();
                    rs1.next();
                    allornot = "_"+rs1.getString(2)+"_";
                }
            } catch (SQLException e) {
                System.out.println("faaaaaaaaaaaaaaailed");
            }

           
        }
        
        
                    ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
                    ViewObject vo = am.createViewObjectFromQueryStmt("vo1", "select pro_name from product where prod_id="+this.getCprod().getValue());
                    vo.executeQuery();
                    if(vo.hasNext()){
                    Row r = vo.next();
                    allornot+="for_"+r.getAttribute(0)+"_";
                    }
                    // Work with your appmodule and view object here
                    Configuration.releaseRootApplicationModule(am, true);
        ADFContext.getCurrent().getSessionScope().put("ffname", allornot);
        if(getGraphName().getValue().equals("1")){
            printCYPAction();
        }else  if(getGraphName().getValue().equals("2")){
            printDispensedGraph();
        }else  if(getGraphName().getValue().equals("3")){
            printServiceStatisticsGraphGraph();
        }
        ADFContext.getCurrent().getSessionScope().put("pressed",false);
        return null;
        
      
    }
    private void printDVTComponent(String clientId) {
    //find starting component
    FacesContext fctx = FacesContext.getCurrentInstance(); 
    UIViewRoot root = fctx.getViewRoot();
    root.invokeOnComponent( fctx,clientId, new DvtContextCallBack());
    }



    public void setDispensedGraph(UIGraph dispensedGraph) {
        this.dispensedGraph = dispensedGraph;
    }

    public UIGraph getDispensedGraph() {
        return dispensedGraph;
    }

    public void setServicesStatisticsGraph(UIGraph servicesStatisticsGraph) {
        this.servicesStatisticsGraph = servicesStatisticsGraph;
    }

    public UIGraph getServicesStatisticsGraph() {
        return servicesStatisticsGraph;
    }

    public void setCypGraph(UIGraph cypGraph) {
        this.cypGraph = cypGraph;
    }

    public UIGraph getCypGraph() {
        return cypGraph;
    }
}
