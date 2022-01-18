package ui;

import java.io.UnsupportedEncodingException;

import java.net.InetAddress;
import java.net.URLEncoder;

import java.net.UnknownHostException;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.management.AttributeNotFoundException;

import javax.management.InstanceNotFoundException;

import javax.management.MBeanException;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import moh.logistics.lib.common.UIDSConnection;

import moh.logistics.lib.reports.MainInterface;

import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.RichPopup;

import oracle.adf.view.rich.component.rich.nav.RichButton;

import oracle.ui.pattern.dynamicShell.TabContext;
import oracle.ui.pattern.dynamicShell.TabContext.TabOverflowException;

import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;


import org.apache.myfaces.trinidad.util.Service;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;

public class Launcher implements MainInterface {
  
   
  
   String openRep = "";
    private RichPopup pmain;
    private RichPopup psub;
    private RichButton cb2;

    public void setOpenRep(String openRep) {
        this.openRep = openRep;
    }

    public String getOpenRep() {
        InitialContext ctx;
               String base="";
               
               try {
               
               

               ctx = new InitialContext();
               MBeanServer mBeanServer = (MBeanServer) ctx.lookup("java:comp/env/jmx/runtime");

               ObjectName rt;
               rt = (ObjectName) mBeanServer.getAttribute(new ObjectName(
                            "com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean"), "ServerRuntime");
               String listenAddress = (String) mBeanServer.getAttribute(rt, "ListenAddress");
                  String server = listenAddress.substring(0, listenAddress.indexOf("/"));
                  int port = (Integer)mBeanServer.getAttribute(rt, "ListenPort"); 
               System.out.println("weblogic port is "+port+" weblogic address "+server);

            //   base = "http://"+server+":"+port+"/rep/main.jsp";
            //base = "http://apps.moh.gov.jo/"+port+"/rep/main.jsp";
            //base = InetAddress.getLocalHost().getAddress()+":"+port+"/rep/main.jsp";
                
            base = url+"/rep/main.jsp";
               
          // base = "../rep/main.jsp";
                 //base =  URLEncoder.encode(base, "UTF-8");
               } catch (NamingException e) {
               } catch (MBeanException e) {
               } catch (AttributeNotFoundException e) {
               } catch (InstanceNotFoundException e) {
               } catch (ReflectionException e) {
               } catch (MalformedObjectNameException e){
               
               } 

        return base;
       
    }

    public void launchOneTF(ActionEvent actionEvent){
      _launchActivity("�������","/WEB-INF/OneTF.xml#OneTF",false);
//    TabContext.getCurrentInstance().removeTab(TabContext.getCurrentInstance().getSelectedTabIndex());
//    _launchActivity("One","/WEB-INF/OneTF.xml#OneTF",false);
   //  _launchActivity("����� �������","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
  }

  public void launchTwoTF(ActionEvent actionEvent){
      _launchActivity("Two","/WEB-INF/TwoTF.xml#TwoTF",false);
//    TabContext.getCurrentInstance().removeTab(TabContext.getCurrentInstance().getSelectedTabIndex());
//    _launchActivity("Two","/WEB-INF/TwoTF.xml#TwoTF",false);
   //  _launchActivity("����� �������","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
  }
  
  public void launchThreeTF(ActionEvent actionEvent){
      _launchActivity("FacTypeTF","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
//    TabContext.getCurrentInstance().removeTab(TabContext.getCurrentInstance().getSelectedTabIndex());
//    _launchActivity("Murabaha Search","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
   //  _launchActivity("����� �������","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
  }
  
    public void launchReportGraphsTF(ActionEvent actionEvent){
        _launchActivity("FacTypeTF","/WEB-INF/reportGraphsTF.xml#reportGraphsTF",false);
  
    }
  
  public void launchFacTypeTF(ActionEvent actionEvent){
      _launchActivity("Facility Type","/WEB-INF/FacTypeTF.xml#FacTypeTF",false);
  //    TabContext.getCurrentInstance().removeTab(TabContext.getCurrentInstance().getSelectedTabIndex());
  //    _launchActivity("Murabaha Search","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
   //  _launchActivity("����� �������","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
  }  
   
   
  public void launchFacilityTF(ActionEvent actionEvent){
      _launchActivity("Facility","/WEB-INF/FacilityTF.xml#FacilityTF",false);
  //    TabContext.getCurrentInstance().removeTab(TabContext.getCurrentInstance().getSelectedTabIndex());
  //    _launchActivity("Murabaha Search","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
   //  _launchActivity("����� �������","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
  }   
  
  public void launchAdjTypeTF(ActionEvent actionEvent){
      _launchActivity("Adjustment Type","/WEB-INF/AdjTypeTF.xml#AdjTypeTF",false);
  //    TabContext.getCurrentInstance().removeTab(TabContext.getCurrentInstance().getSelectedTabIndex());
  //    _launchActivity("Murabaha Search","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
   //  _launchActivity("����� �������","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
  }   
  
  public void launchProductTF(ActionEvent actionEvent){
      _launchActivity("Product","/WEB-INF/ProductTF.xml#ProductTF",false);
  //    TabContext.getCurrentInstance().removeTab(TabContext.getCurrentInstance().getSelectedTabIndex());
  //    _launchActivity("Murabaha Search","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
   //  _launchActivity("����� �������","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
  }   
  
    public void launchApproveTF(ActionEvent actionEvent){
        _launchActivity("Approve","/WEB-INF/ApproveTF.xml#ApproveTF",false);
    //    TabContext.getCurrentInstance().removeTab(TabContext.getCurrentInstance().getSelectedTabIndex());
    //    _launchActivity("Murabaha Search","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
     //  _launchActivity("����� �������","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
    }  
  
    public void launchInitiateMonthsTF(ActionEvent actionEvent){
        _launchActivity("Initiate Month","/WEB-INF/InitiateMonthTF.xml#InitiateMonthTF",false);
    //    TabContext.getCurrentInstance().removeTab(TabContext.getCurrentInstance().getSelectedTabIndex());
    //    _launchActivity("Murabaha Search","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
     //  _launchActivity("����� �������","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
    }  
    
    public void launchQueryTF(ActionEvent actionEvent){
        _launchActivity("Query","/WEB-INF/QueryTF.xml#QueryTF",false);
    //    TabContext.getCurrentInstance().removeTab(TabContext.getCurrentInstance().getSelectedTabIndex());
    //    _launchActivity("Murabaha Search","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
     //  _launchActivity("����� �������","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
    }  
 
    public void launchGroupsTF(ActionEvent actionEvent){
        _launchActivity("Groups","/WEB-INF/GroupsTF.xml#GroupsTF",false);
    //    TabContext.getCurrentInstance().removeTab(TabContext.getCurrentInstance().getSelectedTabIndex());
    //    _launchActivity("Murabaha Search","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
     //  _launchActivity("����� �������","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
    }  
    
    public void launchGofTF(ActionEvent actionEvent){
        _launchActivity("Group Of Facilities","/WEB-INF/GofTF.xml#GofTF",false);
    //    TabContext.getCurrentInstance().removeTab(TabContext.getCurrentInstance().getSelectedTabIndex());
    //    _launchActivity("Murabaha Search","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
     //  _launchActivity("����� �������","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
    }  
    public void launchGofTF1(ActionEvent actionEvent){
        _launchActivity("Group Of Facilities","/WEB-INF/groupsTypes.xml#groupsTypes",false);
    //    TabContext.getCurrentInstance().removeTab(TabContext.getCurrentInstance().getSelectedTabIndex());
    //    _launchActivity("Murabaha Search","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
     //  _launchActivity("����� �������","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
    }  
        
//  public void launchFinanceQueryTF(ActionEvent actionEvent){
//      _launchActivity("ProductTF Search","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
//    TabContext.getCurrentInstance().removeTab(TabContext.getCurrentInstance().getSelectedTabIndex());
//    _launchActivity("Murabaha Search","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
//   //  _launchActivity("����� �������","/WEB-INF/FinanceQueryTF.xml#FinanceQueryTF",false);
//  }
//
//  
//  public void launchRequestTF(ActionEvent actionEvent){
//      _launchActivity("Request","/WEB-INF/RequestTF.xml#RequestTF",false);
//    TabContext.getCurrentInstance().removeTab(TabContext.getCurrentInstance().getSelectedTabIndex());
//    _launchActivity("Request","/WEB-INF/RequestTF.xml#RequestTF",false);
//  }
//  
//  
//  public void launchPartyDocumentsTF(ActionEvent actionEvent){
//      _launchActivity("Documents","/WEB-INF/PartyDocumentsTF.xml#PartyDocumentsTF",false);
//  }
//  
//  public void launchPartyAddressesTF(ActionEvent actionEvent){
//      _launchActivity("Addresses","/WEB-INF/PartyAddressesTF.xml#PartyAddressesTF",false);
//  }
//  
//  public void launchPartyWorkInfoTF(ActionEvent actionEvent){
//      _launchActivity("Work Places","/WEB-INF/PartyWorkInfoTF.xml#PartyWorkInfoTF",false);
//  }
//  
//  public void launchKafeelTF(ActionEvent actionEvent){
//      _launchActivity("Kafeels","/WEB-INF/KafeelTF.xml#KafeelTF",false);
//  }
//  public void launchPACTF(ActionEvent actionEvent){
//    _launchActivity("PAC","/WEB-INF/PACTF.xml#PACTF",false);
//  }
//  
//  public void launchFinanceFollowupReceiveTF(ActionEvent actionEvent){
//    _launchActivity("Inbox","/WEB-INF/FinanceFollowupReceiveTF.xml#FinanceFollowupReceiveTF",false);
//  }
  
  
  
  
  public void _launchActivity(String title,String taskflowId,boolean newTab){
      try {
          if (newTab) {
              TabContext.getCurrentInstance().addTab(title, taskflowId);
          } else {
              TabContext.getCurrentInstance().addOrSelectTab(title, taskflowId);
          }
      } catch (TabOverflowException toe) {
          toe.handleDefault();
      }
  }


    public void setPmain(RichPopup pmain) {
        this.pmain = pmain;
    }

    public RichPopup getPmain() {
       
        return pmain;
    }

    public void pMainHide(ActionEvent actionEvent) {
        pmain.hide();
    }

    public void setPsub(RichPopup psub) {
        this.psub = psub;
    }

    public RichPopup getPsub() {
        
        return psub;
    }

    public String pSubHide() {
      psub.hide();
        return null;
    }

    public void setCb2(RichButton cb2) {
        this.cb2 = cb2;
    }

    public RichButton getCb2() {
        return cb2;
    }

    public String print() {
       // public String print() {
                // Add event code here...
                FacesContext facesContext = FacesContext.getCurrentInstance();

                       org.apache.myfaces.trinidad.render.ExtendedRenderKitService service =
                           org.apache.myfaces.trinidad.util.Service.getRenderKitService(facesContext, ExtendedRenderKitService.class);

                       service.addScript(facesContext, "window.print();");
                return null;
          //  }
    }
    
    
    
    public String printReport() {
        InitialContext ctx;
        try{
               ctx = new InitialContext();
               MBeanServer mBeanServer = (MBeanServer) ctx.lookup("java:comp/env/jmx/runtime");

               ObjectName rt;
               rt = (ObjectName) mBeanServer.getAttribute(new ObjectName(
                            "com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean"), "ServerRuntime");
               String listenAddress = (String) mBeanServer.getAttribute(rt, "ListenAddress");
                  String server = listenAddress.substring(0, listenAddress.indexOf("/"));
                  int port = (Integer)mBeanServer.getAttribute(rt, "ListenPort"); 
       // url = "http://"+url+":"+port;
           }catch(Exception e){
               e.printStackTrace();
           }
        FacesContext fctx = FacesContext.getCurrentInstance();
        
        ExtendedRenderKitService erks = Service.getRenderKitService(fctx, ExtendedRenderKitService.class);
        StringBuilder script = new StringBuilder();
        script.append("window.open(\"" + this.url+"/rep/printedItems.jsp?main=" +ADFContext.getCurrent().getPageFlowScope().get("mainId")+ "\");");
        erks.addScript(fctx, script.toString());
                return null;
          //  }
    }
}
