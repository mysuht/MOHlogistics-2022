package ui;



import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;

import javax.faces.context.FacesContext;

import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

import oracle.jbo.*;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.Number;

public class ActiveMonthsClass {
    private RichSelectOneChoice monthList;
    private RichSelectOneChoice yearList;
    public static void commitAndShowChanges(ApplicationModule appMod) {
       // Helper method to print updated table.

       try {
         appMod.getTransaction().commit();
       } 
       catch (Exception e) {
         System.out.println("\n Could not commit changes.");
         System.exit(0);

       }
       // Define and execute a simple SQL statement.
       String sqlStr = "select 1 from dual ";
       String dumpClass = "oracle.jbo.server.QueryDumpTab";
       String result = appMod.getTransaction().dumpQueryResult(sqlStr, dumpClass, null);

       // Print updated table.
       System.out.println(result);
     }

    public String saveAcitveMonth() {
        String month =  ""+monthList.getValue();
        String year = ""+yearList.getValue();
        String facid = "";
        System.out.println("month ====== "+month +" year============="+year);
        
        String amDef = "model.AM.LOGAM";
        String config = "LOGAMLocal";
        ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
        ViewObject activeMonthVO = am.findViewObject("ActiveMonthsVO");
        activeMonthVO.setWhereClause(" AM_YEAR = " + year + " and AM_MONTH = " + month);
        // Delete row(s).
            activeMonthVO.executeQuery();
            while (activeMonthVO.hasNext()) {
              activeMonthVO.next();
              activeMonthVO.removeCurrentRow();
            }
            // Call a helper method.
            commitAndShowChanges(am);
 
        // Add event code here...
        
        FacesContext fc = FacesContext.getCurrentInstance();
              ELContext elc = fc.getELContext();
              ExpressionFactory ef = fc.getApplication().getExpressionFactory();
                MethodExpression me = ef.createMethodExpression(elc, "#{bindings.Commit.execute}", Object.class, new Class[0]);
                me.invoke(elc, new Object[0]);
        
        
     
        
        ViewObject mainvo = am.findViewObject("CtfMainVO");
        
        ViewObject vo = am.findViewObject("FacilityIter1");
        while(vo.hasNext()){
            Row r = vo.next();
            facid = ""+r.getAttribute(13);
            
            Row mainr = mainvo.createRow();
            mainr.setAttribute(1, facid);
            mainr.setAttribute(2, year+"-"+month+"-01");
            mainvo.insertRow(mainr);            
            
        }
        
        am.getTransaction().commit();
        
        
        // Work with your appmodule and view object here
        Configuration.releaseRootApplicationModule(am, true);
        
        
        
        
        return "goBack";
    }

    public void setMonthList(RichSelectOneChoice monthList) {
        this.monthList = monthList;
    }

    public RichSelectOneChoice getMonthList() {
        return monthList;
    }

    public void setYearList(RichSelectOneChoice yearList) {
        this.yearList = yearList;
    }

    public RichSelectOneChoice getYearList() {
        return yearList;
    }
}
 
