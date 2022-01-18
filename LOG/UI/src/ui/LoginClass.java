package ui;

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;

import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.faces.event.ActionEvent;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;

import oracle.jbo.*;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.*;
import oracle.jbo.domain.Number;

public class LoginClass {
    private RichInputText userName;
    private RichInputText password;
    private RichOutputText msg;
    private RichTable filterDateMainBrowse;
    private RichInputText dateCtfMain;

    public void setUserName(RichInputText userName) {
        this.userName = userName;
    }

    public RichInputText getUserName() {
        return userName;
    }

    public void setPassword(RichInputText password) {
        this.password = password;
    }

    public RichInputText getPassword() {
        return password;
    }

    public String checkUser() {
        // Add event code here...
        
        FacesContext fc = FacesContext.getCurrentInstance();  
        Application app = fc.getApplication();  
        ExpressionFactory ef = app.getExpressionFactory();  
        ELContext elc = fc.getELContext();  
        String amDef = "model.AM.LOGAM";
        String config = "LOGAMLocal";
        ApplicationModule am = Configuration.createRootApplicationModule(amDef, config);
        ViewObject vo = am.findViewObject("WebUsersVO");
        vo.clearCache();
        vo.setWhereClause("usr_name='"+userName.getValue()+"' and usr_password = '"+password.getValue()+"' and usr_form='LOG'");
        if(vo.hasNext()){
        Row r = vo.next();
        String type = ""+r.getAttribute("UsrType");
        ValueExpression v1 = ef.createValueExpression(elc, "#{sessionScope.sType}", Object.class);
        if(type.equals("C")){
            v1.setValue(elc,false);
            Configuration.releaseRootApplicationModule(am, true);
            return "second";
        }else{
            v1.setValue(elc,true);
            Configuration.releaseRootApplicationModule(am, true);
            return "success";
        }
        } else {
            Configuration.releaseRootApplicationModule(am, true);
            msg.setValue("Invalid User Name or Password");
            return "fail";
        }

    }

    public void setMsg(RichOutputText msg) {
        this.msg = msg;
    }

    public RichOutputText getMsg() {
        return msg;
    }

    public void setFilterDateMainBrowse(RichTable filterDateMainBrowse) {
        this.filterDateMainBrowse = filterDateMainBrowse;
    }

    public RichTable getFilterDateMainBrowse() {
        return filterDateMainBrowse;
    }

    public void searchDate(ActionEvent actionEvent) {
     
                  if( this.getDateCtfMain().getValue()+"" !=null){

                    try {
                           RichTable t1 = this.getFilterDateMainBrowse();
                             FilterableQueryDescriptor filterQD =  (FilterableQueryDescriptor) t1.getFilterModel();  
                               Map filterCriteria = filterQD.getFilterCriteria();
                               //---------------
                             DateFormat formatter;
                                       java.util.Date date;

                      formatter = new SimpleDateFormat("MM/yyyy");
                      date = formatter.parse( this.getDateCtfMain().getValue()+"");
                      java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                      oracle.jbo.domain.Date jboDate = new oracle.jbo.domain.Date(sqlDate);

                       // return jboDate;
                        System.out.println(jboDate);
                       filterCriteria.put("PDate", jboDate);      
                       getFilterDateMainBrowse().queueEvent(new QueryEvent(getFilterDateMainBrowse(), filterQD));
                       AdfFacesContext.getCurrentInstance().addPartialTarget(this.getFilterDateMainBrowse());
                       } catch (ParseException e) {

                                e.printStackTrace();
                        }

                 }// -
              
    }

    public void setDateCtfMain(RichInputText dateCtfMain) {
        this.dateCtfMain = dateCtfMain;
    }

    public RichInputText getDateCtfMain() {
        return dateCtfMain;
    }

   
        public String getOut() {
          ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
          HttpServletResponse response = (HttpServletResponse) ectx.getResponse();
          HttpSession session = (HttpSession) ectx.getSession(false);
          session.invalidate();
          try {
                response.sendRedirect("/logistics/faces/Login");
           } catch (Exception e) {
                // TODO: Add catch code
                e.printStackTrace();
            }
          
            return null;
        }

}
