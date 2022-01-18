<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.BelowEmergencyOrderPoint, moh.logistics.lib.reports.code.FacilityTemplate, java.util.*,  java.sql.*, java.text.*"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css" />
         <style  type="text/css" media="print">
        
    @page {
size: Portrait;



} 
  BODY {
   
    width:90%;
   
   
  /* 
   -webkit-transform: rotate(-90deg) scale(.68,.68); 
  -moz-transform: scale(.90,.90); */
  
    zoom:90 ;
   -moz-transform: scale(90%);
    -ms-transform: scale(90%);
}

        
        
 
    </style>
    </head>
    
    <body>
        <%
    String dat = request.getParameter("dat");
    String mon = "";
    String fY = "";
    String tY = "";
    String fM = "";
    String tM = "";
    String type = request.getParameter("p_dir");
String hq = "";
    if(dat.equals("m")){
     mon = request.getParameter("p_mon");
     }if(dat.equals("q")){
      mon = request.getParameter("p_quart");
     }if(dat.equals("u")){
    fY = request.getParameter("p_fyear");
    tY = request.getParameter("p_tyear");
    fM = request.getParameter("p_fmon");
    tM= request.getParameter("p_tmon");
    }if(dat.equals("hq")){
    hq = request.getParameter("p_hq");
    }
    String year = request.getParameter("p_year");
    String dir[] = request.getParameterValues("p_cen");
    String products[] = request.getParameterValues("p_prod");
  
      DecimalFormat df1 = new DecimalFormat("###,###.##");
      DecimalFormat df = new DecimalFormat("###,###.##");
    %>
        <div class="main_div">
            <div class="dLeft">
                Jordan Contraceptive Logistics System 
                <br/>
                 MOH Familiy Planning Program
            </div>
            <div class="dCenter">
                Below Emergency Order Point 
                <br/>
                 Report Period: 
                <%
    if(mon.equals("0")){
    out.println("Annual ,"+year+"<br/>");
    }else{
    if(dat.equals("m")){
    out.println(mon+", "+year+"<br/>" );
    
    }if(dat.equals("q")){
    out.println(mon+" Quarter, "+year+"<br/>" );
    
    }
    if(dat.equals("hq")){
    out.println(hq+"Half Year, "+year+"<br/>" );
    
    }
    
    if(dat.equals("u")){
    out.println(fM+"/"+fY+"   -   "+tM+"/"+tY +"<br/>");
    
    }
    
    }
    
    
    %>
                 
                <%
    ResultSet rs12 = BelowEmergencyOrderPoint.getFacTypesMainName(type);
    rs12.next();
    
    if(type.equals("0")){
    out.println("All Facility Types.");
    }else{
    out.println(rs12.getString(2));
    }
    %>
                 
                <br/>
            </div>
            <div class="dRight">
                Run Date: 
                <%=  BelowEmergencyOrderPoint.getDate() %>
                 
                <br/>
                 Run Time: 
                <%= BelowEmergencyOrderPoint.getTime() %>
            </div>
        </div>
        <br/><br/><br/><br/>
         
   
         
        <%
        BelowEmergencyOrderPoint osr = new BelowEmergencyOrderPoint();
        
if(true){ //all

int i=0;
ResultSet rsProducts = BelowEmergencyOrderPoint.getProducts();
for(int p=0; p<products.length; p++){

ResultSet rsDir = BelowEmergencyOrderPoint.getDirectorates();

List<FacilityTemplate> facilityList = osr.AllFacilitiesXXX(products[p],  mon,  year,  dat,  fY,
                                                           tY,  fM,  tM,  type,  hq,
                                                           dir);
int rloop = 0;
//while(rsDir.next()){
for(FacilityTemplate facility : facilityList){
String facilityId = facility.getFacility().getFacilityId() + "";
String facilityName = facility.getFacility().getFacilityName() + "";
String facilityType = facility.getFacilityType() + "";
String lvl = facility.getFacility().getFacilityTypeHierarchyId();
double mos = facility.getMonthOfSupplier();

boolean condition = (lvl.equals("3") && mos < 0.55 ) || (lvl.equals("2") && mos <= 1.0);

String code = facility.getFacility().getFacCode() + "";
String contact = facility.getFacility().getContact() + "";
String phone = facility.getFacility().getPhone() + "";

if( ! condition )
continue;
int pC = 0;

double closeBalance = Double.valueOf(facility.getClosingBal());         
double avg = Double.valueOf(facility.getAvg());

  pC = 0;
 
 %>

         
        <%


 
%>
         
        <%if( (rloop%4 ==0 && rloop != 0 )  ){
 %>
        <div class="ba" style="clear:both"></div>
        <br/>
         
        <br/><br/><br/><br/>
         
        <%}rloop++;%>
         
        <br/>
        <div align="left" style="text-align:left;clear:both;font-weight:bold">
            Supplier: 
            <%= BelowEmergencyOrderPoint.getDirName(facilityId) %>
             &nbsp; &nbsp; 
            <span style="color:pink">
                <%= BelowEmergencyOrderPoint.getDirCode(facilityId) %></span>
        </div>
        <br/>
        <div style="text-align:left; clear:both; font-weight:bold">
            Product: 
            <%= BelowEmergencyOrderPoint.getProdName(products[p]) %>
        </div>
        <br/>
        <div style=" width:600pt;clear:both; margin:10pt;">
            <div style="float:left; width:400pt;text-align:left;">
                <span style="background-color:yellow">Code: </span>
                 
                <%= code %>
                 
                <br/>
                 
                <span style="background-color:yellow"> Name: </span><%= facilityName  %>
                 
                <br/>
                 
                <span style="background-color:yellow"> Contact: </span><%= contact  %>
                 
                <br/>
                 
                <span style="background-color:yellow; font-weight:bold"> Closing Bal.: </span>
                 
                <%= df.format(closeBalance) %>
            </div>
            <div style="float:left; width:200pt;text-align:left;">
                <span style="background-color:yellow">Type: </span>
                 
                <%= facilityType %>
                 
                <br/>
                 
                <span style="background-color:yellow"> Phone: </span>
                 
                <%= phone %>
                 
                <br/>
                 
                <span style="background-color:yellow"> Avg_Monthly_Cons.: </span>
                 
                <%= avg %>
                 
                <br/>
                 
                <span style="background-color:yellow"> Months Of Supply.: </span>
                 
                <%= mos %>
                 
                <br/>
            </div>
        </div>
        <div style="clear:both"></div>
        <hr/>
        <div style="height:10pt;"></div>
        <br/>
         
        <%
i++;
%>
         
        <%
} // Products Looping
}
}
%>

    </body>
</html>