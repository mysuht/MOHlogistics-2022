<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1256"
         import="moh.logistics.lib.reports.code.LogisticsReportsClass, moh.logistics.lib.reports.code.OverStockFacility, java.util.*,  java.sql.*, java.text.*"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1256"/>
        <link href="resources/css/logCss.css" rel="stylesheet" type="text/css"/>
        <style type="text/css" media="print">
@page {
                    size: Portrait;
                }

                BODY {
                    width: 90%;
                    /* 
   -webkit-transform: rotate(-90deg) scale(.68,.68); 
  -moz-transform: scale(.90,.90); */

                    zoom: 90;
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
    LogisticsReportsClass db = new LogisticsReportsClass();
  
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
                Over Stocked Facilities 
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
    ResultSet rs12 = db.getFacTypesMainName(type);
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
                <%=  db.getDate() %>
                 
                <br/>
                 Run Time: 
                <%= db.getTime() %>
            </div>
        </div>
        <br/><br/><br/><br/>
         
   
         
        <%
        OverStockFacility osr = new OverStockFacility();
        
if(true){ //all

int i=0;
ResultSet rsProducts = db.getProducts();
for(int p=0; p<products.length; p++){

ResultSet rsDir = db.getDirectorates();

List<Map<String, Object>> facilityList = osr.AllFacilities(products[p],  mon,  year,  dat,  fY,
                                                           tY,  fM,  tM,  type,  hq,
                                                           dir);
int rloop = 0;
//while(rsDir.next()){
for(Map<String, Object> facility : facilityList){
String facilityId = facility.get("FACILITY_ID") + "";
String facilityName = facility.get("FacilityName") + "";
String facilityType = facility.get("FacilityType") + "";
String lvl = facility.get("lvl").toString();
double mos = Double.valueOf(OverStockFacility.mos(facilityId, mon, year, products[p], dat, fY, tY, fM, tM, type, hq));

boolean condition = (lvl.equals("3") && mos > 2) || (lvl.equals("2") && mos > 6);

String code = facility.get("Code") + "";
String contact = facility.get("Contact") + "";
String phone = facility.get("Phone") + "";

if( ! condition )
continue;
int pC = 0;

double closeBalance = Double.valueOf(OverStockFacility.closeBal(facilityId, mon, year, products[p], dat, fY, tY, fM, tM, type, hq));         
double avg = Double.valueOf(OverStockFacility.avgMnthlyCons(facilityId, mon, year, products[p], dat, fY, tY, fM, tM, type, hq));

  pC = 0;
 
 %>

         
        <%
// rs = db.getOverStockedFacilities(mon, year, rsDir.getString(1), products[p], dat,fY,tY, fM, tM,type, hq);
//while(rs.next()) {

 
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
            <%= OverStockFacility.getDirName(facilityId) %>
             &nbsp; &nbsp; 
            <span style="color:pink">
                <%= OverStockFacility.getDirCode(facilityId) %></span>
        </div>
        <br/>
        <div style="text-align:left; clear:both; font-weight:bold">
            Product: 
            <%= OverStockFacility.getProdName(products[p]) %>
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
//} // while Loop Facilities
%>
         
        <%
} // Products Looping
}
}
%>
         
        <%
db.s();
%>
    </body>
</html>